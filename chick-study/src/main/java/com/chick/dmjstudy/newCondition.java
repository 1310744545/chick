package com.chick.dmjstudy;

import com.sun.javafx.logging.PulseLogger;

import java.util.ArrayList;
import java.util.concurrent.locks.*;

/**
 * @ClassName newCondition
 * @Author xiaokexin
 * @Date 2022/4/2 16:54
 * @Description newCondition
 * @Version 1.0
 */
public class newCondition {

    static Lock lock = new ReentrantLock();
    static ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    static Condition conditionA = lock.newCondition();
    static Condition conditionB = lock.newCondition();

    public static void threadA(){
        try {
            for (int i = 0; i<100; i++){
                lock.lock();
                System.out.println("A");
                conditionA.await();
            }
        } catch (Exception e){

        } finally {
            conditionB.signal();
            lock.unlock();
        }
    }

    public static void threadB(){
        try {
            lock.lock();
            System.out.println("B");
            conditionB.await();
        } catch (Exception e){

        } finally {
            conditionA.signal();
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        ArrayList<Object> objects = new ArrayList<>(10);
        new Thread(()->{
            for (int i = 0; i<100; i++){
                threadA();
            }
        }).start();
        new Thread(()->{
            for (int i = 0; i<100; i++){
                threadB();
            }
        }).start();
    }
}
