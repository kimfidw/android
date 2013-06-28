package com.android.pdapp;


import java.util.LinkedList;

import com.android.pdapp.ui.MyListView;
import com.android.pdapp.ui.MyListView.OnRefreshListener;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LVFlashActivity extends Activity 
{

	private LinkedList<String> data;
	private BaseAdapter adapter;
	private MyListView listView;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lvflash);

		// 取得屏幕尺寸大小
		displayScreenSize();
		
		data = new LinkedList<String>();
	    for(int i=0;i<10;i++)
	    {
	    	data.add(String.valueOf(i));
	    }

		listView = (MyListView) findViewById(R.id.listview);
		
		adapter = new BaseAdapter() 
		{
			@Override
			public View getView(int position, View convertView, ViewGroup parent) 
			{
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.pmp_list_item, null);
				TextView textView = (TextView) convertView.findViewById(R.id.textView_item);
				textView.setText(data.get(position));
				return convertView;
			}

			@Override
			public long getItemId(int position) 
			{
				return position;
			}

			@Override
			public Object getItem(int position) 
			{
				return data.get(position);
			}

			@Override
			public int getCount() 
			{
				return data.size();
			}
		};
		
		listView.setAdapter(adapter);

		listView.setOnRefreshListener(new OnRefreshListener() 
		{
			@Override
			public void onRefresh() 
			{
				RefreshTask rTask = new RefreshTask();
				rTask.execute(2000);
			}
		});
	}
	
	// AsyncTask异步任务
	class RefreshTask extends AsyncTask<Integer, Integer, String>
    {    
    	@Override
		protected String doInBackground(Integer... params) 
		{
			try 
			{
				Thread.sleep(2500);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			// 在data最前添加数据
			data.addFirst("刷新后的内容");
			//Log.i("add", "添加数据");
			return null;
		}	

		@Override
		protected void onPostExecute(String result) 
		{
			super.onPostExecute(result);
			adapter.notifyDataSetChanged();
			listView.onRefreshComplete();
		}    	
    }
	
	// 取得屏幕尺寸大小
	public void displayScreenSize()
	{
		// 屏幕方面切换时获得方向
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) 
		{		
			setTitle("landscape");
		}
		
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) 
		{
			setTitle("portrait");
		}
		
		// 获得屏幕大小1
		WindowManager manager = getWindowManager();
		int width = manager.getDefaultDisplay().getWidth();
		int height = manager.getDefaultDisplay().getHeight();
		
		Log.v("am10", "width: " + width + " height: " + height);
		
		// 获得屏幕大小2
		DisplayMetrics dMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
		int screenWidth = dMetrics.widthPixels;
		int screenHeight = dMetrics.heightPixels;
		
		Log.v("am10", "screenWidth: " + screenWidth + " screenHeight: " + screenHeight);
	}
}