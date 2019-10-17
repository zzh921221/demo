package com.my.segmentation.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author: zhaozhonghui
 * @create: 2019-06-11 11:39
 */

@ApiModel("分页api参数")
@ToString
public class PageParamsDTO {
    @ApiModelProperty(
            value = "页码",
            required = true
    )
    private Integer pageNo;
    @ApiModelProperty(
            value = "页大小",
            required = true
    )
    private Integer pageSize;
    @ApiModelProperty("开始的索引")
    private Integer startRow;

    public Integer getPageNo() {
        if (null == this.pageNo) {
            this.pageNo = 1;
        }
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        if (this.pageSize == 0){
            this.pageSize = 10;
        }
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getStartRow() {
        return (getPageNo()-1) * getPageSize();
    }

    public void setStartRow(Integer startRow) {
        this.startRow = startRow;
    }
}

