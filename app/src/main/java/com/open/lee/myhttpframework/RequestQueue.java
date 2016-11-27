package com.open.lee.myhttpframework;

import android.util.Log;

import com.open.lee.myhttpframework.cache.RequestCache;
import com.open.lee.myhttpframework.httpstack.HttpStack;
import com.open.lee.myhttpframework.httpstack.URLConnHttpStack;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Lee on 2016/11/27.
 */

public class RequestQueue {

    private BlockingQueue<Request<?>> mRequestQueue = new PriorityBlockingQueue<Request<?>>();

    private AtomicInteger mSerialNumberGenerator = new AtomicInteger(0);

    private static int DEFAULT_EXECUTOR_NUMBER = Runtime.getRuntime().availableProcessors() + 1;

    private int mExecutorNumber = DEFAULT_EXECUTOR_NUMBER;

    private HttpExecutor[] mExecutors = null;

    private HttpStack mHttpStack;

    private RequestCache mCache = new RequestCache();

    public RequestQueue(HttpStack httpStack){
        mHttpStack = httpStack != null ? httpStack : new URLConnHttpStack();
    }

    private void startHttpExecutors(){
        mExecutors = new HttpExecutor[mExecutorNumber];
        for (int i = 0; i < mExecutorNumber; i++){
            mExecutors[i] = new HttpExecutor(mRequestQueue, mHttpStack, mCache);
            mExecutors[i].start();
        }
    }

    public void start(){
        stop();
        startHttpExecutors();
    }

    public void stop(){
        if (mExecutors != null && mExecutors.length > 0){
            for (int i = 0; i < mExecutorNumber; i++){
                mExecutors[i].quitExecuting();
            }
        }
    }

    public void addRequest(Request<?> request){
        if (!mRequestQueue.contains(request)){
            request.setSerialNumber(generateSerialNumber());
            mRequestQueue.add(request);
        } else {
            Log.d("", "请求队列中已经包含:" + request.toString());
        }
    }

    private int generateSerialNumber(){
        return mSerialNumberGenerator.incrementAndGet();
    }
}
