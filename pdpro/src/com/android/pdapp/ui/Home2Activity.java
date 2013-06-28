package com.android.pdapp.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import com.android.pdapp.R;
import com.android.pdapp.adapter.ListViewPMPAdapter;
import com.android.pdapp.app.AppConfig;
import com.android.pdapp.common.UIHelper;
import com.android.pdapp.dao.PdmxDAO;
import com.android.pdapp.dateutil.ClientDBHelper;
import com.android.pdapp.ui.MyListView.OnRefreshListener;
import com.android.pdapp.model.*;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.ViewFlipper;

public class Home2Activity extends BaseActivity implements OnScrollListener {

	private RadioButton homefirst;
	private RadioButton homeSecond;
	private ImageButton headAdd;
	private ImageButton headpmpAdd;
	private View loadMoreView;
	private Button loadMoreButton;
	private View head_ymk;
	private View head_pmp;
	private int visibleLastIndex = 0; //最后的可视项索引
	private int visibleItemCount;  //当前窗口的可见项数目
	private int datasieze = 50;
	
	private Handler hander = new Handler();

	// 通过ViewFlipper在多个View间切换
	private ViewFlipper flipper;
	private BaseAdapter adapter;

	
	private List<Barinfo> date;
	private MyListView pdListView,mxListView;

	private SharedPreferences sharedPreference;
	private ClientDBHelper dbHelper;
	private PdmxDAO pmpDAO;
	private boolean loginresult;
	
