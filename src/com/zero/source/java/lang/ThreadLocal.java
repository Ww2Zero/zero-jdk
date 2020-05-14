/*
 * Copyright (c) 1997, 2013, Oracle and/or its affiliates. All rights reserved.
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

package java.lang;
import java.lang.ref.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * This class provides thread-local variables.  These variables differ from
 * their normal counterparts in that each thread that accesses one (via its
 * {@code get} or {@code set} method) has its own, independently initialized
 * copy of the variable.  {@code ThreadLocal} instances are typically private
 * static fields in classes that wish to associate state with a thread (e.g.,
 * a user ID or Transaction ID).
 *
 * <p>For example, the class below generates unique identifiers local to each
 * thread.
 * A thread's id is assigned the first time it invokes {@code ThreadId.get()}
 * and remains unchanged on subsequent calls.
 * <pre>
 * import java.util.concurrent.atomic.AtomicInteger;
 *
 * public class ThreadId {
 *     // Atomic integer containing the next thread ID to be assigned
 *     private static final AtomicInteger nextId = new AtomicInteger(0);
 *
 *     // Thread local variable containing each thread's ID
 *     private static final ThreadLocal&lt;Integer&gt; threadId =
 *         new ThreadLocal&lt;Integer&gt;() {
 *             &#64;Override protected Integer initialValue() {
 *                 return nextId.getAndIncrement();
 *         }
 *     };
 *
 *     // Returns the current thread's unique ID, assigning it if necessary
 *     public static int get() {
 *         return threadId.get();
 *     }
 * }
 * </pre>
 * <p>Each thread holds an implicit reference to its copy of a thread-local
 * variable as long as the thread is alive and the {@code ThreadLocal}
 * instance is accessible; after a thread goes away, all of its copies of
 * thread-local instances are subject to garbage collection (unless other
 * references to these copies exist).
 *
 * @author  Josh Bloch and Doug Lea
 * @since   1.2
 */
/**
 * 线程局部缓存：为线程缓存数据，将数据保存在线程本地化(即线程中的ThreadLocalMap)
 *
 * 原理：
 * 1. 每个线程由一个ThreadLocalMap属性，本质就是一个数组
 * 2. 数组里面存储的<key, value>称为键值对，存储键值对时需要先求取哈希值
 *    由于哈希值会出现冲突，所以会造成“错位”元素的出现（元素“理想位置”和实际存储位置不一样）
 *    “理想位置”是指该ThreadLocal对象初次计算出的哈希值
 *    如果从“理想位置”到实际存储位置是连续的，这里称该序列是“紧凑”的
 * 3. map里存储的key是一个弱引用，其包装了当前线程中构造的ThreadLocal对象
 *    这意味着，只要ThreadLocal对象丢掉了强引用，那么在下次GC后，map中的ThreadLocal对象也会被清除
 *    对于那些ThreadLocal对象为空的map元素，这里称其为【垃圾值】，稍后会被主动清理
 * 4. map里存储的value就是缓存到当前线程的值，这个value没有弱引用去包装，需要专门的释放策略
 * 5. 一个线程对应多个ThreadLocal，一个ThreadLocal只对应一个值
 *
 * 注，关于哈希值碰撞的问题：
 * 如果是单线程，因为魔数HASH_INCREMENT的存在，且不断扩容，这里不容易出现碰撞
 * 但如果是多线程，哈希值就很容易出现碰撞，因为属性nextHashCode是各线程共享的，会导致生成的指纹出现重复
 *
 * ThreadLocal不能解决线程同步问题。
 *
 * 每个线程有一个ThreadLocalMap（作为map）。但可以有多个ThreadLocal（作为map中的key）。
 * 使用时
 * ThreadLocal<T> sThreadLocal = new ThreadLocal<>();
 * 实际存储在数组中的逻辑为<sThreadLocal, T>形成map的键值对，
 * sThreadLocal作为ThreadLocalMap中的键，用它来查找匹配的值。
 */
