package com.asset.repository;

import com.asset.bo.BalanceBO;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 系统账户repository
 *
 * @author: zhaozhonghui
 * @create: 2019-07-03 18:43
 */
public interface BalanceRepository extends MongoRepository<BalanceBO,Integer> {
}
