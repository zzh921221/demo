package com.asset.repository;

import com.asset.bo.DealContractBO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

/**
 * 交易合约Mongo
 *
 * @author: zhaozhonghui
 * @create: 2019-06-25 10:33
 */
@Component
public interface DealContractRepository extends MongoRepository<DealContractBO,String> {

}
