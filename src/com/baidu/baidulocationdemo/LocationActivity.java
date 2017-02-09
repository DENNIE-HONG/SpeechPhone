package com.baidu.baidulocationdemo;

import java.util.ArrayList;
import java.util.List;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.HeatMap;
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
import com.baidu.baidulocationdemo.IupdateLatLon;
import com.baidu.baidulocationdemo.LocationActivity;
import com.baidu.baidulocationdemo.LocationActivityTwo;
import com.baidu.baidulocationdemo.LocationApplication;
import com.baidu.baidulocationdemo.MyLocationMapActivity;
import com.baidu.baidulocationdemo.LocationApplication.MyLocationListener;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import edu.rutgers.winlab.crowdpp.R;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
//OnGetGeoCoderResultListener

/**
 * The settings fragment 
 * @author Luyan Hong
 */
public class LocationActivity extends Activity implements IupdateLatLon{
//	private int count;
//	private String date;
//	private String start;
//	private String end;
	private TextView map_content;
	private List<LatLng> data = null;
	private double latitude,longitude;
	private int datanum;
	private int total_count;  
	private boolean flag;
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	
	 private LocationMode tempMode = LocationMode.Hight_Accuracy;//高精度
	 private String tempcoor="bd09ll";
//	 private String tempcoor="gcj02";
	 private LocationClient mLocationClient;
	 private HeatMap heatmap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent i = getIntent();
		int[] count = i.getExtras().getIntArray("count");
		//String[] date = i.getStringArrayExtra("date");
		//start = i.getExtras().getString("start");
		//end = i.getExtras().getString("end");
		float[] lat = i.getExtras().getFloatArray("lat");
		float[] lon = i.getExtras().getFloatArray("lon");
		//System.out.println(lat[0]);
		
		//------------------------------
		setContentView(R.layout.activity_my_map);
		flag = true;
		
		//mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomBy(5));
		//mLocationClient= ((LocationApplication)getActivity().getApplication()).mLocationClient;
		
//		((LocationApplication)getActivity().getApplication()).latLon = this;
		 
		mMapView = (MapView) findViewById(R.id.mapview);
		mBaiduMap = mMapView.getMap();
		map_content = (TextView) findViewById(R.id.map_content);
	//	mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(5));
		mLocationClient= ((LocationApplication)getApplication()).mLocationClient;
//			mSearch = GeoCoder.newInstance();
//			mSearch.setOnGetGeoCodeResultListener(this);
		((LocationApplication)getApplication()).latLon = this;
		initLocation(); 
		mLocationClient.start();
		//initLocation();
		
		datanum = count.length;
		//List<LatLng> data = null;
		total_count = 0;
		List<LatLng> list = new ArrayList<LatLng>();
		//while(latitude!= 0){
		//	try {
				for(int j = 0;j < datanum;j++){
					for(int k = 0; k < count[j]; k++){
						list.add(new LatLng(lat[j],lon[j]));
					}
					total_count += count[j];
				}	
				data = list;
//				map_content = (TextView) findViewById(R.id.map_content);
				map_content.setText("Speaker_count < " + (total_count / 10 + 1) * 10 +" people");
				//map_content.setVisibility(View.INVISIBLE);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		
		//heatmap = new HeatMap.Builder().data(data).build();
		//}
		
			//	initLocation();
		
	}
	

//	private Activity getActivity() {
//		// TODO Auto-generated method stub
//		return null;
//	}


//	@Override
//	public void onGetGeoCodeResult(GeoCodeResult arg0) {
//		
//	}

//	@Override
//	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
//		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//			Toast.makeText(LocationActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
//					.show();
//			return;
//		}
//		mBaiduMap.clear();
//		mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
//					.icon(BitmapDescriptorFactory
//							.fromResource(R.drawable.icon_gcoding)));
////		mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
////				.icon(BitmapDescriptorFactory
////						.fromResource(R.drawable.icon_gcoding)));
////		OverlayOptions ooCircle = new CircleOptions().fillColor(0x6aff0000).visible(true)
////				.center(result.getLocation()).stroke(new Stroke(1, 0xAA000000))
////				.radius(1*count);
////		mBaiduMap.addOverlay(ooCircle);
//		// 添加文字
//		OverlayOptions ooText = new TextOptions().bgColor(0x00000000).fontSize(40).fontColor(0xFF000000).text("speak count:n").rotate(0)
//				.position(result.getLocation());
//		mBaiduMap.addOverlay(ooText);
//		
//		
////		Toast.makeText(MyLocationMapActivity.this, result.getAddress(),Toast.LENGTH_SHORT).show();
//		// 添加圆
//		
//		
//		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
//				.getLocation()));
//	}
	
	private  void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(tempMode);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType(tempcoor);//可选，默认gcj02，设置返回的定位结果坐标系，
        int span=3000;
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
	
	public void update(double lat, double lon) {
		System.out.println("---->>>>>>>>我的经度为"+lat+">>>我的纬度为"+lon);
		this.latitude = lat;
		this.longitude = lon;
		
		 //设置中心点
	
			LatLng p = new LatLng(latitude,longitude);
			mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(p));
			mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(17));
			mLocationClient.stop();
			heatmap = new HeatMap.Builder().data(data).build();
			mBaiduMap.addHeatMap(heatmap);
//			for(int i = 0; i < datanum; i++){
				mBaiduMap.addOverlay(new MarkerOptions().position(p)
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.icon_gcoding)));
//				OverlayOptions ooText = new TextOptions().bgColor(0x00000000).fontSize(40).fontColor(0xFF000000).text("me").rotate(0)
//				.position(p);
//		mBaiduMap.addOverlay(ooText);
			//	mBaiduMap.addOverlay(ooCircle);
				
//			}
							
		
	}

}
