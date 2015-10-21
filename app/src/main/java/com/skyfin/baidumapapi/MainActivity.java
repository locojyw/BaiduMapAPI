package com.skyfin.baidumapapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView mHelloTvw ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化百度sdk
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        mHelloTvw = (TextView) findViewById(R.id.hello_tvw);
        mHelloTvw.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent  = new Intent();
        switch (id){
            case R.id.hello_tvw:
                intent.setClass(getApplicationContext(),HelloBaiduMapActivity.class);
                startActivity(intent);
                break;
        }
    }
}