public class ThreadLocal<T> {
    /**
     * ThreadLocals rely on per-thread linear-probe hash maps attached
     * to each thread (Thread.threadLocals and
     * inheritableThreadLocals).  The ThreadLocal objects act as keys,
     * searched via threadLocalHashCode.  This is a custom hash code
     * (useful only within ThreadLocalMaps) that eliminates collisions
     * in the common case where consecutively constructed ThreadLocals
     * are used by the same threads, while remaining well-behaved in
     * less common cases.
     */
    /**
     * 原始种子，由内置种子计算而来，用来生成均匀的索引
     *
     * 一个线程可以有多个ThreadLocal实例，各实例之内的原始种子值不相同
     * 一个ThreadLocal实例也可被多个线程共享，此时多个线程内看到的原始种子值是相同的
     */
    private final int threadLocalHashCode = nextHashCode();

    /**
     * The next hash code to be given out. Updated atomically. Starts at
     * zero.
     */
    // 内置种子，由所有ThreadLocal共享，但每次构造一个ThreadLocal实例，其值都会更新
    private static AtomicInteger nextHashCode =
        new AtomicInteger();

    /**
     * The difference between successively generated hash codes - turns
     * implicit sequential thread-local IDs into near-optimally spread
     * multiplicative hash values for power-of-two-sized tables.
     */
    //HASH_INCREMENT是一个哈希魔数 保证生成随机-均匀 产生 [0,Max-1] 这 Max 个数字。
    private static final int HASH_INCREMENT = 0x61c88647;

    /**
     * Returns the next hash code.
     */
    // 返回计算出的下一个哈希值，其值为i*HASH_INCREMENT，其中i代表调用次数
    private static int nextHashCode() {
        // 每次调用都在上一次的哈希值上加HASH_INCREMENT
        return nextHashCode.getAndAdd(HASH_INCREMENT);
    }

    /**
     * Returns the current thread's "initial value" for this
     * thread-local variable.  This method will be invoked the first
     * time a thread accesses the variable with the {@link #get}
     * method, unless the thread previously invoked the {@link #set}
     * method, in which case the {@code initialValue} method will not
     * be invoked for the thread.  Normally, this method is invoked at
     * most once per thread, but it may be invoked again in case of
     * subsequent invocations of {@link #remove} followed by {@link #get}.
     *
     * <p>This implementation simply returns {@code null}; if the
     * programmer desires thread-local variables to have an initial
     * value other than {@code null}, {@code ThreadLocal} must be
     * subclassed, and this method overridden.  Typically, an
     * anonymous inner class will be used.
     *
     * @return the initial value for this thread-local
     */
    // 为ThreadLocal对象设置关联的初值，具体逻辑可由子类实现
    protected T initialValue() {
        return null;
    }

    /**
     * Creates a thread local variable. The initial value of the variable is
     * determined by invoking the {@code get} method on the {@code Supplier}.
     *
     * @param <S> the type of the thread local's value
     * @param supplier the supplier to be used to determine the initial value
     * @return a new thread local variable
     * @throws NullPointerException if the specified supplier is null
     * @since 1.8
     */
    public static <S> ThreadLocal<S> withInitial(Supplier<? extends S> supplier) {
        return new SuppliedThreadLocal<>(supplier);
    }

    /**
     * Creates a thread local variable.
     * @see #withInitial(java.util.function.Supplier)
     */
    // 构造ThreadLocal实例前，原始种子也会被一同构造
    public ThreadLocal() {
    }

    /**
     * Returns the value in the current thread's copy of this
     * thread-local variable.  If the variable has no value for the
     * current thread, it is first initialized to the value returned
     * by an invocation of the {@link #initialValue} method.
     *
     * @return the current thread's value of this thread-local
     */
    // 获取当前线程中本对象所对应的值
    public T get() {
        Thread t = Thread.currentThread();
        // 返回当前线程t持有的map
        ThreadLocalMap map = getMap(t);
        // 若map存在，则获取this对象对应的值
        if (map != null) {
            ThreadLocalMap.Entry e = map.getEntry(this);
            if (e != null) {
                @SuppressWarnings("unchecked")
                T result = (T)e.value;
                return result;
            }
        }
        /**
         * 如果map为空，或者map不空，但是还没有存储当前的ThreadLocalMap对象，则执行以下逻辑
         * 初始化map，并存储键值对<key, value>，最后返回value
         * 其中，key是当前的ThreadLocal对象，value是为当前的ThreadLocal对象关联的初值
         */
        return setInitialValue();
    }

