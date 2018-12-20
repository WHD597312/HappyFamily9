package com.xr.exception.handlers;

import android.app.Activity;

import com.xr.exception.IHandlerException;
import com.xr.exception.WindowManagerGlobal;

public class EndCurrenPagerHandler implements IHandlerException {
    @Override
    public boolean handler(Throwable e) {
        Activity currenActivity = WindowManagerGlobal.getInstance().getCurrenActivity();
        if (currenActivity != null) {
            currenActivity.finish();
        }
        return false;
    }
}
