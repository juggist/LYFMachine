package com.icoffice.library.utils;
/**
 * author: Michael.Lu
 */
import java.util.ArrayList;

import com.icoffice.library.bean.UserBean;
import com.icoffice.library.bean.db.MachineBean;
import com.icoffice.library.handler.BaseHttpHandler;
import com.icoffice.library.utils.net.AsyncHttpPost;
import com.icoffice.library.utils.net.DefaultThreadPool;
import com.icoffice.library.utils.net.RequestParameter;
import com.icoffice.library.utils.net.RequestResultCallback;

public class NetWork {
	public final static int POST_ENTER = 1;//登入
	public final static int POST_REGISTER_PORT = 2;//注册机器
	public final static int STORE_GOODS = 4;
	public final static int POST_UPDATE = 6;
	public final static int POST_LOG = 7;
	public final static int POST_ACTIVITY = 8;
	public final static int POST_ADD_FOOD = 15;
	public final static int POST_FAILORDERS = 16;
	public final static int POST_BREAK = 17;
	public final static int POST_DOOR_INFO = 18;
	public final static int POST_ERROR = 19;
	public final static int POST_EXCHANGE_INFO = 20;
	public final static int POST_EXCHANGE_DETAIL = 21;
	public final static int POST_ACTIVITY_INFO = 22;
	public final static int POST_ACTIVITY_DETAIL = 23;
	
	public final static int POST_WAY_STATE = 24;//售货机货道货品销售状态及最大库存调整
	public final static int POST_WAYANDGOODS_STATE = 25;//售货机库存更改
	public final static int POST_GOODS_STATE = 26;//售货机货品销售状态调整
	
	public final static int POST_CHECK_RCODE = 31;//检测微商城兑换码,并范围兑换的产品信息
	public final static int POST_ADD_CHANGE_LOG = 32;//微商城兑换码,增加兑换记录
	public final static int POST_QUIT_CONVERT = 33;//微商城兑换码,退出兑换（解除锁定）
	
	public final static int POST_TEMPLATE = 34;//获取模板

	public static NetWork instance = null;
	public MachineBean mMachineBean;
	public String BASE_URL;
	public String WECHAT_BASE_URL;//微商城接口
	public String ACTIVITY_BASE_URL;//活动接口
	
	public BaseHttpHandler mHttpHandler;
	public UserBean mUserBean;

	private NetWork() {
	}

	public static NetWork getInstance() {
		if (instance == null) {
			instance = new NetWork();
		}
		return instance;
	}
	
	public void setmUserBean(UserBean mUserBean) {
		this.mUserBean = mUserBean;
	}

	private String getUrl(int i) {
		String url = "";
		
		String m_code = "";
		if(mMachineBean != null)
			m_code = mMachineBean.getM_code();
		switch (i) {
		case POST_ENTER:
			url = BASE_URL + "machine_oam_a/login/";// 登入接口
			break;
		case POST_REGISTER_PORT:
			url = BASE_URL + "machine_oam_a/register_machine/";// 注册机器mac地址接口
			break;
		case STORE_GOODS:
			url = BASE_URL + "store_change/";// 库存消耗接口
			break;
		case POST_GOODS_STATE:
			url = BASE_URL + "machine_oam_a/store_state/";// 售货机货品销售状态调整接口
			break;
		case POST_UPDATE:
			url = BASE_URL + "download_apk/";// apk更新接口
			break;
		case POST_LOG:
			url = BASE_URL + "upload_log/";// 提交日志接口
			break;
		case POST_ACTIVITY:
			url = BASE_URL + "a_goods/";// 获取活动商品详情的接口
			break;
		case POST_ADD_FOOD:
			url = BASE_URL + "api_store_change/admin";// 补货接口
			break;
		case POST_FAILORDERS:
			url = BASE_URL + "order_fail/";// 支付宝出货失败返回金额接口
			break;
		case POST_BREAK:
			url = BASE_URL + "heart_connect/";// 心跳包接口
			break;
		case POST_DOOR_INFO:
			url = BASE_URL + "machine_info_update/";// 机器状态修改接口
			break;
		case POST_ERROR:
			url = BASE_URL + "exception_record/";// 所有异常接口
			break;
		case POST_CHECK_RCODE:// 微商城兑换码访问接口
			url = WECHAT_BASE_URL + "chk_rcode/";
			m_code = "";
			break;
		case POST_ADD_CHANGE_LOG://微商城兑换码增加兑换记录接口
			url = WECHAT_BASE_URL + "add_change_log/";
			m_code = "";
			break;
		case POST_QUIT_CONVERT:// 微商城兑换码访问接口 退出兑换（解除锁定）
			url = WECHAT_BASE_URL + "quit_convert/";
			m_code = "";
			break;
		case POST_EXCHANGE_DETAIL:
			url = WECHAT_BASE_URL + "use_r_code/";// 微商城兑换码提货接口
			break;
		case POST_ACTIVITY_INFO:
			url = ACTIVITY_BASE_URL + "check_r_code/";// 获取活动商品详情的接口
			break;
		case POST_ACTIVITY_DETAIL:
			url = ACTIVITY_BASE_URL + "use_r_code/";// 获取活动商品出货接口
			break;
		case POST_WAY_STATE:
			url = BASE_URL + "machine_oam_a/way_state/";
			break;
		case POST_WAYANDGOODS_STATE:
			url = BASE_URL + "machine_oam_a/store_change/";
			break;
		case POST_TEMPLATE:
			url = BASE_URL + "machine_oam_a/get_store_template/";
			break;
		default:
			break;
		}
		url += m_code;
		CommonUtils.showLog(CommonUtils.NETWORK_TAG,"url = " + url);
		return url;
	}