	private String plant = "";
	private String pdnum = "";
	private String userid = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home2);

		this.initContent();

		// 判断用户是否登录
		if (super.isLogin()) {
			// 初始化
			this.initFooter();
			this.initHeader();
			this.findViews();
			this.bindListener();
		} else {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
		}

	}

	private void initContent() {
		// TODO Auto-generated method stub
		// detector = new GestureDetector(this, this);
		sharedPreference = getSharedPreferences(AppConfig.App_Config, 0);
		head_ymk = findViewById(R.id.head_ymk);
		head_pmp = findViewById(R.id.head_pmp);
	}

	/***
	 * 绑定按钮
	 */
	private void bindListener() {
		// 点击首页
		homefirst.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				homeSecond.setChecked(false);
				flipper.setDisplayedChild(0);
				head_ymk.setVisibility(View.VISIBLE);
				head_pmp.setVisibility(View.GONE);
			}
		});
		homeSecond.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				homefirst.setChecked(false);
				flipper.setDisplayedChild(1);
				head_ymk.setVisibility(View.GONE);
				head_pmp.setVisibility(View.VISIBLE);
				//绑定数据
				final Handler handler = new Handler() {
					
					@Override
					public void handleMessage(Message msg) {
						// 
						if (msg.what == 1) {
							date = (ArrayList<Barinfo>)msg.obj;
							Log.d("count", String.valueOf(date.size()));
							adapter = new ListViewPMPAdapter(getApplicationContext(), date, R.layout.pmp_list_item);
							mxListView.setAdapter(adapter);
						}
					}
				};
				new Thread()
				{
					@Override
					public void run()
					{
						Message msg = new Message();
						try {
							date = pmpDAO.getPMPDateByUser("NAPP", "5412", userid);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (XmlPullParserException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(date!=null)
						{
							msg.obj = date;
							msg.what=1;
						}else
						{
							msg.what = 0;
						}
						handler.sendMessage(msg);
					}
				}
				.start();
				
				
			}
			
		});
	}

	// 初始化ViewFlipper
	private void findViews() {
		// TODO Auto-generated method stub
		flipper = (ViewFlipper) findViewById(R.id.flipper);
		pmpDAO = new PdmxDAO(getApplicationContext());
		LayoutInflater layoutInflater = this.getLayoutInflater();
		View PDView = layoutInflater.inflate(R.layout.pdlist, null);
		View MXView = layoutInflater.inflate(R.layout.mxlist, null);
		
		
		
		// List<Map<String,Object>> mxList = appclient.getpdMXList("");
		//
		// ListAdapter adapter = new SimpleAdapter(this, mxList,
		// R.layout.listitem, new String[]{"trkno"}, new int[]{R.id.pdid});
		/**
		 * 盘点总表
		 */
		pdListView = (MyListView) PDView.findViewById(R.id.pdlistview);
		
		/***
		 * 盘点明细表的数据显示
		 */
		mxListView = (MyListView) MXView.findViewById(R.id.mxListView);
		//分页
		loadMoreView = getLayoutInflater().inflate(R.layout.loadmore, null);
		loadMoreButton = (Button) loadMoreView.findViewById(R.id.loadMoreButton);
		loadMoreButton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						loadMoreButton.setText("正在加载中...");
						hander.postDelayed(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								loadMoreData();
								adapter.notifyDataSetChanged();
								loadMoreButton.setText("查看更多...");
							}
						}, 2000);
					}
				});
		/*
		if(homeSecond.isChecked())
		{
			date = pmpDAO.getPMPDateByUser(plant, pdnum, userid);
			adapter = new ListViewPMPAdapter(getApplicationContext(), date, R.layout.pmp_list_item);
		
		adapter = new BaseAdapter() {
			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				convertView = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.pmp_list_item, null);
				TextView textView = (TextView) convertView
						.findViewById(R.id.textView_item);
				textView.setText(date.get(position));
				RelativeLayout pmpitem = (RelativeLayout) convertView.findViewById(R.id.pmp_item);
					pmpitem.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(HomeActivity.this,PdInfoActivity.class);
						intent.putExtra("trkno", date.get(position));
						startActivity(intent);
					}
				});
				return convertView;
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public Object getItem(int position) {
				return date.get(position);
			}

			@Override
			public int getCount() {
				return date.size();
			}
		};
		
		//Log.i("adapter", String.valueOf(adapter.getCount()));
		//list与adapter绑定
		mxListView.setAdapter(adapter);
		//Log.i("listview",String.valueOf(mxListView.getCount()));
		
		}
		*/
		
		
		//向下拉刷新
		mxListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				RefreshTask rTask = new RefreshTask();
				rTask.execute(2000);
			}
		});
		
		
		flipper.addView(PDView, 0);
		flipper.addView(MXView, 1);
		// 设置初始化第一个View
		homefirst.setChecked(true);
		flipper.setDisplayedChild(0);
		Bundle bundle = getIntent().getExtras();
		if(bundle!=null)
		{
			String pdnum = bundle.getString("pdnum");
			
		}
		else
		{
			head_ymk.setVisibility(View.VISIBLE);
			head_pmp.setVisibility(View.GONE);
		}
		
		
		pdListView.addFooterView(loadMoreView);
		mxListView.addFooterView(loadMoreView);
		pdListView.setOnScrollListener(this);
		mxListView.setOnScrollListener(this);
		
	}
	
	/**
	 * 加载更多数据
	 * @author Yang Hong
	 *
	 */
	private void loadMoreData()
	{
		int count = adapter.getCount();
		Log.i("count", String.valueOf(count));
		if(count + 10 <=datasieze)
		{
			
		}else
		{
			for(int i=count+1;i<=datasieze;i++)
			{
				
			}
		}
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
			// 在data最前添加数据
			try {
				date = pmpDAO.getPMPDateByUser("8420", pdnum, userid);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			adapter.notifyDataSetChanged();
			mxListView.onRefreshComplete();
		}
	}
	/*
	// 取得屏幕尺寸大小
	public void displayScreenSize() {
		// 屏幕方面切换时获得方向
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			setTitle("landscape");
		}

		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			setTitle("portrait");
		}

		// 获得屏幕大小1
		WindowManager manager = getWindowManager();
		//int width = manager.getDefaultDisplay().getWidth();
		//int height = manager.getDefaultDisplay().getHeight();

		//Log.v("am10", "width: " + width + " height: " + height);

		// 获得屏幕大小2
		DisplayMetrics dMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
		//int screenWidth = dMetrics.widthPixels;
		//int screenHeight = dMetrics.heightPixels;

		//Log.v("am10", "screenWidth: " + screenWidth + " screenHeight: "+ screenHeight);
	}
*/
	/***
	 * 初始化头部
	 */
	private void initHeader() {
		// TODO Auto-generated method stub
		headAdd = (ImageButton) findViewById(R.id.home_head_add);
		headpmpAdd = (ImageButton) findViewById(R.id.home_head_pmp_add);
		
		headAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 判断是否有权限
				Intent intent = new Intent(Home2Activity.this,
						AddPDActivity.class);
				startActivity(intent); 
			}
		});
		
		headpmpAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 判断是否有权限
				if (isAdmin()) {
					// 添加盘点编号
					Intent intent = new Intent(Home2Activity.this,
							AddMXActivity.class);
					startActivity(intent);
				} else {
					UIHelper.ToastMessage(Home2Activity.this,
							getString(R.string.no_authdo));
				}
			}
		});
	}

	/***
	 * 判断登录用户是否为管理员
	 * 
	 * @return
	 */
	protected boolean isAdmin() {
		// TODO Auto-generated method stub
		return true;
	}

	/***
	 * 初始化footer
	 */
	private void initFooter() {
		// TODO Auto-generated method stub
		homefirst = (RadioButton) findViewById(R.id.home_footbar_first);
		homeSecond = (RadioButton) findViewById(R.id.home_footbar_second);
	}

	/**
	 * 创建菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
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

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		this.visibleItemCount = visibleItemCount;
		visibleLastIndex = firstVisibleItem + visibleItemCount-1;
		
		//如果所有记录选项等于数据及条数，则移除列表底部视图
		if(totalItemCount == datasieze)
		{
			if(homefirst.isChecked())
			{
				pdListView.removeFooterView(loadMoreView);
			}else
			{
				mxListView.removeFooterView(loadMoreView);
			}
			UIHelper.ToastMessage(this, "数据已全部加载");
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		// 数据集最后一项的索引
		int itemsLastIndex = adapter.getCount()-1;
		int lastIndex = adapter.getCount();
		if(scrollState== OnScrollListener.SCROLL_STATE_IDLE
				&& visibleLastIndex == lastIndex)
		{
			//如果是自动加载，可以在这里放置异步加载数据的代码
		}
	}

}
