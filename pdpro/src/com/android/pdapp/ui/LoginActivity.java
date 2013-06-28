package com.android.pdapp.ui;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.xmlpull.v1.XmlPullParserException;

import com.android.pdapp.R;
import com.android.pdapp.app.AppConfig;
import com.android.pdapp.app.AppManager;
import com.android.pdapp.common.StringUtils;
import com.android.pdapp.common.UIHelper;
import com.android.pdapp.dao.LoginDAO;
import com.android.pdapp.model.UserInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ViewSwitcher;

public class LoginActivity extends BaseActivity {

	private ViewSwitcher mViewSwitcher;
	private ImageButton clear_btn1;
	private ImageButton clear_btn2;
	private Button btn_login;
	private EditText mUsname;
	private EditText mPwd;
	private AnimationDrawable loadingAnimation;
	private View loginLoading;
	private InputMethodManager imm;

	private SharedPreferences sharedPreferences;
	private LoginDAO loginDao;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginpage);

		this.initContent();

		// ����˺š�������Ϣ
		clear_btn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mUsname.setText("");
			}
		});
		clear_btn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mPwd.setText("");
			}
		});

		// ��¼
		btn_login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// ���������
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				String username = mUsname.getText().toString();
				String password = mPwd.getText().toString();
				// �ж��û����������Ƿ�Ϊ��
				if (StringUtils.isEmpty(username)) {
					UIHelper.ToastMessage(v.getContext(),
							getString(R.string.msg_login_null));
					return;
				}
				if (StringUtils.isEmpty(password)) {
					UIHelper.ToastMessage(v.getContext(),
							getString(R.string.msg_pwd_null));
					return;
				}
				sharedPreferences = getSharedPreferences(AppConfig.App_Config, 0);
				// �����û���������
				SharedPreferences.Editor editor = sharedPreferences.edit();
				editor.putString(AppConfig.User_Name, username);
				editor.putString(AppConfig.Pass_Word, password);
				editor.putBoolean(AppConfig.Is_Login, false);
				editor.commit();
				
				if(networkIsConnect())
				{
					// �û���¼
					login(username, password);
					//��ת����½���ؽ���
					Intent intent = new Intent(LoginActivity.this,LoadingActivity.class);
					startActivity(intent);
				}else
				{
					UIHelper.ToastMessage(LoginActivity.this, getString(R.string.network_disconnect));
					return;
				}
				
			}

		});

	}

	// ��ʼ��
	private void initContent() {
		imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		mViewSwitcher = (ViewSwitcher) findViewById(R.id.logindialog_view_switcher);
		loginLoading = findViewById(R.id.login_loading);
		mUsname = (EditText) findViewById(R.id.login_edit_account);
		mPwd = (EditText) findViewById(R.id.login_edit_pwd);
		clear_btn1 = (ImageButton) findViewById(R.id.ImageButton01);
		clear_btn2 = (ImageButton) findViewById(R.id.ImageButton02);
		btn_login = (Button) findViewById(R.id.login_btn);
		//Log.i("mode", String.valueOf(Activity.MODE_PRIVATE));
		sharedPreferences = getSharedPreferences(AppConfig.App_Config, Context.MODE_PRIVATE);
		mUsname.setText(sharedPreferences.getString(AppConfig.User_Name, ""));
		mPwd.setText(sharedPreferences.getString(AppConfig.Pass_Word, ""));
		
	}

	// ��¼��֤
	private void login(final String username, final String password) {
		
		
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// 1��ʾ��¼�ɹ�
				if (msg.what == 1) {
					
					UserInfo ui = (UserInfo)msg.obj;
					// ����������
					sharedPreferences = getSharedPreferences(AppConfig.App_Config, 0);
					SharedPreferences.Editor editor = sharedPreferences.edit();
					// �Ե�¼�û������
					editor.putBoolean(AppConfig.Is_Login, true);
					// ��¼��¼ʱ��
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					editor.putString(AppConfig.Login_time, sdf.format(new Date()));
					
					editor.putString(AppConfig.User_Name, username);
					editor.putString(AppConfig.Pass_Word, password);
					
					if(ui!=null)
					{
						editor.putString(AppConfig.Code_Name, ui.getCode());
						editor.putString(AppConfig.Who_Name,ui.getWhoname());
					}
					editor.commit();
					Intent intent = new Intent(LoginActivity.this,
							HomeActivity.class);
					startActivity(intent);
					
					AppManager.getAppManager().finishActivity();
				}
				// 0��ʾ��¼ʧ��
				else if (msg.what == 0) {
					UIHelper.ToastMessage(LoginActivity.this,
							getString(R.string.login_false));
					mPwd.requestFocus();
					return;
				}
				// 3��ʾ�����ٶ���
				else if(msg.what==3)
				{
					//ҳ����ת����
					UIHelper.ToastMessage(LoginActivity.this, getString(R.string.network_slow));			
					return;
				}
				
			}
		};
		new Thread() {
			@Override
			public void run() {
				//Log.d("dateThread", "���߳�");
				Message msg = new Message();
				loginDao = new LoginDAO();
				UserInfo ui;
				try {
					ui = loginDao.FindUser(username, password);
					if(ui!=null)
					{
						msg.what = 1;
						msg.obj = ui;
					}
					else
					{
						msg.what=0;
					}
				} catch (IOException e) {
					msg.what=3;
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
		getMenuInflater().inflate(R.menu.menu,menu);
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

}
