package com.icoffice.library.backactivity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.icoffice.library.wheel.NumericWheelAdapter;
import com.icoffice.library.wheel.OnWheelChangedListener;
import com.icoffice.library.wheel.WheelView;
import com.icofficeapp.R;

/**
 * 机器状态
 * 
 * @author lufeisong
 * 
 */
@SuppressLint("ValidFragment")
public class MachineFrgment extends Fragment implements OnClickListener{
	private BaseFragmentActivity mContext;
	private CheckBox rb_coinAndnote,rb_physicsButton,rb_elevator,rb_door;
	private Button btn_commit;
	private WheelView mWheelView;
	
	private String coinAndnoteStatus = "1";
	private String coinWaitTime = "0";
	private String physicsButtonStatus = "1";
	
	private String doorStatus = "1";
	private String elevatorStatus = "1";
	
	private ArrayList<String> list = new ArrayList<String>();//易触机器设置
	private ArrayList<Integer> ly_list = new ArrayList<Integer>();//雷云峰机器设置
	public MachineFrgment(BaseFragmentActivity mContext) {
		this.mContext = mContext;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.library_machine, null);
		initView(view);
		initListener();
		initData();
		return view;
	}

	void initView(View view) {
		rb_coinAndnote = (CheckBox) view.findViewById(R.id.library_choose_coinAndnote);
		rb_physicsButton = (CheckBox) view.findViewById(R.id.library_choose_physicsbutton);
		rb_elevator = (CheckBox) view.findViewById(R.id.library_choose_elevator);
		rb_door = (CheckBox) view.findViewById(R.id.library_choose_door);
		btn_commit = (Button) view.findViewById(R.id.library_machine_commit);
		mWheelView = (WheelView) view.findViewById(R.id.library_machine_sec);
	}

	void initListener() {
		rb_coinAndnote.setOnCheckedChangeListener(listener);
		rb_physicsButton.setOnCheckedChangeListener(listener);
		rb_elevator.setOnCheckedChangeListener(listener);
		rb_door.setOnCheckedChangeListener(listener);
		btn_commit.setOnClickListener(this);
	}

	void initData() {
		list.addAll(mContext.mBaseMachineControl.getMachineSetStatus());
		ly_list.addAll(mContext.mBaseMachineControl.getLYMachineSetStatus());
		
		mWheelView.setAdapter(new NumericWheelAdapter(10, 60, "%02d"));
		mWheelView.setLabel("sec");
		mWheelView.setCyclic(true);
		coinWaitTime = list.get(1);
		mWheelView.setCurrentItem(Integer.parseInt(list.get(1)) - 10);
		addChangingListener(mWheelView, "sec");
		
		if(list.get(0).equals("0")){
			rb_coinAndnote.setChecked(false);
		}else if(list.get(0).equals("1")){
			rb_coinAndnote.setChecked(true);
		}
		if(list.get(2).equals("0")){
			rb_physicsButton.setChecked(false);
		}else if(list.get(2).equals("1")){
			rb_physicsButton.setChecked(true);
		}
		
		if(ly_list.get(0) == 0){
			rb_door.setChecked(false);
		}else if(ly_list.get(0) == 1){
			rb_door.setChecked(true);
		}
		if(ly_list.get(1) == 0){
			rb_elevator.setChecked(false);
		}else if(ly_list.get(1) == 1){
			rb_elevator.setChecked(true);
		}
	}

	private OnCheckedChangeListener listener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if(buttonView.getId() == rb_coinAndnote.getId()){
				
			}
		}
	};
	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.library_machine_commit:
			if(rb_coinAndnote.isChecked()){
				coinAndnoteStatus = "1";
			}else{
				coinAndnoteStatus = "0";
			}
			if(rb_physicsButton.isChecked()){
				physicsButtonStatus = "1";
			}else{
				physicsButtonStatus = "0";
			}
			if(rb_door.isChecked()){
				doorStatus = "1";
			}else{
				doorStatus = "0";
			}
			if(rb_elevator.isChecked()){
				elevatorStatus = "1";
			}else{
				elevatorStatus = "0";
			}
			break;
		}
		mContext.mApplication.setCoinAndnoteStatus(coinAndnoteStatus);
		mContext.mApplication.setCoinWaitTime(coinWaitTime);
		mContext.mApplication.setPhysicsButtonStatus(physicsButtonStatus);
		mContext.mBaseMachineControl.updateCoinAndnoteStatus(coinAndnoteStatus);
		mContext.mBaseMachineControl.updateCoinWaitTime(coinWaitTime);
		mContext.mBaseMachineControl.updatePhysicsButtonStatus(physicsButtonStatus);
		mContext.mBaseMachineControl.updateDoorStatus(doorStatus);
		mContext.mBaseMachineControl.updateElevatorStatus(elevatorStatus);
		mContext.commentToast.show("修改信息成功");
	}
	
	/**
	 * Adds changing listener for wheel that updates the wheel label
	 * @param wheel the wheel
	 * @param label the wheel label
	 */
	private void addChangingListener(final WheelView wheel, final String label) {
		wheel.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				wheel.setLabel(newValue != 1 ? label : label);
				coinWaitTime = newValue + 10 + "";
			}
		});
	}
}
