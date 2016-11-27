package com.open.lee.myhttpframework.httpstack;

import android.util.Log;

import com.open.lee.myhttpframework.Request;
import com.open.lee.myhttpframework.Response;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * Created by Lee on 2016/11/27.
 */

public class URLConnHttpStack implements HttpStack{

    @Override
    public Response performRequest(Request<?> request) {
        HttpURLConnection connection = null;
        try {
            connection = createURLConnection(request.getUrl());
            setRequestHeaders(connection, request);
            setRequestBodyParams(connection, request);
            return fetchResponse(connection);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(connection != null){
                connection.disconnect();
            }
        }
        return null;
    }

    private HttpURLConnection createURLConnection(String url) throws IOException{
        URL newUrl = new URL(url);
        URLConnection urlConnection = newUrl.openConnection();
        urlConnection.setConnectTimeout(10000);
        urlConnection.setReadTimeout(10000);
        urlConnection.setDoInput(true);
        urlConnection.setUseCaches(false);
        return (HttpURLConnection) urlConnection;
    }

    private void setRequestHeaders(HttpURLConnection connection, Request<?> request){
        for (Map.Entry<String, String> entry : request.getHeaders().entrySet()){
            connection.addRequestProperty(entry.getKey(), entry.getValue());
        }
    }

    private void setRequestBodyParams(HttpURLConnection connection, Request<?> request)
    throws ProtocolException, IOException{
        Request.HttpMethod method = request.getHttpMethod();
        connection.setRequestMethod(method.toString());
        byte[] body = request.getBody();
        if(body != null){
            connection.setDoOutput(true);
            connection.addRequestProperty(Request.HEADER_CONTENT_TYPE,
                    request.getBodyContentType());
            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            //隐含调用connect建立TCP连接
            dataOutputStream.write(body);
            dataOutputStream.close();
        }
    }

    private Response fetchResponse(HttpURLConnection connection) throws IOException{
        ProtocolVersion protocolVersion = new ProtocolVersion("HTTP", 1, 1);
        int responseCode = connection.getResponseCode();
        if (responseCode == -1){
            throw new IOException("connection response code is -1!");
        }

        StatusLine responseStatue = new BasicStatusLine(protocolVersion, connection.getResponseCode(),
                connection.getResponseMessage());
        Response response = new Response(responseStatue);
        Log.d("test", "ResponseStatus: " + responseCode + "\n" + responseStatue);
        response.setEntity(entityFromURLConnection(connection));
        addHeadersToResponse(response, connection);
        return response;
    }

    private HttpEntity entityFromURLConnection(HttpURLConnection connection){
        BasicHttpEntity entity = new BasicHttpEntity();
        InputStream inputStream = null;
        try {
            inputStream = connection.getInputStream();
        } catch (IOException e){
            e.printStackTrace();
            inputStream = connection.getErrorStream();
        }

        // TODO : GZIP
        entity.setContent(inputStream);
        entity.setContentLength(connection.getContentLength());
        entity.setContentEncoding(connection.getContentEncoding());
        entity.setContentType(connection.getContentType());

        return entity;
    }

    private void addHeadersToResponse(BasicHttpResponse response, HttpURLConnection connection) {
        for (Map.Entry<String, List<String>> header : connection.getHeaderFields().entrySet()) {
            if (header.getKey() != null) {
                Header h = new BasicHeader(header.getKey(), header.getValue().get(0));
                response.addHeader(h);
            }
        }
    }

}