    /**
     * Variant of set() to establish initialValue. Used instead
     * of set() in case user has overridden the set() method.
     *
     * @return the initial value
     */
    /**
     * 初始化map，并存储键值对<key, value>，最后返回value
     * 其中，key是当前的ThreadLocal对象，value是为当前的ThreadLocal对象关联的初值
     */
    private T setInitialValue() {
        // 初始化value
        T value = initialValue();
        // 获取当前线程对象
        Thread t = Thread.currentThread();
        // 获取当前线程对象所对应的map数组
        ThreadLocalMap map = getMap(t);
        // 若map存在，则设置<ThreadLocal,value>的键值对
        if (map != null)
            map.set(this, value);
        // 若map不存在，则创建map，并存储<ThreadLocal,value>的键值对
        else
            createMap(t, value);
        return value;
    }

    /**
     * Sets the current thread's copy of this thread-local variable
     * to the specified value.  Most subclasses will have no need to
     * override this method, relying solely on the {@link #initialValue}
     * method to set the values of thread-locals.
     *
     * @param value the value to be stored in the current thread's copy of
     *        this thread-local.
     */
    // 为当前ThreadLocal对象关联value值
    public void set(T value) {
        // 获取当前线程对象
        Thread t = Thread.currentThread();
        // 获取当前线程对象中的数组(map)
        ThreadLocalMap map = getMap(t);
        // 若map存在，则创建map
        if (map != null)
            map.set(this, value);
        // 若map不存在，则创建数组，并存储<this,value>的键值对
        else
            createMap(t, value);
    }

    /**
     * Removes the current thread's value for this thread-local
     * variable.  If this thread-local variable is subsequently
     * {@linkplain #get read} by the current thread, its value will be
     * reinitialized by invoking its {@link #initialValue} method,
     * unless its value is {@linkplain #set set} by the current thread
     * in the interim.  This may result in multiple invocations of the
     * {@code initialValue} method in the current thread.
     *
     * @since 1.5
     */
    // 移除当前线程对象中本threadLocal对象
     public void remove() {
         ThreadLocalMap m = getMap(Thread.currentThread());
         if (m != null)
             m.remove(this);
     }

    /**
     * Get the map associated with a ThreadLocal. Overridden in
     * InheritableThreadLocal.
     *
     * @param  t the current thread
     * @return the map
     */
    // 获取线程中的threadLocals对应的map数组
    ThreadLocalMap getMap(Thread t) {
        return t.threadLocals;
    }

    /**
     * Create the map associated with a ThreadLocal. Overridden in
     * InheritableThreadLocal.
     *
     * @param t the current thread
     * @param firstValue value for the initial entry of the map
     */
    // 创建数组，并存储键值对<this, firstValue>
    void createMap(Thread t, T firstValue) {
        t.threadLocals = new ThreadLocalMap(this, firstValue);
    }

    /**
     * Factory method to create map of inherited thread locals.
     * Designed to be called only from Thread constructor.
     *
     * @param  parentMap the map associated with parent thread
     * @return a map containing the parent's inheritable bindings
     */
    // 构造一个新的map，其包含给定的parentMap中当前所有可继承ThreadLocals，且允许修改parentMap中的值
    static ThreadLocalMap createInheritedMap(ThreadLocalMap parentMap) {
        return new ThreadLocalMap(parentMap);
    }

    /**
     * Method childValue is visibly defined in subclass
     * InheritableThreadLocal, but is internally defined here for the
     * sake of providing createInheritedMap factory method without
     * needing to subclass the map class in InheritableThreadLocal.
     * This technique is preferable to the alternative of embedding
     * instanceof tests in methods.
     */
    // 获取parentValue，有时会对其进行加工，主要用于测试，参见子类InheritableThreadLocal等。
    T childValue(T parentValue) {
        throw new UnsupportedOperationException();
    }

