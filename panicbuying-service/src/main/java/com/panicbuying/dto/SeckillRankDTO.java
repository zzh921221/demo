package com.panicbuying.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 抢购排行
 *
 * @author: zhaozhonghui
 * @create: 2019-04-27 15:20
 */
@Data
@ApiModel(value = "抢购榜")
public class SeckillRankDTO {
    @ApiModelProperty("用户id")
    private Long userId;
    @ApiModelProperty("抢购数量")
    private Integer count;
}
