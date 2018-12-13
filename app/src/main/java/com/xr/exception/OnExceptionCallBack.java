package com.xr.exception;

public interface OnExceptionCallBack {
    void onThrowException(Thread t,Throwable e,IHandlerException handler);
}
