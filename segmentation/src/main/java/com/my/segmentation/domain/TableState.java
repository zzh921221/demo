package com.my.segmentation.domain;

import lombok.Data;

/**
 * 表状态
 *
 * @author: zhaozhonghui
 * @create: 2019-06-19 18:33
 */
@Data
public class TableState {

    private Integer id;

    private String tableName;

    private Integer count;

    private Integer max;

    private Integer min;
}
