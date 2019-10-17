package com.panicbuying.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 商品返回对象
 *
 * @author: zhaozhonghui
 * @create: 2019-04-28 16:36
 */
@Data
@ApiModel("商品返回对象")
public class GoodsResultDTO implements Serializable {
    private static final long serialVersionUID = 7988911694651374417L;

    @ApiModelProperty(value = "主键id",hidden = true)
    private Long id;
    @ApiModelProperty("商品名称")
    private String goodsName;
    @ApiModelProperty("商品编码")
    private Long goodsCode;
    @ApiModelProperty("商品库存")
    private Integer goodsStock;
}
