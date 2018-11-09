package com.icoffice.library.bean.db;

import com.icoffice.library.utils.CommonUtils;


@SuppressWarnings("serial")
public class OrderBean extends BaseDbBean {
	private String client_order_no;// 本地订单号
	private String o_number;// 服务器订单号
	private String g_code;// 货品code
	private Integer o_amount;// 购买数量
	private Integer o_unit_price;// 单价
	private Integer total_price;// 总价
	private Integer pay_type;// 支付类型
	private String o_generate_time;// 客户端订单生成时间
	private String o_complete_time;// 客户端出货时间
	private Integer o_state;// 出货状态 (-1：暂未出货 1：出货成功 2：出货失败)
	private Integer o_pay_state;// 支付状态 (0：未支付 1：支付)
	private Integer p_state;// 订单提交服务器状态 (0：未提交 1：提交)
	private String w_code;//货道编号
	private Integer cashIn;//投币金额
	private Integer cashOut;//退币金额
	private String expand;//拓展字段
	public String getClient_order_no() {
		return client_order_no;
	}

	public void setClient_order_no(String client_order_no) {
		this.client_order_no = client_order_no;
		CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"修改订单对象client_order_no = " + client_order_no);
	}

	public String getO_number() {
		return o_number;
	}

	public void setO_number(String o_number) {
		this.o_number = o_number;
		CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"修改订单对象o_number = " + o_number);
	}

	public String getG_code() {
		return g_code;
	}

	public void setG_code(String g_code) {
		this.g_code = g_code;
		CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"修改订单对象g_code = " + g_code);
	}

	public Integer getO_amount() {
		return o_amount;
	}

	public void setO_amount(Integer o_amount) {
		this.o_amount = o_amount;
		CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"修改订单对象o_amount = " + o_amount);
	}

	public Integer getO_unit_price() {
		return o_unit_price;
	}

	public void setO_unit_price(Integer o_unit_price) {
		this.o_unit_price = o_unit_price;
		CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"修改订单对象o_unit_price = " + o_unit_price);
	}

	public Integer getTotal_price() {
		return total_price;
	}

	public void setTotal_price(Integer total_price) {
		this.total_price = total_price;
		CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"修改订单对象total_price = " + total_price);
	}

	public Integer getPay_type() {
		return pay_type;
	}

	public void setPay_type(Integer pay_type) {
		this.pay_type = pay_type;
		CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"修改订单对象pay_type = " + pay_type);
	}

	public String getO_generate_time() {
		return o_generate_time;
	}

	public void setO_generate_time(String o_generate_time) {
		this.o_generate_time = o_generate_time;
		CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"修改订单对象o_generate_time = " + o_generate_time);
	}

	public String getO_complete_time() {
		return o_complete_time;
	}

	public void setO_complete_time(String o_complete_time) {
		this.o_complete_time = o_complete_time;
		CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"修改订单对象o_complete_time = " + o_complete_time);
	}

	public Integer getO_state() {
		return o_state;
	}

	public void setO_state(Integer o_state) {
		this.o_state = o_state;
		CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"修改订单对象o_state = " + o_state);
	}

	public Integer getO_pay_state() {
		return o_pay_state;
	}

	public void setO_pay_state(Integer o_pay_state) {
		this.o_pay_state = o_pay_state;
		CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"修改订单对象o_pay_state = " + o_pay_state);
	}

	public Integer getP_state() {
		return p_state;
	}

	public void setP_state(Integer p_state) {
		this.p_state = p_state;
		CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"修改订单对象p_state = " + p_state);
	}

	public String getW_code() {
		return w_code;
	}

	public void setW_code(String w_code) {
		this.w_code = w_code;
		CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"修改订单对象w_code = " + w_code);
	}

	public Integer getCashIn() {
		return cashIn;
	}

	public void setCashIn(Integer cashIn) {
		this.cashIn = cashIn;
		CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"修改订单对象cashIn = " + cashIn);
	}

	public Integer getCashOut() {
		return cashOut;
	}

	public void setCashOut(Integer cashOut) {
		this.cashOut = cashOut;
		CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"修改订单对象cashOut = " + cashOut);
	}

	public String getExpand() {
		return expand;
	}

	public void setExpand(String expand) {
		this.expand = expand;
	}

		
}
