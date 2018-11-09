package com.icofficeapp.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.icoffice.library.bean.db.SoldGoodsBean;
import com.icoffice.library.utils.BitmapUtil;
import com.icoffice.library.utils.FileUtil;
import com.icoffice.library.utils.GoodsWayUtil;
import com.icofficeapp.R;

@SuppressLint("ResourceAsColor")
public class ShopAdapter extends ICofficeBaseAdapter{
	private Context mContext;
	private ArrayList<SoldGoodsBean> mGoodsBean_current_list = new ArrayList<SoldGoodsBean>();
	public ShopAdapter(Context mContext,ArrayList<SoldGoodsBean> mGoodsBean_current_list){
		this.mContext = mContext;
		this.mGoodsBean_current_list = mGoodsBean_current_list;
	}
	@Override
	public int getCount() {
		return mGoodsBean_current_list.size();
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
			arg1 = LayoutInflater.from(mContext).inflate(R.layout.adapter_shop, null);
			arg1.setTag(vh);
		}else{
			vh = (ViewHolder) arg1.getTag();
		}
		vh.rl_goodsPic_bg = (RelativeLayout) arg1.findViewById(R.id.adapter_shop_goodsPic_bg);
		vh.ll_goodsName_bg = (LinearLayout) arg1.findViewById(R.id.adapter_shop_goodsName_bg);
		vh.iv_goodsPic = (ImageView) arg1.findViewById(R.id.adapter_shop_goodsPic);
		vh.tv_goodsName_CN = (TextView) arg1.findViewById(R.id.adapter_shop_goodsName_CN);
		vh.tv_goodsName_EN = (TextView) arg1.findViewById(R.id.adapter_shop_goodsName_EN);
		vh.tv_goodsPrice = (TextView) arg1.findViewById(R.id.adapter_shop_goodsPrice);
		vh.iv_soldFinish = (ImageView) arg1.findViewById(R.id.adapter_shop_sold_finish);
		
		vh.ll_goodsName_bg.setBackgroundColor(Color.WHITE);
		SoldGoodsBean mSoldGoodsBean = mGoodsBean_current_list.get(arg0);
		if(mSoldGoodsBean.getGt_id() .equals("1")){//饮料
			vh.rl_goodsPic_bg.setBackgroundColor(mContext.getResources().getColor(R.color.shop_drink));
		}else if(mSoldGoodsBean.getGt_id() .equals("2")){//食品
			vh.rl_goodsPic_bg.setBackgroundColor(mContext.getResources().getColor(R.color.shop_food));
		}else if(mSoldGoodsBean.getGt_id() .equals("3")){//其它
			vh.rl_goodsPic_bg.setBackgroundColor(mContext.getResources().getColor(R.color.shop_others));
		}
		int rest_storeNum = 0;
//		String w_code = GoodsWayUtil.getW_code(mSoldGoodsBean.getW_code());
		if(GoodsWayUtil.parseBox(mSoldGoodsBean.getW_code()).equals(GoodsWayUtil.DRINKS_BOX_NAME) || GoodsWayUtil.parseBox(mSoldGoodsBean.getW_code()).equals(GoodsWayUtil.DRINKS_2_BOX_NAME))
			rest_storeNum = 1;
		if(mSoldGoodsBean.getStoreNum() > rest_storeNum){
			vh.iv_soldFinish.setVisibility(View.INVISIBLE);
			displayImage(FileUtil.ROOT_PATH + mSoldGoodsBean.getZ_image(), vh.iv_goodsPic);
//			mImageLoader.displayImage("file://" + FileUtil.ROOT_PATH + mSoldGoodsBean.getZ_image(), vh.iv_goodsPic);
			vh.tv_goodsName_CN.setTextColor(R.color.shop_info_normal);
			vh.tv_goodsName_EN.setTextColor(R.color.shop_info_normal);
			vh.tv_goodsPrice.setTextColor(R.color.shop_info_normal);
		}else{
			vh.iv_soldFinish.setVisibility(View.VISIBLE);
			vh.iv_goodsPic.setImageBitmap(BitmapUtil.convertGreyImg(FileUtil.ROOT_PATH + mSoldGoodsBean.getZ_image()));
			vh.tv_goodsName_CN.setTextColor(R.color.shop_info_finish);
			vh.tv_goodsName_EN.setTextColor(R.color.shop_info_finish);
			vh.tv_goodsPrice.setTextColor(R.color.shop_info_finish);
		}
		vh.tv_goodsName_CN.setText(mSoldGoodsBean.getG_name());
		vh.tv_goodsName_EN.setText(mSoldGoodsBean.getG_ename());
		vh.tv_goodsPrice.setText(Float.parseFloat(mSoldGoodsBean.getUnit_price()) / 100 + "元");
		
		return arg1;
	}
	class ViewHolder{
		private RelativeLayout rl_goodsPic_bg;
		private LinearLayout ll_goodsName_bg;
		private ImageView iv_goodsPic,iv_soldFinish;
		private TextView tv_goodsName_CN,tv_goodsName_EN;
		private TextView tv_goodsPrice;
	}
}
