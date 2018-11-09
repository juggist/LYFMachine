package com.icoffice.library.backadapter;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.icoffice.library.backactivity.BaseFragmentActivity;
import com.icoffice.library.bean.GoodsStateBean;
import com.icoffice.library.bean.db.GoodsStatusBean;
import com.icoffice.library.db.DbHelper;
import com.icoffice.library.moudle.control.BaseMachineControl;
import com.icoffice.library.utils.BitmapUtil;
import com.icoffice.library.utils.CommonUtils;
import com.icoffice.library.utils.FileUtil;
import com.icoffice.library.utils.NetWork;
import com.icoffice.library.widget.MarqueeText;
import com.icofficeapp.R;

public class GoodsStatusAdapter extends LibraryBaseAdapter {
	protected BaseFragmentActivity mContext;
	protected ArrayList<GoodsStateBean> list;
	protected ArrayList<GoodsStatusBean> GoodsStatusBeanList = new ArrayList<GoodsStatusBean>();
	protected ArrayList<ViewHolder> viewHolderList = new ArrayList<ViewHolder>();
	protected StringBuffer sb;
	public List<String>imgUrls = new ArrayList<String>();
	
	private BaseMachineControl mBaseMachineControl;
	public GoodsStatusAdapter(BaseFragmentActivity mContext,ArrayList<GoodsStateBean> list,BaseMachineControl mBaseMachineControl){
		this.mContext = mContext;
		this.list = list;
		this.mBaseMachineControl = mBaseMachineControl;
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

	@SuppressWarnings("unused")
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder vh = null;
		if(vh == null){
			vh = new ViewHolder();
			arg1 = LayoutInflater.from(mContext).inflate(R.layout.library_adapter_goods_set, null);
			vh.g_image_iv = (ImageView) arg1.findViewById(R.id.goods_set_iv);
			vh.g_name_tv = (MarqueeText) arg1.findViewById(R.id.goods_set_name);
			vh.g_code_tv = (MarqueeText) arg1.findViewById(R.id.goods_set_g_code);
			vh.g_price_tv = (MarqueeText) arg1.findViewById(R.id.goods_set_price);
			vh.change_price_et = (EditText) arg1.findViewById(R.id.goods_set_et);
			vh.change_price_btn = (Button) arg1.findViewById(R.id.goods_set_commit);
			vh.commit_status_btn = (Button) arg1.findViewById(R.id.goods_set_status);
			arg1.setTag(vh);
		}else{
			vh = (ViewHolder) arg1.getTag();
		}
		
		
		if(0 == viewHolderList.size()){
			viewHolderList.add(vh);
		}else if(1 == viewHolderList.size() ){
			if(arg0 == 0){
				viewHolderList.remove(0);
				viewHolderList.add(vh);
			}else{
				if(arg0 >= (viewHolderList.size() - 1) && viewHolderList.size() < list.size())
					viewHolderList.add(vh);
			}
		}else{
			if(arg0 != 0){
				if(arg0 >= (viewHolderList.size() - 1) && viewHolderList.size() < list.size())
					viewHolderList.add(vh);
			}
		}
		vh.goodsStateBean = list.get(arg0);
		vh.init_price = vh.goodsStateBean.getPrice();
		displayImage(FileUtil.ROOT_PATH + vh.goodsStateBean.getZ_image(), vh.g_image_iv);
//		mImageLoader.displayImage("file://" + FileUtil.ROOT_PATH + vh.goodsStateBean.getZ_image(), vh.g_image_iv);
		vh.g_name_tv.setText(vh.goodsStateBean.getG_name());
		vh.g_code_tv.setText(vh.goodsStateBean.getG_code());
		vh.g_price_tv.setText(CommonUtils.priceExchange(vh.goodsStateBean.getPrice()));
		if(vh.goodsStateBean.isSold_status() == 0){
			BitmapUtil.displayBackground2Drawable(R.drawable.library_off, vh.commit_status_btn);
			vh.init_sold_state = 0;
		}
		else if(vh.goodsStateBean.isSold_status() == 1){
			BitmapUtil.displayBackground2Drawable(R.drawable.library_on, vh.commit_status_btn);
			vh.init_sold_state = 1;
			imgUrls.add(vh.goodsStateBean.getZ_detail_image());
			imgUrls.add(vh.goodsStateBean.getZ_image());
		}
			
		vh.change_price_btn.setOnClickListener(changePriceBtnListener(vh));
		vh.commit_status_btn.setOnClickListener(commitStatusBtnListener(vh));
		return arg1;
	}
	private class ViewHolder{
		
