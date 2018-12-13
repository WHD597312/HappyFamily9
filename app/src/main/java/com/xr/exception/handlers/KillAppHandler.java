package com.xr.exception.handlers;

import com.xr.exception.IHandlerException;

public class KillAppHandler implements IHandlerException {
    @Override
    public boolean handler(Throwable e) {
        android.os.Process.killProcess(android.os.Process.myPid());
        return true;
    }
}
