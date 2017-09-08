package com.medical.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * SpringApplicationUtil
 *
 * @author ZHIYONG.YE
 * @email 773276516@qq.com
 * @date 2017年9月1日 上午10:49:37
 * @version 1.0
 */
@Component
public class SpringApplicationUtil implements ApplicationContextAware {
    
    /**
     * 日志
     */
    private Logger logger = LoggerFactory.getLogger(SpringApplicationUtil.class);
    
    /**
     * 上下文
     */
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(SpringApplicationUtil.applicationContext == null){
            SpringApplicationUtil.applicationContext = applicationContext;
        }
        
        logger.info("============SpringApplicationUtil.applicationContext配置成功=============");
    }
    
    /**
     * 获取applicationContext
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 通过name获取 Bean.
     * @param name
     * @return
     */
    public static Object getBean(String name){
        return getApplicationContext().getBean(name);
    }

    /**
     * 通过class获取Bean.
     * @param clazz
     * @return
     */
    public static <T> T getBean(Class<T> clazz){
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     * @param name
     * @param clazz
     * @return
     */
    public static <T> T getBean(String name,Class<T> clazz){
        return getApplicationContext().getBean(name, clazz);
    }

}
