package com.xr.exception;

public interface IHandlerExceptionFactory {

    IHandlerException get(Throwable e);

}
