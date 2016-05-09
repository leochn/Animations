package com.jkinfo.animations;



import com.jkinfo.animations.ui.sortActivity.TabMainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnItemClickListener {

	private TextView tvHint;
	private ListView listView_animation;
	private ArrayAdapter<String> itemAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setView();
		initView();
		setListener();
		initData();
	}

	private void setView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE); //去掉title
		setContentView(R.layout.activity_splash);
	}

	private void initView() {
		listView_animation = (ListView) findViewById(R.id.listView_animation);
		tvHint = (TextView) findViewById(R.id.tv_hint);
		itemAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, ContantValue.mainItem);

		Animation ani = new AlphaAnimation(0f, 1f);
		ani.setDuration(1500);
		ani.setRepeatMode(Animation.REVERSE);
		ani.setRepeatCount(Animation.INFINITE);
		tvHint.startAnimation(ani);
	}
	private void setListener() {
		listView_animation.setAdapter(itemAdapter);
		listView_animation.setOnItemClickListener(this);  
	}
	
	//ListView的点击事件(listView_animation)
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = null;		
		System.out.println("MainActivity...onItemClick...position..." + position);
		switch (position) {
		case 0:
			
			break;
		case 9: //页面滑动切换效果集合
			startIntent(TabMainActivity.class);
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
		Intent intent = new Intent(MainActivity.this,class1);
		startActivity(intent);
	}
	
	
	
	private void initData() {
		
	}

}
