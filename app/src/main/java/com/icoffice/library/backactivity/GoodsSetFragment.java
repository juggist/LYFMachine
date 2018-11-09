package com.icoffice.library.backactivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import com.icoffice.library.backadapter.GoodsStatusAdapter;
import com.icoffice.library.bean.GoodsStateBean;
import com.icoffice.library.bean.network.StatusTemplateBean;
import com.icoffice.library.callback.BackStageHandler;
import com.icoffice.library.callback.GoodsStatusCallBack;
import com.icoffice.library.callback.TemplateCallBack;
import com.icoffice.library.handler.BaseViewHandler;
import com.icoffice.library.moudle.control.BaseMachineControl;
import com.icoffice.library.utils.CommonUtils;
import com.icoffice.library.utils.FileUtil;
import com.icoffice.library.utils.download.DownloadService;
import com.icoffice.library.utils.download.DownloadService.DownloadStateListener;
import com.icofficeapp.R;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class GoodsSetFragment extends Fragment implements OnClickListener,GoodsStatusCallBack,TemplateCallBack{
	private BaseMachineControl mBaseMachineControl;
	private BaseFragmentActivity mContext;
	private Button btn_commit,btn_getTemplate;
	private GridView gv;
	private GoodsStatusAdapter adapter;
	private ArrayList<GoodsStateBean> list = new ArrayList<GoodsStateBean>();
	private BackStageHandler mGoodsSetFragmentHandler = new BackStageHandler(){

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case BackStageHandler.goodsSetFragment_init:
				list = mContext.mDbHelper.getGoodsStateBean();
				adapter = new GoodsStatusAdapter(mContext,list,mBaseMachineControl);
				gv.setAdapter(adapter);
				break;
			}
			
			super.handleMessage(msg);
		}
	};
	public GoodsSetFragment(BaseFragmentActivity mContext,BaseMachineControl mBaseMachineControl){
		this.mBaseMachineControl = mBaseMachineControl;
		this.mContext = mContext;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.library_goods_set, null);
		initView(view);
		initListener();
		initData();
		
		return view;
	}
	void initView(View view){
		btn_commit = (Button) view.findViewById(R.id.goods_commit);
		btn_getTemplate = (Button) view.findViewById(R.id.goods_template);
		gv = (GridView) view.findViewById(R.id.goods_gv);
	}
	void initListener(){
		btn_commit.setOnClickListener(this);
		btn_getTemplate.setOnClickListener(this);
		gv.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				CommonUtils.hideKeyBoard(mContext, arg0);
				return false;
			}
		});
	}
	void initData(){
		mBaseMachineControl.setGoodsStatusCallBack(this);
		mBaseMachineControl.setTemplateCallBack(this);
		CommonUtils.sendMessage(mGoodsSetFragmentHandler, BackStageHandler.goodsSetFragment_init, "");
	}
	@Override
	public void onClick(View arg0) {
		if(CommonUtils.isFastDoubleClick())
			return;
		switch(arg0.getId()){
		case R.id.goods_commit:
			btn_commit.setClickable(false);
			CommonUtils.hideKeyBoard(mContext, arg0);
			mContext.showDialog(BaseViewHandler.Title, "");
//			gv.smoothScrollToPositionFromTop(0, -gv.getHeight() * 10, 3000);
//			new Handler().postDelayed(new Runnable() {
//				
//				@Override
//				public void run() {
					adapter.commitStart(mContext.mNetWork);
					btn_commit.setClickable(true);
//				}
//			}, 3100);
			
			break;
		case R.id.goods_template:
			CommonUtils.hideKeyBoard(mContext, arg0);
			if(mBaseMachineControl.isTemplateGoodsStatusBeanClear()){
				btn_getTemplate.setClickable(false);
				mContext.showDialog(BaseViewHandler.Title, "");
				mBaseMachineControl.post_template();
				btn_getTemplate.setClickable(true);
			}else{
				mContext.commentToast.show("有残留数据，不能获取模板");
			}
			break;
		}
		
	}
	@Override
	public void success(String msg) {
		mContext.dismissDialog();
		adapter.commitSuccess(mContext.mDbHelper);
		mContext.commentToast.show(msg);
		DownloadService downloadService = new DownloadService(FileUtil.ROOT_PATH,FileUtil.SECOND_IMAGE_PATH, adapter.imgUrls, new DownloadStateListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFinish() {
				CommonUtils.showLog(CommonUtils.GOODS_TAG, "图片下载成功");
				
			}
			
			@Override
			public void onFailed(int code, Exception e, String errorMsg) {
				// TODO Auto-generated method stub
				
			}
		});
		downloadService.startDownload();
	}
	@Override
	public void fail(String msg) {
		mContext.dismissDialog();
		mContext.commentToast.show(msg);
	}
	@Override
	public void TemplateSuccess(StatusTemplateBean statusTemplateBean) {
		mContext.dismissDialog();
		mContext.commentToast.show(statusTemplateBean.getMsg());
		if(statusTemplateBean.getStatus().equals("1")){
			for(int i = 0;i < list.size();i++){
				for(int j = 0;j < statusTemplateBean.getG_list().size();j++){
					if(list.get(i).getG_code().equals(statusTemplateBean.getG_list().get(j))){
						CommonUtils.showLog(CommonUtils.TEMPLATE_TAG,"Goods:g_code = " + list.get(i).getG_code());
						list.get(i).setSold_status(1);
						break;
					}
				}
			}
			adapter.notifyDataSetChanged();
		}
		CommonUtils.showLog(CommonUtils.TEMPLATE_TAG,"Goods success");
	}
	@Override
	public void TemplateFail(StatusTemplateBean statusTemplateBean) {
		mContext.dismissDialog();
		mContext.commentToast.show(statusTemplateBean.getMsg());
		CommonUtils.showLog(CommonUtils.TEMPLATE_TAG,"Goods fail");
	}
	@Override
	public void TemplateConnectFail(String e) {
		mContext.dismissDialog();
		mContext.commentToast.show(e);
	}

}
