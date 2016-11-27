package com.open.lee.myhttpframework;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;

/**
 * Created by Lee on 2016/11/27.
 */

public class ResponseDispatcher implements Executor{

    private Handler mUIHandler = new Handler(Looper.getMainLooper());

    public void dispatchResponse(final Request<?> request, final Response response){
        Runnable run = new Runnable() {
            @Override
            public void run() {
                request.handleResponse(response);
            }
        };
        execute(run);
    }

    @Override
    public void execute(Runnable runnable) {
        mUIHandler.post(runnable);
    }
}
