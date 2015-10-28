package com.skyfin.baidumapapi.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Gradient;
import com.baidu.mapapi.map.HeatMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.skyfin.baidumapapi.R;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class MyBaiduMapActivity extends AppCompatActivity
        implements View.OnClickListener, BaiduMap.OnMarkerClickListener
        , BaiduMap.OnMapClickListener, BaiduMap.OnMarkerDragListener {

    MapView mMapview;
    BaiduMap mBaidumap;
    MapStatus mMapStatus;
    LatLng centerlatlng = new LatLng(30.663598, 104.07187);
    ImageButton addZoomBtn;
    ImageButton subZoomBtn;
    Button mheatBtn;
    Button mMarkerBtn;
    Button mPolylineBtn;

    HeatMap mHeatMap;                     //热力图
    int[] DEFAULT_GRADIENT_COLORS;        //热力图渐变颜色值
    float[] DEFAULT_GRADIENT_START_POINTS;//渐变颜色起始值
    Gradient gradient;                    //颜色渐变对象
    List<LatLng> mHeatDataList;           //设置热力图的坐标信息

    Polyline mPolyline;                   //多边形覆盖物

    Marker mMarker;

    InfoWindow mInfoWindow;               //弹出窗

    //布局管理器
    LayoutInflater mLayoutInflater = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_baidu_map);
        mMapview = (MapView) findViewById(R.id.bmapView);
        initMapView();
        mBaidumap = mMapview.getMap();
        initMapStatus();
        InitHeatMap();
        addZoomBtn = (ImageButton) findViewById(R.id.add_btn);
        subZoomBtn = (ImageButton) findViewById(R.id.sub_btn);
        mheatBtn = (Button) findViewById(R.id.heatmap_btn);
        mMarkerBtn = (Button) findViewById(R.id.marker_btn);
        mPolylineBtn = (Button) findViewById(R.id.polyline_btn);
        addZoomBtn.setOnClickListener(this);
        subZoomBtn.setOnClickListener(this);
        mheatBtn.setOnClickListener(this);
        mMarkerBtn.setOnClickListener(this);
        mPolylineBtn.setOnClickListener(this);
        mBaidumap.setOnMarkerClickListener(this);
        mBaidumap.setOnMapClickListener(this);

    }
    //初始化Mapview
    private void initMapView() {
        //设置是否显示比例尺控件
        mMapview.showScaleControl(false);
        //设置是否显示缩放控件
        mMapview.showZoomControls(false);
    }
    //初始化地图状态
    private void initMapStatus() {
        //构建一个MapStatus
        MapStatus.Builder pBuilder = new MapStatus.Builder();
        //设置地图俯仰角
        pBuilder.overlook(-30);
        //设置地图缩放级别
        pBuilder.zoom(20);
        //设置地图中心点
        pBuilder.target(centerlatlng);
        //设置地图旋转角度，逆时针旋转。
        pBuilder.rotate(30);
        //构建MapStatus
        mMapStatus = pBuilder.build();
        //百度地图状态配置更新
        MapStatusUpdate pMapStatusUpadate;
        //设置一个新的状态
        pMapStatusUpadate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaidumap.setMapStatus(pMapStatusUpadate);
    }
    //初始化热力图
    private void InitHeatMap() {
        //设置渐变颜色值
        DEFAULT_GRADIENT_COLORS = new int[]{Color.rgb(102, 225, 0), Color.rgb(255, 0, 0)};
        //设置渐变颜色起始值
        DEFAULT_GRADIENT_START_POINTS = new float[]{0.2f, 1f};
        //构造颜色渐变对象
        gradient = new Gradient(DEFAULT_GRADIENT_COLORS, DEFAULT_GRADIENT_START_POINTS);

        mHeatDataList = new ArrayList<>();
        mHeatDataList.add(new LatLng(30.664798, 104.074417));
        mHeatDataList.add(new LatLng(30.664831, 104.070049));
        mHeatDataList.add(new LatLng(30.660339, 104.066619));
        mHeatDataList.add(new LatLng(30.657265, 104.081702));

    }
    //多边形覆盖物
    private void InitPolyline() {
        // 构造折线点坐标
        List<LatLng> points = new ArrayList<LatLng>();
        points.add(new LatLng(30.664798, 104.074417));
        points.add(new LatLng(30.664831, 104.070049));
        points.add(new LatLng(30.660339, 104.066619));
        points.add(new LatLng(30.657265, 104.081702));

        //构建分段颜色索引数组
        List<Integer> colors = new ArrayList<>();
        colors.add(Integer.valueOf(Color.BLUE));
        colors.add(Integer.valueOf(Color.RED));
        colors.add(Integer.valueOf(Color.GREEN));

        OverlayOptions ooPolyline = new PolylineOptions().width(10)
                .colorsValues(colors).points(points);
        //添加在地图中
        mPolyline = (Polyline) mBaidumap.addOverlay(ooPolyline);
    }
    //初始化Marker
    private void initMarker() {
        MarkerOptions pMarkerOptions = new MarkerOptions();
        pMarkerOptions.position(centerlatlng);
        pMarkerOptions.draggable(true);
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.sns_shoot_location_pressed);
        pMarkerOptions.icon(icon);
        pMarkerOptions.title("marker");
        mMarker = (Marker) mBaidumap.addOverlay(pMarkerOptions);
    }
    //初始化InfoWindow
    private void initInfoWindow() {

        mLayoutInflater = getLayoutInflater();
        View view = mLayoutInflater.inflate(R.layout.infowindowlinearlayout, null);
        Button button1 = (Button) view.findViewById(R.id.infowindowbtn1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "item1 click", Toast.LENGTH_SHORT).show();
            }
        });
        mInfoWindow = new InfoWindow(view, centerlatlng, -30);
        mBaidumap.showInfoWindow(mInfoWindow);

    }

    private void InitOverlay(MapPoi mapPoi){
        BitmapDescriptor bdend = BitmapDescriptorFactory
                .fromResource(R.mipmap.sns_shoot_location_pressed);
        OverlayOptions overlayOptions = new MarkerOptions().position(mapPoi.getPosition()).icon(bdend).title(mapPoi.getName());
        mBaidumap.addOverlay(overlayOptions);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        MapStatusUpdate pMapStatusUpadate;
        switch (id) {
            case R.id.add_btn:
                pMapStatusUpadate = MapStatusUpdateFactory.zoomIn();
                mBaidumap.setMapStatus(pMapStatusUpadate);
                break;
            case R.id.sub_btn:
                pMapStatusUpadate = MapStatusUpdateFactory.zoomOut();
                mBaidumap.setMapStatus(pMapStatusUpadate);
                break;
            case R.id.heatmap_btn:
                if (mheatBtn.getText().equals("打开热力图")) {
                    mHeatMap = new HeatMap.Builder()
                            .data(mHeatDataList)
                            .gradient(gradient)
                            .radius(50)
                            .build();
                    //在地图上添加热力图
                    mBaidumap.addHeatMap(mHeatMap);
                    mheatBtn.setText("关闭热力图");
                } else {
                    mHeatMap.removeHeatMap();
                    mheatBtn.setText("打开热力图");
                }
                break;
            case R.id.marker_btn:
                initMarker();
                break;
            case R.id.polyline_btn:
                InitPolyline();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapview.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapview.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapview.onPause();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getTitle().equals("marker")) {
            initInfoWindow();
        }
        return true;
    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }


    @Override
    public void onMapClick(LatLng latLng) {
        mBaidumap.hideInfoWindow();
    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        InitOverlay(mapPoi);
        Toast.makeText(getApplicationContext(), mapPoi.getName(), Toast.LENGTH_SHORT).show();
        return true;
    }
}
