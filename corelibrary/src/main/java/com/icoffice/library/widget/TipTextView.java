package com.icoffice.library.widget;



import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TipTextView extends TextView{
	public void setAttribute(float textSize,int bgColor,String textStr,int width,int height,int left,int top,int right,int bottom){
		this.setTextSize(textSize);
		this.setTextColor(bgColor);
		this.setText(textStr);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
		lp.leftMargin = left;
		lp.topMargin = top;
		lp.rightMargin = right;
		lp.bottomMargin = bottom;
		this.setLayoutParams(lp);
	}
	public TipTextView(Context context) {
		super(context);
	}
	public TipTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public TipTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}
	
}
