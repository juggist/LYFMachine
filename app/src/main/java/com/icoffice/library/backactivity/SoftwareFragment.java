package com.icoffice.library.backactivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.icoffice.library.bean.network.StatusRegisterBean;
import com.icoffice.library.callback.RegisterMachineCallBack;
import com.icoffice.library.configs.Constant;
import com.icoffice.library.moudle.control.BaseMachineControl;
import com.icoffice.library.utils.ApkUtil;
import com.icoffice.library.utils.CommonUtils;
import com.icofficeapp.R;

@SuppressLint("ValidFragment")
public class SoftwareFragment extends Fragment implements OnClickListener,RegisterMachineCallBack{
	private BaseFragmentActivity mContext;
	private TextView tv_version,tv_mac,tv_code,tv_factory,tv_m_no;
	private Button btn_m_no;
	private BaseMachineControl mBaseMachineControl;
	public SoftwareFragment(BaseFragmentActivity mContext,BaseMachineControl mBaseMachineControl){
		this.mContext = mContext;
		this.mBaseMachineControl = mBaseMachineControl;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.library_software, null);
		initView(view);
		initListener();
		initData();
		return view;
	}
	void initView(View view){
		tv_version = (TextView) view.findViewById(R.id.library_update_version);
		tv_mac = (TextView) view.findViewById(R.id.library_machine_mac);
		tv_code = (TextView) view.findViewById(R.id.library_machine_code);
		tv_factory = (TextView) view.findViewById(R.id.library_factory_code);
		tv_m_no = (TextView) view.findViewById(R.id.library_m_no);
		btn_m_no = (Button) view.findViewById(R.id.library_software_btn_m_no);
		
	}
	void initListener(){
		btn_m_no.setOnClickListener(this);
	}
	void initData(){
		tv_version.setText("版本号为:" + ApkUtil.getVerCode(mContext));
		tv_mac.setText("机器mac:" + ApkUtil.getLocalMacAdress(mContext));
		tv_code.setText("机器code:" + mBaseMachineControl.selectMachineCode());
		tv_factory.setText("单片机编码为:" + mBaseMachineControl.getFactoryCode());
		mContext.mBaseMachineControl.setRegisterMachineCallBack(this);
	}
	@Override
	public void onClick(View arg0) {
		if(CommonUtils.isFastDoubleClick())
			return;
		switch(arg0.getId()){
		case R.id.library_software_btn_m_no:
			mContext.mNetWork.checkM_No(mContext.mBaseMachineControl.getMachineMac(),"",mContext.mBaseMachineControl.getFactoryCode(),Constant.mt_id.ordinal() + "");
			break;
		}
		
	}
	@Override
	public void success(StatusRegisterBean statusRegisterBean) {
		tv_m_no.setText("机器编码为:" + mBaseMachineControl.getM_No());
	}
	@Override
	public void fail(String msg) {
		// TODO Auto-generated method stub
		
	}
}
