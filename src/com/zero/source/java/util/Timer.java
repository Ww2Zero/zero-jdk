/*
 * Copyright (c) 1999, 2008, Oracle and/or its affiliates. All rights reserved.
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

package java.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A facility for threads to schedule tasks for future execution in a
 * background thread.  Tasks may be scheduled for one-time execution, or for
 * repeated execution at regular intervals.
 *
 * <p>Corresponding to each <tt>Timer</tt> object is a single background
 * thread that is used to execute all of the timer's tasks, sequentially.
 * Timer tasks should complete quickly.  If a timer task takes excessive time
 * to complete, it "hogs" the timer's task execution thread.  This can, in
 * turn, delay the execution of subsequent tasks, which may "bunch up" and
 * execute in rapid succession when (and if) the offending task finally
 * completes.
 *
 * <p>After the last live reference to a <tt>Timer</tt> object goes away
 * <i>and</i> all outstanding tasks have completed execution, the timer's task
 * execution thread terminates gracefully (and becomes subject to garbage
 * collection).  However, this can take arbitrarily long to occur.  By
 * default, the task execution thread does not run as a <i>daemon thread</i>,
 * so it is capable of keeping an application from terminating.  If a caller
 * wants to terminate a timer's task execution thread rapidly, the caller
 * should invoke the timer's <tt>cancel</tt> method.
 *
 * <p>If the timer's task execution thread terminates unexpectedly, for
 * example, because its <tt>stop</tt> method is invoked, any further
 * attempt to schedule a task on the timer will result in an
 * <tt>IllegalStateException</tt>, as if the timer's <tt>cancel</tt>
 * method had been invoked.
 *
 * <p>This class is thread-safe: multiple threads can share a single
 * <tt>Timer</tt> object without the need for external synchronization.
 *
 * <p>This class does <i>not</i> offer real-time guarantees: it schedules
 * tasks using the <tt>Object.wait(long)</tt> method.
 *
 * <p>Java 5.0 introduced the {@code java.util.concurrent} package and
 * one of the concurrency utilities therein is the {@link
 * java.util.concurrent.ScheduledThreadPoolExecutor
 * ScheduledThreadPoolExecutor} which is a thread pool for repeatedly
 * executing tasks at a given rate or delay.  It is effectively a more
 * versatile replacement for the {@code Timer}/{@code TimerTask}
 * combination, as it allows multiple service threads, accepts various
 * time units, and doesn't require subclassing {@code TimerTask} (just
 * implement {@code Runnable}).  Configuring {@code
 * ScheduledThreadPoolExecutor} with one thread makes it equivalent to
 * {@code Timer}.
 *
 * <p>Implementation note: This class scales to large numbers of concurrently
 * scheduled tasks (thousands should present no problem).  Internally,
 * it uses a binary heap to represent its task queue, so the cost to schedule
 * a task is O(log n), where n is the number of concurrently scheduled tasks.
 *
 * <p>Implementation note: All constructors start a timer thread.
 *
 * @author Josh Bloch
 * @see TimerTask
 * @see Object#wait(long)
 * @since 1.3
 */
//  定时器
public class Timer {
    /**
     * This ID is used to generate thread names.
     */
    private final static AtomicInteger nextSerialNumber = new AtomicInteger(0);
    /**
     * The timer task queue.  This data structure is shared with the timer
     * thread.  The timer produces tasks, via its various schedule calls,
     * and the timer thread consumes, executing timer tasks as appropriate,
     * and removing them from the queue when they're obsolete.
     */
    // 定时器任务队列
    private final TaskQueue queue = new TaskQueue();
    /**
     * The timer thread.
     */
    // 定时器的线程
    private final TimerThread thread = new TimerThread(queue);
    /**
     * This object causes the timer's task execution thread to exit
     * gracefully when there are no live references to the Timer object and no
     * tasks in the timer queue.  It is used in preference to a finalizer on
     * Timer as such a finalizer would be susceptible to a subclass's
     * finalizer forgetting to call it.
     */
    private final Object threadReaper = new Object() {
        protected void finalize() throws Throwable {
            synchronized (queue) {
                // 取消定时器
                thread.newTasksMayBeScheduled = false;
                // 唤醒定时线程
                queue.notify(); // In case queue is empty.
            }
        }
    };

