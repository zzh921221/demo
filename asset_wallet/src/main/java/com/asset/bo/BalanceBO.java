package com.asset.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 系统账户
 *
 * @author: zhaozhonghui
 * @create: 2019-07-03 18:41
 */
@Data
public class BalanceBO {

    private Integer id;

    private BigDecimal amount;

    private Integer userId;
}
