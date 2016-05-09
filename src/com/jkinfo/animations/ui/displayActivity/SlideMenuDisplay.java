package com.jkinfo.animations.ui.displayActivity;



import com.jkinfo.animations.R;
import com.jkinfo.animations.ui.custom.SlideMenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class SlideMenuDisplay extends Activity {
	private ImageView btn_back;
	private SlideMenu slideMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_slidemenudisplay);
		btn_back = (ImageView) findViewById(R.id.btn_back);
		slideMenu = (SlideMenu) findViewById(R.id.slideMenu);
		
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				slideMenu.switchMenu();
			}
		});
	}
}
