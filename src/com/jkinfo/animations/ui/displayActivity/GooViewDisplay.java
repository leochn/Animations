package com.jkinfo.animations.ui.displayActivity;

import com.jkinfo.animations.ui.custom.GooView;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class GooViewDisplay extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(new GooView(GooViewDisplay.this));
	}
}
