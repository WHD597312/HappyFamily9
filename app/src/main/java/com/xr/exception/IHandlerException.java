package com.xr.exception;

public interface IHandlerException {
    /**
     *
     * @param e 发生的异常
     * @return true 退出  false 继续工作
     */
    boolean handler(Throwable e);
}
