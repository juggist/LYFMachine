package com.icoffice.library.backactivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.icoffice.library.callback.BackQuitCallBack;
import com.icoffice.library.configs.Constant;
import com.icoffice.library.utils.ApkUtil;
import com.icoffice.library.widget.CommentDialogUtils;
import com.icofficeapp.R;
import com.icofficeapp.app.ICofficeApplication;

public class BackstageActivity extends BaseFragmentActivity implements OnClickListener,BackQuitCallBack{
	private RelativeLayout ll_menu;
	private LinearLayout ll_menu_show;
	private Button btn_way,btn_goods,btn_sales,btn_update,btn_exit,btn_right,btn_machine,btn_kill,btn_crate,btn_smc,btn_payType,btn_compressor;
	private Button btn_left;
	private TextView tv_title,tv_version;
	private FragmentTransaction t;
	private ICofficeApplication application;
	
	private boolean isBackQuit = true;//是否可以直接退出后台
	private Dialog dialog;//提示dialog
	
	private long timeDown = 0 ;//手指按下去的时间（针对测试版本）
	private long timeUp  = 0;//手指抬起的时间（针对测试版本）
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.library_backstage_activity);
		initView();
		initListener();
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				showBanner();
				initData();
			}
		}, 100);
	}
	
	@Override
	protected void onStart() {
		
		super.onStart();
	}

	@Override
	protected void onResume() {
		
		super.onResume();
	}

	void initView(){
		ll_menu_show = (LinearLayout) findViewById(R.id.library_menu_show);
		ll_menu = (RelativeLayout) findViewById(R.id.library_main_menu);
		btn_way = (Button) findViewById(R.id.library_menu_way);
		btn_goods = (Button) findViewById(R.id.library_menu_goods);
		btn_sales = (Button) findViewById(R.id.library_menu_goodsAndWay);
		btn_update = (Button) findViewById(R.id.library_menu_update);
		btn_machine = (Button) findViewById(R.id.library_menu_machine);
		btn_exit = (Button) findViewById(R.id.library_menu_exit);
		btn_payType = (Button) findViewById(R.id.library_menu_paytype);
		
		btn_right = (Button) findViewById(R.id.library_menu_right);
		
		btn_kill = (Button) findViewById(R.id.library_menu_killapp);
		btn_crate = (Button) findViewById(R.id.library_menu_crate);
		btn_smc = (Button) findViewById(R.id.library_menu_smc);
		btn_compressor = (Button) findViewById(R.id.library_menu_compressor);
	}
	void initListener(){
		btn_way.setOnClickListener(this);
		btn_goods.setOnClickListener(this);
		btn_update.setOnClickListener(this);
		btn_exit.setOnClickListener(this);
		btn_sales.setOnClickListener(this);
		btn_right.setOnClickListener(this);
		btn_machine.setOnClickListener(this);
		btn_kill.setOnClickListener(this);
		btn_crate.setOnClickListener(this);
		btn_smc.setOnClickListener(this);
		btn_payType.setOnClickListener(this);
		btn_compressor.setOnClickListener(this);
		if(!Constant.VERSION_CONTROL){
			btn_exit.setOnTouchListener(btn_returnMenu_touchListener());
			btn_kill.setOnTouchListener(btn_killApp_touchListener());
		}
	}
	void initData(){
		application = (ICofficeApplication) getApplication();
		ll_menu_show.setVisibility(View.INVISIBLE);
		//TODO
//		ll_menu_show.setVisibility(View.VISIBLE);
		if(mBaseMachineControl.isFristRegister())
			changeRegisterMachineFragment();
		else
			changeUserEnterFragment();
		
	}
	void changeRegisterMachineFragment(){
		hideMenu();
		tv_title.setText("注册机器");
		t = this.getSupportFragmentManager().beginTransaction();
		t.replace(R.id.library_main_detail, new RegisterMachineFragment(this));
		t.commit();
	}
	void changeUserEnterFragment(){
		hideMenu();
		tv_title.setText("管理员登入");
		t = this.getSupportFragmentManager().beginTransaction();
		t.replace(R.id.library_main_detail, new UserEnterFragment(this));
		t.commit();
	}
	void changeWayFragment(){
		hideMenu();
		tv_title.setText("最大补货量");
		t = this.getSupportFragmentManager().beginTransaction();
		t.replace(R.id.library_main_detail, new WaySetFragment(this,mBaseMachineControl));
		t.commit();
	}
	void changeGoodsFragment(){
		hideMenu();
		tv_title.setText("选货管理");
		t = this.getSupportFragmentManager().beginTransaction();
		t.replace(R.id.library_main_detail, new GoodsSetFragment(this,mBaseMachineControl));
		t.commit();
		isBackQuit = false;
	}
	void changeWayAndGoodsFragment(){
		hideMenu();
		tv_title.setText("补货管理");
		t = this.getSupportFragmentManager().beginTransaction();
		t.replace(R.id.library_main_detail, new WayAndGoodsFragment(this,mBaseMachineControl));
		t.commit();
		isBackQuit = false;
	}
	void changeMachineFragment(){
		hideMenu();
		tv_title.setText("机器状态");
		t = this.getSupportFragmentManager().beginTransaction();
		t.replace(R.id.library_main_detail, new MachineFrgment(this));
		t.commit();
	}
	void changeUpdateFragment(){
		hideMenu();
		tv_title.setText("软件信息");
		t = this.getSupportFragmentManager().beginTransaction();
		t.replace(R.id.library_main_detail, new SoftwareFragment(this,mBaseMachineControl));
		t.commit();
	}
	void chooseCrateFragment(){
		hideMenu();
		tv_title.setText("选择货柜");
		t = this.getSupportFragmentManager().beginTransaction();
		t.replace(R.id.library_main_detail, new ChooseCrateFragment(this));
		t.commit();
	}
	void chooseSMCFragment(){
		hideMenu();
		tv_title.setText("单片机同步");
		t = this.getSupportFragmentManager().beginTransaction();
		SCMSetFragment mSCMSetFragment = new SCMSetFragment(this);
		t.replace(R.id.library_main_detail, mSCMSetFragment);
		mSCMSetFragment.setBackQuitCallBack(this);
		t.commit();
	}
	void choosePayTypeFragment(){
		hideMenu();
		tv_title.setText("选择支付方式");
		t = this.getSupportFragmentManager().beginTransaction();
		ChoosePayTypeFragment mChoosePayTypeFragment = new ChoosePayTypeFragment(this);
		t.replace(R.id.library_main_detail, mChoosePayTypeFragment);
		t.commit();
	}
	void chooseCompressor(){
		hideMenu();
		tv_title.setText("压缩机设置");
		t = this.getSupportFragmentManager().beginTransaction();
		LYCoolCondenserFragment mLYCoolCondenserFragment = new LYCoolCondenserFragment(this);
		t.replace(R.id.library_main_detail, mLYCoolCondenserFragment);
		t.commit();
	}
	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.library_menu_way:
			changeWayFragment();
			break;
		case R.id.library_menu_goods:
			changeGoodsFragment();
			break;
		case R.id.library_menu_goodsAndWay:
			changeWayAndGoodsFragment();
			break;
		case R.id.library_menu_machine:
			changeMachineFragment();
			break;
		case R.id.library_menu_update:
			changeUpdateFragment();
			break;
		case R.id.library_menu_exit:
			showReturnmenuDialog();
			break;
		case R.id.library_detail_left:
			showMenu();
			break;
		case R.id.library_menu_right:
			hideMenu();
			break;
		case R.id.library_menu_killapp:
			showExitDialog();
			break;
		case R.id.library_menu_crate:
			chooseCrateFragment();
			break;
		case R.id.library_menu_smc:
			chooseSMCFragment();
			break;
		case R.id.library_menu_paytype:
			choosePayTypeFragment();
			break;
		case R.id.library_menu_compressor:
			chooseCompressor();
			break;
		}
		
	}
	void showMenu(){
		if(ll_menu.getVisibility() == View.INVISIBLE)
			ll_menu.setVisibility(View.VISIBLE);
		btn_left.setVisibility(View.INVISIBLE);
	}
	void hideMenu(){
		if(ll_menu.getVisibility() == View.VISIBLE)
			ll_menu.setVisibility(View.VISIBLE);
		btn_left.setVisibility(View.VISIBLE);
	}
	//从登入 跳到 补货管理
	public void showWayView(){
		if(mBaseMachineControl.isExistCrate())
			changeWayAndGoodsFragment();
		else
			chooseCrateFragment();
		ll_menu_show.setVisibility(View.VISIBLE);
	}
	public void showUserEnterView(){
		changeUserEnterFragment();
		ll_menu_show.setVisibility(View.INVISIBLE);
	}
	void showBanner(){
		View view = LayoutInflater.from(getApplication()).inflate(R.layout.library_banner, null);
		PopupWindow popupWindow = new PopupWindow(view, android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, false);
		popupWindow.showAtLocation(findViewById(R.id.library_detail_top), Gravity.TOP, 0, 0);
		popupWindow.update();
		btn_left = (Button) view.findViewById(R.id.library_detail_left);
		tv_title = (TextView) view.findViewById(R.id.library_detail_tv);
		tv_version = (TextView) view.findViewById(R.id.library_vsrsion_tv);
		tv_version.setText(ApkUtil.getVerName(getApplication()));
		btn_left.setOnClickListener(this);
	}
	void showReturnmenuDialog(){
		if(isBackQuit){
			Intent intent = new Intent(ICofficeApplication.NOTICE_UPDATE_ADAPTER);  
            sendBroadcast(intent);
			BackstageActivity.this.finish();
		}else{
			if(application.getPhysicsButtonStatus().equals("1")){
				disMissDialog();
				dialog = new CommentDialogUtils(this, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, R.layout.library_dialog_exit,R.style.MyDialog);
				dialog.setCanceledOnTouchOutside(false);
				dialog.show();
				Button btn_returnmenu_sure = (Button) dialog.findViewById(R.id.library_dialog_exit_sure);
				btn_returnmenu_sure.setOnClickListener(btn_returnmenu_sureListener());
				chooseSMCFragment();
			}else{
				Intent intent = new Intent(ICofficeApplication.NOTICE_UPDATE_ADAPTER);  
	            sendBroadcast(intent);
				BackstageActivity.this.finish();
			}
		}
		
	}

	OnClickListener btn_returnmenu_sureListener(){
		return new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				disMissDialog();
			}
		};
	}
	void showExitDialog(){
		if(isBackQuit){
			application.getBaseMachineControl().exitApp(true);
		}else{
			if(application.getPhysicsButtonStatus().equals("1")){
				disMissDialog();
				dialog = new CommentDialogUtils(this, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, R.layout.library_dialog_returnmenu,R.style.MyDialog);
				dialog.setCanceledOnTouchOutside(false);
				dialog.show();
				Button btn_exit_sure = (Button) dialog.findViewById(R.id.library_dialog_returnmenu_sure);
				btn_exit_sure.setOnClickListener(btn_exit_sureListener());
				chooseSMCFragment();
			}else{
				application.getBaseMachineControl().exitApp(true);
			}
		}
	}
	OnClickListener btn_exit_sureListener(){
		return new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				disMissDialog();
			}
		};
	}
	OnTouchListener btn_returnMenu_touchListener(){
		return new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				switch (arg1.getAction()) {
				case MotionEvent.ACTION_DOWN:
					timeDown = System.currentTimeMillis();
					break;
				case MotionEvent.ACTION_UP:
					timeUp = System.currentTimeMillis();
					if((timeUp - timeDown) > 3000){
						Intent intent = new Intent(ICofficeApplication.NOTICE_UPDATE_ADAPTER);  
			            sendBroadcast(intent);
						BackstageActivity.this.finish();
					}
					break;
				default:
					break;
				}
				return false;
			}
		};
		
	}
	OnTouchListener btn_killApp_touchListener(){
		return new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				switch (arg1.getAction()) {
				case MotionEvent.ACTION_DOWN:
					timeDown = System.currentTimeMillis();
					break;
				case MotionEvent.ACTION_UP:
					timeUp = System.currentTimeMillis();
					if((timeUp - timeDown) > 3000){
						application.getBaseMachineControl().exitApp(true);
					}
					break;
				default:
					break;
				}
				return false;
			}
		};
		
	}
	void disMissDialog(){
		try{
			if(dialog != null)
				dialog.dismiss();
		}catch(Exception e){
			
		}
	}
	@Override
	public void quitSwitch(boolean on_off) {
		isBackQuit = on_off;
	}
}
