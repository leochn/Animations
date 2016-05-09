package com.jkinfo.animations.ui.displayActivity;

import com.jkinfo.animations.R;
import com.jkinfo.animations.adapter.SwipeLayoutAdapter;
import com.jkinfo.animations.ui.custom.SwipeLayout;
import com.jkinfo.animations.ui.custom.SwipeLayout.OnSwipeLayoutListener;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;

public class SwipeLayoutDisplay extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_swipelayoutdisplay);
		// SwipeLayout swipeLayout = (SwipeLayout) findViewById(R.id.sl);
		// swipeLayout.setSwipeLayoutListener(new OnSwipeLayoutListener() {
		// @Override
		// public void onStartOpen(SwipeLayout mSwipeLayout) {
		// Log.d("TAG", "setSwipeLayoutListener...onStartOpen");
		// }
		// @Override
		// public void onStartClose(SwipeLayout mSwipeLayout) {
		// Log.d("TAG", "setSwipeLayoutListener...onStartClose");
		// }
		// @Override
		// public void onOpen(SwipeLayout mSwipeLayout) {
		// Log.d("TAG", "setSwipeLayoutListener...onOpen");
		// }
		// @Override
		// public void onDraging(SwipeLayout mSwipeLayout) {
		// Log.d("TAG", "setSwipeLayoutListener...onDraging");
		// }
		// @Override
		// public void onClose(SwipeLayout mSwipeLayout) {
		// Log.d("TAG", "setSwipeLayoutListener...onClose");
		// }
		// });
		ListView mList = (ListView) findViewById(R.id.lv);
		mList.setAdapter(new SwipeLayoutAdapter(SwipeLayoutDisplay.this));
		
		
	}
}
