package com.icoffice.library.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UserBean implements Serializable {
	private String u_id = "";// 用户id
	private String u_password = "";// 用户id

	public String getU_password() {
		return u_password;
	}

	public void setU_password(String u_password) {
		this.u_password = u_password;
	}

	public String getU_id() {
		return u_id;
	}

	public void setU_id(String u_id) {
		this.u_id = u_id;
	}

}
