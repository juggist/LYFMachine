package com.icoffice.library.moudle.control;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.icoffice.library.handler.BaseCardReaderHandler.CareReaderResult;
import com.icoffice.machine.serial.SerialService;
import com.lf.pos.LFPOSManager;
import com.lf.pos.LFPOS_Ask_Card_Info;
import com.lf.pos.LFPOS_Ask_Pwd_Info;
import com.lf.pos.LFPOS_Aut_Info;
import com.lf.pos.LFPOS_Aut_Param;
import com.lf.pos.LFPOS_Badcard_Info;
import com.lf.pos.LFPOS_Etype;
import com.lf.pos.LFPOS_Event;
import com.lf.pos.LFPOS_Freetrace_Info;
import com.lf.pos.LFPOS_Goodcard_Info;
import com.lf.pos.LFPOS_Init_Param;
import com.lf.pos.LFPOS_Load_Ask_Card_Info;
import com.lf.pos.LFPOS_MifareCard;
import com.lf.pos.LFPOS_Mifare_Result;
import com.lf.pos.LFPOS_Out_State;
import com.lf.pos.LFPOS_Pull_Out_Info;
import com.lf.pos.LFPOS_Query_Result;
import com.lf.pos.LFPOS_RTNHDR;
import com.lf.pos.LFPOS_Read_Cardinfo_Result;
import com.lf.pos.LFPOS_Read_Cardno_Param;
import com.lf.pos.LFPOS_Revert_Result;
import com.lf.pos.LFPOS_Revoke_Param;
import com.lf.pos.LFPOS_Revoke_Result;
import com.lf.pos.LFPOS_Settle_Result;
import com.lf.pos.LFPOS_Signin_Result;
import com.lf.pos.LFPOS_State;
import com.lf.pos.LFPOS_Trade_Ecash_Result;
import com.lf.pos.LFPOS_Trade_Normal_Result;
import com.lf.pos.LFPOS_Trade_Param;
import com.lf.pos.LFPOS_Updata_Info;
import com.lf.pos.LFPOS_Upgrade_Param;
import com.lf.pos.LFPOS_Version_Info;
import com.lf.pos.LFPOS_Version_Param;

public class PosControl {
	
	public static final int POS_PORT_NUM = 3;

	LFPOS_Ask_Card_Info lf_ask_card_info;
	LFPOS_Ask_Pwd_Info lf_ask_pwd_info;
	LFPOS_Aut_Info lf_aut_info;
	LFPOS_Aut_Param lf_aut_param;
	LFPOS_Badcard_Info lf_badcard_info;
	LFPOS_Freetrace_Info lf_freetrace_info;
	LFPOS_Goodcard_Info lf_goodcard_info;
	LFPOS_Init_Param lf_init_param;
	LFPOS_Load_Ask_Card_Info lf_load_ask_card_info;
	LFPOS_Out_State lf_out_state;
	LFPOS_Pull_Out_Info lf_pull_out_info;
	LFPOS_Query_Result lf_query_result;
	LFPOS_Read_Cardinfo_Result lf_read_cardinfo_result;
	LFPOS_Read_Cardno_Param lf_read_cardno_param;
	LFPOS_Revert_Result lf_revert_result;
	LFPOS_Revoke_Param lf_revoke_param;
	LFPOS_Revoke_Result lf_revoke_result;
	LFPOS_RTNHDR lf_rtnhdr;
	LFPOS_Settle_Result lf_settle_result;
	LFPOS_Signin_Result lf_signin_result;
	LFPOS_Trade_Ecash_Result lf_trade_ecase_result;
	LFPOS_Trade_Normal_Result lf_trade_normal_result;
	LFPOS_Trade_Param lf_trade_param;
	LFPOS_Updata_Info lf_updata_info;
	LFPOS_Upgrade_Param lf_upgrade_param;
	LFPOS_Version_Info lf_version_info;
	LFPOS_Version_Param lf_version_param;
	LFPOS_State posstate;
	LFPOS_Etype etype;
	LFPOS_Event posevent;
	LFPOS_MifareCard lf_mifarecard;
	LFPOS_Mifare_Result lf_mifare_result;
	
	private Handler _handler;
	
	static
	{
		System.loadLibrary("lfposjni");
	}
	
