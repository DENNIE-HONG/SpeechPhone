/*
 * Copyright (c) 2015-2016 Luyan Hong
 *
 * This file is part of the Crowdpp.
 *
 * Crowdpp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Crowdpp is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with the Crowdpp. If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package edu.rutgers.winlab.crowdpp.ui;

import edu.rutgers.winlab.crowdpp.R;
import edu.rutgers.winlab.crowdpp.util.Now;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


import org.json.JSONArray;
import org.json.JSONObject;

//import com.amazonaws.util.json.JSONObject;
//import com.amazonaws.util.json.JSONArray;

import com.baidu.baidulocationdemo.IupdateLatLon;
import com.baidu.baidulocationdemo.LocationActivity;
import com.baidu.baidulocationdemo.LocationActivityTwo;
import com.baidu.baidulocationdemo.LocationApplication;
import com.baidu.baidulocationdemo.MyLocationMapActivity;
import com.baidu.baidulocationdemo.LocationApplication.MyLocationListener;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.HeatMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
//import com.baidu.baidulocationdemo.BaseMapDemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import android.support.v4.app.Fragment;

/**
 * The settings fragment 
 * @author Luyan Hong
 */
public class SettingsFragment extends Fragment implements OnClickListener{
	Button btn_start_baidumap;
	//private static final String LTAG = BaseMapDemo.class.getSimpleName();
//	private String tempcoor="bd09ll";
//	private LocationMode tempMode = LocationMode.Hight_Accuracy;//高精度
//	private MapView mMapView;
//	private BaiduMap mBaiduMap;
//	private HeatMap heatmap;
//	private double latitude;
//	private double longitude;

	private TextView ser_year, ser_month, ser_day, ser_starthour, ser_startmin, ser_endhour, ser_endmin;
	private String UrlPath = "http://3.antsbigdata.applinzi.com/web/data.php";
	private String day, starttime, endtime;
	private String json;
	//private String UrlPath = "http://222.199.220.61:81/web/data.php";
//	"http://3.antsbigdata.applinzi.com/web/data.php";
	//public static boolean send_ok;
	
	
	//将输入流转换成字符串  
    public static String inStream2String(InputStream is) throws Exception {  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        byte[] buf = new byte[1024];  
        int len = -1;  
        while ((len = is.read(buf)) != -1) {  
            baos.write(buf, 0, len);  
        }  
        return new String(baos.toByteArray());  
    }
    
    Handler handler = new Handler(){
 	   @Override
 	   public void handleMessage(Message msg){
 		   if(msg.what == 0x123)  
 			   {	
 			   		if(json == null)
 			   			Toast.makeText(getActivity(), "The date you chose did not find speaker.", Toast.LENGTH_SHORT).show();
 			   		else
 			   			Toast.makeText(getActivity(), "Start loading data...", Toast.LENGTH_SHORT).show();
 			   }
 		}
    }; 