    /**
     * Creates a new timer.  The associated thread does <i>not</i>
     * {@linkplain Thread#setDaemon run as a daemon}.
     */
    public Timer() {
        this("Timer-" + serialNumber());
    }

    /**
     * Creates a new timer whose associated thread may be specified to
     * {@linkplain Thread#setDaemon run as a daemon}.
     * A daemon thread is called for if the timer will be used to
     * schedule repeating "maintenance activities", which must be
     * performed as long as the application is running, but should not
     * prolong the lifetime of the application.
     *
     * @param isDaemon true if the associated thread should run as a daemon.
     */
    public Timer(boolean isDaemon) {
        this("Timer-" + serialNumber(), isDaemon);
    }

    /**
     * Creates a new timer whose associated thread has the specified name.
     * The associated thread does <i>not</i>
     * {@linkplain Thread#setDaemon run as a daemon}.
     *
     * @param name the name of the associated thread
     * @throws NullPointerException if {@code name} is null
     * @since 1.5
     */
    // 指定定时线程的名字的构造方法
    public Timer(String name) {
        thread.setName(name);
        thread.start();
    }

    /**
     * Creates a new timer whose associated thread has the specified name,
     * and may be specified to
     * {@linkplain Thread#setDaemon run as a daemon}.
     *
     * @param name     the name of the associated thread
     * @param isDaemon true if the associated thread should run as a daemon
     * @throws NullPointerException if {@code name} is null
     * @since 1.5
     */
    // 指定定时线程的名字和指定是否为守护线程的构造方法
    public Timer(String name, boolean isDaemon) {
        thread.setName(name);
        thread.setDaemon(isDaemon);
        thread.start();
    }

    private static int serialNumber() {
        return nextSerialNumber.getAndIncrement();
    }

    /**
     * Schedules the specified task for execution after the specified delay.
     *
     * @param task  task to be scheduled.
     * @param delay delay in milliseconds before task is to be executed.
     * @throws IllegalArgumentException if <tt>delay</tt> is negative, or
     *                                  <tt>delay + System.currentTimeMillis()</tt> is negative.
     * @throws IllegalStateException    if task was already scheduled or
     *                                  cancelled, timer was cancelled, or timer thread terminated.
     * @throws NullPointerException     if {@code task} is null
     */
    // 安排任务在delay延时后开始执行
    public void schedule(TimerTask task, long delay) {
        if (delay < 0)
            throw new IllegalArgumentException("Negative delay.");
        sched(task, System.currentTimeMillis() + delay, 0);
    }

    /**
     * Schedules the specified task for execution at the specified time.  If
     * the time is in the past, the task is scheduled for immediate execution.
     *
     * @param task task to be scheduled.
     * @param time time at which task is to be executed.
     * @throws IllegalArgumentException if <tt>time.getTime()</tt> is negative.
     * @throws IllegalStateException    if task was already scheduled or
     *                                  cancelled, timer was cancelled, or timer thread terminated.
     * @throws NullPointerException     if {@code task} or {@code time} is null
     */
    public void schedule(TimerTask task, Date time) {
        sched(task, time.getTime(), 0);
    }

    /**
     * Schedules the specified task for repeated <i>fixed-delay execution</i>,
     * beginning after the specified delay.  Subsequent executions take place
     * at approximately regular intervals separated by the specified period.
     *
     * <p>In fixed-delay execution, each execution is scheduled relative to
     * the actual execution time of the previous execution.  If an execution
     * is delayed for any reason (such as garbage collection or other
     * background activity), subsequent executions will be delayed as well.
     * In the long run, the frequency of execution will generally be slightly
     * lower than the reciprocal of the specified period (assuming the system
     * clock underlying <tt>Object.wait(long)</tt> is accurate).
     *
     * <p>Fixed-delay execution is appropriate for recurring activities
     * that require "smoothness."  In other words, it is appropriate for
     * activities where it is more important to keep the frequency accurate
     * in the short run than in the long run.  This includes most animation
     * tasks, such as blinking a cursor at regular intervals.  It also includes
     * tasks wherein regular activity is performed in response to human
     * input, such as automatically repeating a character as long as a key
     * is held down.
     *
     * @param task   task to be scheduled.
     * @param delay  delay in milliseconds before task is to be executed.
     * @param period time in milliseconds between successive task executions.
     * @throws IllegalArgumentException if {@code delay < 0}, or
     *                                  {@code delay + System.currentTimeMillis() < 0}, or
     *                                  {@code period <= 0}
     * @throws IllegalStateException    if task was already scheduled or
     *                                  cancelled, timer was cancelled, or timer thread terminated.
     * @throws NullPointerException     if {@code task} is null
     */
    public void schedule(TimerTask task, long delay, long period) {
        if (delay < 0)
            throw new IllegalArgumentException("Negative delay.");
        if (period <= 0)
            throw new IllegalArgumentException("Non-positive period.");
        sched(task, System.currentTimeMillis() + delay, -period);
    }

