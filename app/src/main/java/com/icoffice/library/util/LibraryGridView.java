package com.icoffice.library.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class LibraryGridView extends GridView {

	public LibraryGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LibraryGridView(Context context) {
		super(context);
	}

	public LibraryGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}