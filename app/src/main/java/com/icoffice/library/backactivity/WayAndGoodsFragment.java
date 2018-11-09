package com.icoffice.library.backactivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.icoffice.library.backadapter.WayAndGoodsStatusAdapter;
import com.icoffice.library.backadapter.WayAndGoodsStatusAdapter.ViewHolder;
import com.icoffice.library.backadapter.WayAndGoodsStatusAdapter.WayAndGoodsInterface;
import com.icoffice.library.bean.GoodsStateBean;
import com.icoffice.library.bean.db.RecoveryRecordsBean;
import com.icoffice.library.bean.db.WayBean;
import com.icoffice.library.bean.db.RecoveryWayBean;
import com.icoffice.library.bean.network.StatusTemplateBean;
import com.icoffice.library.callback.TemplateCallBack;
import com.icoffice.library.callback.WayAndGoodsCallBack;
import com.icoffice.library.handler.BaseViewHandler;
import com.icoffice.library.moudle.control.BaseMachineControl;
import com.icoffice.library.sortlist.CharacterParser;
import com.icoffice.library.sortlist.ClearEditText;
import com.icoffice.library.sortlist.PinyinComparator;
import com.icoffice.library.sortlist.SideBar;
import com.icoffice.library.sortlist.SideBar.OnTouchingLetterChangedListener;
import com.icoffice.library.sortlist.SortAdapter;
import com.icoffice.library.sortlist.SortModel;
import com.icoffice.library.utils.CommonUtils;
import com.icoffice.library.utils.GoodsWayUtil;
import com.icofficeapp.R;

@SuppressLint({ "ValidFragment", "DefaultLocale" })
public class WayAndGoodsFragment extends Fragment implements OnClickListener,WayAndGoodsCallBack,WayAndGoodsInterface,TemplateCallBack{
	private BaseFragmentActivity mContext;
	private BaseMachineControl mBaseMachineControl;
	private Button btn_addAll,btn_commit,btn_template;
	private LinearLayout ll_second;
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	private ClearEditText mClearEditText;
	private Button btn_dismiss;
	private GridView gv;
	private RelativeLayout rl_cover;
	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;
	
	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;
	
	private ViewHolder vh;
	protected HashMap<String, WayBean> wayBean_map = new HashMap<String, WayBean>();//货道map key：way_id value：货道信息
	protected HashMap<String, GoodsStateBean> goodsStateBean_map = new HashMap<String, GoodsStateBean>();//在售商品map key：g_code value：商品信息
	
	protected ArrayList<GoodsStateBean> goodsStateBean_list = new ArrayList<GoodsStateBean>(); //商品修改后的信息
	
	protected HashMap<String, WayBean> wayBeanRecovery_map = new LinkedHashMap<String, WayBean>();//数据备份map key：way_id value：货道信息
	protected RecoveryRecordsBean mRecordsRecovery = new RecoveryRecordsBean();//数据备份 
	private boolean is_commit_RecordsRecovery = false;//是否有备份数据要提交 false:没有数据未提交  true:有数据未提交
	
	private HashMap<String, WayBean> current_wayBean_map = new HashMap<String, WayBean>();//传入adapter里的货道map key：way_id value：货道信息
	
