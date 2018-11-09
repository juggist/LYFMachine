package com.icofficeapp.activity;


import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.icoffice.library.bean.db.AdStatusBean;
import com.icoffice.library.bean.db.SoldGoodsBean;
import com.icoffice.library.callback.MediaPlayerCallBack;
import com.icoffice.library.configs.Constant;
import com.icoffice.library.moudle.control.MediaPlayControl;
import com.icoffice.library.utils.ApkUtil;
import com.icoffice.library.utils.BitmapUtil;
import com.icoffice.library.utils.CommonUtils;
import com.icoffice.library.utils.FileUtil;
import com.icofficeapp.R;
import com.icofficeapp.adapter.AdAdapter;

/**
 * 广告位view
 * @author lufeisong
 *
 */
@SuppressLint({ "ValidFragment", "HandlerLeak" })
public class AdFragment extends BaseFragment implements OnClickListener,MediaPlayerCallBack{
	private BaseFragmentActivity mContext;
	private ChangeViewInterface mChangeViewInterface;
	
	private Button btn_door_open,btn_door_close,btn_ev_open,btn_ev_close,btn_track_open,btn_track_close,btn_outgoods;
	
	private TextView tv_test,tv_version;
	private Button btn_left,btn_right;
	private ImageView iv_more;
	private GridView gv;
	private ImageView iv_bigPic;
	private SurfaceView mSurfaceView; 
	
	private MediaPlayControl mMediaPlayControl;//音频控制对象

	private final int adapter_size = 8;//adapter的item个数
	private int smallPicPage_count = 0;//小广告当前的页面count
	private int smallPicPage_size = 0;//小广告总共的count
	private AdStatusBean mAdStatusBean = new AdStatusBean();//当前广告对象
	private ArrayList<AdStatusBean> AdStatusBean_list = new ArrayList<AdStatusBean>();//小广告总共的pic路径的集合
	private ArrayList<String> ad_picture_current_list = new ArrayList<String>();//小广告adapter当前pic路径的集合
	
