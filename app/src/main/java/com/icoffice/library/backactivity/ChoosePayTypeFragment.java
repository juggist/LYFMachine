package com.icoffice.library.backactivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import CofficeServer.PayType;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.icoffice.library.bean.db.PayTypeBean;
import com.icoffice.library.utils.CommonUtils;
import com.icofficeapp.R;

/**
 * 选择支付方式
 * @author lufeisong
 *
 */
@SuppressLint("ValidFragment")
public class ChoosePayTypeFragment extends Fragment implements OnClickListener{
	private BaseFragmentActivity mContext;
	private CheckBox cb_cash,cb_iCoffice,cb_wechat,cb_alipay,cb_eyypay;
	private Button btn_commit;
	
	private ArrayList<PayTypeBean> payTypeBean_list = new ArrayList<PayTypeBean>();//所有支付方式集合
	private HashMap<Integer,Integer> payTypeBean_map = new LinkedHashMap<Integer, Integer>();//key:支付方式 key:支付方式状态
	public ChoosePayTypeFragment(BaseFragmentActivity context){
		mContext = context;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.library_choosepaytype, null);
		initView(view);
		initListener();
		initData();
		return view;
	}
	void initView(View view){
		cb_cash = (CheckBox) view.findViewById(R.id.library_choosepaytype_cash);
		cb_iCoffice = (CheckBox) view.findViewById(R.id.library_choosepaytype_icoffice);
		cb_wechat = (CheckBox) view.findViewById(R.id.library_choosepaytype_wechat);
		cb_alipay = (CheckBox) view.findViewById(R.id.library_choosepaytype_alipay);
		btn_commit = (Button) view.findViewById(R.id.library_choosepaytype_commit);
		cb_eyypay = (CheckBox) view.findViewById(R.id.library_choosepaytype_eyypay);
	}
	void initListener(){
		cb_cash.setOnCheckedChangeListener(listener);
		cb_iCoffice.setOnCheckedChangeListener(listener);
		cb_wechat.setOnCheckedChangeListener(listener);
		cb_alipay.setOnCheckedChangeListener(listener);
		cb_eyypay.setOnCheckedChangeListener(listener);
		btn_commit.setOnClickListener(this);
	}
	void initData(){
		payTypeBean_list = mContext.mBaseMachineControl.selectPayTypeBean();
		for(int i = 0;i < payTypeBean_list.size();i++){
			PayTypeBean payTypeBean = payTypeBean_list.get(i);
			payTypeBean_map.put(payTypeBean.getPayType(), payTypeBean.getPayStatus());
			if( payTypeBean.getPayType() == PayType.cash.value() ){
				if(payTypeBean.getPayStatus() == 1)
					cb_cash.setChecked(true);
				else
					cb_cash.setChecked(false);
			}
			
			if( payTypeBean.getPayType() == PayType.ali.value() ){
				if(payTypeBean.getPayStatus() == 1)
					cb_alipay.setChecked(true);
				else
					cb_alipay.setChecked(false);

			}
			if( payTypeBean.getPayType() == PayType.card.value()){
				if(	payTypeBean.getPayStatus() == 1)
					cb_iCoffice.setChecked(true);
				else
					cb_iCoffice.setChecked(false);
			}
				
			if( payTypeBean.getPayType() == PayType.wechat.value() ){
				if(payTypeBean.getPayStatus() == 1)
					cb_wechat.setChecked(true);
				else
					cb_wechat.setChecked(false);
				
			}
				
			if( payTypeBean.getPayType() == PayType.yeepay.value() ){
				if( payTypeBean.getPayStatus() == 1)
					cb_eyypay.setChecked(true);
				else
					cb_eyypay.setChecked(false);
			}
		}
	}
	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.library_choosepaytype_commit:
			commit();
			break;
		}
	}
	private OnCheckedChangeListener listener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			switch(buttonView.getId()){
			case R.id.library_choosepaytype_cash:
				
				if(cb_cash.isSelected())
					cb_cash.setSelected(false);
				else
					cb_cash.setSelected(true);
				setPayTypeStatus(PayType.cash.value(), cb_cash.isChecked());
				break;
			case R.id.library_choosepaytype_icoffice:
				if(cb_iCoffice.isSelected())
					cb_iCoffice.setSelected(false);
				else
					cb_iCoffice.setSelected(true);
				setPayTypeStatus(PayType.card.value(), cb_iCoffice.isChecked());
				break;
			case R.id.library_choosepaytype_wechat:
				if(cb_wechat.isSelected())
					cb_wechat.setSelected(false);
				else
					cb_wechat.setSelected(true);
				setPayTypeStatus(PayType.wechat.value(), cb_wechat.isChecked());
				break;
			case R.id.library_choosepaytype_alipay:
				if(cb_alipay.isSelected())
					cb_alipay.setSelected(false);
				else
					cb_alipay.setSelected(true);
				setPayTypeStatus(PayType.ali.value(), cb_alipay.isChecked());
				break;
			case R.id.library_choosepaytype_eyypay:
				if(cb_eyypay.isSelected())
					cb_eyypay.setSelected(false);
				else
					cb_eyypay.setSelected(true);
				setPayTypeStatus(PayType.yeepay.value(), cb_eyypay.isChecked());
				break;
			}
		}
		
	};
	void setPayTypeStatus(Integer key,boolean status){
		if(payTypeBean_map.containsKey(key))
			payTypeBean_map.remove(key);
		payTypeBean_map.put(key, status ? 1 : 0);
	}
	ArrayList<PayTypeBean> getPayTypeBean(){
		ArrayList<PayTypeBean> payTypeBean_list = new ArrayList<PayTypeBean>();
		Iterator<Entry<Integer, Integer>> obj = payTypeBean_map.entrySet().iterator();
		while(obj.hasNext()){
			PayTypeBean payTypeBean = new PayTypeBean();
			Entry<Integer, Integer> item = obj.next();
			payTypeBean.setPayStatus(item.getValue());
			payTypeBean.setPayType(item.getKey());
			payTypeBean_list.add(payTypeBean);
		}
		return payTypeBean_list;
	}
	void commit(){
		ArrayList<PayTypeBean> payTypeBean_list = getPayTypeBean();
		for(int i = 0;i < payTypeBean_list.size();i++){
			mContext.mBaseMachineControl.insertPayTypeBean(payTypeBean_list.get(i));
		}
		mContext.mBaseMachineControl.setPayTypeBean(payTypeBean_list);
		mContext.commentToast.show("修改支付方式成功");
//		CommonUtils.showToast(mContext, "修改支付方式成功");
	}
}
