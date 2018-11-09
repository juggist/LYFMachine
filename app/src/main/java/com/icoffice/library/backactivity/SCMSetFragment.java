package com.icoffice.library.backactivity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.icoffice.library.bean.db.SoldWayBean;
import com.icoffice.library.callback.BackQuitCallBack;
import com.icoffice.library.handler.BaseViewHandler;
import com.icoffice.library.utils.CommonUtils;
import com.icoffice.library.utils.GoodsWayUtil;
import com.icofficeapp.R;
/**
 * 单片机设置
 * @author lufeisong
 *
 */
@SuppressLint("ValidFragment")
public class SCMSetFragment extends Fragment implements OnClickListener{
	private BaseFragmentActivity mContext;
	private Button btn_wayInfo_commit;
	private TextView tv_failInfo,tv_loading;
	
	private ArrayList<SoldWayBean> list = new ArrayList<SoldWayBean>();	//所有SoldWayBean信息集合
	private int max_size = 0;//最大货道数量
	
	private ArrayList<SoldWayBean> fail_list = new ArrayList<SoldWayBean>();//失败的SoldWayBean信息集合
	private SoldWayBean current_soldWayBean;//当前写入单片的SoldWayBean
	
	private BackQuitCallBack mBackQuitCallBack;
	
	private BaseViewHandler scmHandler = new BaseViewHandler(){

		@Override
		public void handleMessage(Message msg) {
			float finish = max_size - list.size() + 1;
			int percent = Math.round( (finish / max_size ) * 100);
			if(percent == 100)
				tv_loading.setText("设置完成");
			else
				tv_loading.setText("正在进行中:" + Math.round( (finish / max_size ) * 100) + "%");
			switch(msg.what){
			case BaseViewHandler.SetRoadSuccess:
				CommonUtils.showLog(CommonUtils.SCM_TAG,"success");
				break;
			case BaseViewHandler.SetRoadFail:
				fail_list.add(current_soldWayBean);
				CommonUtils.showLog(CommonUtils.SCM_TAG,"fail");
				break;
			}
			if(list.size() > 0){
				list.remove(0);
				if(list.size() > 0)
					insertSoldWayBean2SCM();
				else{
					mContext.dismissDialog();
					list.clear();
					if(fail_list.size() > 0){
						StringBuffer sb = new StringBuffer();
						sb.append("设置失败的货道:\n");
						for(int i = 0;i < fail_list.size();i++){
							CommonUtils.showLog(CommonUtils.SCM_TAG,"failList = " + fail_list.get(i).getWay_id());
							sb.append("货道id : " + GoodsWayUtil.getWayName(fail_list.get(i).getWay_id()) + ";补货量 : " + fail_list.get(i).getStoreNum() + ";单价 : " + fail_list.get(i).getPrice() + "\n" );
						}
						sb.append("请再次尝试。。。");
						tv_failInfo.setText(sb.toString() + "");
						list.addAll(fail_list);
					}else{
						mBackQuitCallBack.quitSwitch(true);
					}
				}
			}
			super.handleMessage(msg);
		}
		
	};
	public SCMSetFragment(BaseFragmentActivity context){
		mContext = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.library_scm_set, null);
		initView(view);
		initListener();
		initData();
		return view;
	}
	void initView(View view){
		btn_wayInfo_commit = (Button) view.findViewById(R.id.library_scm_set_wayinfo_commit);
		tv_failInfo = (TextView) view.findViewById(R.id.library_smc_set_tv);
		tv_loading = (TextView) view.findViewById(R.id.library_smc_setSuccess_tv);
//		tv_fail = (TextView) view.findViewById(R.id.library_smc_setFail_tv);
	}
	void initListener(){
		btn_wayInfo_commit.setOnClickListener(this);
	}
	void initData(){
		list = mContext.mBaseMachineControl.selectSoldWayBeanList();
	}
	@Override
	public void onClick(View arg0) {
		if(CommonUtils.isFastDoubleClick())
			return;
		switch(arg0.getId()){
		case R.id.library_scm_set_wayinfo_commit:
			if(list.size() > 0){
				tv_failInfo.setText("");
				tv_loading.setText("");
//				tv_fail.setText("");
				max_size = list.size();
				fail_list.clear();
				mContext.showDialog(BaseViewHandler.Title, "");
				insertSoldWayBean2SCM();
			}else{
				mContext.commentToast.show("货道已经设置完毕。。。");
//				CommonUtils.showToast(getActivity(), "货道已经设置完毕。。。");
			}
			break;
		}
		
	}
	void insertSoldWayBean2SCM(){
		current_soldWayBean  = list.get(0);
		mContext.mBaseMachineControl.setRoad(scmHandler,current_soldWayBean.getWay_id(), !current_soldWayBean.getG_code().isEmpty(), current_soldWayBean.getPrice(), current_soldWayBean.getStoreNum());
	}
	public void setBackQuitCallBack(BackQuitCallBack backQuitCallBack){
		mBackQuitCallBack = backQuitCallBack;
	}
}
