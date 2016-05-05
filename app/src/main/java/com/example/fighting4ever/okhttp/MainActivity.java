package com.example.fighting4ever.okhttp;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button get;
    Button post;
    TextView textView;
    private static final OkHttpClient client = new OkHttpClient();
    private Handler okHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String s = (String) msg.obj;
            textView.setText(s);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        get = (Button) findViewById(R.id.bt_get);
        post = (Button) findViewById(R.id.bt_post);
        textView = (TextView) findViewById(R.id.textview);
        get.setOnClickListener(this);
        post.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bt_get:
                Log.v("F4ever", "getMethod");
                getMethod();
                break;
            case R.id.bt_post:
                postMethod();
                break;
            default:
                break;
        }
    }

    private void getMethod(){
        final Request request = new Request.Builder().get()
                .tag(this)
                .url("http://www.baidu.com")
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    if (response.isSuccessful()){
                        Headers headers = response.headers();
                        Log.v("F4ever", headers.toString());
                        InputStream in = response.body().byteStream();
                        BufferedReader bfr = new BufferedReader(new InputStreamReader(in));
                        StringBuilder builder = new StringBuilder();
                        String str = "";
                        while ((str = bfr.readLine()) != null){
                            builder.append(str);
                        }
                        Message msg = new Message();
                        msg.obj = builder.toString();
                        okHandler.sendMessage(msg);
                        Log.i("F4ever", "打印Get响应的数据：" + builder.toString());
                    }else {
                        throw new IOException("Unexpected code " + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void postMethod(){
        RequestBody formBody = new FormBody.Builder().add("" ,"")
                .build();
        final Request request = new Request.Builder()
                .url("http://www.wooyun.org")
                .post(formBody)
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    if (response.isSuccessful()){
                        InputStream in = response.body().byteStream();
                        BufferedReader bfr = new BufferedReader(new InputStreamReader(in));
                        StringBuilder builder = new StringBuilder();
                        String str = "";
                        while ((str = bfr.readLine()) != null){
                            builder.append(str);
                        }
                        Log.i("F4ever", "打印Post响应的数据：" + builder.toString());
                        Message msg = new Message();
                        msg.obj = builder.toString();
                        okHandler.sendMessage(msg);
                    }else {

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
