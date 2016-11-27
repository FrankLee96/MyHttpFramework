package com.open.lee.myhttpframework.httpstack;

import com.open.lee.myhttpframework.Request;
import com.open.lee.myhttpframework.Response;

/**
 * Created by Lee on 2016/11/27.
 */

public interface HttpStack {
    public Response performRequest(Request<?> request);
}
