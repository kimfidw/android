package com.android.pdapp.dao;

import java.io.IOException;
import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.pdapp.app.AppClient;
import com.android.pdapp.dateutil.ClientDBHelper;
import com.android.pdapp.dateutil.FileUtil;
import com.android.pdapp.model.Barinfo;

public class HomeDAO {

	private Context context;
	private List<Barinfo> date = null;
	private String key;
	private FileUtil fileUtil = null;
	private PdmxDAO mxDAO = null;
	
	public HomeDAO(Context context)
	{
		this.context = context;
		fileUtil = new FileUtil(context);
		mxDAO = new PdmxDAO(context);
	}
	
	/**
	 * 加载时先读取缓存，如缓存不存在，再从服务器读取
	 * @throws XmlPullParserException 
	 * @throws IOException 
	 */
	public List<Barinfo> LoadDate(String plant,String pdnum,String userid) throws IOException, XmlPullParserException,SocketTimeoutException
	{
		key = pdnum+"-"+userid;
		if(fileUtil.CacheExist(key))
		{
			date = (ArrayList<Barinfo>)fileUtil.readObject(key);
			Log.i("doread_cache", "读缓存数据");
		}
		else
		{
			if(!pdnum.equals(""))
			{
				date = mxDAO.getPMPDateByUser(plant, pdnum, userid);
				if(date!=null)
				{
					if(date.size()!=0)
					{
						new Thread(){

							@Override
							public void run() {
								// TODO Auto-generated method stub
								fileUtil.saveObject((Serializable)date, key);
							}
							
						}.start();
						/*
						 * 在background线程中 不用用来执行AsyncTask，他只能在MainApplication中执行
						AddCacheTask addCacheDate = new AddCacheTask();
						addCacheDate.execute(2000);
						*/
					}
				}
			}
		}
		
		return date;
	}
	
	
	/**
	 * 刷新后删除缓存，再从服务器读取数据
	 * @throws XmlPullParserException 
	 * @throws IOException 
	 */
	public List<Barinfo> reflashDate(String plant, final String pdnum,String userid) throws IOException, XmlPullParserException,SocketTimeoutException
	{
		ClientDBHelper dbHelper = ClientDBHelper.getInstance(context);
		key = pdnum+"-"+userid;
		if(fileUtil.CacheExist(key))
		{
			//清空缓存文件
			fileUtil.deleteCacheFile(key);
			//删除缓存数据
			dbHelper.doDelete("delete from ympmp where pdnum='"+pdnum+"'");
			Log.i("do_delete_cache", "删除缓存数据");
		}
		if(!pdnum.equals(""))
		{
			date = mxDAO.getPMPDateByUser(plant, pdnum, userid);
			if(date!=null)
			{
				if(date.size()!=0)
				{
					new Thread(){

						@Override
						public void run() {
							//存储列表缓存数据
							fileUtil.saveObject((Serializable)date, key);
							//更新本地db数据
							AppClient appClient = new AppClient(context);
							for(Barinfo barInfo : date)
							{
								appClient.InsertLocal(pdnum, barInfo.getTrkno(), String.valueOf(barInfo.getAmact()), barInfo.getWhodo());
							}
							Log.i("dosave", "存取缓存数据");
						}
						
					}.start();
				}
			}
		}
		return date;
	}
	
	// AsyncTask异步任务
	class AddCacheTask extends AsyncTask<Integer, Integer, String> {
		@Override
		protected String doInBackground(Integer... params) {
			// 在data最前添加数据
			fileUtil.saveObject((Serializable)date, key);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}
	}
}
