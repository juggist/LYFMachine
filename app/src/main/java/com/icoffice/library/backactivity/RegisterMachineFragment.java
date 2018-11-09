package com.icoffice.library.backactivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.icoffice.library.bean.network.StatusRegisterBean;
import com.icoffice.library.callback.RegisterMachineCallBack;
import com.icoffice.library.configs.Constant;
import com.icoffice.library.handler.BaseViewHandler;
import com.icoffice.library.utils.CommonUtils;
import com.icofficeapp.R;

@SuppressLint("ValidFragment")
public class RegisterMachineFragment extends Fragment implements OnClickListener,RegisterMachineCallBack{
	private Button btn_register;
	private BaseFragmentActivity mContext;
	private EditText et_code;
	public RegisterMachineFragment(BaseFragmentActivity mContext){
		this.mContext = mContext;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.library_registermachine, null);
		initView(view);
		initListener();
		initData();
		return view;
	}
	void initView(View view){
		btn_register = (Button) view.findViewById(R.id.library_registermachine_btn);
		et_code = (EditText) view.findViewById(R.id.library_registermachine_et);
	}
	void initListener(){
		btn_register.setOnClickListener(this);
	}
	void initData(){
		mContext.mBaseMachineControl.setRegisterMachineCallBack(this);
	}
	@Override
	public void onClick(View arg0) {
		if(CommonUtils.isFastDoubleClick())
			return;
		switch(arg0.getId()){
		case R.id.library_registermachine_btn:
			String str = "";
			String str_et = et_code.getText().toString();
			if(!str_et.equals("") || str_et != null)
				str = str_et;
			mContext.showDialog(BaseViewHandler.Title, "");
			mContext.mNetWork.register(mContext.mBaseMachineControl.getMachineMac(),str,mContext.mBaseMachineControl.getFactoryCode(),Constant.mt_id.ordinal() + "");
			break;
		}
		
	}
	@Override
	public void success(StatusRegisterBean statusRegisterBean) {
		mContext.dismissDialog();
		mContext.commentToast.show(statusRegisterBean.getMsg() + ";机器ID为:" + statusRegisterBean.getM_code() + "\n机器编号为:" + statusRegisterBean.getM_no());
		((BackstageActivity)mContext).showUserEnterView();
	}
	@Override
	public void fail(String msg) {
		mContext.dismissDialog();
		mContext.commentToast.show(msg);
	}	
}
