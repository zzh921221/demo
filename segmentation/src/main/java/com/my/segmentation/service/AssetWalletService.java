package com.my.segmentation.service;

import com.alibaba.fastjson.JSONObject;
import com.my.segmentation.constant.CoinTypeEnum;
import com.my.segmentation.domain.AssetWallet;
import com.my.segmentation.domain.CoinIndex;
import com.my.segmentation.domain.TableState;
import com.my.segmentation.dto.AssetWalletReqDTO;
import com.my.segmentation.dto.AssetWalletVO;
import com.my.segmentation.dto.ResultJson;
import com.my.segmentation.mapper.AssetWalletMapper;
import com.my.segmentation.mapper.CoinIndexMapper;
import com.my.segmentation.mapper.TableStateMapper;
import com.my.segmentation.utils.BeanMapperUtils;
import com.my.segmentation.utils.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 资产钱包服务层
 *
 * @author: zhaozhonghui
 * @create: 2019-06-04 14:09
 */
@Slf4j
@Service
public class AssetWalletService {

    @Autowired
    private AssetWalletMapper assetWalletMapper;
    @Autowired
    private CoinIndexMapper coinIndexMapper;
    @Autowired
    private TableStateMapper tableStateMapper;

    @Autowired
    private IdWorker idWorker;

    /**
     * 建表接口
     *
     * @param
     * @return void
     * @author 赵中晖
     * @date 2019/6/11
     */
    public void createTable(Byte coinType) {
        String tableName = getTableName(coinType);
        String table = checkTable(tableName);
        if (StringUtils.isNotBlank(table)) {
            throw new RuntimeException("数据表已存在！");
        }
        assetWalletMapper.createAssetWalletTable(tableName);
    }

    /**
     * 插入数据接口
     *
     * @param
     * @return int
     * @author 赵中晖
     * @date 2019/6/11
     */
    public int insertDate(AssetWalletVO dto) {
        String tableName = null;
        if (CoinTypeEnum.BTC.getCode().equals(dto.getCoinType())) {
            tableName = "asset_wallet_btc";
        }
        if (CoinTypeEnum.ELF.getCode().equals(dto.getCoinType())) {
            tableName = "asset_wallet_elf";
        }
        if (CoinTypeEnum.EOS.getCode().equals(dto.getCoinType())) {
            tableName = "asset_wallet_eos";
        }
        if (CoinTypeEnum.QTHM.getCode().equals(dto.getCoinType())) {
            tableName = "asset_wallet_qthm";
        }
        if (CoinTypeEnum.ETH.getCode().equals(dto.getCoinType())) {
            tableName = "asset_wallet_eth";
        }
        String table = checkTable(tableName);
        if (StringUtils.isBlank(table)) {
            throw new RuntimeException("未找到币种相关表结构");
        }
        AssetWallet assetWallet = BeanMapperUtils.map(dto, AssetWallet.class);
        int count = assetWalletMapper.insertData(tableName, assetWallet);
        insertIndex(dto, assetWallet.getId());
        return count;
    }


    public ResultJson<List<AssetWalletVO>> selectAssetWalletPage(AssetWalletReqDTO dto) {
        List<AssetWallet> list = new ArrayList<>();
        List<List<AssetWallet>> firstSearchList = new ArrayList<>();
        List<List<AssetWallet>> secondSearchList = new ArrayList<>();
        List<String> tableList = assetWalletMapper.checkTable("asset_wallet%");
        TableState tableState = tableStateMapper.selectByTableName("coin_index");
        log.info("总记录数:{}", tableState.getCount());
        // 获取总数据条数
//        totalCount = tableList.stream().mapToLong(table -> {
//            Long count = assetWalletMapper.countAssetWalletPage(dto, table);
//            return count;
//        }).sum();
        log.info("表集合:{}", JSONObject.toJSONString(tableList));

        List<AssetWallet> assetWalletList = new ArrayList<>();
        List<CoinIndex> coinIndices = coinIndexMapper.selectCoinIndexPage(dto);
        coinIndices.stream().forEach(coinIndex -> {
            AssetWallet assetWallet = assetWalletMapper.selectByTableAndId(getTableName(coinIndex.getCoinType()), coinIndex.getId());
            assetWalletList.add(assetWallet);
        });

        return BeanMapperUtils.mapList(assetWalletList, AssetWalletVO.class);

//        dto.setPageSize(dto.getPageSize() / tableList.size());

//
//        int index = 0;
//        // 将查询的总数据数平分到每张表上
//        if (startRow != null && startRow != 0) {
//            index = startRow / tableList.size();
//        }
//        dto.setIndex(index);
//        // 第一次查询 会漏数据
//        tableList.stream().forEach(table -> {
//            List<AssetWallet> assetWallets = assetWalletMapper.selectAssetWalletPage(dto, table);
//                firstSearchList.add(assetWallets);
//        });
//
//        // 获取最小和最大id
//        Long minId = getMinId(firstSearchList);
//        Long maxId = getMaxId(firstSearchList);
//        // 第二次查询 因为数据分配不均匀，存在漏数据的问题，从之前的结果中取得最小id和最大id，再将id在这个范围内的几张表的数据进行补充
//        tableList.stream().forEach(table -> {
//            List<AssetWallet> assetWallets = assetWalletMapper.selectAssetWalletById(dto, table, minId, maxId);
//                secondSearchList.add(assetWallets);
//        });
//
//        // 获取最小id的偏移量
//        int offset = 0;
//        List<AssetWallet> dbRecordsList = new ArrayList<>();
//        for (int i = 0; i < secondSearchList.size(); i++) {
//            offset += (index - (secondSearchList.get(i).size() - firstSearchList.get(i).size()));
//            dbRecordsList.addAll(secondSearchList.get(i));
//        }
//
//        // 根据偏移量重新计算minId，
//        int beginIndex = startRow;
//        if (startRow != 0){
//            beginIndex = startRow - offset;
//        }
//        // 将数据根据id排序
//        dbRecordsList.sort(Comparator.comparing(AssetWallet :: getUserId));
//        // 从新的minId开始往后取数据
//        int endIndex = dbRecordsList.size();
//        // 取结束的索引
//        if (beginIndex + dto.getPageSize() < dbRecordsList.size()) {
//            endIndex = beginIndex + dto.getPageSize();
//        }
//        List<AssetWallet> finalList = dbRecordsList.subList(beginIndex, endIndex);
//        List<AssetWalletVO> resultList = BeanMapperUtils.mapList(finalList, AssetWalletVO.class);
//
//        return resultList;
    }

