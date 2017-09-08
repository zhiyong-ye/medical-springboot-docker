package com.medical.mapper;

import org.apache.ibatis.annotations.Param;

import com.medical.core.mapper.BaseMapper;
import com.medical.entity.User;

/**
 * LoggingEventMapper
 *
 * @author ZHIYONG.YE
 * @email 773276516@qq.com
 * @date 2017年7月28日 上午10:07:51
 * @version 1.0
 */
public interface LoggingEventMapper extends BaseMapper<User> {
    
    /**
     * 创建日志表
     * @param tableName
     * @return
     */
    int createLoggingEventTable();
    
    /**
     * 删除日志表
     * @param tableName
     * @return
     */
    int dropTable(@Param("tableName") String tableName);
    
    /**
     * 查询是否存在日志表
     * @param tableName
     * @return
     */
    int existTable(@Param("tableName") String tableName);

}
