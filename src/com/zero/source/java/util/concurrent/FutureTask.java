/*
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

/*
 *
 *
 *
 *
 *
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */

package java.util.concurrent;

import java.util.concurrent.locks.LockSupport;

/**
 * A cancellable asynchronous computation.  This class provides a base
 * implementation of {@link Future}, with methods to start and cancel
 * a computation, query to see if the computation is complete, and
 * retrieve the result of the computation.  The result can only be
 * retrieved when the computation has completed; the {@code get}
 * methods will block if the computation has not yet completed.  Once
 * the computation has completed, the computation cannot be restarted
 * or cancelled (unless the computation is invoked using
 * {@link #runAndReset}).
 *
 * <p>A {@code FutureTask} can be used to wrap a {@link Callable} or
 * {@link Runnable} object.  Because {@code FutureTask} implements
 * {@code Runnable}, a {@code FutureTask} can be submitted to an
 * {@link Executor} for execution.
 *
 * <p>In addition to serving as a standalone class, this class provides
 * {@code protected} functionality that may be useful when creating
 * customized task classes.
 *
 * @param <V> The result type returned by this FutureTask's {@code get} methods
 * @author Doug Lea
 * @since 1.5
 */
// FutureTask表示一类将来会被完成的异步任务，这类任务带有返回值，且中途可被取消。
public class FutureTask<V> implements RunnableFuture<V> {
    /*
     * Revision notes: This differs from previous versions of this
     * class that relied on AbstractQueuedSynchronizer, mainly to
     * avoid surprising users about retaining interrupt status during
     * cancellation races. Sync control in the current design relies
     * on a "state" field updated via CAS to track completion, along
     * with a simple Treiber stack to hold waiting threads.
     *
     * Style note: As usual, we bypass overhead of using
     * AtomicXFieldUpdaters and instead directly use Unsafe intrinsics.
     */

    private static final int NEW = 0; // 新建任务
    private static final int COMPLETING = 1; // 任务已结束
    private static final int NORMAL = 2; // 任务正常结束，并设置了返回值
    private static final int EXCEPTIONAL = 3; // 任务异常结束，并设置了异常信息
    private static final int CANCELLED = 4; // 任务已取消
    private static final int INTERRUPTING = 5; // 任务正在中断
    private static final int INTERRUPTED = 6; // 任务已中断
    // Unsafe mechanics
    private static final sun.misc.Unsafe UNSAFE;
    private static final long stateOffset;
    private static final long runnerOffset;
    private static final long waitersOffset;