	private AdAdapter adapter;
	private ScheduledExecutorService mScheduledExecutorService;
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				showAd();
				break;
			}
			
			super.handleMessage(msg);
		}
	};
	public AdFragment(){
		
	}
	public AdFragment(BaseFragmentActivity mContext,ChangeViewInterface mChangeViewInterface){
		CommonUtils.showLog(CommonUtils.EXCEPTION_TAG, "AdFragment");
		this.mContext = mContext;
		this.mChangeViewInterface = mChangeViewInterface;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_ad, container, false);
		initView(view);
		initListener();
		initData();
		return view;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initScheduledExecutorService();
	}
	
	@Override
	public void onResume() {
		CommonUtils.showLog(CommonUtils.AD_TAG, "AdFragment onResume");
		super.onResume();
	}
	@Override
	public void onDestroy() {
		CommonUtils.showLog(CommonUtils.AD_TAG, "AdFragment onDestory");
		shutDownScheduledExecutorService();
		super.onDestroy();
	}
	void initView(View view){
		tv_test = (TextView) view.findViewById(R.id.fragment_ad_test);
		tv_version = (TextView) view.findViewById(R.id.fragment_shop_version_tv);
		iv_bigPic = (ImageView) view.findViewById(R.id.fragment_ad_iv_bigPic);
		btn_left = (Button) view.findViewById(R.id.fragment_ad_btn_left);
		btn_right = (Button) view.findViewById(R.id.fragment_ad_btn_right);
		iv_more = (ImageView) view.findViewById(R.id.fragment_ad_iv_more);
		gv = (GridView) view.findViewById(R.id.fragment_ad_gv);
		mSurfaceView = (SurfaceView) view.findViewById(R.id.fragment_ad_sfview);
		
		btn_door_open = (Button) view.findViewById(R.id.btn_door_open);
		btn_door_close = (Button) view.findViewById(R.id.btn_door_close);
		btn_ev_open = (Button) view.findViewById(R.id.btn_ev_open);
		btn_ev_close = (Button) view.findViewById(R.id.btn_ev_close);
		btn_track_open = (Button) view.findViewById(R.id.btn_track_open);
		btn_track_close = (Button) view.findViewById(R.id.btn_track_close);
		btn_outgoods = (Button) view.findViewById(R.id.btn_outgoods);
	}
	void initListener(){
		btn_left.setOnClickListener(this);
		btn_right.setOnClickListener(this);
		iv_more.setOnClickListener(this);
//		iv_bigPic.setOnClickListener(this);
//		mSurfaceView.setOnClickListener(this);
		gv.setOnItemClickListener(getGvListener());
		
		btn_outgoods.setOnClickListener(this);
		btn_track_close.setOnClickListener(this);
		btn_track_open.setOnClickListener(this);
		btn_ev_close.setOnClickListener(this);
		btn_ev_open.setOnClickListener(this);
		btn_door_close.setOnClickListener(this);
		btn_door_open.setOnClickListener(this);
	}
	void initData(){
		mMediaPlayControl = new MediaPlayControl(mSurfaceView);
		mMediaPlayControl.setMediaPlayerCallBack(this);
		AdStatusBean_list = mMachineControl.getAdStatusBeanList();
		if(mContext.getCurrentAd() >= AdStatusBean_list.size())
			mContext.setCurrentAd(0);
		try{
			tv_version.setText(ApkUtil.getVerNumber(mContext));
			showAd();
		}catch(Exception e){
			
		}
//		CommonUtils.sendMessage(mHandler, 1, "");
		if(Constant.VERSION_CONTROL)
			tv_test.setVisibility(View.GONE);
		else
			tv_test.setVisibility(View.VISIBLE);
		try{
			if(AdStatusBean_list.size() % adapter_size == 0)
				smallPicPage_size = AdStatusBean_list.size() % adapter_size;
			else
				smallPicPage_size = AdStatusBean_list.size() / adapter_size + 1;
			adapter = new AdAdapter(mContext.getApplicationContext(), adapter_size,ad_picture_current_list);
			CommonUtils.showLog(CommonUtils.EXCEPTION_TAG, "AdApter 初始化");
			gv.setAdapter(adapter);
			CommonUtils.showLog(CommonUtils.EXCEPTION_TAG, "AdApter setadapter");
			getAdapterData();
		}catch(Exception e){
			
		}
	}
	//获取适配器数据
	void getAdapterData(){
		if(ad_picture_current_list != null)
			ad_picture_current_list.clear();
		else
			ad_picture_current_list = new ArrayList<String>();
		for(int i = smallPicPage_count * adapter_size;i < (smallPicPage_count + 1 ) * adapter_size;i++){
			if(i < AdStatusBean_list.size()){
				String imgPath = AdStatusBean_list.get(i).getSmallImage();
				if(!mContext.mMachineControl.mFileUtil.isFileExist(FileUtil.ROOT_PATH +imgPath ) || imgPath == null || imgPath.equals("") || imgPath.equals("null"))
					imgPath = AdStatusBean_list.get(i).getFile();
				ad_picture_current_list.add(imgPath);
			}
		}
		adapter.notifyDataSetChanged();
		CommonUtils.showLog(CommonUtils.EXCEPTION_TAG, "AdApter notifyDataSetChanged");
	}
	//刷新适配器
	void refreshAdapter(boolean isNext){
		if(isNext){
			smallPicPage_count++;
		}else{
			smallPicPage_count--;
		}
		getAdapterData();
	}
	void initScheduledExecutorService(){
		mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
	}
	//开始启动广告轮播
	void startScheduledExecutorService(int ad_start_time){
		if(mScheduledExecutorService != null)
			mScheduledExecutorService.schedule(new AdTask(), ad_start_time, TimeUnit.SECONDS);
	}
	//结束广告轮播
	void shutDownScheduledExecutorService(){
		if(mScheduledExecutorService != null && !mScheduledExecutorService.isShutdown())
			mScheduledExecutorService.shutdownNow();
	}
	void ad2Goods(){
		if(CommonUtils.isEmpty(mAdStatusBean.getgCode()) || !mContext.isAvmRunning() || mContext.isDoorOpen()){
			mChangeViewInterface.showMainViewInterface(ChangeViewInterface.PAY_SCREEN);
		}else{
			SoldGoodsBean soldGoodsBean = mMachineControl.selectSoldGoodsStatus8g_code(mAdStatusBean.getgCode());
			if(mMachineControl.normalSold(soldGoodsBean)){
				try {
					CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"广告购买 way_id = " + soldGoodsBean.getW_code() + " 剩余库存为 : " + mMachineControl.getStore8Way_id(soldGoodsBean.getW_code()));
					mContext.showMainViewInterface(ChangeViewInterface.PAY_BUTTON);
				} catch (Exception e) {

				}
			}else{
				mChangeViewInterface.showMainViewInterface(ChangeViewInterface.PAY_SCREEN);
			}
		}
	}
	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.fragment_ad_btn_left:
			if(smallPicPage_count > 0){
				refreshAdapter(false);
			}
			break;
		case R.id.fragment_ad_btn_right:
			if(smallPicPage_count < (smallPicPage_size - 1)){
				refreshAdapter(true);
			}
			break;
		case R.id.fragment_ad_iv_more:
			mChangeViewInterface.showMainViewInterface(ChangeViewInterface.PAY_SCREEN);
			break;
		case R.id.fragment_ad_iv_bigPic:
			ad2Goods();
			break;
		case R.id.fragment_ad_sfview:
			ad2Goods();
			break;
			
			
		case R.id.btn_door_open:
			mMachineControl.VMC_Elevator("Z");
			mMachineControl.setAutoCloseDoor(false);
			doorStatus = 0;
			break;
		case R.id.btn_door_close:
