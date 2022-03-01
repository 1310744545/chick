package com.chick.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @ClassName Proxy
 * @Author xiaokexin
 * @Date 2021/12/6 23:23
 * @Description Proxy
 * @Version 1.0
 */

interface Human{
    String getBelief();
    void eat(String food);
}

//被代理类
class SuperMan implements Human{

    @Override
    public String getBelief() {
        return "I believe I can fly";
    }

    @Override
    public void eat(String food) {
        System.out.println("我喜欢吃" + food);
    }
}

/*
    想要实现动态代理，需要解决的问题？
    问题一：如何根据加载到内存中的被代理类，动态的创建一个代理类及其对象
    问题二：当通过代理类的对象调用方法是，如何动态的去调用被代理类中的同名方法
*/

class ProxyFactory{
    //调用此方法，返回一个代理类的对象。解决问题一
    public static Object getProxyInstance(Object obj){
        MyInvocationHandler handler = new MyInvocationHandler();

        handler.bind(obj);

        return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), handler);
    }
}

//仿AOP前置通知和后置通知
class HumanUtil{
    public static void method1(){
        System.out.println("==================================通用方法1==================================");
    }
    public static void method2(){
        System.out.println("==================================通用方法2==================================");
    }
}

class MyInvocationHandler implements InvocationHandler{
    private Object obj;

    public void bind(Object obj){
        this.obj = obj;
    }
    //当我们通过代理类的对象，调用方法a时，就会自动调用如下的方法：invoke()
    //将被代理类要执行的方法a的功能就声明在invoke()中
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //method：即为代理类对象调用的方法，此方法也就作为了被代理类对象调用的方法
        //obj：被代理类的对象
        HumanUtil.method1(); //相当于前置通知，中间的方法是动态的
        Object returnVal = method.invoke(obj, args);
        HumanUtil.method2(); //相当于后置通知，中间的方法是动态的
        //返回只就当作为当前类中invoke()的返回值
        return returnVal;
    }
}

public class ProxyStudy {
    public static void main(String[] args) {
        SuperMan superMan = new SuperMan();
        //proxyInstance：代理类的对象
        Human proxyInstance = (Human) ProxyFactory.getProxyInstance(superMan);
        //当通过代理类对象调用方法时，会自动的调用被代理类中同名的方法
        System.out.println(proxyInstance.getBelief());
        proxyInstance.eat("地三鲜");

        System.out.println("**************************");

        NikeClothFactory nikeClothFactory = new NikeClothFactory();
        ClothFactory proxyClothFactory = (ClothFactory) ProxyFactory.getProxyInstance(nikeClothFactory);
        proxyClothFactory.produceCloth();
    }
}
