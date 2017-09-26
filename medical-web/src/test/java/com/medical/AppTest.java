package com.medical;


import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.medical.entity.User;
import com.medical.entity.request.PageRequest;
import com.medical.service.UserService;

/**
 * jnuit测试
 * @author 77327
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class AppTest {
    
    @Autowired
    private UserService userService;
    
    @Test
    public void testDemo() throws Exception {
        PageRequest pageRequest = new PageRequest();
        List<User> userList = userService.queryUserList(pageRequest);
        System.out.println("junit测试" + userList.toString());
    }
}