package com.medical.core.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.medical.mapper.LoggingEventMapper;

/**
 * AppRunnerInit  项目启动初始化数据
 *
 * @author ZHIYONG.YE
 * @email 773276516@qq.com
 * @date 2017年8月18日 下午2:59:01
 * @version 1.0
 */
@Component
@Order(1)//执行顺序,优先级从小到大
public class AppRunnerInit implements CommandLineRunner {
    
    /**
     * 日志
     */
    private Logger logger = LoggerFactory.getLogger(AppRunnerInit.class);
    
    /**
     * 注入LoggingEventMapper
     */
    @Autowired
    private LoggingEventMapper LoggingEventMapper;
    
    /**
     * 初始化数据
     */
    @Override
    public void run(String... arg0) throws Exception {
        // TODO Auto-generated method stub
        logger.info(">>>>>>>>>>项目启动,执行初始化数据等操作<<<<<<<<<<");
    }

}
