package com.android.pdapp.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import com.android.pdapp.R;
import com.android.pdapp.app.*;
import com.android.pdapp.common.APPCommonValue;
import com.android.pdapp.common.UIHelper;
import com.android.pdapp.dao.LoginDAO;
import com.android.pdapp.model.UserAuth;

public class BaseActivity extends Activity {

	private SharedPreferences sharedPreference;
	private AppClient appClient;
	private boolean loginresult;
	public String whoname;
	public String userid;
	public String plant;
	public String pdnum;
	// �̵��ű༭
	//public String auth_edit;
	// �̵���ҵ
	//public String auth_do;

	private LoginDAO loginDao = null;
	private List<UserAuth> userDate = null;
	private AppContext appContext = null;
	
	private long mExitTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppManager.getAppManager().addActivity(this);
		// ��ȡ��¼�û���Ϣ
		sharedPreference = getSharedPreferences(AppConfig.App_Config, 0);
		userid = sharedPreference.getString(AppConfig.User_Name, "");
		whoname = sharedPreference.getString(AppConfig.Who_Name, "").trim();
		plant = sharedPreference.getString(AppConfig.Code_Name, "");
		//auth_edit = sharedPreference.getString(AppConfig.Pd_Auth1, "");
		//auth_do = sharedPreference.getString(AppConfig.Pd_Auth2, "");
		if (!userid.equals("")) {
			appClient = new AppClient(getApplicationContext());
			pdnum = appClient.pdnum(userid);
		}
		if (plant.equals("8420")) {
			plant = "NAPP";
		} else {
			plant = "ZHP";
		}
		
		networkIsConnect();
	}

	//�����Ƿ�����
	public boolean  networkIsConnect()
	{
		appContext = new AppContext(getApplicationContext());
		
		if(appContext.isNetworkConnected())
		{
			//UIHelper.ToastMessage(this, getString(R.string.network_disconnect));
			return true;
		}
		return false;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// ������ǰActivity
		AppManager.getAppManager().finishActivity(this);
	}

	//����Auth_editȨ��
	public String getAuth_edit()
	{
		return sharedPreference.getString(AppConfig.Pd_Auth1, "");
	}
	
	//�����̵���ҵȨ��
	public String getAuth_do()
	{
		return sharedPreference.getString(AppConfig.Pd_Auth2, "");
	}
	
	// �ж��û��Ƿ��¼
	public boolean isLogin() {
		loginresult = sharedPreference.getBoolean(AppConfig.Is_Login, false);
		String Logindate = sharedPreference.getString(AppConfig.Login_time, "");
		if (Logindate != "") {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				long day = (new Date().getTime() - sdf.parse(Logindate)
						.getTime()) / (24 * 60 * 60 * 1000);
				// ���������޷���½
				if (day < 7) {
					loginresult = true;
				} else {
					loginresult = false;
				}
				if(loginresult)
				{
					new Thread() {
						@Override
						public void run() {
							loginDao = new LoginDAO();
							userDate = loginDao.FindUserAuth(userid);
							if (userDate != null) {
								SharedPreferences.Editor editor = sharedPreference
										.edit();
								for (UserAuth userAuth : userDate) {
									if (userAuth.getAuthno().equals(
											APPCommonValue.AuthEdit)) {
										editor.putString(AppConfig.Pd_Auth1,
												userAuth.getAuthno());
										//auth_edit = userAuth.getAuthno();
									} else if (userAuth.getAuthno().equals(
											APPCommonValue.AuthDo)) {
										editor.putString(AppConfig.Pd_Auth2,
												userAuth.getAuthno());
										//auth_do = userAuth.getAuthno();
									}
								}
								editor.commit();
							}
						}
					}.start();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			loginresult = false;
		}
		return loginresult;
	}

	/***
	 * ��յ�¼��Ϣ
	 */
	private void clearLogin() {
		// TODO Auto-generated method stub
		SharedPreferences.Editor editor = sharedPreference.edit();
		editor.putBoolean(AppConfig.Is_Login, false);
		editor.putString(AppConfig.Login_time, "");
		editor.putString(AppConfig.Pd_Auth1,"");
		editor.putString(AppConfig.Pd_Auth2,"");
		editor.commit();
	}

	/***
	 * �˵�ѡ��
	 * 
	 * @param item
	 */
	public void MenuSelected(MenuItem item) {
		// �õ���ǰѡ�е�menuItem��ID
		int item_id = item.getItemId();
		switch (item_id) {
		// �˳���ǰӦ��
		case R.id.exit:
			clearLogin();
			AppManager.getAppManager().finishAllActivity();
			break;

		}
	}

	/**
	 * ������η��ذ�ť�˳�����������
	 */
	public Boolean ExitByReturnBtn(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Object mHelperUtils;
				UIHelper.ToastMessage(this, "�ٰ�һ���˳�����");
				mExitTime = System.currentTimeMillis();

			} else {
				AppManager.getAppManager().finishAllActivity();
				//������ǰӦ�õĽ���
				android.os.Process.killProcess(android.os.Process.myPid());
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
}
