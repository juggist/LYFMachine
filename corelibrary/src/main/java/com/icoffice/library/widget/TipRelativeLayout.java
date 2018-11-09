package com.icoffice.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class TipRelativeLayout extends RelativeLayout{
	public void setAttribute(int gravity,int bgColor,int width,int height,int left,int top,int right,int bottom) {
		this.setBackgroundColor(bgColor);
//		this.setGravity(gravity);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
		lp.width = width;
		lp.height = height;
		lp.leftMargin = left;
		lp.topMargin = top;
		lp.rightMargin = right;
		lp.bottomMargin = bottom;
		this.setLayoutParams(lp);
	}
	public TipRelativeLayout(Context context) {
		super(context);
	}
	public TipRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public TipRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	

}
