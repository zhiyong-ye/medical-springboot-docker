package com.medical.core.config.mybatis;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

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
@AutoConfigureAfter({ DataSourceConfig.class})
@MapperScan(basePackages = MybatisConfig.BASE_PACKAGE,sqlSessionFactoryRef = "sqlSessionFactory")
public class MybatisConfig {

    //指定包扫描路径
    static final String BASE_PACKAGE = "com.medical.mapper";
    
    //获取扫描mapper文件位置
    static final String MAPPER_LOCATION = "classpath:/mapper/*.xml";

    /**
     * 注入singleDataSource
     */
    @Autowired
    private DataSource singleDataSource;
    
    /**
     * 注入busDataSource
     */
    @Autowired
    private DataSource busDataSource;
    
    /**
     * 注入payDataSource
     */
    @Autowired
    private DataSource payDataSource;
    
    /**
     * 注入userDataSource
     */
    @Autowired
    private DataSource userDataSource;
    
    /**
     * 设置默认数据源,以及加入多数据源路由,可实现读写分离以及读负载均衡。
     * 必须先初始化多数据源bean,否则在注入动态数据源会形成一个循环依赖
     * 可使用@DependsOn注解在bean上或者@AutoConfigureAfter(DataSourceConfig.class)注解在类上解决
     * @Primary 该注解表示在同一个接口有多个实现类可以注入的时候，默认选择哪一个，而不是让@autowire注解报错
     * @Qualifier 根据名称进行注入，通常是在具有相同的多个类型的实例的一个注入（例如有多个DataSource类型的实例）
     * @DependsOn 强制初始化其他bean
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "medical", name = "muti-datasource-open", havingValue = "true")
    public DynamicDataSource dynamicDataSource() {
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
     * 单数据源事务管理器配置
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean
    @ConditionalOnProperty(prefix = "medical", name = "muti-datasource-open", havingValue = "false")
    public DataSourceTransactionManager transactionManager() 
            throws Exception {
        return new DataSourceTransactionManager(singleDataSource);
    }
    
    /**
     * 设置单数据源SqlSessionFactory
     * @return
     * @throws Exception
     */
    @Bean
    @ConditionalOnProperty(prefix = "medical", name = "muti-datasource-open", havingValue = "false")
    public SqlSessionFactory sqlSessionFactory()
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(singleDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(MAPPER_LOCATION));
        return sessionFactory.getObject();
    }
    
    /**
     * 设置多数据源sqlSessionFactory
     * @param dynamicDataSource
     * @return
     * @throws Exception
     */
    @Bean
    @ConditionalOnProperty(prefix = "medical", name = "muti-datasource-open", havingValue = "true")
    public SqlSessionFactory sqlSessionFactory(DynamicDataSource dynamicDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dynamicDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(MAPPER_LOCATION));
        return sessionFactory.getObject();
    }
    
}
