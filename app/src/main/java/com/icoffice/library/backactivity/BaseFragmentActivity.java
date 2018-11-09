package com.icoffice.library.backactivity;

import android.app.Dialog;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Message;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.icoffice.library.activity.BaseActivity;
import com.icoffice.library.bean.UserBean;
import com.icoffice.library.bean.db.RecoverAssistWayBean;
import com.icoffice.library.callback.ConnectNetCallBack;
import com.icoffice.library.db.DbHelper;
import com.icoffice.library.handler.BaseViewHandler;
import com.icoffice.library.moudle.control.BaseMachineControl;
import com.icoffice.library.utils.CommonUtils;
import com.icoffice.library.utils.NetWork;
import com.icoffice.library.widget.CommentDialogUtils;
import com.icofficeapp.R;
import com.icofficeapp.widget.CommentToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class BaseFragmentActivity extends BaseActivity implements ConnectNetCallBack{
	protected BaseMachineControl mBaseMachineControl;
	public ImageLoader mImageLoader;
	public NetWork mNetWork;
	protected UserBean mUserBean;
	public DbHelper mDbHelper;
	private Dialog loading;// 显示dialog
	private TextView title_tv;//主题
	private TextView info_tv;//信息
	
	private HashMap<String, RecoverAssistWayBean> recoverAssistWayBean_map = new LinkedHashMap<String,RecoverAssistWayBean>();//服务器返回的货道信息 key_货道id value_货道信息
		
	public CommentToastUtils commentToast;
	private BaseViewHandler backViewHandler = new BaseViewHandler(){

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case BaseViewHandler.CONNECT_FAIL:
				dismissDialog();
				break;
			case BaseViewHandler.UpgradeSuccess:
				dismissDialog();
				com.icoffice.library.utils.CommonUtils.showToast(BaseFragmentActivity.this, Update_Success);
				break;
			case BaseViewHandler.UpgradeFail:
				dismissDialog();
				com.icoffice.library.utils.CommonUtils.showToast(BaseFragmentActivity.this, Update_Fail);
				break;
			case BaseViewHandler.UpgradeNothingToDo:
				dismissDialog();
				com.icoffice.library.utils.CommonUtils.showToast(BaseFragmentActivity.this, Update_Nothig);
				break;
			case BaseViewHandler.UpgradeDownloading:
				com.icoffice.library.utils.CommonUtils.showLog(CommonUtils.UPGRADE_TAG,"正在下载中。。。");
				title_tv.setText(Download_Title);
				info_tv.setText((Integer)msg.obj + "%");
				break;
			case BaseViewHandler.UpgradeInstalling:
				com.icoffice.library.utils.CommonUtils.showLog(CommonUtils.UPGRADE_TAG,"正在安装中 。。。");
				title_tv.setText(Install_Title);
				break;
			}
			super.handleMessage(msg);
		}
	};
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBaseMachineControl = mApplication.getBaseMachineControl();
		mImageLoader = ImageLoader.getInstance();
		mNetWork = mBaseMachineControl.getNetWork();
		mDbHelper = mBaseMachineControl.getDbHelper();
		mBaseMachineControl.setConnectNetCallBack(this);
		mBaseMachineControl.initBackViewHandler(backViewHandler);
		commentToast = CommentToastUtils.getInstance(this);
	}
	@Override
	public void connectFail(String msg) {
		dismissDialog();
		
	}
	@Override
	public void connectSuccess(int payType) {
		// TODO Auto-generated method stub
		
	}
	public void showDialog(String title,String info){
		if(loading != null)
			loading.dismiss();
		loading = new CommentDialogUtils(this, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, R.layout.library_dialog, R.style.MyDialog);
		title_tv = (TextView) loading.findViewById(R.id.dialog_update_title);
		info_tv = (TextView) loading.findViewById(R.id.dialog_update_info);
		title_tv.setText(title);
		info_tv.setText(info);
		loading.setCanceledOnTouchOutside(false);
		loading.show();
	}
	public void dismissDialog(){
		try{
			if(loading != null)
				loading.dismiss();
		}catch(Exception e){
			
		}
		
	}
	public void upgradeApk(){
		showDialog(BaseViewHandler.Download_Title, BaseViewHandler.Download_version);
		try {
			mBaseMachineControl.upgradeApk(backViewHandler);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}
	public HashMap<String, RecoverAssistWayBean> getRecoverAssistWayBean_map() {
		return recoverAssistWayBean_map;
	}
	public void setRecoverAssistWayBean_map(
			HashMap<String, RecoverAssistWayBean> recoverAssistWayBean_map) {
		this.recoverAssistWayBean_map = recoverAssistWayBean_map;
	}
	
}
