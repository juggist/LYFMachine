package com.icoffice.library.callback;

import com.icoffice.library.bean.network.StatusTemplateBean;

/**
 * 获取模板接口 回调
 * @author lufeisong
 *
 */
public interface TemplateCallBack {
	public void TemplateSuccess(StatusTemplateBean statusTemplateBean);
	public void TemplateFail(StatusTemplateBean statusTemplateBean);
	public void TemplateConnectFail(String e);
}
