package com.jkinfo.animations.ui.custom;

import com.jkinfo.animations.utils.Utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class QuickIndexBar extends View {
	private static final String[] LETTERS = new String[] { 
			"A", "B", "C", "D", "E", "F", "G", 
			"H", "I", "J", "K", "L",
			"M", "N", "O", "P", "Q", 
			"R", "S", "T", "U", "V", 
			"W", "X", "Y", "Z" };
	private static final String TAG = "TAG";
	private Paint mPaint;
	private int cellWidth;
	private float cellHeight;
	//暴露一个字母的监听
	public interface OnLetterUpdateListener{
		void onLetterUpdate(String letter);
	}
	public OnLetterUpdateListener listener;
	public OnLetterUpdateListener getListener(){
		return listener;
	}
	
	//设置字母更新监听
	public void setListener(OnLetterUpdateListener listener){
		this.listener = listener;
	}
	
	public QuickIndexBar(Context context) {
		this(context, null);
	}
	public QuickIndexBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	public QuickIndexBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		//在Android中需要通过graphics类来显示2D图形。
		//graphics中包括了Canvas（画布）,Paint（画笔）,Color（颜色）,Bitmap（图像）等常用的类
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(Color.WHITE);
		mPaint.setTypeface(Typeface.DEFAULT_BOLD);
	}
	// 重写View之后,主要要重写onDraw方法才能进行绘制
	@Override
	protected void onDraw(Canvas canvas) {
		for (int i = 0; i < LETTERS.length; i++) {
			String text = LETTERS[i];
			// 计算坐标
			int x = (int) (cellWidth / 2.0f - mPaint.measureText(text) / 2.0f);
			// 获取文本的高度
			Rect bounds = new Rect();// 矩形
			mPaint.getTextBounds(text, 0, text.length(), bounds);
			int textHeight = bounds.height();
			int y = (int) (cellHeight / 2.0f + textHeight / 2.0f + i * cellHeight);
			float density = getResources().getDisplayMetrics().density;
			int textPx = (int)(28 * density) / 2; //在1280*720(2)屏幕中的字体为28px
			Log.d(TAG, "textPx" + textPx);
			// 根据按下的字母, 设置画笔颜色
			mPaint.setColor(touchIndex == i ? Color.GRAY : Color.WHITE);
						
			// 绘制文本A-Z
			mPaint.setTextSize(textPx); //设置字体大小
			canvas.drawText(text, x, y, mPaint);
			//如:canvas.drawText("3", x, y, paint);
			//指:x默认是‘3’这个字符的左边在屏幕的位置,y是指定这个字符baseline在屏幕上的位置
		}
	}
	int touchIndex = -1;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int index = -1;
		switch (MotionEventCompat.getActionMasked(event)) {
		case MotionEvent.ACTION_DOWN:
			// 获取当前触摸到的字母索引
			index = (int) (event.getY() / cellHeight);
			if(index >= 0 && index < LETTERS.length){
				// 判断是否跟上一次触摸到的一样
				if(index != touchIndex) {
					if (listener != null) {
						listener.onLetterUpdate(LETTERS[index]);
					}
					//Utils.showToast(getContext(), LETTERS[index]);
					Log.d(TAG, "onTouchEvent: " + LETTERS[index]);
					touchIndex = index;
				}
			}
			break;
		case MotionEvent.ACTION_MOVE:
			// 获取当前触摸到的字母索引
			index = (int) (event.getY() / cellHeight);
			if(index >= 0 && index < LETTERS.length){
				// 判断是否跟上一次触摸到的一样
				if(index != touchIndex) {
					if (listener != null) {
						listener.onLetterUpdate(LETTERS[index]);
					}
					//Utils.showToast(getContext(), LETTERS[index]);
					Log.d(TAG, "onTouchEvent: " + LETTERS[index]);
					touchIndex = index;
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			touchIndex = -1;
			break;
		default:
			break;
		}
		invalidate();  // 进行重绘
		return true;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		// 获取单元格的宽和高
		cellWidth = getMeasuredWidth();
		int mHeight = getMeasuredHeight();
		cellHeight = mHeight * 1.0f / LETTERS.length;
	}
}
