package com.icoffice.library.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @Author Jiezhi.G
 * @CreateTime 2014-9-11
 * @UpdateTime 2014-9-11
 * @Function
 */
public class LedTextView extends TextView
{

	public LedTextView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public LedTextView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public LedTextView(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	private void init(Context context)
	{
		Typeface typeface = Typeface.createFromAsset(context.getAssets(),"font/digital-7.ttf");
		setTypeface(typeface);
	}
}
