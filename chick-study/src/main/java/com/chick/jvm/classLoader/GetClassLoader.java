package com.chick.jvm.classLoader;

/**
 * @ClassName GetClassLoader
 * @Author xiaokexin
 * @Date 2021/12/23 11:33
 * @Description 获取类加载器
 * @Version 1.0
 */
public class GetClassLoader {

    public static void main(String[] args) {
        //获取系统类加载器
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        System.out.println(systemClassLoader);

        //获取其上层：扩展类加载器
        ClassLoader extClassLoader = systemClassLoader.getParent();
        System.out.println(extClassLoader);

        //尝试获取其上层：获取不到引导类加载器
        ClassLoader bootstrapClassLoader = extClassLoader.getParent();
        System.out.println(bootstrapClassLoader);

        //对于用户自定义类来说，默认使用系统类加载器进行加载
        ClassLoader classLoader = GetClassLoader.class.getClassLoader();
        System.out.println(classLoader);

        //String类使用引导类加载器进行加载的。----> Java的核心类库都是使用引导类加载器进行加载的。
        ClassLoader classLoaderString = String.class.getClassLoader();
        System.out.println(classLoaderString);
    }
}
