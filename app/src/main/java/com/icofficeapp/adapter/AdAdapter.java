package com.icofficeapp.adapter;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.icoffice.library.utils.BitmapUtil;
import com.icoffice.library.utils.CommonUtils;
import com.icoffice.library.utils.FileUtil;
import com.icofficeapp.R;
/**
 * 广告位小图适配器
 * @author lufeisong
 *
 */
public class AdAdapter extends ICofficeBaseAdapter{
	private Context mContext;
	private int adapter_size;
	private ArrayList<String> ad_picture_list;
	public AdAdapter(Context mContext,int adapter_size,ArrayList<String> ad_picture_list){
		this.mContext = mContext;
		this.adapter_size = adapter_size;
		this.ad_picture_list = ad_picture_list;
		CommonUtils.showLog(CommonUtils.EXCEPTION_TAG, "AdApter 构造函数");
	}
	
	@Override
	public int getCount() {
		return adapter_size;
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
		ViewHolder vh = null;
		if(arg1 == null){
			vh = new ViewHolder();
			if(mContext == null){
				CommonUtils.showLog(CommonUtils.EXCEPTION_TAG, "mContext == null");
			}
			arg1 = LayoutInflater.from(mContext).inflate(R.layout.adapter_ad, null);
			arg1.setTag(vh);
		}else{
			vh = (ViewHolder) arg1.getTag();
		}
		vh.iv_smallPic = (ImageView) arg1.findViewById(R.id.adapter_ad_smallPic);
		if(arg0 < ad_picture_list.size()){
			String img_path = FileUtil.ROOT_PATH + ad_picture_list.get(arg0);
			if(BitmapUtil.isImage(img_path))
				displayImage(img_path, vh.iv_smallPic);
			else if(BitmapUtil.isMedia(img_path))
				vh.iv_smallPic.setImageBitmap(BitmapUtil.createVideoThumbnail(img_path));
		}
		else{
			if(arg0 == ad_picture_list.size())
				displayImage(R.drawable.ad_smallpic_recruit, vh.iv_smallPic);
			else
				vh.iv_smallPic.setImageBitmap(null);
		}
		return arg1;
	}
	class ViewHolder{
		private ImageView iv_smallPic;
	}
}
