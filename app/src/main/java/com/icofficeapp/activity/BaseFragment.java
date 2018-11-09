package com.icofficeapp.activity;

import android.app.Fragment;
import android.os.Bundle;

import com.icoffice.library.moudle.control.BaseMachineControl;
import com.icofficeapp.control.LYMachineControl;

public class BaseFragment extends Fragment{
//	protected SoldGoodsBean mSoldGoodsBean;
	protected BaseMachineControl mMachineControl;
	public BaseFragment(){
		
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		mMachineControl = LYMachineControl.getInstance(getActivity());
		super.onCreate(savedInstanceState);
	}
//	void setSoldGoodsBean(SoldGoodsBean mSoldGoodsBean){
//		this.mSoldGoodsBean = mSoldGoodsBean;
//	}
}
