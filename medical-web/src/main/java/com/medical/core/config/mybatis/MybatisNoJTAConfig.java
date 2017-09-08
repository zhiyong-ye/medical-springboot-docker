package com.medical.core.config.mybatis;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.medical.core.datasource.DataSourceType;
import com.medical.core.datasource.DynamicDataSource;

/**
 * MybatisConfig: mybatis + DruidDataSource多数据源
 *
 * @author ZHIYONG.YE
 * @email 773276516@qq.com
 * @date 2017年8月15日 下午5:30:39
 * @version 1.0
 */
/*@Configuration
@AutoConfigureAfter({DataSourceConfig.class})
@MapperScan(basePackages = MybatisConfigBack.BASE_PACKAGE,sqlSessionFactoryRef = "sqlSessionFactory")*/
public class MybatisNoJTAConfig {

    //指定包扫描路径
    static final String BASE_PACKAGE = "com.medical.mapper";
    
    /**
     * 获取扫描mapper文件位置
     */
    @Value("${mysql.datasource.mapperLocations}")
    private String MAPPER_LOCATION;
    
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
     * 
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
    public DynamicDataSource dynamicDataSource() {
        Map<Object, Object> targetDataSources = new HashMap<>();
        //此key必须和determineCurrentLookupKey()配置的值一致
        targetDataSources.put(DataSourceType.bus.getType(), busDataSource);
        targetDataSources.put(DataSourceType.pay.getType(), payDataSource);
        targetDataSources.put(DataSourceType.user.getType(), userDataSource);
        
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        //将多数据源加入路由
        dynamicDataSource.setTargetDataSources(targetDataSources);
        System.out.println("busDataSource==========" + busDataSource);
        //设置默认的数据源:bus业务库
        dynamicDataSource.setDefaultTargetDataSource(busDataSource);
        return dynamicDataSource;
    }
    
    /**
     * 配置事务管理器
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean
    public DataSourceTransactionManager transactionManager(DynamicDataSource dynamicDataSource) 
            throws Exception {
        return new DataSourceTransactionManager(dynamicDataSource);
    }
    
    
    /**
     * 设置sqlSessionFactory
     * @param dynamicDataSource
     * @return
     * @throws Exception
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(DynamicDataSource dynamicDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dynamicDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(MAPPER_LOCATION));
        return sessionFactory.getObject();
    }
    
}