    /**
     * Schedules the specified task for repeated <i>fixed-delay execution</i>,
     * beginning at the specified time. Subsequent executions take place at
     * approximately regular intervals, separated by the specified period.
     *
     * <p>In fixed-delay execution, each execution is scheduled relative to
     * the actual execution time of the previous execution.  If an execution
     * is delayed for any reason (such as garbage collection or other
     * background activity), subsequent executions will be delayed as well.
     * In the long run, the frequency of execution will generally be slightly
     * lower than the reciprocal of the specified period (assuming the system
     * clock underlying <tt>Object.wait(long)</tt> is accurate).  As a
     * consequence of the above, if the scheduled first time is in the past,
     * it is scheduled for immediate execution.
     *
     * <p>Fixed-delay execution is appropriate for recurring activities
     * that require "smoothness."  In other words, it is appropriate for
     * activities where it is more important to keep the frequency accurate
     * in the short run than in the long run.  This includes most animation
     * tasks, such as blinking a cursor at regular intervals.  It also includes
     * tasks wherein regular activity is performed in response to human
     * input, such as automatically repeating a character as long as a key
     * is held down.
     *
     * @param task      task to be scheduled.
     * @param firstTime First time at which task is to be executed.
     * @param period    time in milliseconds between successive task executions.
     * @throws IllegalArgumentException if {@code firstTime.getTime() < 0}, or
     *                                  {@code period <= 0}
     * @throws IllegalStateException    if task was already scheduled or
     *                                  cancelled, timer was cancelled, or timer thread terminated.
     * @throws NullPointerException     if {@code task} or {@code firstTime} is null
     */
    // 安排【固定延时】任务在firstTime时间时开始执行
    public void schedule(TimerTask task, Date firstTime, long period) {
        if (period <= 0)
            throw new IllegalArgumentException("Non-positive period.");
        sched(task, firstTime.getTime(), -period);
    }

    /**
     * Schedules the specified task for repeated <i>fixed-rate execution</i>,
     * beginning after the specified delay.  Subsequent executions take place
     * at approximately regular intervals, separated by the specified period.
     *
     * <p>In fixed-rate execution, each execution is scheduled relative to the
     * scheduled execution time of the initial execution.  If an execution is
     * delayed for any reason (such as garbage collection or other background
     * activity), two or more executions will occur in rapid succession to
     * "catch up."  In the long run, the frequency of execution will be
     * exactly the reciprocal of the specified period (assuming the system
     * clock underlying <tt>Object.wait(long)</tt> is accurate).
     *
     * <p>Fixed-rate execution is appropriate for recurring activities that
     * are sensitive to <i>absolute</i> time, such as ringing a chime every
     * hour on the hour, or running scheduled maintenance every day at a
     * particular time.  It is also appropriate for recurring activities
     * where the total time to perform a fixed number of executions is
     * important, such as a countdown timer that ticks once every second for
     * ten seconds.  Finally, fixed-rate execution is appropriate for
     * scheduling multiple repeating timer tasks that must remain synchronized
     * with respect to one another.
     *
     * @param task   task to be scheduled.
     * @param delay  delay in milliseconds before task is to be executed.
     * @param period time in milliseconds between successive task executions.
     * @throws IllegalArgumentException if {@code delay < 0}, or
     *                                  {@code delay + System.currentTimeMillis() < 0}, or
     *                                  {@code period <= 0}
     * @throws IllegalStateException    if task was already scheduled or
     *                                  cancelled, timer was cancelled, or timer thread terminated.
     * @throws NullPointerException     if {@code task} is null
     */
    // 安排【固定周期】任务在delay延时后开始执行
    public void scheduleAtFixedRate(TimerTask task, long delay, long period) {
        if (delay < 0)
            throw new IllegalArgumentException("Negative delay.");
        if (period <= 0)
            throw new IllegalArgumentException("Non-positive period.");
        sched(task, System.currentTimeMillis() + delay, period);
    }