	public void onClick(View v) {
	    day = ser_year.getText().toString() + ser_month.getText().toString() + ser_day.getText().toString();
	    starttime = ser_starthour.getText().toString() +  ser_startmin.getText().toString() + "00";
	    endtime = ser_endhour.getText().toString() + ser_endmin.getText().toString() + "59";
	    System.out.println(starttime + endtime);
		new Thread(){
			@Override
			public void run(){
				URL url;
				try {
						url = new URL(UrlPath + "?day=" + day +"&StartTime=" + starttime + "&EndTime=" + endtime);
						//url = new URL(UrlPath);
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						int code = conn.getResponseCode();
						
						if (code == 200) {
							// 等于200了,下面呢我们就可以获取服务器的数据了
							InputStream is = conn.getInputStream();
						    json = inStream2String(is);
//							System.out.println(json);
							JSONArray array = new JSONArray(json);
							//String[] date = new String[array.length()];
						    int[] count =new int[array.length()];
						    float[] latitude = new float[array.length()];
						    float[] longitude = new float[array.length()];
						    String[] MFCC = new String[array.length()];
						    float[] gender = new float[array.length()]; 
						    for(int i = 0; i < array.length(); i++){
								JSONObject jsonObject = array.getJSONObject(i);
								//date[i] = jsonObject.getString("date");
								count[i] = Integer.parseInt(jsonObject.getString("speaker_count"));
								latitude[i] = Float.parseFloat(jsonObject.getString("latitude"));
								longitude[i] = Float.parseFloat(jsonObject.getString("longitude"));
								gender[i] = Float.parseFloat(jsonObject.getString("gender"));
							}
								if(latitude[0] != -1){
									Intent map = new Intent(getActivity(), LocationActivity.class);
									map.putExtra("count", count);
									//map.putExtra("date", date);
									//map.putExtra("start", bin.getStartTime());
									//i.putExtra("end", bin.getEndTime());
									map.putExtra("lat", latitude);
									map.putExtra("lon", longitude);
									startActivity(map);
							}
						    else
						    	json = null;
				            handler.sendEmptyMessage(0x123);
						}else{}
					} catch (IOException e) { 
		            	e.printStackTrace(); 
		            	//send_ok = false;
		            } 
				catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}		 
		}.start();
		
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = inflater.inflate(R.layout.map_layout, container, false);
		ser_year = (TextView) view.findViewById(R.id.service_year);
		ser_year.setText(Now.getYear());
		ser_month = (TextView) view.findViewById(R.id.service_month);
		ser_month.setText(Now.getMonth());
		ser_day = (TextView) view.findViewById(R.id.service_day);
		ser_day.setText(Now.getDay());
		ser_starthour = (TextView) view.findViewById(R.id.service_starthour);
		ser_starthour.setText(Now.getHourBofore());
		ser_startmin = (TextView) view.findViewById(R.id.service_startminute);
		ser_startmin.setText(Now.getMinute());
		ser_endhour = (TextView) view.findViewById(R.id.service_endhour);
		ser_endhour.setText(Now.getHour());
		ser_endmin = (TextView) view.findViewById(R.id.service_endminute);
		ser_endmin.setText(Now.getMinute());
		btn_start_baidumap = (Button) view.findViewById(R.id.btn);
		btn_start_baidumap.setOnClickListener(this);
		
		
		return view;
	}
	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}


	
	/*Button btn_start_baidumap;
	private SharedPreferences settings;
	SharedPreferences.Editor editor;

	String[] 		title, content, choice;
	String[] 		hours_arr				= new String[] 		{ "0:00", "1:00", "2:00", "3:00", "4:00", "5:00", 
																									"6:00", "7:00", "8:00", "9:00", "10:00", "11:00", 
																									"12:00", "13:00", "14:00", "15:00", "16:00", "17:00", 
																									"18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "24:00"}; 
  String[] 		interval_arr 		= new String[] 		{	"10 Minutes", "15 Minutes", "20 Minutes", "30 Minutes", "60 Minutes"};
  String[] 		duration_arr 		= new String[] 		{	"2 Minutes", "3 Minutes", "5 Minutes", "8 Minutes"};
  String[][]	duration_arrs		= new String[][] {{	"2 Minutes", "3 Minutes", "4 Minutes", "5 Minutes"},
  																							{	"3 Minutes", "4 Minutes", "5 Minutes", "8 Minutes"},
  																							{	"3 Minutes", "5 Minutes", "8 Minutes", "10 Minutes"},
  																							{	"5 Minutes", "8 Minutes", "10 Minutes", "15 Minutes"},
  																							{	"10 Minutes", "15 Minutes", "20 Minutes", "30 Minutes"}};
  String[] 		location_enable	= new String[] 		{	"On", "Off"};
  String[] 		upload_enable  	= new String[] 		{	"On", "Off"};
  
  String temp_str;
  int temp_hour;
  int temp_interval;
  
	TextView tv_peroid, tv_interval, tv_duration, tv_location, tv_upload;
  
  @Override  
  public void onListItemClick(ListView l, final View v, int position, long id) {	
  	switch((int)id){
			case 0:   	
				new AlertDialog.Builder(getActivity())
				.setTitle("Start time")
				.setSingleChoiceItems(Arrays.copyOfRange(hours_arr, 0, hours_arr.length - 1), 0, new DialogInterface.OnClickListener() {	
					@Override
					public void onClick(DialogInterface dialog, int which) {
						tv_peroid = (TextView) v.findViewById(R.id.tv_settings_choice);
						temp_str = hours_arr[which].substring(0, hours_arr[which].indexOf(':'));
						editor.putString("start", temp_str); 
						temp_str.concat(" - ");
						temp_hour = which;
						dialog.dismiss();
						final String[] temp_hours = Arrays.copyOfRange(hours_arr, temp_hour + 1, hours_arr.length);
						new AlertDialog.Builder(getActivity())
						.setTitle("End time")
						.setSingleChoiceItems(temp_hours, 0, new DialogInterface.OnClickListener() {	
							@Override
							public void onClick(DialogInterface dialog, int which) {
								editor.putString("end", temp_hours[which].substring(0, temp_hours[which].indexOf(':')));
								temp_str = temp_str.concat(" to ").concat(temp_hours[which].substring(0, temp_hours[which].indexOf(':')));
								tv_peroid.setText(temp_str);																		
								dialog.dismiss();
							  editor.commit();
						  	Toast.makeText(getActivity(), "You need to restart the service to apply these changes", Toast.LENGTH_SHORT).show();  																			
							}
						})
						.show();
					}
				})
				.show();
				break;

			case 1:
				new AlertDialog.Builder(getActivity())
				.setTitle(title[(int)id])
				.setSingleChoiceItems(interval_arr, 0, new DialogInterface.OnClickListener() {	
					@Override
					public void onClick(DialogInterface dialog, int which) {
						temp_interval = which;
						tv_interval = (TextView) v.findViewById(R.id.tv_settings_choice);						
						temp_str = interval_arr[which].substring(0, interval_arr[which].indexOf(' '));
						tv_interval.setText(temp_str.concat(" Min"));
					  Log.i("Debug", temp_str.concat(" Min"));						
						editor.putString("interval", temp_str); 						
					  editor.commit();						
						dialog.dismiss();
				  	Toast.makeText(getActivity(), "You need to restart the service to apply these changes", Toast.LENGTH_SHORT).show();  																	
					}
				})
				.show(); 									
				break;
				
			case 2:
				new AlertDialog.Builder(getActivity())
				.setTitle(title[(int)id])
				.setSingleChoiceItems(duration_arrs[temp_interval], 0, new DialogInterface.OnClickListener() {	
					@Override
					public void onClick(DialogInterface dialog, int which) {
						tv_duration = (TextView) v.findViewById(R.id.tv_settings_choice);
						temp_str = duration_arrs[temp_interval][which].substring(0, duration_arrs[temp_interval][which].indexOf(' '));
						tv_duration.setText(temp_str.concat(" Min"));
					  Log.i("Debug", temp_str.concat(" Min"));

						editor.putString("duration", temp_str); 						
					  editor.commit();							
						dialog.dismiss();	
				  	Toast.makeText(getActivity(), "You need to restart the service to apply these changes", Toast.LENGTH_SHORT).show();  											
					}
				})
				.show();			
				break;
				
			case 3:
				new AlertDialog.Builder(getActivity())
				.setTitle(title[(int)id])
				.setSingleChoiceItems(location_enable, 0, new DialogInterface.OnClickListener() {	
					@Override
					public void onClick(DialogInterface dialog, int which) {
						tv_location = (TextView) v.findViewById(R.id.tv_settings_choice);
						temp_str = location_enable[which];
						tv_location.setText(temp_str);
					  Log.i("Debug", temp_str);
						editor.putString("location", temp_str); 						
					  editor.commit();
						dialog.dismiss();
				  	Toast.makeText(getActivity(), "You need to restart the service to apply these changes", Toast.LENGTH_SHORT).show();  																	
					}
				})
				.show();									
				break;	
				
			case 4:
				new AlertDialog.Builder(getActivity())
				.setTitle(title[(int)id])
				.setSingleChoiceItems(upload_enable, 0, new DialogInterface.OnClickListener() {	
					@Override
					public void onClick(DialogInterface dialog, int which) {
						tv_upload = (TextView) v.findViewById(R.id.tv_settings_choice);
						temp_str = upload_enable[which];
						tv_upload.setText(temp_str);
						editor.putString("upload", temp_str); 						
					  editor.commit();							
						dialog.dismiss();		
				  	Toast.makeText(getActivity(), "You need to restart the service to apply these changes", Toast.LENGTH_SHORT).show();  																	
					}
				})
				.show();									
				break;			
			
			case 5:
				Intent webIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://github.com/lendlice/crowdpp"));  
				startActivity(webIntent);  
				break;

			case 6:
				Intent sendIntent = new Intent(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "chenren.xu@gmail.com" });
				sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Crowd++ bug report");
				sendIntent.setType("message/rfc822");
				startActivity(Intent.createChooser(sendIntent, "Send via"));
				break;
				
			default:
				break;
  	}
  }  

  @Override  
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { 
//	btn_start_baidumap = (Button)view.findViewById(R.id.btn_start_baidumap);
    settings = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
    editor = settings.edit();

    title 	= new String[] {"Period", 
    												"Interval",
    												"Duration",
    												"Location",
    												"Upload",
    												"Project page",
    												"Contact me",
    												"Version"								};
    
    content	= new String[] {"Specify when you want the service run every day.", 
  		  										"Specify how frequently you want the service to run.", 
  		  										"Specify how long you want to record every time.",
  		  										"Specify if you want the location data be collected.",
  		  										"Specify if you want to contribute the data to the cloud.",
  		  										"Brings you to the project page. Please leave a comment.",
  		  										"Report a bug or send your comment via email.",
  		  										"1.0"};
    
    choice 	= new String[] {settings.getString("start", "").concat(" to ").concat(settings.getString("end", "")), 
    												settings.getString("interval", "").concat(" Min"),
    												settings.getString("duration", "").concat(" Min"),
    												settings.getString("location", ""),
    												settings.getString("upload", ""),	
    												"",
    												"",
    												""};

  	ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();

  	for (int i = 0; i < title.length; i++) { 	
    	HashMap<String, String> map = new HashMap<String, String>();  
    	map.put("tv_settings_title", title[i]);  
      map.put("tv_settings_content", content[i]);
      map.put("tv_settings_choice", choice[i]);
      listItem.add(map);
    }
  	
  	// bind the listview adapter with the setting content
    SimpleAdapter mSimpleAdapter = new SimpleAdapter(inflater.getContext(), listItem, R.layout.settings_item,
        new String[] {"tv_settings_title", "tv_settings_content", "tv_settings_choice"},   
        new int[] {R.id.tv_settings_title, R.id.tv_settings_content, R.id.tv_settings_choice}  
    );

    setListAdapter(mSimpleAdapter);

   	return super.onCreateView(inflater, container, savedInstanceState);  
  }  */

	
}