    static {
        try {
            UNSAFE = sun.misc.Unsafe.getUnsafe();
            Class<?> k = FutureTask.class;
            stateOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("state"));
            runnerOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("runner"));
            waitersOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("waiters"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    /**
     * The run state of this task, initially NEW.  The run state
     * transitions to a terminal state only in methods set,
     * setException, and cancel.  During completion, state may take on
     * transient values of COMPLETING (while outcome is being set) or
     * INTERRUPTING (only while interrupting the runner to satisfy a
     * cancel(true)). Transitions from these intermediate to final
     * states use cheaper ordered/lazy writes because values are unique
     * and cannot be further modified.
     * <p>
     * Possible state transitions:
     * NEW -> COMPLETING -> NORMAL
     * NEW -> COMPLETING -> EXCEPTIONAL
     * NEW -> CANCELLED
     * NEW -> INTERRUPTING -> INTERRUPTED
     */
    // 任务状态
    private volatile int state;
    /**
     * The underlying callable; nulled out after running
     */
    // 待执行任务（可由Runnable型任务包装而来）
    private Callable<V> callable;
    /**
     * The result to return or exception to throw from get()
     */
    // 保存正常结束时的计算结果，或者保存异常结束时的异常对象
    private Object outcome; // non-volatile, protected by state reads/writes
    /**
     * The thread running the callable; CASed during run()
     */
    // 正在执行此任务的线程
    private volatile Thread runner;
    /**
     * Treiber stack of waiting threads
     */
    // 等待栈的栈顶游标，等待栈用于存储那些正在等待计算结果的线程
    private volatile WaitNode waiters;

    /**
     * Creates a {@code FutureTask} that will, upon running, execute the
     * given {@code Callable}.
     *
     * @param callable the callable task
     * @throws NullPointerException if the callable is null
     */
    //指定callable的构造器
    public FutureTask(Callable<V> callable) {
        if (callable == null)
            throw new NullPointerException();
        this.callable = callable;
        this.state = NEW;       // ensure visibility of callable
    }

    /**
     * Creates a {@code FutureTask} that will, upon running, execute the
     * given {@code Runnable}, and arrange that {@code get} will return the
     * given result on successful completion.
     *
     * @param runnable the runnable task
     * @param result   the result to return on successful completion. If
     *                 you don't need a particular result, consider using
     *                 constructions of the form:
     *                 {@code Future<?> f = new FutureTask<Void>(runnable, null)}
     * @throws NullPointerException if the runnable is null
     */
    // 指定runable和result的构造器
    public FutureTask(Runnable runnable, V result) {
        this.callable = Executors.callable(runnable, result);
        this.state = NEW;       // ensure visibility of callable
    }

    /**
     * Returns result or throws exception for completed task.
     *
     * @param s completed state value
     */
    // 报告任务状态，返回任务的计算结果（可能会抛出异常）
    @SuppressWarnings("unchecked")
    private V report(int s) throws ExecutionException {
        Object x = outcome;
        if (s == NORMAL)
            return (V) x;
        if (s >= CANCELLED)
            throw new CancellationException();
        throw new ExecutionException((Throwable) x);
    }
    // 任务是否被中止（取消或被中断）
    public boolean isCancelled() {
        return state >= CANCELLED;
    }
    // 任务是否结束（包装正常结束，异常结束，或中止）
    public boolean isDone() {
        return state != NEW;
    }
    // 中止异步任务，包括取消或中断
    // mayInterruptIfRunning指示是否可在任务执行期间中断线程
    public boolean cancel(boolean mayInterruptIfRunning) {
        // 决定使用中断标记还是取消标记
        int status = mayInterruptIfRunning ? INTERRUPTING : CANCELLED;
        // 尝试更新任务状态：NEW-->INTERRUPTING/CANCELLED，如果更新失败，则直接返回
        if (!(state == NEW &&
                UNSAFE.compareAndSwapInt(this, stateOffset, NEW, status)))
            return false;
        try {    // in case call to interrupt throws exception
            // 如果需要中断线程
            if (mayInterruptIfRunning) {
                try {
                    Thread t = runner;
                    if (t != null)
                        // 中断线程（只是给线程预设一个标记，不是立即让线程停下来）
                        t.interrupt();
                } finally { // final state
                    UNSAFE.putOrderedInt(this, stateOffset, INTERRUPTED);
                }
            }
        } finally {
            // 任务结束后，唤醒所有等待结果的线程
            finishCompletion();
        }
        return true;
    }

    /**
     * @throws CancellationException {@inheritDoc}
     */
    // 获取任务计算结果，任务未结束时会一直阻塞
    public V get() throws InterruptedException, ExecutionException {
        int s = state;
        // 如果任务未结束
        if (s <= COMPLETING){
            // 获取任务的状态标记，任务未结束时会一直阻塞
            s = awaitDone(false, 0L);
        }
        // 报告任务状态，返回任务的计算结果
        return report(s);
    }

    /**
     * @throws CancellationException {@inheritDoc}
     */
    // 获取任务计算结果，任务未结束时会阻塞指定的一段时间
    public V get(long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        if (unit == null)
            throw new NullPointerException();
        int s = state;
        // 如果任务未结束
        if(s<=COMPLETING) {
            // 获取任务的状态标记，任务未结束时会阻塞指定的一段时间
            s = awaitDone(true, unit.toNanos(timeout));

            if(s<=COMPLETING){
                throw new TimeoutException();
            }
        }
        // 报告任务状态，返回任务的计算结果
        return report(s);
    }

    /**
     * Protected method invoked when this task transitions to state
     * {@code isDone} (whether normally or via cancellation). The
     * default implementation does nothing.  Subclasses may override
     * this method to invoke completion callbacks or perform
     * bookkeeping. Note that you can query status inside the
     * implementation of this method to determine whether this task
     * has been cancelled.
     */
    // 任务结束，且唤醒所有等待结果的线程之后的回调
    protected void done() {
    }

    /**
     * Sets the result of this future to the given value unless
     * this future has already been set or has been cancelled.
     *
     * <p>This method is invoked internally by the {@link #run} method
     * upon successful completion of the computation.
     *
     * @param v the value
     */
    // 设置计算结果，并将任务状态从NEW更新为COMPLETING-->NORMAL
    protected void set(V v) {
        // 尝试更新任务状态：NEW-->COMPLETING
        if (UNSAFE.compareAndSwapInt(this, stateOffset, NEW, COMPLETING)) {
            // 保存计算结果
            outcome = v;
            // 尝试更新任务状态：COMPLETING-->NORMAL
            UNSAFE.putOrderedInt(this, stateOffset, NORMAL); // final state
            // 任务结束后，唤醒所有等待线程
            finishCompletion();
        }
    }

    /**
     * Causes this future to report an {@link ExecutionException}
     * with the given throwable as its cause, unless this future has
     * already been set or has been cancelled.
     *
     * <p>This method is invoked internally by the {@link #run} method
     * upon failure of the computation.
     *
     * @param t the cause of failure
     */
    // 设置异常信息，并将任务状态从NEW更新为COMPLETING-->EXCEPTIONAL
    protected void setException(Throwable t) {
        // 尝试更新任务状态：NEW-->COMPLETING
        if (UNSAFE.compareAndSwapInt(this, stateOffset, NEW, COMPLETING)) {
            // 保存异常信息
            outcome = t;
            // 尝试更新任务状态：COMPLETING-->EXCEPTIONAL
            UNSAFE.putOrderedInt(this, stateOffset, EXCEPTIONAL); // final state
            // 任务结束后，唤醒所有等待线程
            finishCompletion();
        }
    }
    // 执行任务，并设置执行结果。如果任务正常结束，则其最终状态为NORMAL
    public void run() {
        // 尝试设置当前线程为任务执行线程，如果设置失败，直接返回
        if (state != NEW ||
                !UNSAFE.compareAndSwapObject(this, runnerOffset,
                        null, Thread.currentThread())){
            return;
        }
        try {
            Callable<V> c = callable;
            if (c != null && state == NEW) {
                V result;
                boolean ran;
                try {
                    // 执行任务，并获取计算结果
                    result = c.call();
                    ran = true;
                } catch (Throwable ex) {
                    result = null;
                    ran = false;
                    // 异常结束，设置异常信息，唤醒所有等待线程
                    setException(ex);
                }
                if (ran){
                    // 正常结束，设置计算结果，唤醒所有等待线程
                    set(result);
                }
            }
        } finally {
            // runner must be non-null until state is settled to
            // prevent concurrent calls to run()
            runner = null;
            // state must be re-read after nulling runner to prevent
            // leaked interrupts
            int s = state;
            // 如果任务所在线程需要被中断
            if (s >= INTERRUPTING)
                handlePossibleCancellationInterrupt(s);
        }
    }

    /**
     * Executes the computation without setting its result, and then
     * resets this future to initial state, failing to do so if the
     * computation encounters an exception or is cancelled.  This is
     * designed for use with tasks that intrinsically execute more
     * than once.
     *
     * @return {@code true} if successfully run and reset
     */
    // 执行任务，不设置执行结果。如果任务正常结束，则其最终状态仍为NEW，这意味着该调用该方法可重复执行任务
    protected boolean runAndReset() {
        // 尝试设置当前线程为任务执行线程，如果设置失败，直接返回
        if (state != NEW ||
                !UNSAFE.compareAndSwapObject(this, runnerOffset,
                        null, Thread.currentThread()))
            return false;
        boolean ran = false;
        int s = state;
        try {
            // 待执行任务（可由Runnable型任务包装而来）
            Callable<V> c = callable;
            if (c != null && s == NEW) {
                try {
                    // 执行任务，但不获取计算结果
                    c.call(); // don't set result
                    ran = true;
                } catch (Throwable ex) {
                    // 异常结束，设置异常信息，唤醒所有等待线程
                    setException(ex);
                }
            }
        } finally {
            // runner must be non-null until state is settled to
            // prevent concurrent calls to run()
            runner = null;
            // state must be re-read after nulling runner to prevent
            // leaked interrupts
            s = state;
            // state must be re-read after nulling runner to prevent leaked interrupts
            if (s >= INTERRUPTING)
                handlePossibleCancellationInterrupt(s);
        }
        // 返回值表示任务是否正常结束
        return ran && s == NEW;
    }

    /**
     * Ensures that any interrupt from a possible cancel(true) is only
     * delivered to a task while in run or runAndReset.
     */
    // 确保中断完成
    private void handlePossibleCancellationInterrupt(int s) {
        // It is possible for our interrupter to stall before getting a
        // chance to interrupt us.  Let's spin-wait patiently.
        // 任务正在中断，则等待中断完成
        if (s == INTERRUPTING)
            // 等待中断完成
            while (state == INTERRUPTING)
                // 当前线程让出CPU时间片，大家重新抢占执行权
                Thread.yield(); // wait out pending interrupt

        // assert state == INTERRUPTED;

        // We want to clear any interrupt we may have received from
        // cancel(true).  However, it is permissible to use interrupts
        // as an independent mechanism for a task to communicate with
        // its caller, and there is no way to clear only the
        // cancellation interrupt.
        //
        // Thread.interrupted();
    }

    /**
     * Removes and signals all waiting threads, invokes done(), and
     * nulls out callable.
     */
    // 任务结束后（不管是正常结束，还是异常结束，或者是被取消），唤醒所有等待线程
    private void finishCompletion() {
        // assert state > COMPLETING;
        for (WaitNode q; (q = waiters) != null; ) {
            // 使waiters==null，因为任务已经结束，不需要继续等待了
            if (UNSAFE.compareAndSwapObject(this, waitersOffset, q, null)) {
                // 遍历等待栈，唤醒所有等待线程
                for (; ; ) {
                    Thread t = q.thread;
                    if (t != null) {
                        q.thread = null;
                        LockSupport.unpark(t);
                    }
                    WaitNode next = q.next;
                    if (next == null){
                        break;
                    }
                    // 帮助回收
                    q.next = null; // unlink to help gc
                    q = next;
                }
                break;
            }
        }

        done();

        callable = null;        // to reduce footprint
    }

    /**
     * Awaits completion or aborts on interrupt or timeout.
     *
     * @param timed true if use timed waits  // true表示时间等待
     * @param nanos time to wait, if timed   // 如果时间等待，表示需要等待的时间
     * @return state upon completion
     */
    // 等待任务完成或被强制中断或超时
    private int awaitDone(boolean timed, long nanos) throws InterruptedException {
        final long deadline = timed ? System.nanoTime() + nanos : 0L;
        WaitNode q = null;
        boolean queued = false;
        for (; ; ) {
            // 判断线程是否被中断
            if (Thread.interrupted()) {
                //  如果线程已被设置为中断，则注销当前等待者，并移除等待栈中所有失效结点
                removeWaiter(q);
                throw new InterruptedException();
            }

            int s = state;
            // 如果任务已结束，并且设置了返回结果或异常信息，那么可以结束等待了
            if (s > COMPLETING) {
                if (q != null){
                    q.thread = null;
                }
                // 返回任务具体的状态
                return s;
            }
            // 如果任务已结束，但是还未设置返回结果或异常信息，则需要等待一会儿
            else if (s == COMPLETING) // cannot time out yet
                // 当前线程让出CPU时间片，大家重新抢占执行权
                Thread.yield();
            // 如果等待结点为空，则新建一个等待结点
            else if (q == null){
                // 新建等待结点，存入当前线程
                q = new WaitNode();
            }
            // 将当前线程加入等待栈，并更新等待栈的栈顶游标
            else if (!queued){
                queued = UNSAFE.compareAndSwapObject(this, waitersOffset, q.next = waiters, q);
            }
            // 如果启用了超时设置
            else if (timed) {
                // 计算时间差值
                nanos = deadline - System.nanoTime();
                // 若时间已过期，则移除任务
                if (nanos <= 0L) {
                    removeWaiter(q);
                    return state;
                }
                // 任务还在运行中，则阻塞线程，进入等待nanos的时间
                LockSupport.parkNanos(this, nanos);
            } else{
                // 任务还在运行中，则阻塞线程，进入等待
                LockSupport.park(this);
            }

        }
    }

    /**
     * Tries to unlink a timed-out or interrupted wait node to avoid
     * accumulating garbage.  Internal nodes are simply unspliced
     * without CAS since it is harmless if they are traversed anyway
     * by releasers.  To avoid effects of unsplicing from already
     * removed nodes, the list is retraversed in case of an apparent
     * race.  This is slow when there are a lot of nodes, but we don't
     * expect lists to be long enough to outweigh higher-overhead
     * schemes.
     */
    // 注销给定的等待者，并移除等待栈中所有失效结点
    private void removeWaiter(WaitNode node) {
        if (node != null) {
            // 置空等待线程
            node.thread = null;
            retry:
            for (; ; ) {          // restart on removeWaiter race
                for (WaitNode pred = null, q = waiters, s; q != null; q = s) {
                    s = q.next;
                    if (q.thread != null)
                        pred = q;
                    else if (pred != null) {
                        // 移除等待栈中失效的等待结点
                        pred.next = s;
                        // 可能触发栈顶游标的更新
                        if (pred.thread == null) // check for race
                            continue retry;

                    }
                    // 等待栈栈顶的等待结点失效（没有等待线程了），则需要更新等待栈的栈顶游标
                    else if (!UNSAFE.compareAndSwapObject(this, waitersOffset,
                            q, s))
                        continue retry;
                }
                break;
            }
        }
    }

    /**
     * Simple linked list nodes to record waiting threads in a Treiber
     * stack.  See other classes such as Phaser and SynchronousQueue
     * for more detailed explanation.
     */
    // 等待结点
    static final class WaitNode {
        // 正在等待计算结果的线程
        volatile Thread thread;
        // 前一个等待结点
        volatile WaitNode next;

        WaitNode() {
            thread = Thread.currentThread();
        }
    }

}
