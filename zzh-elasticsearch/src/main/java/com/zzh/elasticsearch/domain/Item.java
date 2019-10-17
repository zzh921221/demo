package com.zzh.elasticsearch.domain;

import lombok.Data;

/**
 * 商品
 *
 * @author: 赵中晖
 * @create: 2019-09-30 11:16
 */
@Data
public class Item {
    private Long id;
    private String title;
    private String category;
    private String brand;
    private Double price;
    private String images;

}
