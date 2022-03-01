package com.chick.jvm.classLoader;

/**
 * @ClassName initiallizationDomp
 * @Author xiaokexin
 * @Date 2021/12/23 0:41
 * @Description initiallizationDomp
 * @Version 1.0
 */
public class InitializationDemo{

    private static int num =1;

    static {
        num = 2;
        number = 20;
    }

    private static int number = 10;
    public static void main(String[] args) {
        System.out.println(InitializationDemo.num);
        System.out.println(InitializationDemo.num);
        System.out.println(InitializationDemo.num);
        System.out.println(InitializationDemo.number);
        System.out.println(InitializationDemo.number);
    }
}
