package com.zero.test.java.ds.queue;

public class ZeroCircularQueue {

    private int[] values;
    private int head;
    private int tail;
    private int size;
    private int len;

    /**
     * Initialize your data structure here. Set the size of the queue to be k.
     */
    public ZeroCircularQueue(int k) {
        this.values = new int[k];
        this.len = k;
        this.head = 0;
        this.tail = 0;
        this.size = 0;
    }

    /**
     * Insert an element into the circular queue. Return true if the operation is successful.
     */
    public boolean enQueue(int value) {
        if (isFull()) {
            return false;
        }
        if (isEmpty()) {
            this.tail = 0;
        } else {
            this.tail = incrementIndex(tail);

        }
        this.values[tail] = value;
        this.size++;
        return true;
    }

    private int incrementIndex(int index) {
        if (index + 1 == len) {
            return 0;
        }
        return index + 1;
    }

    /**
     * Delete an element from the circular queue. Return true if the operation is successful.
     */
    public boolean deQueue() {
        if (isEmpty()) {
            return false;
        }
        int val = this.values[head];
        head = incrementIndex(head);
        size--;
        return true;
    }

    /**
     * Get the front item from the queue.
     */
    public int Front() {
        if (isEmpty()) {
            return -1;
        }
        return this.values[head];
    }

    /**
     * Get the last item from the queue.
     */
    public int Rear() {
        if (isEmpty()) {
            return -1;
        }
        return this.values[tail];
    }

    /**
     * Checks whether the circular queue is empty or not.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Checks whether the circular queue is full or not.
     */
    public boolean isFull() {
        if (size > 0) {
            if (tail > head) {
                if (tail - head + 1 == len) {
                    return true;
                }
            } else {
                if (tail + len - head + 1 == len) {
                    return true;
                }
            }
        }
        return false;
    }
}
