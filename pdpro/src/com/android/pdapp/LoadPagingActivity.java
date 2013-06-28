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
 * ��ҳ��ʾ��������
 * @author Yang Hong
 *
 */
public class LoadPagingActivity extends Activity implements OnScrollListener {

	private ListView listview;
	private int visibleLastIndex = 0; //���Ŀ���������
	private int visibleItemCount;  //��ǰ���ڵĿɼ�����Ŀ
	private int datasieze = 50;     //����������
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
				loadMoreButton.setText("���ڼ�����...");
				hander.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						loadMoreData();
						adapter.notifyDataSetChanged();
						loadMoreButton.setText("�鿴����...");
					}
				}, 2000);
			}
		});
		
		listview = (ListView) findViewById(R.id.PDmxList);
		//�����б�ײ���ͼ
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
		
		//������м�¼ѡ��������ݼ����������Ƴ��б�ײ���ͼ
		if(totalItemCount == datasieze)
		{
			listview.removeFooterView(loadMoreView);
			UIHelper.ToastMessage(this, "������ȫ������");
		}
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// ���ݼ����һ�������
		int itemsLastIndex = adapter.getCount()-1;
		int lastIndex = adapter.getCount();
		if(scrollState== OnScrollListener.SCROLL_STATE_IDLE
				&& visibleLastIndex == lastIndex)
		{
			//������Զ����أ���������������첽�������ݵĴ���
		}
	}
	
	/**
	 * ��ʼ��ListView��������
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
	 * ���ظ�������
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
		 * ��������б���
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
