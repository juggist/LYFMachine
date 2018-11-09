package com.icoffice.library.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

@SuppressLint("DrawAllocation")
public class TipImageView extends ImageView{
	public void setAttribute(ScaleType mScaleType,int width,int height,int left,int top,int right,int bottom){
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
		lp.leftMargin = left;
		lp.topMargin = top;
		lp.rightMargin = right;
		lp.bottomMargin = bottom;
		this.setLayoutParams(lp);
		this.setScaleType(mScaleType);
	}
	public TipImageView(Context context) {
		super(context);
	}
	public TipImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public TipImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
//		Rect rect = new Rect();
//		rect.left = 20;
//		rect.top = 20;
//		rect.right = 120;
//		rect.bottom = 120;
//		Paint paint = new Paint();
//		paint.setColor(Color.WHITE); // 颜色
//		paint.setStyle(Paint.Style.STROKE);
//		paint.setStrokeWidth(0);  
//		canvas.drawRect(rect, paint);
	}
	

}
