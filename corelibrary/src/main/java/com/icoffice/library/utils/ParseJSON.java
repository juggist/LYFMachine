package com.icoffice.library.utils;
/**
 * 网络请求解析
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.icoffice.library.bean.GoodsSortBean;
import com.icoffice.library.bean.db.GoodsBean;
import com.icoffice.library.bean.db.GoodsStatusBean;
import com.icoffice.library.bean.db.RcodeBean;
import com.icoffice.library.bean.db.RecoverAssistWayBean;
import com.icoffice.library.bean.db.WayBean;
import com.icoffice.library.bean.network.GoodsAllBean;
import com.icoffice.library.bean.network.StatusAddChangeLogStatus;
import com.icoffice.library.bean.network.StatusCheckRcode;
import com.icoffice.library.bean.network.StatusGoodsStatus;
import com.icoffice.library.bean.network.StatusQuitConvert;
import com.icoffice.library.bean.network.StatusRegisterBean;
import com.icoffice.library.bean.network.StatusTemplateBean;
import com.icoffice.library.bean.network.StatusUpdateBean;
import com.icoffice.library.bean.network.StatusUserBean;
import com.icoffice.library.bean.network.StatusWayAndGoodsBean;
import com.icoffice.library.bean.network.StatusWayBean;

public class ParseJSON {
	/*
	 * 解析所有商品的信息
	 * 按分类保存
	 */
	public static GoodsAllBean parseGoodListAll(JSONObject object){
		GoodsAllBean mGoodsAllBean = new GoodsAllBean();
		try {
			JSONArray array = object.getJSONArray("list");
			ArrayList<GoodsSortBean> goodsListAllBean = new ArrayList<GoodsSortBean>();
			for(int i = 0;i < array.length();i++){
				GoodsSortBean mGoodsSortBean = new GoodsSortBean();
				JSONObject itemObj = array.getJSONObject(i);
				String gt_id = itemObj.getString("gt_id");
				String gt_name = itemObj.getString("gt_name");
				String gt_ename = itemObj.getString("gt_ename");
				JSONArray itemArray = itemObj.getJSONArray("children");
				ArrayList<GoodsBean> goodsBean = new ArrayList<GoodsBean>();
				for(int j = 0;j < itemArray.length();j++){
					GoodsBean mGoodsBean = new GoodsBean();
					JSONObject goodsItemObj = itemArray.getJSONObject(j);
					mGoodsBean.setG_code(goodsItemObj.getString("g_code"));
					mGoodsBean.setG_name(goodsItemObj.getString("g_name"));
					mGoodsBean.setG_ename(goodsItemObj.getString("g_ename"));
					mGoodsBean.setUnit_price(goodsItemObj.getString("unit_price"));
					mGoodsBean.setG_note(goodsItemObj.getString("g_note"));
					mGoodsBean.setZ_image(goodsItemObj.getString("z_image"));
					mGoodsBean.setZ_detail_image(goodsItemObj.getString("z_detail_image"));
					mGoodsBean.setY_gif(goodsItemObj.getString("y_gif"));
					mGoodsBean.setY_detail_image(goodsItemObj.getString("y_detail_image"));
					mGoodsBean.setGt_id(goodsItemObj.getString("gt_id"));
					mGoodsBean.setG_origin(goodsItemObj.getString("g_origin"));
					mGoodsBean.setG_brand(goodsItemObj.getString("g_brand"));
					mGoodsBean.setG_taste(goodsItemObj.getString("g_taste"));
					mGoodsBean.setG_specification(goodsItemObj.getString("g_specification"));
					mGoodsBean.setG_other(goodsItemObj.getString("g_other"));
					mGoodsBean.setG_pack(goodsItemObj.getString("g_pack"));
					goodsBean.add(mGoodsBean);
				}
				mGoodsSortBean.setGoodsBean(goodsBean);
				mGoodsSortBean.setGt_id(gt_id);
				mGoodsSortBean.setGt_name(gt_name);
				mGoodsSortBean.setGt_ename(gt_ename);
				goodsListAllBean.add(mGoodsSortBean);
			}
			mGoodsAllBean.setGoodsListAllBean(goodsListAllBean);
			mGoodsAllBean.setVersion(object.getString("mt_timestamp"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mGoodsAllBean;
	}
	

	/**
	 * 解析apk更新接口
	 * @param result
	 * @return
	 */
	public static StatusUpdateBean getStatusUpdateBean(String result){
		StatusUpdateBean mStatusUpdateBean = new StatusUpdateBean();
		try {
			JSONObject object = new JSONObject(result);
			mStatusUpdateBean.setStatus(object.getString("status"));
			mStatusUpdateBean.setMsg(object.getString("msg"));
			mStatusUpdateBean.setUrl(object.getString("url"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mStatusUpdateBean;
	}
	/**
	 * 解析注册
	 * @param result
	 * @return
	 */
	public static StatusRegisterBean parseRegister(String result){
		StatusRegisterBean mRegisterStatusBean = new StatusRegisterBean();
		try {
			JSONObject object = new JSONObject(result);
			String status = object.getString("status");
			String msg = object.getString("msg");
			String m_id = object.getString("m_id");
			String m_code = object.getString("m_code");
			String mi_id = object.getString("mi_id");
			String m_no = object.getString("m_no");
			mRegisterStatusBean.setStatus(status);
			mRegisterStatusBean.setMsg(msg);
			mRegisterStatusBean.setM_id(m_id);
			mRegisterStatusBean.setM_code(m_code);
			mRegisterStatusBean.setMi_id(mi_id);
			mRegisterStatusBean.setM_no(m_no);
			
			JSONArray array_GoodsStatusBean = object.getJSONArray("mg_list");
			ArrayList<GoodsStatusBean> mGoodsStatusBean_list = new ArrayList<GoodsStatusBean>();
			for(int i = 0;i < array_GoodsStatusBean.length();i++){
				JSONObject obj_item = array_GoodsStatusBean.getJSONObject(i);
				GoodsStatusBean goodsStatusBean = new GoodsStatusBean();
				goodsStatusBean.setG_code(obj_item.getString("g_code"));
				goodsStatusBean.setSold_status(Integer.parseInt(obj_item.getString("mg_state")));
				goodsStatusBean.setUnit_price(obj_item.getString("unit_price"));
				mGoodsStatusBean_list.add(goodsStatusBean);
			}
			mRegisterStatusBean.getmGoodsStatusBean_list().addAll(mGoodsStatusBean_list);
			
			JSONArray array_WayBean = object.getJSONArray("way_list");
			ArrayList<WayBean> mWayBean_list = new ArrayList<WayBean>();
			for(int i = 0;i < array_WayBean.length();i++){
				JSONObject obj_item = array_WayBean.getJSONObject(i);
				CommonUtils.showLog(CommonUtils.TAG, "parseRegister " + i + " = " + obj_item.toString());
				String way_name = obj_item.getString("mw_code");
				if(way_name != null && !way_name.equals("") && !way_name.equals("null")){
					WayBean wayBean = new WayBean();
					wayBean.setG_code(obj_item.getString("g_code"));
					wayBean.setMaxNum(Integer.parseInt(obj_item.getString("mws_max_inventory")));
					wayBean.setStatus(Integer.parseInt(obj_item.getString("mws_state")));
					wayBean.setStoreNum(Integer.parseInt(obj_item.getString("mws_inventory")));
					wayBean.setWay_id(GoodsWayUtil.getWayId(way_name));
					mWayBean_list.add(wayBean);
					String box = GoodsWayUtil.parseWayId(GoodsWayUtil.getWayId(way_name)).get(0);
					if(!mRegisterStatusBean.getBox_map().containsKey(box))
						mRegisterStatusBean.getBox_map().put(box, box);
				}
			}
			mRegisterStatusBean.getmWayBean_list().addAll(mWayBean_list);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mRegisterStatusBean;
	}
	/**
	 * 解析修改商品状态
	 * @param result
	 * @return
	 */
	public static StatusGoodsStatus parseGoodsStatus(String result){
		StatusGoodsStatus mStatusGoodsStatus = new StatusGoodsStatus();
		try {
			JSONObject object = new JSONObject(result);
			String status = object.getString("status");
			String msg = object.getString("msg");
			String count = object.getString("count");
			mStatusGoodsStatus.setStatus(status);
			mStatusGoodsStatus.setMsg(msg);
			mStatusGoodsStatus.setCount(Integer.parseInt(count));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mStatusGoodsStatus;
	}
	/**
	 * 解析修改货道状态
	 * @param result
	 * @return
	 */
	public static StatusWayBean parseWayStatus(String result){
		StatusWayBean mStatusWayBean = new StatusWayBean();
		try {
			JSONObject object = new JSONObject(result);
			String status = object.getString("status");
			String msg = object.getString("msg");
			String count = object.getString("count");
			mStatusWayBean.setStatus(status);
			mStatusWayBean.setMsg(msg);
			mStatusWayBean.setCount(Integer.parseInt(count));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mStatusWayBean;
	}
	/**
	 * 解析补货状态
	 * @param result
	 * @return
	 */
	public static StatusWayAndGoodsBean parseWayAndGoodsStatus(String result){
		StatusWayAndGoodsBean mStatusWayAndGoods = new StatusWayAndGoodsBean();
		try {
			JSONObject object = new JSONObject(result);
			String status = object.getString("status");
			String msg = object.getString("msg");
			mStatusWayAndGoods.setStatus(status);
			mStatusWayAndGoods.setMsg(msg);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mStatusWayAndGoods;
	}
	/**
	 * 解析用户登入
	 * @param result
	 * @return
	 */
	public static StatusUserBean parseUserStatus(String result){
		StatusUserBean statusUserBean = new StatusUserBean();
		try {
			JSONObject object = new JSONObject(result);
			String status = object.getString("status");
			String msg = object.getString("msg");
			String u_id = object.getString("u_id");
			String u_name = object.getString("u_name");
			if(object.has("way_list")){
				JSONArray array = object.getJSONArray("way_list");
				HashMap<String, RecoverAssistWayBean> wayBean_map = new LinkedHashMap<String, RecoverAssistWayBean>();
				for(int i = 0;i < array.length();i++){
					JSONObject item = array.getJSONObject(i);
					String mw_code = item.getString("mw_code");
					if(!CommonUtils.isEmpty(mw_code) && !mw_code.equals("null")){
						RecoverAssistWayBean wayBean = new RecoverAssistWayBean();
						wayBean.setG_code(item.getString("g_code"));
						wayBean.setMaxNum(Integer.parseInt(item.getString("mws_max_inventory")));
						wayBean.setStatus(Integer.parseInt(item.getString("mws_state")));
						wayBean.setStoreNum(Integer.parseInt(item.getString("mws_inventory")));
						wayBean.setWay_id(GoodsWayUtil.getWayId(mw_code));
						wayBean.setStore_differ(0);
						wayBean_map.put(mw_code, wayBean);
					}
				}
				statusUserBean.getWayBean_map().putAll(wayBean_map);
			}
			statusUserBean.setStatus(status);
			statusUserBean.setMsg(msg);
			statusUserBean.setU_id(u_id);
			statusUserBean.setU_name(u_name);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return statusUserBean;
	}
	/**
	 * 检测微商城兑换码,并范围兑换的产品信息
	 * @param result
	 * @return
	 */
	public static StatusCheckRcode parseStatusCheckRcode(String result){
		StatusCheckRcode statusCheckRcode = new StatusCheckRcode();
		try {
			JSONObject object = new JSONObject(result);
			String code = object.getString("code");
			String msg = object.getString("msg");
			String rid = object.getString("rid");
			String oid = object.getString("oid");
			String arrayResult = object.getString("list");
			statusCheckRcode.setCode(code);
			statusCheckRcode.setMsg(msg);
			statusCheckRcode.setRid(rid);
			statusCheckRcode.setOid(oid);
			JSONArray array = new JSONArray(arrayResult);
			ArrayList<RcodeBean> list = new ArrayList<RcodeBean>();
			for(int i = 0;i < array.length();i++){
				RcodeBean statusExpandRcodeItem = new RcodeBean();
				JSONObject obj = array.getJSONObject(i);
				statusExpandRcodeItem.setAmounts(obj.getString("amounts"));
				statusExpandRcodeItem.setG_code(obj.getString("g_code"));
				statusExpandRcodeItem.setG_id(obj.getString("g_id"));
				statusExpandRcodeItem.setG_name(obj.getString("goods_name"));
				statusExpandRcodeItem.setOrder_gl_id(obj.getString("order_gl_id"));
				list.add(statusExpandRcodeItem);
			}
			statusCheckRcode.getList().addAll(list);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return statusCheckRcode;
	}
	/**
	 * 测试 检测微商城兑换码,并范围兑换的产品信息
	 * @param result
	 * @return
	 */
	public static StatusCheckRcode test_parseStatusCheckRcode(String result){
		StatusCheckRcode statusCheckRcode = new StatusCheckRcode();
		statusCheckRcode.setCode("1");
		statusCheckRcode.setMsg("yes");
		statusCheckRcode.setRid("1");
		statusCheckRcode.setOid("1");
		ArrayList<RcodeBean> list = new ArrayList<RcodeBean>();
//		for (int i = 0; i < 1; i++) {
			RcodeBean statusExpandRcodeItem = new RcodeBean();
			statusExpandRcodeItem.setAmounts("1");
			statusExpandRcodeItem.setG_code("A0001");
			statusExpandRcodeItem.setG_id("1");
			statusExpandRcodeItem.setG_name("每日c");
			statusExpandRcodeItem.setOrder_gl_id("1");
			list.add(statusExpandRcodeItem);
//		}
			RcodeBean statusExpandRcodeItem_02 = new RcodeBean();
			statusExpandRcodeItem_02.setAmounts("1");
			statusExpandRcodeItem_02.setG_code("A0004");
			statusExpandRcodeItem_02.setG_id("1");
			statusExpandRcodeItem_02.setG_name("每日c");
			statusExpandRcodeItem_02.setOrder_gl_id("1");
			list.add(statusExpandRcodeItem_02);
		statusCheckRcode.getList().addAll(list);
		return statusCheckRcode;
	}
	/**
	 * 微商城
	 * 增加兑换记录
	 * @param result
	 * @return
	 */
	public static StatusAddChangeLogStatus parseStatusAddChangeLogStatus(String result){
		StatusAddChangeLogStatus statusAddChangeLogStatus = new StatusAddChangeLogStatus();
		try {
			JSONObject obj = new JSONObject(result);
			String code = obj.getString("code");
			String msg = obj.getString("msg");
			statusAddChangeLogStatus.setCode(code);
			statusAddChangeLogStatus.setMsg(msg);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return statusAddChangeLogStatus;
	}
	/**
	 * 退出兑换（解除锁定）
	 * @param result
	 * @return
	 */
	public static StatusQuitConvert parseStatusQuitConvert(String result){
		StatusQuitConvert statusQuitConvert = new StatusQuitConvert();
		try {
			JSONObject obj = new JSONObject(result);
			String code = obj.getString("code");
			String msg = obj.getString("msg");
			statusQuitConvert.setCode(code);
			statusQuitConvert.setMsg(msg);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return statusQuitConvert;
	}
	/**
	 * 解析模板接口
	 * @param result
	 * @return
	 */
	public static StatusTemplateBean parseStatusTemplateBean(String result){
		StatusTemplateBean statusTemplateBean = new StatusTemplateBean();
		try {
			JSONObject obj = new JSONObject(result);
			String code = obj.getString("status");
			String msg = obj.getString("msg");
			statusTemplateBean.setStatus(code);
			statusTemplateBean.setMsg(msg);
			JSONArray array_g_list = obj.getJSONArray("g_list");
			ArrayList<String> g_list = new ArrayList<String>();
			for(int i = 0;i < array_g_list.length();i++){
				JSONObject array_obj = array_g_list.getJSONObject(i);
				g_list.add(array_obj.getString("g_code"));
			}
			statusTemplateBean.setG_list(g_list);
			JSONArray array_mw_list = obj.getJSONArray("mw_list");
			ArrayList<WayBean> mw_list = new ArrayList<WayBean>();
			for(int i = 0;i < array_mw_list.length();i++){
				JSONObject array_obj = array_mw_list.getJSONObject(i);
				WayBean wayBean = new WayBean();
				wayBean.setG_code(array_obj.getString("g_code"));
				wayBean.setWay_id(array_obj.getString("mw_code"));
				wayBean.setMaxNum(Integer.parseInt(array_obj.getString("mws_inventory")));
				wayBean.setW_type(0);
				mw_list.add(wayBean);
			}
			statusTemplateBean.setMw_list(mw_list);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return statusTemplateBean;
	}
}
