package com.medical.mapper;

import com.medical.core.datasource.DataSource;
import com.medical.core.datasource.DataSourceType;
import com.medical.core.mapper.BaseMapper;
import com.medical.entity.User;

/**
 * UserMapper
 *
 * @author ZHIYONG.YE
 * @email 773276516@qq.com
 * @date 2017年7月28日 上午10:07:51
 * @version 1.0
 */
@DataSource(DataSourceType.user)
public interface UserMapper extends BaseMapper<User> {

}
