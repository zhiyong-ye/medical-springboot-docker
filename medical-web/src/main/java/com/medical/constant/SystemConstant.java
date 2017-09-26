package com.medical.constant;

import java.io.Serializable;

/**
 * SystemConstant
 *
 * @author ZHIYONG.YE
 * @email 773276516@qq.com
 * @date 2017年9月26日 下午2:01:53
 * @version 1.0
 */
public class SystemConstant implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8843969071051597386L;
    
    /**
     * mybatis指定包扫描路径
     */
    public static final String BASE_PACKAGE = "com.medical.mapper";
    
    /**
     * mybatis获取扫描mapper文件位置
     */
    public static final String MAPPER_LOCATION = "classpath:/mapper/*.xml";

}
