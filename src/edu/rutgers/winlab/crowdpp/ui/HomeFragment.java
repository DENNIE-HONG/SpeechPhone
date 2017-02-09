/**
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
//import org.apache.http.util.EntityUtils;

//import com.sina.cloudstorage.http.impl.client.SdkHttpClient;
//
//
//import com.sina.cloudstorage.auth.AWSCredentials;
//import com.sina.cloudstorage.auth.BasicAWSCredentials;
//import com.sina.cloudstorage.services.scs.SCS;
//import com.sina.cloudstorage.services.scs.SCSClient;
//import com.sina.cloudstorage.services.scs.model.Bucket;
//import com.sina.cloudstorage.services.scs.model.PutObjectRequest;

//import com.amazonaws.auth.AWSCredentials;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.regions.Region;
//import com.amazonaws.regions.Regions;
//import com.amazonaws.services.s3.AmazonS3Client;
//import com.amazonaws.services.s3.model.Bucket;
//import com.amazonaws.services.s3.model.PutObjectRequest;

import com.baidu.baidulocationdemo.IupdateLatLon;
import com.baidu.baidulocationdemo.LocationApplication;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;

import edu.rutgers.winlab.crowdpp.R;
import edu.rutgers.winlab.crowdpp.audio.SpeakerCount;
import edu.rutgers.winlab.crowdpp.audio.MFCC;
import edu.rutgers.winlab.crowdpp.audio.Yin;
import edu.rutgers.winlab.crowdpp.db.DataBaseHelper;
import edu.rutgers.winlab.crowdpp.service.AudioRecordService;
//import edu.rutgers.winlab.crowdpp.service.SpeakerCountService;
//import edu.rutgers.winlab.crowdpp.service.SpeakerCountService.S3PutDataBaseTask;
import edu.rutgers.winlab.crowdpp.util.Constants;
import edu.rutgers.winlab.crowdpp.util.FileProcess;
import edu.rutgers.winlab.crowdpp.util.Now;
import android.app.ActivityManager;
//import android.app.AlertDialog;
//import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
//import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * The home fragment 
 * @author Luyan Hong
 */
public class HomeFragment extends Fragment implements IupdateLatLon{

	private ToggleButton tb_service, tb_test;
	private TextView tv_cal_content, tv_debug, tv_record;
	private Chronometer timer_test;
//	private RelativeLayout rl_service, rl_test;
	private File crowdppDir, testDir; 
	private ImageView record;
	private AnimationDrawable anim;
	//baidu map
	private MapView testMapView;
	private BaiduMap testBaiduMap;
	private LocationMode tempMode = LocationMode.Hight_Accuracy;//高精度
	private String tempcoor="bd09ll";
	private LocationClient mLocationClient;
	
//	private LocationTracker gps = null;
	
	public static String calWavFile;
	private String testWavFile;
	
	private long sys_time;
	private String date, start, end;
	private int speaker_count;
	private int[] gender;
	private double[][] mfcc;
	
	// default values when the data is not available
	private double percentage = -1;
	public static double latitude = -1;
	public static double longitude = -1;	
	private String test_log;
	
	private DataBaseHelper mDatabase = null; 
	private Cursor mCursor = null;
	private SQLiteDatabase mDB = null;
	
	private ProgressBar bar;
//	private AmazonS3Client s3Client;
//	AWSCredentials credentials = new BasicAWSCredentials(Constants.ACCESS_KEY_ID, Constants.SECRET_KEY);
//	SCS conn = new SCSClient(credentials);
//	private boolean isMyServiceRunning() {
//	    ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
//	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//	        if ("edu.rutgers.winlab.crowdpp.service.SpeakerCountService".equals(service.service.getClassName())) {
//	            return true;
//	            
//	        }
//	    }
//	    return false;
//	}
	
