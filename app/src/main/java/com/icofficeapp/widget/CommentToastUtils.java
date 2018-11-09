package com.icofficeapp.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.icofficeapp.R;
/**
 * 自定义toast
 * @author lufeisong
 *
 */
public class CommentToastUtils extends Toast{
	private static CommentToastUtils mCommentToastUtils;
	private Context mContext;
	private CommentToastUtils(Context context) {
		super(context);
		mContext = context;
	}
	public static CommentToastUtils getInstance(Context context){
		if(mCommentToastUtils == null)
			mCommentToastUtils = new CommentToastUtils(context);
		return mCommentToastUtils;
	}
	public void show(String str){
		Toast toastCustom = new Toast(mContext);
		LayoutInflater inflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflate.inflate(R.layout.library_toast, null);
		// 在这里初始化一下里面的文字啊什么的
		toastCustom.setView(v);
		TextView tv = (TextView) v.findViewById(R.id.library_toast_tv);
		tv.setText(str + "");
		toastCustom.setGravity(Gravity.CENTER, 0, 0);
		toastCustom.setDuration(Toast.LENGTH_LONG);
		toastCustom.show();
	}
}
