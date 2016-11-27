package com.open.lee.myhttpframework;

import android.util.Log;

import com.open.lee.myhttpframework.cache.Cache;
import com.open.lee.myhttpframework.cache.RequestCache;
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

    private Cache<String, Response> mCache;

    public HttpExecutor(BlockingQueue<Request<?>> queue, HttpStack stack, RequestCache cache){
        mRequestQueue = queue;
        mHttpStack = stack;
        mCache = cache;
    }

    @Override
    public void run() {
        try {
            while (!isStop){
                Request<?> request = mRequestQueue.take();
                if (request.isCanceled()){
                    continue;
                }

                Response response = null;
                if (request.shouldCache() && mCache.get(request.getUrl()) != null){
                    Log.d("test", "Hit!!!");
                    response = mCache.get(request.getUrl());
                } else {
                    response = mHttpStack.performRequest(request);
                    if (request.shouldCache() && isSuccessful(response)){
                        mCache.put(request.getUrl(), response);
                    }
                }
                mResponseDispatcher.dispatchResponse(request, response);
            }
        } catch (InterruptedException e){
            Log.i("", "执行线程: " + Thread.currentThread().toString() + "退出");
        }

    }

    private boolean isSuccessful(Response response){
        return response != null && response.getStatusCode() == 200;
    }

    public void quitExecuting(){
        isStop = true;
        interrupt();
    }
}
