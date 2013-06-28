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
	private int visibleLastIndex = 0; //���Ŀ���������
	private int visibleItemCount;  //��ǰ���ڵĿɼ�����Ŀ
	private int datasieze = 50;
	
	private Handler hander = new Handler();

	// ͨ��ViewFlipper�ڶ��View���л�
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

		// �ж��û��Ƿ��¼
		if (super.isLogin()) {
			// ��ʼ��
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
	 * �󶨰�ť
	 */
	private void bindListener() {
		// �����ҳ
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
				//������
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

	// ��ʼ��ViewFlipper
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
		 * �̵��ܱ�
		 */
		pdListView = (MyListView) PDView.findViewById(R.id.pdlistview);
		
		/***
		 * �̵���ϸ���������ʾ
		 */
		mxListView = (MyListView) MXView.findViewById(R.id.mxListView);
		//��ҳ
		loadMoreView = getLayoutInflater().inflate(R.layout.loadmore, null);
		loadMoreButton = (Button) loadMoreView.findViewById(R.id.loadMoreButton);
		loadMoreButton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						loadMoreButton.setText("���ڼ�����...");
						hander.postDelayed(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								loadMoreData();
								adapter.notifyDataSetChanged();
								loadMoreButton.setText("�鿴����...");
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
		//list��adapter��
		mxListView.setAdapter(adapter);
		//Log.i("listview",String.valueOf(mxListView.getCount()));
		
		}
		*/
		
		
		//������ˢ��
		mxListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				RefreshTask rTask = new RefreshTask();
				rTask.execute(2000);
			}
		});
		
		
		flipper.addView(PDView, 0);
		flipper.addView(MXView, 1);
		// ���ó�ʼ����һ��View
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
	 * ���ظ�������
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

	// AsyncTask�첽����
	class RefreshTask extends AsyncTask<Integer, Integer, String> {
		@Override
		protected String doInBackground(Integer... params) {
			try {
				Thread.sleep(2500);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// ��data��ǰ�������
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
	// ȡ����Ļ�ߴ��С
	public void displayScreenSize() {
		// ��Ļ�����л�ʱ��÷���
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			setTitle("landscape");
		}

		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			setTitle("portrait");
		}

		// �����Ļ��С1
		WindowManager manager = getWindowManager();
		//int width = manager.getDefaultDisplay().getWidth();
		//int height = manager.getDefaultDisplay().getHeight();

		//Log.v("am10", "width: " + width + " height: " + height);

		// �����Ļ��С2
		DisplayMetrics dMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
		//int screenWidth = dMetrics.widthPixels;
		//int screenHeight = dMetrics.heightPixels;

		//Log.v("am10", "screenWidth: " + screenWidth + " screenHeight: "+ screenHeight);
	}
*/
	/***
	 * ��ʼ��ͷ��
	 */
	private void initHeader() {
		// TODO Auto-generated method stub
		headAdd = (ImageButton) findViewById(R.id.home_head_add);
		headpmpAdd = (ImageButton) findViewById(R.id.home_head_pmp_add);
		
		headAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// �ж��Ƿ���Ȩ��
				Intent intent = new Intent(Home2Activity.this,
						AddPDActivity.class);
				startActivity(intent); 
			}
		});
		
		headpmpAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// �ж��Ƿ���Ȩ��
				if (isAdmin()) {
					// ����̵���
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
	 * �жϵ�¼�û��Ƿ�Ϊ����Ա
	 * 
	 * @return
	 */
	protected boolean isAdmin() {
		// TODO Auto-generated method stub
		return true;
	}

	/***
	 * ��ʼ��footer
	 */
	private void initFooter() {
		// TODO Auto-generated method stub
		homefirst = (RadioButton) findViewById(R.id.home_footbar_first);
		homeSecond = (RadioButton) findViewById(R.id.home_footbar_second);
	}

	/**
	 * �����˵�
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	/***
	 * �˵�����
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
		
		//������м�¼ѡ��������ݼ����������Ƴ��б�ײ���ͼ
		if(totalItemCount == datasieze)
		{
			if(homefirst.isChecked())
			{
				pdListView.removeFooterView(loadMoreView);
			}else
			{
				mxListView.removeFooterView(loadMoreView);
			}
			UIHelper.ToastMessage(this, "������ȫ������");
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		// ���ݼ����һ�������
		int itemsLastIndex = adapter.getCount()-1;
		int lastIndex = adapter.getCount();
		if(scrollState== OnScrollListener.SCROLL_STATE_IDLE
				&& visibleLastIndex == lastIndex)
		{
			//������Զ����أ���������������첽�������ݵĴ���
		}
	}

}
