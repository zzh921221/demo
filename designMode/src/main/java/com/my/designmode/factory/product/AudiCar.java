package com.my.designmode.factory.product;

import com.my.designmode.factory.Car;
import com.my.designmode.factory.base.CarBase;

import java.time.LocalDate;

/**
 * 奥迪
 *
 * @author: zhaozhonghui
 * @create: 2019-06-03 16:27
 */
public class AudiCar implements CarBase {
    @Override
    public Car makeCar() {
        Car car = new Car();
        car.setName("奥迪");
        car.setMakeDate(LocalDate.now());
        return car;
    }
}
