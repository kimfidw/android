package com.android.pdapp.app;

import android.content.Context;
import android.content.SharedPreferences;

/***
 * 应用配置类：用于保存用户相关信息
 * @author Yang Hong
 *
 */
public class AppConfig {
	
	public final static String App_Config = "userinfo";
	//sharepreference文件中对应的key
	public final static String User_Name= "username";
	public final static String Pass_Word = "pwd";
	public final static String Code_Name = "code";
	public final static String Is_Login = "islogin";
	public final static String Login_time = "logintime";
	public final static String Who_Name = "whoname";
	/**
	 * 盘点编号维护
	 */
	public final static String Pd_Auth1 = "auth1";
	/**
	 * 盘点作业
	 */
	public final static String Pd_Auth2 = "auth2";
	
	private Context mContext;
	private static AppConfig appconfig;
	//利用单例模式
	public static AppConfig getAppConfig(Context context)
	{
		if(appconfig==null)
		{
			appconfig = new AppConfig();
			appconfig.mContext = context;
		}
		return appconfig;
	}
	
	//获取SharedPreferenced对象
	public static SharedPreferences getSharedPreferences(Context context)
	{
		return context.getSharedPreferences(App_Config, Context.MODE_PRIVATE);
	}
	
}
