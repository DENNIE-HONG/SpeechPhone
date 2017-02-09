package com.baidu.baidulocationdemo;

import com.baidu.location.LocationClient;
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
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class LocationActivityTwo extends Activity implements OnGetGeoCoderResultListener{
	private int count;
	private String date;
	private String start;
	private String end;
	private float lat,lon;
	
	
	private boolean flag;
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	
	 private LocationMode tempMode = LocationMode.Hight_Accuracy;//高精度
	 private String tempcoor="bd09ll";
//	 private String tempcoor="gcj02";
	 private LocationClient mLocationClient;
	 LatLng p ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent i = getIntent();
		count = i.getExtras().getInt("count");
		date = i.getExtras().getString("date");
		//start = i.getExtras().getString("start");
		//end = i.getExtras().getString("end");
		lat = i.getExtras().getFloat("lat");
		lon = i.getExtras().getFloat("lon");
		
		
		//------------------------------
		setContentView(R.layout.activity_my_map);
		flag = true;
		mMapView = (MapView) findViewById(R.id.mapview);
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomBy(20));
		
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		  p = new LatLng(lat, lon);
		  mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(p));
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult arg0) {
		
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(LocationActivityTwo.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
					.show();
			return;
		}
		mBaiduMap.clear();
		mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.icon_gcoding)));
		OverlayOptions ooCircle = new CircleOptions().fillColor(0x6aff0000).visible(true)
				.center(result.getLocation()).stroke(new Stroke(1, 0xAA000000))
				.radius(1*count);
		mBaiduMap.addOverlay(ooCircle);
		// 添加文字
		OverlayOptions ooText = new TextOptions().bgColor(0x00000000).fontSize(40).fontColor(0xFF000000).text("speak count:"+count).rotate(0)
				.position(result.getLocation());
		mBaiduMap.addOverlay(ooText);
		
		
//		Toast.makeText(MyLocationMapActivity.this, result.getAddress(),Toast.LENGTH_SHORT).show();
		// 添加圆
		
		
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
				.getLocation()));
	}

}
