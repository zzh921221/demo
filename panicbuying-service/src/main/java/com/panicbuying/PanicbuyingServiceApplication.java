package com.panicbuying;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author zhaozhonghui
 */
@SpringBootApplication(scanBasePackages = {"com.panicbuying"})
@MapperScan("com.panicbuying.mapper")
@EnableEurekaClient
@EnableAdminServer
public class PanicbuyingServiceApplication {
    @Autowired
    private RestTemplateBuilder restTemplateBuilder;
    public static void main(String[] args) {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(PanicbuyingServiceApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(){
        return restTemplateBuilder.build();
    }

}
