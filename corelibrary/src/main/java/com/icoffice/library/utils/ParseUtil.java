package com.icoffice.library.utils;

import com.icoffice.library.bean.UserBean;
import com.icoffice.library.bean.db.MachineBean;
import com.icoffice.library.bean.network.StatusRegisterBean;
import com.icoffice.library.bean.network.StatusUserBean;

public class ParseUtil {
	public static MachineBean getMachineDbbean(StatusRegisterBean mRegisterStatusBean){
		MachineBean mMachineDbBean = new MachineBean();
		mMachineDbBean.setG_version("1");
		mMachineDbBean.setMt_id("2");
		mMachineDbBean.setM_code(mRegisterStatusBean.getM_code());
		mMachineDbBean.setM_id(Integer.parseInt(mRegisterStatusBean.getM_id()));
		mMachineDbBean.setM_no(mRegisterStatusBean.getM_no());
		mMachineDbBean.setMi_id(Integer.parseInt(mRegisterStatusBean.getMi_id()));
		return mMachineDbBean;
	}
	public static UserBean getUserBean(StatusUserBean mStatusUserBean){
		UserBean mUserBean = new UserBean();
		mUserBean.setU_id(mStatusUserBean.getU_id());
		return mUserBean;
	}
}
