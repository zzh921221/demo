package com.my.designmode.factory.factory;

import com.my.designmode.factory.Car;
import com.my.designmode.factory.constant.CarConstant;
import com.my.designmode.factory.product.AudiCar;
import com.my.designmode.factory.product.BMWCar;
import com.my.designmode.factory.product.BenzCar;

/**
 * 汽车工厂
 *
 * @author: zhaozhonghui
 * @create: 2019-06-03 16:29
 */
public class CarFactory {

    public Car build(Byte type) {
        if (CarConstant.CarType.BENZ.equals(type)) {
            return new BenzCar().makeCar();
        } else if (CarConstant.CarType.BMW.equals(type)) {
            return new BMWCar().makeCar();
        } else if (CarConstant.CarType.AUDI.equals(type)) {
            return new AudiCar().makeCar();
        }
        return null;
    }

    public static void main(String[] args) {
        CarFactory factory = new CarFactory();
        Car benz = factory.build(CarConstant.CarType.BENZ);
        System.out.println("benz:" + benz.toString());
        Car bmw = factory.build(CarConstant.CarType.BMW);
        System.out.println("bmw:" + bmw.toString());
        Car audi = factory.build(CarConstant.CarType.AUDI);
        System.out.println("audi:" + audi.toString());
    }
}
