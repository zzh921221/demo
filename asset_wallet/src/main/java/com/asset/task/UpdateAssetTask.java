package com.asset.task;

import com.asset.config.ApplicationContextProvider;
import com.asset.service.AssetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author: zhaozhonghui
 * @create: 2019-06-24 18:15
 */
@Slf4j
@Component
public class UpdateAssetTask implements Runnable {
    private AssetService assetService;

    private String userId;

    public UpdateAssetTask() {
        this.assetService = ApplicationContextProvider.getBean(AssetService.class);
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public void run() {
        try {
            int count = 1;

            int i = 1;
            long beginTime = System.currentTimeMillis();
            log.info("===========开始计数：{}==========",beginTime);
            while (true) {
                if (count > 1000000) {
                    long endTime = System.currentTimeMillis();
                    log.info("==========结束计数:{} 经过:{}秒 ==========",endTime,(endTime-beginTime)/1000);
                    return;
                }
                if (i >= 5) {
                    Thread.yield();
                    i = 1;
                }
                assetService.updateAssetAmount(getUserId());
                log.info("+++++++++++++++++++userId:{},计数:{}++++++++++++",userId,count);
                i++;
                count++;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }
}
