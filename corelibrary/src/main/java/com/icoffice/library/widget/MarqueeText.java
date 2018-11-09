package com.icoffice.library.widget;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 自定义走马灯
 * @author lufeisong
 *
 */
public class MarqueeText extends TextView {
	public void setAttribute(float textSize,int bgColor,String textStr,int width,int height,int left,int top,int right,int bottom){
		this.setTextSize(textSize);
		this.setTextColor(bgColor);
		this.setText(textStr);
		this.setSingleLine();
		this.setEllipsize(TruncateAt.MARQUEE);
		this.setFocusableInTouchMode(true);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
		lp.leftMargin = left;
		lp.topMargin = top;
		lp.rightMargin = right;
		lp.bottomMargin = bottom;
		this.setLayoutParams(lp);
	}
	public MarqueeText(Context con) {
		super(con);
	}

	public MarqueeText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MarqueeText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean isFocused() {
		return true;
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction,
			Rect previouslyFocusedRect) {
	}
}