   Handler handler = new Handler(){
	   @Override
	   public void handleMessage(Message msg){
		   if(msg.what == 0x123){
			   bar.setVisibility(View.INVISIBLE);
			   Toast.makeText(getActivity(), "Send data success!", Toast.LENGTH_SHORT).show();
			   
		   }
		   if(msg.what == 0x111){
			   bar.setVisibility(View.INVISIBLE);
			   Toast.makeText(getActivity(), "Connection failed,please try again later.", Toast.LENGTH_SHORT).show();
		   }
		   
	   }
   }; 
   
//   public int doWork(){
//	   data[hasData++] = (int)(Math.random() * 100);
//	   try{
//		   Thread.sleep(100);
//	   }
//	   catch (InterruptedException e){
//		   e.printStackTrace();
//	   }
//	   return hasData;
//   };
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mLocationClient= ((LocationApplication)getActivity().getApplication()).mLocationClient;
		 initLocation();
		 ((LocationApplication)getActivity().getApplication()).latLon = this;
		  
		  
		  ////////
		final View view = inflater.inflate(R.layout.home_fragment_layout, container, false);				
		
		//tb_cal = (ToggleButton) view.findViewById(R.id.tb_home_calibration);
		tb_service = (ToggleButton) view.findViewById(R.id.tb_home_service);
		tb_test = (ToggleButton) view.findViewById(R.id.tb_home_test);
		//tv_cal_content = (TextView) view.findViewById(R.id.tv_home_calibration_content);
		//tv_cal_text = (TextView) view.findViewById(R.id.tv_home_calibration_text);
		tv_debug = (TextView) view.findViewById(R.id.tv_home_test_debug);
		tv_record = (TextView) view.findViewById(R.id.tv_home_test_record);
		//timer_cal = (Chronometer) view.findViewById(R.id.timer_calibration);		
		timer_test = (Chronometer) view.findViewById(R.id.timer_test);
		//rl_service = (RelativeLayout) view.findViewById(R.id.rl_home_service);	    	
		//rl_test = (RelativeLayout) view.findViewById(R.id.rl_home_test);
		bar = (ProgressBar)view.findViewById(R.id.bar);
		bar.setVisibility(View.INVISIBLE);
		testMapView = (MapView) view.findViewById(R.id.test_map);
		testBaiduMap = testMapView.getMap();
		testMapView.setVisibility(view.INVISIBLE);
		record = (ImageView) view.findViewById(R.id.record_image);
		record.setVisibility(View.INVISIBLE);
	
//		if(isMyServiceRunning()==true)
//			tb_service.setChecked(true);

//    if (Constants.calibration())
//    	tv_cal_content.setText("You are all set for the calibration.");
    
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			Toast.makeText(getActivity(), "Can not find SD card ...", Toast.LENGTH_SHORT).show();			
			getActivity().finish();
		}

		crowdppDir = new File(Constants.crowdppPath);
		if (!crowdppDir.exists() || !crowdppDir.isDirectory()) {
			crowdppDir.mkdir();
		} 

		testDir = new File(Constants.testPath);
		if (!testDir.exists() || !testDir.isDirectory()) {
			testDir.mkdir();
		}	
		
		calWavFile = crowdppDir + "/" + Constants.PHONE_ID + ".wav";

		//timer_cal.setVisibility(View.INVISIBLE);
		timer_test.setVisibility(View.INVISIBLE);
	  
		mDatabase = new DataBaseHelper(getActivity().getApplicationContext());
		mDB = mDatabase.getWritableDatabase();
//		AWSCredentials credentials = new BasicAWSCredentials(Constants.ACCESS_KEY_ID, Constants.SECRET_KEY);
//		SCS conn = new SCSClient(credentials);
//		s3Client = new AmazonS3Client(new BasicAWSCredentials(Constants.ACCESS_KEY_ID, Constants.SECRET_KEY));
//		s3Client.setRegion(Region.getRegion(Regions.US_WEST_2));