    /**
     * An extension of ThreadLocal that obtains its initial value from
     * the specified {@code Supplier}.
     */
    // ThreadLocal的一个扩展。其ThreadLocal关联的初值由字段supplier给出
    static final class SuppliedThreadLocal<T> extends ThreadLocal<T> {

        private final Supplier<? extends T> supplier;

        SuppliedThreadLocal(Supplier<? extends T> supplier) {
            this.supplier = Objects.requireNonNull(supplier);
        }

        @Override
        protected T initialValue() {
            return supplier.get();
        }
    }

    /**
     * ThreadLocalMap is a customized hash map suitable only for
     * maintaining thread local values. No operations are exported
     * outside of the ThreadLocal class. The class is package private to
     * allow declaration of fields in class Thread.  To help deal with
     * very large and long-lived usages, the hash table entries use
     * WeakReferences for keys. However, since reference queues are not
     * used, stale entries are guaranteed to be removed only when
     * the table starts running out of space.
     */
    // 线程中存储本地值的实际数组对象
    static class ThreadLocalMap {

        /**
         * The entries in this hash map extend WeakReference, using
         * its main ref field as the key (which is always a
         * ThreadLocal object).  Note that null keys (i.e. entry.get()
         * == null) mean that the key is no longer referenced, so the
         * entry can be expunged from table.  Such entries are referred to
         * as "stale entries" in the code that follows.
         */
        // 键值对实体的存储结构
        static class Entry extends WeakReference<ThreadLocal<?>> {
            /** The value associated with this ThreadLocal. */
            Object value;

            Entry(ThreadLocal<?> k, Object v) {
                super(k);
                value = v;
            }
        }
        /**
         * The initial capacity -- MUST be a power of two.
         */
        // 默认初始化容器大小 必须为2的指数倍
        private static final int INITIAL_CAPACITY = 16;

        /**
         * The table, resized as necessary.
         * table.length MUST always be a power of two.
         */
        // 存储Map中的键值对实体数组
        private Entry[] table;

        /**
         * The number of entries in the table.
         */
        // 实际存储的键值对数量
        private int size = 0;

        /**
         * The next size value at which to resize.
         */
        // resize的阈值
        private int threshold; // Default to 0

        /**
         * Set the resize threshold to maintain at worst a 2/3 load factor.
         */
        private void setThreshold(int len) {
            threshold = len * 2 / 3;
        }

        /**
         * Increment i modulo len.
         */
        private static int nextIndex(int i, int len) {
            return ((i + 1 < len) ? i + 1 : 0);
        }

        /**
         * Decrement i modulo len.
         */
        private static int prevIndex(int i, int len) {
            return ((i - 1 >= 0) ? i - 1 : len - 1);
        }

        /**
         * Construct a new map initially containing (firstKey, firstValue).
         * ThreadLocalMaps are constructed lazily, so we only create
         * one when we have at least one entry to put in it.
         */
        // 初始化map，并存储键值对<firstKey, firstValue>
        ThreadLocalMap(ThreadLocal<?> firstKey, Object firstValue) {
            // 初始化数组
            table = new Entry[INITIAL_CAPACITY];
            // 计算firstKey存续的位置
            int i = firstKey.threadLocalHashCode & (INITIAL_CAPACITY - 1);
            // 新建键值对并存在指定的数组对象中
            table[i] = new Entry(firstKey, firstValue);
            // 设置size
            size = 1;
            // 设置扩容阈值
            setThreshold(INITIAL_CAPACITY);
        }

        /**
         * Construct a new map including all Inheritable ThreadLocals
         * from given parent map. Called only by createInheritedMap.
         *
         * @param parentMap the map associated with parent thread.
         */
        // 构造一个新的map，其包含给定的parentMap中当前所有可继承ThreadLocals，且允许修改parentMap中的值。仅由createInheritedMap调用
        private ThreadLocalMap(ThreadLocalMap parentMap) {
            Entry[] parentTable = parentMap.table;
            int len = parentTable.length;
            setThreshold(len);
            table = new Entry[len];

            for (int j = 0; j < len; j++) {
                Entry e = parentTable[j];
                if (e != null) {
                    @SuppressWarnings("unchecked")
                    ThreadLocal<Object> key = (ThreadLocal<Object>) e.get();
                    if (key != null) {
                        Object value = key.childValue(e.value);
                        Entry c = new Entry(key, value);
                        int h = key.threadLocalHashCode & (len - 1);
                        while (table[h] != null)
                            h = nextIndex(h, len);
                        table[h] = c;
                        size++;
                    }
                }
            }
        }

