package com.z2011.blogapi.工厂模式;

public class Test {
    public static void main(String[] args) {
        //直接调用方法，需要创建类才能调用
        Commodity commodity = new Commodity();
        commodity.getCommodityName("45665");
        Commodity commodity2 = new Commodity();
        Commodity commodity3 = new Commodity();
        Factory factory = new Factory();

    }

}
