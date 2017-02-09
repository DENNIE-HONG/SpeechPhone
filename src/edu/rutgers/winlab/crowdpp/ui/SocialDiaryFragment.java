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

import java.util.ArrayList;
import java.util.List;

import com.baidu.baidulocationdemo.LocationActivityTwo;
import com.baidu.baidulocationdemo.MyLocationMapActivity;

import de.fau.cs.jstk.app.Convert;

import edu.rutgers.winlab.crowdpp.R;
import edu.rutgers.winlab.crowdpp.db.DataBaseHelper;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * The social diary fragment 
 * @author Luyan Hong
 */
public class SocialDiaryFragment extends Fragment {
	
//	Button bt_date;
//	TextView tv_record;
	Button btn_start_baidumap;
	ListView mListView;
	List<TestBin> mlist = new ArrayList<TestBin>();
	
	DataBaseHelper mDatabase = null; 
	Cursor mCursor = null;
	SQLiteDatabase mDB = null;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.socialdiary_fragment_layout, container, false);				
	//	btn_start_baidumap = (Button) view.findViewById(R.id.btn_start_baidumap);
		mListView = (ListView) view.findViewById(R.id.lv_item);
		initDB();
		mListView.setAdapter(new MyAdapter());
		
		

		//map点击
//		btn_start_baidumap.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				//startActivity(new Intent(getActivity(), MainActivity.class));
//				startActivity(new Intent(getActivity(), MyLocationMapActivity.class));
//				
//			}
//		});
		return view;
	}
	
	private void initDB(){
		mDatabase = new DataBaseHelper(getActivity().getApplicationContext());
		mDB = mDatabase.getWritableDatabase();
		Cursor c = null;
		try {
			c = mDB.rawQuery("select date,start,end,count,latitude,longitude from Test order by time desc limit 50", null);
			if (c.moveToFirst()) {
				do{
					TestBin bin = new TestBin();
					bin.setmDate(c.getString(0));
					bin.setStartTime(c.getString(1));
					bin.setEndTime(c.getString(2));
					bin.setCount(c.getInt(3));
					bin.setLatitude(c.getFloat(4));
					bin.setLongitude(c.getFloat(5));
					mlist.add(bin);
					
				}while(c.moveToNext());
			}
		}finally{
			if (c!=null) {
				c.close();
			}
		}
		
	}
	
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mlist.size();
		}

		@Override
		public Object getItem(int position) {
			return mlist.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final TestBin bin = mlist.get(position);
			ViewHolder holder;
			if(convertView==null){
				holder = new ViewHolder();
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.test_item, null);
				holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
				holder.tv_start = (TextView) convertView.findViewById(R.id.tv_start);
				holder.tv_end = (TextView) convertView.findViewById(R.id.tv_end);
				holder.tv_count = (TextView) convertView.findViewById(R.id.tv_count);
				holder.tv_latitude = (TextView) convertView.findViewById(R.id.tv_latitude);
				holder.tv_longitude = (TextView) convertView.findViewById(R.id.tv_longitude);
				holder.ll_item = (LinearLayout) convertView.findViewById(R.id.ll_item);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_date.setText("Date: "+bin.getmDate());
			holder.tv_start.setText("start:"+bin.getStartTime());
			holder.tv_end.setText("end:"+bin.getEndTime());
			holder.tv_count.setText("count:"+bin.getCount());
			holder.tv_latitude.setText("latitude:"+bin.getLatitude());
			holder.tv_longitude.setText("longitude:"+bin.getLongitude());
			holder.ll_item.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					Intent intent = new Intent(getActivity(), MyLocationMapActivity.class);
//					intent.putExtra("count", bin.getCount());
//					intent.putExtra("latitude", bin.getLatitude());
//					intent.putExtra("longitude", bin.getLongitude());
//					startActivity(intent);
					Intent i = new Intent(getActivity(), LocationActivityTwo.class);
					i.putExtra("count", bin.getCount());
					i.putExtra("date", bin.getmDate());
					//i.putExtra("start", bin.getStartTime());
					//i.putExtra("end", bin.getEndTime());
					i.putExtra("lat", bin.getLatitude());
					i.putExtra("lon", bin.getLongitude());
					startActivity(i);
					
				}
			});
			
			
			
			
			return convertView;
		}
		
		
		
		
		class ViewHolder{
		 TextView tv_date,tv_start,tv_end,tv_count,tv_latitude,tv_longitude;
		 LinearLayout ll_item;
		}
		
	}
}




