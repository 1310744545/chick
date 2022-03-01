package com.chick.jvm.heap;

/**
 * @ClassName HeapDemo
 * @Author xiaokexin
 * @Date 2021/12/30 0:18
 * @Description -Xms10m -Xmx10m
 * @Version 1.0
 */
public class HeapDemo {
    public static void main(String[] args) {
        System.out.println("start...");
        try {
            Thread.sleep(1000000);
        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("end...");
    }
}
