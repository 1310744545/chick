package com.chick.jvm.classLoader;

import org.apache.poi.ss.formula.functions.T;

import java.io.FileNotFoundException;

/**
 * @ClassName CustomClassLoader
 * @Author xiaokexin
 * @Date 2021/12/23 21:06
 * @Description 自定义classLoader
 * @Version 1.0
 */
public class CustomClassLoader extends ClassLoader{

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException{

        try {
            byte[] result = getClassFromCustomPath(name);
            if (result == null){
                throw new FileNotFoundException();
            } else {
                return defineClass(name, result, 0, result.length);
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
        throw new ClassNotFoundException(name);
    }

    private byte[] getClassFromCustomPath(String name){
        //从自定义路径中加载指定类：细节略
        //如果指定路径的字节码文件进行了加密，则需要再次放啊中进行解密操作
        return null;
    }
}