	public PosControl()
	{
		lf_ask_card_info = new LFPOS_Ask_Card_Info();
    	lf_ask_pwd_info = new LFPOS_Ask_Pwd_Info();
    	lf_aut_info = new LFPOS_Aut_Info();
    	lf_aut_param = new LFPOS_Aut_Param();
    	lf_badcard_info = new LFPOS_Badcard_Info();
    	lf_freetrace_info = new LFPOS_Freetrace_Info();
    	lf_goodcard_info = new LFPOS_Goodcard_Info();
    	lf_init_param = new LFPOS_Init_Param();
    	lf_load_ask_card_info = new LFPOS_Load_Ask_Card_Info();
    	lf_out_state = new LFPOS_Out_State();
    	lf_pull_out_info = new LFPOS_Pull_Out_Info();
    	lf_query_result = new LFPOS_Query_Result();
    	lf_read_cardinfo_result = new LFPOS_Read_Cardinfo_Result();
    	lf_read_cardno_param = new LFPOS_Read_Cardno_Param();
    	lf_revert_result = new LFPOS_Revert_Result();
    	lf_revoke_param = new LFPOS_Revoke_Param();
    	lf_revoke_result = new LFPOS_Revoke_Result();
    	lf_rtnhdr = new LFPOS_RTNHDR();
    	lf_settle_result = new LFPOS_Settle_Result();
    	lf_signin_result = new LFPOS_Signin_Result();
    	lf_trade_ecase_result = new LFPOS_Trade_Ecash_Result();
    	lf_trade_normal_result = new LFPOS_Trade_Normal_Result();
    	lf_trade_param = new LFPOS_Trade_Param();
    	lf_updata_info = new LFPOS_Updata_Info();
    	lf_upgrade_param = new LFPOS_Upgrade_Param();
    	lf_version_info = new LFPOS_Version_Info();
    	lf_version_param = new LFPOS_Version_Param();
    	posstate = new LFPOS_State();
    	etype = new LFPOS_Etype();
    	posevent = new LFPOS_Event();
    	lf_mifarecard = new LFPOS_MifareCard();
    	lf_mifare_result = new LFPOS_Mifare_Result();
    	
        LFPOSManager.getInstance().setListener(new LFPOSManager.ResultListener() {
			
			@Override
			public int onTradeResult(int event, int errorCode, String message) {
				//回调函数，处理event对应的事件
				Log.i("pos",event + "");
				String strlcdinfo = "未知event";

				Message msg = new Message();
				msg.what = CareReaderResult.Unknown.ordinal();
				msg.obj = strlcdinfo;
				
				if(event == posevent.LFPOS_EVT_GLITCH)
				{
					strlcdinfo = "POS有故障!"
								+"\n错误码: "+errorCode
								+"\n描述信息: "+message;

					msg.what = CareReaderResult.Fail.ordinal();
					msg.obj = strlcdinfo;
				}
				else if(event == posevent.LFPOS_EVT_ASK_CARD)
				{
					strlcdinfo = "读卡中,请稍后...";

					msg.what = CareReaderResult.Reading.ordinal();
					msg.obj = strlcdinfo;
				}
				else if(event == posevent.LFPOS_EVT_BADCARD)
				{
					strlcdinfo = "读卡失败"
								+"\n错误码: "+errorCode
								+"\n描述信息: "+message;

					msg.what = CareReaderResult.Fail.ordinal();
					msg.obj = strlcdinfo;
				}
				else if (event == posevent.LFPOS_EVT_M1_READSECTOR)
				{
					if(errorCode == 0)
					{
						try
						{
							lf_mifare_result.getNativeObject();
		 					
							String sector = lf_mifare_result.getreadsector();
							String card_no = sector.substring(6,8)
									+sector.substring(4,6)
									+sector.substring(2,4)
									+sector.substring(0,2);
							long no = Long.parseLong(card_no, 16);
							card_no = String.format("%010d", no);

							strlcdinfo = "读M1卡成功"
									+"\n卡信息: "+sector
									+"\n读卡长度: "+lf_mifare_result.getSectorReadLen()
									+"\n卡号: "+card_no;
							Log.i("pos","cardNo = " + strlcdinfo);

							msg.what = CareReaderResult.Success.ordinal();
							msg.obj = card_no;
						}
						catch (Exception e)
						{
							Log.i("pos","cardNo = " + e.toString());
							e.printStackTrace();

							msg.what = CareReaderResult.Fail.ordinal();
							msg.obj = e.toString();
						}
					}
					else
					{
						strlcdinfo = "读M1卡失败!"
								+"\n错误码: "+errorCode
								+"\n描述信息: "+message;
						Log.i("pos","cardNo = " + strlcdinfo);

						msg.what = CareReaderResult.Fail.ordinal();
						msg.obj = strlcdinfo;
					}
				}

				_handler.sendMessage(msg);
				return 0;
			}
		});

        // 用于实时检测回调的线程
	    new Thread (new Runnable() {
    		public void run() {
    			LFPOSManager.getInstance().threadloop();
    		}
    	}).start();
			
		///////////////////////POS状态检测///////////////////
		new Thread (new Runnable() {
			int pos_state = 0;
			int old_pos_state = 0;
			public void run() {
				int detec_pos_state_exit = 0;
				while(detec_pos_state_exit == 0)
				{
					LFPOSManager.getInstance().getstatePos(lf_out_state);
					pos_state = lf_out_state.getsta();
					
					if(pos_state == LFPOS_State.LFPOS_S_UNK)
					{
						/*等待初始化*/;
						old_pos_state = LFPOS_State.LFPOS_S_UNK;
					}
					else if(pos_state == LFPOS_State.LFPOS_S_INIT)
					{
						/*正在初始化*/;
						
						old_pos_state = LFPOS_State.LFPOS_S_INIT;
					}
					else if(pos_state == LFPOS_State.LFPOS_S_IDLE)
					{
						/*空闲*/;
				      if(old_pos_state == LFPOS_State.LFPOS_S_INIT)
				      {
				      		/*初始化成功*/;
				      }
				      
						old_pos_state = LFPOS_State.LFPOS_S_IDLE;
					}
					else if(pos_state == LFPOS_State.LFPOS_S_BUSY)
					{
						/*BUSY*/;
						old_pos_state = LFPOS_State.LFPOS_S_BUSY;
					}
					
					try
					{
					     Thread.sleep(100);
					}
					catch (InterruptedException e) {
						    e.printStackTrace();
					}
				}
			}
		}).start();
		
		
		//getPosState();
	}

