package com.my.segmentation.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 资产钱包查询类
 *
 * @author: zhaozhonghui
 * @create: 2019-06-04 13:52
 */
@Data
@ApiModel("资产钱包查询类")
public class AssetWalletReqDTO extends PageParamsDTO{
    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "用户id")
    private Integer userId;
    @ApiModelProperty(value = "币种id")
    private Integer coinId;
    @ApiModelProperty(value = "充币地址")
    private String coinInAddress;
    @ApiModelProperty(value = "开始的索引",hidden = true)
    private Integer index;
}
