package com.android.pdapp.ui;

import com.android.pdapp.R;
import com.android.pdapp.R.layout;
import com.android.pdapp.R.menu;
import com.android.pdapp.app.AppManager;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.view.Menu;

public class LoadingActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
		//等待5000时间后执行run方法
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				AppManager.getAppManager().finishActivity(LoadingActivity.this);
			}
		}, 4000);
	}

	
}
