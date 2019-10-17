package com.my.segmentation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: zhaozhonghui
 * @create: 2019-06-04 13:50
 */
@SpringBootApplication(scanBasePackages = "com.my.segmentation")
@MapperScan({"com.my.segmentation.mapper"})
public class StartApplication {

    public static void main(String[] args) {
        SpringApplication.run(StartApplication.class,args);
    }
}
