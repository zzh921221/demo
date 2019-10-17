package com.my.designmode.factory;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

/**
 * 汽车接口
 *
 * @author: zhaozhonghui
 * @create: 2019-06-03 15:26
 */
@Data
@ToString
public class Car {

    private String name;

    private LocalDate makeDate;
}
