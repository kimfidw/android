package com.android.pdapp.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.pdapp.R;
import com.android.pdapp.model.pdHD;
import com.android.pdapp.ui.HomeActivity;

public class ListViewYMKAdapter extends BaseAdapter {
	private ListViewYMKAdapter adapter;
	private Context context;
	private List<pdHD> listItems;   //数据集
	private int itemViewResource;   //自定义项视图源 
	private LayoutInflater inFlater;  //视图容器
	private RelativeLayout pmpitem;
	
	public ListViewYMKAdapter(Context context,List<pdHD> date,int resource)
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
	public Object getItem(int position) {
		return listItems.get(position);
	}
	
	@Override
	public int getCount() {
		return listItems.size();
	}
	
	//添加list列表元素
	public void AddBarInfo(pdHD pmk)
	{
		listItems.add(pmk);
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView = inFlater.inflate(itemViewResource, null);
		TextView tvPDnum = (TextView) convertView
				.findViewById(R.id.textView_item);
		tvPDnum.setText(listItems.get(position).getPdnum());
		TextView pdTime = (TextView) convertView.findViewById(R.id.pdTime);
		pdTime.setText(listItems.get(position).getPdtime());
		
		pmpitem = (RelativeLayout) convertView.findViewById(R.id.pmk_item);
		pmpitem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context,HomeActivity.class);
				intent.putExtra("pdnum", listItems.get(position).getPdnum());
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			}
		});
		
		return convertView;
	}
}
