package com.icofficeapp.util;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
@SuppressLint("NewApi") 
public class AnimaUtils  {
	
	/**
	 * 要对其进行判断是否为空
	 */
//	public static ObjectAnimator fristToShop;
	/**
	 * 进入上平界面的动画
	 * @param leftView动画的作用对象
	 * @param animal 动画类型
	 * @param timer 动画持续时间
	 * @param values 数组
	 */
	public ObjectAnimator menuShopView(final View leftView,int timer){
		
        	PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f,  
        			1.4f, 1.2f);  //0.7
            PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY",  1f,  
            		 1.4f, 1.2f);  
//            PropertyValuesHolder pvhh = PropertyValuesHolder.ofFloat("alpha",  0f,  
//            		1f, 0.3f);  
            final ObjectAnimator fristToShop = ObjectAnimator.ofPropertyValuesHolder(leftView,pvhY,pvhZ);
        	fristToShop.setDuration(timer);
         	fristToShop.start();
        	return fristToShop;
	}
	
	
	/**
	 * 向下平移  可用于微商城 上平详情
	 * @param view 动画执行对象
	 * @param time 动画时间
	 * @param values 数组 动画执行 的值
	 * 
	 * 商品首页 传的值int chushizhi = DisplayUtil.dip2px(this, 258);
	 * 
	 *  int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);  
	        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);  
	        view.measure(w, h);  
	        int height =view.getMeasuredHeight(); 
	 */
	public void translateDown(View view, int time, float... values) {
		
		PropertyValuesHolder pvh = PropertyValuesHolder.ofFloat("translationY",
				values);
		ObjectAnimator.ofPropertyValuesHolder(view, pvh).setDuration(time)
				.start();
	}
	
	/**
	 * 烟雾的扩散效果
	 * @param view  动画执行的对象
	 * duration持续时间 
	 * 烟雾
	 * delaye 延时时间
	 * 
	 */ 
	@SuppressLint("NewApi") 
	public void smokeDiffusionAnima(View animaView,int duration,int delaye){
		
//		ObjectAnimator.ofFloat(view, "alpha", 0f,1f);
		PropertyValuesHolder pvh = PropertyValuesHolder.ofFloat("alpha",
				 1f,0f);
		PropertyValuesHolder pvhsY = PropertyValuesHolder.ofFloat("scaleY",
				1f,2f);
		PropertyValuesHolder pvhsX = PropertyValuesHolder.ofFloat("scaleX",
				1.5f,2f);
		PropertyValuesHolder pvhtrans = PropertyValuesHolder.ofFloat("translationY",0f,-40f,-20f,-200f);
		
		ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(animaView, pvhsY,pvhsX,pvh,pvhtrans);
		animator.setStartDelay(delaye);
		animator.setDuration(duration).start();
		
	}
	
	 private AnimatorSet musicanimatorSet;//动画逐次显示
	 private  AnimatorSet hideAnimatorSet; //隐藏动画效果
	 private AnimatorSet dynamicAnimatorSet; 
	 
	public void startMusicAnimation(final View view_video,final View view_one,View view_two,View view_three,View view_four,View view_five,View view_six,View view_seven){
		
		//x轴放大2被 缩小
		PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 1f);
		//y轴放大2被 缩小
		PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 1f);
		//透明度 
		PropertyValuesHolder Alpha = PropertyValuesHolder.ofFloat("alpha", 0f,1f);
		//X轴平移
		PropertyValuesHolder translatesX = PropertyValuesHolder.ofFloat("translationX", 0);
		//Y轴平移
		PropertyValuesHolder translatesY = PropertyValuesHolder.ofFloat("translationY", 0);
		
		//旋转 x
		PropertyValuesHolder ratate1 = PropertyValuesHolder.ofFloat("rotation",0,10f,-10,0);
		PropertyValuesHolder ratate2 = PropertyValuesHolder.ofFloat("rotation",0,30,0);
		PropertyValuesHolder ratate3 = PropertyValuesHolder.ofFloat("rotation",0,10f,-10,0);
		PropertyValuesHolder ratate4 = PropertyValuesHolder.ofFloat("rotation",0,20,0);
		PropertyValuesHolder ratate5 = PropertyValuesHolder.ofFloat("rotation",0,50f,0);
		PropertyValuesHolder ratate6 = PropertyValuesHolder.ofFloat("rotation",0,10f,0);
		//旋转 Y
		PropertyValuesHolder ratateY = PropertyValuesHolder.ofFloat("rotationX",0, 360);
		//平面旋转
		PropertyValuesHolder ratateVideo = PropertyValuesHolder.ofFloat("rotation",0, 7f,-7f, 7f,-7f,0);
		PropertyValuesHolder translateVideo = PropertyValuesHolder.ofFloat("translationY",0f,-3f,0f,3f,0f);
		PropertyValuesHolder scaHolderX = PropertyValuesHolder.ofFloat("scaleX", 1f,1.1f,1f,1.1f,1f,1.1f);
		PropertyValuesHolder scaHolderY = PropertyValuesHolder.ofFloat("scaleY", 1f,1.1f,1f,1.1f,1f,1.1f);
		
		ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(view_video, scaHolderX,scaHolderY,ratateVideo,translateVideo);
		ofPropertyValuesHolder.setDuration(2000);
		ofPropertyValuesHolder.setRepeatCount(ValueAnimator.INFINITE);
		ofPropertyValuesHolder.start();
		
		//用于改变 显示情况   动画结束后  隐藏动画
		ObjectAnimator alphaAnimator01 = ObjectAnimator.ofFloat(view_one, "alpha", 1f,0f);
		ObjectAnimator alphaAnimator02 = ObjectAnimator.ofFloat(view_two, "alpha", 1f,0f);
		ObjectAnimator alphaAnimator03 = ObjectAnimator.ofFloat(view_three, "alpha", 1f,0f);
		ObjectAnimator alphaAnimator04 = ObjectAnimator.ofFloat(view_four, "alpha", 1f,0f);
		ObjectAnimator alphaAnimator05 = ObjectAnimator.ofFloat(view_five, "alpha", 1f,0f);
		ObjectAnimator alphaAnimator06 = ObjectAnimator.ofFloat(view_six, "alpha", 1f,0f);
		ObjectAnimator alphaAnimator07 = ObjectAnimator.ofFloat(view_seven, "alpha", 1f,0f);
		
		//音符的   逐次 显示 音符
		ObjectAnimator showAnimator1 = ObjectAnimator.ofPropertyValuesHolder(view_one, Alpha,scaleX,scaleY);
		ObjectAnimator showAnimator2 = ObjectAnimator.ofPropertyValuesHolder(view_two, Alpha,scaleX,scaleY);
		ObjectAnimator showAnimator3 = ObjectAnimator.ofPropertyValuesHolder(view_three, Alpha,scaleX,scaleY);
		ObjectAnimator showAnimator4 = ObjectAnimator.ofPropertyValuesHolder(view_four,Alpha,scaleX,scaleY);
		ObjectAnimator showAnimator5 = ObjectAnimator.ofPropertyValuesHolder(view_five, Alpha,scaleX,scaleY);
		ObjectAnimator showAnimator6 = ObjectAnimator.ofPropertyValuesHolder(view_six, Alpha,scaleX,scaleY);
		ObjectAnimator showAnimator7 = ObjectAnimator.ofPropertyValuesHolder(view_seven, Alpha,scaleX,scaleY);
		
		//音符的 动态效果 可进行设置  旋转 等 可自行设置
		ObjectAnimator NoteDynamicAnimator1 = ObjectAnimator.ofPropertyValuesHolder(view_one, ratate1);
		NoteDynamicAnimator1.setRepeatCount(ValueAnimator.INFINITE);
