package com.icoffice.library.widget;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

@SuppressLint("DrawAllocation")
public class TipLinearLayout extends LinearLayout {

	public void setAttribute(int gravity,int bgColor,int oreientation,int width,int height,int left,int top,int right,int bottom) {
		this.setBackgroundColor(bgColor);
		this.setOrientation(oreientation);
		this.setGravity(gravity);
		LayoutParams lp = new LayoutParams(width, height);
		lp.width = width;
		lp.height = height;
		lp.leftMargin = left;
		lp.topMargin = top;
		lp.rightMargin = right;
		lp.bottomMargin = bottom;
		this.setLayoutParams(lp);
	}
	public void setAttribute(int oreientation,int width,int height,int left,int top,int right,int bottom) {
		this.setOrientation(oreientation);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, height);
		lp.width = width;
		lp.height = height;
		lp.leftMargin = left;
		lp.topMargin = top;
		lp.rightMargin = right;
		lp.bottomMargin = bottom;
		lp.addRule(RelativeLayout.CENTER_IN_PARENT);
		this.setLayoutParams(lp);
	}
	public void setAttribute(int bgColor,int oreientation,int width,int height) {
		this.setBackgroundColor(bgColor);
		this.setOrientation(oreientation);
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(width, height);
		lp.width = width;
		lp.height = height;
		this.setLayoutParams(lp);
	}
	public void setAttribute(int oreientation,int width,int height) {
		this.setOrientation(oreientation);
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, height);
		lp.width = width;
		lp.height = height;
		this.setLayoutParams(lp);
	}
	public TipLinearLayout(Context context) {
		super(context);
	}

	public TipLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TipLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

}
