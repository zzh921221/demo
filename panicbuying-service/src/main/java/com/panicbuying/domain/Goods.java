package com.panicbuying.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 商品基础类
 * @author zhaozhonghui
 */
@Data
@ApiModel("商品基础类")
@Document(indexName = "demo_goods",type = "goods",shards = 5,refreshInterval = "-1")
public class Goods {
    @ApiModelProperty(value = "主键id",hidden = true)
    private Long id;
    @ApiModelProperty("商品名称")
    @Field(type = FieldType.Auto,ignoreFields = "goodsName")
    private String goodsName;
    @ApiModelProperty("商品编码")
    private Long goodsCode;
    @ApiModelProperty("商品库存")
    private Integer goodsStock;
}