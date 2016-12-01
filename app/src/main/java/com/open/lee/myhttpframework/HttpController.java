package com.open.lee.myhttpframework;

import com.open.lee.myhttpframework.httpstack.OkHttpStack;
import com.open.lee.myhttpframework.httpstack.URLConnHttpStack;

/**
 * Created by Lee on 2016/11/27.
 */

public class HttpController {

    public static RequestQueue createNewRequestQueue() {
        RequestQueue newQueue = new RequestQueue(new OkHttpStack());
        newQueue.start();
        return newQueue;
    }
}
