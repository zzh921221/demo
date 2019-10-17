package com.asset.schedule;

import com.asset.config.ApplicationContextProvider;
import com.asset.task.DealContractTask;
import com.asset.task.UpdateAssetTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * redis定时任务
 *
 * @author: zhaozhonghui
 * @create: 2019-06-26 18:36
 */
@Slf4j
@Component
@EnableScheduling
public class MyRedisSchedule {

//    @Scheduled(cron = "0 0 1 * * ?")
//    public void update3UserAsset(){
//        ThreadPoolTaskExecutor taskExecutor = ApplicationContextProvider.getBean("taskExecutor", ThreadPoolTaskExecutor.class);
//
//        for (int i = 0; i < 10; i++) {
//            UpdateAssetTask task = new UpdateAssetTask();
//            task.setUserId("1");
//            taskExecutor.submit(task);
//
//            task = new UpdateAssetTask();
//            task.setUserId("2");
//            taskExecutor.submit(task);
//
//            task = new UpdateAssetTask();
//            task.setUserId("3");
//            taskExecutor.submit(task);
//        }
//    }

//    @Scheduled(cron = "0 0 5 * * ?")
    public void insertMongo(){
        ThreadPoolTaskExecutor taskExecutor = ApplicationContextProvider.getBean("taskExecutor", ThreadPoolTaskExecutor.class);

        for (int i = 0; i < 20; i++) {
            DealContractTask task = new DealContractTask();
            taskExecutor.submit(task);
        }

    }
}