//		tb_cal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//	    	// prevent the calibration button being pressed when either and test or service is running 
//	    	if (tb_service.isChecked()) {
//	  			Toast.makeText(getActivity(), "Please turn off the service...", Toast.LENGTH_SHORT).show();		
//	  			tb_cal.setChecked(false);
//	    	}
//	    	else if (tb_test.isChecked()) {
//	  			Toast.makeText(getActivity(), "Please turn off the test...", Toast.LENGTH_SHORT).show();		
//	  			tb_cal.setChecked(false);	  			
//	    	}	
//	    	// calibration
//	    	else {
//		    	if (isChecked) {
//		    		tv_cal_text.setText(Constants.cal_text);
//		    		rl_service.setVisibility(View.INVISIBLE);
//		    		rl_test.setVisibility(View.INVISIBLE);	 
//		    		timer_cal.setVisibility(View.VISIBLE);
//		    		
//		    		AlertDialog dialog = new AlertDialog.Builder(getActivity()).create(); 
//		        dialog.setTitle("Calibration");
//		        dialog.setMessage(Constants.cal_dialog);
//						dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
//							@Override
//							public void onClick(DialogInterface dialog, int which) {
//				    		timer_cal.setBase(SystemClock.elapsedRealtime());
//				    		timer_cal.start();
//				      	tv_cal_content.setText("Recording your voice...");
//
//				  			Bundle mbundle = new Bundle();
//				  			mbundle.putString("audiopath", calWavFile);
//				  			Log.i("HomeFragment", "start audio recording service");
//				  			
//				  			// delete the existing calibration data before the recalibration 
//				  			FileProcess.deleteFile(calWavFile);
//    			  			FileProcess.deleteFile(calWavFile + ".jstk.mfcc.txt");
//				  			FileProcess.deleteFile(calWavFile + ".YIN.pitch.txt");
//				  			
//				  			// start audio recording
//				  			Intent recordIntent = new Intent(getActivity(), AudioRecordService.class);
//				  			recordIntent.putExtras(mbundle);
//				  			getActivity().startService(recordIntent);		    				    		
//							}
//						});
//						dialog.show();        				
//		        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(20);		    		
//					} 
//		    	else {
//		    		// stop audio recording
//		  			Intent recordIntent = new Intent(getActivity(), AudioRecordService.class);
//		  			getActivity().stopService(recordIntent);
//		  			timer_cal.stop();
//		  			tb_cal.setClickable(false);
//		      	tv_cal_content.setText("Calibrating....");
//		      	// start calibration
//		  			new Calibration().execute();
//		  			tb_cal.setClickable(true);
//		    		tv_cal_text.setText("");
//		    		rl_service.setVisibility(View.VISIBLE);
//		    		rl_test.setVisibility(View.VISIBLE);	  	    	
//		    		timer_cal.setVisibility(View.INVISIBLE);
//					}
//	    	}
//	    }
//		});

		tb_service.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	    	// prevent the service button being pressed when either and test or calibration is running 	    	
