package com.skyfin.baidumapapi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.skyfin.baidumapapi.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

     TextView mHelloTvw ;
     TextView mLocationTvw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化百度sdk
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        mHelloTvw = (TextView) findViewById(R.id.hello_tvw);
        mLocationTvw = (TextView) findViewById(R.id.location_tvw);
        mHelloTvw.setOnClickListener(this);
        mLocationTvw.setOnClickListener(this);
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
            case R.id.location_tvw:
                intent.setClass(getApplicationContext(),LocationActivity.class);
                startActivity(intent);
                break;
        }
    }
}
