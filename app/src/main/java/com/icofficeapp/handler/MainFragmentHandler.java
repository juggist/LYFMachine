package com.icofficeapp.handler;

import android.os.Bundle;
import android.os.Handler;

public class MainFragmentHandler extends Handler{
	protected Bundle bundle = new Bundle();
	public static final String TASK_TIMER = "task_timer";
	
	public static final int INIT_FLAG = 0;//初始化
	public static final int SHOP_DETAIL_FLAG = 1;//回到购买主界面
	public static final int TASK_FLAG = 2;//计时器
	public static final int REFRESH_FLAG = 4;//刷新数据
}
