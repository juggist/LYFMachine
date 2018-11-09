package com.icoffice.library.service;//package com.icoffice.library.service;
//
//import java.io.File;
//import java.io.IOException;
//
//import android.app.Service;
//import android.content.Intent;
//import android.media.MediaPlayer;
//import android.os.Binder;
//import android.os.IBinder;
//
//import com.icoffice.library.utils.CommonUtils;
//import com.icoffice.library.utils.FileUtil;
//
///**
// * 为了可以使得在后台播放音乐，我们需要Service
// * Service就是用来在后台完成一些不需要和用户交互的动作
// * @author Administrator
// *
// */
//public class AudioService extends Service implements MediaPlayer.OnCompletionListener{
//	private String[] sound_array = new String[]{
//			"open_alipay.mp3","paying.mp3","pay_success.mp3","outgoods_success.mp3","plz_wait.mp3","",
//												//01_请点击您想购买的商品，上下翻页查看更多选择
//												//02_网络不稳定，请改用现金购买
//												//03_请打开微信扫一扫，扫描屏幕上的二维码
//												//04_出货成功，请从取物口领取商品
//												//05_支付超时，请重新选购
//												//10_请投币
//												//11_出货成功，请从取物口领取商品，如需退币请按退币钮
//												//14_请输入兑换码，并按确定按钮
//												//15_请选择您想领取的商品
//												//17_请将客非卡靠近感应区
//												//18_出货失败，请联系客服
//												//19_机器维护中，如有疑问请联系客服
//												//20_请选择支付方式
//			"01.mp3","02.mp3","03.mp3","04.mp3","05.mp3",
//			"10.mp3","11.mp3","14.mp3","15.mp3","17.mp3",
//			"18.mp3","19.mp3","20.mp3"
//			
//	};
//	private MediaPlayer player;
//	
//	private final IBinder binder = new AudioBinder();
//	@Override
//	public IBinder onBind(Intent arg0) {
//		return binder;
//	}
//	/**
//	 * 当Audio播放完的时候触发该动作
//	 */
//	@Override
//	public void onCompletion(MediaPlayer player) {
////		stopSelf();//结束了，则结束Service
//	}
//	
//	//在这里我们需要实例化MediaPlayer对象
//	public void onCreate(){
//		super.onCreate();
//		//我们从raw文件夹中获取一个应用自带的mp3文件
//		player = new MediaPlayer();
//		player.setOnCompletionListener(this);
//	}
//	
//	/**
//	 * 该方法在SDK2.0才开始有的，替代原来的onStart方法
//	 */
//	public int onStartCommand(Intent intent, int flags, int startId){
////		if(!player.isPlaying()){
////			player.start();
////		}
//		return START_STICKY;
//	}
//	
//	public void onDestroy(){
//		//super.onDestroy();
//		if(player.isPlaying()){
//			player.stop();
//		}
//		player.release();
//	}
//	
//	//为了和Activity交互，我们需要定义一个Binder对象
//	public class AudioBinder extends Binder{
//		
//		//返回Service对象
//		public AudioService getService(){
//			return AudioService.this;
//		}
//	}
//	
//	public void startSound(int resId){
//		CommonUtils.showLog("audio id = " + resId);
//		if(player.isPlaying())
//			player.stop();
//		player.reset();
//		try {
//			File file = new File(FileUtil.ROOT_PATH + FileUtil.SECOND_MEDIA_PATH,sound_array[resId]);
//			if(file.exists()){
//				player.setDataSource(file.toString());
//				player.prepare();
//				player.start();
//			}else{
//				//TODO 音频文件不存在
//				 CommonUtils.showLog("音频文件不存在");
//			}
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//		} catch (SecurityException e) {
//			e.printStackTrace();
//		} catch (IllegalStateException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	public void stopSound(){
//		if(player == null)
//			return;
//		if(player.isPlaying())
//			player.stop();
//		player.release();
//	}
//}
