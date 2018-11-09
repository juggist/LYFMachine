package com.icoffice.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class TipImageButton extends ImageButton{
	public void setAttribute(ScaleType mScaleType,int gravity,int width,int height,int left,int top,int right,int bottom) {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
		lp.width = width;
		lp.height = height;
		lp.leftMargin = left;
		lp.topMargin = top;
		lp.rightMargin = right;
		lp.bottomMargin = bottom;
		this.setLayoutParams(lp);
		this.setScaleType(mScaleType);
	}
	public TipImageButton(Context context) {
		super(context);
	}
	public TipImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public TipImageButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

}
