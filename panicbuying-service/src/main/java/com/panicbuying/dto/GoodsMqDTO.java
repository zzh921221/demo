package com.panicbuying.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * mq商品信息 创建订单用
 *
 * @author: zhaozhonghui
 * @create: 2019-04-26 11:33
 */
@Data
@ApiModel("mq商品信息")
public class GoodsMqDTO implements Serializable {
    private static final long serialVersionUID = 5685478495365199804L;
    @ApiModelProperty("商品编码")
    private Long goodsCode;
    @ApiModelProperty("用户ID")
    private Long userId;
}
