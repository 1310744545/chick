package com.chick.juc;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
/**
 * 集合线程安全案例
 */
public class NotSafeDemo {
    /**
     * 多个线程同时对集合进行修改
     * @param args
     */
    public static void main(String[] args) {
        List list = new CopyOnWriteArrayList();
        for (int i = 0; i < 100; i++) {
            new Thread(() ->{
                list.add(UUID.randomUUID().toString());
                System.out.println(list);
            }, "线程" + i).start();
        }
    }
}
