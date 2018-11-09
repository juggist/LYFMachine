package com.icoffice.library.app;
/**
 * author: Michael.Lu
 */
import android.app.Activity;
import android.app.Application;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.icoffice.library.bean.db.PayTypeBean;
import com.icoffice.library.listener.CofficePhoneStateListener;
import com.icoffice.library.moudle.control.BaseMachineControl;
import com.icoffice.library.moudle.control.FeedWatchDog;
import com.icoffice.library.utils.CommonUtils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
@SuppressWarnings("rawtypes")
public abstract class BaseApplication extends Application {
	private BaseMachineControl mBaseMachineControl;
	private ArrayList<PayTypeBean> payTypeBean_list = new ArrayList<PayTypeBean>();//所有支付方式集合
	protected Class c;//启动app 的activity
	protected String className;//启动app 的activity名称
	private String coinAndnoteStatus = "";//纸硬币起状态
	private String coinWaitTime = "";//投币等待时间
	private String physicsButtonStatus = "";//物理选货按钮状态
	
	@Override
	public void onCreate() {
		watchDog();
		initImageLoader();
		super.onCreate();
	}

	public void initBaseMachineControl(BaseMachineControl baseMachineControl){
		mBaseMachineControl = baseMachineControl;
		//异常捕获
//		CrashHandler crashHandler = CrashHandler.getInstance();
//		crashHandler.init(this,mBaseMachineControl);
		//创建本地文件夹
		mBaseMachineControl.createFile();
		// 注册状态通知
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		telephonyManager.listen(new CofficePhoneStateListener(baseMachineControl),
				PhoneStateListener.LISTEN_SIGNAL_STRENGTHS
						| PhoneStateListener.LISTEN_SERVICE_STATE);
	}
	
	public BaseMachineControl getBaseMachineControl() {
		return mBaseMachineControl;
	}
	
	
	public ArrayList<PayTypeBean> getPayTypeBean_list() {
		return payTypeBean_list;
	}

	public void setPayTypeBean_list(ArrayList<PayTypeBean> payTypeBean_list) {
		this.payTypeBean_list = payTypeBean_list;
	}
	/**
	 * 设置启动app入口的activity
	 */
	public abstract void setC();
	/**
	 * 设置app入口的activity的完整路径
	 */
	public abstract void setClassName();
	
	public Class getC() {
		return c;
	}

	public String getClassName() {
		return className;
	}
	

	public String getCoinAndnoteStatus() {
		CommonUtils.showLog(CommonUtils.TAG, "CoinAndnoteStatus = " + coinAndnoteStatus);
		return coinAndnoteStatus;
	}

	public void setCoinAndnoteStatus(String coinAndnoteStatus) {
		this.coinAndnoteStatus = coinAndnoteStatus;
	}
	

	public String getCoinWaitTime() {
		return coinWaitTime;
	}

	public void setCoinWaitTime(String coinWaitTime) {
		this.coinWaitTime = coinWaitTime;
	}

	
	public String getPhysicsButtonStatus() {
		return physicsButtonStatus;
	}

	public void setPhysicsButtonStatus(String physicsButtonStatus) {
		this.physicsButtonStatus = physicsButtonStatus;
	}


	/**
	 * 运用list来保存们每一个activity是关键
	 */
    private List<Activity> mList = new LinkedList<Activity>();  
    
    public void addActivity(Activity activity) {   
        mList.add(activity);   
    }   
    //关闭每一个list内的activity  
    public void exit() {   

        try {   
            for (Activity activity:mList) {   
                if (activity != null)   {
                	 activity.finish(); 
                }
                     
            }
        } catch (Exception e) {   
            e.printStackTrace();   
        } finally {   
        	CommonUtils.showLog(CommonUtils.APP_TAG,"app被摧毁");
    		ImageLoader.getInstance().clearMemoryCache();
    		ImageLoader.getInstance().clearDiscCache();
        	mBaseMachineControl.mRootUtil.showStatusBar();
            System.exit(0);  
//        	onLowMemory();
            
        }   
    }   
    //杀进程  
    public void onLowMemory() {   
        super.onLowMemory();       
        System.gc();   
    }

	public void onDestroy() {
	}
    
	/**
	 * 看门狗 
	 * 硬件检测app是否在正常运行
	 */
	void watchDog() {
		try {
			FeedWatchDog mFeedWatchDog = FeedWatchDog.getInstance(this);
			/* 每隔5秒喂一次狗 */
			mFeedWatchDog.schedule(0, 1000 * 5);
		} catch (Exception e) {

		}
	}
	/**
	 * 初始化imageloader工具类
	 * 需要第三方jar包 universal-image-loader-1.8.6-with-sources.jar
	 */
	void initImageLoader() {
//		File cacheDir =StorageUtils.getOwnCacheDirectory(this, "imageloader/Cache");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this).denyCacheImageMultipleSizesInMemory()
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				.memoryCacheSize(2 * 1024 * 1024)
				.discCacheSize(50 * 1024 * 1024)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
//				.discCache(new UnlimitedDiscCache(cacheDir))
				//没有图片资源时的默认图片  
//				.showImageOnFail(R.drawable.buy_back) 				 //加载失败时的图片
				.discCacheFileCount(100).build();
		
		ImageLoader.getInstance().init(config);
	}

}