//		animator1.setDuration(2000).start();
		ObjectAnimator NoteDynamicAnimator2 = ObjectAnimator.ofPropertyValuesHolder(view_two,ratate2);
		NoteDynamicAnimator2.setRepeatCount(ValueAnimator.INFINITE);
//		animator20.setDuration(2000).start();
		ObjectAnimator NoteDynamicAnimator3 = ObjectAnimator.ofPropertyValuesHolder(view_three,ratate3);
		NoteDynamicAnimator3.setRepeatCount(ValueAnimator.INFINITE);
//		animator30.setDuration(2000).start();
		ObjectAnimator NoteDynamicAnimator4 = ObjectAnimator.ofPropertyValuesHolder(view_four,ratate4);
		NoteDynamicAnimator4.setRepeatCount(ValueAnimator.INFINITE);
//		animator40.setDuration(2000).start();
		ObjectAnimator NoteDynamicAnimator5 = ObjectAnimator.ofPropertyValuesHolder(view_five,ratate5);
		NoteDynamicAnimator5.setRepeatCount(ValueAnimator.INFINITE);
//		animator50.setDuration(2000).start();
		ObjectAnimator NoteDynamicAnimator6 = ObjectAnimator.ofPropertyValuesHolder(view_six, ratate6);
		NoteDynamicAnimator6.setRepeatCount(ValueAnimator.INFINITE);
