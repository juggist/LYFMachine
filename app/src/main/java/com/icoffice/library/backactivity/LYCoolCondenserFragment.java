package com.icoffice.library.backactivity;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.icofficeapp.R;
import com.leiyuntop.Refrigerator.EAlarm;
import com.leiyuntop.Refrigerator.ECommand;
import com.leiyuntop.Refrigerator.ERunModel;
import com.leiyuntop.Refrigerator.ERunState;
import com.leiyuntop.Refrigerator.EUnitState;
import com.leiyuntop.Refrigerator.Refrigerator;

@SuppressLint( "ValidFragment")
public class LYCoolCondenserFragment extends Fragment implements OnClickListener{
	private BaseFragmentActivity _context;
	private Refrigerator mRefrigerator;//运行
	private ERunModel runModel;
	private ERunState runState;
	
	private EAlarm mEAlarm;//超温
	
	private EUnitState mEUnitState;//设备
	private Timer mTimer = new Timer();
	private TimerTask mTimerTask;
	private LinkedHashMap<ECommand, int[]> limitValue;//温度限值
	
	
	private Button startBnt,stopBnt,hot_startBnt;
	private TextView nowTemp_tv;
	private double[] nowTemp;
	private HashMap<String,Button> runModelMap = new LinkedHashMap<String, Button>();
	private TextView tv_1_1,tv_1_2,tv_1_3,tv_1_4,tv_1_5,tv_1_6,tv_1_7;
	private HashMap<String,TextView> runStateMap = new LinkedHashMap<String, TextView>();
	private TextView tv_2_1,tv_2_2,tv_2_3,tv_2_4;
	private TextView tv_3_1,tv_3_2,tv_3_3; 
	
