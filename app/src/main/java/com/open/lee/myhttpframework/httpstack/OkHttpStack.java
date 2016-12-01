package com.open.lee.myhttpframework.httpstack;

import android.util.Log;

import com.open.lee.myhttpframework.Request;
import com.open.lee.myhttpframework.Response;

import org.apache.http.ProtocolVersion;

import java.io.IOException;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by Lee on 2016/11/30.
 */

public class OkHttpStack implements HttpStack{
    private OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    public Response performRequest(Request<?> request) {
        try {
            okhttp3.Request okRequest = createOkRequest(request);

            return fetchResponse(okRequest);
        } catch (IOException e){
            e.printStackTrace();
        }

        return null;
    }

    private okhttp3.Request createOkRequest(Request<?> request){
        okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
        builder.url(request.getUrl());
        setRequestHeaders(builder, request);
        setRequestBody(builder, request);

        return builder.build();
    }

    private void setRequestHeaders(okhttp3.Request.Builder builder, Request<?> request){
        Log.d("test", request.getHeaders().size() + "");
        for (Map.Entry<String, String> entry : request.getHeaders().entrySet()){
            builder.addHeader(entry.getKey(), entry.getValue());
            Log.d("test", entry.getKey()  + ": " + entry.getValue());
        }
    }

    private void setRequestBody(okhttp3.Request.Builder builder, Request<?> request){
        if(request.getBody() != null) {
            builder.addHeader(request.HEADER_CONTENT_TYPE, request.getBodyContentType());
            builder.post(RequestBody.create(MediaType.parse(request.getBodyContentType()), request.getBody()));
        }
    }

    private Response fetchResponse(okhttp3.Request request) throws IOException{
        okhttp3.Response okResponse = okHttpClient.newCall(request).execute();
        Response realResponse = new Response(okResponse.code(), okResponse.message());

        ResponseBody body = okResponse.body();

        realResponse.setRawDataDirectly(body.bytes());
        addHeadersToResponse(okResponse, realResponse);
        Log.d("test", "ResponseStatus: " + okResponse.code() + "\n" + okResponse.message());

        return realResponse;
    }

    private void addHeadersToResponse(okhttp3.Response okResponse, Response realResponse){
        Headers headers = okResponse.headers();
        for (int i = 0; i < headers.size(); i ++){
            realResponse.addHeader(headers.name(i), headers.value(i));
        }
    }
}
