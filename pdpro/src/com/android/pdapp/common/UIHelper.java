package com.android.pdapp.common;

import android.content.Context;
import android.widget.Toast;

public class UIHelper {
	
	/**
	 * µ¯³öToastÏûÏ¢
	 * 
	 * @param msg
	 */
	public static void ToastMessage(Context cont, String msg) {
		Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
	}
	
	public static void ToastLongMessage(Context cont,String msg)
	{
		Toast.makeText(cont, msg, Toast.LENGTH_LONG).show();
	}
}
