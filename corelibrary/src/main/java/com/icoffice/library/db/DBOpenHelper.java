package com.icoffice.library.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.icoffice.library.bean.db.AdStatusBean;
import com.icoffice.library.bean.db.AppRestarTime;
import com.icoffice.library.bean.db.LYMachineSetBean;
import com.icoffice.library.bean.db.MachineSetBean;
import com.icoffice.library.bean.db.PayTypeBean;
import com.icoffice.library.bean.db.RecoveryRecordsBean;
import com.icoffice.library.bean.db.RecoveryWayBean;
import com.icoffice.library.bean.db.WayBean;
import com.icoffice.library.utils.CommonUtils;
import com.icoffice.library.utils.FileUtil;
import com.icoffice.library.utils.GoodsWayUtil;

import java.io.File;
import java.lang.reflect.Field;

import CofficeServer.PayType;
/**
 * 数据库版本控制类
 * @author lufeisong
 *
 */
@SuppressWarnings("rawtypes")
public class DBOpenHelper extends SQLiteOpenHelper{
	
	private Class<?>[] classList;
	private static final int EXAMPLE = 2;//demo
	private static final int ADD_FIELD_MachineBean = 3;//machineBean 添加字段 appRestartTime(每次自动重启app的时间)
	private static final int ADD_W_CODE = 4;//WayBean 添加字段 w_code（显示的货道id）
	private static final int ADD_PHYSICSBUTTONSTATUS = 5;//MachineSetBean 添加字段 physicsButtonStatus（是否开启物理按钮使用）
	private static final int ADD_W_TYPE = 7;//WayBean 添加字段 w_type（是否开启物理按钮使用）
	private Context mContext;
	/***
	 * 数据库的版本必须大于0，否则报错：
	 */
	public DBOpenHelper(Context context, String name, CursorFactory factory,int version,Class<?>[] classList) {
		super(context, name, factory, version);
		mContext = context;
		this.classList = classList;
	}
	/**
	 * 这个方法
     * 1、在第一次打开数据库的时候才会走
     * 2、在清除数据之后再次运行-->打开数据库，这个方法会走
     * 3、没有清除数据，不会走这个方法
     * 4、数据库升级的时候这个方法不会走
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		CommonUtils.showLog(CommonUtils.DATABASE_TAG,"db onCreate");
		createTable(db,classList);
		initPayTypeBean(db);
		initMachineSetBean(db);
		insertPhysicsButtonStatus(db);
		insertLYMachineSetBean(db);
	}
	/**
	 * 1、第一次创建数据库的时候，这个方法不会走
	 * 2、清除数据后再次运行(相当于第一次创建)这个方法不会走
	 * 3、数据库已经存在，而且版本升高的时候，这个方法才会调用
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		CommonUtils.showLog(CommonUtils.DATABASE_TAG, "oldVersion = " + oldVersion + ";newVersion = " + newVersion);
		if(newVersion == 2){
			createTable(db, PayTypeBean.class);
			initPayTypeBean(db);
			createTable(db, AppRestarTime.class);
		}
		if(newVersion == 3){
			switch(oldVersion){
			case 1:
				createTable(db, PayTypeBean.class);
				initPayTypeBean(db);
				createTable(db, AppRestarTime.class);
				break;
			case 2:
				break;
			}
			createTable(db, RecoveryWayBean.class);
			createTable(db, RecoveryRecordsBean.class);
			
		}
		if(newVersion == 4){
			switch(oldVersion){
			case 1:
				createTable(db, PayTypeBean.class);
				initPayTypeBean(db);
				createTable(db, AppRestarTime.class);
				createTable(db, RecoveryWayBean.class);
				createTable(db, RecoveryRecordsBean.class);
				break;
			case 2:
				createTable(db, RecoveryWayBean.class);
				createTable(db, RecoveryRecordsBean.class);
				break;
			case 3:
				break;
			}
			createTable(db, MachineSetBean.class);
			initMachineSetBean(db);
			upgradeTables(db, WayBean.class, ADD_W_CODE);
			insertW_code(db);
		}
		if(newVersion == 5){
			switch(oldVersion){
			case 1:
				createTable(db, PayTypeBean.class);
				initPayTypeBean(db);
				createTable(db, AppRestarTime.class);
				createTable(db, RecoveryWayBean.class);
				createTable(db, RecoveryRecordsBean.class);
				createTable(db, MachineSetBean.class);
				initMachineSetBean(db);
				upgradeTables(db, WayBean.class, ADD_W_CODE);
				insertW_code(db);
				break;
			case 2:
				createTable(db, RecoveryWayBean.class);
				createTable(db, RecoveryRecordsBean.class);
				createTable(db, MachineSetBean.class);
				initMachineSetBean(db);
				upgradeTables(db, WayBean.class, ADD_W_CODE);
				insertW_code(db);
				break;
			case 3:
				createTable(db, MachineSetBean.class);
				initMachineSetBean(db);
				upgradeTables(db, WayBean.class, ADD_W_CODE);
				insertW_code(db);
				break;
			case 4:
				
				break;
			}
			FileUtil.renameFile();
			createTable(db, AdStatusBean.class);
		}
		if(newVersion == 6){
			switch(oldVersion){
			case 1:
				createTable(db, PayTypeBean.class);
				initPayTypeBean(db);
				createTable(db, AppRestarTime.class);
				createTable(db, RecoveryWayBean.class);
				createTable(db, RecoveryRecordsBean.class);
				createTable(db, MachineSetBean.class);
				initMachineSetBean(db);
				upgradeTables(db, WayBean.class, ADD_W_CODE);
				insertW_code(db);
				FileUtil.renameFile();
				createTable(db, AdStatusBean.class);
				break;
			case 2:
				createTable(db, RecoveryWayBean.class);
				createTable(db, RecoveryRecordsBean.class);
				createTable(db, MachineSetBean.class);
				initMachineSetBean(db);
				upgradeTables(db, WayBean.class, ADD_W_CODE);
				insertW_code(db);
				FileUtil.renameFile();
				createTable(db, AdStatusBean.class);
				break;
			case 3:
				createTable(db, MachineSetBean.class);
				initMachineSetBean(db);
				upgradeTables(db, WayBean.class, ADD_W_CODE);
				insertW_code(db);
				FileUtil.renameFile();
				createTable(db, AdStatusBean.class);
				break;
			case 4:
				FileUtil.renameFile();
				createTable(db, AdStatusBean.class);
				break;
			case 5:
				break;
				
			}
			File sdFiles = mContext.getExternalFilesDir(null);
			String setDir = sdFiles.getParent()+"/set";
			FileUtil.CopyAssets(mContext, "set", setDir);
			
			String mediaDir = FileUtil.ROOT_PATH + FileUtil.SECOND_MEDIA_PATH;
			FileUtil.CopyAssets(mContext, "media", mediaDir);
		}
		if(newVersion == 7){
			File sdFiles = mContext.getExternalFilesDir(null);
			String setDir = sdFiles.getParent()+"/set";
			String mediaDir = FileUtil.ROOT_PATH + FileUtil.SECOND_MEDIA_PATH;
			switch(oldVersion){
			case 1:
				createTable(db, PayTypeBean.class);
				initPayTypeBean(db);
				createTable(db, AppRestarTime.class);
				createTable(db, RecoveryWayBean.class);
				createTable(db, RecoveryRecordsBean.class);
				createTable(db, MachineSetBean.class);
				initMachineSetBean(db);
				upgradeTables(db, WayBean.class, ADD_W_CODE);
				insertW_code(db);
				FileUtil.renameFile();
				createTable(db, AdStatusBean.class);
				
				FileUtil.CopyAssets(mContext, "set", setDir);
				FileUtil.CopyAssets(mContext, "media", mediaDir);
				break;
			case 2:
				createTable(db, RecoveryWayBean.class);
				createTable(db, RecoveryRecordsBean.class);
				createTable(db, MachineSetBean.class);
				initMachineSetBean(db);
				upgradeTables(db, WayBean.class, ADD_W_CODE);
				insertW_code(db);
				FileUtil.renameFile();
				createTable(db, AdStatusBean.class);
				FileUtil.CopyAssets(mContext, "set", setDir);
				FileUtil.CopyAssets(mContext, "media", mediaDir);
				break;
			case 3:
				createTable(db, MachineSetBean.class);
				initMachineSetBean(db);
				upgradeTables(db, WayBean.class, ADD_W_CODE);
				insertW_code(db);
				FileUtil.renameFile();
				createTable(db, AdStatusBean.class);
				FileUtil.CopyAssets(mContext, "set", setDir);
				FileUtil.CopyAssets(mContext, "media", mediaDir);
				break;
			case 4:
				FileUtil.renameFile();
				createTable(db, AdStatusBean.class);
				FileUtil.CopyAssets(mContext, "set", setDir);
				FileUtil.CopyAssets(mContext, "media", mediaDir);
				break;
			case 5:
				FileUtil.CopyAssets(mContext, "set", setDir);
				FileUtil.CopyAssets(mContext, "media", mediaDir);
				break;
			case 6:
				break;
			}
			upgradeTables(db, MachineSetBean.class, ADD_PHYSICSBUTTONSTATUS);
			insertPhysicsButtonStatus(db);
		}
		if(newVersion == 8){
			File sdFiles = mContext.getExternalFilesDir(null);
			String setDir = sdFiles.getParent()+"/set";
			String mediaDir = FileUtil.ROOT_PATH + FileUtil.SECOND_MEDIA_PATH;
			switch(oldVersion){
			case 1:
				createTable(db, PayTypeBean.class);
				initPayTypeBean(db);
				createTable(db, AppRestarTime.class);
				createTable(db, RecoveryWayBean.class);
				createTable(db, RecoveryRecordsBean.class);
				createTable(db, MachineSetBean.class);
				initMachineSetBean(db);
				upgradeTables(db, WayBean.class, ADD_W_CODE);
				insertW_code(db);
				FileUtil.renameFile();
				createTable(db, AdStatusBean.class);
				
				FileUtil.CopyAssets(mContext, "set", setDir);
//				FileUtil.CopyAssets(mContext, "media", mediaDir);
				upgradeTables(db, MachineSetBean.class, ADD_PHYSICSBUTTONSTATUS);
				insertPhysicsButtonStatus(db);
				break;
			case 2:
				createTable(db, RecoveryWayBean.class);
				createTable(db, RecoveryRecordsBean.class);
				createTable(db, MachineSetBean.class);
				initMachineSetBean(db);
				upgradeTables(db, WayBean.class, ADD_W_CODE);
				insertW_code(db);
				FileUtil.renameFile();
				createTable(db, AdStatusBean.class);
				FileUtil.CopyAssets(mContext, "set", setDir);
//				FileUtil.CopyAssets(mContext, "media", mediaDir);
				upgradeTables(db, MachineSetBean.class, ADD_PHYSICSBUTTONSTATUS);
				insertPhysicsButtonStatus(db);
				break;
			case 3:
				createTable(db, MachineSetBean.class);
				initMachineSetBean(db);
				upgradeTables(db, WayBean.class, ADD_W_CODE);
				insertW_code(db);
				FileUtil.renameFile();
				createTable(db, AdStatusBean.class);
				FileUtil.CopyAssets(mContext, "set", setDir);
//				FileUtil.CopyAssets(mContext, "media", mediaDir);
				upgradeTables(db, MachineSetBean.class, ADD_PHYSICSBUTTONSTATUS);
				insertPhysicsButtonStatus(db);
				break;
			case 4:
				FileUtil.renameFile();
				createTable(db, AdStatusBean.class);
				FileUtil.CopyAssets(mContext, "set", setDir);
//				FileUtil.CopyAssets(mContext, "media", mediaDir);
				upgradeTables(db, MachineSetBean.class, ADD_PHYSICSBUTTONSTATUS);
				insertPhysicsButtonStatus(db);
				break;
			case 5:
				FileUtil.CopyAssets(mContext, "set", setDir);
//				FileUtil.CopyAssets(mContext, "media", mediaDir);
				upgradeTables(db, MachineSetBean.class, ADD_PHYSICSBUTTONSTATUS);
				insertPhysicsButtonStatus(db);
				break;
			case 6:
				upgradeTables(db, MachineSetBean.class, ADD_PHYSICSBUTTONSTATUS);
				insertPhysicsButtonStatus(db);
				break;
			case 7:
				insertPhysicsButtonStatus(db);
				break;
			}
			FileUtil.CopyAssets(mContext, "media", mediaDir);
		}
		if(newVersion == 10){
			createTable(db, LYMachineSetBean.class);
			insertLYMachineSetBean(db);
			upgradeTables(db, LYMachineSetBean.class, ADD_W_TYPE);
			insertW_type(db);
		}
		CommonUtils.showLog(CommonUtils.DATABASE_TAG,"db onUpgrade");
	}
	/**
	 * 执行数据库的降级操作
	 * 1、只有新版本比旧版本低的时候才会执行
	 * 2、如果不执行降级操作，会抛出异常
	 */
	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		CommonUtils.showLog(CommonUtils.DATABASE_TAG,"db onDowngrade");
		super.onDowngrade(db, oldVersion, newVersion);
	}

	/**
	 * 创建所有表
	 * 
	 * 
	 * @return
	 */
	private void createTable(SQLiteDatabase db,Class<?>[] classList) {
		for (int i = 0; i < classList.length; i++) {
			Class<?> c = classList[i];
			createTable(db, c);
		}
	}
	void createTable(SQLiteDatabase db,Class c){
		String tableName = CommonUtils.getClassName(c);
		String text = "create table " + tableName
				+ "(id integer primary key autoincrement,";
		Field[] field = c.getDeclaredFields(); // 获取实体类的所有属性，返回Field数组
		for (int j = 0; j < field.length; j++) { // 遍历所有属性
			String name = field[j].getName(); // 获取属性的名字
			String type = field[j].getGenericType().toString(); // 获取属性的类型
			if (type.equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
				text += name + " text,";
			}
			if (type.equals("class java.lang.Integer")) {
				text += name + " integer,";
			}
			if (type.equals("class java.lang.Float")) {
				text += name + " float,";
			}
			if (type.equals("class java.lang.Double")) {
			}
			if (type.equals("class java.lang.Boolean")) {
			}
			if (type.equals("class java.util.Date")) {
			}
//			if (field.length - 1 != j)
//				text += " ,";
		}
		text = text.substring(0,text.length() - 1);
		text += ")";
		db.execSQL(text);
		
	}
	// 升级数据库字段
	private void upgradeTables(SQLiteDatabase db, Class c, int Flag) {
		try {
			String tableName = CommonUtils.getClassName(c);
			db.beginTransaction();
			// 1, Rename table.
			String tempTableName = tableName + "_temp";
			String sql = "ALTER TABLE " + tableName + " RENAME TO " + tempTableName;
			db.execSQL(sql);
			// 2, Create table.
			createTable(db, c);
			// 3, Load data
			switch(Flag){
			case EXAMPLE:
				sql = "INSERT INTO " + tableName + " select id ,cashIn,cashOut,client_order_no,expand,g_code,o_amount,o_complete_time,o_generate_time,o_number,o_pay_state,o_state,o_unit_price,p_state,pay_type,\"\",total_price,w_code FROM " + tempTableName + "";
				break;
			case ADD_FIELD_MachineBean:
				break;
			case ADD_W_CODE:
				sql = "INSERT INTO " + tableName + " select id ,g_code,maxNum,status,storeNum,\"\" ,way_id FROM " + tempTableName + "";
				break;
			case ADD_PHYSICSBUTTONSTATUS:
				sql = "INSERT INTO " + tableName + " select id,coinAndnoteStatus,coinWaitTime ,\"\" FROM " + tempTableName + "";
				break;
			case ADD_W_TYPE:
				sql = "INSERT INTO " + tableName + " select id ,g_code,maxNum,status,storeNum,w_code,\"\",way_id FROM " + tempTableName + "";
				break;
			}
			
			CommonUtils.showLog(CommonUtils.DATABASE_TAG, sql);
			db.execSQL(sql);
			// 4, delete data
			db.execSQL("DROP TABLE IF EXISTS " + tempTableName);
			db.setTransactionSuccessful();
		} catch (Exception e) {
			CommonUtils.showLog(CommonUtils.DATABASE_TAG, "exception = " + e.toString());
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}
	/**
	 * 初始化支付方式数据库
	 * 默认打开 现金支付，易支付
	 */
	void initPayTypeBean(SQLiteDatabase db){
		for(int i = 0;i < PayType.values().length;i++){
			PayTypeBean payTypeBean = new PayTypeBean();
			Integer payType = PayType.values()[i].value();
			if(payType == PayType.cash.value() || payType == PayType.wechat.value() || payType == PayType.ali.value())
				payTypeBean.setPayStatus(1);
			else
				payTypeBean.setPayStatus(0);
			payTypeBean.setPayType(payType);
			ContentValues cv = new ContentValues();
			cv.put("payStatus", payTypeBean.getPayStatus());
			cv.put("payType", payTypeBean.getPayType());
			String tableName = CommonUtils.getClassName(PayTypeBean.class);
			db.insert(tableName, null, cv);
		}
	}
	/**
	 * 插入w_code
	 * @param db
	 */
	void insertW_code(SQLiteDatabase db){
		Cursor cursor = db.rawQuery("select * from WayBean", null);
		int count  = cursor.getCount();
		cursor.moveToFirst();
		for(int i = 0;i < count;i++){
			String way_id = cursor.getString(cursor.getColumnIndex("way_id"));
			db.execSQL("UPDATE WayBean SET w_code = '" + GoodsWayUtil.getWayName(way_id) + "' where way_id = '" + way_id + "'");
			cursor.moveToNext();
		}
		cursor.close();
	}
	/**
	 * 初始化机器设备设置
	 */
	void initMachineSetBean(SQLiteDatabase db){
		ContentValues cv = new ContentValues();
		cv.put("coinAndnoteStatus", "1");
		cv.put("coinWaitTime", "15");
		String tableName = CommonUtils.getClassName(MachineSetBean.class);
		db.insert(tableName, null, cv);
	}
	/**
	 * 插入physicsButtonStatus
	 * @param db
	 */
	void insertPhysicsButtonStatus(SQLiteDatabase db){
		Cursor cursor = db.rawQuery("select * from MachineSetBean", null);
		int count = cursor.getCount();
		if(count > 0){
			db.execSQL("update MachineSetBean set physicsButtonStatus = '1'");
		}
		cursor.close();
	}
	/**
	 * 初始化雷云峰机器设备设置
	 * @param db
	 */
	void insertLYMachineSetBean(SQLiteDatabase db){
		ContentValues cv = new ContentValues();
		cv.put("doorStatus", "1");
		cv.put("elevatorStatus", "1");
		String tableName = CommonUtils.getClassName(LYMachineSetBean.class);
		db.insert(tableName, null, cv);
	}
	/**
	 * 插入w_type
	 * @param db
	 */
	void insertW_type(SQLiteDatabase db){
		Cursor cursor = db.rawQuery("select * from WayBean", null);
		int count  = cursor.getCount();
		cursor.moveToFirst();
		for(int i = 0;i < count;i++){
			db.execSQL("UPDATE WayBean SET w_type = '0'");
			cursor.moveToNext();
		}
		cursor.close();
	}
}