    /**
     * Schedules the specified task for repeated <i>fixed-rate execution</i>,
     * beginning at the specified time. Subsequent executions take place at
     * approximately regular intervals, separated by the specified period.
     *
     * <p>In fixed-rate execution, each execution is scheduled relative to the
     * scheduled execution time of the initial execution.  If an execution is
     * delayed for any reason (such as garbage collection or other background
     * activity), two or more executions will occur in rapid succession to
     * "catch up."  In the long run, the frequency of execution will be
     * exactly the reciprocal of the specified period (assuming the system
     * clock underlying <tt>Object.wait(long)</tt> is accurate).  As a
     * consequence of the above, if the scheduled first time is in the past,
     * then any "missed" executions will be scheduled for immediate "catch up"
     * execution.
     *
     * <p>Fixed-rate execution is appropriate for recurring activities that
     * are sensitive to <i>absolute</i> time, such as ringing a chime every
     * hour on the hour, or running scheduled maintenance every day at a
     * particular time.  It is also appropriate for recurring activities
     * where the total time to perform a fixed number of executions is
     * important, such as a countdown timer that ticks once every second for
     * ten seconds.  Finally, fixed-rate execution is appropriate for
     * scheduling multiple repeating timer tasks that must remain synchronized
     * with respect to one another.
     *
     * @param task      task to be scheduled.
     * @param firstTime First time at which task is to be executed.
     * @param period    time in milliseconds between successive task executions.
     * @throws IllegalArgumentException if {@code firstTime.getTime() < 0} or
     *                                  {@code period <= 0}
     * @throws IllegalStateException    if task was already scheduled or
     *                                  cancelled, timer was cancelled, or timer thread terminated.
     * @throws NullPointerException     if {@code task} or {@code firstTime} is null
     */
    // 安排【固定周期】任务在firstTime时间时开始执行
    public void scheduleAtFixedRate(TimerTask task, Date firstTime, long period) {
        if (period <= 0)
            throw new IllegalArgumentException("Non-positive period.");
        sched(task, firstTime.getTime(), period);
    }

    /**
     * Schedule the specified timer task for execution at the specified
     * time with the specified period, in milliseconds.  If period is
     * positive, the task is scheduled for repeated execution; if period is
     * zero, the task is scheduled for one-time execution. Time is specified
     * in Date.getTime() format.  This method checks timer state, task state,
     * and initial execution time, but not period.
     *
     * @throws IllegalArgumentException if <tt>time</tt> is negative.
     * @throws IllegalStateException    if task was already scheduled or
     *                                  cancelled, timer was cancelled, or timer thread terminated.
     * @throws NullPointerException     if {@code task} is null
     */
    /*
     * 安排任务以待执行
     *
     * time  ：任务触发时间
     *
     * period：任务的重复模式：
     *         零：非重复任务：只执行一次
     *         正数：重复性任务：固定周期，从任务初次被触发开始，以后每隔period时间就被触发一次
     *         负数：重复性任务：固定延时，任务下次的开始时间=任务上次结束时间+(-period)
     */
    private void sched(TimerTask task, long time, long period) {
        if (time < 0)
            throw new IllegalArgumentException("Illegal execution time.");

        // Constrain value of period sufficiently to prevent numeric
        // overflow while still being effectively infinitely large.
        if (Math.abs(period) > (Long.MAX_VALUE >> 1))
            period >>= 1;

        synchronized (queue) {
            if (!thread.newTasksMayBeScheduled)
                throw new IllegalStateException("Timer already cancelled.");

            synchronized (task.lock) {
                // 如果任务已经开始执行或已被取消，抛出异常
                if (task.state != TimerTask.VIRGIN)
                    throw new IllegalStateException(
                            "Task already scheduled or cancelled");
                // 记录任务触发时间
                task.nextExecutionTime = time;
                // 记录重复次数
                task.period = period;
                // 修改状态
                task.state = TimerTask.SCHEDULED;
            }
            // 添加到队列中
            queue.add(task);
            // 如果当前任务排在队头
            if (queue.getMin() == task) {
                // 唤醒任务
                queue.notify();
            }

        }
    }

