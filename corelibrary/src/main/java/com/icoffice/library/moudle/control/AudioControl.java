package com.icoffice.library.moudle.control;

import java.io.File;
import java.io.IOException;

import android.media.MediaPlayer;

import com.icoffice.library.utils.FileUtil;
import com.icoffice.library.utils.CommonUtils;

public class AudioControl {
	private String[] sound_array = new String[]{
			"open_alipay.mp3","21.mp3","pay_success.mp3","outgoods_success.mp3","plz_wait.mp3","",
												//01_请点击您想购买的商品，上下翻页查看更多选择
												//02_网络不稳定，请改用现金购买
												//03_请打开微信扫一扫，扫描屏幕上的二维码
												//04_出货成功，请从取物口领取商品
												//05_支付超时，请重新选购
												//10_请投币
												//11_出货成功，请从取物口领取商品，如需退币请按退币钮
												//14_请输入兑换码，并按确定按钮
												//15_请选择您想领取的商品
												//17_请将客非卡靠近感应区
												//18_出货失败，请联系客服
												//19_机器维护中，如有疑问请联系客服
												//20_请选择支付方式
												//21_90秒支付
												//22_10秒支付
			"01.mp3","02.mp3","03.mp3","04.mp3","05.mp3",
			"10.mp3","11.mp3","14.mp3","15.mp3","17.mp3",
			"18.mp3","19.mp3","20.mp3","22.mp3"
			
	};
	private MediaPlayer soundPlayer;
	private static AudioControl mAudioControl;
	private AudioControl(){
		if(soundPlayer != null){
			soundPlayer.release();
			soundPlayer = null;
		}
		soundPlayer = new MediaPlayer();
	}
	public static AudioControl getInstance(){
		if(mAudioControl == null){
			mAudioControl = new AudioControl();
		}
		return mAudioControl;
	}
	void startSound(int resId){
		try {
			if(soundPlayer == null)
				return;
			if(soundPlayer.isPlaying()){
				soundPlayer.stop();
			}
			soundPlayer.reset();
			File file = new File(FileUtil.ROOT_PATH + FileUtil.SECOND_MEDIA_PATH,sound_array[resId]);
			if(file.exists()){
				soundPlayer.setDataSource(file.toString());
				soundPlayer.prepare();
				soundPlayer.start();
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void stopSound(){
		if(soundPlayer == null)
			return;
		if(soundPlayer.isPlaying())
			soundPlayer.stop();
		soundPlayer.release();
	}
}
