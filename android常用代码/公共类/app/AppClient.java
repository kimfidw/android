package com.android.pdapp.app;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.android.pdapp.dateutil.*;

public class AppClient {

	private ClientDBHelper dbHelper;
	
	private Context context;
	public AppClient(Context context)
	{
		this.context = context;
	}

	/**
	 * 按年月来返回当前日期字符串
	 * @return
	 */
	public String getDateByFormat()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	/**
	 *  插入缓存
	 */
	public boolean InsertLocal(String pdnum, String trkno, String amact,String userid) {
		boolean result = false;
		dbHelper = ClientDBHelper.getInstance(context);
		String pdtime = getDateByFormat();
		
		ContentValues cv = new ContentValues();
		cv.put("trkno", trkno);
		cv.put("pdnum", pdnum);
		cv.put("amcat", amact);
		cv.put("pdtime", pdtime);
		cv.put("whodo", userid);
		if (dbHelper.doInsert(cv, trkno) != -1) {
			result = true;
		}
		dbHelper.close();
		return result;
	}

	/**
	 * 得到缓存的记录总数
	 */
	public String LocalNowTemp(String pdnum,String userid) {
		dbHelper = ClientDBHelper.getInstance(context);
		String count = "";
		Cursor cursor = dbHelper.fetchDataAll("select count(*) from YMPMP where pdnum='"+pdnum+"' and whodo='"+userid+"'");
		if(cursor!=null)
		{
			if (cursor.moveToFirst()) {
				count = cursor.getString(0);
			}
		}
		cursor.close();
		dbHelper.close();
		return count;
	}
	
	/**
	 * 根据用户id和盘点时间返回盘点编号
	 * @param userid
	 * @return
	 */
	public String pdnum(String userid)
	{
		dbHelper = ClientDBHelper.getInstance(context);
		String pdnum = "";
		if(userid!=null)
		{
			
			String pdtime = getDateByFormat();
			Cursor cursor = dbHelper.fetchDataAll("select pdnum from ympmp where whodo='"+userid+"' and pdtime='"+pdtime+"'");
			if(cursor!=null)
			{
				if (cursor.moveToFirst()) {
					pdnum = cursor.getString(0);
				}
			}
			cursor.close();
			dbHelper.close();
		}
		return pdnum;
	}
	

}
