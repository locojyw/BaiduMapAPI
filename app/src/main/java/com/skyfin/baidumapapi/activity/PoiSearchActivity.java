package com.skyfin.baidumapapi.activity;

import android.app.Dialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.BusLineOverlay;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.busline.BusLineResult;
import com.baidu.mapapi.search.busline.BusLineSearch;
import com.baidu.mapapi.search.busline.BusLineSearchOption;
import com.baidu.mapapi.search.busline.OnGetBusLineSearchResultListener;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.skyfin.baidumapapi.R;

import java.util.ArrayList;
import java.util.List;

public class PoiSearchActivity extends AppCompatActivity implements View.OnClickListener, OnGetPoiSearchResultListener, OnGetBusLineSearchResultListener {

    private MapView mMapView = null;

    BaiduMap mBaidumap;
    BusLineSearch mBusLineSearch;
    PoiSearch mPoiSearch;

    LatLng mCenterlLatLng = new LatLng(39.886332, 116.416707);

    Button mPoiSearchbtn;
    Button mBusLinebtn;

    //Poi搜索结果
    PoiResult mPoiResult = null;

    String mBusLineUid = null;

    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = getLayoutInflater();
        view = inflater.inflate(R.layout.activity_poi_search, null);
        setContentView(view);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaidumap = mMapView.getMap();
        initSearch();
    }

    private void initSearch() {
        mPoiSearchbtn = (Button) findViewById(R.id.poiSearch);
        mBusLinebtn = (Button) findViewById(R.id.bussearch);
        mPoiSearchbtn.setOnClickListener(this);
        mBusLinebtn.setOnClickListener(this);

        mBusLineSearch = BusLineSearch.newInstance();
        mPoiSearch = PoiSearch.newInstance();
    }

    private void initPoiNearbySearchOption() {
        PoiNearbySearchOption poiNearbySearchOption = new PoiNearbySearchOption();
        poiNearbySearchOption.location(mCenterlLatLng);
        poiNearbySearchOption.keyword("公交");
        poiNearbySearchOption.radius(1000);
        mPoiSearch.searchNearby(poiNearbySearchOption);
        mPoiSearch.setOnGetPoiSearchResultListener(this);
    }

    private void initBusLineSearchOption() {
        if (mBusLineUid != null) {
            BusLineSearchOption busLineSearchOption = new BusLineSearchOption();
            busLineSearchOption.city("北京市");
            busLineSearchOption.uid(mBusLineUid);
            mBusLineSearch.searchBusLine(busLineSearchOption);
            mBusLineSearch.setOnGetBusLineSearchResultListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.poiSearch:
                initPoiNearbySearchOption();
                break;
            case R.id.bussearch:
                initBusLineSearchOption();
                break;
        }
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {

        if (poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Log.d("skyfin", "error");
        } else {
            mBaidumap.clear();
            //创建PoiOverlay
            PoiOverlay overlay = new MyPoiOverlay(mBaidumap);
            //设置overlay可以处理标注点击事件
            mBaidumap.setOnMarkerClickListener(overlay);
            overlay.setData(poiResult); //设置PoiOverlay数据
            overlay.addToMap(); //添加PoiOverlay到地图中
            overlay.zoomToSpan();
            this.mPoiResult = poiResult;
        }
        BusLineSearch pBusLineSearch = BusLineSearch.newInstance();
        BusLineSearchOption pBusLineSearchOption = new BusLineSearchOption().city("北京").uid("UID");
        pBusLineSearch.searchBusLine(pBusLineSearchOption);
        pBusLineSearch.setOnGetBusLineSearchResultListener(new OnGetBusLineSearchResultListener() {
            @Override
            public void onGetBusLineResult(BusLineResult busLineResult) {

            }
        });

    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    //获得BusLineResults
    @Override
    public void onGetBusLineResult(BusLineResult busLineResult) {

        if (busLineResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {

            Snackbar.make(view, "由于百度的问题,POI搜索出来的POIINFO的type只为BUS_STATION, POINT, SUBWAY_STATION,没有BUS_LINE,导致有些功能不能使用,这里还是贴出来代码", Snackbar.LENGTH_SHORT).show();
        } else {
            mBaidumap.clear();
            //显示在地图上
            BusLineOverlay pBusLineOverlay = new BusLineOverlay(mBaidumap);
            pBusLineOverlay.setData(busLineResult);
            pBusLineOverlay.addToMap();
            pBusLineOverlay.zoomToSpan();
        }
    }

    private class MyPoiOverlay extends PoiOverlay {
        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int index) {
            super.onPoiClick(index);
            if (mPoiResult != null) {
                List<PoiInfo> poiInfoList = mPoiResult.getAllPoi();
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.poiresult_layout, null);
                ((TextView) view.findViewById(R.id.address)).setText("address: " + poiInfoList.get(index).address);
                ((TextView) view.findViewById(R.id.city)).setText("city: " + poiInfoList.get(index).city);
                ((TextView) view.findViewById(R.id.location)).setText("location: " + poiInfoList.get(index).location);
                ((TextView) view.findViewById(R.id.name)).setText("name: " + poiInfoList.get(index).name);
                ((TextView) view.findViewById(R.id.phoneNum)).setText("phoneNum: " + poiInfoList.get(index).phoneNum);
                ((TextView) view.findViewById(R.id.type)).setText("type: " + poiInfoList.get(index).type);
                ((TextView) view.findViewById(R.id.uid)).setText("uid: " + poiInfoList.get(index).uid);
                Dialog dialog = new AlertDialog.Builder(PoiSearchActivity.this, R.style.Mystyle)
                        .setView(view)
                        .setTitle("title")
                        .create();
                dialog.show();
                if (poiInfoList.get(index).type == PoiInfo.POITYPE.BUS_LINE
                        || poiInfoList.get(index).type == PoiInfo.POITYPE.SUBWAY_LINE) {
                    Log.d("skyfin", "由于百度的问题,POI搜索出来的POIINFO的type只为BUS_STATION, POINT, SUBWAY_STATION,没有BUS_LINE,导致有些功能不能使用,这里还是贴出来代码");
                }
                mBusLineUid = poiInfoList.get(index).uid;
            }
            return true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mBusLineSearch.destroy();
        mPoiSearch.destroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
        mBusLineSearch.destroy();
        mPoiSearch.destroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
        mBusLineSearch.destroy();
        mPoiSearch.destroy();
    }
}
