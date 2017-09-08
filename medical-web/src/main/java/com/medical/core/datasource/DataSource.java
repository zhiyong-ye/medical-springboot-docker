package com.medical.core.datasource;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * DataSource 多数据源注解
 *
 * @author ZHIYONG.YE
 * @email 773276516@qq.com
 * @date 2017年8月15日 下午4:00:06
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)//注解会在class字节码文件中存在，在运行时可以通过反射获取到  
@Target({ElementType.METHOD,ElementType.TYPE})//定义注解的作用目标**作用范围字段
@Documented
public @interface DataSource {
    
    /**
     * 数据源值
     * @return
     */
    DataSourceType value();
}
