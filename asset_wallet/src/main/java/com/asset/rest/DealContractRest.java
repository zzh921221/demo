package com.asset.rest;

import com.asset.bo.DealContractBO;
import com.asset.config.ApplicationContextProvider;
import com.asset.service.DealContractService;
import com.asset.task.DealContractTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 交易合约rest
 *
 * @author: zhaozhonghui
 * @create: 2019-06-25 16:01
 */
@Slf4j
@RestController
public class DealContractRest {
    @Autowired
    private DealContractService dealContractService;


    @GetMapping("/save")
    public String save(){
        ThreadPoolTaskExecutor executor = ApplicationContextProvider.getBean("taskExecutor",ThreadPoolTaskExecutor.class);

        for (int i = 0; i < 20; i++) {
            DealContractTask task = new DealContractTask();
            executor.submit(task);
        }

        return "success";
    }
//
//    @PostMapping("/update")
//    public DealContractBO update(@RequestBody DealContractBO bo){
//        return dealContractService.investAmount(bo);
//    }
}
