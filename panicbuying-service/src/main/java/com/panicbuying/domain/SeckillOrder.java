package com.panicbuying.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单基础类
 * @author zhaozhonghui
 */
@Data
@ApiModel("订单基础类")
public class SeckillOrder {
    @ApiModelProperty(value = "主键ID",hidden = true)
    private Long id;
    @ApiModelProperty("订单号")
    private Long orderCode;
    @ApiModelProperty("商品编码")
    private Long goodsCode;
    @ApiModelProperty("用户id")
    private Long userId;

}