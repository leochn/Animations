package com.jkinfo.animations.ui.custom;

import com.jkinfo.animations.utils.GeometryUtil;
import com.jkinfo.animations.utils.Utils;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

public class GooView extends View {
	private static final String TAG = "TAG";
	private Paint mPaint;

	public GooView(Context context) {
		this(context, null);
	}

	public GooView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public GooView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// 初始化操作
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(Color.RED);
	}

	// 初始化变量参数
	PointF mDragCenter = new PointF(180f, 180f); // 拖拽圆 的圆心
	float mDragRadius = 40f; // 拖拽圆 的半径
	PointF mStickCenter = new PointF(260f, 260f); // 固定圆 的圆心
	float mStickRadius = 30f;
	// 计算拖拽圆 的附着点
	float mDragPointTempX = mDragCenter.x + (int) (mDragRadius / 1.414);
	float mDragPointTempY = mDragCenter.x - (int) (mDragRadius / 1.414);

	// 计算固定圆 的附着点
	float mStickPointTempX = mStickCenter.x + (int) (mStickRadius / 1.414);
	float mStickPointTempY = mStickCenter.x - (int) (mStickRadius / 1.414);

	PointF[] mDragPoints = new PointF[] { // 拖拽圆 的附着点
			new PointF(mDragPointTempX, mDragPointTempY), // 圆点的右上
			new PointF(mDragPointTempY, mDragPointTempX) // 圆点的左下
	};
	PointF[] mStickPoints = new PointF[] { // 固定圆 的附着点
			new PointF(mStickPointTempX, mStickPointTempY), // 圆点的右上
			new PointF(mStickPointTempY, mStickPointTempX) // 圆点的左下
	};
	float controlPointTempX = (mStickCenter.x - mDragCenter.x) / 2 + mDragCenter.x;
	PointF mControlPoint = new PointF(controlPointTempX, controlPointTempX); // 控制点
	private float farestDistance = 200f; // 拖拽的最远距离
	private int statusBarHeight;
	private boolean isOutofRange;
	private boolean isDisappear;

	@Override
	protected void onDraw(Canvas canvas) {
		// 计算连接点值, 控制点, 固定圆半径
		// 1. 获取固定圆半径(根据两圆圆心距离)
		float tempStickRadius = getTempStickRadius();
		// 2. 获取直线与圆的交点
		float yOffset = mStickCenter.y - mDragCenter.y;
		float xOffset = mStickCenter.x - mDragCenter.x;
		Double lineK = null;
		if (xOffset != 0) {
			lineK = (double) (yOffset / xOffset); // 直线的斜率（两个圆点组成的直线）
		}
		// 通过几何图形工具获取交点坐标
		mDragPoints = GeometryUtil.getIntersectionPoints(mDragCenter, mDragRadius, lineK);
		mStickPoints = GeometryUtil.getIntersectionPoints(mStickCenter, tempStickRadius, lineK);
		// 3. 获取控制点坐标
		mControlPoint = GeometryUtil.getMiddlePoint(mDragCenter, mStickCenter);
		// 保存画布状态
		canvas.save();
		canvas.translate(0, -statusBarHeight);
		// 画出最大范围(参考用)
		mPaint.setStyle(Style.STROKE);
		canvas.drawCircle(mStickCenter.x, mStickCenter.y, farestDistance, mPaint);
		mPaint.setStyle(Style.FILL);
		if (!isDisappear) {
			if (!isOutofRange) {
				// 4. 绘制连接线
				Path path = new Path();
				// 跳到点1
				path.moveTo(mStickPoints[0].x, mStickPoints[0].y);
				// 画曲线1 -> 2
				path.quadTo(mControlPoint.x, mControlPoint.y, mDragPoints[0].x, mDragPoints[0].y);
				// 画直线2 -> 3
				path.lineTo(mDragPoints[1].x, mDragPoints[1].y);
				// 画曲线3 -> 4
				path.quadTo(mControlPoint.x, mControlPoint.y, mStickPoints[1].x, mStickPoints[1].y);
				path.close();
				canvas.drawPath(path, mPaint);
				// 画附着点(参考用)
				mPaint.setColor(Color.BLUE);
				canvas.drawCircle(mDragPoints[0].x, mDragPoints[0].y, 6f, mPaint);
				canvas.drawCircle(mDragPoints[1].x, mDragPoints[1].y, 6f, mPaint);
				canvas.drawCircle(mStickPoints[0].x, mStickPoints[0].y, 6f, mPaint);
				canvas.drawCircle(mStickPoints[1].x, mStickPoints[1].y, 6f, mPaint);
				mPaint.setColor(Color.RED);
				// 5. 画固定圆
				canvas.drawCircle(mStickCenter.x, mStickCenter.y, tempStickRadius, mPaint);
			}
			// 6. 画拖拽圆
			canvas.drawCircle(mDragCenter.x, mDragCenter.y, mDragRadius, mPaint);
		}
		// 恢复上次的保存状态
		canvas.restore();
	}

	// 获取固定圆半径(根据两圆圆心距离)
	private float getTempStickRadius() {
		float distance = GeometryUtil.getDistanceBetween2Points(mDragCenter, mStickCenter);
		distance = Math.min(distance, farestDistance);
		// percent: 0.0f -> 1.0f
		float percent = distance / farestDistance;
		Log.d(TAG, "percent: " + percent);
		// mStickRadius: 100% -> 40%
		return evaluate(percent, mStickRadius, mStickRadius * 0.4f);
	}

	public Float evaluate(float fraction, Number startValue, Number endValue) {
		float startFloat = startValue.floatValue();
		return startFloat + fraction * (endValue.floatValue() - startFloat);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		statusBarHeight = Utils.getStatusBarHeight(this);
	}
	
	/**
	 * 更新拖拽圆圆心坐标,并重绘界面
	 * 
	 * @param x
	 * @param y
	 */
	private void updateDragCenter(float x, float y) {
		mDragCenter.set(x, y);
		invalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x;
		float y;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Log.d(TAG, "qwe...ACTION_DOWN...");
			isOutofRange = false;  
			isDisappear = false;
			x = event.getRawX();
			y = event.getRawY();
			updateDragCenter(x, y);
			break;
		case MotionEvent.ACTION_MOVE:
			isOutofRange = false;
			isDisappear = false;
			x = event.getRawX();
			y = event.getRawY();
			updateDragCenter(x, y);
			// 处理断开的事件
			float distance = GeometryUtil.getDistanceBetween2Points(mDragCenter, mStickCenter);
			if (distance > farestDistance) {
				isOutofRange = true; // 只绘制拖拽圆
				invalidate();
			}
			break;
		case MotionEvent.ACTION_UP:
			float d = GeometryUtil.getDistanceBetween2Points(mDragCenter, mStickCenter);
			if (d > farestDistance) {
				isOutofRange = true; // 只绘制拖拽圆
				isDisappear = true;
				invalidate();
				Log.d(TAG, "qwe...d > farestDistance");
			}else {
				// c. 拖拽没超出范围, 松手,弹回去
				final PointF tempDragCenter = new PointF(mDragCenter.x, mDragCenter.y);
				ValueAnimator mAnim = ValueAnimator.ofFloat(1.0f);
				mAnim.addUpdateListener(new AnimatorUpdateListener() {
					@Override
					public void onAnimationUpdate(ValueAnimator mAnim) {
						// 0.0 -> 1.0f
						float percent = mAnim.getAnimatedFraction();
						PointF p = GeometryUtil.getPointByPercent(tempDragCenter, mStickCenter, percent);
						updateDragCenter(p.x, p.y);
					}
				});
				mAnim.setInterpolator(new OvershootInterpolator(4));
				mAnim.setDuration(500);
				mAnim.start();
			}
			break;
		default:
			break;
		}
		return true;
	}
}