	public int getPosState()
	{
	    // 获取pos机状态,参考LFPOS_Out_State类
        LFPOSManager.getInstance().getstatePos(lf_out_state);
        String strInfo = "";
        int statFlag = 0;
        switch (lf_out_state.getsta()) {
        case LFPOS_State.LFPOS_S_UNK:
        	strInfo = "POS状态未知！！！";
        	statFlag = LFPOS_State.LFPOS_S_UNK;
        	break;
        case LFPOS_State.LFPOS_S_INIT:
        	strInfo = "POS状态：初始化";
        	statFlag = LFPOS_State.LFPOS_S_INIT;
        	break;
        case LFPOS_State.LFPOS_S_MGMT:
        	strInfo =  "POS状态：管理模式";
        	statFlag = LFPOS_State.LFPOS_S_MGMT;
        	break;
        case LFPOS_State.LFPOS_S_IDLE:
        	strInfo = "POS状态：正常（空闲）";
        	statFlag = LFPOS_State.LFPOS_S_IDLE;
        	break;
        case LFPOS_State.LFPOS_S_BUSY:
        	strInfo = "POS状态：正常（忙碌："+lf_out_state.getop()+"）";
        	statFlag = LFPOS_State.LFPOS_S_BUSY;
        	break;
        }
        return statFlag;
	}
	
	public int initPos(final String strIp, final int port)
	{
		lf_init_param.setUnionpayPort(port);
        lf_init_param.setDiagnosis(1);
        lf_init_param.setCommType(0);
        lf_init_param.setCommPort(POS_PORT_NUM);
        lf_init_param.setLinkMode(0);
        lf_init_param.setTimeoutCard(30);
        lf_init_param.setTimeoutPwd(30);
        lf_init_param.setUnionpayIp(strIp);
        lf_init_param.setManager(0);
        lf_init_param.setPrintMode(0);
        lf_init_param.setLowBalance(0);
        lf_init_param.setSettleMode(0);
        lf_init_param.setSigninMode(0);
        
		int ret = LFPOSManager.getInstance().initPos(lf_init_param);
		return ret;
	}

	public int signinPos()
	{
		int ret = LFPOSManager.getInstance().signinPos();
		return ret;
	}
	
	public int readCardNo(Handler handler)
	{
		_handler = handler;
		lf_mifarecard.setsectorindex(0);
		lf_mifarecard.setblockindex(0);
		String key = "ffffffffffff";
		lf_mifarecard.setsectorkey(key);
		lf_mifarecard.setwritesector("");
		int ret = LFPOSManager.getInstance().readsectorPos(lf_mifarecard);
		return ret;
	}
	
	public int cancelPos()
	{
//		int ret = LFPOSManager.getInstance().cancelPos();
		SerialService service = new SerialService();
		int ret = service.cancelLfPos(POS_PORT_NUM);
		return ret;
	}
	
	public int resetPos()
	{
		//int ret = LFPOSManager.getInstance().resetPos();
		SerialService service = new SerialService();
		int ret = service.resetLfPos(POS_PORT_NUM);
		return ret;
	}
}
