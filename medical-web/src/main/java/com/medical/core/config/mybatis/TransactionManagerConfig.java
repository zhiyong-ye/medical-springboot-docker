package com.medical.core.config.mybatis;

import java.util.Properties;

import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.transaction.jta.JtaTransactionManager;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;

/**
 * jta事务配置
 * @author 77327
 *
 */
@Configuration
@EnableTransactionManagement
@ConditionalOnProperty(prefix = "medical", name = "muti-datasource-open", havingValue = "true")
public class TransactionManagerConfig {
    
    /**
     * jta事务
     * @return
     * @throws Throwable
     */
    @Bean(name = "userTransaction")
    public UserTransaction userTransaction() throws Throwable {
            UserTransactionImp userTransactionImp = new UserTransactionImp();
            userTransactionImp.setTransactionTimeout(10000);
            return userTransactionImp;
    }

    /**
     * jta事务
     * @return
     * @throws Throwable
     */
    @Bean(name = "atomikosTransactionManager", initMethod = "init", destroyMethod = "close")
    public TransactionManager atomikosTransactionManager() throws Throwable {
            UserTransactionManager userTransactionManager = new UserTransactionManager();
            userTransactionManager.setForceShutdown(false);
            return userTransactionManager;
    }

    /**
     * jta事务管理器
     * @DependsOn 强制初始化其他bean
     * @return
     * @throws Throwable
     */
    @Bean(name = "transactionManager")
    @DependsOn({ "userTransaction", "atomikosTransactionManager" })
    public PlatformTransactionManager platformTransactionManager() throws Throwable {
            JtaTransactionManager manager = new JtaTransactionManager(userTransaction(), atomikosTransactionManager());
            System.out.println("transactionManager============" + manager.toString() + "========");
            return manager;
    }
    
    /**
     * jta事务拦截器
     * @param platformTransactionManager
     * @return
     */
    @Bean(name = "transactionInterceptor")
    public TransactionInterceptor transactionInterceptor(PlatformTransactionManager platformTransactionManager) {
            TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
            // 事物管理器
            transactionInterceptor.setTransactionManager(platformTransactionManager);
            System.out.println(platformTransactionManager.toString());
            Properties transactionAttributes = new Properties();

            // 新增
            transactionAttributes.setProperty("insert*", "PROPAGATION_REQUIRED,-Throwable");
            // 新增
            transactionAttributes.setProperty("save*", "PROPAGATION_REQUIRED,-Throwable");
            // 新增
            transactionAttributes.setProperty("create*", "PROPAGATION_REQUIRED,-Throwable");
            // 修改
            transactionAttributes.setProperty("update*", "PROPAGATION_REQUIRED,-Throwable");
            // 删除
            transactionAttributes.setProperty("delete*", "PROPAGATION_REQUIRED,-Throwable");
            // 删除
            transactionAttributes.setProperty("remove*", "PROPAGATION_REQUIRED,-Throwable");
            // 删除
            transactionAttributes.setProperty("drop*", "PROPAGATION_REQUIRED,-Throwable");
            // 查询
            transactionAttributes.setProperty("get*", "PROPAGATION_REQUIRED,-Throwable");
            // 查询
            transactionAttributes.setProperty("query*", "PROPAGATION_REQUIRED,-Throwable,readOnly");
            // 查询
            transactionAttributes.setProperty("select*", "PROPAGATION_REQUIRED,-Throwable,readOnly");
            // 查询
            transactionAttributes.setProperty("exist*", "PROPAGATION_REQUIRED,-Throwable,readOnly");

            transactionInterceptor.setTransactionAttributes(transactionAttributes);
            return transactionInterceptor;

    }

    /**
     * jta事务代理到ServiceImpl的Bean
     * @return
     */
    @Bean
    public BeanNameAutoProxyCreator transactionAutoProxy() {
            BeanNameAutoProxyCreator transactionAutoProxy = new BeanNameAutoProxyCreator();
            transactionAutoProxy.setProxyTargetClass(true);
            transactionAutoProxy.setBeanNames("atomikosTransactionServiceImpl");
            transactionAutoProxy.setInterceptorNames("transactionInterceptor");
            return transactionAutoProxy;
    }
    
}