//	    	if (tb_cal.isChecked()) {
//	  			Toast.makeText(getActivity(), "Please turn off the calibration...", Toast.LENGTH_SHORT).show();		
//	  			tb_service.setChecked(false);
//	    	}
	    	if (tb_test.isChecked()) {
	  			Toast.makeText(getActivity(), "Please turn off the test...", Toast.LENGTH_SHORT).show();		
	  			tb_service.setChecked(false);	  			
	    	}
	    	// speaker counting service
	    	else {
					//Intent countIntent = new Intent(getActivity(), SpeakerCountService.class);	    	
		    	if (isChecked) {
					  //getActivity().startService(countIntent);
		    		if(date == null)
		    			Toast.makeText(getActivity(), "Please turn on the test first...", Toast.LENGTH_SHORT).show();
		    		//Http send data to WebService	
		    		else{
		    				
//		    			new S3PutDataBaseTask().execute();
		    			Log.i("Upload", "Finish");
		    			if(latitude != -1&& speaker_count != 0){
		    					bar.setVisibility(View.VISIBLE);
					    		new Thread()
					    		{
					    			@Override
					    			public void run(){
//					    				String url = "http://10.0.118.22:80/speech-phone/index.php";
					    				
					    				String url = "http://3.antsbigdata.applinzi.com/web/index.php";
					    		    	HttpPost httpRequest = new HttpPost(url);
					    		    	List<NameValuePair> params = new ArrayList<NameValuePair>();
					    		    	params.add(new BasicNameValuePair("date", date));
					    		    	params.add(new BasicNameValuePair("start", start));
					    		    	params.add(new BasicNameValuePair("end", end)); 
					    		    	params.add(new BasicNameValuePair("speaker_count", String.valueOf(speaker_count)));
					    		    	params.add(new BasicNameValuePair("latitude", String.valueOf(latitude)));
					    		    	params.add(new BasicNameValuePair("longitude", String.valueOf(longitude)));
//					    		    	System.out.println(arraytostring(mfcc));
					    		    	params.add(new BasicNameValuePair("gender", Arrays.toString(gender)));
					    		    	params.add(new BasicNameValuePair("MFCC", arraytostring(mfcc)));
					    		  
					    		    	try { 
						    		    		HttpEntity httpEntity = new UrlEncodedFormEntity(params,"utf-8"); 
					    				    	httpRequest.setEntity(httpEntity); 
					    				    	HttpClient httpClient = new DefaultHttpClient();
//					    				    	SdkHttpClient httpClient = new SdkHttpClient(null, params);
					    				    	HttpResponse httpResponse = httpClient.execute(httpRequest);
					    				    	if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){ 
					    				    		String result = EntityUtils.toString(httpResponse.getEntity()); 
					    				    		Log.i("save", result);
					    				    		handler.sendEmptyMessage(0x123); //Update UI
					    				    	}
							    			    else{
							    			    	handler.sendEmptyMessage(0x111);
							    			    	} 
					    				    
							    			  }catch (IOException e) { 
							    				   e.printStackTrace(); 
							    			   }
					    		 	}
					    			
					    		}.start();
					    		
					 		}
		    				else
		    				{
		    					Toast.makeText(getActivity(), "Can't get your location,please try again later.", Toast.LENGTH_SHORT).show();
		    				}
						 }
			    }
		    	else {
		    			Toast.makeText(getActivity(), "Turn off the service...", Toast.LENGTH_SHORT).show();
		    			bar.setVisibility(View.INVISIBLE);
				}
	    	}
	    }
		});		
		
		tb_test.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	    	// prevent the test button being pressed when either and service or calibration is running 	    	
