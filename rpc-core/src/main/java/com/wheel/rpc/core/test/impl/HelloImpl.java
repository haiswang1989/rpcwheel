package com.wheel.rpc.core.test.impl;

import com.wheel.rpc.core.test.IHello;

public class HelloImpl implements IHello {

    @Override
    public String sayHello(String name) {
        return "hello " + name;
    }

}
