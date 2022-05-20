//package com.chick.util;
//
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.BeanFactory;
//import org.springframework.beans.factory.BeanFactoryAware;
//import org.springframework.beans.factory.BeanNameAware;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.beans.factory.config.BeanPostProcessor;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//import org.springframework.stereotype.Component;
//
///**
// * @ClassName BeanTest
// * @Author xiaokexin
// * @Date 2022-04-26 10:26
// * @Description BeanTest
// * @Version 1.0
// */
//@Component
//public class BeanTest implements BeanNameAware, BeanFactoryAware, ApplicationContextAware, BeanPostProcessor, InitializingBean {
//    @Override
//    public void setBeanName(String s) {
//        System.out.println("这是setBeanName方法-->" + s);
//    }
//
//    @Override
//    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
//        System.out.println("这是setBeanFactory方法-->" + beanFactory);
//    }
//
//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        System.out.println("这是setApplicationContext方法-->" + applicationContext);
//    }
//
//    @Override
//    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//        System.out.println("这是postProcessBeforeInitialization方法-->" + beanName);
//        return bean;
//    }
//
//    @Override
//    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//        System.out.println("这是postProcessAfterInitialization方法-->" + beanName);
//        return bean;
//    }
//
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        System.out.println("这是afterPropertiesSet方法");
//    }
//}
