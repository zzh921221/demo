package com.my.segmentation.mapper;

import com.my.segmentation.domain.AssetWallet;
import com.my.segmentation.dto.AssetWalletReqDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 资产钱包mapper
 *
 * @author: zhaozhonghui
 * @create: 2019-06-04 13:55
 */
public interface AssetWalletMapper {
    /** 根据币种建资产表 */
    void createAssetWalletTable(@Param("tableName") String tableName);
    /** 插入资产表数据 */
    int insertData(@Param("tableName")String tableName, @Param("assetWallet") AssetWallet assetWallet);
    /** 检查表是否存在 */
    List<String> checkTable(@Param("tableName")String tableName);
    /** 查询全局表 */
    List<AssetWallet> selectAssetWallet(AssetWalletReqDTO dto);
    /** 分页查询资产表 */
    List<AssetWallet> selectAssetWalletPage(@Param("assetWallet") AssetWalletReqDTO dto,@Param("tableName")String tableName);
    /** 根据ID范围查询资产表 */
    List<AssetWallet> selectAssetWalletById(@Param("assetWallet") AssetWalletReqDTO dto,@Param("tableName")String tableName,@Param("minId")Long minId,@Param("maxId")Long maxId);
    /** 查询资产表数据总记录数 */
    long countAssetWalletPage(@Param("assetWallet") AssetWalletReqDTO dto,@Param("tableName")String tableName);
    /** 批量插入全局表 */
    int batchInsertDataToTotal(List<AssetWallet> list);
    /** 查询全局表数据数（模拟限制xxx条） */
    long selectAssetWalletCount();
    /** 批量删除全局表数据 */
    int batchDeleteDataFromTotal(List<Long> list);
    /** 查询最早的数据id */
    List<Long> selectLastId(@Param("limit") int limit);
    /** 根据表名和ID查找数据 */
    AssetWallet selectByTableAndId(@Param("tableName")String tableName,@Param("id")Long Id);


}
