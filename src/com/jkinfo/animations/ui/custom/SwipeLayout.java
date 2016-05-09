package com.jkinfo.animations.ui.custom;

import com.jkinfo.animations.utils.Utils;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 侧拉删除
 * 
 * @author leo
 *
 */
public class SwipeLayout extends FrameLayout {
	//定义状态枚举
	public static enum Status{
		Close, Open, Draging
	}
	public static interface OnSwipeLayoutListener {
		void onClose(SwipeLayout mSwipeLayout);
		void onOpen(SwipeLayout mSwipeLayout);
		void onDraging(SwipeLayout mSwipeLayout);
		// 要去关闭
		void onStartClose(SwipeLayout mSwipeLayout);
		// 要去开启
		void onStartOpen(SwipeLayout mSwipeLayout);
	}
	
	private Status status = Status.Close; //默认状态Close
	
	private OnSwipeLayoutListener swipeLayoutListener;
	
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public OnSwipeLayoutListener getSwipeLayoutListener() {
		return swipeLayoutListener;
	}
	public void setSwipeLayoutListener(OnSwipeLayoutListener swipeLayoutListener) {
		this.swipeLayoutListener = swipeLayoutListener;
	}
	
	private ViewDragHelper mDragHelper;
	public SwipeLayout(Context context) {
		this(context, null);
	}
	public SwipeLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// 1.初始化ViewDragHelper
		mDragHelper = ViewDragHelper.create(this, 1.0f, mCallback);
	}
	// 3. 重写监听
	ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {
		//3.1根据返回结果决定当前child是否可以拖拽
		@Override
		public boolean tryCaptureView(View arg0, int arg1) {
			return true;
		}
		//3.2限定移动范围,根据建议值修正将要移动到的(横向)位置
		public int clampViewPositionHorizontal(View child, int left, int dx) {
			// left
			if (child == mFrontView) {
				if (left > 0) {
					return 0;
				} else if (left < -mRange) {
					return -mRange;
				}
			} else if (child == mBackView) {
				if (left > mWidth) {
					return mWidth;
				} else if (left < mWidth - mRange) {
					return mWidth - mRange;
				}
			}
			return left;
		};
		//3.3当View位置改变时,处理要做的事情,(传递偏移量)
		public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
			// 传递事件
			if (changedView == mFrontView) {
				mBackView.offsetLeftAndRight(dx);
			} else if (changedView == mBackView) {
				mFrontView.offsetLeftAndRight(dx);
			}
			dispatchSwipeEvent();//分发事件
			// 兼容老版本
			invalidate();
		};
		//3.4当View被释放的时候, 处理的事情(执行动画)
		public void onViewReleased(View releasedChild, float xvel, float yvel) {
			if (xvel == 0 && mFrontView.getLeft() < -mRange / 2.0f) {
				open();
			}else if (xvel < 0) {
				open();
			}else {
				close();
			}
		}
	};
	
	private void dispatchSwipeEvent() {
		if (swipeLayoutListener != null) {
			swipeLayoutListener.onDraging(this);
		}
		//记录上一次的状态
		Status preStatus = status;
		status = updateStatus();
		if (preStatus != status && swipeLayoutListener != null) {
			if (status == Status.Close) {
				swipeLayoutListener.onClose(this);
			}else if (status == Status.Open) {
				swipeLayoutListener.onOpen(this);
			}else if (status == Status.Draging) {
				if (preStatus == Status.Close) {
					//原来是关闭状态,现在是拖动状态,开始打开
					swipeLayoutListener.onStartOpen(this);
				}else if (preStatus == Status.Open) {
					//原来是打开状态,现在是拖动状态,开始关闭
					swipeLayoutListener.onStartClose(this);
				}
			}
		}
	}
	private Status updateStatus() {
		int left = mFrontView.getLeft();
		if (left == 0) {
			return Status.Close;
		}
		if(left == 0 - mRange){
			return Status.Open;
		}
		return Status.Draging;
	}
	public void open() {
		Utils.showToast(getContext(), "Open");
		//layoutContent(true);
		open(true);
	};
	public void open(boolean isSmooth){
		int finalLeft = -mRange;
		if(isSmooth){
			//3.4.1.触发一个平滑动画
			if(mDragHelper.smoothSlideViewTo(mFrontView, finalLeft, 0)){
				ViewCompat.postInvalidateOnAnimation(this);
			}
		}else {
			layoutContent(true);
		}
	}
	public void close() {
		Utils.showToast(getContext(), "Close");
		//layoutContent(false);
		close(true);
	}
	public void close(boolean isSmooth){
		int finalLeft = 0;
		if(isSmooth){
			//开始动画
			if(mDragHelper.smoothSlideViewTo(mFrontView, finalLeft, 0)){
				ViewCompat.postInvalidateOnAnimation(this);
			}
		}else {
			layoutContent(false);
		}
	}
	//3.4.2持续平滑动画(高频率调用)
	@Override
	public void computeScroll() {
		super.computeScroll();
		
		if(mDragHelper.continueSettling(true)){
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}

	private View mBackView;
	private View mFrontView;
	private int mHeight;
	private int mWidth;
	private int mRange;
	// 2.传递触摸事件
	public boolean onInterceptTouchEvent(android.view.MotionEvent ev) {
		return mDragHelper.shouldInterceptTouchEvent(ev);
	};

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		try {
			mDragHelper.processTouchEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
		// 摆放位置
		layoutContent(false);
	}

	private void layoutContent(boolean isOpen) {
		// 摆放前View
		Rect frontRect = computeFrontViewRect(isOpen);
		mFrontView.layout(frontRect.left, frontRect.top, frontRect.right, frontRect.bottom);
		// 摆放后View(Call, Delete ),(后面绘制的会把前面绘制的挡住)
		Rect backRect = computeBackViewRect(frontRect);
		mBackView.layout(backRect.left, backRect.top, backRect.right, backRect.bottom);
		// 调整顺序,把mFrontView前置
		bringChildToFront(mFrontView);
	}
	private Rect computeBackViewRect(Rect frontRect) {
		int left = frontRect.right;
		return new Rect(left, 0, left + mRange, 0 +mHeight);
	}
	private Rect computeFrontViewRect(boolean isOpen) {
		int left = 0;
		if (isOpen) {
			left = -mRange;
		}
		return new Rect(left, 0, left + mWidth, 0 +mHeight);
	}
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		// 当xml被填充完毕时调用
		mBackView = getChildAt(0); // call, delete
		mFrontView = getChildAt(1);
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mHeight = mFrontView.getMeasuredHeight();
		mWidth = mFrontView.getMeasuredWidth();
		mRange = mBackView.getMeasuredWidth();
	}
}
