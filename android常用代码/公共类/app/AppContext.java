package com.android.pdapp.app;




import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

public class AppContext extends Application {
	
	private Context context;
	public AppContext(){}
	public AppContext(Context context)
	{
		this.context = context;
	}
	/***
	 * 判断SD卡是否存在
	 */
	public boolean hasSDcard()
	{
		 String status = Environment.getExternalStorageState();
	      if (status.equals(Environment.MEDIA_MOUNTED)) {
	          return true;
	      } else {
	          return false;
	      }
	}
	
	/***
	 * 判断缓存文件是否存在
	 */
	/*
	public boolean checkFile()
	{
		boolean status = false;
		File file = null;
		if(hasSDcard())
		{
			File dir = new File(APPCommonValue.File_Path);
			if(!dir.exists())
			{
				dir.mkdir();
			}
			file = new File(APPCommonValue.File_Path+APPCommonValue.File_Name);
			status = file.exists();
		}else
		{
			//file = getFileStreamPath(APPCommonValue.File_Name);
		}
		return status;
	}
	*/
	
	/**
	 * 获取当前网络类型
	 * @return 0：没有网络   1：WIFI网络   2：WAP网络    3：NET网络
	 
	public int getNetworkType() {
		int netType = 0;
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}		
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			String extraInfo = networkInfo.getExtraInfo();
			if(!StringUtils.isEmpty(extraInfo)){
				if (extraInfo.toLowerCase().equals("cmnet")) {
					netType = NETTYPE_CMNET;
				} else {
					netType = NETTYPE_CMWAP;
				}
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = NETTYPE_WIFI;
		}
		return netType;
	}
	*/
	/**
	 * 检测网络是否可用
	 * @return
	 */
	public boolean isNetworkConnected() {
		try
		{
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo ni = cm.getActiveNetworkInfo();
			if(ni != null && ni.isConnectedOrConnecting())
			{
				if(ni.getState()==NetworkInfo.State.CONNECTED)
				{
					return true;
				}
			}
			return false;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
}
