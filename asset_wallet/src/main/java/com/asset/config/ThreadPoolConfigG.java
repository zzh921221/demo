package com.asset.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 * @author shengyao
 */
@EnableAsync
@Configuration
public class ThreadPoolConfigG {
    /**
     * 初始化线程池
     * @return 返回线程池执行器
     * @author shengyao
     */
    @Bean("taskExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(30);
        // 设置最大线程数
        executor.setMaxPoolSize(60);
        // 设置缓冲队列
        executor.setQueueCapacity(200);
        // 设置允许线程的空闲时间
        executor.setKeepAliveSeconds(60);
        // 设置线程池名称前缀
        executor.setThreadNamePrefix("taskExecutor-");
        // 设置线程池对拒绝任务的处理策略：此处采用了CallerRunsPolicy策略
        // 当线程池没有处理能力的时候，该策略会直接在execute方法的调用线程中运行被拒绝的任务；
        // 如果执行程序已被关闭，则会丢弃该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁
        // 以确保应用最后能够被关闭，而不是阻塞住
        executor.setAwaitTerminationSeconds(60);
        return executor;
    }
}