    /**
     * Terminates this timer, discarding any currently scheduled tasks.
     * Does not interfere with a currently executing task (if it exists).
     * Once a timer has been terminated, its execution thread terminates
     * gracefully, and no more tasks may be scheduled on it.
     *
     * <p>Note that calling this method from within the run method of a
     * timer task that was invoked by this timer absolutely guarantees that
     * the ongoing task execution is the last task execution that will ever
     * be performed by this timer.
     *
     * <p>This method may be called repeatedly; the second and subsequent
     * calls have no effect.
     */
    // 取消任务
    public void cancel() {
        synchronized (queue) {
            // 不再执行任务
            thread.newTasksMayBeScheduled = false;
            // 清空任务
            queue.clear();
            // 唤醒定时器线程
            queue.notify();  // In case queue was already empty.
        }
    }

    /**
     * Removes all cancelled tasks from this timer's task queue.  <i>Calling
     * this method has no effect on the behavior of the timer</i>, but
     * eliminates the references to the cancelled tasks from the queue.
     * If there are no external references to these tasks, they become
     * eligible for garbage collection.
     *
     * <p>Most programs will have no need to call this method.
     * It is designed for use by the rare application that cancels a large
     * number of tasks.  Calling this method trades time for space: the
     * runtime of the method may be proportional to n + c log n, where n
     * is the number of tasks in the queue and c is the number of cancelled
     * tasks.
     *
     * <p>Note that it is permissible to call this method from within a
     * a task scheduled on this timer.
     *
     * @return the number of tasks removed from the queue.
     * @since 1.5
     */
    // 清除所有进入【取消】状态的任务
    public int purge() {
        int result = 0;

        synchronized (queue) {
            // 遍历queue
            for (int i = queue.size(); i > 0; i--) {
                // 找出已取消的任务
                if (queue.get(i).state == TimerTask.CANCELLED) {
                    // 移除已取消的任务
                    queue.quickRemove(i);
                    result++;
                }
            }
            // 将重建小顶堆的时间推迟到这里
            if (result != 0)
                // 重建小顶堆
                queue.heapify();
        }

        return result;
    }
}

/**
 * This "helper class" implements the timer's task execution thread, which
 * waits for tasks on the timer queue, executions them when they fire,
 * reschedules repeating tasks, and removes cancelled tasks and spent
 * non-repeating tasks from the queue.
 */
class TimerThread extends Thread {
    /**
     * This flag is set to false by the reaper to inform us that there
     * are no more live references to our Timer object.  Once this flag
     * is true and there are no more tasks in our queue, there is no
     * work left for us to do, so we terminate gracefully.  Note that
     * this field is protected by queue's monitor!
     */
    // 定时器是否已取消（不再执行新任务）
    boolean newTasksMayBeScheduled = true;

    /**
     * Our Timer's queue.  We store this reference in preference to
     * a reference to the Timer so the reference graph remains acyclic.
     * Otherwise, the Timer would never be garbage-collected and this
     * thread would never go away.
     */
    // 任务队列
    private TaskQueue queue;

    TimerThread(TaskQueue queue) {
        this.queue = queue;
    }

    // 初始化定时器后，定时器线程随之启动
    public void run() {
        try {
            // 进入定时器主循环
            mainLoop();
        } finally {
            // Someone killed this Thread, behave as if Timer cancelled
            synchronized (queue) {
                newTasksMayBeScheduled = false;
                // 清空任务队列
                queue.clear();  // Eliminate obsolete references
            }
        }
    }