//	    	if (tb_cal.isChecked()) {
//	  			Toast.makeText(getActivity(), "Please turn off the calibration...", Toast.LENGTH_SHORT).show();		
//	  			tb_test.setChecked(false);
//	    	}
	    	if (tb_service.isChecked()) {
	  			Toast.makeText(getActivity(), "Please turn off the service...", Toast.LENGTH_SHORT).show();		
	  			tb_test.setChecked(false);	  			
	    	}	 
	    	// perform the speaker counting test
	    	else {
		    	if (isChecked) {
		    		initLocation();
		            mLocationClient.start();//定位SDK start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
		            testMapView.setVisibility(view.INVISIBLE);
		            testBaiduMap.clear();
		            record.setVisibility(View.VISIBLE);
		            record.setImageResource(R.drawable.animation);
		            anim = (AnimationDrawable) record.getDrawable(); 
		            anim.start();
		            
		            
		    		// get location information
//		  		  gps.getLocation();
//		  		  if (gps.canGetLocation()) {
////		  		  	latitude = gps.getLatitude();
////		  		  	longitude = gps.getLongitude();
//		  		  } 
//		  		  else {
//		  		  	latitude  = -1;
//		  		  	longitude = -1;
//		  		  }
//		  	    gps.stopUsingGPS();
		    		//mLocationClient.start();
		    	
		  	    
		    		timer_test.setBase(SystemClock.elapsedRealtime());
		    		timer_test.start();
		    		tv_debug.setText("Recording...");
		  			testWavFile = testDir + "/" + FileProcess.newFileOnTime("wav");
		  			// start audio recording
		  			Bundle mbundle = new Bundle();
		  			mbundle.putString("audiopath", testWavFile);
		  			Log.i("HomeFragment", "start audio recording service");
		  			Intent recordIntent = new Intent(getActivity(), AudioRecordService.class);
		  			recordIntent.putExtras(mbundle);
		  			date = Now.getDate();
		  			start = Now.GetTime();
		  			getActivity().startService(recordIntent);
		  			timer_test.setVisibility(View.VISIBLE);
		  			
		  			 //update(latitude,longitude);
		  			 
		  				
		  			// mLocationClient.stop();
		    	} 
		    	else {	
		  			// stop audio recording
		  			Intent recordIntent = new Intent(getActivity(), AudioRecordService.class);
		  			getActivity().stopService(recordIntent);
		  			timer_test.stop();
		  			end = Now.GetTime();
		  			tb_test.setClickable(false);
		  			// start speaker counting test
		  			new Test().execute();
		  			timer_test.setVisibility(View.INVISIBLE);
		  			tb_test.setClickable(true);
		  			 System.out.println("我的经度为"+latitude+"我的纬度为"+longitude);	
		  			mLocationClient.stop();
		  			if (mLocationClient.isStarted()) {
		  				 mLocationClient.stop();
					}
		  			record.setVisibility(View.INVISIBLE);
		  			testMapView.setVisibility(View.VISIBLE);
		  			anim = (AnimationDrawable) record.getDrawable(); 
		  			anim.stop();
//		  			new S3PutDataBaseTask().execute();
		      }
	    	}
	    }
		});
		return view;
	}

	/** Calibration task. */
	class Calibration extends AsyncTask<String, String, Integer> {
		@Override
		protected Integer doInBackground(String... arg0) {
			// generate the MFCC and pitch feature data
			try {
				Yin.writeFile(calWavFile);
				Log.i("SpeakerCountTask", "Finish YIN");
				MFCC.writeFile(calWavFile);
				Log.i("SpeakerCountTask", "Finish MFCC");
//				new S3PutDataBaseTask().execute();
				
				// calibration succeeded with enough audio data
				if (SpeakerCount.selfCalibration(calWavFile)) {
					return 1;
				}
				// calibration failed without enough audio data
				else {
					Log.i("CalibrationTask", "Failed");
					return 0;
				}
		  } catch (IOException e) {
		  	e.printStackTrace();
				return 0;		  	
			} catch (Exception e) {
				e.printStackTrace();
				return 0;		  	
			}
		}
		
    @Override
    protected void onProgressUpdate(String... values) {

    }

    @Override
    protected void onCancelled(Integer result) {
    	
    }
      
    @Override
    protected void onPreExecute() {
    	
    }

    @Override
    protected void onPostExecute(Integer result) {
    	if (result == 1) {
  			Toast.makeText(getActivity(), "Congratulation! You are all set for the calibration.", Toast.LENGTH_SHORT).show();		
      	tv_cal_content.setText("You are all set for the calibration.");
		 		//new S3PutDataBaseTask().execute(); 
    	}
    	else if (result == 0) {
  			Toast.makeText(getActivity(), "Calibration data is not sufficient (less than 30 seconds), please do it again...", Toast.LENGTH_SHORT).show();	
      	tv_cal_content.setText("Crowd++ needs your voice to calibrate the system.");
    	}
    }
	}
	
	/** Speaker counting task. */
	class Test extends AsyncTask<String, String, Integer> {
		@Override
		protected Integer doInBackground(String... arg0) {
			// generate the MFCC and pitch feature data
			try {
				Yin.writeFile(testWavFile);
				Log.i("SpeakerCountTask", "Finish YIN");
				MFCC.writeFile(testWavFile);
				Log.i("SpeakerCountTask", "Finish MFCC");				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			String[] tst_files = new String[2];
			tst_files[0] = testWavFile + ".jstk.mfcc.txt";
			tst_files[1] = testWavFile + ".YIN.pitch.txt";
			
			// semisupervised speaker counting with owner's calibration data 
//		  if (Constants.calibration()) {
//				String[] cal_files = new String[2];
//				cal_files[0] = calWavFile + ".jstk.mfcc.txt";
//				cal_files[1] = calWavFile + ".YIN.pitch.txt";
//				try {
//					double rv[] = SpeakerCount.semisupervised(tst_files, cal_files);
//					speaker_count = (int)rv[0];
//					percentage = rv[1];	
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//	    }
			// unsupervised speaker counting without calibration data 
//	else {
			try {
				double [][] result;
				result = SpeakerCount.unsupervised(tst_files);
				int rows = result.length - 1;
				int cols = result[0].length;
				speaker_count = (int)result[rows][0];
				System.out.println(speaker_count);
				if(speaker_count != 0){
					mfcc = new double [rows][cols];
					gender = new int[rows];
					
					
					percentage = -1;
					for(int i = 0; i < rows; i++){
						for(int j = 0; j < cols; j++){
							mfcc[i][j] = result[i][j];
						}
						gender[i] = (int)result[rows][i+1];
					}
				}
			
			} catch (IOException e) {
				e.printStackTrace();
			}
//			}
		  
		  // log the test record 
			if (Constants.log) {
				String log	= testWavFile + "\tDate:\t" + date + "\tstart:\t" + start + "\tend:\t" + end
										+ "\tspeaker count:\t" + Integer.toString(speaker_count) + "\tspeach percentage:\t" + Double.toString(percentage)
										+ "\tlatitude:\t" + Double.toString(latitude) + "\tlongitude:\t" + Double.toString(longitude) + "\n";			
				
				File sdFile = new File(testDir, "log.txt");
				FileOutputStream fos;
				try {
					fos = new FileOutputStream(sdFile, true);
					fos.write(log.getBytes());
					fos.close();				
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// insert the record into the test table
			sys_time = System.currentTimeMillis();
			mDatabase.insertTest(mDB, sys_time, date, start, end, speaker_count, percentage, latitude, longitude);
			//test_log = "Recent ten records:\n";
			mCursor = mDatabase.queryTest(mDB);
			if (mCursor.moveToFirst()) {
				test_log = "Date\t\t\t\t\t\t\t\t\t\tTime\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tPeople\n";
				int record_num = 0;
				while (mCursor.isAfterLast() == false) {
					String record = mCursor.getString(0) + "\t\t\t" 
												+ mCursor.getString(1) + " - "  + mCursor.getString(2) + "\t\t\t" 
												+ mCursor.getString(3);
					test_log = test_log + record;						
					mCursor.moveToNext();
					record_num++;
					if (record_num == 1)
						break;
				}					
			}
			Log.i("SpeakerCountTask", "Finish writing file");
			
			
			if (!Constants.test_raw_keep) {
				FileProcess.deleteFile(testWavFile);
			}
			if (!Constants.test_feature_keep) {
				FileProcess.deleteFile(tst_files[0]);
				FileProcess.deleteFile(tst_files[1]);					
			}
			
			return speaker_count;
			
		}
		
    @Override
    protected void onProgressUpdate(String... values) {
    	tv_debug.setText("Counting...");
			Log.i("SpeakCountTask", "Counting");
    }

    @Override
    protected void onCancelled(Integer result) {
			Log.i("SpeakCountTask", "Cancelled");
    }
      
    @Override
    protected void onPreExecute() {
			Log.i("SpeakCountTask", "Begin to count");
    }

    @Override
    protected void onPostExecute(Integer result) {
    	tv_debug.setText("Speaker count: " + result);
    	tv_record.setText(test_log);
    	if(latitude != -1){
    		LatLng p = new LatLng(latitude,longitude);
			testBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(p));
			testBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(18));
			if(speaker_count != 0){
				for(int i = 0; i < gender.length; i++){
					LatLng q = new LatLng(latitude,longitude + 0.0002*i);
					if(gender[i] == 1){
						testBaiduMap.addOverlay(new MarkerOptions().position(q)
								.icon(BitmapDescriptorFactory
										.fromResource(R.drawable.he)));
					}
					if(gender[i] == 2){
						testBaiduMap.addOverlay(new MarkerOptions().position(q)
								.icon(BitmapDescriptorFactory
										.fromResource(R.drawable.she)));
					}
				}
				
			}
			mLocationClient.stop();
		}
    	else{
    		Toast.makeText(getActivity(), "Can't get your location...", Toast.LENGTH_SHORT).show();
    	}
    	
    }
    
 
    
	}

	/** Put the calibration data into Amazon S3. */
//	private class S3PutDataBaseTask extends AsyncTask<String, String, Integer> {
//
//		protected void onPreExecute() {
//
//		}	
//		
//		protected Integer doInBackground(String... params) {
//			AWSCredentials credentials = new BasicAWSCredentials(Constants.ACCESS_KEY_ID, Constants.SECRET_KEY);
//			SCS client = new SCSClient(credentials);
//			List<Bucket> list = client.listBuckets();  
//			  
//			for (Bucket bucket : list)   
//			{             
//			    System.out.println("Bucket: " + bucket.getName());        
//			}  
//			File wavFile = new File(testWavFile);
//			try {
////				PutObjectResult putObjectResult = conn.getObjectMetadata(Constants.dbBucket, Constants.PHONE_ID + ".wav", wavFile);
//				PutObjectRequest por = new PutObjectRequest(Constants.dbBucket, Constants.PHONE_ID + ".wav", wavFile);  
//				client.putObject(por);
//			} catch (Exception exception) {
//				Log.e("Upload", "Error");
//				return 0;
//			}
//			
//			File mfccFile = new File(testWavFile + ".jstk.mfcc.txt");
//			try {
//				PutObjectRequest por = new PutObjectRequest(Constants.dbBucket, Constants.PHONE_ID + ".jstk.mfcc.txt", mfccFile);  
//				client.putObject(por);
//			} catch (Exception exception) {
//				Log.e("Upload", "Error");
//				return 0;
//			}
//			
//			File pitchFile = new File(calWavFile + ".YIN.pitch.txt");
//			try {
//				PutObjectRequest por = new PutObjectRequest(Constants.dbBucket, Constants.PHONE_ID + ".YIN.pitch.txt", pitchFile);  
//				client.putObject(por);
//			} catch (Exception exception) {
//				Log.e("Upload", "Error");
//				return 0;
//			}
//			return 1;
//		}
//	
//		protected void onPostExecute(Integer result) {	
//
//		}
//	}
	
	
	
	@Override
	public void onResume() {
	  super.onResume();
	  
	 
	}

	@Override
	public void onPause() {
		super.onPause();
		
	}

	@Override
	public void onDestroy() {
		 if ( mLocationClient.isStarted()) {
				mLocationClient.stop();
			}
		
		if (mDB != null) {
			mDB.close();
		}
		if (mDatabase != null)	{
			mDatabase.close();
		}
		Log.i("HomeFragment", "Destroying HomeFragment");
		super.onDestroy();		
	}
	
	
	 private  void initLocation(){
	        LocationClientOption option = new LocationClientOption();
	        option.setLocationMode(tempMode);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
	        option.setCoorType(tempcoor);//可选，默认gcj02，设置返回的定位结果坐标系，
	        int span=7000;
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
	public void update(double lat, double lon) {
		this.latitude = lat;
		this.longitude = lon;
//		LatLng p = new LatLng(latitude,longitude);
//		testBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(p));
//		testBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(18));
//		testBaiduMap.addOverlay(new MarkerOptions().position(p)
//				.icon(BitmapDescriptorFactory
//						.fromResource(R.drawable.icon_gcoding)));
		mLocationClient.stop();
		
	}
	private String arraytostring(double[][] mfcc){
		String result = "";
		for(int i = 0; i < mfcc.length; i++){
			if(i < mfcc.length-1)
				result += Arrays.toString(mfcc[i])+",";
			else
				result += Arrays.toString(mfcc[i]);	
		}
		return result;
		
	}
	
	
	
//	/** Put the database into Amazon S3. */
//	private class S3PutDataBaseTask extends AsyncTask<String, String, Integer> {
//
//		protected void onPreExecute() {
//
//		}	
//		
//		protected Integer doInBackground(String... params) {
//			File dbFile = new File(getApplicationContext().getDatabasePath(DataBaseHelper.dbName).toString());
//			try {
//				PutObjectRequest por = new PutObjectRequest(Constants.calBucket, Constants.dbName, dbFile);  
//				s3Client.putObject(por);
//			} catch (Exception exception) {
//				Log.e("Upload", "Error");
//				return 0;
//			}
//			return 1;
//		}
//	
//		protected void onPostExecute(Integer result) {	
//
//		}
//	}
	
}
