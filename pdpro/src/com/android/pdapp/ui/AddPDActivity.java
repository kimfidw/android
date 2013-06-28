package com.android.pdapp.ui;

import java.io.IOException;
import java.util.Calendar;

import org.xmlpull.v1.XmlPullParserException;

import com.android.pdapp.R;
import com.android.pdapp.app.AppManager;
import com.android.pdapp.common.StringUtils;
import com.android.pdapp.common.UIHelper;
import com.android.pdapp.dao.PdymkDAO;
import com.android.pdapp.model.MsgInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

public class AddPDActivity extends BaseActivity {

	//�ı���
	private EditText pdnumEdit;
	private EditText pdcode;
	private EditText pdtime;
	private EditText pdmemo;
	
	//��ť
	private Button save;
	private ImageButton back;
	
	// ���뷨����
	private InputMethodManager imm;
	
	
	private SharedPreferences sharedPreference;
	private PdymkDAO ymkDao = null;
	private boolean result = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addpdpage);
		
		this.initContent();
		
		pdnumEdit.setOnFocusChangeListener(pdnumEditFocusChane);
		
		//����
		save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//���������
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				
				String pdnumstr = pdnumEdit.getText().toString();
				String pdtimestr = pdtime.getText().toString();
				String pdmemostr = pdmemo.getText().toString();
				if(StringUtils.isEmpty(pdnumstr))
				{
					UIHelper.ToastMessage(AddPDActivity.this, getString(R.string.pdnum_null));
					return;
				}
				if(StringUtils.isEmpty(pdtimestr))
				{
					UIHelper.ToastMessage(AddPDActivity.this, getString(R.string.pdtime_null));
					return;
				}
				if(!result)
				{
					AddYMK(plant, pdnumstr, pdtimestr, pdmemostr, userid,whoname);
				}else
				{
					UIHelper.ToastMessage(AddPDActivity.this, getString(R.string.pdnum_same));
					return;
				}
			}
		});
		//����
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AddPDActivity.this,HomeActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		//����̵�ʱ��ʱ����
		pdtime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//1.���������
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				//2.��ȡ��ǰʱ��
				Calendar timenow = Calendar.getInstance();
				//3.��ʼ�����ڶԻ���
				new DatePickerDialog(AddPDActivity.this, new OnDateSetListener() {
					
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear,
							int dayOfMonth) {
						String pdtimes = "";
						if((monthOfYear+1)<10)
						{
							pdtimes = String.valueOf(year)+"0"+String.valueOf(monthOfYear+1);
						}else
						{
							pdtimes = String.valueOf(year)+String.valueOf(monthOfYear+1);
						}
						pdtime.setText(pdtimes);
					}
				}, timenow.get(Calendar.YEAR),timenow.get(Calendar.MONTH),timenow.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
	}
	
	//����ʧȥ����
	private View.OnFocusChangeListener pdnumEditFocusChane = new OnFocusChangeListener() {
		
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if(!hasFocus)
			{
				final Handler handler = new Handler(){

					@Override
					public void handleMessage(Message msg) {
						if(msg.what==2)
						{
							UIHelper.ToastMessage(AddPDActivity.this, getString(R.string.network_slow));
							return;
						}else
						{
							result = (Boolean)msg.obj;
							if(result)
							{
								UIHelper.ToastMessage(AddPDActivity.this, getString(R.string.pdnum_same));
								return;
							}
						}
					}
				};
				new Thread(){

					@Override
					public void run() {
						Message message = new Message();
						ymkDao = new PdymkDAO(getApplicationContext());
						try {
							message.obj = ymkDao.FindPDnum(pdnumEdit.getText().toString(), plant);
						} catch (IOException e) {
							message.what=2;
							e.printStackTrace();
						} catch (XmlPullParserException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						handler.sendMessage(message);
					}
				}.start();
			}
		}
	};
	
	/***
	 * ����̵��ܱ���Ϣ
	 * @param pdcodestr ��������
	 * @param pdnumstr �̵���
	 * @param pdtimestr �̵�ʱ��
	 * @param pdmemostr ��ע
	 * @param whodo �����˹���
	 * @param whoname ����������
	 */
	private void AddYMK(final String pdcodestr, final String pdnumstr,
			final String pdtimestr, final String pdmemostr, final String whodo,
			final String whoname) {
		final Handler handler = new Handler()
		{
			@Override
			public void handleMessage(Message message)
			{
				if(message.what ==1)
				{
					MsgInfo mi = (MsgInfo)message.obj;
					
					if(mi.getMsgtyp().equals("S"))
					{
						Intent intent = new Intent(AddPDActivity.this,HomeActivity.class);
						startActivity(intent);
						UIHelper.ToastMessage(AddPDActivity.this, getString(R.string.save_success));
						AppManager.getAppManager().finishActivity();
					}else
					{
						if(mi.getMsg().contains("unique constraint"))
						{
							UIHelper.ToastMessage(AddPDActivity.this, "��Ϣ��д����ȷ");
						}
					}
				}else if(message.what==2)
				{
					UIHelper.ToastMessage(AddPDActivity.this,getString(R.string.network_slow));
					return;
				}
			}
		};
		new Thread(){
		@Override
		public void run() {
			Message msg = new Message();
			//�ύ��������
			PdymkDAO ymkDao = new PdymkDAO(getApplicationContext());
			MsgInfo msginfo;
			try {
				msginfo = ymkDao.AddYMK(pdcodestr, pdnumstr, pdtimestr, pdmemostr, whodo, whoname);
				if(msginfo!=null)
				{
					msg.what = 1;
					msg.obj = msginfo;
				}else
				{
					msg.what = 0;
				}
			} catch (IOException e) {
				msg.what = 2;
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			handler.sendMessage(msg);
		}
	}.start();
		
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
	public boolean onOptionsItemSelected(MenuItem item)
	{
		super.MenuSelected(item);
		return true;
	}
	/***
	 * ��ʼ��ҳ�����
	 */
	private void initContent() {
		pdnumEdit = (EditText) findViewById(R.id.pd_edit_pdnum);
		pdcode = (EditText) findViewById(R.id.pd_edit_pdcode);
		pdtime = (EditText) findViewById(R.id.pd_edit_pdtime);
		pdmemo = (EditText) findViewById(R.id.pd_edit_memo);
		save = (Button) findViewById(R.id.pd_btn_save);
		back = (ImageButton) findViewById(R.id.addpd_head_back);
		imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		pdcode.setText(plant);
	}
	
	/**
	 * �����η��ؼ��˳�
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
        return super.ExitByReturnBtn(keyCode, event);
	}
	
}
