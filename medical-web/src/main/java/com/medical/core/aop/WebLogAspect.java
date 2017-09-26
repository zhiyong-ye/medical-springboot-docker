package com.medical.core.aop;

import java.util.Arrays;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * WebLogAspec web请求aop日志记录
 *
 * @author ZHIYONG.YE
 * @email 773276516@qq.com
 * @date 2017年8月15日 下午2:50:44
 * @version 1.0
 */
/**
 * 使用@Aspect注解将一个java类定义为切面类
        使用@Pointcut定义一个切入点，可以是一个规则表达式，比如下例中某个package下的所有函数，也可以是一个注解等。
        根据需要在切入点不同位置的切入内容
        使用@Before在切入点开始处切入内容
        使用@After在切入点结尾处切入内容
        使用@AfterReturning在切入点return内容之后切入内容（可以用来对处理返回值做一些加工处理）
        使用@Around在切入点前后切入内容，并自己控制何时执行切入点自身的内容
        使用@AfterThrowing用来处理当切入内容部分抛出异常之后的处理逻辑
   @Order(i)注解来标识切面的优先级:
               在切入点前的操作(@Before),按order的值由小到大执行;在切入点后的操作(@After/@AfterReturning)，按order的值由大到小执行
 *
 */
@Aspect
@Component
@Order(10)
public class WebLogAspect {
    
    /**
     * 日志记录
     */
    private Logger logger = LoggerFactory.getLogger(WebLogAspect.class);
    
    /**
     * 解决同步时间问题
     */
    ThreadLocal<Long> startTime = new ThreadLocal<Long>();
    
    /**
     * 定义一个切入点
     * 第一个 * 代表任意修饰符及任意返回值
     * 第二个 * 任意类
     * 第三个 * 代表任意方法
     * .. 匹配任意数量的参数
     */
    @Pointcut("execution(public * com.medical.controller.*.*(..))")
    public void webLog(){}
    
    /**
     * 请求前aop处理
     * @param joinPoint
     */
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        //web请求开始时间
        startTime.set(System.currentTimeMillis());
        
        //接收到请求，记录请求内容
        logger.info("WebLogAspect.doBefore()");
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        
        //记录请求内容
        logger.info("URL : " + request.getRequestURL().toString());
        logger.info("HTTP_METHOD : " + request.getMethod());
        logger.info("IP : " + request.getRemoteAddr());
        logger.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        logger.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));
        logger.info("REQUEST_PARAMS: ");
        Arrays.asList(request.getParameterNames()).forEach((Enumeration<String> enu) -> {
            while (enu.hasMoreElements()) {
                String paramName = (String) enu.nextElement();
                logger.info(paramName + ": " + request.getParameter(paramName));
            }
        });
        
    }
    
    /**
     * 请求后aop处理
     * @param joinPoint
     */
    @AfterReturning("webLog()")
    public void doAfterReturning(JoinPoint joinPoint) {
        //处理完请求,返回内容
        logger.info("WebLogAspect.doAfterReturning()");
        logger.info("耗时(毫秒) : " + (System.currentTimeMillis() - startTime.get()));
    }
    
}