    /**
     * The main timer loop.  (See class comment.)
     */
    // 定时器主循环
    private void mainLoop() {
        while (true) {
            try {
                TimerTask task;
                boolean taskFired;
                synchronized (queue) {
                    // 当队列中没有任务时，进入等待
                    // Wait for queue to become non-empty
                    while (queue.isEmpty() && newTasksMayBeScheduled) {
                        queue.wait();
                    }
                    // 被唤醒-判断队列是否为空，为空则退出，表示该任务被取消
                    if (queue.isEmpty()) {
                        break; // Queue is empty and will forever remain; die
                    }

                    // Queue nonempty; look at first evt and do the right thing
                    long currentTime, executionTime;
                    // 获取最小的任务
                    task = queue.getMin();
                    synchronized (task.lock) {
                        // 若任务状态为取消状态，则移除该任务，进入下一次循环
                        if (task.state == TimerTask.CANCELLED) {
                            // 移除队头任务，并将触发时间最近的任务放在队头
                            queue.removeMin();
                            // 重新开始主循环
                            continue;  // No action required, poll queue again
                        }
                        // 获取当前的时间戳
                        currentTime = System.currentTimeMillis();
                        // 获取任务触发时间
                        executionTime = task.nextExecutionTime;
                        // 如果任务可以开始执行了
                        if (taskFired = (executionTime <= currentTime)) {
                            // 若任务只需要执行一次，则移除该任务，并修改状态为【执行】状态
                            if (task.period == 0) { // Non-repeating, remove
                                queue.removeMin();
                                task.state = TimerTask.EXECUTED;
                            } else { // Repeating task, reschedule
                                // 计算重复性任务的下次触发时间
                                long newTime = task.period < 0
                                        ? currentTime - task.period     // 固定延时，任务下次的触发时间=任务本次实际触发时间+(-period)
                                        : executionTime + task.period;  // 固定周期，从任务初次被触发开始，以后每隔period时间就被触发一次

                                // 重置队头任务的触发时间，并将其调整到队列中的合适位置
                                queue.rescheduleMin(newTime);
                            }
                        }
                    }
                    // 如果任务还未到触发时间，定时器线程进入阻塞
                    if (!taskFired) // Task hasn't yet fired; wait
                        queue.wait(executionTime - currentTime);
                }
                // 如果任务可以开始执行了
                if (taskFired)  // Task fired; run it, holding no locks
                    task.run();
            } catch (InterruptedException e) {
            }
        }
    }
}

/**
 * This class represents a timer task queue: a priority queue of TimerTasks,
 * ordered on nextExecutionTime.  Each Timer object has one of these, which it
 * shares with its TimerThread.  Internally this class uses a heap, which
 * offers log(n) performance for the add, removeMin and rescheduleMin
 * operations, and constant time performance for the getMin operation.
 */
class TaskQueue {
    /**
     * Priority queue represented as a balanced binary heap: the two children
     * of queue[n] are queue[2*n] and queue[2*n+1].  The priority queue is
     * ordered on the nextExecutionTime field: The TimerTask with the lowest
     * nextExecutionTime is in queue[1] (assuming the queue is nonempty).  For
     * each node n in the heap, and each descendant of n, d,
     * n.nextExecutionTime <= d.nextExecutionTime.
     */
    // 任务队列，实际存储任务的地方，索引0处空闲
    private TimerTask[] queue = new TimerTask[128];

    /**
     * The number of tasks in the priority queue.  (The tasks are stored in
     * queue[1] up to queue[size]).
     */
    // 任务数量
    private int size = 0;

    /**
     * Returns the number of tasks currently on the queue.
     */
    // 访问任务数量
    int size() {
        return size;
    }

    /**
     * Adds a new task to the priority queue.
     */
    // 添加任务
    void add(TimerTask task) {
        // 若添加任务后任务数量大于数组的容量，则进行扩容，2倍
        // Grow backing store if necessary
        if (size + 1 == queue.length)
            queue = Arrays.copyOf(queue, 2 * queue.length);
        // 存放数据
        queue[++size] = task;
        // 调整size处的任务到队列中的合适位置
        fixUp(size);
    }

    /**
     * Return the "head task" of the priority queue.  (The head task is an
     * task with the lowest nextExecutionTime.)
     */
    // 获取队头任务
    TimerTask getMin() {
        return queue[1];
    }

    /**
     * Return the ith task in the priority queue, where i ranges from 1 (the
     * head task, which is returned by getMin) to the number of tasks on the
     * queue, inclusive.
     */
    // 获取指定位置的任务
    TimerTask get(int i) {
        return queue[i];
    }

