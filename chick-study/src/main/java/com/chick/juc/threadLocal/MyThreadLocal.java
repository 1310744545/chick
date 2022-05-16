package com.chick.juc.threadLocal;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName MyThreadLocal
 * @Author xiaokexin
 * @Date 2022/4/4 21:08
 * @Description MyThreadLocal
 * @Version 1.0
 */
public class MyThreadLocal {
    ThreadLocal<String> localName = new ThreadLocal<>();
    ThreadLocal<String> localAge = new ThreadLocal<>();

    public String getName() {
        return localName.get();
    }

    public void setName(String name) {
        localName.set(name);
    }

    public String getAge() {
        return localAge.get();
    }

    public void setAge(String age) {
        localAge.set(age);
    }

    public static void main(String[] args) {
        ReentrantLock reentrantLock = new ReentrantLock();
        StringBuilder stringBuilder = new StringBuilder();
        BlockingQueue<String> objects = new ArrayBlockingQueue<>(20);
        reentrantLock.lock();
        new CountDownLatch(11);
        MyThreadLocal test = new MyThreadLocal();
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                test.setName(Thread.currentThread().getName());
                test.setAge(Thread.currentThread().getName());
                System.out.println("-----------------");
                System.out.println(Thread.currentThread().getName() + "--姓名->" + test.getName());
                System.out.println(Thread.currentThread().getName() + "--年龄->" + test.getAge());
            }, i + "").start();
        }
    }
}
