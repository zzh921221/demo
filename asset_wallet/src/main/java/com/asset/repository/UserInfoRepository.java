package com.asset.repository;

import com.asset.bo.UserInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * userInfoRepository
 *
 * @author: zhaozhonghui
 * @create: 2019-06-25 19:42
 */
public interface UserInfoRepository extends MongoRepository<UserInfo,Long> {
    /**
     * 查询id在某个范围内的数据
     * @param
     * @return java.util.List<com.asset.bo.UserInfo>
     */
    List<UserInfo> findUserInfosByIdBetween(long beginIndex,long endIndex);

}
