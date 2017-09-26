package com.medical.core.aop;

import java.lang.reflect.Method;

import javax.transaction.UserTransaction;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.medical.core.datasource.JtaTransactional;
import com.medical.core.util.SpringApplicationUtil;

/**
 * 
 * JtaAspect: jta分布式事务-编程式aop实现
 *
 * @author ZHIYONG.YE
 * @email 773276516@qq.com
 * @date 2017年9月26日 下午10:12:34
 * @version 1.0
 */
@Aspect
@Component
@Order(1)
@ConditionalOnProperty(prefix = "medical", name = "muti-datasource-open", havingValue = "true")
public class JtaAspect {
    
    /**
     * 日志记录
     */
    private Logger logger = LoggerFactory.getLogger(JtaAspect.class);
    
    /**
     * 解决事务线程同步问题
     */
    ThreadLocal<UserTransaction> userTransactionThreadLocal = new ThreadLocal<UserTransaction>();

    /**
     * 定义切面表达式
     */
    @Pointcut("execution(public * com.medical.*.*.*(..))")
    public void jtaAspect(){}
    
    /**
     * aop切入点前处理: 获取jta分布式事务对象,放入线程变量中,且开启事务
     * @param joinPoint
     */
    @Before("jtaAspect()")
    public void doBefore(JoinPoint joinPoint) {
        Object target = joinPoint.getTarget();

        try {
            Method[] methods = target.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(JtaTransactional.class)) {
                    //获取jta分布式事务对象,放入线程变量中,且开启事务
                    UserTransaction userTransaction = SpringApplicationUtil.getBean("userTransaction",UserTransaction.class);
                    userTransactionThreadLocal.set(userTransaction);
                    userTransaction.begin();
                    break;
                }
            }
        } catch (Exception e) {
                e.printStackTrace();
                logger.info("==============jtaAspect begin Exception================");
        }
        
    }
    
    /**
     * aop切入中异常处理: 获取线程变量中的jta分布式事务对象,回滚事务
     * @param joinPoint
     */
    @AfterThrowing("jtaAspect()")
    public void doAfterThrowing(JoinPoint joinPoint) {
        Object target = joinPoint.getTarget();
        
        try {
            Method[] methods = target.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(JtaTransactional.class)) {
                    //获取线程变量中的jta分布式事务对象,回滚事务
                    UserTransaction userTransaction = userTransactionThreadLocal.get();
                    userTransaction.rollback();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("==============jtaAspect rollback Exception================");
        }
        
    }
    
    /**
     * aop切入后处理: 获取线程变量中的jta分布式事务对象,且事务对象不为空时,提交事务
     * @param joinPoint
     */
    @AfterReturning("jtaAspect()")
    public void doAfterReturning(JoinPoint joinPoint) {
        Object target = joinPoint.getTarget();
        
        try {
            Method[] methods = target.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(JtaTransactional.class)) {
                    //获取线程变量中的事务对象,且事务对象不为空时,提交事务
                    UserTransaction userTransaction = userTransactionThreadLocal.get();
                    if (userTransaction != null) {
                        userTransaction.commit();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("==============jtaAspect commit Exception================");
        }
        
    }
    
}
