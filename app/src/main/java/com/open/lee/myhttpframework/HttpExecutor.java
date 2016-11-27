package com.open.lee.myhttpframework;

import android.util.Log;

import com.open.lee.myhttpframework.httpstack.HttpStack;

import java.util.concurrent.BlockingQueue;

/**
 * Created by Lee on 2016/11/27.
 */

public class HttpExecutor extends Thread {
    private BlockingQueue<Request<?>> mRequestQueue;

    private HttpStack mHttpStack;

    private ResponseDispatcher mResponseDispatcher = new ResponseDispatcher();

    private boolean isStop = false;

    public HttpExecutor(BlockingQueue<Request<?>> queue, HttpStack stack){
        mRequestQueue = queue;
        mHttpStack = stack;
    }

    @Override
    public void run() {
        try {
            while (!isStop){
                Request<?> request = mRequestQueue.take();
                if (request.isCanceled()){
                    continue;
                }
                Response response = mHttpStack.performRequest(request);
                mResponseDispatcher.dispatchResponse(request, response);
            }
        } catch (InterruptedException e){
            Log.i("", "执行线程: " + Thread.currentThread().toString() + "退出");
        }

    }

    public void quitExecuting(){
        isStop = true;
        interrupt();
    }
}
