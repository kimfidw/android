package com.android.pdapp.app;

import com.android.pdapp.R;
import com.android.pdapp.ui.LoginActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class AppStart extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View view = View.inflate(this, R.layout.start, null);
		setContentView(view);
		
		
		
		// 渐变展示启动屏
		AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
		aa.setDuration(2000);
		view.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				/*
				//检查版本 自动更新
				UpdateManager update = new UpdateManager(AppStart.this);
				//判断应用程序版本是否有更新
				update.checkUpdate();
				//AppManager.getAppManager().finishAllActivity();
				 *
				 */
				
				redirectTo();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
				
			}

		});
	}

	//跳转到主界面
	private void redirectTo() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this,LoginActivity.class);
		startActivity(intent);
		finish();
	}
}
