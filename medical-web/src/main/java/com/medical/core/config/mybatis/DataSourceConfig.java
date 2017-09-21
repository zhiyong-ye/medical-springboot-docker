package com.medical.core.config.mybatis;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * DruidXADataSource多数据源与DruidDataSource单数据源配置
 * @author 77327
 *
 */
@Configuration
public class DataSourceConfig {

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
        System.out.println("===========AtomikosDataSourceBean busDataSource init==========");
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
        System.out.println("===========AtomikosDataSourceBean payDataSource init==========");
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
        System.out.println("===========AtomikosDataSourceBean userDataSource init==========");
        return new AtomikosDataSourceBean();
    }
    
    /**
     * 注入busJdbcTemplate
     * @param dataSource
     * @return
     */
    @Bean("busJdbcTemplate")
    @ConditionalOnProperty(prefix = "medical", name = "muti-datasource-open", havingValue = "true")
    public JdbcTemplate busJdbcTemplate(@Qualifier("busDataSource") DataSource dataSource) {
            return new JdbcTemplate(dataSource);
    }

    /**
     * 注入payJdbcTemplate
     * @param dataSource
     * @return
     */
    @Bean("payJdbcTemplate")
    @ConditionalOnProperty(prefix = "medical", name = "muti-datasource-open", havingValue = "true")
    public JdbcTemplate payJdbcTemplate(@Qualifier("payDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
    
    /**
     * 注入userJdbcTemplate
     * @param dataSource
     * @return
     */
    @Bean("userJdbcTemplate")
    @ConditionalOnProperty(prefix = "medical", name = "muti-datasource-open", havingValue = "true")
    public JdbcTemplate userJdbcTemplate(@Qualifier("userDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
