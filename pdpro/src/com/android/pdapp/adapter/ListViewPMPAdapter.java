package com.android.pdapp.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.pdapp.R;
import com.android.pdapp.model.*;
import com.android.pdapp.ui.PdInfoActivity;

public class ListViewPMPAdapter extends BaseAdapter {
	
	private ListViewPMPAdapter adapter;
	private Context context;
	private List<Barinfo> listItems;   //���ݼ�
	private int itemViewResource;   //�Զ�������ͼԴ 
	private LayoutInflater inFlater;  //��ͼ����
	private RelativeLayout pmpitem;
	private long selectposition = -1;  //����listviewѡ�е�λ��
	
	public ListViewPMPAdapter(Context context,List<Barinfo> date,int resource)
	{
		this.context = context;
		this.inFlater = LayoutInflater.from(context);
		this.itemViewResource = resource;
		this.listItems = date;
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getCount() {
		if(listItems!=null)
		{
			return listItems.size();
		}
		return 0;
	}
	
	@Override
	public Object getItem(int position) {
		return listItems.get(position);
	}
	
	public void setSelectedPosition(int position)
	{
		selectposition = position;
	}
	
	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView = inFlater.inflate(itemViewResource, null);
		TextView textView = (TextView) convertView
				.findViewById(R.id.textView_item);
		textView.setText(listItems.get(position).getTrkno());
		
		
		
		//pmpitem = (RelativeLayout) convertView.findViewById(R.id.pmp_item);
		/*
		pmpitem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setBackgroundResource(R.color.help_view);
				Intent intent = new Intent(context,PdInfoActivity.class);
				intent.putExtra("barinfo", listItems.get(position));
				//���µ��߳�������activity
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			}
		});
		*/
		if(selectposition == position)
		{
			convertView.setBackgroundColor(R.color.list_view_select);
			//pmpitem.setBackgroundResource(R.color.list_view_select);
		}else
		{
			convertView.setBackgroundColor(R.color.lisg_view_default);
		}
		
		
		return convertView;
	}
	
	

	
}
