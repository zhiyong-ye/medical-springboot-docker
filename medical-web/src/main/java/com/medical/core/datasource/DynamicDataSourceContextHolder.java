package com.medical.core.datasource;
/**
 * DynamicDataSourceContextHolder 保存一个线程安全的DatabaseType
 *
 * @author ZHIYONG.YE
 * @email 773276516@qq.com
 * @date 2017年8月15日 下午4:08:56
 * @version 1.0
 */
public class DynamicDataSourceContextHolder {
    
    /**
     * 使用线程变量,保证线程安全
     */
    public static final ThreadLocal<DataSourceType> contextHolder = new ThreadLocal<DataSourceType>();  
    
    public static void putDataSource(DataSourceType datasourceType) {  
        contextHolder.set(datasourceType);  
    }  
  
    public static DataSourceType getDataSouce() {  
        return contextHolder.get();  
    }
}
