package com.asset.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 交易合约
 * @author shengyao
 */
@Data
public class DealContractBO {
    /**
     * 合约Id
     */
    private String contractId;
    /**
     * 交易对名称（交易币种名称_支付币种名称）
     */
    private String dealPairName;
    /**
     * 买用户ID
     */
    private Integer buyUid;
    /**
     * 买委托单Id
     */
    private Long buyOrderId;
    /**
     * 买委托价格
     */
    private BigDecimal buyEntrustPrice;
    /**
     * 买委托数量
     */
    private BigDecimal buyEntrustAmount;
    /**
     * 卖用户ID
     */
    private Integer sellUid;
    /**
     * 卖委托单Id
     */
    private Long sellOrderId;
    /**
     * 卖委托价格
     */
    private BigDecimal sellEntrustPrice;
    /**
     * 卖委托数量
     */
    private BigDecimal sellEntrustAmount;
    /**
     * 买方是否被动（true被动；false主动）；
     * 用于区分哪一个委托是队列中的委托，便于更新委托队列
     */
    private Boolean buyPassive;
    /**
     * 卖方是否被动（true被动；false主动）；
     * 用于区分哪一个委托是队列中的委托，便于更新委托队列
     */
    private Boolean sellPassive;
    /**
     * 成交价格
     */
    private BigDecimal dealPrice;
    /**
     * 成交数量
     */
    private BigDecimal dealAmount;
    /**
     * 成交时间
     */
    private Long dealTime;
    /**
     * 交易状态 (true 成功；false 失败）
     */
    private Boolean dealStatus;
    /**
     * 合约流转处理状态  (true 成功；false 失败）
     */
    private Boolean disposeStatus;
    /**
     * 合约创建时间
     */
    private Long createTime;
    /**
     * 合约更新时间
     */
    private Long updateTime;
    /**
     * 异常消息 Normal 表示正常
     */
    private String exception = "Normal";
}
