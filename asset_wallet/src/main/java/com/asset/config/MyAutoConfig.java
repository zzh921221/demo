package com.asset.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 自动加载类
 *
 * @author: zhaozhonghui
 * @create: 2019-06-27 17:48
 */
@Configuration
public class MyAutoConfig {

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = ApplicationContextProvider.getBean("taskExecutor", ThreadPoolTaskExecutor.class);
        return taskExecutor;
    }
//    @Bean
//    @ConditionalOnMissingBean({PlatformTransactionManager.class})
//    public DataSourceTransactionManager transactionManager(DataSourceProperties properties) {
//        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(this.dataSource);
//        if (this.transactionManagerCustomizers != null) {
//            this.transactionManagerCustomizers.customize(transactionManager);
//        }
//
//        return transactionManager;
//    }

}
