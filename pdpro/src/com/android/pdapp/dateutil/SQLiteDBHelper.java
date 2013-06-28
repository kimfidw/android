package com.android.pdapp.dateutil;



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDBHelper extends SQLiteOpenHelper {

	

	private final static String DB_NAME = "pd.db";
	private final static int DB_VERSION = 1;
	//表名
	private final static String DB_TABLE = "YMPMP";
	//建表语句
	public final static String DB_CREATE = "CREATE TABLE "+DB_TABLE
			+"(trkno varchar(10),pdnum varchar(10)" +
			",pdtime varchar(6),amcat NUMERIC(10,3),whodo varchar(10))";
	
	
	public SQLiteDBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DB_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
