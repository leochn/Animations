package com.jkinfo.animations.ui.sortActivity;

import com.jkinfo.animations.ContantValue;
import com.jkinfo.animations.R;
import com.jkinfo.animations.ui.displayActivity.GooViewDisplay;
import com.jkinfo.animations.ui.displayActivity.QuickIndexBarDisplay;
import com.jkinfo.animations.ui.displayActivity.SlideMenuDisplay;
import com.jkinfo.animations.ui.displayActivity.SwipeLayoutDisplay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class TabMainActivity extends Activity implements OnItemClickListener{
	private ListView listView_anim_complex;
	private ArrayAdapter<String> itemAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setView();
		initView();
		setListener();
	}
	
	private void setView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE); //去掉title
		setContentView(R.layout.activity_anim_complex);
	}
	
	private void initView() {
		listView_anim_complex = (ListView) findViewById(R.id.listView_anim_complex);

		//adapter = new AnimationAdapter(this, ContantValue.tabName);
		itemAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, ContantValue.tabName);
	}
	
	private void setListener() {
		listView_anim_complex.setAdapter(itemAdapter);
		listView_anim_complex.setOnItemClickListener(this);  
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		System.out.println("TabMainActivity...onItemClick...position..." + position);
		switch (position) {
		case 0:  //  侧滑菜单
			startIntent(SlideMenuDisplay.class);
			break;
		case 1:  //  快速索引
			startIntent(QuickIndexBarDisplay.class);
			break;
		case 2:  //  侧拉删除
			startIntent(SwipeLayoutDisplay.class);
			break;
		case 3:  //  粘性控件
			startIntent(GooViewDisplay.class);
			break;

		default:
			break;
		}
	}
	
	
	/**
	 * 切换Activity
	 * @param class1
	 */
	public void startIntent(Class class1){
		Intent intent = new Intent(TabMainActivity.this,class1);
		startActivity(intent);
	}
}