        /**
         * Get the entry associated with key.  This method
         * itself handles only the fast path: a direct hit of existing
         * key. It otherwise relays to getEntryAfterMiss.  This is
         * designed to maximize performance for direct hits, in part
         * by making this method readily inlinable.
         *
         * @param  key the thread local object
         * @return the entry associated with key, or null if no such
         */
        // 返回key关联的键值对实体
        private Entry getEntry(ThreadLocal<?> key) {
            int i = key.threadLocalHashCode & (table.length - 1);
            Entry e = table[i];
            if (e != null && e.get() == key)
                return e;
            else
                return getEntryAfterMiss(key, i, e);
        }

        /**
         * Version of getEntry method for use when key is not found in
         * its direct hash slot.
         *
         * @param  key the thread local object
         * @param  i the table index for key's hash code
         * @param  e the entry at table[i]
         * @return the entry associated with key, or null if no such
         */
        // 从i开始向后遍历找到键值对实体
        private Entry getEntryAfterMiss(ThreadLocal<?> key, int i, Entry e) {
            Entry[] tab = table;
            int len = tab.length;

            while (e != null) {
                ThreadLocal<?> k = e.get();
                if (k == key)
                    return e;
                if (k == null)
                    // 从索引i开始，遍历一段【连续】的元素，清理其中的垃圾值，并使各元素排序更紧凑
                    expungeStaleEntry(i);
                else
                    i = nextIndex(i, len);
                e = tab[i];
            }
            return null;
        }

        /**
         * Set the value associated with key.
         *
         * @param key the thread local object
         * @param value the value to be set
         */
        // 在map中存储键值对<key, value>
        private void set(ThreadLocal<?> key, Object value) {

            // We don't use a fast path as with get() because it is at
            // least as common to use set() to create new entries as
            // it is to replace existing ones, in which case, a fast
            // path would fail more often than not.

            Entry[] tab = table;
            int len = tab.length;
            // 计算key对应的存储位置，需要考虑一个线程有多个ThreadLocal的情形
            int i = key.threadLocalHashCode & (len-1);
            // 遍历一段连续的元素，以查找匹配的ThreadLocal对象，从计算的得到的i开始
            for (Entry e = tab[i]; e != null; e = tab[i = nextIndex(i, len)]) {
                // 获取该哈希值处的ThreadLocal对象
                ThreadLocal<?> k = e.get();
                // 键值ThreadLocal匹配，直接更改map中的value
                if (k == key) {
                    e.value = value;
                    return;
                }
                /**
                 * 如果当前位置未找到匹配的ThreadLocal，就一直遍历Entry（由于哈希值存在碰撞问题，所以可能初次计算出的哈希值没法用）
                 * 向后遍历的过程中，会出现以下情形：
                 * 1. 找到了匹配的ThreadLocal，那么执行上面的if语句，并退出
                 * 2. 遇到了一个垃圾值
                 */
                if (k == null) {
                    /**
                     * 继续从索引index开始遍历map，给ThreadLocal对象安排合适的位置
                     * 安排完ThreadLocal对象后，还会清理一部分垃圾
                     */
                    replaceStaleEntry(key, value, i);
                    return;
                }
            }
            // 直到遇见了空槽也没找到匹配的ThreadLocal对象，那么在此空槽处安排ThreadLocal对象和缓存的value
            tab[i] = new Entry(key, value);
            int sz = ++size;
            // 从下标i开始向后遍历，清理一部分垃圾值，清理过后元素依然是紧凑的
            boolean isRemoved = cleanSomeSlots(i, sz);
            // 如果没有元素被清理，那么就要检查当前元素数量是否超过了容量阙值，以便决定是否扩容
            if (!isRemoved && sz >= threshold)
                // 需要扩容，扩容的过程也是对所有的key重新哈希的过程
                rehash();
        }