    /**
     * 查询全局表数据
     *
     * @param
     * @return java.util.List<com.my.segmentation.dto.AssetWalletVO>
     * @author 赵中晖
     * @date 2019/6/12
     */
    public List<AssetWalletVO> selectAssetWallet(AssetWalletReqDTO dto) {
        List<AssetWallet> assetWallets = assetWalletMapper.selectAssetWallet(dto);
        // 若是全局表没数据，则从基本表再查
        if (null == assetWallets || assetWallets.isEmpty()) {
            List<AssetWallet> assetWalletList = new ArrayList<>();
            CoinTypeEnum[] values = CoinTypeEnum.values();
            for (CoinTypeEnum value : values) {
                String tableName = getTableName(value.getCode());
                List<AssetWallet> walletList = assetWalletMapper.selectAssetWalletPage(dto, tableName);
                assetWalletList.addAll(walletList);
            }
            if (!assetWalletList.isEmpty()) {
                batchInsertAssetWallet(assetWalletList);
                List<AssetWalletVO> resultList = BeanMapperUtils.mapList(assetWalletList, AssetWalletVO.class);
                return resultList;
            } else {
                return Collections.emptyList();
            }
            //全局表有数据 直接返回
        } else {
            List<AssetWalletVO> resultList = BeanMapperUtils.mapList(assetWallets, AssetWalletVO.class);
            return resultList;
        }
    }

//    ======================================私有方法====================================

    /**
     * 检查数据库表是否存在
     *
     * @param
     * @return java.lang.String
     * @author 赵中晖
     * @date 2019/6/11
     */
    private String checkTable(String tableName) {
        List<String> tables = assetWalletMapper.checkTable(tableName);
        return tables.get(0);
    }

    /**
     * 获取表名
     *
     * @param
     * @return java.lang.String
     * @author 赵中晖
     * @date 2019/6/11
     */
    private String getTableName(Byte coinType) {
        String tableName = null;
        if (CoinTypeEnum.BTC.getCode().equals(coinType)) {
            tableName = "asset_wallet_btc";
        }
        if (CoinTypeEnum.ELF.getCode().equals(coinType)) {
            tableName = "asset_wallet_elf";
        }
        if (CoinTypeEnum.EOS.getCode().equals(coinType)) {
            tableName = "asset_wallet_eos";
        }
        if (CoinTypeEnum.QTHM.getCode().equals(coinType)) {
            tableName = "asset_wallet_qthm";
        }
        if (CoinTypeEnum.ETH.getCode().equals(coinType)) {
            tableName = "asset_wallet_eth";
        }
        return tableName;
    }

    /**
     * 获取最小id
     *
     * @param list
     * @return java.lang.Long
     * @author 赵中晖
     * @date 2019/6/11
     */
    private Long getMinId(List<List<AssetWallet>> list) {
        // ②再取所有表中最小的那个id
        long minId = list.stream().filter(assetWallet -> !assetWallet.isEmpty()).mapToLong(item ->
                //①先取出每个表中最小的id
                item.stream().mapToLong(AssetWallet::getUserId).min().getAsLong()
        ).min().getAsLong();

        return minId;

    }

    /**
     * 获取最大id
     *
     * @param list
     * @return java.lang.Long
     * @author 赵中晖
     * @date 2019/6/11
     */
    private Long getMaxId(List<List<AssetWallet>> list) {
        // ②再取所有表中最大的那个id
        long maxId = list.stream().filter(assetWallet -> !assetWallet.isEmpty()).mapToLong(item ->
                //①先取出每个表中最大的id
                item.stream().mapToLong(AssetWallet::getUserId).max().getAsLong()
        ).max().getAsLong();

        return maxId;
    }

    /**
     * 批量插入全局表
     *
     * @param
     * @return int
     * @author 赵中晖
     * @date 2019/6/12
     */
    private int batchInsertAssetWallet(List<AssetWallet> list) {
        // 模拟全局表最大储存数量
        long size = 10000L;
        long count = assetWalletMapper.selectAssetWalletCount();
        // 若是添加完数据后总数据量超过定值，则将最早的x条数据删掉，x为新插入的数据条数
        if (count != 0 && count + list.size() > size) {
            List<Long> ids = assetWalletMapper.selectLastId(list.size());
            int num = assetWalletMapper.batchDeleteDataFromTotal(ids);
            log.info("删除数据{}条", num);
        }
        // 批量插入全局表
        return assetWalletMapper.batchInsertDataToTotal(list);
    }

    /**
     * 插入数据到索引表
     *
     * @param
     * @return void
     * @author 赵中晖
     * @date 2019/6/14
     */
    private void insertIndex(AssetWalletVO dto, Long id) {
        CoinIndex coinIndex = new CoinIndex();
        coinIndex.setId(id);
        coinIndex.setCoinType(dto.getCoinType());
        coinIndex.setUserId(dto.getUserId());
        coinIndexMapper.insertData(coinIndex);
    }
}