//			mMachineControl.VMC_Elevator("Y");
			mMachineControl.setAutoCloseDoor(true);
			doorStatus = 1;
			break;
		case R.id.btn_ev_open:
			evStatus = 1;
			break;
		case R.id.btn_ev_close:
			evStatus = 0;
			break;
		case R.id.btn_track_open:
			trackStatus = "1";
			break;
		case R.id.btn_track_close:
			trackStatus = "0";
			break;
		case R.id.btn_outgoods:
//			if(trackStatus.equals("1")){
//				mMachineControl.track(doorStatus, evStatus, "D1");
//			}else if(trackStatus.equals("0")){
//				mMachineControl.unTrack(doorStatus, evStatus, "A1");
//			} 
			break;
		}
	}
	private String trackStatus;
	private Integer doorStatus,evStatus;
	void showAd(){
		String pathName = "";
		int ad_start_time = 0;
		int bigPic_count = mContext.getCurrentAd();
		if(bigPic_count < AdStatusBean_list.size()){
			mAdStatusBean = AdStatusBean_list.get(bigPic_count);
			pathName =  FileUtil.ROOT_PATH + mAdStatusBean.getFile();
			ad_start_time = mAdStatusBean.getInterval();
			bigPic_count++;
			mContext.setCurrentAd(bigPic_count);
			startScheduledExecutorService(ad_start_time);
		}else{
			bigPic_count = 0;
			mContext.setCurrentAd(bigPic_count);
			if(AdStatusBean_list.size() > 0){
				mAdStatusBean = AdStatusBean_list.get(bigPic_count);
				pathName = FileUtil.ROOT_PATH + mAdStatusBean.getFile();
				ad_start_time = mAdStatusBean.getInterval();
				startScheduledExecutorService(ad_start_time);
			}
		}
		CommonUtils.showLog(CommonUtils.AD_TAG, "ad Interval time = " + ad_start_time);
		CommonUtils.showLog(CommonUtils.AD_TAG, "pathName = " + pathName);
		if(BitmapUtil.isImage(pathName)){
			iv_bigPic.setVisibility(View.VISIBLE);
			BitmapUtil.displayBitmap2Sdcard(pathName, iv_bigPic);
			mMediaPlayControl.resetMedia();
		}else if(BitmapUtil.isMedia(pathName)){
			mMediaPlayControl.startMedia(pathName);
			iv_bigPic.setVisibility(View.INVISIBLE);
		}
	}
	//点击广告小图的动作
	GridView.OnItemClickListener getGvListener(){
		return new GridView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				int bigPic_count = smallPicPage_count * adapter_size + arg2;
				if(bigPic_count == (AdStatusBean_list.size())){
					
				}else{
					mContext.setCurrentAd(bigPic_count);
					shutDownScheduledExecutorService();
					initScheduledExecutorService();
					CommonUtils.sendMessage(mHandler, 1, "");
				}
			}
		};
	}
	class AdTask implements Runnable{

		@Override
		public void run() {
			CommonUtils.sendMessage(mHandler, 1, "");
		}
	}
	@Override
	public void onStart() {
		CommonUtils.showLog(CommonUtils.AD_TAG, "AdFragment onStart");
		try{
			mContext.setmAdFragment(this);
		}catch(Exception e){
			
		}
		
		super.onStart();
	}

	@Override
	public void onDetach() {
		CommonUtils.showLog(CommonUtils.AD_TAG, "AdFragment onDetach");
		try{
			mContext.setmAdFragment(null);
		}catch(Exception e){
			
		}
		
		super.onDetach();
	}
	@Override
	public void onCompletion() {
		
	}
	/**
	 * 跳转购买模块 接口
	 * 1.通过物理按键或者点击绑定商品的广告
	 * 2.通过点击未绑定商品的广告或者“更多商品”按钮进入
	 * @author lufeisong
	 *
	 */
	public interface ChangeViewInterface {
		public static final int PAY_BUTTON = 0;//通过物理按键或者点击绑定商品的广告              购买
		public static final int PAY_SCREEN = 1;//通过点击未绑定商品的广告或者“更多商品”按钮进入    购买
		void showMainViewInterface(int BUY_TYPE);
	}
}
