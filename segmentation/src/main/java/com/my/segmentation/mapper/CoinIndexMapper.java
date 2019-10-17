package com.my.segmentation.mapper;

import com.my.segmentation.domain.CoinIndex;
import com.my.segmentation.dto.AssetWalletReqDTO;

import java.util.List;

/**
 * 索引表mapper
 *
 * @author: zhaozhonghui
 * @create: 2019-06-14 15:07
 */
public interface CoinIndexMapper {
    /**
     * 插入数据到索引表
     * @param
     * @return int
     * @author 赵中晖
     * @date 2019/6/14
     */
    int insertData(CoinIndex index);
    /**
     * 分页查询索引
     * @param
     * @return java.util.List<com.my.segmentation.domain.CoinIndex>
     * @author 赵中晖
     * @date 2019/6/14
     */
    List<CoinIndex> selectCoinIndexPage(AssetWalletReqDTO dto);
    /**
     * 查询记录总数
     * @param
     * @return long
     * @author 赵中晖
     * @date 2019/6/19
     */
    long selectCountCoinIndex(AssetWalletReqDTO dto);
}
