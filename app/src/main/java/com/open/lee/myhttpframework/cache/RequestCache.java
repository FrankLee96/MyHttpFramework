package com.open.lee.myhttpframework.cache;

import android.util.LruCache;

import com.open.lee.myhttpframework.Response;

/**
 * Created by Lee on 2016/11/27.
 */

public class RequestCache implements Cache<String, Response> {

    private LruCache<String, Response> mCache;

    public RequestCache(){
        final int maxSize = (int) Runtime.getRuntime().maxMemory() / 1024;
        final int cacheSize = maxSize / 5;
        mCache = new LruCache<String, Response>(cacheSize) {
            @Override
            protected int sizeOf(String key, Response value) {
                return value.getRawData().length;
            }
        };
    }

    @Override
    public Response get(String key) {
        return mCache.get(key);
    }

    @Override
    public void put(String key, Response value) {
        mCache.put(key, value);
    }

    @Override
    public void remove(String key) {
        mCache.remove(key);
    }
}
