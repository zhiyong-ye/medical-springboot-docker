package com.medical.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.medical.constant.ResponseCode;
import com.medical.entity.User;
import com.medical.entity.base.BaseResponse;
import com.medical.entity.request.PageRequest;
import com.medical.service.UserService;

/**
 * UserController 用户控制层
 *
 * @author ZHIYONG.YE
 * @email 773276516@qq.com
 * @date 2017年7月28日 上午10:22:00
 * @version 1.0
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {
    
    /**
     * 注入userService
     */
    @Autowired
    private UserService userService;
    
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public Object queryUserList(PageRequest pageRequest) {
        
        //查询用户列表
        List<User> result = userService.queryUserList(pageRequest);
        
        //响应
        BaseResponse response = new BaseResponse(true, ResponseCode.SUCCESS, result);
        
        return response;
    }
    
    @RequestMapping(value = "/save",method = RequestMethod.GET)
    public Object saveUser(User user) {
        
        //查询用户列表
        Object result = userService.saveUser(user);
        
        //响应
        BaseResponse response = new BaseResponse(true, ResponseCode.SUCCESS, result);
        
        return response;
    }
    
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public ResponseEntity<User> queryUserById(@PathVariable("id") Long id) {
        
        //查询单个用户信息
        User user = userService.queryUserById(id);
        
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

}
