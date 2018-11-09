package com.icofficeapp.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.icoffice.library.bean.db.AdStatusBean;
import com.icoffice.library.utils.DisplayUtil;
import com.icofficeapp.R;
import com.icofficeapp.adapter.ActivityAdapter;
import com.icofficeapp.handler.ActivityHandler;
/**
 * 活动适配器
 * @author lufeisong
 *
 */
@SuppressLint("ValidFragment")
public class ActivityFragment extends BaseFragment implements OnClickListener{
	private BaseFragmentActivity mContext;
	private ListView lv;
	private ActivityAdapter adapter;
	private ImageButton iBtn_up,iBtn_down;
	
	private int lv_height = 0;
	
	private ArrayList<AdStatusBean> pic_list = new ArrayList<AdStatusBean>();
	public ActivityFragment(BaseFragmentActivity context){
		mContext = context;
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_activity, null);
		initView(view);
		initListener();
		initData();
		return view;
	}
	void initView(View view){
		lv = (ListView) view.findViewById(R.id.fragment_activity_lv);
		iBtn_up = (ImageButton) view.findViewById(R.id.fragment_activity_up);
		iBtn_down = (ImageButton) view.findViewById(R.id.fragment_activity_down);
	}
	void initListener(){
		iBtn_up.setOnClickListener(this);
		iBtn_down.setOnClickListener(this);
	}
	void initData(){
		mContext.setmActivityFragment(this);
		mContext.activityTimerTask(ActivityHandler.ACTIVITY_2_SHOP, ActivityHandler.ACTIVITY_2_SHOP_TIME);
		
		adapter = new ActivityAdapter(getActivity(), pic_list);
		lv.setAdapter(adapter);
		ArrayList<AdStatusBean> pic_list = mMachineControl.getSelectActivityAdStatusBean();
		this.pic_list.addAll(pic_list);
		adapter.notifyDataSetChanged();
		lv_height = 178 * pic_list.size();
	}
	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.fragment_activity_up://上拉more
			lv.smoothScrollToPositionFromTop(0, lv_height, 1000);
			break;
		case R.id.fragment_activity_down://下拉last
			lv.smoothScrollToPositionFromTop(lv_height, 0, 1000);
			break;
		}
	}
	@Override
	public void onStart() {
		
		super.onStart();
	}
	
	@Override
	public void onDestroy() {
		mContext.setmActivityFragment(null);
		super.onDestroy();
	}
	@Override
	public void onDetach() {
		mContext.cancleActivityTask();
		super.onDetach();
	}

}
