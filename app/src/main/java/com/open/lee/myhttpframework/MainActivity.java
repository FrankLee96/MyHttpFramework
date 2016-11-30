package com.open.lee.myhttpframework;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.open.lee.myhttpframework.entity.BigMultipartEntity;
import com.open.lee.myhttpframework.entity.MultipartEntity;
import com.open.lee.myhttpframework.listener.RequestCompleteListener;
import com.open.lee.myhttpframework.request.BigMultipartRequest;
import com.open.lee.myhttpframework.request.MultipartRequest;
import com.open.lee.myhttpframework.request.StringRequest;

import java.io.File;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private TextView showTextView;
    private Button connectButton;
    String MULTIPART_FROM_DATA = "multipart/form-data";

    RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQueue = HttpController.createNewRequestQueue();
        initViews();
    }

    private void initViews(){
        showTextView = (TextView) findViewById(R.id.text_show);
        connectButton = (Button) findViewById(R.id.button_connect);
        final MultipartRequest request = new MultipartRequest("http://192.168.199.143:51416/Face/check/", new RequestCompleteListener<String>() {
            @Override
            public void onComplete(int stateCode, String response, String errMsg) {
                showTextView.setText(response);
            }
        });
        Map<String, String> map = request.getHeaders();
        map.put("connection", "keep-alive");
        map.put("Charset", "UTF-8");
        map.put("Content-Type", MULTIPART_FROM_DATA
                + "; boundary=" + request.getMultiPartEntity().getBoundary());
        MultipartEntity entity = request.getMultiPartEntity();
        entity.addStringPart("test", "okok");
        entity.addFilePart("face", new File("storage/emulated/0/face.jpg"));
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mQueue.addRequest(request);
            }
        });
    }
}