	private WayAndGoodsStatusAdapter gvAdapter;
	public WayAndGoodsFragment(BaseFragmentActivity mContext,BaseMachineControl mBaseMachineControl){
		this.mContext = mContext;
		this.mBaseMachineControl = mBaseMachineControl;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.library_wayandgoods_set, null);
		initView(view);
		initListener();
		initData();
		return view;
	}
	void initView(View view){
		btn_addAll = (Button) view.findViewById(R.id.wayAndGoods_addAll);
		btn_commit = (Button) view.findViewById(R.id.wayAndGoods_commit);
		gv = (GridView) view.findViewById(R.id.wayAndGoods_gv);
		ll_second = (LinearLayout) view.findViewById(R.id.wayAndGoods_item);
		sideBar = (SideBar) view.findViewById(R.id.sidrbar);
		dialog = (TextView) view.findViewById(R.id.dialog);
		sortListView = (ListView) view.findViewById(R.id.country_lvcountry);
		mClearEditText = (ClearEditText) view.findViewById(R.id.filter_edit);
		btn_dismiss = (Button) view.findViewById(R.id.wayAndGoods_dismiss);
		rl_cover = (RelativeLayout) view.findViewById(R.id.library_wayandgoods_set_cover);
		btn_template = (Button) view.findViewById(R.id.wayAndGoods_template);
	}
	void initListener(){
		btn_addAll.setOnClickListener(this);
		btn_commit.setOnClickListener(this);
		btn_template.setOnClickListener(this);
		btn_dismiss.setOnClickListener(this);
		gv.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				CommonUtils.hideKeyBoard(mContext, arg0);
				return false;
			}
		});
	}
	void initData(){
		//实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		
		pinyinComparator = new PinyinComparator();
		sideBar.setTextView(dialog);
		mBaseMachineControl.setWayAndGoodsCallBack(this);
		mBaseMachineControl.setTemplateCallBack(this);
		
		wayBean_map = mBaseMachineControl.selectWayBean();
		goodsStateBean_map = mBaseMachineControl.selectSoldGoodsBean();
		getRecovery();
		gvAdapter = new WayAndGoodsStatusAdapter(mContext, mBaseMachineControl, mBaseMachineControl.getWCode(), current_wayBean_map, goodsStateBean_map,this,mContext.getRecoverAssistWayBean_map());
		gv.setAdapter(gvAdapter);
		
		Iterator<Entry<String, GoodsStateBean>> obj = goodsStateBean_map.entrySet().iterator();
		while(obj.hasNext()){
			 Entry<String, GoodsStateBean> item = obj.next();
			 GoodsStateBean mGoodsStateBean = item.getValue();
			 goodsStateBean_list.add(mGoodsStateBean);
		}
		initSortListView();
	}
	void initSortListView(){
		sideBar.setTextView(dialog);

		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(sideBarListener());

		sortListView.setOnItemClickListener(sortListViewListener());

		SourceDateList = filledData(goodsStateBean_list);

		// 根据a-z进行排序源数据
		Collections.sort(SourceDateList, pinyinComparator);
		adapter = new SortAdapter(mContext, SourceDateList);
		sortListView.setAdapter(adapter);

		// 根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(mClearEditTextListener());
	}
	@Override
	public void onClick(View arg0) {
		if(CommonUtils.isFastDoubleClick())
			return;
		CommonUtils.hideKeyBoard(mContext, arg0);
		switch(arg0.getId()){
		case R.id.wayAndGoods_addAll:
			if(mBaseMachineControl.isTemplateRecordsRecoveryClear()){
				btn_commit.setClickable(false);
				btn_addAll.setClickable(false);
//				gv.smoothScrollToPositionFromTop(0, -gv.getHeight() * 10, 3000);
				gvAdapter.addFull();
				btn_commit.setClickable(true);
				btn_addAll.setClickable(true);
				mContext.commentToast.show("一键补货成功");
			}else{
				mContext.commentToast.show("有数据未提交，不能一键补货");
			}
			break;
		case R.id.wayAndGoods_commit:
			mContext.showDialog(BaseViewHandler.Title, "");
			if(is_commit_RecordsRecovery){
				gvAdapter.commitRecover(mContext.mNetWork,mRecordsRecovery);
			}
			else	
				gvAdapter.commit(mContext.mNetWork);
			break;
		case R.id.wayAndGoods_dismiss:
			ll_second.setVisibility(View.INVISIBLE);
			break;
		case R.id.wayAndGoods_template:
			CommonUtils.hideKeyBoard(mContext, arg0);
			if(mBaseMachineControl.isTemplateWayAndGoodsBeanClear()){
				btn_template.setClickable(false);
				mContext.showDialog(BaseViewHandler.Title, "");
				mBaseMachineControl.post_template();
				btn_template.setClickable(true);
			}else{
				mContext.commentToast.show("有数据未提交，不能获取模板");
			}
			break;
		}
		
	}
	OnItemClickListener lvListener(){
		return new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				ll_second.setVisibility(View.INVISIBLE);
				gvAdapter.addGoods(vh,goodsStateBean_list.get(arg2));
				
			}
		};
	}
	OnTouchingLetterChangedListener sideBarListener(){
		return new OnTouchingLetterChangedListener() {
			
			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}
			}
		};
		
	}
	OnItemClickListener sortListViewListener(){
		return new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
				ll_second.setVisibility(View.INVISIBLE);
				for(int i = 0;i < goodsStateBean_list.size();i++){
					if(SourceDateList.get(arg2).getG_code() == goodsStateBean_list.get(i).getG_code()){
						gvAdapter.addGoods(vh,goodsStateBean_list.get(i));
						break;
					}
				}
				
			}
		}; 
		
	}
	TextWatcher mClearEditTextListener(){
		return new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(arg0.toString());
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				
			}
		};
		
	}
	/**
	 * 为ListView填充数据
	 * @param date
	 * @return
	 */
	private List<SortModel> filledData(ArrayList<GoodsStateBean> goods_list){
		List<SortModel> mSortList = new ArrayList<SortModel>();
		
		for(int i=0; i<goods_list.size(); i++){
			SortModel sortModel = new SortModel();
			GoodsStateBean goodsStateBean = goods_list.get(i);
			sortModel.setName(goodsStateBean.getG_name());
			sortModel.setG_code(goodsStateBean.getG_code());
			//汉字转换成拼音
			String pinyin = characterParser.getSelling(goodsStateBean.getG_name());
			String sortString = pinyin.substring(0, 1).toUpperCase();
			
			// 正则表达式，判断首字母是否是英文字母
			if(sortString.matches("[A-Z]")){
				sortModel.setSortLetters(sortString.toUpperCase());
			}else{
				sortModel.setSortLetters("#");
			}
			
			mSortList.add(sortModel);
		}
		return mSortList;
		
	}
	
	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * @param filterStr
	 */
	private void filterData(String filterStr){
		List<SortModel> filterDateList = new ArrayList<SortModel>();
		
		if(TextUtils.isEmpty(filterStr)){
			filterDateList = SourceDateList;
		}else{
			filterDateList.clear();
			for(SortModel sortModel : SourceDateList){
				String name = sortModel.getName();
				if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
					filterDateList.add(sortModel);
				}
			}
		}
		
		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}
	public void showGoodsList(ViewHolder vh) {
		this.vh = vh;
		ll_second.setVisibility(View.VISIBLE);
	}
	@Override
	public void success(String msg) {
		hideCover();
		mContext.dismissDialog();
		gvAdapter.commitSuccess(mContext.mDbHelper);
		mContext.commentToast.show(msg);
		CommonUtils.showLog(CommonUtils.WAYANDGOODS_TAG, "补完货的库存量 ＝ " + mContext.mBaseMachineControl.analysisCursor("select * from WayBean"));
		deleteRecovery();
	}
	@Override
	public void fail(String msg) {
		hideCover();
		mContext.dismissDialog();
		mContext.commentToast.show(msg);
		deleteRecovery();
	}
	@Override
	public void connectFail(String msg) {
		mContext.dismissDialog();
		mContext.commentToast.show(msg);
		gvAdapter.connectFail(mBaseMachineControl);
		getRecovery();
	}
	void deleteRecovery(){
		mBaseMachineControl.deleteRecordsRecovery();
		mBaseMachineControl.deleteWayBeanRecovery();
	}
	void showCover(){
		is_commit_RecordsRecovery = true;
		rl_cover.setVisibility(View.VISIBLE);
	}
	void hideCover(){
		is_commit_RecordsRecovery = false;
		rl_cover.setVisibility(View.GONE);
	}
	void getRecovery(){
		wayBeanRecovery_map = mBaseMachineControl.selectWayBeanRecovery();
		mRecordsRecovery = mBaseMachineControl.selectRecordsRecovery();
		current_wayBean_map.clear();
		if(mRecordsRecovery.getVersion() != null){
			showCover();
			current_wayBean_map.putAll(wayBeanRecovery_map);
		}
		else{
			hideCover();
			current_wayBean_map.putAll(wayBean_map) ;
		}
	}
	@Override
	public void TemplateSuccess(StatusTemplateBean statusTemplateBean) {
		mContext.dismissDialog();
		mContext.commentToast.show(statusTemplateBean.getMsg());
		if(statusTemplateBean.getStatus().equals("1")){
			Iterator<Entry<String, WayBean>> obj = wayBean_map.entrySet().iterator();
			while(obj.hasNext()){
				Entry<String, WayBean> item = obj.next();
				for(int i = 0;i < statusTemplateBean.getMw_list().size();i++){
					WayBean wayBean = statusTemplateBean.getMw_list().get(i);
					if(item.getKey().equals(GoodsWayUtil.getWayId(wayBean.getWay_id()))){
						CommonUtils.showLog(CommonUtils.TEMPLATE_TAG,"GoodsAndWay:way_id = " + item.getKey() + ";g_code = " + wayBean.getG_code());
						item.getValue().setG_code(wayBean.getG_code());;
						break;
					}
				}
			}
			getRecovery();
			gvAdapter.addTemplateGoods(wayBean_map);
		}
		CommonUtils.showLog(CommonUtils.TEMPLATE_TAG,"GoodsAndWay success");
	}
	@Override
	public void TemplateFail(StatusTemplateBean statusTemplateBean) {
		// TODO Auto-generated method stub
		CommonUtils.showLog(CommonUtils.TEMPLATE_TAG,"GoodsAndWay fail");
	}
	@Override
	public void TemplateConnectFail(String e) {
		mContext.dismissDialog();
		mContext.commentToast.show(e);
	}
}
