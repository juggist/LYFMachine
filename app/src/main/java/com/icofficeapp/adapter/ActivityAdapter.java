package com.icofficeapp.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.icofficeapp.R;
import com.icoffice.library.bean.db.AdStatusBean;
import com.icoffice.library.utils.CommonUtils;
import com.icoffice.library.utils.FileUtil;

/**
 * 活动适配器
 * @author lufeisong
 *
 */
public class ActivityAdapter extends ICofficeBaseAdapter{
	private Context mContext;
	private ArrayList<AdStatusBean> pic_list = new ArrayList<AdStatusBean>();
	
	public ActivityAdapter(Context mContext,ArrayList<AdStatusBean> pic_list){
		this.mContext = mContext;
		this.pic_list = pic_list;
	}
	@Override
	public int getCount() {
		return pic_list==null?0:pic_list.size();
	}
	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder vh;
		if(arg1 == null){
			arg1 = LayoutInflater.from(mContext).inflate(R.layout.adapter_activity, null);
			vh = new ViewHolder();
			arg1.setTag(vh);
		}else{
			vh = (ViewHolder) arg1.getTag();
		}
		vh.iv_activityPic = (ImageView) arg1.findViewById(R.id.adapter_activity_iv);
		displayImage(FileUtil.ROOT_PATH+pic_list.get(arg0).getFile(), vh.iv_activityPic);
		return arg1;
	}
	class ViewHolder{
		ImageView iv_activityPic;
	}
}
