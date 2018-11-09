package com.icoffice.library.callback;
/**
 * 补货回调接口
 * @author lufeisong
 *
 */
public interface WayAndGoodsCallBack {
	public void success(String msg);
	public void fail(String msg);
	public void connectFail(String msg);
}
