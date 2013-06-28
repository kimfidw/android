package com.android.pdapp.ui;

import com.android.pdapp.R;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.content.Intent;

import com.android.pdapp.model.*;

public class PdInfoActivity extends BaseActivity {

	private EditText pdnumEdit;
	private EditText trkno;
	private EditText bartyp;
	private EditText pdcno;
	private EditText npmno;
	private EditText psqwt;
	private EditText widcal;
	private EditText lencal;
	private EditText amacl;
	private EditText amact;
	private EditText plantEdit;
	private EditText whonameEdit;
	private EditText Whendo;
	
	private ImageButton headBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pdinfopage);
		PageInit();
		Bundle extras = getIntent().getExtras();
		Barinfo barinfo = (Barinfo)extras.getSerializable("barinfo");
		InitValue(barinfo);
	}
	
	/**
	 * 初始化页面
	 */
	private void PageInit()
	{
		pdnumEdit = (EditText) findViewById(R.id.pd_edit_pdnum);
		trkno = (EditText) findViewById(R.id.pd_edit_trkno);
		bartyp = (EditText) findViewById(R.id.pd_edit_bartyp);
		pdcno = (EditText) findViewById(R.id.pd_edit_pdcno);
		npmno = (EditText) findViewById(R.id.pd_edit_Npmno);
		psqwt = (EditText) findViewById(R.id.pd_edit_psqwt);
		widcal = (EditText) findViewById(R.id.pd_edit_widcal);
		lencal = (EditText) findViewById(R.id.pd_edit_lencal);
		amacl = (EditText) findViewById(R.id.pd_edit_amcal);
		amact = (EditText) findViewById(R.id.pd_edit_amcat);
		plantEdit = (EditText) findViewById(R.id.pd_edit_plant);
		whonameEdit = (EditText) findViewById(R.id.pd_edit_whoname);
		
		headBack = (ImageButton) findViewById(R.id.pdinfo_head_back);
		
		headBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PdInfoActivity.this,HomeActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
	
	/**
	 * 赋初值
	 */
	private void InitValue(Barinfo barinfo)
	{
		if(barinfo!=null)
		{
			pdnumEdit.setText(pdnum);
			plantEdit.setText(plant);
			trkno.setText(barinfo.getTrkno());
			bartyp.setText(barinfo.getBartyp());
			pdcno.setText(barinfo.getPdcno());
			npmno.setText(barinfo.getNpmno());
			psqwt.setText(String.valueOf(barinfo.getPsqwt()));
			widcal.setText(String.valueOf(barinfo.getWidth()));
			lencal.setText(String.valueOf(barinfo.getLenth()));
			amacl.setText(String.valueOf(barinfo.getAmount()));
			amact.setText(String.valueOf(barinfo.getAmact()));
			whonameEdit.setText(whoname);
		}
	}
	
	/**
	 * 按两次返回键退出
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
        return super.ExitByReturnBtn(keyCode, event);
	}
	
	/**
	 * 创建菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu,menu);
		return true;
	}
	
	/***
	 * 菜单处理
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.MenuSelected(item);
		return true;
	}
}
