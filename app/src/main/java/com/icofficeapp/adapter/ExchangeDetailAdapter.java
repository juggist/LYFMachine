package com.icofficeapp.adapter;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.icoffice.library.bean.db.RcodeBean;
import com.icoffice.library.utils.BitmapUtil;
import com.icoffice.library.utils.FileUtil;
import com.icofficeapp.R;
import com.icofficeapp.activity.BaseFragmentActivity;

/**
 * 兑换详情页面 适配器
 * 
 * @author lufeisong
 * 
 */
public class ExchangeDetailAdapter extends ICofficeBaseAdapter {
	private BaseFragmentActivity mContext;
	private  ArrayList<RcodeBean> list;
	public ExchangeDetailAdapter(BaseFragmentActivity context, ArrayList<RcodeBean> list){
		mContext = context;
		this.list = list;
	}
	@Override
	public int getCount() {
		return list.size();
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
			arg1 = LayoutInflater.from(mContext).inflate(R.layout.adapter_exchangedetail, null);
			arg1.setTag(vh);
		}else{
			vh = (ViewHolder) arg1.getTag();
		}
		vh.rl_pic_bg = (LinearLayout) arg1.findViewById(R.id.adapter_exchangedetail_goodsPic_bg);
		vh.iv_pic = (ImageView) arg1.findViewById(R.id.adapter_exchangedetail_goodsPic);
		vh.iv_sold_finish = (ImageView) arg1.findViewById(R.id.adapter_exchangedetail_sold_finish_iv);
		vh.tv_name_CN = (TextView) arg1.findViewById(R.id.adapter_exchangedetail_goodsName_CN);
		vh.tv_name_EN = (TextView) arg1.findViewById(R.id.adapter_exchangedetail_goodsName_EN);
		vh.tv_sold_status = (TextView) arg1.findViewById(R.id.adapter_exchangedetail_soldStatus);
		vh.tv_tip = (TextView) arg1.findViewById(R.id.adapter_exchangedetail_sold_finish);
		if(arg0 < list.size()){
			RcodeBean rcodeBean = list.get(arg0);
			vh.tv_name_CN.setText(rcodeBean.getG_name() + "");
			vh.tv_name_EN.setText(rcodeBean.getG_ename() + "");
			int storeNum = rcodeBean.getStoreNum();
			int amounts = Integer.parseInt(rcodeBean.getAmounts());
			int rest_storeNum = 0;
			if(storeNum > rest_storeNum){
				vh.iv_sold_finish.setVisibility(View.INVISIBLE);
				displayImage(FileUtil.ROOT_PATH + rcodeBean.getZ_image(), vh.iv_pic);
//				mImageLoader.displayImage("file://" + FileUtil.ROOT_PATH + rcodeBean.getZ_image(), vh.iv_pic);
			}else{
				vh.iv_sold_finish.setVisibility(View.VISIBLE);
				vh.iv_pic.setImageBitmap(BitmapUtil.convertGreyImg(FileUtil.ROOT_PATH + rcodeBean.getZ_image()));
			}
			if(amounts == 0){
				vh.tv_tip.setVisibility(View.INVISIBLE);
				vh.tv_tip.setTextColor(mContext.getResources().getColor(R.color.exchange_bg_02));
				vh.tv_name_CN.setTextColor(mContext.getResources().getColor(R.color.exchange_bg_02));
				vh.tv_name_EN.setTextColor(mContext.getResources().getColor(R.color.exchange_bg_02));
				vh.tv_sold_status.setText(mContext.getResources().getText(R.string.exchangedetail_06));
			}else{
				vh.tv_tip.setVisibility(View.VISIBLE);
				vh.tv_tip.setText("x" + amounts);
				vh.tv_tip.setTextColor(mContext.getResources().getColor(R.color.exchange_bg_01));
				vh.tv_sold_status.setText(mContext.getResources().getText(R.string.exchangedetail_05));
				vh.tv_sold_status.setTextColor(mContext.getResources().getColor(R.color.shop_arrow));
			}
		}else{
			vh.rl_pic_bg.setVisibility(View.INVISIBLE);
		}
		return arg1;
	}

	class ViewHolder {
		private LinearLayout rl_pic_bg;
		private ImageView iv_pic,iv_sold_finish;
		private TextView tv_name_CN, tv_name_EN,  tv_tip,tv_sold_status;
	}
}
