package com.jkinfo.animations.adapter;

import java.util.ArrayList;

import com.jkinfo.animations.R;
import com.jkinfo.animations.bean.Person;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class HaoHanAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<Person> persons;

	public HaoHanAdapter(Context mContext, ArrayList<Person> persons) {
		this.mContext = mContext;
		this.persons = persons;
	}
	@Override
	public int getCount() {
		return persons.size();
	}
	@Override
	public Object getItem(int position) {
		return persons.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		View view = convertView;
		if(convertView == null){
			view = view.inflate(mContext, R.layout.quickindexbardisplay_item_list, null);
			viewHolder = new ViewHolder();
			viewHolder.mIndex = (TextView) view.findViewById(R.id.tv_index);
			viewHolder.mName = (TextView) view.findViewById(R.id.tv_name);
			view.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) view.getTag();
		}
		Person p = persons.get(position);
		
		String str = null;
		String currentLetter = p.getPinyin().charAt(0) + "";
		// 根据上一个首字母,决定当前是否显示字母
		if(position == 0){
			str = currentLetter;
		}else {
			// 上一个人的拼音的首字母
			String preLetter = persons.get(position - 1).getPinyin().charAt(0) + "";
			if(!TextUtils.equals(preLetter, currentLetter)){
				str = currentLetter;
			}
		}
		// 根据str是否为空,决定是否显示索引栏
		viewHolder.mIndex.setVisibility(str == null ? View.GONE : View.VISIBLE);
		viewHolder.mIndex.setText(currentLetter);
		viewHolder.mName.setText(p.getName());
		return view;
	}
	
	static class ViewHolder {
		TextView mIndex;
		TextView mName;
	}
}