        /**
         * Remove the entry for key.
         */
        private void remove(ThreadLocal<?> key) {
            Entry[] tab = table;
            int len = tab.length;
            int i = key.threadLocalHashCode & (len-1);
            for (Entry e = tab[i];
                 e != null;
                 e = tab[i = nextIndex(i, len)]) {
                if (e.get() == key) {
                    e.clear();
                    expungeStaleEntry(i);
                    return;
                }
            }
        }

        /**
         * Replace a stale entry encountered during a set operation
         * with an entry for the specified key.  The value passed in
         * the value parameter is stored in the entry, whether or not
         * an entry already exists for the specified key.
         *
         * As a side effect, this method expunges all stale entries in the
         * "run" containing the stale entry.  (A run is a sequence of entries
         * between two null slots.)
         *
         * @param  key the key
         * @param  value the value to be associated with key
         * @param  staleSlot index of the first stale entry encountered while
         *         searching for key.
         */
        /**
         * 从索引staleSlot开始遍历map，给ThreadLocal对象安排合适的位置
         * 安排完ThreadLocal对象后，还会清理一部分垃圾
         *
         * key代表待匹配的ThreadLocal对象，value就是键值对里的值
         * staleSlot是遍历连续的元素去匹配ThreadLocal对象的过程中遇到的第一个垃圾值
         */
        private void replaceStaleEntry(ThreadLocal<?> key, Object value,
                                       int staleSlot) {
            Entry[] tab = table;
            int len = tab.length;
            Entry e;

            // Back up to check for prior stale entry in current run.
            // We clean out whole runs at a time to avoid continual
            // incremental rehashing due to garbage collector freeing
            // up refs in bunches (i.e., whenever the collector runs).
            int slotToExpunge = staleSlot;
            // 从staleSlot开始往前遍历一段连续的元素，找出最早出现垃圾值的位置
            for (int i = prevIndex(staleSlot, len);
                 (e = tab[i]) != null;
                 i = prevIndex(i, len))
                // 遇到了垃圾值
                if (e.get() == null)
                    // slotToExpunge用来记录在索引staleSlot之前的那段连续的元素中最早出现的垃圾值的下标
                    slotToExpunge = i;

            /**
             * 至此，i指向了一个空槽
             * 如果slotToExpunge == staleSlot，说明在(i, staleSlot)这段没有垃圾值
             * 如果slotToExpunge != staleSlot，说明在(i, staleSlot)这段有垃圾值，且从i开始遇到的第一个垃圾值是slotToExpunge
             *
             * 注：在此要想象一个循环链表，(i, staleSlot)只代表一段区域，i和staleSlot的值谁大谁小并不确定
             */
            // Find either the key or trailing null slot of run, whichever
            // occurs first
            // 从staleSlot开始向后遍历一段连续的元素，找出最晚出现垃圾值的位置
            for (int i = nextIndex(staleSlot, len);
                 (e = tab[i]) != null;
                 i = nextIndex(i, len)) {
                ThreadLocal<?> k = e.get();

                // If we find key, then we need to swap it
                // with the stale entry to maintain hash table order.
                // The newly stale slot, or any other stale slot
                // encountered above it, can then be sent to expungeStaleEntry
                // to remove or rehash all of the other entries in run.
                // 找到了匹配的ThreadLocal对象
                if (k == key) {
                    // 直接设置值
                    e.value = value;

                    /**
                     * 将ThreadLocal对象尽量往前挪，已知离理想位置最近且安全的“空”位置就是staleSlot
                     * 与此同时，垃圾值后移，稍后被清理
                     */
                    tab[i] = tab[staleSlot];
                    tab[staleSlot] = e;

                    // Start expunge at preceding stale entry if it exists
                    if (slotToExpunge == staleSlot)
                        // 可能需要更新slotToExpunge的位置（往后设置）
                        slotToExpunge = i;
                    // 从索引slotToExpunge开始，遍历一段【连续】的元素，清理其中的垃圾值，并使各元素排序更紧凑，返回值是那个终止遍历过程的空槽下标
                    int stop = expungeStaleEntry(slotToExpunge);

                    // 从下标stop开始向后遍历，清理一部分垃圾值
                    cleanSomeSlots(stop, len);
                    return;
                }

                // If we didn't find stale entry on backward scan, the
                // first stale entry seen while scanning for key is the
                // first still present in the run.
                // 发现新的垃圾值，将slotToExpunge设置到靠后一点的位置
                if (k == null && slotToExpunge == staleSlot)
                    slotToExpunge = i;
            }

            /**
             * 至此，j指向了一个空槽
             * 如果slotToExpunge == staleSlot，说明在(staleSlot, j)这段没有垃圾值
             * 如果slotToExpunge != staleSlot，有两种可能：
             * 1.slotToExpunge在staleSlot之前最远的垃圾值处
             * 2.slotToExpunge在staleSlot之后最近的垃圾值处
             *
             * 注1：这里的最远最近都是建立在连续元素的基础上讨论的，连续元素的意思是中间没有空槽（但可能有垃圾值）
             *
             * 注2：同上，在此也要想象一个循环链表
             */

            // If key not found, put new entry in stale slot
            tab[staleSlot].value = null;
            tab[staleSlot] = new Entry(key, value);

            // If there are any other stale entries in run, expunge them
            // 清理标记处的垃圾值
            if (slotToExpunge != staleSlot){
                // 从索引slotToExpunge开始，遍历一段【连续】的元素，清理其中的垃圾值，并使各元素排序更紧凑，返回值是那个终止遍历过程的空槽下标
                int stop = expungeStaleEntry(slotToExpunge);

                // 从下标stop开始向后遍历，捎带清理一部分垃圾值，清理过后元素依然是紧凑的
                cleanSomeSlots(stop, len);
            }

        }

