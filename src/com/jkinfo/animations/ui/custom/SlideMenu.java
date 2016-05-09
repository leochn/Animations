package com.jkinfo.animations.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

public class SlideMenu extends FrameLayout {   //extends ViewGroup
	private View menuView,mainView;
	private int menuWidth = 0;
	private Scroller scroller;

	public SlideMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SlideMenu(Context context) {
		super(context);
		init();
	}
	
	private void init(){
		scroller = new Scroller(getContext());
	}
	
	/**
	 * 当1级的子view全部加载完调用，可以用初始化子view的引用
	 * 注意，这里无法获取子view的宽高
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		menuView = getChildAt(0); //获取 include 中的布局
		mainView = getChildAt(1);
		menuWidth = menuView.getLayoutParams().width;
	}
	
	/**
	 * widthMeasureSpec和heightMeasureSpec是系统测量SlideMenu时传入的参数，
	 * 这2个参数测量出的宽高能让SlideMenu充满窗体，其实是正好等于屏幕宽高
	 */
	//因为onMesure的实现比较复杂,可以不重写该方法,让SlideMenuActivity直接继承FrameLayout
	//如果SlideMenuActivity直接继承ViewGroup,那么需要重写onMeasure方法
	
//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		
//		int measureSpec = MeasureSpec.makeMeasureSpec(menuWidth, MeasureSpec.EXACTLY);
//		
//		//测量所有子view的宽高
//		//通过getLayoutParams方法可以获取到布局文件中指定宽高
//		menuView.measure(measureSpec, heightMeasureSpec);
//		//直接使用SlideMenu的测量参数，因为它的宽高都是充满父窗体
//		mainView.measure(widthMeasureSpec, heightMeasureSpec);
//	}
	
	
	/**
	 * l: 当前子view的左边在父view的坐标系中的x坐标
	 * t: 当前子view的顶边在父view的坐标系中的y坐标
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		menuView.layout(-menuWidth, 0, 0, menuView.getMeasuredHeight());
		mainView.layout(0, 0, r, b);
	}
	
	
	private int downX;
	//是否拦截
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = (int) ev.getX();
			break;
			
		case MotionEvent.ACTION_MOVE:
			int deltax = (int) (ev.getX() - downX);
			if (Math.abs(deltax) > 8) {
				return true;
			}
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}
	
	//onTouchEvent:是否消费	1. return true:消费事件	2. return false:不消费事件
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = (int) event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			int moveX = (int) event.getX();
			int deltaX = moveX - downX;
			int newScrollX = getScrollX() - deltaX;
			if (newScrollX < - menuWidth) {
				newScrollX = - menuWidth;
			}
			if (newScrollX > 0) {
				newScrollX = 0;
			}
			scrollTo(newScrollX, 0);
			downX = moveX;
			break;
		case MotionEvent.ACTION_UP:
			if (getScrollX() > - menuWidth / 2) {
				closeMenu();
			}else {
				openMenu();
			}
			break;
		}
		return true;
	}
	
	
	/**
	 * 切换菜单的开和关
	 */
	public void switchMenu() {
		System.out.println("switchMenu.......");
		if(getScrollX()==0){
			//需要打开
			openMenu();
		}else {
			//需要关闭
			closeMenu();
		}
	}
	
	private void closeMenu(){
		scroller.startScroll(getScrollX(), 0, 0-getScrollX(), 0, 400);
		invalidate();
	}
	
	private void openMenu(){
		scroller.startScroll(getScrollX(), 0, -menuWidth-getScrollX(), 0, 400);
		invalidate();
	}
	
	/**
	 * Scroller不主动去调用这个方法
	 * 而invalidate()可以掉这个方法
	 * invalidate->draw->computeScroll
	 */
	@Override
	public void computeScroll() {
		super.computeScroll();
		if (scroller.computeScrollOffset()) {
			scrollTo(scroller.getCurrX(), 0);
			invalidate();
		}
	}

}
