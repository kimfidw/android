package com.android.pdapp;

import java.util.ArrayList;
import java.util.List;

import com.android.pdapp.common.UIHelper;
import com.android.pdapp.model.pdMX;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/***
 * 分页显示多条数据
 * @author Yang Hong
 *
 */
public class LoadPagingActivity extends Activity implements OnScrollListener {

	private ListView listview;
	private int visibleLastIndex = 0; //最后的可视项索引
	private int visibleItemCount;  //当前窗口的可见项数目
	private int datasieze = 50;     //数据总条数
	private CustomAdapter adapter;
	private View loadMoreView;
	private Button loadMoreButton;
	private Handler hander = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loadpaging);
		
		loadMoreView = getLayoutInflater().inflate(R.layout.loadmore, null);
		loadMoreButton = (Button) loadMoreView.findViewById(R.id.loadMoreButton);
		loadMoreButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				loadMoreButton.setText("正在加载中...");
				hander.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						loadMoreData();
						adapter.notifyDataSetChanged();
						loadMoreButton.setText("查看更多...");
					}
				}, 2000);
			}
		});
		
		listview = (ListView) findViewById(R.id.PDmxList);
		//设置列表底部视图
		listview.addFooterView(loadMoreView);
		initializeAdapter();
		listview.setAdapter(adapter);
		listview.setOnScrollListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.load_paging, menu);
		return true;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.visibleItemCount = visibleItemCount;
		visibleLastIndex = firstVisibleItem + visibleItemCount-1;
		
		//如果所有记录选项等于数据及条数，则移除列表底部视图
		if(totalItemCount == datasieze)
		{
			listview.removeFooterView(loadMoreView);
			UIHelper.ToastMessage(this, "数据已全部加载");
		}
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// 数据集最后一项的索引
		int itemsLastIndex = adapter.getCount()-1;
		int lastIndex = adapter.getCount();
		if(scrollState== OnScrollListener.SCROLL_STATE_IDLE
				&& visibleLastIndex == lastIndex)
		{
			//如果是自动加载，可以在这里放置异步加载数据的代码
		}
	}
	
	/**
	 * 初始化ListView的适配器
	 * @author Yang Hong
	 *
	 */
	private void initializeAdapter()
	{
		List<pdMX> mxList = new ArrayList<pdMX>();
		for(int i=0;i<=10;i++)
		{
			pdMX mx = new pdMX();
			mx.setTrkno("aaa"+i);
			mx.setBartyp("bb"+i);
			mxList.add(mx);
		}
		adapter = new CustomAdapter(mxList);
	}
	
	/**
	 * 加载更多数据
	 * @author Yang Hong
	 *
	 */
	private void loadMoreData()
	{
		int count = adapter.getCount();
		
		if(count + 10 <=datasieze)
		{
			for(int i=count+1;i<=count+10;i++)
			{
				pdMX mx = new pdMX();
				mx.setTrkno("aaa"+i);
				mx.setBartyp("bb"+i);
				adapter.addpdMXItem(mx);
			}
		}else
		{
			for(int i=count+1;i<=datasieze;i++)
			{
				pdMX mx = new pdMX();
				mx.setTrkno("aaa"+i);
				mx.setBartyp("bb"+i);
				adapter.addpdMXItem(mx);
			}
		}
	}
	class CustomAdapter extends BaseAdapter {

		List<pdMX> mxItems;
		
		public CustomAdapter(List<pdMX> mxitems)
		{
			this.mxItems = mxitems;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mxItems.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mxItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		
		/***
		 * 添加数据列表项
		 * @param mxitem
		 */
		public void addpdMXItem(pdMX mxitem)
		{
			mxItems.add(mxitem);
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(view==null)
			{
				view = getLayoutInflater().inflate(R.layout.listitem, null);
			}
			TextView trkno = (TextView) view.findViewById(R.id.pdid);
			TextView title = (TextView) view.findViewById(R.id.newstitle);
			trkno.setText(mxItems.get(position).getTrkno());
			title.setText(mxItems.get(position).getBartyp());
			return view;
		}

	}


}
