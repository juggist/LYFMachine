package com.icoffice.library.db;
/**
 * author: Michael.Lu
 */
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import CofficeServer.PayType;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.icoffice.library.bean.GoodsSortBean;
import com.icoffice.library.bean.GoodsStateBean;
import com.icoffice.library.bean.db.AdStatusBean;
import com.icoffice.library.bean.db.AppRestarTime;
import com.icoffice.library.bean.db.BaseDbBean;
import com.icoffice.library.bean.db.GoodsBean;
import com.icoffice.library.bean.db.GoodsStatusBean;
import com.icoffice.library.bean.db.LYMachineSetBean;
import com.icoffice.library.bean.db.MachineBean;
import com.icoffice.library.bean.db.MachineSetBean;
import com.icoffice.library.bean.db.OrderBean;
import com.icoffice.library.bean.db.PayTypeBean;
import com.icoffice.library.bean.db.RcodeBean;
import com.icoffice.library.bean.db.RecoveryRecordsBean;
import com.icoffice.library.bean.db.RecoveryWayBean;
import com.icoffice.library.bean.db.SoldGoodsBean;
import com.icoffice.library.bean.db.SoldWayBean;
import com.icoffice.library.bean.db.WayBean;
import com.icoffice.library.bean.network.GoodsAllBean;
import com.icoffice.library.bean.network.StatusCheckRcode;
import com.icoffice.library.bean.network.StatusRegisterBean;
import com.icoffice.library.utils.CommonUtils;
import com.icoffice.library.utils.GoodsWayUtil;
import com.icoffice.library.utils.ParseDb;

public class DbHelper {
	private SQLiteDatabase db;
	private static final int DB_VERSION = 10;
	/**
	 * 1.初始化数据库
	 * 2.添加PayTypeBean表,MachineBean里添加字段 appRestarTime（记录每次重启app的时间）
	 * 3.添加数据备份表 WayBeanRecovery RecordsRecovery
	 * 4.version 15 (WayBean 添加字段 w_code。添加MachineSetBaen表)
	 * 5.version 16  (添加AdStatusBean表)
	 * 6.version 18 自动修改set文件 自动拷贝音频文件(21.mp3,22.mp3)
	 * 7.version 19 MachineSetBean表添加physicsButtonStatus字段
	 * 8.version 20 自动拷贝音频文件(open_alipay.mp3)
	 * 10.version   WayBean表添加w_type，增加了LYMachineSetBean表
	 */
	private DBOpenHelper mDBOpenHelper = null;
	private String dbName = "iCoffice_DB.db";
	private static DbHelper instance = null;
	
	public final static int TABLE_MACHINEBEAN = 0;// 机器表
	public final static int TABLE_GOODSBEAN = 1;// 商品表
	public final static int TABLE_ORDERBEAN = 2;// 订单表
	public final static int TABLE_GOODSSTATUSBEAN = 3;// 商品状态表
	public final static int TABLE_WAYSTATUSBEAN = 4;// 货道状态表
	public final static int TABLE_PAYTYPEBEAN = 5;// 支付状态表
	public final static int TABLE_APPRESTARTBEAN = 6;//app启动，关闭时间表
	public final static int TABLE_WAYBEANRECOVERY = 7;//数据备份 货道信息
	public final static int TABLE_RECORDSRECOVERY = 8;//数据备份 补货信息
	public final static int TABLE_MACHINESET = 9;//machine设置表
	public final static int TABLE_ADSTATUSBEAN = 10;//广告信息表
	public final static int TABLE_LYMACHINESET = 11;//雷云峰机器 门与云台的状态设置表
	
	private final Class<?>[] classList = { MachineBean.class, GoodsBean.class,
			OrderBean.class ,GoodsStatusBean.class ,WayBean.class,PayTypeBean.class,AppRestarTime.class,RecoveryWayBean.class,RecoveryRecordsBean.class,MachineSetBean.class,AdStatusBean.class,LYMachineSetBean.class};

	private DbHelper(Context mContext) {
		mDBOpenHelper = new DBOpenHelper(mContext,dbName,null,DB_VERSION,classList);
		db = mDBOpenHelper.getWritableDatabase();
		CommonUtils.showLog(CommonUtils.DATABASE_TAG,"db version = " + db.getVersion() + "");
	}

	public static DbHelper getInstance(Context mContext) {
		if (instance == null) {
			instance = new DbHelper(mContext);
		}
		return instance;
	}

	/**
	 * 创建所有表
	 * 
	 * 
	 * @return
	 */
	private void createTable() {
		for (int i = 0; i < classList.length; i++) {
			Class<?> c = classList[i];
			String tableName = getClassName(c);
			if (!isTableExist(tableName)) {
				String text = "create table " + tableName
						+ "(id integer primary key autoincrement,";
				Field[] field = c.getDeclaredFields(); // 获取实体类的所有属性，返回Field数组
				for (int j = 0; j < field.length; j++) { // 遍历所有属性
					String name = field[j].getName(); // 获取属性的名字
					String type = field[j].getGenericType().toString(); // 获取属性的类型
					if (type.equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
						text += name + " text";
					}
					if (type.equals("class java.lang.Integer")) {
						text += name + " integer";
					}
					if (type.equals("class java.lang.Float")) {
						text += name + " float";
					}
					if (type.equals("class java.lang.Double")) {
					}
					if (type.equals("class java.lang.Boolean")) {
					}
					if (type.equals("class java.util.Date")) {
					}
					if (field.length - 1 != j)
						text += " ,";
				}
				text += ")";
				db.execSQL(text);
			}
		}
	}

	/**
	 * 判断表是否存在
	 * 
	 * 
	 * @return
	 */
	private boolean isTableExist(String tableName) {
		boolean result = false;
		if (tableName == "" || tableName == null) {
			return false;
		}
		try {
			Cursor cursor = null;
			String sql = "select count(1) as c from sqlite_master where type ='table' and name ='"
					+ tableName + "' ";
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				} else {

				}
			}
			cursor.close();
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * 获取类名方法
	 * 
	 * 
	 * @return
	 */
	private String getClassName(Class<?> c) {
		String tableName = c.getName();
		return tableName.substring(tableName.lastIndexOf('.') + 1);
	}
	/**
	 * 获取表名
	 * @param index
	 * @return
	 */
	private String getTableName(int index){
		Class<?> c = classList[index];
		return CommonUtils.getClassName(c);
	}
	/**
	 * 通用删除数据
	 * 
	 * 
	 * @return
	 */
	private void clearTable(Integer index) {
		Class<?> c = classList[index];
		String tableName = getClassName(c);
		db.execSQL("delete from " + tableName);
	}

