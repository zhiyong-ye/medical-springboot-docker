package com.medical.core.config.mybatis;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.medical.constant.SystemConstant;

/**
 * 
 * MybatisNoJtaConfig: mybatis + DruidDataSource单数据源配置
 *
 * @author ZHIYONG.YE
 * @email 773276516@qq.com
 * @date 2017年9月26日 下午1:53:58
 * @version 1.0
 */
@Configuration
@EnableTransactionManagement //开启事务管理器
@MapperScan(basePackages = SystemConstant.BASE_PACKAGE,sqlSessionFactoryRef = "sqlSessionFactory")
@ConditionalOnProperty(prefix = "medical", name = "muti-datasource-open", havingValue = "false")
public class MybatisNoJtaConfig {

    /**
     * 日志记录
     */
    private Logger logger = LoggerFactory.getLogger(MybatisNoJtaConfig.class);

    /**
     * 获取单数据源类型
     */
    @Value("${mysql.datasource.type}")
    private Class<? extends DataSource> dataSourceType;
    
    /**
     * 单数据源
     * @return
     */
    @Bean(name = "singleDataSource")
    @ConfigurationProperties(prefix = "mysql.datasource")
    public DataSource singleDataSource() {
        logger.info("singleDataSource dataSourceType: " + dataSourceType.getName());
        logger.info("=====================singleDataSource init======================");
        //创建druid数据源
        return DataSourceBuilder.create().type(dataSourceType).build();
    }
    
    /**
     * 单数据源事务管理器配置
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean
    public DataSourceTransactionManager transactionManager(@Qualifier("singleDataSource")DataSource singleDataSource) 
            throws Exception {
        return new DataSourceTransactionManager(singleDataSource);
    }
    
    /**
     * 设置单数据源SqlSessionFactory
     * @return
     * @throws Exception
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier("singleDataSource")DataSource singleDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        logger.info("=======singleDataSource: " + singleDataSource.toString() + "=========");
        sessionFactory.setDataSource(singleDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(SystemConstant.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }
    
}
