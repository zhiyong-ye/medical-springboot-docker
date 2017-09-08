package com.medical.core.config.mybatis;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.transaction.jta.JtaTransactionManager;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.medical.core.datasource.DataSourceType;
import com.medical.core.datasource.DynamicDataSource;

/**
 * MybatisConfig: mybatis + DruidXADataSource多数据源 + jta
 *
 * @author ZHIYONG.YE
 * @email 773276516@qq.com
 * @date 2017年8月15日 下午5:30:39
 * @version 1.0
 */
@Configuration
@MapperScan(basePackages = MybatisJTAConfig.BASE_PACKAGE,sqlSessionFactoryRef = "sqlSessionFactory")
@EnableTransactionManagement(proxyTargetClass = true) //开启事务管理
public class MybatisJTAConfig {

    //指定包扫描路径
    static final String BASE_PACKAGE = "com.medical.mapper";
    
    //获取扫描mapper文件位置
    static final String MAPPER_LOCATION = "classpath:/mapper/*.xml";
    
    /**
     * 获取单数据源类型
     */
    @Value("${mysql.datasource.type}")
    private Class<? extends DataSource> dataSourceType;
    
    /**
     * 支付库
     * @return
     */
    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "mysql.datasource")
    @ConditionalOnProperty(prefix = "medical", name = "muti-datasource-open", havingValue = "false")
    public DataSource singleDataSource() {
        System.out.println("=====================singleDataSource init======================");
        //创建druid数据源
        return DataSourceBuilder.create().type(dataSourceType).build();
    }
    
    /**
     * 业务库:默认主库
     * @return
     */
    @Bean(name = "busDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.jta.atomikos.datasource.bus")
    @ConditionalOnProperty(prefix = "medical", name = "muti-datasource-open", havingValue = "true")
    public DataSource busDataSource() {
        return new AtomikosDataSourceBean();
    }
    
    /**
     * 支付库
     * @return
     */
    @Bean(name = "payDataSource")
    @ConfigurationProperties(prefix = "spring.jta.atomikos.datasource.pay")
    @ConditionalOnProperty(prefix = "medical", name = "muti-datasource-open", havingValue = "true")
    public DataSource payDataSource() {
        return new AtomikosDataSourceBean();
    }
    
    /**
     * 用户库
     * @return
     */
    @Bean(name = "userDataSource")
    @ConfigurationProperties(prefix = "spring.jta.atomikos.datasource.user")
    @ConditionalOnProperty(prefix = "medical", name = "muti-datasource-open", havingValue = "true")
    public DataSource userDataSource() {
        return new AtomikosDataSourceBean();
    }
    
    /**
     * 设置默认数据源,以及加入多数据源路由,可实现读写分离以及读负载均衡。
     * 必须先初始化多数据源bean,否则在注入动态数据源会形成一个循环依赖
     * 可使用@DependsOn注解在bean上或者@AutoConfigureAfter(DataSourceConfig.class)注解在类上解决
     * @Primary 该注解表示在同一个接口有多个实现类可以注入的时候，默认选择哪一个，而不是让@autowire注解报错
     * @Qualifier 根据名称进行注入，通常是在具有相同的多个类型的实例的一个注入（例如有多个DataSource类型的实例）
     * @DependsOn 强制初始化其他bean
     * @return
     */
    @Bean(name = "dataSource")
    @ConditionalOnProperty(prefix = "medical", name = "muti-datasource-open", havingValue = "true")
    public DataSource dynamicDataSource(@Qualifier("busDataSource")DataSource busDataSource,
            @Qualifier("payDataSource")DataSource payDataSource,
            @Qualifier("userDataSource")DataSource userDataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        //此key必须和determineCurrentLookupKey()配置的值一致
        targetDataSources.put(DataSourceType.bus.getType(), busDataSource);
        targetDataSources.put(DataSourceType.pay.getType(), payDataSource);
        targetDataSources.put(DataSourceType.user.getType(), userDataSource);
        
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        //将多数据源加入路由
        dynamicDataSource.setTargetDataSources(targetDataSources);
        //设置默认的数据源:bus业务库
        dynamicDataSource.setDefaultTargetDataSource(busDataSource);
        return dynamicDataSource;
    }
    
    /**
     * jta事务
     * @return
     * @throws Throwable
     */
    @Bean(name = "userTransaction")
    @ConditionalOnProperty(prefix = "medical", name = "muti-datasource-open", havingValue = "true")
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
    @ConditionalOnProperty(prefix = "medical", name = "muti-datasource-open", havingValue = "true")
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
    @ConditionalOnProperty(prefix = "medical", name = "muti-datasource-open", havingValue = "true")
    public PlatformTransactionManager transactionManager() throws Throwable {
            JtaTransactionManager manager = new JtaTransactionManager(userTransaction(), atomikosTransactionManager());
            manager.setAllowCustomIsolationLevels(true);
            return manager;
    }
    
    /**
     * jta事务拦截器
     * @param platformTransactionManager
     * @return
     */
    @Bean(name = "transactionInterceptor")
    @ConditionalOnProperty(prefix = "medical", name = "muti-datasource-open", havingValue = "true")
    public TransactionInterceptor transactionInterceptor(@Qualifier("transactionManager")PlatformTransactionManager platformTransactionManager) {
            TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
            // 事物管理器
            transactionInterceptor.setTransactionManager(platformTransactionManager);
            Properties transactionAttributes = new Properties();

            // 新增
            transactionAttributes.setProperty("insert*", "PROPAGATION_REQUIRED,-Exception");
            // 新增
            transactionAttributes.setProperty("save*", "PROPAGATION_REQUIRED,-Exception");
            // 新增
            transactionAttributes.setProperty("create*", "PROPAGATION_REQUIRED,-Exception");
            // 修改
            transactionAttributes.setProperty("update*", "PROPAGATION_REQUIRED,-Exception");
            // 删除
            transactionAttributes.setProperty("delete*", "PROPAGATION_REQUIRED,-Exception");
            // 删除
            transactionAttributes.setProperty("remove*", "PROPAGATION_REQUIRED,-Exception");
            // 删除
            transactionAttributes.setProperty("drop*", "PROPAGATION_REQUIRED,-Exception");
            // 查询
            transactionAttributes.setProperty("get*", "PROPAGATION_REQUIRED,-Exception,readOnly");
            // 查询
            transactionAttributes.setProperty("query*", "PROPAGATION_REQUIRED,-Exception,readOnly");
            // 查询
            transactionAttributes.setProperty("select*", "PROPAGATION_REQUIRED,-Exception,readOnly");
            // 查询
            transactionAttributes.setProperty("exist*", "PROPAGATION_REQUIRED,-Exception,readOnly");

            transactionInterceptor.setTransactionAttributes(transactionAttributes);
            return transactionInterceptor;

    }

    /**
     * jta事务代理到ServiceImpl的Bean
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "medical", name = "muti-datasource-open", havingValue = "true")
    public BeanNameAutoProxyCreator transactionAutoProxy() {
            BeanNameAutoProxyCreator transactionAutoProxy = new BeanNameAutoProxyCreator();
            transactionAutoProxy.setProxyTargetClass(true);
            transactionAutoProxy.setBeanNames("atomikosTransactionServiceImpl");
            transactionAutoProxy.setInterceptorNames("transactionInterceptor");
            return transactionAutoProxy;
    }
    
    /**
     * 单数据源事务管理器
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean
    @ConditionalOnProperty(prefix = "medical", name = "muti-datasource-open", havingValue = "false")
    public DataSourceTransactionManager transactionManager(@Qualifier("dataSource")DataSource dataSource) 
            throws Exception {
        return new DataSourceTransactionManager(dataSource);
    }
    
    /**
     * 设置sqlSessionFactory
     * @param dynamicDataSource
     * @return
     * @throws Exception
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource")DataSource dataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(MAPPER_LOCATION));
        return sessionFactory.getObject();
    }
    
}