	/**
	 * 通用插入单条数据
	 * 
	 * 
	 * @return
	 */
	private long insertData(Integer index, ContentValues mContentValues) {
		Class<?> c = classList[index];
		String tableName = getClassName(c);
		if(tableName.equals("OrderBean"))
			CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "插入初始化的订单信息");
		return db.insert(tableName, null, mContentValues);
	}
	
	/**
	 * 通用插入多条数据
	 * 
	 * 
	 * @return
	 */
	private void insertData(Integer index, ArrayList<ContentValues> cvList) {
		for (int i = 0; i < cvList.size(); i++) {
			insertData(index, cvList.get(i));
		}
	}

	/**
	 * 通用修改数据
	 * 
	 * 
	 * @return
	 */
	private void updateData(Integer index, Integer id, String content) {
		Class<?> c = classList[index];
		String tableName = getClassName(c);
		String sql = "update " + tableName + " set " + content + " where id = "
				+ id;
		db.execSQL(sql);
	}

	/**
	 * 通用查询表数据
	 * 
	 * 
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	private ArrayList<Object> selectData(Integer index) {
		Class<?> c = classList[index];
		ArrayList<Object> list = new ArrayList<Object>();
		String tableName = getClassName(c);
		String sql = "select * from " + tableName;
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.getCount() > 0) {
			try {
				cursor.moveToFirst();
				for (int i = 0; i < cursor.getCount(); i++) {
					BaseDbBean obj = null;
					obj = (BaseDbBean) c.newInstance();
					Method[] methods = c.getMethods();
					obj.setId(cursor.getInt(cursor.getColumnIndex("id")));
					// 遍历对象的方法
					for (Method method : methods) {
						String methodName = method.getName();
						// 如果对象的方法以set开头
						if (methodName.startsWith("set")) {
							// 根据方法名字得到数据表格中字段的名字
							String columnName = methodName.substring(3,
									methodName.length()).toLowerCase();
							// 得到方法的参数类型
							Class<?>[] parmts = method.getParameterTypes();
							if (parmts[0] == String.class) { // 如果参数为String类型，则从结果集中按照列名取得对应的值，并且执行改set方法
								method.invoke(obj, cursor.getString(cursor
										.getColumnIndex(columnName)));
							}
							if (parmts[0] == Integer.class) {
								method.invoke(obj, cursor.getInt(cursor
										.getColumnIndex(columnName)));
							}
						}
					}
					list.add(obj);
					cursor.moveToNext();
				}
				cursor.close();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	/**
	 * 通用查询表数据（附加条件查询）
	 * 
	 * 
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	private ArrayList<Object> selectData(Integer index,String sql) {
		Class<?> c = classList[index];
		ArrayList<Object> list = new ArrayList<Object>();
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.getCount() > 0) {
			try {
				cursor.moveToFirst();
				for (int i = 0; i < cursor.getCount(); i++) {
					BaseDbBean obj = null;
					obj = (BaseDbBean) c.newInstance();
					Method[] methods = c.getMethods();
					obj.setId(cursor.getInt(cursor.getColumnIndex("id")));
					// 遍历对象的方法
					for (Method method : methods) {
						String methodName = method.getName();
						// 如果对象的方法以set开头
						if (methodName.startsWith("set")) {
							// 根据方法名字得到数据表格中字段的名字
							String columnName = methodName.substring(3,
									methodName.length()).toLowerCase();
							// 得到方法的参数类型
							Class<?>[] parmts = method.getParameterTypes();
							if (parmts[0] == String.class) { // 如果参数为String类型，则从结果集中按照列名取得对应的值，并且执行改set方法
								method.invoke(obj, cursor.getString(cursor
										.getColumnIndex(columnName)));
							}
							if (parmts[0] == Integer.class) {
								method.invoke(obj, cursor.getInt(cursor
										.getColumnIndex(columnName)));
							}
						}
					}
					list.add(obj);
					cursor.moveToNext();
				}
				cursor.close();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	/**
	 * 通用插入
	 * @param table
	 * @param prams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String getInsertSql(int index, Map<String, Object> prams) {
		Class<?> c = classList[index];
		String tableName = CommonUtils.getClassName(c);
		String res = "insert into " + tableName + "(";
		Set<String> key = prams.keySet();
		String keynames = "";
		String values = "";

		for (Iterator it = key.iterator(); it.hasNext();) {
			String s = (String) it.next();
			keynames += s + ",";
			String className = "";
			if (prams.get(s) != null) {
				className = prams.get(s).getClass().getName();
			}
			String name = className.substring(className.lastIndexOf(".") + 1);
			if ("String".equals(name)) {
				values += "'" + prams.get(s) + "',";
			} else {
				values += prams.get(s) + ",";
			}
		}
		keynames = keynames.substring(0, keynames.length() - 1);
		values = values.substring(0, values.length() - 1);
		res += keynames + ") values (" + values + ")";
		return res;
	}
	/**
	 * 通用删除
	 * 
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String getDeleteSql(int index, Map<String, Object> condition) {
		Class<?> c = classList[index];
		String tableName = CommonUtils.getClassName(c);
		String sql = "DELETE FROM " + tableName;
		Set<String> key = condition.keySet();
		sql += " where 1=1";
		for (Iterator it = key.iterator(); it.hasNext();) {
			String classItem = (String) it.next();
			String className = condition.get(classItem).getClass().getName();
			String name = className.substring(className.lastIndexOf(".") + 1);
			if ("String".equals(name)) {
				String con = (String) condition.get(classItem);
				if (con.indexOf(",") == -1) {
					sql += " and " + classItem + " ='" + condition.get(classItem) + "'";
				} else {
					sql += " and " + classItem + " in (" + condition.get(classItem) + ")";
				}
			} else {
				sql += " and " + classItem + "=" + condition.get(classItem);
			}
		}
		return sql;
	}
	/**
	 * select machineBean表
	 * 
	 * 
	 * @return
	 */
	public MachineBean selectMachineBean() {
		MachineBean mMachineBean = null;
		ArrayList<Object> list = selectData(TABLE_MACHINEBEAN);
		if (0 != list.size()) {
			mMachineBean = (MachineBean) list.get(0);
		}
		return mMachineBean;
	}

	/**
	 * select goodsBean表
	 * 
	 * 
	 * @return
	 */
	public ArrayList<GoodsBean> selectGoodsBean() {
		ArrayList<GoodsBean> gList = new ArrayList<GoodsBean>();
		ArrayList<Object> oList = selectData(TABLE_GOODSBEAN);
		for (int i = 0; i < oList.size(); i++) {
			gList.add((GoodsBean) oList.get(i));
		}
		return gList;
	}

	/**
	 * select orderBean表
	 * 
	 * 
	 * @return
	 */
	public ArrayList<OrderBean> selectOrderBean() {
		ArrayList<OrderBean> orderList = new ArrayList<OrderBean>();
		ArrayList<Object> oList = selectData(TABLE_ORDERBEAN);
		for (int i = 0; i < oList.size(); i++) {
			orderList.add((OrderBean) oList.get(i));
		}
		return orderList;
	}

	/**
	 * insert machineBean表
	 * 
	 * 
	 * @return
	 */
	public void insertMachineBean() {
		ArrayList<ContentValues> cvList = new ArrayList<ContentValues>();
		ContentValues cvMachineBean = new ContentValues();
		cvMachineBean.put("m_code", "");
		cvList.add(cvMachineBean);
		insertData(TABLE_MACHINEBEAN, cvList);
	}

	/**
	 * insert goodsBean表
	 * 
	 * 
	 * @return
	 */
	public void insertGoodsBean(GoodsAllBean mGoodsAllBean) {
		MachineBean mMachineBean = new MachineBean();
		mMachineBean.setG_version(mGoodsAllBean.getVersion());
		updateMachineBean(mMachineBean);// 货品版本号

		ArrayList<ContentValues> cvList = new ArrayList<ContentValues>();
		ArrayList<GoodsSortBean> goodsListAllBean = mGoodsAllBean
				.getGoodsListAllBean();
		for (int i = 0; i < goodsListAllBean.size(); i++) {
			GoodsSortBean mGoodsListBean = goodsListAllBean.get(i);
			ArrayList<GoodsBean> goodsBean = mGoodsListBean.getGoodsBean();
			for (int j = 0; j < goodsBean.size(); j++) {
				GoodsBean mGoodsBean = goodsBean.get(j);
				ContentValues cvGoodBean = new ContentValues();
				cvGoodBean.put("gt_id", mGoodsListBean.getGt_id());
				cvGoodBean.put("gt_name", mGoodsListBean.getGt_name());
				cvGoodBean.put("gt_ename", mGoodsListBean.getGt_ename());
				cvGoodBean.put("unit_price", mGoodsBean.getUnit_price());
				cvGoodBean.put("g_name", mGoodsBean.getG_name());
				cvGoodBean.put("g_ename", mGoodsBean.getG_ename());
				cvGoodBean.put("g_code", mGoodsBean.getG_code());
				cvGoodBean.put("g_origin", mGoodsBean.getG_origin());
				cvGoodBean.put("g_brand", mGoodsBean.getG_brand());
				cvGoodBean.put("g_taste", mGoodsBean.getG_taste());
				cvGoodBean.put("g_specification",
						mGoodsBean.getG_specification());
				cvGoodBean.put("g_other", mGoodsBean.getG_other());
				cvGoodBean.put("g_note", mGoodsBean.getG_note());
				cvGoodBean.put("z_image", mGoodsBean.getZ_image());
				cvGoodBean
						.put("z_detail_image", mGoodsBean.getZ_detail_image());
				cvGoodBean.put("y_gif", mGoodsBean.getY_gif());
				cvGoodBean
						.put("y_detail_image", mGoodsBean.getY_detail_image());
				cvGoodBean.put("g_pack", mGoodsBean.getG_pack());
				cvList.add(cvGoodBean);
			}
		}
		clearTable(TABLE_GOODSBEAN);// 清除
		insertData(TABLE_GOODSBEAN, cvList);
	}

	/**
	 * insert orderBean表
	 * 
	 * 
	 * @return
	 */
	public int insertOrderBean(OrderBean mOrderBean) {
		ContentValues cvOrderBean = new ContentValues();
		cvOrderBean.put("client_order_no", mOrderBean.getClient_order_no());
		cvOrderBean.put("g_code", mOrderBean.getG_code());
		cvOrderBean.put("o_amount", mOrderBean.getO_amount());
		cvOrderBean.put("o_unit_price", mOrderBean.getO_unit_price());
		cvOrderBean.put("total_price", mOrderBean.getTotal_price());
		cvOrderBean.put("pay_type", mOrderBean.getPay_type());
		cvOrderBean.put("o_generate_time", mOrderBean.getO_generate_time());
		cvOrderBean.put("w_code", mOrderBean.getW_code());
		cvOrderBean.put("cashIn", mOrderBean.getCashIn());
		cvOrderBean.put("cashOut", mOrderBean.getCashOut());
		cvOrderBean.put("p_state", mOrderBean.getP_state());
		cvOrderBean.put("o_state", mOrderBean.getO_state());
		cvOrderBean.put("o_pay_state", mOrderBean.getO_pay_state());
		cvOrderBean.put("o_number", mOrderBean.getO_number());
		cvOrderBean.put("expand", mOrderBean.getExpand());
		cvOrderBean.put("o_complete_time", mOrderBean.getO_complete_time());
		return (int) insertData(TABLE_ORDERBEAN, cvOrderBean);
	}

	/**
	 * update machineBean表
	 * 
	 * 
	 * @return
	 */
	public void updateMachineBean(MachineBean mMachineBean) {
		String content = "";
		MachineBean mMachineBean1 = selectMachineBean();
		if (null != mMachineBean1) {
			int id = mMachineBean1.getId();
			if (null != mMachineBean.getM_code()) {
				content += "m_code = '" + mMachineBean.getM_code() + "',";
			}
			if (null != mMachineBean.getG_version()) {
				content += "g_version = '" + mMachineBean.getG_version() + "',";
			}
			if (null != mMachineBean.getMt_id()) {
				content += "mt_id = '" + mMachineBean.getMt_id() + "',";
			}
			if(null != mMachineBean.getM_id()){
				content += "m_id = '" + mMachineBean.getM_id() + "',";
			}
			if(null != mMachineBean.getM_no()){
				content += "m_no = '" + mMachineBean.getM_no() + "',";
			}
			if (!content.equals("")) {
				content = content.substring(0, content.length() - 1);
				updateData(TABLE_MACHINEBEAN, id, content);
			}
			
		} else {

		}
	}
	
	/**
	 * update orderBean表
	 * 
	 * 
	 * @return
	 */

	public void updateOrderBean(OrderBean mOrderBean) {
		String content = "";
		if (null != mOrderBean.getO_number()) {
			content += "o_number = '" + mOrderBean.getO_number() + "',";
		}
		if (null != mOrderBean.getO_pay_state()) {
			content += "o_pay_state = " + mOrderBean.getO_pay_state() + ",";
		}
		if (null != mOrderBean.getO_state()) {
			content += "o_state = " + mOrderBean.getO_state() + ",";
		}
		if (null != mOrderBean.getO_complete_time()) {
			content += "o_complete_time = '" + mOrderBean.getO_complete_time()
					+ "',";
		}
		if (null != mOrderBean.getP_state()) {
			content += "p_state = " + mOrderBean.getP_state() + ",";
		}
		if (null != mOrderBean.getCashIn()){
			content += "cashIn = " + mOrderBean.getCashIn() + ",";
		}
		if (null != mOrderBean.getCashOut()){
			content += "cashOut = " + mOrderBean.getCashOut() + ",";
		}
		if (null != mOrderBean.getW_code()){
			content += "w_code = '" + mOrderBean.getW_code() + "',";
		}
		int id = mOrderBean.getId();
		if (!content.equals("")) {
			content = content.substring(0, content.length() - 1);
			updateData(TABLE_ORDERBEAN, id, content);
			CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"修改订单 = " + content);
		}
	}
	/**
	 * select GoodsStateBean表
	 * @return
	 */
	public ArrayList<GoodsStateBean> getGoodsStateBean(){
		Cursor cursor = db.rawQuery("select gb.*,gsb.sold_status,gsb.unit_price change_price from GoodsBean gb left join GoodsStatusBean gsb ON gb.g_code=gsb.g_code", null);
		ArrayList<GoodsStateBean> list = ParseDb.getGoodsStateBeanList(cursor);
		cursor.close();
		return list;
	}
	/**
	 * update GoodsStatusBean表
	 */
	public void updateGoodsStatusBean(ArrayList<GoodsStatusBean> list){
		for(int i = 0;i < list.size();i++){
			Map<String,Object> map = new HashMap<String, Object>();
			Map<String, Object> prams = new HashMap<String, Object>();
			GoodsStatusBean mGoodsStatusBean = list.get(i);
			map.put("g_code", mGoodsStatusBean.getG_code());
			prams.put("g_code", mGoodsStatusBean.getG_code());
			prams.put("sold_status", mGoodsStatusBean.getSold_status());
			prams.put("unit_price", mGoodsStatusBean.getUnit_price());
			
			String deleteSql = getDeleteSql(TABLE_GOODSSTATUSBEAN,map);
			db.execSQL(deleteSql);
			String insertSql = getInsertSql(TABLE_GOODSSTATUSBEAN, prams);
			db.execSQL(insertSql);
		}
	}
	
	/**
	 * select 在售商品
	 * @return
	 */
	public HashMap<String, GoodsStateBean> selectSoldGoodsBean(){
		 HashMap<String, GoodsStateBean> map = new HashMap<String, GoodsStateBean>();
		 String tableName_01 = getTableName(TABLE_GOODSBEAN);
		 String tableName_02 = getTableName(TABLE_GOODSSTATUSBEAN);
		 Cursor cursor = db.rawQuery("select gb.*,gsb.sold_status,gsb.unit_price change_price from " + tableName_01 + " gb join " + tableName_02 + " gsb on gb.g_code=gsb.g_code where sold_status = '1'",null); 
		 map = ParseDb.getGoodsStateBean(cursor);
		 cursor.close();
		 return map;
	}
	/**
	 * select WayBean
	 * 获取模板 
	 * 货道对应的商品是否为空
	 * @return
	 */
	public boolean selectWayBean8g_code(){
		String sql = "select * from " + getTableName(TABLE_WAYSTATUSBEAN) + " WHERE g_code != '' ";
		Cursor cursor = db.rawQuery(sql, null);
		int count = cursor.getCount();
		cursor.close();
		if(count > 0)
			return false;
		else
			return true;
	}
	/**
	 * select WayBean
	 * 获取模板 
	 * 最大补货量是否都为空
	 * @return
	 */
	public ArrayList<WayBean> selectWayBean8maxNum(){
		ArrayList<WayBean> list = new ArrayList<WayBean>();
		String tableName = getTableName(TABLE_WAYSTATUSBEAN);
		Cursor cursor = db.rawQuery("select * from " + tableName + " where maxNum > '0' and maxNum != ''",null); 
		list = ParseDb.getWayBeanList(cursor);
		cursor.close();
		return list;
	}
	
	/**
	 * select WayBean表
	 * @return
	 */
	public HashMap<String, WayBean> selectWayBean(){
		 HashMap<String, WayBean> map = new HashMap<String, WayBean>();
		 String tableName = getTableName(TABLE_WAYSTATUSBEAN);
		 Cursor cursor = db.rawQuery("select * from " + tableName,null); 
		 map = ParseDb.getWayMap(cursor);
		 cursor.close();
		 return map;
	}
	/**
	 * 查询数据库
	 * 
	 * @return
	 */
	public ArrayList<HashMap<String, String>> selectDataBase(String sql){
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
		 Cursor cursor = db.rawQuery(sql,null); 
		 ArrayList<String> column_list = new ArrayList<String>();
		 for(int i = 0;i < cursor.getColumnCount();i++){
			 column_list.add(cursor.getColumnName(i));
		 }
		 cursor.moveToFirst();
		 for(int i = 0;i < cursor.getCount();i++){
			 HashMap<String, String> map = new HashMap<String, String>();
			 for(int j = 0;j < column_list.size();j++){
				 map.put(column_list.get(j), cursor.getString(cursor.getColumnIndex(column_list.get(j))));
			 }
			 list.add(map);
			 cursor.moveToNext();
		 }
		 cursor.close();
		 return list;
	}
	/**
	 * select WayBean
	 * 获取所有打开的货道信息
	 * 根据货道类型 返回货柜
	 * @return
	 */
	public ArrayList<WayBean> selectWayBeanList(){
		ArrayList<WayBean> list = new ArrayList<WayBean>();
		String tableName = getTableName(TABLE_WAYSTATUSBEAN);
		Cursor cursor = db.rawQuery("SELECT * FROM " + tableName + " GROUP BY substr(way_id,1,2)",null); 
		list = ParseDb.getWayBeanList(cursor);
		cursor.close();
		return list;
	}
