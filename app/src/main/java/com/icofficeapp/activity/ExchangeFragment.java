package com.icofficeapp.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.icofficeapp.R;
import com.icofficeapp.handler.ExchangeHandler;
import com.icoffice.library.configs.Constant;
import com.icoffice.library.utils.CommonUtils;
import com.icoffice.library.widget.CommentDialogUtils;
/**
 * 兑换界面
 * @author lufeisong
 *
 */
@SuppressLint("ValidFragment")
public class ExchangeFragment extends BaseChildFragment implements OnClickListener{
	private BaseFragmentActivity mContext;
	private TextView tv_number;
	private ImageButton iBtn_01, iBtn_02, iBtn_03, iBtn_04, iBtn_05, iBtn_06,
			iBtn_07, iBtn_08, iBtn_09, iBtn_00, iBtn_delete, iBtn_clear;
	private ImageButton iBtn_commit;
	public ExchangeFragment(BaseFragmentActivity context) {
		mContext = context;
	}
	private StringBuffer buffer;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_exchange, null);
		initView(view);
		initListener();
		initData();
		return view;
	}
	void initView(View view){
		tv_number = (TextView) view.findViewById(R.id.fragment_exchange_et);
		iBtn_01 = (ImageButton) view.findViewById(R.id.keyboard_ibtn_01);
		iBtn_02 = (ImageButton) view.findViewById(R.id.keyboard_ibtn_02);
		iBtn_03 = (ImageButton) view.findViewById(R.id.keyboard_ibtn_03);
		iBtn_04 = (ImageButton) view.findViewById(R.id.keyboard_ibtn_04);
		iBtn_05 = (ImageButton) view.findViewById(R.id.keyboard_ibtn_05);
		iBtn_06 = (ImageButton) view.findViewById(R.id.keyboard_ibtn_06);
		iBtn_07 = (ImageButton) view.findViewById(R.id.keyboard_ibtn_07);
		iBtn_08 = (ImageButton) view.findViewById(R.id.keyboard_ibtn_08);
		iBtn_09 = (ImageButton) view.findViewById(R.id.keyboard_ibtn_09);
		iBtn_00 = (ImageButton) view.findViewById(R.id.keyboard_ibtn_00);
		iBtn_delete = (ImageButton) view.findViewById(R.id.keyboard_ibtn_delete);
		iBtn_clear = (ImageButton) view.findViewById(R.id.keyboard_ibtn_clear);
		iBtn_commit = (ImageButton) view.findViewById(R.id.fragment_exchange_commit);
	}
	void initListener(){
		iBtn_01.setOnClickListener(this);
		iBtn_02.setOnClickListener(this);
		iBtn_03.setOnClickListener(this);
		iBtn_04.setOnClickListener(this);
		iBtn_05.setOnClickListener(this);
		iBtn_06.setOnClickListener(this);
		iBtn_07.setOnClickListener(this);
		iBtn_08.setOnClickListener(this);
		iBtn_09.setOnClickListener(this);
		iBtn_00.setOnClickListener(this);
		iBtn_delete.setOnClickListener(this);
		iBtn_clear.setOnClickListener(this);
		iBtn_commit.setOnClickListener(this);
		buffer = new StringBuffer();
	}
	void initData(){
		mMachineControl.startSound(13);
		mContext.setmExchangeFragment(this);
		mContext.exchangeTimerTask(ExchangeHandler.EXCHANGE_2_SHOP, ExchangeHandler.EXCHANGE_2_SHOP_TIME);
	}
	@Override
	public void onClick(View arg0) {
		mContext.setExchangeFinishTimer(ExchangeHandler.RESEET_TIME);
		if(buffer==null){
			buffer = new StringBuffer();
		}
		switch(arg0.getId()){
		case R.id.keyboard_ibtn_01:
			buffer.append("1");
			break;
		case R.id.keyboard_ibtn_02:
			buffer.append( "2");
			break;
		case R.id.keyboard_ibtn_03:
			buffer.append( "3");
			break;
		case R.id.keyboard_ibtn_04:
			buffer.append( "4");
			break;
		case R.id.keyboard_ibtn_05:
			buffer.append( "5");
			break;
		case R.id.keyboard_ibtn_06:
			buffer.append( "6");
			break;
		case R.id.keyboard_ibtn_07:
			buffer.append( "7");
			break;
		case R.id.keyboard_ibtn_08:
			buffer.append( "8");
			break;
		case R.id.keyboard_ibtn_09:
			buffer.append("9");
			break;
		case R.id.keyboard_ibtn_00:
			buffer.append( "0");
			break;
		case R.id.keyboard_ibtn_delete:
			if(buffer.length()==0){
				return;
			}
			buffer.deleteCharAt(buffer.length()-1);
			break;
		case R.id.keyboard_ibtn_clear:
			buffer.delete(0, buffer.length());
			break;
		case R.id.fragment_exchange_commit:
			if(!mContext.isAvmRunning())
				return;
			
			String rcode = tv_number.getText().toString();
			mContext.showExchangeDialog();
			if( rcode.length() == 7 && rcode.subSequence(0, 4).equals("0128") ){
				if(!Constant.VERSION_CONTROL){
					CommonUtils.showLog(CommonUtils.EXCHANGE_TAG, "本地兑换 兑换码: " + rcode);
					mMachineControl.test_post_chk_rcode("");
				}else {
					if(null!=mContext){
						mContext.dismissExchangeDialog();
					}
				}
				
			}
			else if(rcode.length() == 12) {
				CommonUtils.showLog(CommonUtils.EXCHANGE_TAG, "服务器出货 兑换码: " + rcode);
				String shamClient_order_no = 999999999999L * 2 - Long.parseLong(rcode) + "";
				String client_order_no = mMachineControl.selectClient_order_no(shamClient_order_no);
				String result = mMachineControl.serverOutGoods(client_order_no);
				mContext.dismissExchangeDialog();
				Bundle mBundle = new Bundle();
				mBundle.putString("result", result);
				CommonUtils.sendMessage(mContext.mViewHandler, CommentDialogUtils.DIALOG_SERVICE_OUTGOODS, mBundle);
			}
			else{
				CommonUtils.showLog(CommonUtils.EXCHANGE_TAG, "微商城兑换 兑换码: " + rcode);
				mMachineControl.post_chk_rcode(rcode);
//				CommonUtils.showToast(mContext, "验证码不匹配");
			}
			break;
		}
		if(buffer==null){
			tv_number.setText("");
		}else {
			tv_number.setText(buffer.toString());
		}
	}
	
	@Override
	public void onDestroy() {
		mContext.setmExchangeFragment(null);
		super.onDestroy();
	}

	@Override
	public void onStart() {
		
		super.onStart();
	}

	@Override
	public void onDetach() {
		mContext.cancleExchangeTask();
		super.onDetach();
	}
}