		private ImageView g_image_iv;
		private MarqueeText g_name_tv;
		private MarqueeText g_code_tv;
		private MarqueeText g_price_tv;
		private EditText change_price_et;
		
		private Button change_price_btn;
		private Button commit_status_btn;
		
		private GoodsStateBean goodsStateBean;
		private Integer init_sold_state;
		private String init_price;
	}
	View.OnClickListener changePriceBtnListener(final ViewHolder vh){
		return new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String etStr = vh.change_price_et.getEditableText().toString() + "";
				if(CommonUtils.bPositiveIntgerNum(etStr)){
					vh.change_price_et.setText("");
					vh.g_price_tv.setText(CommonUtils.priceExchange(etStr));
					vh.goodsStateBean.setPrice(etStr);
				}else{
//					CommonUtils.showToast(mContext, "请输入整数");
					mContext.commentToast.show("请输入整数");
				}
			}
		};
	}
	View.OnClickListener commitStatusBtnListener(final ViewHolder vh){

		return new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(mBaseMachineControl.checkSoldStatus(vh.goodsStateBean.getG_code())){
					//关闭商品选项
					if(vh.goodsStateBean.isSold_status() == 1){
						vh.goodsStateBean.setSold_status(0);
						BitmapUtil.displayBackground2Drawable(R.drawable.library_off, vh.commit_status_btn);
					for (int i = 0; i < imgUrls.size(); i++) {
						if(imgUrls.get(i).equals(vh.goodsStateBean.getZ_detail_image())){
							imgUrls.remove(i);
						}if(imgUrls.get(i).equals(vh.goodsStateBean.getZ_image())){
							imgUrls.remove(i);
						}
					}
					
					}else if(vh.goodsStateBean.isSold_status() == 0){
						//打开商品选项
						vh.goodsStateBean.setSold_status(1);
						BitmapUtil.displayBackground2Drawable(R.drawable.library_on, vh.commit_status_btn);
						imgUrls.add(vh.goodsStateBean.getZ_detail_image());
						imgUrls.add(vh.goodsStateBean.getZ_image());
					}
					
				}else{
					com.icoffice.library.utils.CommonUtils.showToast(mContext, "库存量大于0，不能退库");
				}
			}
		};
	}
	public void commitStart(NetWork mNetWork){
		sb = new StringBuffer();
		for(int i = 0;i < viewHolderList.size();i++){
			ViewHolder vh = viewHolderList.get(i);
			if(vh.goodsStateBean.getG_code() != null && !vh.goodsStateBean.getG_code().equals(""))
				if(!vh.init_price.equals(vh.goodsStateBean.getPrice()) || vh.init_sold_state != vh.goodsStateBean.isSold_status()){
					int sold_state = 0;
					if(vh.goodsStateBean.isSold_status() == 1)
						sold_state = 1;
					else if(vh.goodsStateBean.isSold_status() == 0)
						sold_state = 0;
					sb.append((vh.goodsStateBean.getG_code() + "," + sold_state + "," +  vh.goodsStateBean.getPrice() + "|"));
				}
		}
		CommonUtils.showLog(CommonUtils.GOODS_TAG,"sb = " + sb.toString());
		mNetWork.postGoodsState(sb.toString());
	}
	public void commitSuccess(DbHelper mDbHelper){
		
		for(int i = 0;i < viewHolderList.size();i++){
			ViewHolder vh = viewHolderList.get(i);
			if(vh.goodsStateBean.getG_code() != null && !vh.goodsStateBean.getG_code().equals("")){
				GoodsStatusBean goodsStatusBean = new GoodsStatusBean();
				vh.init_price = vh.goodsStateBean.getPrice();
				vh.init_sold_state = vh.goodsStateBean.isSold_status();
				goodsStatusBean.setG_code(vh.goodsStateBean.getG_code());
				goodsStatusBean.setSold_status(vh.init_sold_state);
				goodsStatusBean.setUnit_price(vh.init_price);
				GoodsStatusBeanList.add(goodsStatusBean);
			}
		}
		mDbHelper.updateGoodsStatusBean(GoodsStatusBeanList);
		GoodsStatusBeanList.clear();
	}
	public void commitFail(NetWork mNetWork){
	}
	
}