//	/**
//	 * select WayBean表
//	 * 补货的货道按升序排列
//	 * 
//	 * @return
//	 */
//	public HashMap<String,WayBean> selectWayBeanGroupByWayId(){
//		 HashMap<String, WayBean> map = new HashMap<String, WayBean>();
//		 String tableName = getTableName(TABLE_WAYSTATUSBEAN);
//		 Cursor cursor = db.rawQuery("select * from " + tableName + " GROUP BY way_id",null); 
//		 map = ParseDb.getWayMap(cursor);
//		 cursor.close();
//		 return map;
//	}
	/**
	 * select WayBean表
	 * 获取打开的货道 以及 对应的商品信息
	 * @return
	 */
	public ArrayList<SoldWayBean> selectSoldWayBean(){
		ArrayList<SoldWayBean> list = new ArrayList<SoldWayBean>();
		Cursor cursor = db.rawQuery("select wb.*,gsb.unit_price gsb_price FROM " + getTableName(TABLE_WAYSTATUSBEAN) + " wb left join " + getTableName(TABLE_GOODSSTATUSBEAN) + " gsb on wb.g_code = gsb.g_code",null); 
		list = ParseDb.getSoldWayBeanList(cursor);
		cursor.close();
		return list;
	}
	/**
	 * delete WayBean
	 * 清空waybean数据库
	 */
	public void deleteWayBean(){
		String deleteSql = "delete from " + getTableName(TABLE_WAYSTATUSBEAN);
		db.execSQL(deleteSql);
	}
	/**
	 * update WayBean表
	 * 第一次注册机器
	 * 从服务器获取该机器在线的数据
	 * @param wayBean_list
	 */
	public void updateRefreshWayBean(ArrayList<WayBean> wayBean_list){
		for(int i = 0;i < wayBean_list.size();i++){
			WayBean wayBean = wayBean_list.get(i);
			Cursor cursor = db.rawQuery("select g_code from " + getTableName(TABLE_WAYSTATUSBEAN) + " where way_id = '" +  wayBean.getWay_id() + "'" , null);
			if(cursor.getCount() > 0){
				db.execSQL("update " + getTableName(TABLE_WAYSTATUSBEAN) + " SET g_code = '" + wayBean.getG_code() + "', maxNum = '" + wayBean.getMaxNum() + "',status = '" + wayBean.getStatus() + "',storeNum = '" + wayBean.getStoreNum() + "' where way_id = '" + wayBean.getWay_id() + "'");
			}else{
				Map<String, Object> prams = new HashMap<String, Object>();
				prams.put("g_code", wayBean.getG_code());
				prams.put("maxNum", wayBean.getMaxNum());
				prams.put("status", wayBean.getStatus());
				prams.put("storeNum", wayBean.getStoreNum());
				prams.put("way_id", wayBean.getWay_id());
				prams.put("w_code", GoodsWayUtil.getWayName(wayBean.getWay_id()));
				prams.put("w_type", '0');
				String insertSql = getInsertSql(TABLE_WAYSTATUSBEAN, prams);
				db.execSQL(insertSql);
			}
			cursor.close();
		}
	}
	
	/**
	 * 
	 * insert WayBean表
	 * 初始化新货道信息
	 */
	public void insertWayBean(ArrayList<String> list){
		for(int i = 0;i < list.size();i++){
			Cursor cursor = db.rawQuery("select * from " + getTableName(TABLE_WAYSTATUSBEAN) + " where way_id = '" + list.get(i) + "'", null);
			if(cursor.getCount() == 0){
				Map<String, Object> prams = new HashMap<String, Object>();
				prams.put("way_id", list.get(i));
				prams.put("g_code", "");
				prams.put("maxNum", 0);
				prams.put("storeNum", 0);
				prams.put("status", 1);
				prams.put("w_code", GoodsWayUtil.getWayName(list.get(i)));
				prams.put("w_type", 0);
				String insertSql = getInsertSql(TABLE_WAYSTATUSBEAN, prams);
				db.execSQL(insertSql);
			}
			cursor.close();
		}
	}
	/**
	 *  delete wayBean表
	 *  根据w_code删除数据
	 */
	public void deleteWCode(ArrayList<String> list){
		for(int i = 0;i < list.size();i++){
			String sql = "delete from " + getTableName(TABLE_WAYSTATUSBEAN) +  " where way_id = '" + list.get(i) + "'";
			db.execSQL(sql);
		}
	}
	/**
	 * 
	 * @param mWayBean
	 */
	public void updateOrInsertWayBean(WayBean mWayBean){
//		clearTable(TABLE_WAYSTATUSBEAN);
		ContentValues cv = new ContentValues();
		cv.put("g_code", mWayBean.getG_code());
		cv.put("maxNum", mWayBean.getMaxNum());
		cv.put("status", mWayBean.getStatus());
		cv.put("storeNum", mWayBean.getStoreNum());
		cv.put("way_id", mWayBean.getWay_id());
		cv.put("w_code", GoodsWayUtil.getWayName(mWayBean.getWay_id()));
		cv.put("w_type", 0);
		insertData(TABLE_WAYSTATUSBEAN, cv);
//		String sql = "select * from " + getTableName(TABLE_WAYSTATUSBEAN) + "where way_id = '" + mWayBean.getWay_id() + "'";
//		Cursor cursor = db.rawQuery(sql, null);
//		int count = cursor.getCount();
//		if(count > 0){
//			sql = "UPDATE  " + getTableName(TABLE_WAYSTATUSBEAN)  + " set storeNum = '" + mWayBean.getStoreNum() + "' where way_id = '" + mWayBean.getWay_id() +"'" ;
//			db.execSQL(sql);
//		}else{
//			Map<String, Object> prams = new HashMap<String, Object>();
//			prams.put("way_id", mWayBean.getWay_id());
//			prams.put("g_code", mWayBean.getG_code());
//			prams.put("maxNum", mWayBean.getMaxNum());
//			prams.put("storeNum", mWayBean.getStoreNum());
//			prams.put("status", 1);
//			String insertSql = getInsertSql(TABLE_WAYSTATUSBEAN, prams);
//			db.execSQL(insertSql);
//		}
	}
	/**
	 * update WayBean表
	 */
	public void updateWayBean(WayBean mWayBean){
		String content = "";
		if (null != mWayBean.getMaxNum()) {
			content += "maxNum = " + mWayBean.getMaxNum() + ",";
		}
		if (null != mWayBean.getStatus()) {
			content += "status = " + mWayBean.getStatus() + ",";
		}
		if (null != mWayBean.getStoreNum()) {
			content += "storeNum = " + mWayBean.getStoreNum() + ",";
		}
		if (null != mWayBean.getG_code()) {
			content += "g_code = '" + mWayBean.getG_code() + "',";
		}
		if (null != mWayBean.getWay_id()) {
			content += "way_id = '" + mWayBean.getWay_id() + "',";
			content += "w_code = '" + GoodsWayUtil.getWayName(mWayBean.getWay_id()) + "',";
		}
		if (null != mWayBean.getW_type()) {
			content += "w_type = '" + mWayBean.getW_type() + "',";
		}
		
		int id = mWayBean.getId();
		if (!content.equals("")) {
			content = content.substring(0, content.length() - 1);
			updateData(TABLE_WAYSTATUSBEAN, id, content);
		}
	}
	/**
	 * insert machineBean表
	 * @param mRegisterStatusBean
	 */
	public void insertMachine(StatusRegisterBean mRegisterStatusBean){
		ContentValues cv = ParseDb.getmachineBeanList(mRegisterStatusBean);
		clearTable(TABLE_MACHINEBEAN);// 清除
		insertData(TABLE_MACHINEBEAN, cv);
	}
	/**
	 * select goodsBean表
	 * 饮料分类
	 */
	public ArrayList<SoldGoodsBean> selectDrinkGoodsBean(){
		ArrayList<SoldGoodsBean> list = new ArrayList<SoldGoodsBean>();
		String sql = " select * , gsb.unit_price gsb_price ,wb.storeNum wb_storeNum,wb.way_id wb_w_code from " + getTableName(TABLE_GOODSBEAN) + " gb join " + getTableName(TABLE_WAYSTATUSBEAN) + " wb  on gb.gt_id = '1' and gb.g_code = wb.g_code join " + getTableName(TABLE_GOODSSTATUSBEAN) + " gsb on gsb.g_code = gb.g_code ORDER BY wb.way_id";
		Cursor goodInfo_cursor = db.rawQuery(sql, null);
		list = getGoodsBeanList(goodInfo_cursor);
		goodInfo_cursor.close();
		return list;
	}
	/**
	 * select goodsBean表
	 * 食品分类
	 */
	public ArrayList<SoldGoodsBean> selectFoodGoodsBean(){
		ArrayList<SoldGoodsBean> list = new ArrayList<SoldGoodsBean>();
		String sql = " select * , gsb.unit_price gsb_price ,wb.storeNum wb_storeNum ,wb.way_id wb_w_code from " + getTableName(TABLE_GOODSBEAN) + " gb join " + getTableName(TABLE_WAYSTATUSBEAN) + " wb  on gb.gt_id = '2' and gb.g_code = wb.g_code join " + getTableName(TABLE_GOODSSTATUSBEAN) + " gsb on gsb.g_code = gb.g_code ORDER BY wb.way_id";
		Cursor goodInfo_cursor = db.rawQuery(sql, null);
		list = getGoodsBeanList(goodInfo_cursor);
		goodInfo_cursor.close();
		return list;
	}
	/**
	 * select goodsBean表
	 * 其他分类
	 */
	public ArrayList<SoldGoodsBean> selectOthersGoodsBean(){
		ArrayList<SoldGoodsBean> list = new ArrayList<SoldGoodsBean>();
		String sql = " select * , gsb.unit_price gsb_price ,wb.storeNum wb_storeNum ,wb.way_id wb_w_code from " + getTableName(TABLE_GOODSBEAN) + " gb join " + getTableName(TABLE_WAYSTATUSBEAN) + " wb  on gb.gt_id = '3' and gb.g_code = wb.g_code join " + getTableName(TABLE_GOODSSTATUSBEAN) + " gsb on gsb.g_code = gb.g_code ORDER BY wb.way_id";
		Cursor goodInfo_cursor = db.rawQuery(sql, null);
		list = getGoodsBeanList(goodInfo_cursor);
		goodInfo_cursor.close();		
		return list;
	}
	/**
	 * select wayBean表
	 * 根据货道号获取 goodsBean
	 */
	public GoodsBean selectShopGoodsBean(String way_id){
		GoodsBean mGoodsBean = null;
		String sql = "select * from " + getTableName(TABLE_WAYSTATUSBEAN) + " where storeNum > 0 and way_id = '"+ way_id + "'";
		ArrayList<Object> oList = selectData(TABLE_GOODSBEAN,sql);
		if(oList.size() > 0)
			mGoodsBean = (GoodsBean) oList.get(0);
		return mGoodsBean;
	}
	private ArrayList<SoldGoodsBean> getGoodsBeanList(Cursor goodInfo_cursor){
		ArrayList<SoldGoodsBean> list = new ArrayList<SoldGoodsBean>();
		if(goodInfo_cursor.getCount() > 0){
			goodInfo_cursor.moveToFirst();
			for(int i = 0;i < goodInfo_cursor.getCount();i++){
				SoldGoodsBean mSoldGoodsBean = new SoldGoodsBean();
				mSoldGoodsBean.setG_code(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("g_code")));
				mSoldGoodsBean.setG_name(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("g_name")));
				mSoldGoodsBean.setG_ename(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("g_ename")));
				mSoldGoodsBean.setUnit_price(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("gsb_price")));
				mSoldGoodsBean.setG_note(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("g_note")));
				mSoldGoodsBean.setGt_id(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("gt_id")));
				mSoldGoodsBean.setG_origin(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("g_origin")));
				mSoldGoodsBean.setG_brand(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("g_brand")));
				mSoldGoodsBean.setG_taste(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("g_taste")));
				mSoldGoodsBean.setG_specification(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("g_specification")));
				mSoldGoodsBean.setG_other(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("g_other")));
				mSoldGoodsBean.setZ_image(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("z_image")));
				mSoldGoodsBean.setZ_detail_image(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("z_detail_image")));
				mSoldGoodsBean.setY_gif(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("y_gif")));
				mSoldGoodsBean.setY_detail_image(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("y_detail_image")));
				mSoldGoodsBean.setG_pack(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("g_pack")));
				mSoldGoodsBean.setStoreNum(goodInfo_cursor.getInt(goodInfo_cursor.getColumnIndex("wb_storeNum")));
				mSoldGoodsBean.setW_code(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("wb_w_code")));
				list.add(mSoldGoodsBean);
				goodInfo_cursor.moveToNext();
			}
		}
		return list;
	}
	private GoodsBean getGoodsBean(Cursor goodInfo_cursor){
		GoodsBean goodsBean = new GoodsBean();
		if(goodInfo_cursor.getCount() > 0){
			goodInfo_cursor.moveToFirst();
			for(int i = 0;i < goodInfo_cursor.getCount();i++){
				goodsBean.setG_code(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("g_code")));
				goodsBean.setG_name(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("g_name")));
				goodsBean.setG_ename(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("g_ename")));
				goodsBean.setUnit_price(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("gsb_price")));
				goodsBean.setG_note(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("g_note")));
				goodsBean.setGt_id(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("gt_id")));
				goodsBean.setG_origin(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("g_origin")));
				goodsBean.setG_brand(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("g_brand")));
				goodsBean.setG_taste(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("g_taste")));
				goodsBean.setG_specification(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("g_specification")));
				goodsBean.setG_other(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("g_other")));
				goodsBean.setZ_image(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("z_image")));
				goodsBean.setZ_detail_image(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("z_detail_image")));
				goodsBean.setY_gif(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("y_gif")));
				goodsBean.setY_detail_image(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("y_detail_image")));
				goodsBean.setG_pack(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("g_pack")));
				goodInfo_cursor.moveToNext();
			}
		}
		return goodsBean;
	}

	/**
	 * select orderBean表
	 * 现金订单
	 */
	public OrderBean[] selectCashOrder(){
		OrderBean[] array = null;
		String sql = " select * from " + getTableName(TABLE_ORDERBEAN) + " where pay_type = ' "+ PayType.cash.value() + " ' and o_pay_state = '1' and p_state = '0'";
		Cursor cursor = db.rawQuery(sql, null);
		ArrayList<OrderBean> oList = selectOrderBean(cursor);
		if(oList.size() > 0){
			array = new OrderBean[oList.size()];
			for (int i = 0; i < oList.size(); i++) {
				array[i] = (OrderBean) oList.get(i);
			}
		}
		cursor.close();
		return array;
	}
	/**
	 * select orderBean表
	 * 网络订单
	 */
	public OrderBean[] selectNetOrder(){
		OrderBean[] array = null;
		String sql = " select * from " + getTableName(TABLE_ORDERBEAN) + " where pay_type in (" + PayType.ali.value() + ","+ PayType.wechat.value() + "," + PayType.card.value() +")  and o_pay_state = '1' and p_state = '0'";
		Cursor cursor = db.rawQuery(sql, null);
		ArrayList<OrderBean> oList = selectOrderBean(cursor);
		if(oList.size() > 0){
			array = new OrderBean[oList.size()];
			for (int i = 0; i < oList.size(); i++) {
				array[i] = (OrderBean) oList.get(i);
			}
		}
		cursor.close();
		return array;
	}
	/**
	 * select orderBean表
	 * 网络订单
	 */
	public OrderBean[] selectCofficeShopOrder(){
		OrderBean[] array = null;
		String sql = " select * from " + getTableName(TABLE_ORDERBEAN) + " where pay_type = ' "+ PayType.exchangepay.value() + " ' and o_pay_state = '1' and p_state = '0'";
		Cursor cursor = db.rawQuery(sql, null);
		ArrayList<OrderBean> oList = selectOrderBean(cursor);
		if(oList.size() > 0){
			array = new OrderBean[oList.size()];
			for (int i = 0; i < oList.size(); i++) {
				array[i] = (OrderBean) oList.get(i);
			}
		}
		cursor.close();
		return array;
	}
	public ArrayList<OrderBean> selectOrderBean(Cursor cursor){
		ArrayList<OrderBean> list = new ArrayList<OrderBean>();
		int count = cursor.getCount();
		if(count > 0){
			cursor.moveToFirst();
			for(int i = 0;i < count;i++){
				OrderBean mOrderBean = new OrderBean();
				mOrderBean.setId(cursor.getInt(cursor.getColumnIndex("id")));
				mOrderBean.setCashIn(cursor.getInt(cursor.getColumnIndex("cashIn")));
				mOrderBean.setCashOut(cursor.getInt(cursor.getColumnIndex("cashOut")));
				mOrderBean.setClient_order_no(cursor.getString(cursor.getColumnIndex("client_order_no")));
				mOrderBean.setG_code(cursor.getString(cursor.getColumnIndex("g_code")));
				mOrderBean.setO_amount(cursor.getInt(cursor.getColumnIndex("o_amount")));
				mOrderBean.setO_complete_time(cursor.getString(cursor.getColumnIndex("o_complete_time")));
				mOrderBean.setO_generate_time(cursor.getString(cursor.getColumnIndex("o_generate_time")));
				mOrderBean.setO_number(cursor.getString(cursor.getColumnIndex("o_number")));
				mOrderBean.setO_pay_state(cursor.getInt(cursor.getColumnIndex("p_state")));
				mOrderBean.setO_state(cursor.getInt(cursor.getColumnIndex("o_state")));
				mOrderBean.setO_unit_price(cursor.getInt(cursor.getColumnIndex("o_unit_price")));
				mOrderBean.setP_state(cursor.getInt(cursor.getColumnIndex("p_state")));
				mOrderBean.setPay_type(cursor.getInt(cursor.getColumnIndex("pay_type")));
				mOrderBean.setTotal_price(cursor.getInt(cursor.getColumnIndex("total_price")));
				mOrderBean.setW_code(cursor.getString(cursor.getColumnIndex("w_code")));
				mOrderBean.setExpand(cursor.getString(cursor.getColumnIndex("expand")));
				list.add(mOrderBean);
				cursor.moveToNext();
			}
		}
		return list;
	}
	/**
	 * select wayBean表
	 * 根据g_code获取对应的货道
	 */
	public String test_selectWay_id(String g_code){
		String way_id = null;
		String sql = "SELECT * from " + getTableName(TABLE_WAYSTATUSBEAN) + " where g_code = '" + g_code + "' ORDER BY storeNum DESC";
		Cursor cursor = db.rawQuery(sql, null);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			for(int i = 0;i < cursor.getCount();i++){
				way_id = cursor.getString(cursor.getColumnIndex("way_id"));
				cursor.moveToNext();
			}
		}
		cursor.close();
		CommonUtils.showLog(CommonUtils.DATABASE_TAG,GoodsWayUtil.getWayName(way_id));
		return way_id;
	}
	/**
	 * select wayBean表
	 * 兑换接口
	 * 根据g_code获取对应的货道
	 */
	public String selectWay_id8g_codeRandom(String g_code){
		String way_id = "";
		String sql = "SELECT * from " + getTableName(TABLE_WAYSTATUSBEAN) + " where g_code = '" + g_code + "' Order By RANDOM()";
		Cursor cursor = db.rawQuery(sql, null);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			for(int i = 0;i < cursor.getCount();i++){
				way_id = cursor.getString(cursor.getColumnIndex("way_id"));
				Integer storeNum = cursor.getInt(cursor.getColumnIndex("storeNum"));
				if(GoodsWayUtil.parseBox(way_id).equals(GoodsWayUtil.DRINKS_BOX_NAME)){
					if(storeNum > 1)
						break;
				}else
					break;
				cursor.moveToNext();
			}
		}
		cursor.close();
		CommonUtils.showLog(CommonUtils.DATABASE_TAG,"兑换 way_id = " + way_id);
		return way_id;
	}
	/**
	 * update wayBean表
	 * 减少库存
	 */
	public void updateWayBean(String way_id,int rest_storeNum){
//		String sql = "UPDATE  " + getTableName(TABLE_WAYSTATUSBEAN)  + " set storeNum = storeNum - 1 where way_id = '" + way_id + "' and storeNum > '" + rest_storeNum + "'";
		String sql = "UPDATE  " + getTableName(TABLE_WAYSTATUSBEAN)  + " set storeNum = storeNum - 1 where way_id = '" + way_id + "'";
		db.execSQL(sql);
		CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "货道 = " + way_id + " 库存减一");
		CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "way_id = " + way_id + " 剩余库存为 : " + getStrore8Way_id(way_id));
	}
	/**
	 * 根据货道id 获取库存量
	 * @param way_id
	 */
	public String getStrore8Way_id(String way_id){
		String store = "";
		try{
			Cursor cursor = db.rawQuery("select storeNum from WayBean where way_id = '" + way_id +  "'", null);
			int count = cursor.getCount();
			cursor.moveToFirst();
			if(count > 0){
				store = cursor.getString(cursor.getColumnIndex("storeNum"));
				
			}
			cursor.close();
		}catch(Exception e){
			CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "way_id = " + way_id + "剩余库存为 : exception" + e.toString());
		}
		return store;
	}
	/**
	 * select wayBean表
	 * 查询库存量
	 */
	public boolean selectSoldStatus(String g_code){
		Integer storeNum = 0;
		String sql = "select sum(storeNum) storeNum from " + getTableName(TABLE_WAYSTATUSBEAN)  + " where g_code = '" + g_code + "'";
		Cursor cursor = db.rawQuery(sql, null);
		int count = cursor.getCount();
		if(count > 0){
			cursor.moveToFirst();
			for(int i = 0;i < count;i++){
				storeNum = cursor.getInt(cursor.getColumnIndex("storeNum"));
			}
		}
		if(storeNum > 0)
			return false;
		else if(storeNum == 0)
			return true;
		else
			return false;
			
	}
	/**
	 * update orderBean表
	 * 修改p_state状态
	 */
	public void updateP_state(String client_order_no){
		String sql = "update " + getTableName(TABLE_ORDERBEAN)  + " set p_state = '1' where client_order_no = '" + client_order_no +"'";
		db.execSQL(sql);
	}
	/**
	 * select wayBean,GoodsBean,GoodsStatusBean
	 * 根据way_id 获取对应的goodsBean
	 */
	public SoldGoodsBean selectSoldGoodsBean8w_code(String w_code){
		SoldGoodsBean soldGoodsBean = null;
		String sql = "select gb.* ,gsb.unit_price gsb_price ,wb.storeNum wb_storeNum ,wb.way_id wb_w_code from " + getTableName(TABLE_WAYSTATUSBEAN) + " wb join " + getTableName(TABLE_GOODSBEAN) + " gb on wb.way_id = '" + w_code + "' and wb.g_code = gb.g_code join " + getTableName(TABLE_GOODSSTATUSBEAN) + " gsb on gb.g_code = gsb.g_code";
		Cursor goodInfo_cursor = db.rawQuery(sql, null);
		ArrayList<SoldGoodsBean> list = getGoodsBeanList(goodInfo_cursor);
		if(list.size() > 0)
			soldGoodsBean = list.get(0);
		goodInfo_cursor.close();		
		return soldGoodsBean;
	}
	/**
	 * select wayBean,GoodsBean,GoodsStatusBean
	 * 根据g_code 获取对应的goodsBean
	 */
	public SoldGoodsBean selectSoldGoodsBean8g_code(String g_code){
		SoldGoodsBean soldGoodsBean = null;
		String sql = "select gb.* ,gsb.unit_price gsb_price ,wb.storeNum wb_storeNum ,wb.way_id wb_w_code from " + getTableName(TABLE_WAYSTATUSBEAN) + " wb join " + getTableName(TABLE_GOODSBEAN) + " gb on wb.g_code = '" + g_code + "' and wb.g_code = gb.g_code join " + getTableName(TABLE_GOODSSTATUSBEAN) + " gsb on gb.g_code = gsb.g_code ORDER BY wb_storeNum DESC";
		Cursor goodInfo_cursor = db.rawQuery(sql, null);
		ArrayList<SoldGoodsBean> list = getGoodsBeanList(goodInfo_cursor);
		if(list.size() > 0)
			soldGoodsBean = list.get(0);
		goodInfo_cursor.close();		
		return soldGoodsBean;
	}
	/**
	 * select goodsBean
	 * @param box
	 * @param w_code
	 * @return
	 */
	public GoodsBean selectGoodsBean(String w_code){
		GoodsBean goodsBean = null;
		String sql = "select gb.* ,gsb.unit_price gsb_price from " + getTableName(TABLE_WAYSTATUSBEAN) + " wb join " + getTableName(TABLE_GOODSBEAN) + " gb on wb.way_id = '" + w_code + "' and wb.g_code = gb.g_code join " + getTableName(TABLE_GOODSSTATUSBEAN) + " gsb on gb.g_code = gsb.g_code";
		Cursor goodInfo_cursor = db.rawQuery(sql, null);
		goodsBean = getGoodsBean(goodInfo_cursor);
		
		goodInfo_cursor.close();	
		return goodsBean;
	}
	
