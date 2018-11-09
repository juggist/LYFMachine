package com.icoffice.library.backadapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.icoffice.library.backactivity.BaseFragmentActivity;
import com.icoffice.library.bean.GoodsStateBean;
import com.icoffice.library.bean.db.RecoverAssistWayBean;
import com.icoffice.library.bean.db.RecoveryRecordsBean;
import com.icoffice.library.bean.db.WayBean;
import com.icoffice.library.db.DbHelper;
import com.icoffice.library.moudle.control.BaseMachineControl;
import com.icoffice.library.utils.CommonUtils;
import com.icoffice.library.utils.FileUtil;
import com.icoffice.library.utils.GoodsWayUtil;
import com.icoffice.library.utils.NetWork;
import com.icoffice.library.widget.MarqueeText;
import com.icofficeapp.R;
import com.nostra13.universalimageloader.core.ImageLoader;
/**
 *根据货道设置商品以及库存adapter
 * @author lufeisong
 *
 */
public class WayAndGoodsStatusAdapter extends LibraryBaseAdapter{
	protected BaseFragmentActivity mContext;
	protected BaseMachineControl mBaseMachineControl;
	private boolean statusNormal = true;//货道商品状态是否正常  false：不正常  true：正常
	
	protected ArrayList<String> way_idList = new ArrayList<String>();
	protected HashMap<String, WayBean> wayMap;
	protected HashMap<String, GoodsStateBean> goodsStateMap;
	protected WayAndGoodsInterface mWayAndGoodsInterface;
	protected ArrayList<ViewHolder> viewHolderList = new ArrayList<ViewHolder>();
	private HashMap<String, RecoverAssistWayBean> _server_wayBean_map = new LinkedHashMap<String, RecoverAssistWayBean>();//服务器返回的货道信息 key_货道id  value_货道信息
	private RecoveryRecordsBean mRecordsRecovery;
	public WayAndGoodsStatusAdapter(BaseFragmentActivity context,
			BaseMachineControl baseMachineControl,
			HashMap<Integer,ArrayList<String>> way_idMap,
			HashMap<String, WayBean> wayMap,
			HashMap<String, GoodsStateBean> goodsStateMap,
			WayAndGoodsInterface mWayAndGoodsInterface,HashMap<String, RecoverAssistWayBean> serVer_wayBean_map){
		this.mContext = context;
		mBaseMachineControl = baseMachineControl;
		this.wayMap = wayMap;
		this.goodsStateMap = goodsStateMap;
		this.mWayAndGoodsInterface = mWayAndGoodsInterface;
		_server_wayBean_map = serVer_wayBean_map;
		Iterator<Entry<Integer, ArrayList<String>>> obj = way_idMap.entrySet().iterator();
		while(obj.hasNext()){
			Entry<Integer, ArrayList<String>> item = obj.next();
			ArrayList<String> value = item.getValue();
			int valueSize = value.size();
//			int leftCount = valueSize % 3;
			for(int i = 0;i < valueSize;i++){
				way_idList.add(value.get(i));
			}
//			if(leftCount == 0)
//				size += valueSize;
//			else{
//				size += (valueSize + (3 - leftCount));
//				for(int j = 0;j < (3 - leftCount);j++){
//					way_idList.add(null);
//				}
//			}
			Collections.sort(way_idList);
		}
	}
	@Override
	public int getCount() {
		return way_idList.size();
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
			arg1 = LayoutInflater.from(mContext).inflate(R.layout.library_adapter_wayandgoods, null);
			vh.frist_view = (LinearLayout) arg1.findViewById(R.id.wayAndGoods_frist);
			vh.second_view = (LinearLayout) arg1.findViewById(R.id.wayAndGoods_second);
			vh.g_image_iv = (ImageView) arg1.findViewById(R.id.wayAndGoods_set_iv);
			vh.wayId_tv = (TextView) arg1.findViewById(R.id.wayAndGoods_id);
			vh.g_name_tv = (MarqueeText) arg1.findViewById(R.id.wayAndGoods_set_name);
			vh.g_price_tv = (MarqueeText) arg1.findViewById(R.id.wayAndGoods_set_g_code);
			vh.storeNum_tv = (MarqueeText) arg1.findViewById(R.id.wayAndGoods_set_storeNum);
			vh.server_storeNum_tv = (MarqueeText) arg1.findViewById(R.id.wayAndGoods_set_server_storeNum);
			vh.change_maxNum_btn = (Button) arg1.findViewById(R.id.wayAndGoods_set_commit);
			vh.sales_return_btn = (Button) arg1.findViewById(R.id.wayAndGoods_set_status);
			vh.add_goods_btn = (Button) arg1.findViewById(R.id.wayAndGoods_commit);
			vh.add_num_btn = (Button) arg1.findViewById(R.id.wayAndGoods_set_add);
			vh.reduce_num_btn = (Button) arg1.findViewById(R.id.wayAndGoods_set_reduce);
			arg1.setTag(vh);
		}else{
			vh = (ViewHolder) arg1.getTag();
		}
			vh.str_way_id = way_idList.get(arg0);
			if(vh.str_way_id != null){
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
				vh.wayId_tv.setText(GoodsWayUtil.getWayName(vh.str_way_id));
				vh.mWayBean = wayMap.get(vh.str_way_id);
				String g_code = vh.mWayBean.getG_code();
				if( g_code != null && !g_code.equals("")){
					if(goodsStateMap.containsKey(g_code)){
						vh.g_name_tv.setTextColor(Color.WHITE);
						GoodsStateBean mGoodsStateBean = goodsStateMap.get(g_code);
						vh.g_name_tv.setText(mGoodsStateBean.getG_name() + "");
						vh.g_price_tv.setText(CommonUtils.priceExchange(mGoodsStateBean.getPrice()) + "");
						displayImage(FileUtil.ROOT_PATH + mGoodsStateBean.getZ_image(), vh.g_image_iv);
					}else{
						vh.g_name_tv.setTextColor(Color.RED);
						vh.g_name_tv.setText("商品状态异常");
						statusNormal = false;
						
					}
					if(_server_wayBean_map.containsKey(GoodsWayUtil.getWayName(vh.str_way_id))){
						RecoverAssistWayBean recoverAssistWayBean = _server_wayBean_map.get(GoodsWayUtil.getWayName(vh.str_way_id));
						int server_storeNum = recoverAssistWayBean.getStoreNum();
						vh.store_differ = recoverAssistWayBean.getStore_differ();
						if(server_storeNum == vh.mWayBean.getStoreNum()){
							vh.server_storeNum_tv.setTextColor(Color.WHITE);
						}else{
							vh.server_storeNum_tv.setTextColor(Color.RED);
						}
						vh.server_storeNum_tv.setText(server_storeNum + "");
					}else{
						if(vh.mWayBean.getStoreNum() == 0)
							vh.server_storeNum_tv.setTextColor(Color.WHITE);
						else
							vh.server_storeNum_tv.setTextColor(Color.RED);
						
						vh.server_storeNum_tv.setText("0");
					}
					vh.storeNum_tv.setText(vh.mWayBean.getStoreNum() + "");
					vh.second_view.setVisibility(View.INVISIBLE);
					vh.frist_view.setVisibility(View.VISIBLE);
					vh.str_g_code_init = g_code;
					vh.str_g_number_init = vh.mWayBean.getStoreNum();
				}else{
					vh.second_view.setVisibility(View.VISIBLE);
					vh.frist_view.setVisibility(View.INVISIBLE);
					vh.str_g_code_init = "";
					vh.str_g_number_init = 0;
				}
				arg1.setVisibility(View.VISIBLE);
			}else{
				arg1.setVisibility(View.INVISIBLE);
			}
		
		
		vh.change_maxNum_btn.setOnClickListener(change_maxNumListener(vh));
		vh.sales_return_btn.setOnClickListener(sales_returnListener(vh));
		vh.add_goods_btn.setOnClickListener(sales_addListener(vh));
		vh.add_num_btn.setOnClickListener(add_numListener(vh));
		vh.reduce_num_btn.setOnClickListener(reduce_numListener(vh));
		return arg1;
	}
	public class ViewHolder{
		private LinearLayout frist_view;
		private LinearLayout second_view;
		private ImageView g_image_iv;
		private TextView wayId_tv;
		private MarqueeText g_name_tv;
		private MarqueeText g_price_tv;
		private MarqueeText storeNum_tv;//本地库存
		private MarqueeText server_storeNum_tv;//线上库存
		private Button change_maxNum_btn;//修改库存按钮
		private Button sales_return_btn;//退货按钮
		private Button add_goods_btn;//
		private Button add_num_btn;
		private Button reduce_num_btn;
		
		private String str_way_id;
		public WayBean mWayBean;
		private String str_g_code_init;//初始化的g_code
		private Integer str_g_number_init;//初始化g_code对应的库存
		private int store_differ = 0;//补货，减货，换货，库库存差
	
	}
	//提交库存修改按钮事件
	View.OnClickListener change_maxNumListener(final ViewHolder vh){
		return new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				int maxNum = vh.mWayBean.getMaxNum();
				vh.mWayBean.setStoreNum(maxNum);
				vh.storeNum_tv.setText(maxNum + "");
			}
		};
	}
	//退货按钮事件
	View.OnClickListener sales_returnListener(final ViewHolder vh){
		return new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				vh.second_view.setVisibility(View.VISIBLE);
				vh.frist_view.setVisibility(View.INVISIBLE);
				vh.mWayBean.setG_code("");
				vh.mWayBean.setStoreNum(0);
			}
		};
	}
	//添加商品按钮事件
	View.OnClickListener sales_addListener(final ViewHolder vh){
		return new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mWayAndGoodsInterface.showGoodsList(vh);
			}
		};
	}
	//递增数量
	View.OnClickListener add_numListener(final ViewHolder vh){
		return new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				int current_num = Integer.parseInt(vh.storeNum_tv.getText().toString());
				if(current_num >= vh.mWayBean.getMaxNum()){
					mContext.commentToast.show("达到最大库存量");
				}else{
					current_num++;
					vh.storeNum_tv.setText(current_num + "");
					vh.mWayBean.setStoreNum(current_num);
				}
			}
		};
		
	}
	//递减数量
	View.OnClickListener reduce_numListener(final ViewHolder vh){
		return new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				int current_num = Integer.parseInt(vh.storeNum_tv.getText().toString());
				if(current_num <= 0){
					//TODO
					mContext.commentToast.show("库存量为0，不能再次减少");
				}else{
					current_num--;
					vh.storeNum_tv.setText(current_num + "");
					vh.mWayBean.setStoreNum(current_num);
				}
			}
		};
		
	}
	//添加商品
	public void addGoods(ViewHolder vh,GoodsStateBean goodsStateBean){
		vh.mWayBean.setG_code(goodsStateBean.getG_code());
		vh.mWayBean.setStoreNum(vh.mWayBean.getMaxNum());
		vh.second_view.setVisibility(View.INVISIBLE);
		vh.frist_view.setVisibility(View.VISIBLE);
		vh.g_name_tv.setText(goodsStateBean.getG_name());
		vh.g_price_tv.setText(Float.parseFloat(goodsStateBean.getPrice()) / 100 + "");
		vh.storeNum_tv.setText(vh.mWayBean.getMaxNum() + "");
		displayImage(FileUtil.ROOT_PATH + goodsStateBean.getZ_image(), vh.g_image_iv);
	}
	public void addTemplateGoods(HashMap<String, WayBean> wayBean_map){
		Iterator<Entry<String, WayBean>> obj = wayBean_map.entrySet().iterator();
		while(obj.hasNext()){
			Entry<String, WayBean> item = obj.next();
			String way_id = item.getKey();
			String g_code = item.getValue().getG_code();
			if(g_code != null && !g_code.equals("")){
				for(int i = 0; i < viewHolderList.size();i++){
					ViewHolder vh = viewHolderList.get(i);
					if(way_id.equals(vh.str_way_id)){
						GoodsStateBean goodsStateBean = goodsStateMap.get(g_code);
						vh.mWayBean.setG_code(g_code);
						vh.mWayBean.setStoreNum(0);
						vh.second_view.setVisibility(View.INVISIBLE);
						vh.frist_view.setVisibility(View.VISIBLE);
						vh.g_name_tv.setText(goodsStateBean.getG_name());
						vh.g_price_tv.setText(Float.parseFloat(goodsStateBean.getPrice()) / 100 + "");
						vh.storeNum_tv.setText("0");
						displayImage(FileUtil.ROOT_PATH + goodsStateBean.getZ_image(), vh.g_image_iv);
						break;
					}
				}
			}
		}
	}
	//提交
	public void commit(NetWork mNetWork){
		if(!statusNormal){
			mContext.commentToast.show("商品状态异常，无法提交数据");
			mContext.dismissDialog();
			return;
		}
		StringBuffer records0 = new StringBuffer();//退货记录(可传)
		StringBuffer records1 = new StringBuffer();//补货记录(可传)
		StringBuffer records2 = new StringBuffer();//出货记录(可传)
		for(int i = 0;i < viewHolderList.size();i++){
			ViewHolder vh = viewHolderList.get(i);
			int change_num = 0;
			String change_g_code = "";
			int salesReturn_num = 0;
			String salesReturn_g_code = "";
			int salesAdd_num = 0;
			String salesAdd_g_code = "";
			if(vh.str_way_id != null){
				String wayName = GoodsWayUtil.getWayName(vh.str_way_id);
				if((vh.mWayBean.getG_code() == vh.str_g_code_init) && !vh.mWayBean.getG_code().equals("")){//补货
					change_num = vh.mWayBean.getStoreNum() - vh.str_g_number_init;
					change_g_code = vh.mWayBean.getG_code();
					if(change_num > 0 && change_g_code != null && !change_g_code.equals("")){//加货
						records1.append(wayName + "," + change_g_code + "," + change_num + "|");
					}else if(change_num < 0 && change_g_code != null && !change_g_code.equals("")){//减货
						records2.append(wayName + "," + change_g_code + "," + -change_num + "|");
					}
					vh.store_differ = change_num;
					CommonUtils.showLog(CommonUtils.WAYANDGOODS_TAG,"补货:way_id = " + GoodsWayUtil.getWayName(vh.str_way_id) + ";init_g_code = " + vh.str_g_code_init +  ";change_g_code = " + change_g_code + ";change_num = " + change_num + ";store_differ = " + vh.store_differ);
				}else if((vh.mWayBean.getG_code() != vh.str_g_code_init) && vh.str_g_code_init != "" && !vh.mWayBean.getG_code().equals("")){//从A换到B
					salesReturn_num = vh.str_g_number_init;
					salesReturn_g_code = vh.str_g_code_init;
					salesAdd_num = vh.mWayBean.getStoreNum();
					salesAdd_g_code = vh.mWayBean.getG_code();
					if(salesReturn_num >= 0 && salesReturn_g_code != null && !salesReturn_g_code.equals(""))
						records0.append(wayName + "," + salesReturn_g_code + "," + salesReturn_num + "|");
					if(salesAdd_num >= 0 && salesAdd_g_code != null && !salesAdd_g_code.equals(""))
						records1.append(wayName + "," + salesAdd_g_code + "," + salesAdd_num + "|");
					vh.store_differ = salesAdd_num - salesReturn_num;
					CommonUtils.showLog(CommonUtils.WAYANDGOODS_TAG,"A->B:way_id = "+ GoodsWayUtil.getWayName(vh.str_way_id) + ";init_g_code = " + vh.str_g_code_init + ";salesReturn_g_code = " + salesReturn_g_code + ";salesReturn_num = " + salesReturn_num + ";salesAdd_g_code = " + salesAdd_g_code + ";salesAdd_num = " + salesAdd_num + ";store_differ = " + vh.store_differ);
				}else if((vh.mWayBean.getG_code() != vh.str_g_code_init) && vh.str_g_code_init == "" && !vh.mWayBean.getG_code().equals("")){//从无换到A
					salesAdd_num = vh.mWayBean.getStoreNum();
					salesAdd_g_code = vh.mWayBean.getG_code();
					if(salesAdd_num >= 0 && salesAdd_g_code != null && !salesAdd_g_code.equals(""))
						records1.append(wayName + "," + salesAdd_g_code + "," + salesAdd_num + "|");
					vh.store_differ = salesAdd_num;
					CommonUtils.showLog(CommonUtils.WAYANDGOODS_TAG,"null->A:way_id = " + GoodsWayUtil.getWayName(vh.str_way_id) + ";init_g_code = " + vh.str_g_code_init + ";salesAdd_g_code = " + salesAdd_g_code + ";salesAdd_num = " + salesAdd_num + ";store_differ = " + vh.store_differ);
				}else if(vh.str_g_code_init != "" && !vh.str_g_code_init.equals("") && (vh.mWayBean.getG_code().equals("") || vh.mWayBean.getG_code() == null)){//从A换到无
					salesReturn_num = vh.str_g_number_init;
					salesReturn_g_code = vh.str_g_code_init;
					records0.append(wayName + "," + salesReturn_g_code + "," + salesReturn_num + "|");
					vh.store_differ = -salesReturn_num;
					CommonUtils.showLog(CommonUtils.WAYANDGOODS_TAG,"A->null:way_id = " + GoodsWayUtil.getWayName(vh.str_way_id) + ";init_g_code = " + vh.str_g_code_init + ";salesReturn_g_code = " + salesReturn_g_code + ";salesReturn_num = " + salesReturn_num + ";store_differ = " + vh.store_differ);
				}
			}
			
		}
		
		mRecordsRecovery = new RecoveryRecordsBean();
		mRecordsRecovery.setVersion(System.currentTimeMillis() + "");
		mRecordsRecovery.setRecords0(records0.toString());
		mRecordsRecovery.setRecords1(records1.toString());
		mRecordsRecovery.setRecords2(records2.toString());
		mNetWork.post_wayAndGoods_state(records0.toString(), records1.toString(), records2.toString(),System.currentTimeMillis() + "");
	}
	/**
	 * 提交数据
	 * 数据备份
	 * @param netWork
	 * @param recordsRecovery
	 */
	public void commitRecover(NetWork netWork,RecoveryRecordsBean recordsRecovery){
		if(!statusNormal){
			mContext.commentToast.show("商品状态异常，无法提交数据");
			mContext.dismissDialog();
			return;
		}
		mRecordsRecovery = recordsRecovery;
		netWork.post_wayAndGoods_state(recordsRecovery.getRecords0(), recordsRecovery.getRecords1(), recordsRecovery.getRecords2(),recordsRecovery.getVersion());
	}
	//提交成功
	public void commitSuccess(DbHelper mDbHelper){
		HashMap<String, RecoverAssistWayBean> recoverAssistWayBean_map = new LinkedHashMap<String, RecoverAssistWayBean>();
		mDbHelper.deleteWayBean();
		for(int i = 0;i < viewHolderList.size();i++){
			ViewHolder vh = viewHolderList.get(i);
			WayBean wayBean = vh.mWayBean;
			vh.str_g_code_init = wayBean.getG_code();
			vh.str_g_number_init = wayBean.getStoreNum();
			String str = vh.server_storeNum_tv.getText().toString();
			int service_storeNum = 0;
			if(CommonUtils.bIntgerNum(str)){
				service_storeNum = Integer.parseInt(str);
			}
			service_storeNum = service_storeNum + vh.store_differ;
			mDbHelper.updateOrInsertWayBean(wayBean);
			CommonUtils.showLog(CommonUtils.WAYANDGOODS_TAG,"way_id = " + wayBean.getWay_id() + ";service_storeNum = " + vh.server_storeNum_tv.getText().toString() + ";service_storeNum = " + service_storeNum);
			if(!CommonUtils.isEmpty(wayBean.getG_code())){
				RecoverAssistWayBean recoverAssistWayBean = new RecoverAssistWayBean();
				recoverAssistWayBean.setG_code(wayBean.getG_code());
				recoverAssistWayBean.setMaxNum(wayBean.getMaxNum());
				recoverAssistWayBean.setStatus(wayBean.getStatus());
				recoverAssistWayBean.setStore_differ(0);
				recoverAssistWayBean.setStoreNum(service_storeNum);
				recoverAssistWayBean.setWay_id(wayBean.getWay_id());
				recoverAssistWayBean_map.put(GoodsWayUtil.getWayName(wayBean.getWay_id()), recoverAssistWayBean);
			}
			vh.server_storeNum_tv.setTextColor(Color.WHITE);
			vh.server_storeNum_tv.setText(service_storeNum + "");
			vh.store_differ = 0;
		}
		mContext.setRecoverAssistWayBean_map(recoverAssistWayBean_map);
	}
	//链接失败 添加数据备份
	public void connectFail(BaseMachineControl mBaseMachineControl){
		ArrayList<WayBean> list = new ArrayList<WayBean>();
		HashMap<String, RecoverAssistWayBean> recoverAssistWayBean_map = new LinkedHashMap<String, RecoverAssistWayBean>();
		
		
		for(int i = 0;i < viewHolderList.size();i++){
			ViewHolder vh = viewHolderList.get(i);
			WayBean wayBean = vh.mWayBean;
			list.add(wayBean);
			
			if(!CommonUtils.isEmpty(wayBean.getG_code())){
				RecoverAssistWayBean recoverAssistWayBean = new RecoverAssistWayBean();
				String str = vh.server_storeNum_tv.getText().toString();
				int service_storeNum = 0;
				if(CommonUtils.bIntgerNum(str)){
					service_storeNum = Integer.parseInt(str);
				}
				recoverAssistWayBean.setG_code(wayBean.getG_code());
				recoverAssistWayBean.setMaxNum(wayBean.getMaxNum());
				recoverAssistWayBean.setStatus(wayBean.getStatus());
				recoverAssistWayBean.setStore_differ(vh.store_differ);
				recoverAssistWayBean.setStoreNum(service_storeNum);
				recoverAssistWayBean.setWay_id(wayBean.getWay_id());
				recoverAssistWayBean_map.put(GoodsWayUtil.getWayName(wayBean.getWay_id()), recoverAssistWayBean);
			}
		}
		mBaseMachineControl.insertWayBeanRecovery(list);
		mBaseMachineControl.insertRecordsRecovery(mRecordsRecovery);
		mContext.setRecoverAssistWayBean_map(recoverAssistWayBean_map);
	}
	//一键加满
	public void addFull(){
		for(int i = 0;i < viewHolderList.size();i++){
			ViewHolder vh = viewHolderList.get(i);
			if(vh.str_way_id != null){
				
				int maxNum = vh.mWayBean.getMaxNum();
				vh.storeNum_tv.invalidate();
				vh.storeNum_tv.setText(maxNum + "");
				vh.mWayBean.setStoreNum(maxNum);
			}
		}
	}
	public interface WayAndGoodsInterface{
		void showGoodsList(ViewHolder vh);
	}
}
