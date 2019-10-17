package com.my.designmode.factory.base;

import com.my.designmode.factory.Car;

/**
 * 汽车接口
 *
 * @author: zhaozhonghui
 * @create: 2019-06-03 15:30
 */
public interface CarBase {

    /**
     * 汽车生产接口
     * @param
     * @return Car
     * @author 赵中晖
     * @date 2019/6/3
     */
    Car makeCar();
}
