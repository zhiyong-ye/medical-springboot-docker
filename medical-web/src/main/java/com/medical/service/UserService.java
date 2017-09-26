package com.medical.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
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
    
//    @Autowired
//    private JtaTransactionManager transactionManager;
    
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

    public Object saveUser(User user) {
        try {
            this.saveUserOpt(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        System.out.println("productMapper===============");
        
        return "";
    }

    @Transactional
    public void saveUserOpt(User user) {
        userMapper.insert(user);
        System.out.println(user.getId());
        productMapper.insert(user);
        
        
//        UserTransaction userTransaction = transactionManager.getUserTransaction();
//        try {
//            userTransaction.begin();
//            userMapper.insert(user);
//            System.out.println(user.getId());
//            productMapper.insert(user);
//            userTransaction.commit();
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            try {
//                userTransaction.rollback();
//            } catch (Exception e1) {
//                e1.printStackTrace();
//            } 
//        }
        
        
    }
}
