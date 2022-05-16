package com.chick.dmjstudy;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName test12345678910
 * @Author xiaokexin
 * @Date 2022/4/17 20:17
 * @Description test12345678910
 * @Version 1.0
 */
public class test12345678910 implements Runnable {

    static int a = 1;
    ReentrantLock lock = new ReentrantLock();
    Condition condition = lock.newCondition();

    @Override
    public void run() {
        while (true) {
            lock.lock();
            condition.signalAll();
            if (a <= 10) {
                System.out.println(Thread.currentThread().getName() + "----" + a);
                a++;
                try {
                    condition.await();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            } else {
                break;
            }
        }
    }

    public static void main(String[] args) {
        test12345678910 test = new test12345678910();
        Thread thread = new Thread(test, "a");
        Thread thread2 = new Thread(test, "b");
        thread.start();
        thread2.start();
    }
}
