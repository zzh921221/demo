package com.my.segmentation.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 资产钱包
 *
 * @author: zhaozhonghui
 * @create: 2019-06-04 13:52
 */
@Data
@ApiModel("资产钱包")
public class AssetWalletVO {
    @ApiModelProperty(value = "id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    @ApiModelProperty(value = "用户id")
    private Integer userId;
    @ApiModelProperty(value = "币种id")
    private Integer coinId;
    @ApiModelProperty(value = "用户余额")
    private BigDecimal amount;
    @ApiModelProperty(value = "用户冻结金额")
    private BigDecimal amountFrozen;
    @ApiModelProperty(value = "充币地址")
    private String coinInAddress;
    @ApiModelProperty(value = "创建时间",hidden = true)
    private Long createTime;
    @ApiModelProperty(value = "修改时间",hidden = true)
    private Long updateTime;
    @ApiModelProperty("币种类型")
    private Byte coinType;
}
