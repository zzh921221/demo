package com.asset.service;

import com.asset.bo.DealContractBO;
import com.asset.repository.BalanceRepository;
import com.asset.repository.DealContractRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;

import java.util.List;

/**
 * 交易合约service
 *
 * @author: zhaozhonghui
 * @create: 2019-06-25 10:35
 */
@Slf4j
@Service
public class DealContractService {
    @Autowired
    private DealContractRepository dealContractRepository;

    /**
     * 储存交易合约对象到mongo
     *
     * @param
     * @return void
     * @author 赵中晖
     * @date 2019/6/25
     */
    public void saveDealContract(List<DealContractBO> dealContractBO) {
        dealContractRepository.insert(dealContractBO);
    }

}
