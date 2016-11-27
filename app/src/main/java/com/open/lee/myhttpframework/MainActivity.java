package com.open.lee.myhttpframework;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.open.lee.myhttpframework.listener.RequestCompleteListener;

public class MainActivity extends AppCompatActivity {
    private TextView showTextView;
    private Button connectButton;

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
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mQueue.addRequest(new StringRequest(Request.HttpMethod.GET, "http://blog.csdn.net/qmhball/article/details/7838989",
                        new RequestCompleteListener<String>() {
                            @Override
                            public void onComplete(int stateCode, String response, String errMsg) {
                                showTextView.setText(response);
                            }
                        }));
            }
        });
    }
}
