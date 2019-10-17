package com.my.segmentation.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * id和表对应关系索引表
 *
 * @author: zhaozhonghui
 * @create: 2019-06-14 14:59
 */
@Data
@ApiModel("资产钱包索引")
public class CoinIndex {
    @ApiModelProperty("数据ID")
    private Long id;
    @ApiModelProperty("币种类型--对应分表")
    private Byte coinType;
    @ApiModelProperty("用户ID")
    private Integer userId;
}
