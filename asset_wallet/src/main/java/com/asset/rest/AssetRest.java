package com.asset.rest;

import com.asset.config.ApplicationContextProvider;
import com.asset.service.AssetService;
import com.asset.task.UpdateAssetTask;
import com.asset.task.UpdateAssetTaskV2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 资产rest
 *
 * @author: zhaozhonghui
 * @create: 2019-06-24 16:01
 */
@Slf4j
@RestController
public class AssetRest {
    @Autowired
    private AssetService assetService;


    @GetMapping("/asset")
    public String updateAsset() {
        ThreadPoolTaskExecutor taskExecutor = ApplicationContextProvider.getBean("taskExecutor", ThreadPoolTaskExecutor.class);

        for (int i = 0; i < 10; i++) {
            UpdateAssetTask task = new UpdateAssetTask();
            task.setUserId("1");
            taskExecutor.submit(new Thread(() -> {

            }));

            task = new UpdateAssetTask();
            task.setUserId("2");
            taskExecutor.submit(task);

            task = new UpdateAssetTask();
            task.setUserId("3");
            taskExecutor.submit(task);
        }

//        for (int i = 0; i < 10; i++) {
//            UpdateAssetTaskV2 task2 = new UpdateAssetTaskV2();
//            task2.setUserId("1");
//            taskExecutor.submit(task2);
//        }

        return "success";
    }

    @GetMapping("/getValue")
    public String getScore() {
        return assetService.getScore();
    }

    @GetMapping("/delete")
    public String deleteKey() {
        assetService.deleteKey();
        return "delete success";
    }

    @GetMapping("/test")
    public String hello() {
        return "asset-wallet-service";
    }
}
