package com.idouban;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Spring Boot 主启动类
 * 配置Spring Boot应用入口
 * 
 * @author iDouban Team
 */
@SpringBootApplication
@MapperScan("com.idouban.mapper")
@ServletComponentScan
@EnableTransactionManagement
public class IDoubanApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(IDoubanApplication.class, args);
        System.out.println("iDouban Application Started Successfully!");
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(IDoubanApplication.class);
    }
}