        /**
         * Expunge a stale entry by rehashing any possibly colliding entries
         * lying between staleSlot and the next null slot.  This also expunges
         * any other stale entries encountered before the trailing null.  See
         * Knuth, Section 6.4
         *
         * @param staleSlot index of slot known to have null key
         * @return the index of the next null slot after staleSlot
         * (all between staleSlot and this slot will have been checked
         * for expunging).
         */
        /**
         * 从索引staleSlot开始，遍历一段【连续】的元素，清理其中的垃圾值，并使各元素排序更紧凑
         * 返回值是那个终止遍历过程的空槽下标
         *
         * 执行过程：
         * 1. 清理staleSlot中的垃圾值
         * 2. 遍历staleSlot后面的元素，直到遇见Entry数组中的空槽（即tab[i]==null）才停止。遍历过程中：
         * 2.1 清理遇到的垃圾值
         * 2.2 遇到“错位”的元素，将其向前放置在离“理想位置”最近的地方
         *     换句话说，经过2.2的操作后，从“理想位置”出发查找某个元素，只要该元素是存在的，
         *     那么在找到它的过程中，路过的Entry元素是连成一片的。
         *     理解这一点很重要，这是理解set方法的基础之一。
         */
        private int expungeStaleEntry(int staleSlot) {
            Entry[] tab = table;
            int len = tab.length;
            // 索引staleSlot处本身标识的就是一个垃圾值，所以需要首先清理掉
            // expunge entry at staleSlot
            tab[staleSlot].value = null;

            tab[staleSlot] = null;
            size--;
            // 继续往后遍历连续的Entry数组，直到遇见一个空槽后停止遍历
            // Rehash until we encounter null
            Entry e;
            int i;
            for (i = nextIndex(staleSlot, len);
                 (e = tab[i]) != null;
                 i = nextIndex(i, len)) {
                ThreadLocal<?> k = e.get();
                // 如果当前Entry已经不包含ThreadLocal，说明这是个垃圾值，需要清理
                if (k == null) {
                    e.value = null;
                    tab[i] = null;
                    size--;
                } else {
                    // 该ThreadLocal对象的“理想位置”
                    int h = k.threadLocalHashCode & (len - 1);
                    // 遇到“错位”的元素
                    if (h != i) {
                        // 将当前位置置空
                        tab[i] = null;

                        // Unlike Knuth 6.4 Algorithm R, we must scan until
                        // null because multiple entries could have been stale.
                        // 将其向前放置在离“理想位置”最近的地方
                        while (tab[h] != null)
                            h = nextIndex(h, len);
                        // 将该ThreadLocal对象放进去
                        tab[h] = e;
                    }
                }
            }
            return i;
        }

