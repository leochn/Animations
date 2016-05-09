package com.jkinfo.animations.ui.displayActivity;

import java.util.ArrayList;
import java.util.Collections;

import com.jkinfo.animations.R;
import com.jkinfo.animations.adapter.HaoHanAdapter;
import com.jkinfo.animations.bean.Person;
import com.jkinfo.animations.ui.custom.QuickIndexBar;
import com.jkinfo.animations.ui.custom.QuickIndexBar.OnLetterUpdateListener;
import com.jkinfo.animations.utils.Cheeses;
import com.jkinfo.animations.utils.Utils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

public class QuickIndexBarDisplay extends Activity {
	private ListView mMainList;
	private ArrayList<Person> persons;
	private TextView tv_center;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_quickindexbardisplay);
		QuickIndexBar bar = (QuickIndexBar) findViewById(R.id.bar);
		bar.setListener(new OnLetterUpdateListener() {
			@Override
			public void onLetterUpdate(String letter) {
				//Utils.showToast(QuickIndexBarDisplay.this, letter);
				showLetter(letter);
				// 根据字母定位ListView, 找到集合中第一个以letter为拼音首字母的对象,得到索引
				for (int i = 0; i < persons.size(); i++) {
					Person person = persons.get(i);
					String text = person.getPinyin().charAt(0)+"";
					if (TextUtils.equals(letter, text)) {
						//匹配成功
						mMainList.setSelection(i);
						Utils.showToast(QuickIndexBarDisplay.this, letter);
						break;
					}
				}
			}
		});
		
		mMainList = (ListView) findViewById(R.id.lv_main);
		persons = new ArrayList<Person>();
		
		//填充数据 , 排序
		fillAndSortData(persons);
		mMainList.setAdapter(new HaoHanAdapter(QuickIndexBarDisplay.this,persons));
		tv_center = (TextView) findViewById(R.id.tv_center);
		
	}
	private void fillAndSortData(ArrayList<Person> persons) {
		// 填充数据 
		for (int i = 0; i < Cheeses.NAMES.length; i++) {
			String name = Cheeses.NAMES[i];
			persons.add(new Person(name));
		}
		// 进行排序
		Collections.sort(persons);
	}
	private Handler mHandler = new Handler();
	//显示字母
	private void showLetter(String letter) {
		tv_center.setVisibility(View.VISIBLE);
		tv_center.setText(letter);
		mHandler.removeCallbacksAndMessages(null);
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				tv_center.setVisibility(View.GONE);				
			}
		}, 2000);
	}
}
