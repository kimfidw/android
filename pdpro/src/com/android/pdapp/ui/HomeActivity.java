package com.android.pdapp.ui;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import com.android.pdapp.R;
import com.android.pdapp.adapter.ListViewPMPAdapter;
import com.android.pdapp.common.UIHelper;
import com.android.pdapp.dao.HomeDAO;
import com.android.pdapp.dao.PdmxDAO;
import com.android.pdapp.model.Barinfo;
import com.android.pdapp.ui.MyListView.OnRefreshListener;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;

public class HomeActivity extends BaseActivity {

	private ImageButton AddPMK;
	private ImageButton AddPMP;
	private View head;
	private View content;
	private MyListView pmpListView;
	private PdmxDAO pmpDAO; 
	
	private HomeDAO homeDAO;
	private List<Barinfo> date =null;
	private BaseAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		if(super.isLogin())
		{
			
			this.PageInit();
			
			this.PageHeader();
			
			if(networkIsConnect())
			{
				this.bindView();
			}else
			{
				UIHelper.ToastMessage(HomeActivity.this, getString(R.string.network_disconnect));
			}
		}
		else
		{
			Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
			startActivity(intent);
			finish();
		}
		
	}

	/**
	 * 绑定数据
	 */
	private void bindView() {
		
		homeDAO = new HomeDAO(getApplicationContext());
		
		//向下拉刷新
		pmpListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				if(networkIsConnect())
				{
					RefreshTask rTask = new RefreshTask();
					rTask.execute(2000);
					/**
					 * 重新绑定数据
					 */
					if(date!=null){	
						adapter = new ListViewPMPAdapter(getApplicationContext(), date, R.layout.pmp_list_item);
						pmpListView.setAdapter(adapter);
							
					}
					else
					{
						UIHelper.ToastMessage(HomeActivity.this, getString(R.string.pddate_null));
						//adapter.notifyDataSetChanged();
						return;
					}
				}else
				{
					UIHelper.ToastMessage(HomeActivity.this, getString(R.string.network_disconnect));
				}
			}
		});
		
		final Handler handle = new Handler(Looper.getMainLooper()){
			@Override
			public void handleMessage(Message msg) {
				// 
				if (msg.what == 1) {
					date = (ArrayList<Barinfo>)msg.obj;	
					adapter = new ListViewPMPAdapter(getApplicationContext(), date, R.layout.pmp_list_item);
					pmpListView.setAdapter(adapter);
					
					pmpListView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							for(int i=0;i<arg0.getCount();i++)
							{
								View v = arg0.getChildAt(i);
								if(i==arg2)
								{
									((ListViewPMPAdapter) adapter).setSelectedPosition(arg2);
									v.setBackgroundResource(R.color.list_view_select);
									Intent intent = new Intent(HomeActivity.this,PdInfoActivity.class);
									intent.putExtra("barinfo", (Barinfo)arg0.getItemAtPosition(i));
									startActivity(intent);
								}else
								{
									v.setBackgroundResource(R.color.white);
								}
							}
							
						}
					});
					
				}else if(msg.what==0)
				{
					UIHelper.ToastMessage(HomeActivity.this, getString(R.string.pddate_null));
					return;
				}
				else if(msg.what==2)
				{
					UIHelper.ToastMessage(HomeActivity.this, getString(R.string.network_slow));
					return;
				}else if(msg.what ==3)
				{
					UIHelper.ToastMessage(HomeActivity.this, getString(R.string.network_timeout));
					return;
				}
			}
		};
		new Thread()
		{
			@Override
			public void run()
			{
				Message msg = new Message();
				// 加载数据前删除缓存
				//date = pmpDAO.getPMPDateByUser(plant, pdnum, userid);
				try {
					date = homeDAO.LoadDate(plant, pdnum, userid);
					if(date!=null)
					{
						msg.obj = date;
						msg.what=1;
					}else
					{
						msg.what = 0;
					}
				} 
				catch(SocketTimeoutException e)
				{
					msg.what = 3;
					e.printStackTrace();
				}
				catch (IOException e) {
					msg.what = 2;
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				handle.sendMessage(msg);
			}
		}
		.start();
		
		
	}


	/***
	 * 头部页面初始化
	 */
	private void PageHeader() {
		AddPMK = (ImageButton) head.findViewById(R.id.pmk_add);
		AddPMP = (ImageButton) head.findViewById(R.id.head_add);
		//盘点编号维护
		AddPMK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!getAuth_edit().equals(""))
				{
					Intent intent = new Intent(HomeActivity.this,AddPDActivity.class);
					startActivity(intent);
				}else
				{
					UIHelper.ToastMessage(HomeActivity.this, getString(R.string.no_authedit));
					return;
				}
			}
		});
		//盘点作业
		AddPMP.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this,
						AddMXActivity.class);
				startActivity(intent);
				/*
				if(!getAuth_do().equals(""))
				{
					Intent intent = new Intent(HomeActivity.this,
							AddMXActivity.class);
					startActivity(intent);
				}else
				{
					UIHelper.ToastMessage(HomeActivity.this, getString(R.string.no_authdo));
					return;
				}
				*/
			}
		});
		
		
	}


	/**
	 * 初始化界面
	 */
	private void PageInit() {
		// TODO Auto-generated method stub
		head = findViewById(R.id.head_pmp);
		content = findViewById(R.id.content_pmp);
		pmpListView = (MyListView)content.findViewById(R.id.mxListView);
		
	}
	
	
	// AsyncTask异步任务
	class RefreshTask extends AsyncTask<Integer, Integer, String> {
		@Override
		protected String doInBackground(Integer... params) {
			try {
				Thread.sleep(2500);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 加载数据前删除缓存
			//date = pmpDAO.getPMPDateByUser(plant, pdnum, userid);
			homeDAO = new HomeDAO(getApplicationContext());
			String result = "";
			try {
				date = homeDAO.reflashDate(plant, pdnum, userid);
			} 
			catch(SocketTimeoutException e)
			{
				result = getString(R.string.network_timeout);
				//UIHelper.ToastMessage(HomeActivity.this, getString(R.string.network_timeout));
				e.printStackTrace();
			}
			catch (IOException e) {
				//UIHelper.ToastMessage(HomeActivity.this, getString(R.string.network_slow));
				result = getString(R.string.network_slow);
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			//Log.d("exec", "执行");
			if(result!="")
			{
				UIHelper.ToastMessage(HomeActivity.this, result);
			}else
			{
				adapter = new ListViewPMPAdapter(getApplicationContext(), date, R.layout.pmp_list_item);
				pmpListView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				pmpListView.onRefreshComplete();
			}
			
			
		}
		
	}

	/**
	 * 按两次返回键退出
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
        return super.ExitByReturnBtn(keyCode, event);
	}

	/**
	 * 创建菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu,menu);
		return true;
	}
	
	/***
	 * 菜单处理
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.MenuSelected(item);
		return true;
	}

}