        /**
         * Heuristically scan some cells looking for stale entries.
         * This is invoked when either a new element is added, or
         * another stale one has been expunged. It performs a
         * logarithmic number of scans, as a balance between no
         * scanning (fast but retains garbage) and a number of scans
         * proportional to number of elements, that would find all
         * garbage but would cause some insertions to take O(n) time.
         *
         * @param i a position known NOT to hold a stale entry. The
         * scan starts at the element after i.
         *
         * @param n scan control: {@code log2(n)} cells are scanned,
         * unless a stale entry is found, in which case
         * {@code log2(table.length)-1} additional cells are scanned.
         * When called from insertions, this parameter is the number
         * of elements, but when from replaceStaleEntry, it is the
         * table length. (Note: all this could be changed to be either
         * more or less aggressive by weighting n instead of just
         * using straight log n. But this version is simple, fast, and
         * seems to work well.)
         *
         * @return true if any stale entries have been removed.
         */
        // 从下标i开始向后遍历，清理一部分垃圾值，清理过后元素依然是紧凑的
        private boolean cleanSomeSlots(int i, int n) {
            boolean removed = false;
            Entry[] tab = table;
            int len = tab.length;
            do {
                i = nextIndex(i, len);
                Entry e = tab[i];
                if (e != null && e.get() == null) {
                    n = len;
                    removed = true;
                    // 从索引i开始，遍历一段【连续】的元素，清理其中的垃圾值，并使各元素排序更紧凑，返回值是那个终止遍历过程的空槽下标
                    i = expungeStaleEntry(i);
                }
            } while ( (n >>>= 1) != 0);
            return removed;
        }

        /**
         * Re-pack and/or re-size the table. First scan the entire
         * table removing stale entries. If this doesn't sufficiently
         * shrink the size of the table, double the table size.
         */
        // 扩容并再哈希
        private void rehash() {
            // 再次清理表中所有垃圾值
            expungeStaleEntries();

            // Use lower threshold for doubling to avoid hysteresis
            if (size >= threshold - threshold / 4)
                resize();
        }

        /**
         * Double the capacity of the table.
         */
        // 加倍扩容
        private void resize() {
            Entry[] oldTab = table;
            int oldLen = oldTab.length;
            int newLen = oldLen * 2;
            Entry[] newTab = new Entry[newLen];
            int count = 0;

            for (int j = 0; j < oldLen; ++j) {
                Entry e = oldTab[j];
                if (e != null) {
                    ThreadLocal<?> k = e.get();
                    // 仍然有垃圾值，则标记清理该元素的引用，以便GC回收
                    if (k == null) {
                        e.value = null; // Help the GC
                    } else {
                        // 计算新的“理想位置”
                        int h = k.threadLocalHashCode & (newLen - 1);
                        // 如果发生冲突，使用线性探测往后寻找合适的位置
                        while (newTab[h] != null)
                            h = nextIndex(h, newLen);
                        newTab[h] = e;
                        count++;
                    }
                }
            }
            // 设置新的容量阙值
            setThreshold(newLen);
            size = count;
            table = newTab;
        }

        /**
         * Expunge all stale entries in the table.
         */
        // 清理表中所有垃圾值
        private void expungeStaleEntries() {
            Entry[] tab = table;
            int len = tab.length;
            for (int j = 0; j < len; j++) {
                Entry e = tab[j];
                if (e != null && e.get() == null)
                    // 从索引j开始，遍历一段【连续】的元素，清理其中的垃圾值，并使各元素排序更紧凑
                    expungeStaleEntry(j);
            }
        }
    }
}
