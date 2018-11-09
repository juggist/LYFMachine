package com.icoffice.library.utils.net;

public interface RequestResultCallback {
	public void onSuccess(Object o);
	public void onFail(Exception e);
}
