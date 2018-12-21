package com.xr.exception;

import com.xr.exception.handlers.EndCurrenPagerHandler;
import com.xr.exception.handlers.IgnoreHandler;
import com.xr.exception.handlers.KillAppHandler;

public class HandlerExceptionFactory implements IHandlerExceptionFactory {
    @Override
    public IHandlerException get(Throwable e) {
        if(e instanceof IllegalStateException){
            return new EndCurrenPagerHandler();
        }

        if(e instanceof SecurityException){
            return new KillAppHandler();
        }

        return new IgnoreHandler();
    }
}
