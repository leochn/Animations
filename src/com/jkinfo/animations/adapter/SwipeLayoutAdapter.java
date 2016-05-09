package com.jkinfo.animations.adapter;


import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.jkinfo.animations.R;
import com.jkinfo.animations.ui.custom.SwipeLayout;
import com.jkinfo.animations.ui.custom.SwipeLayout.OnSwipeLayoutListener;
import com.jkinfo.animations.utils.Cheeses;
import com.jkinfo.animations.utils.Utils;

public class SwipeLayoutAdapter extends BaseAdapter implements ListAdapter {
	private Context context;
	private ArrayList<SwipeLayout> opendItems;
	public SwipeLayoutAdapter(Context context) {
		super();
		this.context = context;
		opendItems = new ArrayList<SwipeLayout>();
	}
	@Override
	public int getCount() {
		return Cheeses.NAMES.length;
	}
	@Override
	public Object getItem(int position) {
		return Cheeses.NAMES[position];
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		View view = convertView;
		if (convertView == null) {
			view = View.inflate(context, R.layout.swipelayoutdisplay_item_list, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_call = (TextView) view.findViewById(R.id.tv_call);
			viewHolder.tv_del = (TextView) view.findViewById(R.id.tv_del);
			view.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) view.getTag();
		}
		SwipeLayout sl = (SwipeLayout) view;
		
		viewHolder.tv_call.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int id = v.getId();
				if (id == R.id.tv_call) {
					Utils.showToast(context, "call ... onClick......");
				} 
			}
		});
		viewHolder.tv_del.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int id = v.getId();
				if (id == R.id.tv_del) {
					Utils.showToast(context, "del ... onClick......");
				} 
			}
		});
		
		sl.setSwipeLayoutListener(new OnSwipeLayoutListener(){
			@Override
			public void onStartOpen(SwipeLayout mSwipeLayout) {
				Utils.showToast(context, "onStartOpen");
				// 要去开启时,先遍历所有已打开条目, 逐个关闭
				for (SwipeLayout layout : opendItems) {
					layout.close();
				}
				opendItems.clear();
				opendItems.add(mSwipeLayout);
			}
			@Override
			public void onStartClose(SwipeLayout mSwipeLayout) {
				Utils.showToast(context, "onStartClose");
				for (SwipeLayout layout : opendItems) {
					layout.close();
				}
				opendItems.clear();
			}
			@Override
			public void onOpen(SwipeLayout mSwipeLayout) {
				Log.d("TAG", "setSwipeLayoutListener...onOpen");
				// 添加进集合
				opendItems.add(mSwipeLayout);
			}
			@Override
			public void onDraging(SwipeLayout mSwipeLayout) {
				//Log.d("TAG", "setSwipeLayoutListener...onDraging");
			}
			@Override
			public void onClose(SwipeLayout mSwipeLayout) {
				Log.d("TAG", "setSwipeLayoutListener...onClose");
				// 移除集合
				opendItems.remove(mSwipeLayout);
			}
		});
		return view;
	}
	static class ViewHolder{
		TextView tv_call;
		TextView tv_del;
	}
}
