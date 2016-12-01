package com.open.lee.myhttpframework;

import org.apache.http.HttpEntity;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lee on 2016/11/27.
 */

public class Response {

    private Map<String, String> mHeadersMap = new HashMap<>();

    private int stateCode;
    private String message;

    private byte[] rawData = new byte[0];

    public Response(int code, String reason) {
        this.stateCode = code;
        this.message = reason;
    }

    public void setRawDataDirectly(byte[] bytes) {
        rawData = bytes;
    }

    public byte[] getRawData() {
        return rawData;
    }

    public int getStatusCode() {
        return stateCode;
    }

    public String getMessage() {
        return message;
    }

    public void addHeader(String key, String value){
        mHeadersMap.put(key, value);
    }
}
