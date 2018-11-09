package com.icoffice.library.bean.db;

@SuppressWarnings("serial")
public class MachineBean extends BaseDbBean {
	private Integer mi_id;// 机器id
	private String m_code;// 机器code
	private String mt_id;// 机器出厂类型(综合机器)
	private String m_no;
	private Integer m_id;
	private String g_version;// 货品版本

	public String getM_code() {
		return m_code;
	}

	public void setM_code(String m_code) {
		this.m_code = m_code;
	}

	public String getMt_id() {
		return mt_id;
	}

	public void setMt_id(String mt_id) {
		this.mt_id = mt_id;
	}

	public int getMi_id() {
		return mi_id;
	}

	public void setMi_id(int mi_id) {
		this.mi_id = mi_id;
	}

	public String getG_version() {
		return g_version;
	}

	public void setG_version(String g_version) {
		this.g_version = g_version;
	}

	public String getM_no() {
		return m_no;
	}

	public void setM_no(String m_no) {
		this.m_no = m_no;
	}

	public Integer getM_id() {
		return m_id;
	}

	public void setM_id(Integer m_id) {
		this.m_id = m_id;
	}

	public void setMi_id(Integer mi_id) {
		this.mi_id = mi_id;
	}

	
}
