package com.open.lee.myhttpframework.listener;

/**
 * Created by Lee on 2016/11/27.
 */

public interface RequestCompleteListener<T> {
    void onComplete(int stateCode, T response, String errMsg);
}
