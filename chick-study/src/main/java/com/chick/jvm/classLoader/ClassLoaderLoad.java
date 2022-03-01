package com.chick.jvm.classLoader;

import sun.misc.Launcher;
import sun.security.ec.CurveDB;

import java.net.URL;
import java.security.Provider;

/**
 * @ClassName ClassLoaderLoad
 * @Author xiaokexin
 * @Date 2021/12/23 20:48
 * @Description 加载器详情
 * @Version 1.0
 */
public class ClassLoaderLoad {

    public static void main(String[] args) {
        System.out.println("*******************引导类加载器*******************");
        //获取bootstrapClassLoader能够加载的api的路径
        URL[] urLs = Launcher.getBootstrapClassPath().getURLs();
        for (URL element: urLs) {
            System.out.println(element.toExternalForm());
        }
        //从上面的路径中随意选择一个类，来看看他的类加载器是什么：引导类加载器
        ClassLoader classLoader = Provider.class.getClassLoader();
        System.out.println(classLoader);

        System.out.println("*******************扩展类加载器*******************");
        String property = System.getProperty("java.ext.dirs");
        for (String path: property.split(";")) {
            System.out.println(path);
        }
        //从上面的路径中随意选择一个类，来看看他的类加载器是什么：扩展类加载器
        ClassLoader classLoaderCurve = CurveDB.class.getClassLoader();
        System.out.println(classLoaderCurve); //扩展类加载器
    }
}
