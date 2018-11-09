package com.icoffice.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;

public class TipEditText extends EditText{
	public void setAttribute(float textSize,int width,int height,int left,int top,int right,int bottom){
		this.setTextSize(textSize);
		this.setSingleLine();
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
		lp.leftMargin = left;
		lp.topMargin = top;
		lp.rightMargin = right;
		lp.bottomMargin = bottom;
		this.setLayoutParams(lp);
	}
	public TipEditText(Context context) {
		super(context);
	}
	public TipEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public TipEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

}
