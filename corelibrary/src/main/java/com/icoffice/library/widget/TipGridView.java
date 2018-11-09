package com.icoffice.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class TipGridView extends GridView{
	public void setAttribute(int numColumns,int scrollBarStyle,int horizontalSpacing,int verticalSpacing){
		this.setNumColumns(numColumns);
		this.setScrollBarStyle(scrollBarStyle);
		this.setHorizontalSpacing(horizontalSpacing);
		this.setVerticalSpacing(verticalSpacing);
	}
	public TipGridView(Context context) {
		super(context);
	}
	public TipGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public TipGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
}
