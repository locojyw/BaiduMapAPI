package com.skyfin.baidumapapi.appserver;

import android.app.Application;
import android.app.Service;
import android.os.Vibrator;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.skyfin.baidumapapi.bean.MyLocation;

/**
 * 主Application，所有百度定位SDK的接口说明请参考线上文档：http://developer.baidu.com/map/loc_refer/index.html
 *
 * 百度定位SDK官方网站：http://developer.baidu.com/map/index.php?title=android-locsdk
 */
public class LocationApplication extends Application {
    public LocationClient mLocationClient;
    public MyLocationListener mMyLocationListener;

    public Vibrator mVibrator;

    public  OnGetDBLocation mOnGetDBLocation;
    @Override
    public void onCreate() {
        super.onCreate();
        mLocationClient = new LocationClient(this.getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
    }


    /**
     * 实现实时位置回调监听
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            MyLocation pMyLocation = new MyLocation();

            StringBuilder sb = new StringBuilder(256);
            pMyLocation.setTime(location.getTime());
            pMyLocation.setErrorcode(location.getLocType());

            pMyLocation.setLatitude(location.getLatitude());

            pMyLocation.setLontitude(location.getLongitude());
            pMyLocation.setRadius(location.getRadius());

            if (location.getLocType() == BDLocation.TypeGpsLocation){// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");
                Log.d("skyfin", "gps定位成功");
                Log.d("skyfin",sb.toString());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){// 网络定位结果
                pMyLocation.setAddr(location.getAddrStr());
                pMyLocation.setDescribe("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                pMyLocation.setAddr(location.getAddrStr());
                pMyLocation.setDescribe("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                pMyLocation.setAddr(location.getAddrStr());
                pMyLocation.setDescribe("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                pMyLocation.setAddr(location.getAddrStr());
                pMyLocation.setDescribe("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                pMyLocation.setAddr(location.getAddrStr());
                pMyLocation.setDescribe("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            pMyLocation.setLocationdescribe(location.getLocationDescribe());// 位置语义化信息

            // POI信息
            if (location.getPoiList().size()!=0) {
                pMyLocation.setList(location.getPoiList());
            }
            mOnGetDBLocation.get(pMyLocation);
        }
    }

    public static interface OnGetDBLocation{
        void get(MyLocation location);
    }
    public void setDBLocationListener(OnGetDBLocation dbLocationListener){
        this.mOnGetDBLocation = dbLocationListener;
    }
}
