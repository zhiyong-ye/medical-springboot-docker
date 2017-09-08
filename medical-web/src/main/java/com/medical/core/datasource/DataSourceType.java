package com.medical.core.datasource;
/**
 * DatasourceType 多数据源枚举类
 *
 * @author ZHIYONG.YE
 * @email 773276516@qq.com
 * @date 2017年8月15日 下午4:11:34
 * @version 1.0
 */
public enum DataSourceType {
    
    bus("bus", "业务库"),
    pay("pay", "支付库"),
    user("user", "用户库");
    
    private String type;
    
    private String name;

    private DataSourceType(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
