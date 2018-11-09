package com.icoffice.library.moudle.control;
/**
 * 视频控制类
 */
import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.icoffice.library.callback.MediaPlayerCallBack;
import com.icoffice.library.utils.CommonUtils;

public class MediaPlayControl {
	private MediaPlayer mediaPlayer;    //播放器控件
	private SurfaceView item_view;
	private MediaPlayerCallBack mMediaPlayerCallBack;//视频播放回调函数
	private String _mediaPlayer_path = "";//播放路径
	private boolean surfaceView_alive = false;//SurfaceView是否存在
	@SuppressWarnings("deprecation")
	public MediaPlayControl(SurfaceView item_view){
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnCompletionListener(new MediaPlateronCompletion());
		this.item_view = item_view;
		item_view.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);   //不缓冲
		item_view.getHolder().setKeepScreenOn(true);   //保持屏幕高亮
		item_view.getHolder().addCallback(new surFaceView());   //设置监听事件
	}
	public void setMediaPlayerCallBack(MediaPlayerCallBack mediaPlayerCallBack){
		mMediaPlayerCallBack = mediaPlayerCallBack;
	}
	class PreparedMovie extends Thread {   //播放视频的线程
		private String mMediaPlayer_path;
		public PreparedMovie(String mediaPlayer_path){
			mMediaPlayer_path = mediaPlayer_path;
		}
		@Override
		public void run() {
			preparedMedia(mMediaPlayer_path);
			super.run();
		}
	}

	class Ok implements OnPreparedListener {

		public void onPrepared(MediaPlayer mp) {
			// 当prepare完成后，该方法触发，在这里我们播放视频  
			if (mediaPlayer != null) { 
				mediaPlayer.start();  //开始播放视频
			} else {
				return;
			}
		}
	}

	private class surFaceView implements Callback {     //上面绑定的监听的事件

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			CommonUtils.showLog(CommonUtils.AD_TAG, "SurfaceHolder surfaceChanged");
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {   //创建完成后调用
			surfaceView_alive = true;
			preparedMedia(_mediaPlayer_path);
			CommonUtils.showLog(CommonUtils.AD_TAG, "SurfaceHolder surfaceCreated");
		}
		@Override
		public void surfaceDestroyed(SurfaceHolder holder) { //activity调用过pause方法，重置到0时刻
			surfaceView_alive = false;
			resetMedia();
			CommonUtils.showLog(CommonUtils.AD_TAG, "SurfaceHolder surfaceDestroyed");
		}
	}
	public void preparedMedia(String mediaPlayer_path){
		if(surfaceView_alive){
			try {
				CommonUtils.showLog(CommonUtils.AD_TAG, "mediaPlayer_path = " + mediaPlayer_path);
				mediaPlayer.reset();    //回复播放器默认
				mediaPlayer.setDataSource(mediaPlayer_path);   //设置播放路径
				mediaPlayer.setDisplay(item_view.getHolder());  //把视频显示在SurfaceView上
				mediaPlayer.setOnPreparedListener(new Ok());  //设置监听事件
				mediaPlayer.setLooping(true);
				mediaPlayer.prepare();  //准备播放
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				CommonUtils.showLog(CommonUtils.AD_TAG, "IllegalArgumentException " + e.toString());
			} catch (SecurityException e) {
				e.printStackTrace();
				CommonUtils.showLog(CommonUtils.AD_TAG, "SecurityException " + e.toString());
			} catch (IllegalStateException e) {
				e.printStackTrace();
				CommonUtils.showLog(CommonUtils.AD_TAG, "IllegalStateException " + e.toString());
			} catch (IOException e) {
				e.printStackTrace();
				CommonUtils.showLog(CommonUtils.AD_TAG, "IOException " + e.toString());
			} 
		}
	}
	/**
	 * 停止播放视屏
	 */
	public void stopMedia(){
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
		}
	}
	/**
	 * 播放视频
	 */
	public void startMedia(String mediaPlayer_path){
//		new PreparedMovie(pathName).start(); 
		_mediaPlayer_path = mediaPlayer_path;
		preparedMedia(mediaPlayer_path);
	}
	public void resetMedia(){
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
		}
	}
	/**
	 * 摧毁mediaplayer
	 */
	public void destoryMedia(){
		if (mediaPlayer != null ) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}
	class MediaPlateronCompletion implements OnCompletionListener{

		@Override
		public void onCompletion(MediaPlayer arg0) {
			resetMedia();
			mMediaPlayerCallBack.onCompletion();
		}
		
	}
}
