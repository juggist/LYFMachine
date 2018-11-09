package com.icoffice.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ListView;

public class TipListView extends ListView{
	public void setAttribute(int gravity,int bgColor,int width,int height,int left,int top,int right,int bottom) {
		this.setBackgroundColor(bgColor);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
		lp.width = width;
		lp.height = height;
		lp.leftMargin = left;
		lp.topMargin = top;
		lp.rightMargin = right;
		lp.bottomMargin = bottom;
		this.setLayoutParams(lp);
	}
	public TipListView(Context context) {
		super(context);
	}
	public TipListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public TipListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

}
