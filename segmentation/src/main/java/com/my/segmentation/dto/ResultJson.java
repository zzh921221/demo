package com.my.segmentation.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("响应视图")
public class ResultJson<T> {
    @ApiModelProperty("响应码：200正常；401无权限；500异常")
    private Integer code;
    @ApiModelProperty("响应消息")
    private String message;
    @ApiModelProperty("返回的数据")
    private T data;

    public ResultJson() {
    }

    public void setStatus(ResultStatus resultStatus) {
        this.code = resultStatus.getCode();
        this.message = resultStatus.getMessage();
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