	/**
	 * 通用数据请求
	 */
	public void getDataFromHttp(int url, ArrayList<RequestParameter> parameter,
			RequestResultCallback mRequestResultCallback) {
		AsyncHttpPost post = new AsyncHttpPost(null, getUrl(url), parameter,
				mRequestResultCallback);
		DefaultThreadPool.getInstance().execute(post);
	}
	/**
	 * 通用数据请求
	 */
	public void postDataFromHttp(int url, ArrayList<RequestParameter> parameter,
			RequestResultCallback mRequestResultCallback) {
		AsyncHttpPost post = new AsyncHttpPost(null, getUrl(url), parameter,
				mRequestResultCallback);
		DefaultThreadPool.getInstance().execute(post);
	}
	/**
	 * 查询机器编号
	 */
	public void checkM_No(String mac,String m_no, String factory_code,String Mt_id){
		ArrayList<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("mac", mac));
		parameter.add(new RequestParameter("m_no",m_no));
		parameter.add(new RequestParameter("factory_code", factory_code));
		parameter.add(new RequestParameter("mt_id",Mt_id));
		postDataFromHttp(POST_REGISTER_PORT, parameter,
				new RequestResultCallback() {

					@Override
					public void onSuccess(Object o) {
						CommonUtils.showLog(CommonUtils.NETWORK_TAG,"checkM_No success = " +  (String) o);
						CommonUtils.sendMessage(mHttpHandler, BaseHttpHandler.POST_CHECK_M_NO_SUCCESS, (String) o);
					}

					@Override
					public void onFail(Exception e) {
						CommonUtils.showLog(CommonUtils.NETWORK_TAG,"checkM_No fail = " +  e.toString());
						CommonUtils.sendMessage(mHttpHandler, BaseHttpHandler.POST_CHECK_M_NO_FAIL, e.toString());
					}
				});
	}
	/**
	 * 注册机器
	 */
	public void register(String mac,String m_no, String factory_code,String Mt_id){
		ArrayList<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("mac", mac));
		parameter.add(new RequestParameter("m_no",m_no));
		parameter.add(new RequestParameter("factory_code", factory_code));
		parameter.add(new RequestParameter("mt_id",Mt_id));
		postDataFromHttp(POST_REGISTER_PORT, parameter,
				new RequestResultCallback() {

					@Override
					public void onSuccess(Object o) {
						CommonUtils.showLog(CommonUtils.NETWORK_TAG,"connect success = " +  (String) o);
						CommonUtils.sendMessage(mHttpHandler, BaseHttpHandler.POST_REGISTER_SUCCESS, (String) o);
					}

					@Override
					public void onFail(Exception e) {
						CommonUtils.showLog(CommonUtils.NETWORK_TAG,"connect fail = " +  e.toString());
						CommonUtils.sendMessage(mHttpHandler, BaseHttpHandler.POST_REGISTER_FAIL, e.toString());
					}
				});
	}
	/**
	 * 修改商品状态
	 * @param status
	 * @param u_id
	 */
	public void postGoodsState(String status){
		CommonUtils.showLog(CommonUtils.NETWORK_TAG,"postGoodsState status = " + status);
		ArrayList<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("status", status));
		parameter.add(new RequestParameter("u_id", mUserBean.getU_id()));
		postDataFromHttp(POST_GOODS_STATE, parameter,
				new RequestResultCallback() {

					@Override
					public void onSuccess(Object o) {
						CommonUtils.sendMessage(mHttpHandler, BaseHttpHandler.POST_GOODSSTATE_SUCCESS, (String) o);
					}

					@Override
					public void onFail(Exception e) {
						CommonUtils.showLog(CommonUtils.NETWORK_TAG,"goods fail = " +  e.toString());
						CommonUtils.sendMessage(mHttpHandler, BaseHttpHandler.POST_GOODSSTATE_FAIL, e.toString());
					}
				});
	}
	/**
	 * 修改货道状态
	 * 需要把所有在售的货道均发送给服务器，不能仅发送修改的
	 * @param status
	 */
	public void post_way_state(String status){
		ArrayList<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("status", status));
		CommonUtils.showLog(CommonUtils.NETWORK_TAG,"post_way_state status = " + status);
		postDataFromHttp(POST_WAY_STATE, parameter,
				new RequestResultCallback() {

					@Override
					public void onSuccess(Object o) {
						CommonUtils.showLog(CommonUtils.NETWORK_TAG,"way success = " + (String) o);
						CommonUtils.sendMessage(mHttpHandler, BaseHttpHandler.POST_WAY_STATE_SUCCESS, (String) o);
					}

					@Override
					public void onFail(Exception e) {
						CommonUtils.showLog(CommonUtils.NETWORK_TAG,"way fail = " + e.toString());
						CommonUtils.sendMessage(mHttpHandler, BaseHttpHandler.POST_WAY_STATE_FAIL, e.toString());
					}
				});
	}
	/**
	 * 补货接口
	 * @param records0
	 * @param records1
	 * @param records2
	 */
	public void post_wayAndGoods_state(String records0,String records1,String records2,String number){
		ArrayList<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("u_id", mUserBean.getU_id()));
		parameter.add(new RequestParameter("records0", records0));
		parameter.add(new RequestParameter("records1", records1));
		parameter.add(new RequestParameter("records2", records2));
		parameter.add(new RequestParameter("number", number));
		CommonUtils.showLog(CommonUtils.NETWORK_TAG,"post_wayAndGoods_state:records0 = " + records0);
		CommonUtils.showLog(CommonUtils.NETWORK_TAG,"post_wayAndGoods_state:records1 = " + records1);
		CommonUtils.showLog(CommonUtils.NETWORK_TAG,"post_wayAndGoods_state:records2 = " + records2);
		CommonUtils.showLog(CommonUtils.NETWORK_TAG,"post_wayAndGoods_state:number = " + number);
		postDataFromHttp(POST_WAYANDGOODS_STATE, parameter,
				new RequestResultCallback() {

					@Override
					public void onSuccess(Object o) {
						CommonUtils.sendMessage(mHttpHandler, BaseHttpHandler.POST_WAYANDGOODSSTATE_SUCCESS, (String) o);
					}

					@Override
					public void onFail(Exception e) {
						CommonUtils.sendMessage(mHttpHandler, BaseHttpHandler.POST_WAYANDGOODSSTATE_FAIL, e.toString());
					}
				});
	}
	/**
	 * 用户登入接口
	 * @param str_account
	 * @param str_pwd
	 */
	public void post_userEnter(String str_account,String str_pwd,String factory_code){
		ArrayList<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("u_code", str_account));
		parameter.add(new RequestParameter("password", EncodeUtil.getMD5(str_pwd.getBytes())));
		parameter.add(new RequestParameter("m_code", mMachineBean.getM_code()));
		parameter.add(new RequestParameter("factory_code", factory_code));
		postDataFromHttp(POST_ENTER, parameter, new RequestResultCallback() {

			@Override
			public void onSuccess(Object o) {
				CommonUtils.sendMessage(mHttpHandler,BaseHttpHandler.POST_USERENTER_SUCCESS, (String) o);
			}

			@Override
			public void onFail(Exception e) {
				CommonUtils.sendMessage(mHttpHandler,BaseHttpHandler.POST_USERENTER_FAIL, e.toString());
			}
		});
	}
	/**
	 * 检测微商城兑换码,并范围兑换的产品信息
	 * @param rcode
	 */
	public void post_chk_rcode(String rcode){
		ArrayList<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("rcode", rcode));
		parameter.add(new RequestParameter("m_code", mMachineBean.getM_code()));
		CommonUtils.showLog(CommonUtils.NETWORK_TAG,"post_chk_rcode:rcode = " + rcode + ";m_code = " + mMachineBean.getM_code());
		postDataFromHttp(POST_CHECK_RCODE, parameter, new RequestResultCallback() {

			@Override
			public void onSuccess(Object o) {
				CommonUtils.sendMessage(mHttpHandler,BaseHttpHandler.POST_CHECK_RCODE_SUCCESS, (String) o);
				CommonUtils.showLog(CommonUtils.NETWORK_TAG,"post_chk_rcode:result = " + (String) o);
			}

			@Override
			public void onFail(Exception e) {
				CommonUtils.sendMessage(mHttpHandler,BaseHttpHandler.POST_CHECK_RCODE_FAIL, e.toString());
				CommonUtils.showLog(CommonUtils.NETWORK_TAG,"post_chk_rcode:fail = " + e.toString());
			}
		});
	}
	/**
	 * 测试兑换 本地数据
	 */
	public void test_post_chk_rcode(String rcode){
		CommonUtils.sendMessage(mHttpHandler,BaseHttpHandler.POST_TEST_CHECK_M_NO_SUCCESS, "");
	}
	/**
	 * 增加兑换记录
	 * @param order_gl_id
	 * @param g_id
	 * @param rid
	 */
	public void post_add_change_log(String order_gl_id,String g_id,String rid,String onumber){
		ArrayList<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("order_gl_id", order_gl_id));
		parameter.add(new RequestParameter("m_code", mMachineBean.getM_code()));
		parameter.add(new RequestParameter("g_id", g_id));
		parameter.add(new RequestParameter("rid", rid));
		parameter.add(new RequestParameter("onumber", onumber));
		CommonUtils.showLog(CommonUtils.NETWORK_TAG,"post_add_change_log:order_gl_id = " + order_gl_id + ";g_id = " + g_id + ";rid = " + rid + ";m_code = " + mMachineBean.getM_code());
		postDataFromHttp(POST_ADD_CHANGE_LOG, parameter, new RequestResultCallback() {

			@Override
			public void onSuccess(Object o) {
				CommonUtils.sendMessage(mHttpHandler,BaseHttpHandler.POST_ADD_CHANGE_LOG_SUCCESS, (String) o);
				CommonUtils.showLog(CommonUtils.NETWORK_TAG,"post_add_change_log:post_add_change_log result = " + (String) o);
			}

			@Override
			public void onFail(Exception e) {
				CommonUtils.sendMessage(mHttpHandler,BaseHttpHandler.POST_ADD_CHANGE_LOG_FAIL, e.toString());
				CommonUtils.showLog(CommonUtils.NETWORK_TAG,"post_add_change_log:fail = " + e.toString());
			}
		});
	}
	
	/**
	 * 退出兑换（解除锁定）
	 * @param rid
	 */
	public void post_quit_convert(String rid){
		ArrayList<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("m_code", mMachineBean.getM_code()));
		parameter.add(new RequestParameter("rid", rid));
		CommonUtils.showLog(CommonUtils.NETWORK_TAG,"post_quit_convert:rid = " + rid);
		postDataFromHttp(POST_QUIT_CONVERT, parameter, new RequestResultCallback() {

			@Override
			public void onSuccess(Object o) {
				CommonUtils.sendMessage(mHttpHandler,BaseHttpHandler.POST_QUIT_CONVERT_SUCCESS, (String) o);
				CommonUtils.showLog(CommonUtils.NETWORK_TAG,"post_quit_convert:result = " + (String) o);
			}

			@Override
			public void onFail(Exception e) {
				CommonUtils.sendMessage(mHttpHandler,BaseHttpHandler.POST_QUIT_CONVERT_FAIL, e.toString());
				CommonUtils.showLog(CommonUtils.NETWORK_TAG,"post_quit_convert:fail = " + e.toString());
			}
		});
	}
	/**
	 * 获取模板
	 * @param mw_codes
	 */
	public void post_template(String cabinet){
		ArrayList<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("cabinet",cabinet));
		CommonUtils.showLog(CommonUtils.NETWORK_TAG,"post_template:cabinet = " + cabinet);
		postDataFromHttp(POST_TEMPLATE, parameter, new RequestResultCallback() {

			@Override
			public void onSuccess(Object o) {
				CommonUtils.sendMessage(mHttpHandler,BaseHttpHandler.POST_TEMPLATE_SUCCESS, (String) o);
				CommonUtils.showLog(CommonUtils.NETWORK_TAG,"post_template:result = " + (String) o);
			}

			@Override
			public void onFail(Exception e) {
				CommonUtils.sendMessage(mHttpHandler,BaseHttpHandler.POST_TEMPLATE_FAIL, e.toString());
				CommonUtils.showLog(CommonUtils.NETWORK_TAG,"post_template:fail = " + e.toString());
			}
		});
	}
}
