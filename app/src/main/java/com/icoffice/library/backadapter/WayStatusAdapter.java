package com.icoffice.library.backadapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.icoffice.library.backactivity.BaseFragmentActivity;
import com.icoffice.library.bean.db.WayBean;
import com.icoffice.library.db.DbHelper;
import com.icoffice.library.utils.GoodsWayUtil;
import com.icoffice.library.utils.NetWork;
import com.icoffice.library.widget.MarqueeText;
import com.icofficeapp.R;
import com.icoffice.library.utils.CommonUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class WayStatusAdapter extends LibraryBaseAdapter{
	protected BaseFragmentActivity mContext;
	protected HashMap<Integer,ArrayList<String>> way_idMap;
	protected ArrayList<String> way_idList = new ArrayList<String>();
	protected HashMap<String, WayBean> wayMap;
	protected ArrayList<ViewHolder> viewHolderList = new ArrayList<ViewHolder>();
	private int size = 0;
	private StringBuffer sb;
	public WayStatusAdapter(BaseFragmentActivity context,HashMap<Integer,ArrayList<String>> way_idMap,HashMap<String, WayBean> wayMap){
		this.mContext = context;
		this.way_idMap = way_idMap;
		this.wayMap = wayMap;
		Iterator<Entry<Integer, ArrayList<String>>> obj = way_idMap.entrySet().iterator();
		while(obj.hasNext()){
			Entry<Integer, ArrayList<String>> item = obj.next();
			ArrayList<String> value = item.getValue();
			int valueSize = value.size();
			int leftCount = valueSize % 3;
			for(int i = 0;i < valueSize;i++){
				way_idList.add(value.get(i));
				
			}
			if(leftCount == 0)
				size += valueSize;
			else{
				size += (valueSize + (3 - leftCount));
				for(int j = 0;j < (3 - leftCount);j++){
					way_idList.add(null);
				}
			}
		}
	}
	@Override
	public int getCount() {
		return size;
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressWarnings("unused")
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder vh = null;
		if(vh == null){
			vh = new ViewHolder();
			arg1 = LayoutInflater.from(mContext).inflate(R.layout.library_adapter_way_set, null);
			vh.wayId_tv = (TextView) arg1.findViewById(R.id.way_id);
			vh.maxNum_init_tv = (MarqueeText) arg1.findViewById(R.id.way_maxNum);
			vh.change_maxNum_et = (EditText) arg1.findViewById(R.id.way_et);
			vh.change_maxNum_btn = (Button) arg1.findViewById(R.id.way_commit);
			vh.rb_track = (CheckBox) arg1.findViewById(R.id.library_choose_track);
			arg1.setTag(vh);
		}else{
			vh = (ViewHolder) arg1.getTag();
		}
		
		vh.way_id = way_idList.get(arg0);
		if(vh.way_id != null){
			if(0 == viewHolderList.size()){
				viewHolderList.add(vh);
			}else if(1 == viewHolderList.size()){
				if(arg0 == 0){
					viewHolderList.remove(0);
					viewHolderList.add(vh);
				}else{
					if(arg0 >= (viewHolderList.size() - 1) && viewHolderList.size() < wayMap.size()){
						viewHolderList.add(vh);
					}
						
				}
			}else{
				if(arg0 != 0){
					if(arg0 >= (viewHolderList.size() - 1) && viewHolderList.size() < wayMap.size()){
						viewHolderList.add(vh);
					}
				}
			}
			vh.mWayBean = wayMap.get(vh.way_id);
			if(vh.mWayBean.getStatus() == 1){//货道正常
				vh.wayId_tv.setText(GoodsWayUtil.getWayName(vh.way_id));
				vh.maxNum_init_tv.setText(vh.mWayBean.getMaxNum() + "");
				if(vh.mWayBean.getW_type() == 1){
					vh.rb_track.setChecked(true);
				}else if(vh.mWayBean.getW_type() == 0){
					vh.rb_track.setChecked(false);
				}
			}else if(vh.mWayBean.getStatus() == 0){//货道异常
				//TODO 货道异常处理
				vh.wayId_tv.setText(GoodsWayUtil.getWayName(vh.way_id));
				vh.maxNum_init_tv.setText("货道异常");
			}
			arg1.setVisibility(View.VISIBLE);
		}else{
			arg1.setVisibility(View.INVISIBLE);
		}
		vh.change_maxNum_btn.setOnClickListener(change_maxNumListener(vh));
		vh.rb_track.setOnCheckedChangeListener(listener(vh.rb_track,vh.mWayBean));
		return arg1;
	}
	private class ViewHolder{
		
		private TextView wayId_tv;
		private MarqueeText maxNum_init_tv;
		private EditText change_maxNum_et;
		private Button change_maxNum_btn;
		private CheckBox rb_track;
		
		private String way_id;
		private WayBean mWayBean;
	}
	
	View.OnClickListener change_maxNumListener(final ViewHolder vh){
		return new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(CommonUtils.bPositiveIntgerNum(vh.change_maxNum_et.getEditableText().toString())){
					int maxStoreNum_finally = Integer.parseInt(vh.change_maxNum_et.getEditableText().toString());
					CommonUtils.showLog(CommonUtils.WAY_TAG,"vh.maxStoreNum_finally 01= " + maxStoreNum_finally);
					if(maxStoreNum_finally < vh.mWayBean.getStoreNum()){
//						CommonUtils.showToast(mContext, "在售库存大于最大库存量，无法修改");
						mContext.commentToast.show("在售库存大于最大库存量，无法修改");
					}else{
						vh.maxNum_init_tv.setText(maxStoreNum_finally + "");
						vh.change_maxNum_et.setText("");
						vh.mWayBean.setMaxNum(maxStoreNum_finally);
					}
				}else{
//					CommonUtils.showToast(mContext, "请输入整数");
					mContext.commentToast.show("请输入整数");
				}
				
			}
		};
	}
	public void commitStart(NetWork mNetWork){
		sb = new StringBuffer();
		for(int i = 0;i < viewHolderList.size();i++){
			ViewHolder vh = viewHolderList.get(i);
			if(vh.way_id != null){
				if(vh.mWayBean.getStatus() == 1)
					sb.append(GoodsWayUtil.getWayName(vh.way_id) + "," + vh.mWayBean.getMaxNum() + "|");
			}
		}
		mNetWork.post_way_state(sb.toString());
		CommonUtils.showLog(CommonUtils.WAY_TAG,"sb = " + sb.toString());
	}
	public void commitSuccess(DbHelper mDbHelper){
		for(int i = 0;i < viewHolderList.size();i++){
			ViewHolder vh = viewHolderList.get(i);
			if(vh.way_id != null){
				if(vh.mWayBean.getStatus() == 1){
					mDbHelper.updateWayBean(vh.mWayBean);
				}
			}
		}
		sb.delete(0,sb.length()-1);
	}
	OnCheckedChangeListener listener(final CheckBox rb_track,final WayBean mWayBean){
		return new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if(buttonView.getId() == rb_track.getId()){
					if(isChecked)
						mWayBean.setW_type(1);
					else
						mWayBean.setW_type(0);
				}
			}
		};
	}
}
