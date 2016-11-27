package com.open.lee.myhttpframework;

import com.open.lee.myhttpframework.listener.RequestCompleteListener;

/**
 * Created by Lee on 2016/11/27.
 */

public class StringRequest extends Request<String>{

    public StringRequest(HttpMethod method, String url, RequestCompleteListener<String> listener){
        super(method, url, listener);
    }

    @Override
    public String parseResponse(Response response) {
        if (response == null)
            return "null!";
        return new String(response.getRawData());
    }
}
