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
	 * ����ʱ�ȶ�ȡ���棬�绺�治���ڣ��ٴӷ�������ȡ
	 * @throws XmlPullParserException 
	 * @throws IOException 
	 */
	public List<Barinfo> LoadDate(String plant,String pdnum,String userid) throws IOException, XmlPullParserException,SocketTimeoutException
	{
		key = pdnum+"-"+userid;
		if(fileUtil.CacheExist(key))
		{
			date = (ArrayList<Barinfo>)fileUtil.readObject(key);
			Log.i("doread_cache", "����������");
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
						 * ��background�߳��� ��������ִ��AsyncTask����ֻ����MainApplication��ִ��
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
	 * ˢ�º�ɾ�����棬�ٴӷ�������ȡ����
	 * @throws XmlPullParserException 
	 * @throws IOException 
	 */
	public List<Barinfo> reflashDate(String plant, final String pdnum,String userid) throws IOException, XmlPullParserException,SocketTimeoutException
	{
		ClientDBHelper dbHelper = ClientDBHelper.getInstance(context);
		key = pdnum+"-"+userid;
		if(fileUtil.CacheExist(key))
		{
			//��ջ����ļ�
			fileUtil.deleteCacheFile(key);
			//ɾ����������
			dbHelper.doDelete("delete from ympmp where pdnum='"+pdnum+"'");
			Log.i("do_delete_cache", "ɾ����������");
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
							//�洢�б�������
							fileUtil.saveObject((Serializable)date, key);
							//���±���db����
							AppClient appClient = new AppClient(context);
							for(Barinfo barInfo : date)
							{
								appClient.InsertLocal(pdnum, barInfo.getTrkno(), String.valueOf(barInfo.getAmact()), barInfo.getWhodo());
							}
							Log.i("dosave", "��ȡ��������");
						}
						
					}.start();
				}
			}
		}
		return date;
	}
	
	// AsyncTask�첽����
	class AddCacheTask extends AsyncTask<Integer, Integer, String> {
		@Override
		protected String doInBackground(Integer... params) {
			// ��data��ǰ�������
			fileUtil.saveObject((Serializable)date, key);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}
	}
}
