package com.asset.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 运行时上下文提供者
 * @author shengyao
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {
    /**
     * 上下文对象实例
     */
    private static ApplicationContext applicationContext;

    /**
     * 重写父类设置上下文方法
     * @param applicationContext 上下文
     * @throws BeansException
     * @author shengyao
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextProvider.applicationContext = applicationContext;
    }

    /**
     * 获取上下文
     * @return 返回上下文
     * @author shengyao
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 通过name获取 Bean.
     * @param name Bean的名称
     * @return 返回Bean对象
     * @author shengyao
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * 通过class获取Bean.
     * @param clazz 创建Bean的类
     * @param <T> 泛型
     * @return 返回Bean对象
     * @author shengyao
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     * @param name Bean的名称
     * @param clazz 创建Bean的类
     * @param <T> 泛型
     * @return 返回Bean对象
     * @author shengyao
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }
}
