package com.medical.core.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.medical.core.datasource.DataSource;
import com.medical.core.datasource.DataSourceType;
import com.medical.core.datasource.DynamicDataSourceContextHolder;

/**
 * DataSourceAspect 多数据源aop
 *
 * @author ZHIYONG.YE
 * @email 773276516@qq.com
 * @date 2017年8月15日 下午4:03:12
 * @version 1.0
 */
@Aspect
@Component
@Order(1)
@ConditionalOnProperty(prefix = "medical", name = "muti-datasource-open", havingValue = "true")
public class DataSourceAspect {
    
    /**
     * 日志记录
     */
    private Logger logger = LoggerFactory.getLogger(DataSourceAspect.class);

    /**
     * 定义切面表达式
     */
    @Pointcut("execution(public * com.medical.mapper.*.*(..))")
    public void dataSourceAspect(){}
    
    /**
     * 
     * @param joinPoint
     */
    @Before("dataSourceAspect()")
    public void doBefore(JoinPoint joinPoint) {
        Object target = joinPoint.getTarget();
        String method = joinPoint.getSignature().getName();

        Class<?>[] classz = target.getClass().getInterfaces();

        Class<?>[] parameterTypes = ((MethodSignature) joinPoint.getSignature())
                        .getMethod().getParameterTypes();
        try {
            DataSourceType dataSourceType = null;
            Method m = classz[0].getMethod(method, parameterTypes);
            //如果方法上的注解是datasourse,则优先使用方法上的数据源注解,否则使用接口上的数据源注解
            if (m != null && m.isAnnotationPresent(DataSource.class)) {
                dataSourceType = m.getAnnotation(DataSource.class).value();
                DynamicDataSourceContextHolder.putDataSource(dataSourceType);
            }else{
                //连接数据库的mapper
                logger.info("mapper: " + classz[0].getName());
                //使用接口上的数据源注解
                DataSource[] dataSources = classz[0].getDeclaredAnnotationsByType(DataSource.class);
                if(dataSources != null && dataSources.length > 0){
                    dataSourceType = dataSources[0].value();
                    logger.info(classz[0].getName() + " annotation: " + dataSourceType.getType());
                    DynamicDataSourceContextHolder.putDataSource(dataSourceType);
                }else{
                    logger.info(classz[0].getName() + " annotation: bus(默认数据源)");
                }
            }
        } catch (Exception e) {
                e.printStackTrace();
                logger.info("==============DataSourceAspect Exception================");
        }
    }
}
