package com.xr.exception.handlers;

import com.xr.exception.IHandlerException;

public class IgnoreHandler implements IHandlerException {
    @Override
    public boolean handler(Throwable e) {
        return false;
    }
}
