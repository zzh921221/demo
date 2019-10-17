package com.my.segmentation.rest;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.my.segmentation.dto.AssetWalletReqDTO;
import com.my.segmentation.dto.AssetWalletVO;
import com.my.segmentation.service.AssetWalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

/**
 * 资产钱包表现层
 *
 * @author: zhaozhonghui
 * @create: 2019-06-04 15:21
 */
@Slf4j
@RestController
@RequestMapping("/assetWallet")
public class AssetWalletRest {

    @Autowired
    private AssetWalletService assetWalletService;

    @GetMapping("/addCoin")
    public String addCoin(@RequestParam("coinType")Byte coinType){

        assetWalletService.createTable(coinType);
        return "success";
    }

    @PostMapping("/insert")
    public String insert(@RequestBody AssetWalletVO dto){
        assetWalletService.insertDate(dto);
        return "success";
    }

    @PostMapping("/page")
    public List<AssetWalletVO> page(@RequestBody AssetWalletReqDTO dto){
//        if (dto.getCoinId() == null && dto.getCoinInAddress() == null && dto.getId() == null && dto.getUserId() == null){
//
//        return assetWalletService.selectAssetWalletPage(dto);
//        }
        Long begin = System.currentTimeMillis();
        log.info("【开始分页查询】:{}",begin);
        List<AssetWalletVO> assetWalletVOS = assetWalletService.selectAssetWalletPage(dto);
        Long end = System.currentTimeMillis();
        long duringTime = (end - begin)/1000;
        log.info("【结束分页查询】:{}，经过时间(秒):{}",end,duringTime);
        return assetWalletService.selectAssetWalletPage(dto);
    }

    @PostMapping("/search")
    public List<AssetWalletVO> search(@RequestBody AssetWalletReqDTO dto){
        return assetWalletService.selectAssetWallet(dto);
    }

    @GetMapping("/mutil")
    public void mutil(){
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%d").build();
        ExecutorService singleThreadPool = new ThreadPoolExecutor(32, 1000,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        int i = 1;
        while (i < 5000000) {

            AssetWalletVO dto = new AssetWalletVO();
            byte coinType = (byte)(Math.random() * 5 + 1);
            dto.setCoinId((int)coinType);
            dto.setCoinInAddress("银行" + i%20);
            dto.setUserId(i);
            dto.setAmount(new BigDecimal(i));
            dto.setCoinType(coinType);
            dto.setId(Long.valueOf(i));
            if(((ThreadPoolExecutor) singleThreadPool).getActiveCount() < 32){
                singleThreadPool.execute(() -> assetWalletService.insertDate(dto));
                i++;
                log.info("{} 已插入{}条数据",Thread.currentThread().getId(),i);
            }

        }
        log.info("数据插入结束！");
    }
}