	private TextView tv_show_1,tv_show_2,tv_show_3,tv_show_4,tv_show_5,tv_show_6,tv_show_7,tv_show_8,tv_show_9,tv_show_10;
	private Button bnt_left_1,bnt_left_2,bnt_left_3,bnt_left_4,bnt_left_5,bnt_left_6,bnt_left_7,bnt_left_8,bnt_left_9,bnt_left_10;
	private TextView tv_set_1,tv_set_2,tv_set_3,tv_set_4,tv_set_5,tv_set_6,tv_set_7,tv_set_8,tv_set_9,tv_set_10;
	private Button bnt_right_1,bnt_right_2,bnt_right_3,bnt_right_4,bnt_right_5,bnt_right_6,bnt_right_7,bnt_right_8,bnt_right_9,bnt_right_10;
	private Button bnt_set_1,bnt_set_2,bnt_set_3,bnt_set_4,bnt_set_5,bnt_set_6,bnt_set_7,bnt_set_8,bnt_set_9,bnt_set_10;
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:
				if(mRefrigerator != null){
					tv_show_1.setText(mRefrigerator.getPowerProtectionTime() + "");
					tv_show_2.setText(mRefrigerator.getEvaporatorFanOffDelayTime() + "");
					tv_show_3.setText(mRefrigerator.getRefrigerationTemp() + "");
					tv_show_4.setText(mRefrigerator.getRefrigerationBacklash() + "");
					tv_show_5.setText(mRefrigerator.getDefrostTime() + "");
					tv_show_6.setText(mRefrigerator.getDefrostInterval() + "");
					tv_show_7.setText(mRefrigerator.getCondenserTempAlarmTemp() + "");
					tv_show_8.setText(mRefrigerator.getCondenserFanStartTemp()+ "");
					
					tv_set_1.setText(mRefrigerator.getPowerProtectionTime() + "");
					tv_set_2.setText(mRefrigerator.getEvaporatorFanOffDelayTime() + "");
					tv_set_3.setText(mRefrigerator.getRefrigerationTemp() + "");
					tv_set_4.setText(mRefrigerator.getRefrigerationBacklash() + "");
					tv_set_5.setText(mRefrigerator.getDefrostTime() + "");
					tv_set_6.setText(mRefrigerator.getDefrostInterval() + "");
					tv_set_7.setText(mRefrigerator.getCondenserTempAlarmTemp() + "");
					tv_set_8.setText(mRefrigerator.getCondenserFanStartTemp()+ "");
				}
				break;
			case 1:
				nowTemp_tv.setText("箱体温度：" + nowTemp[0] + "");
				if(runModel != null){
					startBnt.setTextColor(Color.WHITE);
					stopBnt.setTextColor(Color.WHITE);
					hot_startBnt.setTextColor(Color.WHITE);
					startBnt.setTag("1");
					stopBnt.setTag("1");
					hot_startBnt.setTag("1");
					if (runModel == ERunModel.制冷){
						startBnt.setTextColor(Color.RED);
						startBnt.setTag("0");
					}else if(runModel == ERunModel.停机){
						stopBnt.setTextColor(Color.RED);
						stopBnt.setTag("0");
					}else if(runModel == ERunModel.加热){
						hot_startBnt.setTextColor(Color.RED);
						hot_startBnt.setTag("0");
					}
				}
				if(runState != null){
					tv_1_1.setTextColor(Color.WHITE);
					tv_1_2.setTextColor(Color.WHITE);
					tv_1_3.setTextColor(Color.WHITE);
					tv_1_4.setTextColor(Color.WHITE);
					tv_1_5.setTextColor(Color.WHITE);
					tv_1_6.setTextColor(Color.WHITE);
					tv_1_7.setTextColor(Color.WHITE);
					if(runState == ERunState.停机){
						tv_1_1.setTextColor(Color.RED);
					}
					if(runState == ERunState.延时){
						tv_1_2.setTextColor(Color.RED);
					}
					if(runState == ERunState.制冷){
						tv_1_3.setTextColor(Color.RED);
					}
					if(runState == ERunState.恒温){
						tv_1_4.setTextColor(Color.RED);
					}
					if(runState == ERunState.除霜){
						tv_1_5.setTextColor(Color.RED);
					}
					if(runState == ERunState.故障){
						tv_1_6.setTextColor(Color.RED);
					}
					if(runState == ERunState.加热){
						tv_1_7.setTextColor(Color.RED);
					}
				}
				if(mEAlarm != null){
					if(mEAlarm.getBox()){
						tv_3_1.setTextColor(Color.RED);
					}else{
						tv_3_1.setTextColor(Color.WHITE);
					}
					if(mEAlarm.getEvaporator()){
						tv_3_2.setTextColor(Color.RED);
					}else{
						tv_3_2.setTextColor(Color.WHITE);
					}
					if(mEAlarm.getCondenser()){
						tv_3_3.setTextColor(Color.RED);
					}else{
						tv_3_3.setTextColor(Color.WHITE);
					}
				}
				if(mEUnitState != null){
					if(mEUnitState.getCompressor()){
						tv_2_1.setTextColor(Color.RED);
					}else{
						tv_2_1.setTextColor(Color.WHITE);
					}
					if(mEUnitState.getEvaporatorFan()){
						tv_2_2.setTextColor(Color.RED);
					}else{
						tv_2_2.setTextColor(Color.WHITE);
					}
					if(mEUnitState.getCondenserFan()){
						tv_2_3.setTextColor(Color.RED);
					}else{
						tv_2_3.setTextColor(Color.WHITE);
					}
					if(mEUnitState.getDefrostValve()){
						tv_2_4.setTextColor(Color.RED);
					}else{
						tv_2_4.setTextColor(Color.WHITE);
					}
				}
				break;
			}
			super.handleMessage(msg);
		}
	};
	public LYCoolCondenserFragment(){
		
	}
	public LYCoolCondenserFragment(BaseFragmentActivity context){
		_context = context;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_back_condenser, null);
		
		initView(view);
		initData();
		listener();
		return view;
	}
	void initView(View view ){
		startBnt = (Button)view. findViewById(R.id.start_bnt);
		stopBnt = (Button) view. findViewById(R.id.stop_bnt);
		hot_startBnt = (Button) view.findViewById(R.id.hot_start_bnt);
		nowTemp_tv = (TextView) view. findViewById(R.id.activity_back_comdenser_nowTemp);
		
		tv_1_1 = (TextView)view.  findViewById(R.id.tingji);
		tv_1_2 = (TextView)view.  findViewById(R.id.qidongchaoshi);
		tv_1_3 = (TextView)view.  findViewById(R.id.zhileng);
		tv_1_4 = (TextView) view. findViewById(R.id.hengwen);
		tv_1_5 = (TextView) view. findViewById(R.id.chushang);
		tv_1_6 = (TextView) view. findViewById(R.id.guzhang);
		tv_1_7 = (TextView) view. findViewById(R.id.hot);
		
		tv_2_1 = (TextView) view. findViewById(R.id.yasuoji);
		tv_2_2 = (TextView) view. findViewById(R.id.zhengfajifengshan);
		tv_2_3 = (TextView) view. findViewById(R.id.lengningqifengshan);
		tv_2_4 = (TextView) view. findViewById(R.id.chushuangdianchifa);
		tv_3_1 = (TextView) view. findViewById(R.id.xiangti);
		tv_3_2 = (TextView) view. findViewById(R.id.zhengfaqi);
		tv_3_3 = (TextView) view. findViewById(R.id.lengningqi);
		
		tv_show_1 = (TextView) view. findViewById(R.id.tv_show_time_star);
		tv_show_2 = (TextView) view. findViewById(R.id.zhengfaqi_time_star);
		tv_show_3 = (TextView) view. findViewById(R.id.zhileng_wendu_time_star);
		tv_show_4 = (TextView) view. findViewById(R.id.zhileng_huicha_star);
		tv_show_5 = (TextView) view. findViewById(R.id.huashuang_wendu_star);
		tv_show_6 = (TextView) view. findViewById(R.id.huashuang_time_star);
		tv_show_7 = (TextView) view. findViewById(R.id.lengningqibaojing_wendu_star);
		tv_show_8 = (TextView) view. findViewById(R.id.lengningqistar_wendu_star);
		tv_show_9 = (TextView) view. findViewById(R.id.hot_wendu_star);
		tv_show_10 = (TextView) view. findViewById(R.id.hothuicha_wendu_star);
		
		bnt_left_1 = (Button) view. findViewById(R.id.tv_star_time_left);
		bnt_left_2 = (Button) view. findViewById(R.id.zhengfaqi_time_left);
		bnt_left_3 = (Button) view. findViewById(R.id.zhileng_wendu_time_left);
		bnt_left_4 = (Button) view. findViewById(R.id.zhileng_huicha_left);
		bnt_left_5 = (Button) view. findViewById(R.id.huashuang_wendu_left);
		bnt_left_6 = (Button) view. findViewById(R.id.huashuang_time_left);
		bnt_left_7 = (Button) view. findViewById(R.id.lengningqibaojing_wendu_left);
		bnt_left_8 = (Button) view. findViewById(R.id.lengningqistar_wendu_left);
		bnt_left_9 = (Button) view. findViewById(R.id.hot_wendu_left);
		bnt_left_10 = (Button) view. findViewById(R.id.hothuicha_wendu_left);
		
		bnt_right_1 = (Button) view. findViewById(R.id.tv_star_time_right);
		bnt_right_2 = (Button) view. findViewById(R.id.zhengfaqi_time_right);
		bnt_right_3 = (Button) view. findViewById(R.id.zhileng_wendu_time_right);
		bnt_right_4 = (Button) view. findViewById(R.id.zhileng_huicha_right);
		bnt_right_5 = (Button) view. findViewById(R.id.huashuang_wendu_right);
		bnt_right_6 = (Button) view. findViewById(R.id.huashuang_time_right);
		bnt_right_7 = (Button) view. findViewById(R.id.lengningqibaojing_wendu_right);
		bnt_right_8 = (Button) view. findViewById(R.id.lengningqistar_wendu_right);
		bnt_right_9 = (Button) view. findViewById(R.id.hot_wendu_right);
		bnt_right_10 = (Button) view. findViewById(R.id.hothuicha_wendu_right);
		
		tv_set_1 = (TextView) view. findViewById(R.id.tv_set_time_star);
		tv_set_2 = (TextView) view. findViewById(R.id.zhengfaqi_time_set);
		tv_set_3 = (TextView) view. findViewById(R.id.zhileng_wendu_time_set);
		tv_set_4 = (TextView) view. findViewById(R.id.zhileng_huicha_set);
		tv_set_5 = (TextView) view. findViewById(R.id.huashuang_wendu_set);
		tv_set_6 = (TextView) view. findViewById(R.id.huashuang_time_set);
		tv_set_7 = (TextView)view. findViewById(R.id.lengningqibaojing_wendu_set);
		tv_set_8 = (TextView) view. findViewById(R.id.lengningqistar_wendu_set);
		tv_set_9 = (TextView)view. findViewById(R.id.hot_wendu_set);
		tv_set_10 = (TextView) view. findViewById(R.id.hothuicha_wendu_set);
		
		bnt_set_1 = (Button) view. findViewById(R.id.tv_star_setting);
		bnt_set_2 = (Button) view. findViewById(R.id.zhengfaqi_setting);
		bnt_set_3 = (Button) view. findViewById(R.id.zhileng_wendu_setting);
		bnt_set_4 = (Button) view. findViewById(R.id.zhileng_huicha_setting);
		bnt_set_5 = (Button) view. findViewById(R.id.huashuang_wendu_setting);
		bnt_set_6 = (Button) view. findViewById(R.id.huashuang_time_setting);
		bnt_set_7 = (Button) view. findViewById(R.id.lengningqibaojing_wendu_setting);
		bnt_set_8 = (Button) view. findViewById(R.id.lengningqistar_wendu_setting);
		bnt_set_9 = (Button) view. findViewById(R.id.hot_wendu_setting);
		bnt_set_10 = (Button) view. findViewById(R.id.hothuicha_wendu_setting);
		
	};
	void initData(){
		
		mRefrigerator = _context.mBaseMachineControl.getmRefrigerator();
//		if(mRefrigerator.getRunModel() == ERunModel.加热)
//			mRefrigerator.setRunMode(ERunModel.停机);
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				Message msg = new Message();
				msg.what = 0;
				mHandler.sendMessage(msg);
			}
		}, 7000);
		startBnt.setTag("0");
		stopBnt.setTag("0");
		hot_startBnt.setTag("0");
		
		runModelMap.put("停机", startBnt);
		runModelMap.put("制冷 ", stopBnt);
		runModelMap.put("加热 ", hot_startBnt);
		
		runStateMap.put("停机", tv_1_1);
		runStateMap.put("延时", tv_1_2);
		runStateMap.put("制冷", tv_1_3);
		runStateMap.put("恒温", tv_1_4);
		runStateMap.put("除霜", tv_1_5);
		runStateMap.put("故障", tv_1_6);
		runStateMap.put("加热", tv_1_7);
		
		mTimerTask = new TimerTask() {

			@Override
			public void run() {
				if(mRefrigerator != null){
					runModel = mRefrigerator.getRunModel();//启动
					runState = mRefrigerator.getRunState();//运行
					limitValue = mRefrigerator.getLimitValue();//获取温度限值
					
					nowTemp = mRefrigerator.getTemp();
					mEAlarm = mRefrigerator.getAlarm();
					mEUnitState = mRefrigerator.getUnitState();
					Message msg = new Message();
					msg.what = 1;
					mHandler.sendMessage(msg);
				}
			}
		};
		mTimer.schedule(mTimerTask, 1000, 1000);
		
	}
	void listener(){
		startBnt.setOnClickListener(this);
		hot_startBnt.setOnClickListener(this);
		stopBnt.setOnClickListener(this);
		bnt_left_1.setOnClickListener(this);
		bnt_left_2.setOnClickListener(this);
		bnt_left_3.setOnClickListener(this);
		bnt_left_4.setOnClickListener(this);
		bnt_left_5.setOnClickListener(this);
		bnt_left_6.setOnClickListener(this);
		bnt_left_7.setOnClickListener(this);
		bnt_left_8.setOnClickListener(this);
		bnt_left_9.setOnClickListener(this);
		bnt_left_10.setOnClickListener(this);
		bnt_right_1.setOnClickListener(this);
		bnt_right_2.setOnClickListener(this);
		bnt_right_3.setOnClickListener(this);
		bnt_right_4.setOnClickListener(this);
		bnt_right_5.setOnClickListener(this);
		bnt_right_6.setOnClickListener(this);
		bnt_right_7.setOnClickListener(this);
		bnt_right_8.setOnClickListener(this);
		bnt_right_9.setOnClickListener(this);
		bnt_right_10.setOnClickListener(this);
		bnt_set_1.setOnClickListener(this);
		bnt_set_2.setOnClickListener(this);
		bnt_set_3.setOnClickListener(this);
		bnt_set_4.setOnClickListener(this);
		bnt_set_5.setOnClickListener(this);
		bnt_set_6.setOnClickListener(this);
		bnt_set_7.setOnClickListener(this);
		bnt_set_8.setOnClickListener(this);
		bnt_set_9.setOnClickListener(this);
		bnt_set_10.setOnClickListener(this);
	}
	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.start_bnt:
			if(runModel != null){
				if(startBnt.getTag().toString().equals("1")){
					mRefrigerator.setRunMode(ERunModel.制冷);
					startBnt.setTextColor(Color.RED);
				}
			}
			break;
		case R.id.hot_start_bnt:
			if(runModel != null){
				if(hot_startBnt.getTag().toString().equals("1")){
					mRefrigerator.setRunMode(ERunModel.加热);
					hot_startBnt.setTextColor(Color.RED);
				}
			}
			break;
		case R.id.stop_bnt:
			if(runModel != null){
				if(stopBnt.getTag().toString().equals("1")){
					mRefrigerator.setRunMode(ERunModel.停机);
					stopBnt.setTextColor(Color.RED);
				}
			}
			break;
		case R.id.tv_star_time_left:
			if(Integer.parseInt(tv_set_1.getText().toString()) <= limitValue.get(ECommand.写开机保护时间)[0] )
				return;
			tv_set_1.setText(Integer.parseInt(tv_set_1.getText().toString()) - 1 + "");
			break;
		case R.id.zhengfaqi_time_left:
			if(Integer.parseInt(tv_set_2.getText().toString()) <= limitValue.get(ECommand.写蒸发器风扇延时关时间)[0] )
				return;
			tv_set_2.setText(Integer.parseInt(tv_set_2.getText().toString()) - 1 + "");
			break;
		case R.id.zhileng_wendu_time_left:
			if(Integer.parseInt(tv_set_3.getText().toString()) <= limitValue.get(ECommand.写制冷控制温度)[0] )
				return;
			tv_set_3.setText(Integer.parseInt(tv_set_3.getText().toString()) - 1 + "");
			
			break;
		case R.id.zhileng_huicha_left:
			if(Integer.parseInt(tv_set_4.getText().toString()) <= limitValue.get(ECommand.写制冷控制回差)[0] )
				return;
			tv_set_4.setText(Integer.parseInt(tv_set_4.getText().toString()) - 1 + "");
			
			break;
		case R.id.huashuang_wendu_left:
			if(Integer.parseInt(tv_set_5.getText().toString()) <= limitValue.get(ECommand.写化霜时间)[0] )
				return;
			tv_set_5.setText(Integer.parseInt(tv_set_5.getText().toString()) - 1 + "");
			
			break;
		case R.id.huashuang_time_left:
			if(Integer.parseInt(tv_set_6.getText().toString()) <= limitValue.get(ECommand.写化霜间隔)[0] )
				return;
			tv_set_6.setText(Integer.parseInt(tv_set_6.getText().toString()) - 1 + "");
			
			break;
		case R.id.lengningqibaojing_wendu_left:
			if(Integer.parseInt(tv_set_7.getText().toString()) <= limitValue.get(ECommand.写冷凝器高温报警温度)[0] )
				return;
			tv_set_7.setText(Integer.parseInt(tv_set_7.getText().toString()) - 1 + "");
			
			break;
		case R.id.lengningqistar_wendu_left:
			if(Integer.parseInt(tv_set_8.getText().toString()) <= limitValue.get(ECommand.写冷凝器风扇启动温度)[0] )
				return;
			tv_set_8.setText(Integer.parseInt(tv_set_8.getText().toString()) - 1 + "");
			break;
		case R.id.hot_wendu_left:
			if(Integer.parseInt(tv_set_9.getText().toString()) <= limitValue.get(ECommand.写加热控制温度)[0])
				return;
			tv_set_9.setText(Integer.parseInt(tv_set_9.getText().toString()) - 1 + "");
			break;
		case R.id.hothuicha_wendu_left:
			if(Integer.parseInt(tv_set_10.getText().toString()) <= limitValue.get(ECommand.写加热控制回差)[0])
				return;
			tv_set_10.setText(Integer.parseInt(tv_set_10.getText().toString()) - 1 + "");

			break;
		case R.id.tv_star_time_right:
			if(Integer.parseInt(tv_set_1.getText().toString()) >= limitValue.get(ECommand.写开机保护时间)[1] )
				return;
			tv_set_1.setText(Integer.parseInt(tv_set_1.getText().toString()) + 1 + "");
			break;
		case R.id.zhengfaqi_time_right:
			if(Integer.parseInt(tv_set_2.getText().toString()) >= limitValue.get(ECommand.写蒸发器风扇延时关时间)[1] )
				return;
			tv_set_2.setText(Integer.parseInt(tv_set_2.getText().toString()) + 1 + "");
			break;
		case R.id.zhileng_wendu_time_right:
			if(Integer.parseInt(tv_set_3.getText().toString()) >= limitValue.get(ECommand.写制冷控制温度)[1] )
				return;
			tv_set_3.setText(Integer.parseInt(tv_set_3.getText().toString()) + 1 + "");
			break;
		case R.id.zhileng_huicha_right:
			if(Integer.parseInt(tv_set_4.getText().toString()) >= limitValue.get(ECommand.写制冷控制回差)[1] )
				return;
			tv_set_4.setText(Integer.parseInt(tv_set_4.getText().toString()) + 1 + "");
			break;
		case R.id.huashuang_wendu_right:
			if(Integer.parseInt(tv_set_5.getText().toString()) >= limitValue.get(ECommand.写化霜时间)[1] )
				return;
			tv_set_5.setText(Integer.parseInt(tv_set_5.getText().toString()) + 1 + "");
			break;
		case R.id.huashuang_time_right:
			if(Integer.parseInt(tv_set_6.getText().toString()) >= limitValue.get(ECommand.写化霜间隔)[1] )
				return;
			tv_set_6.setText(Integer.parseInt(tv_set_6.getText().toString()) + 1 + "");
			break;
		case R.id.lengningqibaojing_wendu_right:
			if(Integer.parseInt(tv_set_7.getText().toString()) >= limitValue.get(ECommand.写冷凝器高温报警温度)[1] )
				return;
			tv_set_7.setText(Integer.parseInt(tv_set_7.getText().toString()) + 1 + "");
			break;
		case R.id.lengningqistar_wendu_right:
			if(Integer.parseInt(tv_set_8.getText().toString()) >= limitValue.get(ECommand.写冷凝器风扇启动温度)[1] )
				return;
			tv_set_8.setText(Integer.parseInt(tv_set_8.getText().toString()) + 1 + "");
			break;
		case R.id.hot_wendu_right:
			if(Integer.parseInt(tv_set_9.getText().toString()) >= limitValue.get(ECommand.写加热控制温度)[1])
				return;
			tv_set_9.setText(Integer.parseInt(tv_set_9.getText().toString()) + 1 + "");

			break;
		case R.id.hothuicha_wendu_right:
			if(Integer.parseInt(tv_set_10.getText().toString()) >= limitValue.get(ECommand.写加热控制回差)[1])
				return;
			tv_set_9.setText(Integer.parseInt(tv_set_9.getText().toString()) + 1 + "");
			break;
		case R.id.tv_star_setting:
			mRefrigerator.setPowerProtectionTime(Integer.parseInt(tv_set_1.getText().toString()));
			tv_show_1.setText(tv_set_1.getText().toString());
			break;
		case R.id.zhengfaqi_setting:
			mRefrigerator.setEvaporatorFanOffDelayTime(Integer.parseInt(tv_set_2.getText().toString()));
			tv_show_2.setText(tv_set_2.getText().toString());
			break;
		case R.id.zhileng_wendu_setting:
			mRefrigerator.setRefrigerationTemp(Integer.parseInt(tv_set_3.getText().toString()));
			tv_show_3.setText(tv_set_3.getText().toString());
			break;
		case R.id.zhileng_huicha_setting:
			mRefrigerator.setRefrigerationBacklash(Integer.parseInt(tv_set_4.getText().toString()));
			tv_show_4.setText(tv_set_4.getText().toString());
			break;
		case R.id.huashuang_wendu_setting:
			mRefrigerator.setDefrostTime(Integer.parseInt(tv_set_5.getText().toString()));
			tv_show_5.setText(tv_set_5.getText().toString());
			break;
		case R.id.huashuang_time_setting:
			mRefrigerator.setDefrostInterval(Integer.parseInt(tv_set_6.getText().toString()));
			tv_show_6.setText(tv_set_6.getText().toString());
			break;
		case R.id.lengningqibaojing_wendu_setting:
			mRefrigerator.setCondenserTempAlarmTemp(Integer.parseInt(tv_set_7.getText().toString()));
			tv_show_7.setText(tv_set_7.getText().toString());
			break;
		case R.id.lengningqistar_wendu_setting:
			mRefrigerator.setCondenserFanStartTemp(Integer.parseInt(tv_set_8.getText().toString()));
			tv_show_8.setText(tv_set_8.getText().toString());
			break;
		case R.id.hot_wendu_setting:
			mRefrigerator.setHeatTemp(Integer.parseInt(tv_set_9.getText().toString()));
			tv_show_9.setText(tv_set_9.getText().toString());
			break;
		case R.id.hothuicha_wendu_setting:
			mRefrigerator.setHeatBacklash(Integer.parseInt(tv_set_10.getText().toString()));;
			tv_show_10.setText(tv_set_10.getText().toString());
			break;	
		}
	}
}
