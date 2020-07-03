package com.zero.test.java.ds.queue;

import com.sun.tools.javac.util.Assert;

public class Test {

    public static void main(String[] args) {

        ZeroCircularQueue circularQueue = new ZeroCircularQueue(3); // 设置长度为 3
        Assert.check(circularQueue.enQueue(1));
        ;  // 返回 true
        Assert.check(circularQueue.enQueue(2));
        ;  // 返回 true
        Assert.check(circularQueue.enQueue(3));
        ;  // 返回 true
        Assert.check(circularQueue.enQueue(4) == false);
        ;  // 返回 false，队列已满
        Assert.check(circularQueue.Rear() == 3);  // 返回 3
        Assert.check(circularQueue.isFull());
        ;  // 返回 true
        Assert.check(circularQueue.deQueue());
        ;  // 返回 true
        Assert.check(circularQueue.enQueue(4));
        ;  // 返回 true
        Assert.check(circularQueue.enQueue(8) == false);
        ;  // 返回 true
        Assert.check(circularQueue.Rear() == 4);
        ;  // 返回 4
        circularQueue.deQueue();
        circularQueue.deQueue();
        circularQueue.deQueue();
        Assert.check(circularQueue.Rear() == -1);
        ;
    }
}
