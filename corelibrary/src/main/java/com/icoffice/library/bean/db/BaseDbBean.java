package com.icoffice.library.bean.db;

import java.io.Serializable;

@SuppressWarnings("serial")
public class BaseDbBean implements Serializable {
	private Integer id;// 默认第一个为自增主键

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
