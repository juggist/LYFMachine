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
import android.widget.TextView;

import com.icoffice.library.bean.network.StatusUserBean;
import com.icoffice.library.callback.UserEnterCallBack;
import com.icoffice.library.handler.BaseViewHandler;
import com.icoffice.library.utils.ApkUtil;
import com.icoffice.library.utils.CommonUtils;
import com.icofficeapp.R;

@SuppressLint("ValidFragment")
public class UserEnterFragment extends Fragment implements OnClickListener,UserEnterCallBack{
	private EditText et_account,et_pwd;
	private Button btn_commit;
	private BaseFragmentActivity mContext;
	public UserEnterFragment(BaseFragmentActivity mContext){
		this.mContext = mContext;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.library_userenter, null);
		initView(view);
		initListener();
		initData();
		return view;
	}
	void initView(View view){
		et_account = (EditText) view.findViewById(R.id.library_userenter_account);
		et_pwd = (EditText) view.findViewById(R.id.library_userenter_pwd);
		btn_commit = (Button) view.findViewById(R.id.library_userenter_commit);
	}
	void initListener(){
		btn_commit.setOnClickListener(this);
	}
	void initData(){
		mContext.mBaseMachineControl.setUserEnterCallBack(this);
	}
	@Override
	public void onClick(View arg0) {
		if(CommonUtils.isFastDoubleClick())
			return;
		switch(arg0.getId()){
		case R.id.library_userenter_commit:
			String str_account = et_account.getText().toString();
			String str_pwd = et_pwd.getText().toString();
			if(str_account == null ||str_account.equals("") || str_pwd == null || str_pwd.equals("")){
				mContext.commentToast.show("请输入完整信息");
			} else {
				mContext.showDialog(BaseViewHandler.Title, "");
				mContext.mNetWork.post_userEnter(str_account, str_pwd,mContext.mBaseMachineControl.getFactoryCode());
			}
			CommonUtils.hideKeyBoard(mContext, arg0);
			break;
		}
	}


	@Override
	public void fail(String msg) {
		mContext.dismissDialog();
		mContext.commentToast.show(msg);
	}

	@Override
	public void success(StatusUserBean statusUserBean) {
		mContext.dismissDialog();
		((BackstageActivity) mContext).showWayView();
		mContext.commentToast.show(statusUserBean.getMsg());
		mContext.setRecoverAssistWayBean_map(statusUserBean.getWayBean_map());
	}
}
