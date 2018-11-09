package com.icoffice.library.utils;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.database.Cursor;

import com.icoffice.library.bean.GoodsStateBean;
import com.icoffice.library.bean.db.GoodsBean;
import com.icoffice.library.bean.db.SoldWayBean;
import com.icoffice.library.bean.db.WayBean;
import com.icoffice.library.bean.db.RecoveryWayBean;
import com.icoffice.library.bean.network.StatusRegisterBean;

public class ParseDb {
	/**
	 * 根据g_code获取价格
	 */
	public static Integer getPrice(Cursor cursor){
		int count = cursor.getCount();
		Integer price = 0;
		if(count > 0){
			cursor.moveToFirst();
			for(int i = 0;i < count;i++){
				price = Integer.parseInt(cursor.getString(cursor.getColumnIndex("unit_price")));
				cursor.moveToNext();
			}
		}
		return price;
	}
	/**
	 * 解析商品
	 * @param list
	 * @return
	 */
	public static ArrayList<ContentValues> getGoodsBeanList(ArrayList<GoodsBean> list){
		ArrayList<ContentValues> cvList = new ArrayList<ContentValues>();
		for(int i = 0;i < list.size();i++){
			GoodsBean mGoodsBean = list.get(i);
			ContentValues cvGoodBean = new ContentValues();
			cvGoodBean.put("gt_id", mGoodsBean.getGt_id());
			cvGoodBean.put("gt_name", mGoodsBean.getGt_name());
			cvGoodBean.put("gt_ename", mGoodsBean.getGt_ename());
			cvGoodBean.put("unit_price", mGoodsBean.getUnit_price());
			cvGoodBean.put("g_name", mGoodsBean.getG_name());
			cvGoodBean.put("g_ename", mGoodsBean.getG_ename());
			cvGoodBean.put("g_code", mGoodsBean.getG_code());
			cvGoodBean.put("g_origin", mGoodsBean.getG_origin());
			cvGoodBean.put("g_brand", mGoodsBean.getG_brand());
			cvGoodBean.put("g_taste", mGoodsBean.getG_taste());
			cvGoodBean.put("g_specification", mGoodsBean.getG_specification());
			cvGoodBean.put("g_other", mGoodsBean.getG_other());
			cvGoodBean.put("g_note", mGoodsBean.getG_note());
			cvGoodBean.put("z_image", mGoodsBean.getZ_image());
			cvGoodBean.put("z_detail_image", mGoodsBean.getZ_detail_image());
			cvGoodBean.put("y_gif", mGoodsBean.getY_gif());
			cvGoodBean.put("y_detail_image", mGoodsBean.getY_detail_image());
			cvGoodBean.put("g_pack", mGoodsBean.getG_pack());
			cvList.add(cvGoodBean);
		}
		return cvList;
	}
	/**
	 * 解析商品信息
	 * @param cursor
	 * @return
	 */
	public static ArrayList<GoodsStateBean> getGoodsStateBeanList(Cursor cursor){
		ArrayList<GoodsStateBean> list = new ArrayList<GoodsStateBean>();
		int count = cursor.getCount();
		if(count > 0){
			cursor.moveToFirst();
			for(int i = 0;i < count;i++){
				GoodsStateBean mGoodsStatusBean = new GoodsStateBean();
//				GoodsBean mGoodsBean = null;
//				mGoodsBean = getGoodsBean(cursor);
//				mGoodsStatusBean.setmGoodsBean(mGoodsBean);
				mGoodsStatusBean.setGt_id(cursor.getString(cursor.getColumnIndex("gt_id")));
				mGoodsStatusBean.setGt_name(cursor.getString(cursor.getColumnIndex("gt_name")));
				mGoodsStatusBean.setGt_ename(cursor.getString(cursor.getColumnIndex("gt_ename")));
				mGoodsStatusBean.setG_code(cursor.getString(cursor.getColumnIndex("g_code")));
				mGoodsStatusBean.setG_name(cursor.getString(cursor.getColumnIndex("g_name")));
				mGoodsStatusBean.setG_ename(cursor.getString(cursor.getColumnIndex("g_ename")));
				mGoodsStatusBean.setUnit_price(cursor.getString(cursor.getColumnIndex("unit_price")));
				mGoodsStatusBean.setG_note(cursor.getString(cursor.getColumnIndex("g_note")));
				mGoodsStatusBean.setZ_image(cursor.getString(cursor.getColumnIndex("z_image")));
				mGoodsStatusBean.setZ_detail_image(cursor.getString(cursor.getColumnIndex("z_detail_image")));
				mGoodsStatusBean.setY_gif(cursor.getString(cursor.getColumnIndex("y_gif")));
				mGoodsStatusBean.setY_detail_image(cursor.getString(cursor.getColumnIndex("y_detail_image")));
				mGoodsStatusBean.setG_origin(cursor.getString(cursor.getColumnIndex("g_origin")));
				mGoodsStatusBean.setG_brand(cursor.getString(cursor.getColumnIndex("g_brand")));
				mGoodsStatusBean.setG_taste(cursor.getString(cursor.getColumnIndex("g_taste")));
				mGoodsStatusBean.setG_specification(cursor.getString(cursor.getColumnIndex("g_specification")));
				mGoodsStatusBean.setG_other(cursor.getString(cursor.getColumnIndex("g_other")));
				mGoodsStatusBean.setG_pack(cursor.getString(cursor.getColumnIndex("g_pack")));
				String change_price = cursor.getString(cursor.getColumnIndex("change_price"));
				if(change_price == null || change_price.equals(""))
					change_price = mGoodsStatusBean.getUnit_price();
				mGoodsStatusBean.setPrice(change_price);
				Integer sold_status = cursor.getInt(cursor.getColumnIndex("sold_status"));
				if(sold_status == null || sold_status.equals(""))
					sold_status = 0;
				mGoodsStatusBean.setSold_status(sold_status);
				
				list.add(mGoodsStatusBean);
				cursor.moveToNext();
			}
		}
		return list;
	}
	/**
	 * 解析机器信息
	 * @param mRegisterStatusBean
	 * @return
	 */
	public static ContentValues getmachineBeanList(StatusRegisterBean mRegisterStatusBean){
		ContentValues cv = new ContentValues();
		cv.put("m_id", mRegisterStatusBean.getM_id());
		cv.put("m_code", mRegisterStatusBean.getM_code());
		cv.put("mi_id", mRegisterStatusBean.getMi_id());
		cv.put("m_no", mRegisterStatusBean.getM_no());
		return cv;
	}
	/**
	 * 解析货道信息
	 * @param cursor
	 * @return
	 */
	public static ArrayList<WayBean> getWayBeanList(Cursor cursor){
		ArrayList<WayBean> list = new ArrayList<WayBean>();
		int count = cursor.getCount();
		if(count > 0){
			cursor.moveToFirst();
			for(int i = 0;i < count;i++){
				WayBean mWayBean = new WayBean();
				Integer id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
				String way_id = cursor.getString(cursor.getColumnIndex("way_id"));
				Integer maxNum = Integer.parseInt(cursor.getString(cursor.getColumnIndex("maxNum")));
				String g_code = cursor.getString(cursor.getColumnIndex("g_code"));
				Integer storeNum = Integer.parseInt(cursor.getString(cursor.getColumnIndex("storeNum")));
				Integer status = Integer.parseInt(cursor.getString(cursor.getColumnIndex("status")));
				String w_type = cursor.getString(cursor.getColumnIndex("w_type"));
				mWayBean.setWay_id(way_id);
				mWayBean.setMaxNum(maxNum);
				mWayBean.setG_code(g_code);
				mWayBean.setStatus(status);
				mWayBean.setStoreNum(storeNum);
				mWayBean.setId(id);
				mWayBean.setW_type(w_type == null? 0:Integer.parseInt(w_type));
				list.add( mWayBean);
				cursor.moveToNext();
			}
		}
		return list;
	}
	/**
	 * 解析货道信息
	 * @param cursor
	 * @return
	 */
	public static HashMap<String, WayBean> getWayMap(Cursor cursor){
		HashMap<String, WayBean> map = new HashMap<String, WayBean>();
		int count = cursor.getCount();
		if(count > 0){
			cursor.moveToFirst();
			for(int i = 0;i < count;i++){
				WayBean mWayBean = new WayBean();
				Integer id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
				String way_id = cursor.getString(cursor.getColumnIndex("way_id"));
				Integer maxNum = Integer.parseInt(cursor.getString(cursor.getColumnIndex("maxNum")));
				String g_code = cursor.getString(cursor.getColumnIndex("g_code"));
				Integer storeNum = Integer.parseInt(cursor.getString(cursor.getColumnIndex("storeNum")));
				Integer status = Integer.parseInt(cursor.getString(cursor.getColumnIndex("status")));
				String w_type = cursor.getString(cursor.getColumnIndex("w_type"));
				mWayBean.setWay_id(way_id);
				mWayBean.setMaxNum(maxNum);
				mWayBean.setG_code(g_code);
				mWayBean.setStatus(status);
				mWayBean.setStoreNum(storeNum);
				mWayBean.setId(id);
				mWayBean.setW_type(w_type == null? 0:Integer.parseInt(w_type));
				map.put(way_id, mWayBean);
				cursor.moveToNext();
			}
		}
		return map;
	}
	/**
	 * 解析货道信息
	 * 数据备份
	 * @param cursor
	 * @return
	 */
	public static HashMap<String, WayBean> getWayBeanRecoveryMap(Cursor cursor){
		HashMap<String, WayBean> map = new HashMap<String, WayBean>();
		int count = cursor.getCount();
		if(count > 0){
			cursor.moveToFirst();
			for(int i = 0;i < count;i++){
				WayBean wayBean = new WayBean();
				Integer id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
				String way_id = cursor.getString(cursor.getColumnIndex("way_id"));
				Integer maxNum = Integer.parseInt(cursor.getString(cursor.getColumnIndex("maxNum")));
				String g_code = cursor.getString(cursor.getColumnIndex("g_code"));
				Integer storeNum = Integer.parseInt(cursor.getString(cursor.getColumnIndex("storeNum")));
				Integer status = Integer.parseInt(cursor.getString(cursor.getColumnIndex("status")));
				wayBean.setWay_id(way_id);
				wayBean.setMaxNum(maxNum);
				wayBean.setG_code(g_code);
				wayBean.setStatus(status);
				wayBean.setStoreNum(storeNum);
				wayBean.setId(id);
				map.put(way_id, wayBean);
				cursor.moveToNext();
			}
		}
		return map;
	}
	/**
	 * 获取在售的货道信息 以及 对应的商品信息
	 * @param cursor
	 * @return
	 */
	public static ArrayList<SoldWayBean> getSoldWayBeanList(Cursor cursor){
		ArrayList<SoldWayBean> list = new ArrayList<SoldWayBean>();
		int count = cursor.getCount();
		if(count > 0){
			cursor.moveToFirst();
			for(int i = 0;i < count;i++){
				SoldWayBean soldWayBean = new SoldWayBean();
				Integer id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
				String way_id = cursor.getString(cursor.getColumnIndex("way_id"));
				Integer maxNum = Integer.parseInt(cursor.getString(cursor.getColumnIndex("maxNum")));
				String g_code = cursor.getString(cursor.getColumnIndex("g_code"));
				Integer storeNum = Integer.parseInt(cursor.getString(cursor.getColumnIndex("storeNum")));
				Integer status = Integer.parseInt(cursor.getString(cursor.getColumnIndex("status")));
				String str_price = cursor.getString(cursor.getColumnIndex("gsb_price"));
				String w_type = cursor.getString(cursor.getColumnIndex("w_type"));
				Integer price = 0;
				if(str_price != null)
					price = Integer.parseInt(str_price);
				soldWayBean.setWay_id(way_id);
				soldWayBean.setMaxNum(maxNum);
				soldWayBean.setG_code(g_code);
				soldWayBean.setStatus(status);
				soldWayBean.setStoreNum(storeNum);
				soldWayBean.setPrice(price);
				soldWayBean.setId(id);
				soldWayBean.setW_type(w_type == null? 0:Integer.parseInt(w_type));
				list.add(soldWayBean);
				cursor.moveToNext();
			}
		}
		return list;
	}
	/**
	 * 解析在售商品的信息
	 * @param cursor
	 * @return
	 */
	public static HashMap<String, GoodsStateBean> getGoodsStateBean(Cursor cursor){
		HashMap<String, GoodsStateBean> map = new HashMap<String, GoodsStateBean>();
		int count = cursor.getCount();
		if(count > 0){
			cursor.moveToFirst();
			for(int i = 0;i < count;i++){
				GoodsStateBean mGoodsStatusBean = new GoodsStateBean();
//				GoodsBean mGoodsBean = null;
//				mGoodsBean = getGoodsBean(cursor);
//				mGoodsStatusBean.setmGoodsBean(mGoodsBean);
				mGoodsStatusBean.setGt_id(cursor.getString(cursor.getColumnIndex("gt_id")));
				mGoodsStatusBean.setGt_name(cursor.getString(cursor.getColumnIndex("gt_name")));
				mGoodsStatusBean.setGt_ename(cursor.getString(cursor.getColumnIndex("gt_ename")));
				mGoodsStatusBean.setG_code(cursor.getString(cursor.getColumnIndex("g_code")));
				mGoodsStatusBean.setG_name(cursor.getString(cursor.getColumnIndex("g_name")));
				mGoodsStatusBean.setG_ename(cursor.getString(cursor.getColumnIndex("g_ename")));
				mGoodsStatusBean.setUnit_price(cursor.getString(cursor.getColumnIndex("unit_price")));
				mGoodsStatusBean.setG_note(cursor.getString(cursor.getColumnIndex("g_note")));
				mGoodsStatusBean.setZ_image(cursor.getString(cursor.getColumnIndex("z_image")));
				mGoodsStatusBean.setZ_detail_image(cursor.getString(cursor.getColumnIndex("z_detail_image")));
				mGoodsStatusBean.setY_gif(cursor.getString(cursor.getColumnIndex("y_gif")));
				mGoodsStatusBean.setY_detail_image(cursor.getString(cursor.getColumnIndex("y_detail_image")));
				mGoodsStatusBean.setG_origin(cursor.getString(cursor.getColumnIndex("g_origin")));
				mGoodsStatusBean.setG_brand(cursor.getString(cursor.getColumnIndex("g_brand")));
				mGoodsStatusBean.setG_taste(cursor.getString(cursor.getColumnIndex("g_taste")));
				mGoodsStatusBean.setG_specification(cursor.getString(cursor.getColumnIndex("g_specification")));
				mGoodsStatusBean.setG_other(cursor.getString(cursor.getColumnIndex("g_other")));
				mGoodsStatusBean.setG_pack(cursor.getString(cursor.getColumnIndex("g_pack")));
				String change_price = cursor.getString(cursor.getColumnIndex("change_price"));
				if(change_price == null || change_price.equals(""))
					change_price = mGoodsStatusBean.getUnit_price();
				mGoodsStatusBean.setPrice(change_price);
				Integer sold_status = cursor.getInt(cursor.getColumnIndex("sold_status"));
				if(sold_status == null || sold_status.equals(""))
					sold_status = 0;
				mGoodsStatusBean.setSold_status(sold_status);
				map.put(mGoodsStatusBean.getG_code(), mGoodsStatusBean);
				cursor.moveToNext();
			}
		}
		return map;
	}
	//TODO  common解析
	/**
	 * 解析商品
	 * @param cursor
	 * @return
	 */
	private static GoodsBean getGoodsBean(Cursor cursor){
		GoodsBean mGoodsBean = new GoodsBean();
		mGoodsBean.setGt_id(cursor.getString(cursor.getColumnIndex("gt_id")));
		mGoodsBean.setGt_name(cursor.getString(cursor.getColumnIndex("gt_name")));
		mGoodsBean.setGt_ename(cursor.getString(cursor.getColumnIndex("gt_ename")));
		mGoodsBean.setG_code(cursor.getString(cursor.getColumnIndex("g_code")));
		mGoodsBean.setG_name(cursor.getString(cursor.getColumnIndex("g_name")));
		mGoodsBean.setG_ename(cursor.getString(cursor.getColumnIndex("g_ename")));
		mGoodsBean.setUnit_price(cursor.getString(cursor.getColumnIndex("unit_price")));
		mGoodsBean.setG_note(cursor.getString(cursor.getColumnIndex("g_note")));
		mGoodsBean.setZ_image(cursor.getString(cursor.getColumnIndex("z_image")));
		mGoodsBean.setZ_detail_image(cursor.getString(cursor.getColumnIndex("z_detail_image")));
		mGoodsBean.setY_gif(cursor.getString(cursor.getColumnIndex("y_gif")));
		mGoodsBean.setY_detail_image(cursor.getString(cursor.getColumnIndex("y_detail_image")));
		mGoodsBean.setG_origin(cursor.getString(cursor.getColumnIndex("g_origin")));
		mGoodsBean.setG_brand(cursor.getString(cursor.getColumnIndex("g_brand")));
		mGoodsBean.setG_taste(cursor.getString(cursor.getColumnIndex("g_taste")));
		mGoodsBean.setG_specification(cursor.getString(cursor.getColumnIndex("g_specification")));
		mGoodsBean.setG_other(cursor.getString(cursor.getColumnIndex("g_other")));
		mGoodsBean.setG_pack(cursor.getString(cursor.getColumnIndex("g_pack")));
		return mGoodsBean;
	}
	
}
