package com.chick.dmjstudy;

import com.sun.javafx.logging.PulseLogger;

import java.util.*;
import java.util.concurrent.CountDownLatch;
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
        int[] a = new int[10000];
        int[] b = new int[1000000];
        Random rd = new Random();
        for (int i = 0; i < a.length; i++) {
            a[i] = rd.nextInt(1000000);
        }
        for (int j = 0; j < b.length; j++) {
            b[j] = rd.nextInt(1000000);
        }
        long s1 = System.currentTimeMillis();

        ArrayList<Integer> c = new ArrayList<>();
        Arrays.sort(a);
        Arrays.sort(b);
        int i = 0, j = 0;
        try {
            while (true){
                if (a[i] < b[j]){
                    i ++;
                }
                if (a[i] > b[j]){
                    j ++;
                }
                if (a[i] == b[j]){
                    i++;
                    j++;
                    c.add(a[i]);
                }
            }
        } catch (Exception e) {}
        long s2 = System.currentTimeMillis();
        System.out.println(c);
        System.out.println("耗时毫秒" + (s2 - s1));

//        CountDownLatch countDownLatch = new CountDownLatch(1);
//        ArrayList<Object> objects = new ArrayList<>(10);
//        new Thread(()->{
//            for (int i = 0; i<100; i++){
//                threadA();
//            }
//        }).start();
//        new Thread(()->{
//            for (int i = 0; i<100; i++){
//                threadB();
//            }
//        }).start();
    }
}
