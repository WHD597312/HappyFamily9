package com.xr.exception;

import com.xr.exception.handlers.IgnoreHandler;

public class ChildThreadHandlerExceptionFactory implements IHandlerExceptionFactory {
    @Override
    public IHandlerException get(Throwable e) {
        return new IgnoreHandler();
    }
}
