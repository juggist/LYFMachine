package com.icoffice.library.utils;
/**
 * 网络请求解析
 */
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.icoffice.library.bean.GoodsSortBean;
import com.icoffice.library.bean.db.AdStatusBean;
import com.icoffice.library.bean.db.GoodsBean;
import com.icoffice.library.bean.network.GoodsAllBean;
import com.icoffice.library.bean.network.StatusAliPayStateBean;
import com.icoffice.library.bean.network.StatusAlipaySoniceBean;
import com.icoffice.library.bean.network.StatusExchangeOrder;
import com.icoffice.library.bean.network.StatusLocalOrder;
import com.icoffice.library.bean.network.StatusNetOrder;
import com.icoffice.library.bean.network.StatusWeChatStateBean;

public class ParseICEJSON {
	
	/*
	 * 解析所有商品的信息
	 * 按分类保存
	 */
	public static GoodsAllBean parseGoodListAll(JSONArray array,String mt_timestamp){
		GoodsAllBean mGoodsAllBean = new GoodsAllBean();
		try {
			ArrayList<GoodsSortBean> goodsListAllBean = new ArrayList<GoodsSortBean>();
			for(int i = 0;i < array.length();i++){
				GoodsSortBean mGoodsSortBean = new GoodsSortBean();
				JSONObject itemObj = array.getJSONObject(i);
				String gt_id = itemObj.getString("id");
				String gt_name = itemObj.getString("name");
				String gt_ename = itemObj.getString("eName");
				JSONArray itemArray = itemObj.getJSONArray("children");
				ArrayList<GoodsBean> goodsBean = new ArrayList<GoodsBean>();
				for(int j = 0;j < itemArray.length();j++){
					GoodsBean mGoodsBean = new GoodsBean();
					JSONObject goodsItemObj = itemArray.getJSONObject(j);
					mGoodsBean.setG_code(goodsItemObj.getString("code"));
					mGoodsBean.setG_name(goodsItemObj.getString("name"));
					mGoodsBean.setG_ename(goodsItemObj.getString("eName"));
					mGoodsBean.setUnit_price(goodsItemObj.getString("unitPrice"));
					mGoodsBean.setG_note(goodsItemObj.getString("note"));
					mGoodsBean.setZ_image(goodsItemObj.getString("zImage"));
					mGoodsBean.setZ_detail_image(goodsItemObj.getString("zDetailImage"));
					mGoodsBean.setY_gif(goodsItemObj.getString("yGif"));
					mGoodsBean.setY_detail_image(goodsItemObj.getString("yDetailImage"));
					mGoodsBean.setGt_id(goodsItemObj.getString("gtId"));
					mGoodsBean.setG_origin(goodsItemObj.getString("origin"));
					mGoodsBean.setG_brand(goodsItemObj.getString("brand"));
					mGoodsBean.setG_taste(goodsItemObj.getString("taste"));
					mGoodsBean.setG_specification(goodsItemObj.getString("specification"));
					mGoodsBean.setG_other(goodsItemObj.getString("other"));
					mGoodsBean.setG_pack(goodsItemObj.getString("pack"));
					goodsBean.add(mGoodsBean);
				}
				mGoodsSortBean.setGoodsBean(goodsBean);
				mGoodsSortBean.setGt_id(gt_id);
				mGoodsSortBean.setGt_name(gt_name);
				mGoodsSortBean.setGt_ename(gt_ename);
				goodsListAllBean.add(mGoodsSortBean);
			}
			mGoodsAllBean.setGoodsListAllBean(goodsListAllBean);
			mGoodsAllBean.setVersion(mt_timestamp);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mGoodsAllBean;
	}
	/**
	 * 解析 线上出货成功订单
	 * @param map
	 * @return
	 */
	public static StatusNetOrder parseStatusNetOrder(HashMap<String, String> map){
		StatusNetOrder mStatusNetOrder = new StatusNetOrder();
		mStatusNetOrder.setMsg(map.get("msg"));
		mStatusNetOrder.setStatus(map.get("status"));
		mStatusNetOrder.setO_count(map.get("o_count"));
		String successarr = map.get("successarr");
		ArrayList<String> list = new ArrayList<String>();
		try {
			JSONArray array = new JSONArray(successarr);
			for(int i = 0;i < array.length();i++){
				JSONObject item = array.getJSONObject(i);
				String clientOrderNo = item.getString("clientOrderNo");
				list.add(clientOrderNo);
			}
			mStatusNetOrder.getSuccessarr().addAll(list);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mStatusNetOrder;
	}
	/**
	 * 解析 线下出货成功订单
	 * @param map
	 * @return
	 */
	public static StatusLocalOrder parseStatusLocalOrder(HashMap<String, String> map){
		StatusLocalOrder statusNetOrder = new StatusLocalOrder();
		statusNetOrder.setMsg(map.get("msg"));
		statusNetOrder.setStatus(map.get("status"));
		statusNetOrder.setO_count(map.get("o_count"));
		String successarr = map.get("successarr");
		ArrayList<String> list = new ArrayList<String>();
		try {
			JSONArray array = new JSONArray(successarr);
			for(int i = 0;i < array.length();i++){
				JSONObject item = array.getJSONObject(i);
				String clientOrderNo = item.getString("clientOrderNo");
				list.add(clientOrderNo);
			}
			statusNetOrder.getSuccessarr().addAll(list);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return statusNetOrder;
	}
	/**
	 * 解析 微商城兑换出货成功订单
	 * @param map
	 * @return
	 */
	public static StatusExchangeOrder parseStatusExchangeOrder(HashMap<String, String> map){
		StatusExchangeOrder statusExchangeOrder = new StatusExchangeOrder();
		statusExchangeOrder.setMsg(map.get("msg"));
		statusExchangeOrder.setStatus(map.get("status"));
		statusExchangeOrder.setO_count(map.get("o_count"));
		String successarr = map.get("successarr");
		ArrayList<String> list = new ArrayList<String>();
		try {
			JSONArray array = new JSONArray(successarr);
			for(int i = 0;i < array.length();i++){
				JSONObject item = array.getJSONObject(i);
				String clientOrderNo = item.getString("clientOrderNo");
				list.add(clientOrderNo);
			}
			statusExchangeOrder.getSuccessarr().addAll(list);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return statusExchangeOrder;
	}
	/**
	 * 解析微信支付状态
	 */
	public static StatusWeChatStateBean getWeChatState(HashMap<String,String> map){
		StatusWeChatStateBean mStatusWeChatStateBean = new StatusWeChatStateBean();
		mStatusWeChatStateBean.setStatus(map.get("status"));
		mStatusWeChatStateBean.setMsg(map.get("msg"));
		mStatusWeChatStateBean.setClientOrderNo(map.get("clientOrderNo"));
		mStatusWeChatStateBean.setNumber(map.get("number"));
		return mStatusWeChatStateBean;
	}
	/**
	 * 解析alipay声波
	 * @param result
	 * @return
	 */
	public static StatusAlipaySoniceBean getAlipaySoniceBean(HashMap<String,String> map){
		StatusAlipaySoniceBean mAlipayBean = new StatusAlipaySoniceBean();
		mAlipayBean.setStatus(map.get("status"));
		mAlipayBean.setMsg(map.get("msg"));
		mAlipayBean.setResult_code(map.get("result_code"));
		mAlipayBean.setNumber(map.get("number"));
		mAlipayBean.setClientOrderNo(map.get("clientOrderNo"));
		return mAlipayBean;
		
	}
	/**
	 * 解析支付宝轮询结果
	 */
	public static StatusAliPayStateBean getStatusAliPayStateBean(HashMap<String,String> map){
		StatusAliPayStateBean mStatusAliPayStateBean = new StatusAliPayStateBean();
			mStatusAliPayStateBean.setStatus(map.get("status"));
			mStatusAliPayStateBean.setMsg(map.get("msg"));
			mStatusAliPayStateBean.setClientOrderNo(map.get("clientOrderNo"));
			mStatusAliPayStateBean.setNumber(map.get("number"));
		return mStatusAliPayStateBean;
	}
	/**
	 * 解析广告结果
	 * @param array
	 * @return
	 */
	public static ArrayList<AdStatusBean> parseAdStatusBeanList(JSONArray array){
		ArrayList<AdStatusBean> adStatusBean_list = new ArrayList<AdStatusBean>();
		for(int i = 0;i < array.length();i++){
			AdStatusBean mAdStatusBean = new AdStatusBean();
			try {
				JSONObject obj = array.getJSONObject(i);
				mAdStatusBean.setAid(obj.getInt("id"));
				mAdStatusBean.setAtId(obj.getInt("atId"));
				mAdStatusBean.setEndDate(obj.getString("endDate"));
				mAdStatusBean.setFile(obj.getString("file"));
				mAdStatusBean.setgCode(obj.getString("gCode"));
				mAdStatusBean.setInterval(obj.getInt("interval"));
				mAdStatusBean.setSmallImage(obj.getString("smallImage"));
				mAdStatusBean.setStartDate(obj.getString("startDate"));
				mAdStatusBean.setFileMd5(obj.getString("fileMd5"));
				mAdStatusBean.setSmallImageMd5(obj.getString("smallImageMd5"));
				mAdStatusBean.setDownload(0);
			} catch (JSONException e) {
				CommonUtils.showLog(CommonUtils.ICE_TAG, "parseAdStatusBeanList Exception " + e.toString());
				e.printStackTrace();
			}
			adStatusBean_list.add(mAdStatusBean);
		}
		
		return adStatusBean_list;
	}
}
