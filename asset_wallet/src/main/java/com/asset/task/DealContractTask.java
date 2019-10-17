package com.asset.task;

import com.asset.bo.DealContractBO;
import com.asset.config.ApplicationContextProvider;
import com.asset.service.DealContractService;
import com.asset.utils.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 交易合约task
 *
 * @author: zhaozhonghui
 * @create: 2019-06-25 10:37
 */
@Slf4j
@Component
public class DealContractTask implements Runnable {
    private DealContractService dealContractService;

    public DealContractTask(){
        this.dealContractService = ApplicationContextProvider.getBean(DealContractService.class);
    }

    @Override
    public void run() {
        int count = 1;
        int i = 0;
        long begin = System.currentTimeMillis();
        log.info("++++++++++++++++++mongo开始执行+++++++++++++++");
        List<DealContractBO> list = new ArrayList<>();
        while (true) {
            if (i >= 5) {
                Thread.yield();
                i=0;
            }
            if (count > 100000) {
                if (!list.isEmpty() && list.size() < 1000) {
                    dealContractService.saveDealContract(list);
                    list.clear();
                }

                long end = System.currentTimeMillis();
                long time = (end - begin) / 1000;
                log.info("++++++++++++++++++++++mongo执行结束，花费:{}秒+++++++++++++++++",time);
                return;
            }

            DealContractBO dealContractBO = new DealContractBO();
            dealContractBO.setContractId(String.valueOf(new IdWorker().nextId())+"_"+Thread.currentThread().getId());
            list.add(dealContractBO);
            if (list.size() >= 1000) {
                dealContractService.saveDealContract(list);
                list.clear();
            }
            log.info("**************计数:{}******************",count);

            i++;
            count++;
        }



    }
}
