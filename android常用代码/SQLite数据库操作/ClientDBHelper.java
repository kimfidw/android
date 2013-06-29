package com.android.pdapp.dateutil;

import java.io.File;
import java.io.IOException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.android.pdapp.app.*;

/**
 * 对SQLite数据库操作的常用类
 * @author 东伟
 *
 */
public class ClientDBHelper {

	// 文件路径
	private final static String DATABASE_PATH = android.os.Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/pdtemp/";
	// 数据库名
	private final static String DB_NAME = "pd.db";
	private final static String TB_NAME_PMP = "YMPMP";

	private static SQLiteDatabase mySQLiteDatabase;
	private static SQLiteDBHelper myDBHelper;
	private static Context context;
	private static ClientDBHelper instance;
	private Cursor cursor;
	
	public ClientDBHelper(Context context) {
		ClientDBHelper.context = context;
		mySQLiteDatabase = openDatabase(context);
	}

	public static ClientDBHelper getInstance(Context context) {
		if (instance == null) {
			instance = new ClientDBHelper(context);
		}
		return instance;
	}

	/**
	 * 打开数据库
	 */
	private static SQLiteDatabase openDatabase(Context context) {
		try {
			AppContext appcontext = new AppContext();
			if (appcontext.hasSDcard()) {
				File dir = new File(DATABASE_PATH);
				File file = new File(DATABASE_PATH + DB_NAME);
				if (!dir.exists()) {
					dir.mkdir();
				}
				if (!file.exists()) {
					file.createNewFile();
				}
				if (file.exists()) {
					mySQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(
							file, null);
				}
			} else {
				myDBHelper = new SQLiteDBHelper(context);
				mySQLiteDatabase = myDBHelper.getWritableDatabase();
			}
			return mySQLiteDatabase;
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	/***
	 * 打开连接
	 */
	public void open()
	{
		if (!mySQLiteDatabase.isOpen()) {
			mySQLiteDatabase = openDatabase(context);
		}
	}
	
	/***
	 * 插入数据
	 * 
	 * @param cv
	 * @param keyid
	 * @return
	 */
	public long doInsert(ContentValues cv, String keyid) {
		try {
			ContainsTable(TB_NAME_PMP);
			open();
			String sql = "select count(*) from ympmp where pdnum='"+cv.getAsString("pdnum")+"' and trkno=";
			Cursor cursor = mySQLiteDatabase.rawQuery(sql + keyid, null);
			//根据盘点编号和条码判断记录是否存在
			if(cursor!=null)
			{
				if(cursor.moveToFirst())
				{
					//count不为0代表记录存在，则删除
					int count = cursor.getInt(0);
					if(count!=0)
					{
						//根据盘点编号和条码删除记录
						//mySQLiteDatabase.execSQL("delete from ympmp where pdnum='"+cv.getAsString("pdnum")+"' and trkno='"+keyid+"'");
						return 1;
					}
				}
			}
			cursor.close();
			return mySQLiteDatabase.insert(TB_NAME_PMP, keyid, cv);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		finally
		{
			close();
		}
		return -1;
	}

	/***
	 * 查询指定数据
	 * 
	 * @param sql
	 * @param keyid
	 * @return
	 */
	public Cursor fetchData(String sql, String keyid) {
		ContainsTable(TB_NAME_PMP);
		open();
		cursor = mySQLiteDatabase.rawQuery(sql + keyid, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		//close();
		return cursor;
	}

	/***
	 * 查询所有记录
	 * 
	 * @param sql
	 * @return
	 */
	public Cursor fetchDataAll(String sql) {
		
		ContainsTable(TB_NAME_PMP);
		open();
		cursor = mySQLiteDatabase.rawQuery(sql, null);
		//close();
		return cursor;
	}

	/***
	 * 删除数据
	 */
	public void doDelete(String sql) {
		ContainsTable(TB_NAME_PMP);
		open();
		mySQLiteDatabase.execSQL(sql);
		close();
	}

	/***
	 * 根据表名判断表是否已存在 
	 * @param tablename
	 * @return
	 */
	public void ContainsTable(String tablename) {
		int count = 0;
		open();
		cursor = mySQLiteDatabase.rawQuery(
				"SELECT COUNT(*) FROM sqlite_master where type='table' and name='"
						+ tablename + "'", null);
		if (cursor.moveToFirst()) {
			count = cursor.getInt(0);
		}
		if(count==0)
		{
			mySQLiteDatabase.execSQL(SQLiteDBHelper.DB_CREATE);
		}
		cursor.close();
		close();
	}

	/**
	 * 关闭数据库
	 */
	public void close() {
		if (mySQLiteDatabase.isOpen()) 
		{
			mySQLiteDatabase.close();
		}
	}

}