    /**
     * Remove the head task from the priority queue.
     */
    // 移除队头任务
    void removeMin() {
        // 将队尾任务赋值给队头
        queue[1] = queue[size];
        //丢弃重复队尾
        queue[size--] = null;  // Drop extra reference to prevent memory leak
        // 调整当前队头任务（之前的队尾任务）到队列中合适的位置
        fixDown(1);
    }

    /**
     * Removes the ith element from queue without regard for maintaining
     * the heap invariant.  Recall that queue is one-based, so
     * 1 <= i <= size.
     */
    // 快速移除索引i处的任务（没有重建小顶堆）
    void quickRemove(int i) {
        assert i <= size;

        queue[i] = queue[size];
        queue[size--] = null;  // Drop extra ref to prevent memory leak
    }

    /**
     * Sets the nextExecutionTime associated with the head task to the
     * specified value, and adjusts priority queue accordingly.
     */
    // 重置队头任务的触发时间，并将其调整到队列中的合适位置
    void rescheduleMin(long newTime) {
        // 重新设置触发时间
        queue[1].nextExecutionTime = newTime;
        // 向下重建小顶堆
        fixDown(1);
    }

    /**
     * Returns true if the priority queue contains no elements.
     */
    // 判断队列是否为为空
    boolean isEmpty() {
        return size == 0;
    }

    /**
     * Removes all elements from the priority queue.
     */
    // 清空任务
    void clear() {
        // Null out task references to prevent memory leak
        for (int i = 1; i <= size; i++)
            queue[i] = null;

        size = 0;
    }

    /**
     * Establishes the heap invariant (described above) assuming the heap
     * satisfies the invariant except possibly for the leaf-node indexed by k
     * (which may have a nextExecutionTime less than its parent's).
     * <p>
     * This method functions by "promoting" queue[k] up the hierarchy
     * (by swapping it with its parent) repeatedly until queue[k]'s
     * nextExecutionTime is greater than or equal to that of its parent.
     */
    //  调整size处的任务到队列中的合适位置
    // 插入。需要从小顶堆的结点k开始，向【上】查找一个合适的位置插入原k索引处的任务
    private void fixUp(int k) {
        while (k > 1) {
            // 获取父结点索引
            int j = k >> 1;
            //  如果待插入元素大于父节点中的元素，则退出循环
            if (queue[j].nextExecutionTime <= queue[k].nextExecutionTime) {
                break;
            }
            // 子结点保存父结点中的元素
            TimerTask tmp = queue[j];
            queue[j] = queue[k];
            queue[k] = tmp;
            // 再次向上查找
            k = j;
        }
    }

    /**
     * Establishes the heap invariant (described above) in the subtree
     * rooted at k, which is assumed to satisfy the heap invariant except
     * possibly for node k itself (which may have a nextExecutionTime greater
     * than its children's).
     * <p>
     * This method functions by "demoting" queue[k] down the hierarchy
     * (by swapping it with its smaller child) repeatedly until queue[k]'s
     * nextExecutionTime is less than or equal to those of its children.
     */
    // 插入。需要从小顶堆的结点k开始，向【下】查找一个合适的位置插入原k索引处的任务
    private void fixDown(int k) {
        int j;
        while ((j = k << 1) <= size && j > 0) {
            //  让j存储子结点中较小结点的索引
            if (j < size && queue[j].nextExecutionTime > queue[j + 1].nextExecutionTime) {
                j++; // j indexes smallest kid
            }
            // 如果待插入元素小于子结点中较小的元素，则退出循环
            if (queue[k].nextExecutionTime <= queue[j].nextExecutionTime) {
                break;
            }
            // 交换父子节点
            TimerTask tmp = queue[j];
            queue[j] = queue[k];
            queue[k] = tmp;
            // 重新赋值父节点，再次向下查找
            k = j;
        }
    }

    /**
     * Establishes the heap invariant (described above) in the entire tree,
     * assuming nothing about the order of the elements prior to the call.
     */
    // 重建小顶堆
    void heapify() {
        for (int i = size / 2; i >= 1; i--)
            fixDown(i);
    }
}
