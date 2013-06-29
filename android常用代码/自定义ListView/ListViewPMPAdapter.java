package com.android.pdapp.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.pdapp.R;
import com.android.pdapp.model.*;

public class ListViewPMPAdapter extends BaseAdapter {
	
	private ListViewPMPAdapter adapter;
	private Context context;
	private List<Barinfo> listItems;   //数据集
	private int itemViewResource;   //自定义项视图源 
	private LayoutInflater inFlater;  //视图容器
	private RelativeLayout pmpitem;
	private long selectposition = -1;  //定义listview选中的位置
	
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
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView = inFlater.inflate(itemViewResource, null);
		TextView textView = (TextView) convertView
				.findViewById(R.id.textView_item);
		textView.setText(listItems.get(position).getTrkno());
		
		return convertView;
	}
	
	

	
}
