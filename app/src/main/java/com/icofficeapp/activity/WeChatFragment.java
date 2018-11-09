package com.icofficeapp.activity;


import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.icofficeapp.R;
import com.icofficeapp.handler.WeChatHandler;
import com.icofficeapp.util.AnimaUtils;
/**
 * 微商城fragment
 * @author lufeisong
 *
 */
@SuppressLint({ "ValidFragment", "HandlerLeak" })
public class WeChatFragment extends BaseChildFragment{
	private BaseFragmentActivity mContext;
	
	private ImageView iv_pic;
	public WeChatFragment(){
		
	}
	public WeChatFragment(BaseFragmentActivity context){
		mContext = context;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_wechat, null);
		initView(view);
		initListener();
		initData();
		AnimaUtils animaUtils = new AnimaUtils();
		animaUtils.translateDown(view, 600, -600f,0f);
		return view;
	}
	void initView(View view){
		iv_pic = (ImageView) view.findViewById(R.id.fragment_wechat_iv);
	}
	void initListener(){
		
	}
	void initData(){
		mContext.setmWeChatFragment(this);
		ArrayList<String> pic_list = mMachineControl.getWechatAdList();
		if(pic_list.size() > 0)
			mImageLoaderControl.displayImageBig(pic_list.get(0), iv_pic, R.drawable.empty_detail);
		mContext.weChatShopTimerTask(WeChatHandler.WECHATSHOP_2_SHOP, WeChatHandler.WECHATSHOP_2_SHOP_TIME);
	}
	@Override
	public void onDestroy() {
		mContext.setmWeChatFragment(null);
		super.onDestroy();
	}
	@Override
	public void onStart() {
		
		super.onStart();
	}

	@Override
	public void onDetach() {
		mContext.cancleWeChatShopTask();
		super.onDetach();
	}
}
