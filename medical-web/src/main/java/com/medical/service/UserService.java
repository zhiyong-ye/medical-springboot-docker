package com.medical.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.medical.core.datasource.JtaTransactional;
import com.medical.entity.User;
import com.medical.entity.request.PageRequest;
import com.medical.mapper.ProductMapper;
import com.medical.mapper.UserMapper;

/**
 * UserService
 *
 * @author ZHIYONG.YE
 * @email 773276516@qq.com
 * @date 2017年7月28日 上午10:23:37
 * @version 1.0
 */
@Service
public class UserService {
    
    /**
     * 日志
     */
    private Logger logger = LoggerFactory.getLogger(UserService.class);
    
    /**
     * 注入userMapper
     */
    @Autowired
    private UserMapper userMapper;
    
    /**
     * 注入productMapper
     */
    @Autowired
    private ProductMapper productMapper;
    
    /**
     * 注入redis操作类
     */
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    
    /**
     * 查询用户列表
     * @param pageRequest 
     * @return
     */
    public List<User> queryUserList(PageRequest pageRequest) {
        
        //使用分页参数
        User user = new User();
        user.setPageNum(pageRequest.getPageNum());
        user.setPageSize(pageRequest.getPageSize());
        logger.debug("debug日志:分页查询用户列表信息:");
        logger.info("info日志:分页查询用户列表信息:");
        logger.warn("warn日志:分页查询用户列表信息:");
        logger.error("error日志:分页查询用户列表信息:");
        List<User> userList = userMapper.select(user);
        
        String userRedisKey = "userList";
        
        stringRedisTemplate.opsForValue().set(userRedisKey, JSONObject.toJSONString(userList));
        stringRedisTemplate.expire(userRedisKey, 30, TimeUnit.DAYS);
        
        logger.info("userRedisKey:" + userRedisKey + " userRedisValue:" + stringRedisTemplate.opsForValue().get(userRedisKey));
        
        return userList;
    }

    /**
     * 根据用户id,查询用户信息
     * @param id
     * @return
     */
    public User queryUserById(Long id) {
        User user = new User();
        user.setId(1L);
        return userMapper.selectOne(user);
    }

    /**
     * 注解@Transactional,只能加在外层service,且使用后不能切换数据源,
     * 第二个以后数据库操作，会默认使用第一个数据库操作数据源。同一数据源,事务可以采用。
     * 若想实现jta分布式事务,得采用手动编程式实现,
     * @param user
     * @return
     */
//    @Transactional 
    @JtaTransactional
    public Object saveUser(User user) {
        this.saveUserOpt(user);
        System.out.println("productMapper===============");
        
        return "";
    }

    public void saveUserOpt(User user) {
        userMapper.insert(user);
        System.out.println(user.getId());
        productMapper.insert(user);
/*        
        //不同数据源,实现jta分布式事务,采用手动编程式
        UserTransaction userTransaction = SpringApplicationUtil.getBean("userTransaction",UserTransaction.class);
        try {
            userTransaction.begin();
            userMapper.insert(user);
            System.out.println(user.getId());
            productMapper.insert(user);
            userTransaction.commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            try {
                userTransaction.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            } 
        }*/
        
    }
}
