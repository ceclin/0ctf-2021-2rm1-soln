/*
 * Decompiled with CFR 0.151.
 */
package com.yxxx;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class Gadget
implements InvocationHandler,
Serializable {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            Runtime.getRuntime().exec((String[])args[0]);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

