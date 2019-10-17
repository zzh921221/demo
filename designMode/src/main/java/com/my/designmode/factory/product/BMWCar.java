package com.my.designmode.factory.product;

import com.my.designmode.factory.Car;
import com.my.designmode.factory.base.CarBase;

import java.time.LocalDate;

/**
 * 宝马
 *
 * @author: zhaozhonghui
 * @create: 2019-06-03 16:26
 */
public class BMWCar implements CarBase {
    @Override
    public Car makeCar() {
        Car car = new Car();
        car.setName("宝马");
        car.setMakeDate(LocalDate.now());
        return car;
    }
}
