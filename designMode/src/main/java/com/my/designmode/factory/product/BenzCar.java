package com.my.designmode.factory.product;

import com.my.designmode.factory.base.CarBase;
import com.my.designmode.factory.Car;

import java.time.LocalDate;

/**
 * 奔驰
 *
 * @author: zhaozhonghui
 * @create: 2019-06-03 16:19
 */
public class BenzCar implements CarBase {
    @Override
    public Car makeCar() {
        Car car = new Car();
        car.setName("奔驰");
        car.setMakeDate(LocalDate.now());
        return car;
    }
}
