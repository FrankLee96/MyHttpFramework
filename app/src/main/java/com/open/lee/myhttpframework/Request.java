package com.open.lee.myhttpframework;

import com.open.lee.myhttpframework.listener.RequestCompleteListener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lee on 2016/11/27.
 */

public abstract class Request<T> implements Comparable<Request<T>>{

    public static enum HttpMethod {
        GET("GET"),
        POST("POST"),
        PUT("PUT"),
        DELETE("DELETE");

        private String mHttpMethod = "";

        HttpMethod(String method){
            mHttpMethod = method;
        }

        @Override
        public String toString() {
            return mHttpMethod;
        }
    }

    public static enum Priority {
        LOW,
        NORMAL,
        HIGH,
        IMMEDIATE
    }

    private static final String DEFAULT_ENCODING_PARAMS = "UTF-8";

    /**
     * Default Content-type
     */
    public final static String HEADER_CONTENT_TYPE = "Content-Type";

    protected int mSerialNumber = 0;

    protected Priority mPriority = Priority.NORMAL;

    protected boolean isCancel = false;

    private boolean shouldCache = true;

    private RequestCompleteListener<T> mRequestCompleteListener;

    private String mUrl = "";

    HttpMethod mHttpMethod = HttpMethod.GET;

    private Map<String, String> mHeadersMap = new HashMap<>();

    private Map<String, String> mBodyParamsMap = new HashMap<>();

    public Request(HttpMethod method, String url, RequestCompleteListener<T> listener){
        this.mHttpMethod = method;
        this.mUrl = url;
        this.mRequestCompleteListener = listener;
    }

    /**
     *  留给子类具体去写如何解析数据(byte To T)
     * @param response
     * @return
     */
    public abstract T parseResponse(Response response);

    /**
     * 运行在UI线程的Response处理函数
     */
    public void handleResponse(Response response){
        T result = parseResponse(response);
        if(mRequestCompleteListener != null){
            int stateCode = response != null ? response.getStatusCode() : -1;
            String msg = response != null ? response.getMessage() : "unknown err: response is null!";
            mRequestCompleteListener.onComplete(stateCode, result, msg);
        }
    }

    public String getUrl() {
        return mUrl;
    }

    public RequestCompleteListener<T> getRequestListener() {
        return mRequestCompleteListener;
    }

    public int getSerialNumber() {
        return mSerialNumber;
    }

    public void setSerialNumber(int mSerialNum) {
        this.mSerialNumber = mSerialNum;
    }

    public Priority getPriority() {
        return mPriority;
    }

    public void setPriority(Priority mPriority) {
        this.mPriority = mPriority;
    }

    protected String getParamsEncoding() {
        return DEFAULT_ENCODING_PARAMS;
    }

    public String getBodyContentType() {
        return "application/x-www-form-urlencoded; charset=" + getParamsEncoding();
    }

    public HttpMethod getHttpMethod() {
        return mHttpMethod;
    }

    public Map<String, String> getHeaders() {
        return mHeadersMap;
    }

    public Map<String, String> getParams() {
        return mBodyParamsMap;
    }

    public boolean isHttps() {
        return mUrl.startsWith("https");
    }

    /**
     * 该请求是否应该缓存
     *
     * @param shouldCache
     */
    public void setShouldCache(boolean shouldCache) {
        this.shouldCache = shouldCache;
    }

    public boolean shouldCache() {
        return shouldCache;
    }

    public void cancel() {
        isCancel = true;
    }

    public boolean isCanceled() {
        return isCancel;
    }

    public byte[] getBody(){
        if(mBodyParamsMap != null && mBodyParamsMap.size() > 0){
            return encodeBodyParameters(mBodyParamsMap, DEFAULT_ENCODING_PARAMS);
        } else {
            return null;
        }
    }

    /**
     * 把参数转为URL编码的参数串
     * @param params
     * @param encodingParams
     * @return
     */
    private byte[] encodeBodyParameters(Map<String, String> params, String encodingParams){
        StringBuilder encodedParams = new StringBuilder();
        try {
            for(Map.Entry<String, String> entry : params.entrySet()){
                encodedParams.append(URLEncoder.encode(entry.getKey(), encodingParams));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(), encodingParams));
                encodedParams.append('&');
            }
            return encodedParams.toString().getBytes(encodingParams);
        } catch (UnsupportedEncodingException e){
            throw new RuntimeException("Encoding not support: " + encodingParams);
        }
    }

    @Override
    public int compareTo(Request<T> another) {
        Priority anotherPriority = another.getPriority();
        // 如果优先级相等,那么按照添加到队列的序列号顺序来执行
        return mPriority.equals(anotherPriority) ? this.getSerialNumber()
                - another.getSerialNumber()
                : mPriority.ordinal() - anotherPriority.ordinal();
    }

    @Override
    public String toString() {
        return "MyHttpFrameWork request: \n"
                + "URL: " + mUrl;
    }
}
