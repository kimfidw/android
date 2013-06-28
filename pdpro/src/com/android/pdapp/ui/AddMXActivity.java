package com.android.pdapp.ui;

import java.io.IOException;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import com.android.pdapp.R;
import com.android.pdapp.app.AppClient;
import com.android.pdapp.app.AppManager;
import com.android.pdapp.common.APPCommonValue;
import com.android.pdapp.common.StringUtils;
import com.android.pdapp.common.UIHelper;
import com.android.pdapp.dao.PdmxDAO;
import com.android.pdapp.dao.PdymkDAO;
import com.android.pdapp.model.*;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Contacts.Intents.UI;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.content.Intent;
import android.view.View.OnClickListener;

public class AddMXActivity extends BaseActivity {

	private EditText numstr;
	private EditText trknostr;
	private EditText amactstr;
	private TextView mxcountstr;
	private TextView pdcount;
	private Button savebtn;
	private Button capturebtn;
	private ImageButton addmx_head_back;
	
	//private SharedPreferences sharedPreferences;
	public String trkno;
	
	public String amact;
	private List<Barinfo> barinfoList;
	private AppClient appclient = null;
	private PdymkDAO ymkDao = null;
	private String pdnumTemp = "";
	private boolean result = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addmxpage);
		this.initContent();
		
		numstr.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				
				if(!hasFocus)
				{
					if(pdnum.equals(""))
					{
						pdnumTemp = numstr.getText().toString();
					}
					final Handler handle = new Handler(){

						@Override
						public void handleMessage(Message msg) {
							if(msg.what==2)
							{
								UIHelper.ToastMessage(AddMXActivity.this, getString(R.string.network_slow));
								return;
							}else
							{
								result = (Boolean)msg.obj;
								if(!result)
								{
									if(!pdnum.equals(""))
									{
										UIHelper.ToastMessage(AddMXActivity.this, getString(R.string.pdnum_same));
									}else
									{
										UIHelper.ToastMessage(AddMXActivity.this, getString(R.string.pdnum_null));
									}
									return;
								}
							}
						}
					};
					new Thread(){

						@Override
						public void run() {
							Message msg = new Message();
							ymkDao = new PdymkDAO(getApplicationContext());
							try {
								msg.obj = ymkDao.FindPDnum(numstr.getText().toString(), plant);
							} catch (IOException e) {
								msg.what =2;
								e.printStackTrace();
							} catch (XmlPullParserException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							handle.sendMessage(msg);
						}
						
					}.start();
				}
				
			}
		});
		
		/*
		trknostr.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AddMXActivity.this,CaptureActivity.class);
				startActivity(intent);
				onPause();
			}
		});
		*/
		
		addmx_head_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AddMXActivity.this,HomeActivity.class);
				startActivity(intent);
				AppManager.getAppManager().finishActivity();
			}
		});
		
		//条码扫描
		capturebtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AddMXActivity.this,CaptureActivity.class);
				intent.putExtra("pdnum", numstr.getText().toString());
				startActivity(intent);
				onPause();
			}
		});
		
		//保存盘点结果
		savebtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pdnum = numstr.getText().toString();
				trkno = trknostr.getText().toString();
				amact = amactstr.getText().toString();
				
				if(StringUtils.isEmpty(trkno))
				{
					UIHelper.ToastMessage(AddMXActivity.this, getString(R.string.pdtrkno_null));
					return;
				}
				if(!pdnum.equals(""))
				{
					
					//比较盘点编号是否为本月的盘点编号
					if(!numstr.getText().toString().equals(pdnum))
					{
						UIHelper.ToastMessage(AddMXActivity.this, getString(R.string.pdnum_wrong));
						numstr.requestFocus();
						return;
					}
				}
				//保存盘点信息
				SaveMX(plant, numstr.getText().toString(), trkno, userid, whoname, amact);
				
			}

			private void SaveMX(final String plant, final String pdnum, final String trkno,
					final String whodo, final String whoname, final String amact) {
				
				
				
				Log.i("savemx", "保存盘点数据");
				final Handler handler = new Handler(){
					@Override
					public void handleMessage(Message msg)
					{
						MsgInfo msginfo = (MsgInfo)msg.obj;
						if(msg.what==1)
						{
							//插入数据
							InsertHCTask ihcTask = new InsertHCTask();
							ihcTask.execute(2000);
							UIHelper.ToastMessage(AddMXActivity.this, getString(R.string.save_success));
						}else if(msg.what==0)
						{
							UIHelper.ToastMessage(AddMXActivity.this, msginfo.getMsg());
							return;
						}else if(msg.what==2)
						{
							UIHelper.ToastMessage(AddMXActivity.this, getString(R.string.network_slow));
							return;
						}
					}
					
				};
				
				new Thread(){
					@Override
					public void run() {
						Message msg = new Message();
						
						PdmxDAO mxDao = new PdmxDAO(AddMXActivity.this);
						//保存数据到数据库
						MsgInfo msginfo;
						try {
							msginfo = mxDao.AddPDMX(plant, pdnum, trkno, whodo, whoname, amact);
							if(msginfo!=null)
							{
								String msgtyp = msginfo.getMsgtyp();
								msg.obj = msginfo;
								if(msgtyp.equals("S"))
								{
									msg.what=1;
								}else
								{
									msg.what = 0;
								}
							}
							
						} catch (IOException e) {
							msg.what=2;
							e.printStackTrace();
						} catch (XmlPullParserException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						msg.what=1;
						handler.sendMessage(msg);
					}
				}.start();
			}
			
			
		});
	}
	
	
	@Override
	protected void onResume()
	{
		super.onResume();
		Bundle bundle = getIntent().getExtras();
		if(bundle!=null)
		{
			trkno = bundle.getString("CaptureValue");
			trknostr.setText(trkno);
			pdnumTemp = bundle.getString("pdnums");
			numstr.setText(pdnumTemp);
			amactstr.requestFocus();
		}
	}
	
	/**
	 * 插入缓存数据
	 * @author kimifdw
	 *
	 */
	class InsertHCTask extends AsyncTask<Integer, Integer, String>
	{
		@Override
		protected String doInBackground(Integer... params) {
			
			//插入缓存
			appclient.InsertLocal(pdnum,trkno,amact,userid);
			
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			trknostr.setText("");
			amactstr.setText("");
			mxcountstr.setText(getString(R.string.pd_all)+appclient.LocalNowTemp(pdnum,userid));
		}
		
	}
	
	/**
	 * 初始化布局文件
	 */
	private void initContent() {
		// TODO Auto-generated method stub
		numstr = (EditText) findViewById(R.id.pd_edit_pdnum);
		trknostr = (EditText) findViewById(R.id.pd_edit_trkno);
		amactstr = (EditText) findViewById(R.id.pd_edit_amact);
		mxcountstr = (TextView) findViewById(R.id.pd_edit_all);
		pdcount = (TextView) findViewById(R.id.pdcount);
		savebtn = (Button) findViewById(R.id.pd_btn_save);
		capturebtn = (Button) findViewById(R.id.capture_button);
		addmx_head_back = (ImageButton) findViewById(R.id.addmx_head_back);
		appclient = new AppClient(getApplicationContext());
		if(pdnum!="")
		{
			numstr.setText(pdnum);
		}else
		{
			numstr.setText(pdnumTemp);
		}
		amactstr.setText(APPCommonValue.amactByDefault);
		mxcountstr.setText(getString(R.string.pd_all)+appclient.LocalNowTemp(pdnum,userid));
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
	
	/**
	 * 按两次返回键退出
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
        return super.ExitByReturnBtn(keyCode, event);
	}

}
