package com.icoffice.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class TipButton extends Button{
	public void setAttribute(String name,float textSize,int textColor,int bgColor,int gravity,int width,int height,int left,int top,int right,int bottom) {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
		lp.width = width;
		lp.height = height;
		lp.leftMargin = left;
		lp.topMargin = top;
		lp.rightMargin = right;
		lp.bottomMargin = bottom;
		this.setLayoutParams(lp);
		this.setText(name);
		this.setTextSize(textSize);
		this.setTextColor(textColor);
		this.setBackgroundColor(bgColor);
	}
	public void setAttribute(String name,float textSize,int textColor,int bgColor,int width,int height) {
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, height);
		lp.width = width;
		lp.height = height;
		lp.leftMargin = 180;
		this.setLayoutParams(lp);
		this.setText(name);
		this.setTextSize(textSize);
		this.setTextColor(textColor);
		this.setBackgroundColor(bgColor);
	}
	public TipButton(Context context) {
		super(context);
	}
	public TipButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public TipButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
}
