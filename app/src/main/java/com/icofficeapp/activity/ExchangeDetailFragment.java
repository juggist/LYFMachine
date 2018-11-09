package com.icofficeapp.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.icoffice.library.bean.db.RcodeBean;
import com.icoffice.library.bean.network.StatusCheckRcode;
import com.icoffice.library.handler.BaseViewHandler;
import com.icoffice.library.utils.CommonUtils;
import com.icoffice.library.utils.GoodsWayUtil;
import com.icoffice.library.utils.Utils;
import com.icofficeapp.R;
import com.icofficeapp.adapter.ExchangeDetailAdapter;
import com.icofficeapp.handler.ExchangeDetailHandler;

/**
 * 兑换详情页面
 * @author lufeisong
 *
 */
@SuppressLint("ValidFragment")
public class ExchangeDetailFragment extends BaseChildFragment implements OnClickListener{
	private BaseFragmentActivity mContext;
	private TextView tv_num;
	private Button btn_quit;
	private ImageButton ibtn_up,ibtn_down;
	private GridView gv;
	
	private int total_pageCount = 0;//兑换商品的总页数
	private int current_pageCount = 0;//兑换商品当前页数
	
	private ArrayList<RcodeBean> total_list = new ArrayList<RcodeBean>();//所有兑换商品的信息
	private ArrayList<RcodeBean> current_list = new ArrayList<RcodeBean>();//当前展示的商品信息
	private ExchangeDetailAdapter adapter;
	private StatusCheckRcode mStatusCheckRcode;
	public ExchangeDetailFragment(BaseFragmentActivity context,StatusCheckRcode statusCheckRcode){
		mContext = context;
		mStatusCheckRcode = statusCheckRcode;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_exchangedetail, null);
		initView(view);
		initListener();
		initData();
		return view;
	}
	void initView(View view){
		tv_num = (TextView) view.findViewById(R.id.fragment_exchangedetail_rcode);
		btn_quit = (Button) view.findViewById(R.id.fragment_exchangedetail_quit);
		ibtn_up = (ImageButton) view.findViewById(R.id.fragment_exchangedetail_up);
		ibtn_down = (ImageButton) view.findViewById(R.id.fragment_exchangedetail_down);
		gv = (GridView) view.findViewById(R.id.fragment_exchangedetail_gv);
	}
	void initListener(){
		btn_quit.setOnClickListener(this);
		ibtn_up.setOnClickListener(this);
		ibtn_down.setOnClickListener(this);
		gv.setOnItemClickListener(getGvListener());
	}
	void initData(){
		mMachineControl.startSound(14);
		mContext.setmExchangeDetailFragment(this);
		mContext.exchangeDetailTimerTask(ExchangeDetailHandler.EXCHANGEDETAIL_2_EXCHANGE, ExchangeDetailHandler.EXCHANGEDETAIL_2_SHOP_TIME);
		tv_num.setText(mStatusCheckRcode.getOid() + "");
		adapter = new ExchangeDetailAdapter(mContext, current_list);
		gv.setAdapter(adapter);
		initAdapterData();
	}
	void initAdapterData(){
		mStatusCheckRcode = mMachineControl.selectStatusCheckRcode(mStatusCheckRcode);
		total_list.clear();
		total_list.addAll(mStatusCheckRcode.getList());
		if (total_list.size() % 6 == 0)
			total_pageCount = total_list.size() / 6;
		else
			total_pageCount = total_list.size() / 6 + 1;
		refreshAdapter();
	}
	void refreshAdapter(){
		current_list.clear();
		for (int i = current_pageCount * 6; i < (current_pageCount + 1) * 6; i++) {
			if (i < total_list.size()) {
				current_list.add(total_list.get(i));
			}
		}
		adapter.notifyDataSetChanged();
	}
	void post_quit_convert(){
		mMachineControl.post_quit_convert(mStatusCheckRcode.getRid());
		mContext.setExchangeDetailFinishTimer(ExchangeDetailHandler.RESEET_EXCHANGEDETAIL_TIME);
	}
	
	@Override
	public void onDetach() {
		mContext.setmExchangeDetailFragment(null);
		mContext.cancleExchangeDetailTask();
		post_quit_convert();
		super.onDetach();
	}
	@Override
	public void onClick(View arg0) {
		if(Utils.isFastDoubleClick())
			return;
		mContext.setExchangeFinishTimer(ExchangeDetailHandler.RESEET_EXCHANGEDETAIL_TIME);
		switch(arg0.getId()){
		case R.id.fragment_exchangedetail_quit:
			post_quit_convert();
			break;
		case R.id.fragment_exchangedetail_up:
			if (current_pageCount > 0) {
				current_pageCount--;
				refreshAdapter();
			}
			break;
		case R.id.fragment_exchangedetail_down:
			if (current_pageCount < total_pageCount - 1) {
				current_pageCount++;
				refreshAdapter();
			}
			break;
		}
		
	}
	// 点击商品出货
	GridView.OnItemClickListener getGvListener() {
		return new GridView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				if(CommonUtils.isFastDoubleClick())
					return;
				if (!mContext.isAvmRunning() || mContext.isDoorOpen()) {
					return;
				}
				RcodeBean rcodeBean = current_list.get(arg2);
				int amounts = Integer.parseInt(rcodeBean.getAmounts());
				Integer storeNum = rcodeBean.getStoreNum();
				String way_id = mMachineControl.selectWay_id8g_codeRandom(rcodeBean.getG_code());
				rcodeBean.setW_code(way_id);
				CommonUtils.showLog(CommonUtils.EXCHANGE_TAG,"amounts = " + amounts + ";storeNum = " + storeNum + ";way_id = " + rcodeBean.getW_code());
				boolean checkRoad = mMachineControl.checkRoad(GoodsWayUtil.getW_code(way_id));
				int rest_storeNum = 0;
				if(amounts <= 0 ){
					CommonUtils.showLog(CommonUtils.EXCHANGE_TAG,"amounts < 0");
					CommonUtils.sendMessage(mContext.mViewHandler,BaseViewHandler.GOODS_RCODE_OVER, "");
				}else if(storeNum <= rest_storeNum || !checkRoad){
					CommonUtils.sendMessage(mContext.mViewHandler,BaseViewHandler.GOODS_SOLD_SELL_OUT, "");
				}else{
					rcodeBean.setAmounts(Integer.parseInt(rcodeBean.getAmounts()) - 1 + "");
					mMachineControl.startExchangePay(rcodeBean,mStatusCheckRcode.getRid() + "",mStatusCheckRcode.getOid());
				}
			}
		};
	}
	public void refresh(){
		current_pageCount = 0;
		mContext.setExchangeDetailFinishTimer(ExchangeDetailHandler.EXCHANGEDETAIL_2_SHOP_TIME);
		initAdapterData();
	}
}
