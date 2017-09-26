package com.medical;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * AppServletInitializer 
 * 部署到JavaEE容器中,还需修改pom文件打包方式为war,修改spring-boot-starter-web排除自带tomcat插件
 *
 * @author ZHIYONG.YE
 * @email 773276516@qq.com
 * @date 2017年9月25日 下午2:57:27
 * @version 1.0
 */
public class AppServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(App.class);
    }

}
