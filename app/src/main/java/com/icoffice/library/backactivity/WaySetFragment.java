package com.icoffice.library.backactivity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import com.icoffice.library.backadapter.WayStatusAdapter;
import com.icoffice.library.bean.db.WayBean;
import com.icoffice.library.bean.network.StatusTemplateBean;
import com.icoffice.library.callback.TemplateCallBack;
import com.icoffice.library.callback.WayStatusCallBack;
import com.icoffice.library.handler.BaseViewHandler;
import com.icoffice.library.moudle.control.BaseMachineControl;
import com.icoffice.library.utils.CommonUtils;
import com.icoffice.library.utils.GoodsWayUtil;
import com.icofficeapp.R;

@SuppressLint("ValidFragment")
public class WaySetFragment extends Fragment implements OnClickListener,WayStatusCallBack,TemplateCallBack{
	private BaseMachineControl mBaseMachineControl;
	private BaseFragmentActivity mContext;
	private Button btn_commit,btn_template;
	private GridView gv;
	protected HashMap<String, WayBean> wayMap = new HashMap<String, WayBean>();//货道map key：way_id value：货道信息
	private WayStatusAdapter adapter;
	public WaySetFragment(BaseFragmentActivity mContext,BaseMachineControl mBaseMachineControl){
		this.mContext = mContext;
		this.mBaseMachineControl = mBaseMachineControl;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.library_way_set, null);
		initView(view);
		initListener();
		initData();
		return view;
	}
	void initView(View view){
		btn_commit = (Button) view.findViewById(R.id.way_commit);
		btn_template = (Button) view.findViewById(R.id.way_template);
		gv = (GridView) view.findViewById(R.id.way_gv);
	}
	void initListener(){
		btn_commit.setOnClickListener(this);
		btn_template.setOnClickListener(this);
		gv.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				CommonUtils.hideKeyBoard(mContext, arg0);
				return false;
			}
		});
	}
	void initData(){
		mBaseMachineControl.setWayStatusCallBack(this);
		mBaseMachineControl.setTemplateCallBack(this);
		wayMap = mContext.mDbHelper.selectWayBean();
		adapter = new WayStatusAdapter(mContext, mBaseMachineControl.getWCode(), wayMap);
				
		gv.setAdapter(adapter);
	}

	@Override
	public void onClick(View arg0) {
		if(CommonUtils.isFastDoubleClick())
			return;
		switch(arg0.getId()){
		case R.id.way_commit:
			btn_commit.setClickable(false);
			mContext.showDialog(BaseViewHandler.Title, "");
			CommonUtils.hideKeyBoard(mContext, arg0);
//			gv.smoothScrollToPositionFromTop(0, -gv.getHeight() * 10, 3000);
//			new Handler().postDelayed(new Runnable() {
//				
//				@Override
//				public void run() {
					
					adapter.commitStart(mContext.mNetWork);
					btn_commit.setClickable(true);
//				}
//			}, 3100);
//			
			break;
		case R.id.way_template:
			CommonUtils.hideKeyBoard(mContext, arg0);
			if(mBaseMachineControl.isTemplateWayBeanClear()){
				btn_template.setClickable(false);
				mContext.showDialog(BaseViewHandler.Title, "");
				mBaseMachineControl.post_template();
				btn_template.setClickable(true);
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
			Iterator<Entry<String, WayBean>> obj = wayMap.entrySet().iterator();
			while(obj.hasNext()){
				Entry<String, WayBean> item = obj.next();
				for(int i = 0;i < statusTemplateBean.getMw_list().size();i++){
					WayBean wayBean = statusTemplateBean.getMw_list().get(i);
					if(item.getKey().equals(GoodsWayUtil.getWayId(wayBean.getWay_id()))){
						item.getValue().setStatus(1);
						item.getValue().setMaxNum(wayBean.getMaxNum());
						break;
					}
				}
				
			}
			adapter.notifyDataSetChanged();
		}
		CommonUtils.showLog(CommonUtils.TEMPLATE_TAG,"way success");
	}

	@Override
	public void TemplateFail(StatusTemplateBean statusTemplateBean) {
		mContext.dismissDialog();
		mContext.commentToast.show(statusTemplateBean.getMsg());
		CommonUtils.showLog(CommonUtils.TEMPLATE_TAG,"way fail");
	}

	@Override
	public void TemplateConnectFail(String e) {
		mContext.dismissDialog();
		mContext.commentToast.show(e);
	}
}