//		animator60.setDuration(2000).start();
//		ObjectAnimator animator70 = ObjectAnimator.ofPropertyValuesHolder(view_seven,ratate);
//		animator70.setRepeatCount(ValueAnimator.INFINITE);
//		animator70.setDuration(1000).start();
		
		//启动音符的动态效果
		if(dynamicAnimatorSet==null){
			dynamicAnimatorSet= new AnimatorSet();
			dynamicAnimatorSet.play(NoteDynamicAnimator1).with(NoteDynamicAnimator2).with(NoteDynamicAnimator3).
			with(NoteDynamicAnimator4).with(NoteDynamicAnimator5).with(NoteDynamicAnimator6);
			dynamicAnimatorSet.setDuration(2000).start();
		}else {
			dynamicAnimatorSet.start();
		}
		
		//音符显示设置、主要是轨迹显示
		if(musicanimatorSet==null){
			musicanimatorSet = new AnimatorSet();
			musicanimatorSet.play(showAnimator1).with(showAnimator2);
			musicanimatorSet.play(showAnimator2).after(300).before(showAnimator3);
			musicanimatorSet.play(showAnimator3).before(showAnimator4);
			musicanimatorSet.play(showAnimator4).before(showAnimator5);
			musicanimatorSet.play(showAnimator5).before(showAnimator6);
			musicanimatorSet.play(showAnimator6).before(showAnimator7);
			musicanimatorSet.setDuration(350).start();
		}else {
			musicanimatorSet.start();
		}
		
		musicanimatorSet.start();
		
		//隐藏动画  将动画透明度设为0
	if(hideAnimatorSet==null){
			hideAnimatorSet = new AnimatorSet();
			hideAnimatorSet.play(alphaAnimator01).with(alphaAnimator02).with(alphaAnimator03)
			.with(alphaAnimator04).with(alphaAnimator05).with(alphaAnimator06).with(alphaAnimator07);
			hideAnimatorSet.setDuration(350);
		}
			
		//监听事件  循环完成后 在在另一个动画   进行循环加载
		musicanimatorSet.addListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator arg0) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animator arg0) {
				
			}
			
			@Override
			public void onAnimationEnd(final Animator animator) {
				view_one.clearAnimation();
				hideAnimatorSet.start();
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						animator.start();
					}
				}, 500);
			}
			
			@Override
			public void onAnimationCancel(Animator arg0) {
				
			}
		});
		
	}
	/**
	 * 音效动画停止
	 * @param views
	 */
	public void stopMusicAnimal(View...views){
		if(views!=null){
			for (int i = 0; i < views.length; i++) {
				if(views[i]!=null){
					views[i].clearAnimation();
				}
			}
		}
			  if(musicanimatorSet!=null){
			  musicanimatorSet.removeAllListeners();
			  musicanimatorSet.cancel();
			  musicanimatorSet=null;
		  }if(hideAnimatorSet!=null){
			  hideAnimatorSet.removeAllListeners();
			  hideAnimatorSet.cancel();
			  hideAnimatorSet = null;  
		  }if(dynamicAnimatorSet!=null){
			  dynamicAnimatorSet.removeAllListeners();
			  dynamicAnimatorSet.cancel();
			  dynamicAnimatorSet=null;
		  }
	}
	
	
	public void startVideoAnimal(ImageView iv_video){
		if(iv_video==null){
			return;
		}
		//平面旋转
		PropertyValuesHolder ratateVideo = PropertyValuesHolder.ofFloat("rotation",0, 7f,-7f, 7f,-7f,0);
		PropertyValuesHolder translateVideo = PropertyValuesHolder.ofFloat("translationY",0f,-3f,0f,3f,0f);
		PropertyValuesHolder scaHolderX = PropertyValuesHolder.ofFloat("scaleX", 1f,1.1f,1f,1.1f,1f,1.1f);
		PropertyValuesHolder scaHolderY = PropertyValuesHolder.ofFloat("scaleY", 1f,1.1f,1f,1.1f,1f,1.1f);
		
		
		ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(iv_video, scaHolderX,scaHolderY,ratateVideo,translateVideo);
		ofPropertyValuesHolder.setDuration(2000);
		ofPropertyValuesHolder.setRepeatCount(ValueAnimator.INFINITE);
		ofPropertyValuesHolder.start();
	}
	
	/**
	 * 微商城抖动效果
	 * @param view
	 */
	@SuppressLint("NewApi") 
	public static ObjectAnimator WeiShopJitterAnimal(View view){
		PropertyValuesHolder pvhtrx = PropertyValuesHolder.ofFloat("translationX", 0f,10f,0f,-10,0f,10f,0f,-10,0f,10f,0f,-10,0f);
		PropertyValuesHolder pvhry = PropertyValuesHolder.ofFloat("rotation", 0f,10f,0f,-10f,0f,10f,0f,-10f,0f,10f,0f,-10f,0f,10f,0f,-10f,0f,10f,-10f,0f,10f,0f,-10f,0f,10f,0f);
		ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view,pvhtrx, pvhry);
		 animator.setDuration(1000);
		 animator.start();
		 return animator;
//		 if(){
//			 
//		 }
	}
	
	public static void threth(){
		
	}
	
	
}
