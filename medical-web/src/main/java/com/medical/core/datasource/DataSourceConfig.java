package com.medical.core.datasource;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * DataSourceConfig: DruidDataSource多数据源
 *
 * @author ZHIYONG.YE
 * @email 773276516@qq.com
 * @date 2017年9月1日 上午9:19:07
 * @version 1.0
 */
//@Configuration
public class DataSourceConfig {

    /**
     * 获取数据源类型
     */
    @Value("${mysql.datasource.type}")
    private Class<? extends DataSource> dataSourceType;
    
    /**
     * 业务库:默认主库
     * @return
     */
    @Bean(name = "busDataSource")
    @Primary
    @ConfigurationProperties(prefix = "mysql.datasource.bus")
    public DataSource busDataSource() {
        System.out.println("=====================busDataSource init======================");
        //创建druid数据源
        return DataSourceBuilder.create().type(dataSourceType).build();
    }
    
    /**
     * 支付库
     * @return
     */
    @Bean(name = "payDataSource")
    @ConfigurationProperties(prefix = "mysql.datasource.pay")
    public DataSource payDataSource() {
        System.out.println("=====================payDataSource init======================");
        //创建druid数据源
        return DataSourceBuilder.create().type(dataSourceType).build();
    }
    
    /**
     * 用户库
     * @return
     */
    @Bean(name = "userDataSource")
    @ConfigurationProperties(prefix = "mysql.datasource.user")
    public DataSource userDataSource() {
        System.out.println("=====================userDataSource init======================");
        //创建druid数据源
        return DataSourceBuilder.create().type(dataSourceType).build();
    }
    
}
