package com.android.pdapp.app;

import android.content.Context;
import android.content.SharedPreferences;

/***
 * Ӧ�������ࣺ���ڱ����û������Ϣ
 * @author Yang Hong
 *
 */
public class AppConfig {
	
	public final static String App_Config = "userinfo";
	//sharepreference�ļ��ж�Ӧ��key
	public final static String User_Name= "username";
	public final static String Pass_Word = "pwd";
	public final static String Code_Name = "code";
	public final static String Is_Login = "islogin";
	public final static String Login_time = "logintime";
	public final static String Who_Name = "whoname";
	/**
	 * �̵���ά��
	 */
	public final static String Pd_Auth1 = "auth1";
	/**
	 * �̵���ҵ
	 */
	public final static String Pd_Auth2 = "auth2";
	
	private Context mContext;
	private static AppConfig appconfig;
	//���õ���ģʽ
	public static AppConfig getAppConfig(Context context)
	{
		if(appconfig==null)
		{
			appconfig = new AppConfig();
			appconfig.mContext = context;
		}
		return appconfig;
	}
	
	//��ȡSharedPreferenced����
	public static SharedPreferences getSharedPreferences(Context context)
	{
		return context.getSharedPreferences(App_Config, Context.MODE_PRIVATE);
	}
	
}
