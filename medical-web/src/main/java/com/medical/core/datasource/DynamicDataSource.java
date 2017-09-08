package com.medical.core.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.StringUtils;

/**
 * DynamicDataSource 动态数据源(需要继承AbstractRoutingDataSource)
 *
 * @author ZHIYONG.YE
 * @email 773276516@qq.com
 * @date 2017年8月15日 下午4:27:22
 * @version 1.0
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    /**
     * 获取数据源
     */
    @Override
    protected Object determineCurrentLookupKey() {
        DataSourceType dataSouce = DynamicDataSourceContextHolder.getDataSouce();
        return StringUtils.isEmpty(dataSouce)?null:dataSouce.getType();
    }

}
