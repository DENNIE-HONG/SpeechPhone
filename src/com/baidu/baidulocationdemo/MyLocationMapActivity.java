package com.baidu.baidulocationdemo;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import edu.rutgers.winlab.crowdpp.R;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.widget.Toast;

public class MyLocationMapActivity extends Activity implements IupdateLatLon,OnGetGeoCoderResultListener{
	private boolean flag;
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	
	 private LocationMode tempMode = LocationMode.Hight_Accuracy;//高精度
	 private String tempcoor="bd09ll";
//	 private String tempcoor="gcj02";
	 private LocationClient mLocationClient;
	 private double lat = 39.99726195644362,lon = 116.36018577243168;
	 LatLng p ;
//	// 转换百度坐标系
//	Point resultPoint = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_map);
		flag = true;
		mMapView = (MapView) findViewById(R.id.mapview);
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomBy(20));
		mLocationClient= ((LocationApplication)getApplication()).mLocationClient;
			  ((LocationApplication)getApplication()).latLon = this;
			  initLocation();
			  mLocationClient.start();
	// 初始化搜索模块，注册事件监听
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		  p = new LatLng(lat, lon);
			
		
		
	}
	
	 private void initLocation(){
	        LocationClientOption option = new LocationClientOption();
	        option.setLocationMode(tempMode);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
	        option.setCoorType(tempcoor);//可选，默认gcj02，设置返回的定位结果坐标系，
	        int span=10000;
	        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
	        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
	        option.setOpenGps(true);//可选，默认false,设置是否使用gps
	        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
	        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
	        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
	        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
	        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
	        mLocationClient.setLocOption(option);
	    }
	
	@Override
	protected void onPause() {
		super.onPause();
		flag = false;
		// activity 暂停时同时暂停地图控件
		mMapView.onPause();
		 if ( mLocationClient.isStarted()) {
			mLocationClient.stop();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// activity 恢复时同时恢复地图控件
		mMapView.onResume();
		flag = true;
		 if ( !mLocationClient.isStarted()) {
				mLocationClient.start();
			}
		
		
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// activity 销毁时同时销毁地图控件
		mMapView.onDestroy();
		if ( mLocationClient.isStarted()) {
			mLocationClient.stop();
			
		}
		mLocationClient=null;
		flag = false;
		mSearch.destroy();
	}

	@Override
	public void update(double lat,double lon) {
		System.out.println("---->>>>>>>>我的经度为"+lat+">>>我的纬度为"+lon);
		this.lat = lat;
		this.lon = lon;
		p = new LatLng(lat,lon);
		
//		resultPoint = CoordinateConverter.converter(COOR_TYPE.COOR_TYPE_GCJ02, sourcePoint);
		if(flag)
			mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(p));
		
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(MyLocationMapActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
					.show();
			return;
		}
		mBaiduMap.clear();
		mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.icon_gcoding)));
		OverlayOptions ooCircle = new CircleOptions().fillColor(0x9aff0000).visible(true)
				.center(result.getLocation()).stroke(new Stroke(1, 0xAA000000))
				.radius(5);
		mBaiduMap.addOverlay(ooCircle);
		// 添加文字
		OverlayOptions ooText = new TextOptions().bgColor(0x00000000).fontSize(40).fontColor(0xFF000000).text("speak_count:n").rotate(0)
				.position(result.getLocation());
		mBaiduMap.addOverlay(ooText);
		
		
//		Toast.makeText(MyLocationMapActivity.this, result.getAddress(),Toast.LENGTH_SHORT).show();
		// 添加圆
		
		
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
				.getLocation()));

	}
	

}