//	select gb.* , gsb.unit_price gsb_price ,wb.storeNum wb_storeNum
//	from GoodsBean gb
//	join (select g_code,sum(storeNum) storeNum from WayBean
//	group by g_code) wb ON gb.g_code = wb.g_code
//	JOIN GoodsStatusBean gsb on gsb.g_code = gb.g_code
//	WHERE gb.gt_id = '1'
//	;
	/**
	 * 获取机器的code
	 * @return
	 */
	public String selectMachineCode(){
		String machineCode = "";
		String sql = "select * from " + getTableName(TABLE_MACHINEBEAN);
		Cursor cursor = db.rawQuery(sql, null);
		int count = cursor.getCount();
		if(count > 0 ){
			cursor.moveToFirst();
			machineCode = cursor.getString(cursor.getColumnIndex("m_code"));
		}
		return machineCode;
	}
	/**
	 * 获取货柜信息
	 * ArrayList<Integer> list list.get(0)_存在的货道数量  list.get(1)_货道对应存在的商品g_code数量
	 * @param w_code_list
	 * @return
	 */
	public ArrayList<Integer> selectBox(ArrayList<String> w_code_list){
		ArrayList<Integer> list = new ArrayList<Integer>();
		int ExistW_code_count = 0;
		int ExistG_code_count = 0;
		for(int i = 0;i < w_code_list.size();i++){
			Cursor cursor = db.rawQuery("SELECT  * FROM " + getTableName(TABLE_WAYSTATUSBEAN) + " where way_id = '" + w_code_list.get(i) + "'", null);
			if(cursor.getCount() > 0){
				cursor.moveToFirst();
				ExistW_code_count++;
				String g_code = cursor.getString(cursor.getColumnIndex("g_code"));
				if(g_code != null && !g_code.equals("")){
					ExistG_code_count++;
				}
			}
			cursor.close();
		}
		list.add(ExistW_code_count);
		list.add(ExistG_code_count);
		return list;
	}
	/**
	 * select GoodsStatusBean 
	 * 根据g_code获取 price
	 * @param g_code
	 * @return
	 */
	public Integer selectPrice(String g_code){
		Integer price = 0;
		String sql = "select * from " + getTableName(TABLE_GOODSSTATUSBEAN) + " where g_code = '" + g_code + "'";
		CommonUtils.showLog(CommonUtils.DATABASE_TAG,"sql = " + sql);
		Cursor cursor = db.rawQuery(sql, null);
		price = ParseDb.getPrice(cursor);
		cursor.close();
		return price;
	}
	/**
	 * select StatusCheckRcode
	 * 获取微商城兑换码下 商品的详细信息
	 * @param statusCheckRcode
	 * @return
	 */
	public StatusCheckRcode selectStatusCheckRcode(StatusCheckRcode statusCheckRcode){
		ArrayList<RcodeBean> list = statusCheckRcode.getList();
		for(int i = 0;i < list.size();i++){
			RcodeBean rcodeBean = list.get(i);
			rcodeBean.setStoreNum(0);//每次查询库存时，清空库存量
			String sql = "select gb.* ,gsb.unit_price gsb_price,wb.storeNum wb_storeNum,wb.way_id wb_way_id from " + getTableName(TABLE_GOODSBEAN) + " gb left join " + getTableName(TABLE_GOODSSTATUSBEAN) + " gsb on gb.g_code = gsb.g_code left join " + getTableName(TABLE_WAYSTATUSBEAN) + " wb on wb.g_code = gb.g_code where gb.g_code = '" + rcodeBean.getG_code() + "' ";
			Cursor goodInfo_cursor = db.rawQuery(sql, null);
			int count = goodInfo_cursor.getCount();
			if(count > 0){
				goodInfo_cursor.moveToFirst();
				for(int j = 0; j < count;j++){
					rcodeBean.setG_code(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("g_code")));
					rcodeBean.setG_name(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("g_name")));
					rcodeBean.setG_ename(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("g_ename")));
					rcodeBean.setUnit_price(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("gsb_price")));
					rcodeBean.setG_note(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("g_note")));
					rcodeBean.setGt_id(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("gt_id")));
					rcodeBean.setG_origin(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("g_origin")));
					rcodeBean.setG_brand(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("g_brand")));
					rcodeBean.setG_taste(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("g_taste")));
					rcodeBean.setG_specification(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("g_specification")));
					rcodeBean.setG_other(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("g_other")));
					rcodeBean.setZ_image(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("z_image")));
					rcodeBean.setZ_detail_image(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("z_detail_image")));
					rcodeBean.setY_gif(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("y_gif")));
					rcodeBean.setY_detail_image(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("y_detail_image")));
					rcodeBean.setG_pack(goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("g_pack")));
					String way_id = goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("wb_way_id"));
					if(way_id == null || way_id.equals(""))
						way_id = "";
					rcodeBean.setW_code(way_id);
					int storeNum = 0;
					String wb_storeNum = goodInfo_cursor.getString(goodInfo_cursor.getColumnIndex("wb_storeNum"));
					if(wb_storeNum == null || wb_storeNum.equals(""))
						storeNum = 0;
					else{
						storeNum = Integer.parseInt(wb_storeNum);
						if(GoodsWayUtil.parseBox(way_id).equals(GoodsWayUtil.DRINKS_BOX_NAME))
							storeNum--;
					}
					if(rcodeBean.getStoreNum() != null){
						storeNum = storeNum + rcodeBean.getStoreNum();
					}
					rcodeBean.setStoreNum(storeNum);
					goodInfo_cursor.moveToNext();
					CommonUtils.showLog(CommonUtils.DATABASE_TAG,"兑换 g_code = " + rcodeBean.getG_code() + ";storeNum = " + rcodeBean.getStoreNum());
				}
			}
			goodInfo_cursor.close();
		}
		
		return statusCheckRcode;
	}
	/**
	 * insert PayTypeBean
	 * 插入支付方式状态
	 * @param payType
	 * @param payStatus
	 */
	public void insertPayTypeStatus(PayTypeBean payTypeBean,SQLiteDatabase db){
		Cursor cursor = null;
		String sql = "";
		sql = "select * from " + getTableName(TABLE_PAYTYPEBEAN) + " where payType = '" + payTypeBean.getPayType() + "'" ;
		CommonUtils.showLog(CommonUtils.DATABASE_TAG,"insertPayTypeStatus sql = " + sql);
		cursor = db.rawQuery(sql, null);
		if(cursor.getCount() > 0){
			updatePayTypeStatus(payTypeBean);
		}else{
			ContentValues cv = new ContentValues();
			cv.put("payStatus", payTypeBean.getPayStatus());
			cv.put("payType", payTypeBean.getPayType());
			insertData(TABLE_PAYTYPEBEAN, cv);
		}
		cursor.close();
	}
	public void insertPayTypeStatus(PayTypeBean payTypeBean){
		Cursor cursor = null;
		String sql = "";
		sql = "select * from " + getTableName(TABLE_PAYTYPEBEAN) + " where payType = '" + payTypeBean.getPayType() + "'" ;
		cursor = db.rawQuery(sql, null);
		if(cursor.getCount() > 0){
			updatePayTypeStatus(payTypeBean);
		}else{
			ContentValues cv = new ContentValues();
			cv.put("payStatus", payTypeBean.getPayStatus());
			cv.put("payType", payTypeBean.getPayType());
			insertData(TABLE_PAYTYPEBEAN, cv);
		}
		cursor.close();
	}

	/**
	 * update PayTypeBean
	 * 更新支付方式状态
	 * @param payTypeBean
	 */
	public void updatePayTypeStatus(PayTypeBean payTypeBean){
		String sql = "";
		sql = "UPDATE " + getTableName(TABLE_PAYTYPEBEAN) + " SET payStatus = '" + payTypeBean.getPayStatus() + "',payType = '" + payTypeBean.getPayType() + "' WHERE payType = '" + payTypeBean.getPayType() + "'";
		db.execSQL(sql);
	}
	/**
	 * select PayTypeBean
	 * 查询所有支付方式的状态
	 * @return
	 */
	public ArrayList<PayTypeBean> selectPayTypeStatus(){
		ArrayList<PayTypeBean> list = new ArrayList<PayTypeBean>();
		Cursor cursor = null;
		String sql = "";
		sql = "select * from " + getTableName(TABLE_PAYTYPEBEAN);
		cursor = db.rawQuery(sql, null);
		int count = cursor.getCount();
		if(count > 0){
			cursor.moveToFirst();
			for(int i = 0;i < count;i++){
				PayTypeBean payTypeBean = new PayTypeBean();
				payTypeBean.setPayStatus(Integer.parseInt(cursor.getString(cursor.getColumnIndex("payStatus"))));
				payTypeBean.setPayType(Integer.parseInt(cursor.getString(cursor.getColumnIndex("payType"))));
				payTypeBean.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("id"))));
				list.add(payTypeBean);
				cursor.moveToNext();
			}
		}
		cursor.close();
		return list;
	}
	/**
	 * select AppRestartTime
	 * 获取最后一次自动关闭时的时间
	 * 
	 * @return
	 */
	public String selectAutoRestartTime(){
		String sql = "";
		String time = "";
		Cursor cursor = null;
		sql = "select * from " + getTableName(TABLE_APPRESTARTBEAN) + " where mode = '1'";
		cursor = db.rawQuery(sql, null);
		
		if(cursor != null){
			int count = cursor.getCount();
			if(count > 0){
				cursor.moveToLast();
				time = cursor.getString(cursor.getColumnIndex("time"));
			}
		}
		return time;
	}
	/**
	 * select AppRestartTime
	 * 获取最后一次非自动关闭时的时间
	 * 
	 * @return
	 */
	public String selectRestartTime(){
		String sql = "";
		String time = "";
		Cursor cursor = null;
		sql = "select * from " + getTableName(TABLE_APPRESTARTBEAN) + " where mode = '0'";
		cursor = db.rawQuery(sql, null);
		
		if(cursor != null){
			int count = cursor.getCount();
			if(count > 0){
				cursor.moveToLast();
				time = cursor.getString(cursor.getColumnIndex("time"));
			}
		}
		return time;
	}
	/**
	 * insert AppRestartTime
	 * @param appRestarTime
	 */
	public void insertAppRestartTime(AppRestarTime appRestarTime){
		ContentValues cv = new ContentValues();
		cv.put("time", appRestarTime.getTime());
		cv.put("mode", appRestarTime.getMode());
		insertData(TABLE_APPRESTARTBEAN, cv);
	}
	/**
	 * insert WayBeanRecovery
	 * 数据备份
	 * @param list
	 */
	public void insertWayBeanRecovery(ArrayList<WayBean> list){
		for(int i = 0;i < list.size();i++){
			WayBean wayBean = list.get(i);
			ContentValues cv = new ContentValues();
			cv.put("g_code", wayBean.getG_code());
			cv.put("maxNum", wayBean.getMaxNum());
			cv.put("status", wayBean.getStatus());
			cv.put("storeNum", wayBean.getStoreNum());
			cv.put("way_id", wayBean.getWay_id());
			insertData(TABLE_WAYBEANRECOVERY, cv);
		}
	}
	
	/**
	 * insert RecordsRecovery
	 * 数据备份
	 * @param recordsRecovery
	 */
	public void insertRecordsRecovery(RecoveryRecordsBean recordsRecovery){
		deleteRecordsRecovery();
		ContentValues cv = new ContentValues();
		cv.put("version", recordsRecovery.getVersion());
		cv.put("records0", recordsRecovery.getRecords0());
		cv.put("records1", recordsRecovery.getRecords1());
		cv.put("records2", recordsRecovery.getRecords2());
		insertData(TABLE_RECORDSRECOVERY, cv);
	}
	/**
	 * select WayBeanRecovery
	 * 数据备份
	 * @return
	 */
	public HashMap<String, WayBean> selectWayBeanRecovery(){
		 HashMap<String, WayBean> map = new HashMap<String, WayBean>();
		 String tableName = getTableName(TABLE_WAYBEANRECOVERY);
		 Cursor cursor = db.rawQuery("select * from " + tableName,null); 
		 map = ParseDb.getWayBeanRecoveryMap(cursor);
		 cursor.close();
		 return map;
	}
	/**
	 * select RecordsRecovery
	 * 数据备份
	 * @return
	 */
	public RecoveryRecordsBean selectRecordsRecovery(){
		RecoveryRecordsBean recordsRecovery = new RecoveryRecordsBean();
		String tableName = getTableName(TABLE_RECORDSRECOVERY);
		Cursor cursor = db.rawQuery("select * from " + tableName,null);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			recordsRecovery.setVersion(cursor.getString(cursor.getColumnIndex("version")));
			recordsRecovery.setRecords0(cursor.getString(cursor.getColumnIndex("records0")));
			recordsRecovery.setRecords1(cursor.getString(cursor.getColumnIndex("records1")));
			recordsRecovery.setRecords2(cursor.getString(cursor.getColumnIndex("records2")));
			
		}
		cursor.close();
		return recordsRecovery;
	}
	/**
	 * delete WayBeanRecovery
	 * 数据备份
	 */
	public void deleteWayBeanRecovery(){
		clearTable(TABLE_WAYBEANRECOVERY);// 清除
	}
	/**
	 * delete RecordsRecovery
	 * 数据备份
	 */
	public void deleteRecordsRecovery(){
		clearTable(TABLE_RECORDSRECOVERY);// 清除
	}
	/**
	 * 获取机器设置的信息
	 * @return
	 */
	public ArrayList<String> selectMachineSetStatus(){
		ArrayList<String> list = new ArrayList<String>();
		Cursor cursor = db.rawQuery("select * from MachineSetBean", null);
		int count = cursor.getCount();
		cursor.moveToFirst();
		if(count > 0){
			list.add(cursor.getString(cursor.getColumnIndex("coinAndnoteStatus")));
			list.add(cursor.getString(cursor.getColumnIndex("coinWaitTime")));
//			list.add(cursor.getString(cursor.getColumnIndex("physicsButtonStatus")));
			String temp = cursor.getString(cursor.getColumnIndex("physicsButtonStatus"));
			list.add(temp == null ? "1" : temp); // 若没有设置过物理键，则默认开启物理键
			cursor.moveToNext();
		}
		cursor.close();
		return list;
	}
	/**
	 * 获取雷云峰机器设置的信息
	 * @return
	 */
	public ArrayList<Integer> selectLYMachineSetStatus(){
		ArrayList<Integer> list = new ArrayList<Integer>();
		Cursor cursor = db.rawQuery("select * from LYMachineSetBean", null);
		int count = cursor.getCount();
		cursor.moveToFirst();
		if(count > 0){
			list.add(cursor.getInt(cursor.getColumnIndex("doorStatus")));
			list.add(cursor.getInt(cursor.getColumnIndex("elevatorStatus")));
			cursor.moveToNext();
		}
		cursor.close();
		return list;
	}
	/**
	 * update machineSetBean 
	 * CoinAndnoteStatus的状态
	 * @param status
	 */
	public void updateCoinAndnoteStatus(String status){
		String sql = "UPDATE  " + getTableName(TABLE_MACHINESET)  + " set coinAndnoteStatus = '" + status + "'";
		db.execSQL(sql);
	}
	/**
	 * update machineSetBean 
	 * coinWaitTime的状态
	 * @param status
	 */
	public void updateCoinWaitTime(String time){
		String sql = "UPDATE  " + getTableName(TABLE_MACHINESET)  + " set coinWaitTime = '" + time + "'";
		db.execSQL(sql);
	}
	/**
	 * update machineSetBean 
	 * physicsButtonStatus的状态
	 * @param status
	 */
	public void updatePhysicsButtonStatus(String physicsButtonStatus){
		String sql = "UPDATE  " + getTableName(TABLE_MACHINESET)  + " set physicsButtonStatus = '" + physicsButtonStatus + "'";
		db.execSQL(sql);
	}
	/**
	 * 雷云峰
	 * update LyMachineSetBean 
	 * door的状态
	 * @param status
	 */
	public void updateDoorStatus(String doorStatus){
		String sql = "UPDATE  " + getTableName(TABLE_LYMACHINESET)  + " set doorStatus = '" + doorStatus + "'";
		db.execSQL(sql);
	}
	/**
	 * 雷云峰
	 * update LyMachineSetBean 
	 * elevator的状态
	 * @param status
	 */
	public void updateElevatorStatus(String elevatorStatus){
		String sql = "UPDATE  " + getTableName(TABLE_LYMACHINESET)  + " set elevatorStatus = '" + elevatorStatus + "'";
		db.execSQL(sql);
	}
	/**
	 * select AdStatusBean表
	 * 所有广告的集合
	 * @return
	 */
	public ArrayList<AdStatusBean> selectAllAdStatusBean(){
		Cursor cursor = db.rawQuery("select * from AdStatusBean where '" + CommonUtils.currentTime_ymd() + "' BETWEEN startDate and endDate and file is not null", null);
		ArrayList<AdStatusBean> adStatusBean_list = analysisAdStatusBean_list(cursor);
		cursor.close();
		return adStatusBean_list;
	}
	/**
	 * select AdStatusBean表
	 * 所有首页广告的集合
	 * @return
	 */
	public ArrayList<AdStatusBean> selectAdStatusBean(){
		Cursor cursor = db.rawQuery("select * from AdStatusBean where '" + CommonUtils.currentTime_ymd() + "' BETWEEN startDate and endDate and file is not null and atId = '1'", null);
		ArrayList<AdStatusBean> adStatusBean_list = analysisAdStatusBean_list(cursor);
		cursor.close();
		return adStatusBean_list;
	}
	/**
	 * select AdStatusBean表
	 * 所有活动广告的集合
	 * @return
	 */
	public ArrayList<AdStatusBean> selectActivityAdStatusBean(){
		Cursor cursor = db.rawQuery("select * from AdStatusBean where '" + CommonUtils.currentTime_ymd() + "' BETWEEN startDate and endDate and file is not null and atId = '2'", null);
		ArrayList<AdStatusBean> adStatusBean_list = analysisAdStatusBean_list(cursor);
		cursor.close();
		return adStatusBean_list;
	}
	/*
	 * 工具方法
	 * 
	 * 解析广告cursor
	 */
	private ArrayList<AdStatusBean> analysisAdStatusBean_list(Cursor cursor){
		ArrayList<AdStatusBean> adStatusBean_list = new ArrayList<AdStatusBean>();
		int count = cursor.getCount();
		cursor.moveToFirst();
		for(int i = 0;i < count;i++){
			AdStatusBean adStatusBean = new AdStatusBean();
			adStatusBean.setAid(cursor.getInt(cursor.getColumnIndex("aid")));
			adStatusBean.setAtId(cursor.getInt(cursor.getColumnIndex("atId")));
			adStatusBean.setFile(cursor.getString(cursor.getColumnIndex("file")));
			adStatusBean.setSmallImage(cursor.getString(cursor.getColumnIndex("smallImage")));
			adStatusBean.setInterval(cursor.getInt(cursor.getColumnIndex("interval")));
			adStatusBean.setStartDate(cursor.getString(cursor.getColumnIndex("startDate")));
			adStatusBean.setEndDate(cursor.getString(cursor.getColumnIndex("endDate")));
			adStatusBean.setgCode(cursor.getString(cursor.getColumnIndex("gCode")));
			adStatusBean.setFileMd5(cursor.getString(cursor.getColumnIndex("fileMd5")));
			adStatusBean.setSmallImageMd5(cursor.getString(cursor.getColumnIndex("smallImageMd5")));
			adStatusBean.setDownload(cursor.getInt(cursor.getColumnIndex("download")));
			cursor.moveToNext();
			adStatusBean_list.add(adStatusBean);
		}
		return adStatusBean_list;
	}
	/**
	 * insert AdStatusBean表
	 * @param adStatusBean_list
	 */
	public void insertAdStatusBean(ArrayList<AdStatusBean> adStatusBean_list){ 
		deleteAdStatusBean();
		for(int i = 0;i < adStatusBean_list.size();i++){
			AdStatusBean mAdStatusBean = adStatusBean_list.get(i);
			ContentValues cv = new ContentValues();
			cv.put("aid", mAdStatusBean.getAid());
			cv.put("atId", mAdStatusBean.getAtId());
			cv.put("file", mAdStatusBean.getFile());
			cv.put("smallImage", mAdStatusBean.getSmallImage());
			cv.put("startDate", mAdStatusBean.getStartDate());
			cv.put("endDate", mAdStatusBean.getEndDate());
			cv.put("interval", mAdStatusBean.getInterval());
			cv.put("gCode", mAdStatusBean.getgCode());
			cv.put("fileMd5", mAdStatusBean.getFileMd5());
			cv.put("smallImageMd5", mAdStatusBean.getSmallImageMd5());
			cv.put("download", mAdStatusBean.getDownload());
			insertData(TABLE_ADSTATUSBEAN, cv);
		}
	}
	/**
	 * delete AdStatusBean表
	 */
	public void deleteAdStatusBean(){
		String deleteSql = "delete from " + getTableName(TABLE_ADSTATUSBEAN);
		db.execSQL(deleteSql);
	}
	/**
	 * update AdStatusBean表
	 * @param adStatusBean
	 */
	public void updateAdStatusBean(AdStatusBean adStatusBean){
		try{
			String sql = "";
			sql = "UPDATE " + getTableName(TABLE_ADSTATUSBEAN) + " SET download = '1' WHERE file = '" + adStatusBean.getFile() + "'";
			db.execSQL(sql);
		}catch(Exception e){
			
		}
	}
	/**
	 * 根据订单号获取OrderBean
	 * @return
	 */
	public OrderBean selectOrderBean(String client_order_no){
		OrderBean mOrderBean = null;
		Cursor cursor = db.rawQuery("select * from " + getTableName(TABLE_ORDERBEAN) + " where client_order_no = '" + client_order_no + "'", null);
		if(cursor.getCount() > 0){
			ArrayList<OrderBean> list = selectOrderBean(cursor);
			if(list.size() > 0)
				mOrderBean = list.get(0);
		}
		return mOrderBean;
	}
	/**
	 * 根据模糊查询 获取本地订单号
	 * @param shamClient_order_no
	 * @return
	 */
	public String selectClient_order_no(String shamClient_order_no){
		String client_order_no = "";
		String sql = "select * from " + getTableName(TABLE_ORDERBEAN) + " where client_order_no like '%" + shamClient_order_no + "%'";
		CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "selectClient_order_no " + sql);
		Cursor cursor = db.rawQuery(sql, null);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			client_order_no = cursor.getString(cursor.getColumnIndex("client_order_no"));
		}
		cursor.close();
		return client_order_no;
	}
	/**
	 * 获取雷云峰机器的 门和云台的设置状态
	 * @return
	 */
	public LYMachineSetBean selectLYMachineSetBean(){
		LYMachineSetBean mLYMachineSetBean = new LYMachineSetBean();
		String sql = "select * from LYMachineSetBean";
		Cursor cursor = db.rawQuery(sql, null);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			Integer doorStatus = cursor.getInt(cursor.getColumnIndex("doorStatus"));
			Integer elevatorStatus = cursor.getInt(cursor.getColumnIndex("elevatorStatus"));
			mLYMachineSetBean.setDoorStatus(doorStatus);
			mLYMachineSetBean.setElevatorStatus(elevatorStatus);
		}
		cursor.close();
		return mLYMachineSetBean;
	}
	/**
	 * 根据way_id获取货道类型
	 * @param way_id
	 * @return
	 */
	public String selectWayType(String way_id){
		String wayType = "";
		String sql = "select * from WayBean where way_id = '" + way_id + "'";
		Cursor cursor = db.rawQuery(sql, null);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			wayType = cursor.getString(cursor.getColumnIndex("w_type"));
		}
		cursor.close();
		return wayType;
	}
}
