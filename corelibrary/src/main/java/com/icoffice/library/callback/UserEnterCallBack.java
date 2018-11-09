package com.icoffice.library.callback;

import com.icoffice.library.bean.network.StatusUserBean;

public interface UserEnterCallBack {
	public void success(StatusUserBean statusUserBean);
	public void fail(String msg);
}
