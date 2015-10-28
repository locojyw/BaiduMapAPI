package com.skyfin.baidumapapi.activity;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.busline.BusLineSearch;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.skyfin.baidumapapi.R;

public class PoiSearchActivity extends AppCompatActivity implements View.OnClickListener, OnGetPoiSearchResultListener {

    private MapView mMapView = null;

    BusLineSearch mBusLineSearch;
    PoiSearch mPoiSearch;
    RoutePlanSearch mRoutePlanSearch;
    SuggestionSearch mSuggestionSearch;

    LatLng mCenterlLatLng = new LatLng(39.886332, 116.416707);

    Button poiSearchbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_search);
        mMapView = (MapView) findViewById(R.id.bmapView);
        initSearch();
    }

    private void initSearch() {
        poiSearchbtn = (Button) findViewById(R.id.poiSearch);
        poiSearchbtn.setOnClickListener(this);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.poiSearch:
                initPoiNearbySearchOption();
                break;
        }
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {

        if (poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Log.d("skyfin", "error");
        } else {
         /*   //创建PoiOverlay
            PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
            //设置overlay可以处理标注点击事件
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(mPoiResult); //设置PoiOverlay数据
            overlay.addToMap(); //添加PoiOverlay到地图中
            overlay.zoomToSpan();*/
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }
   /* private class MyPoiOverlay extends PoiOver {
        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int index) {
                super.onPoiClick(index);
            return true;
        }
    }*/
}
