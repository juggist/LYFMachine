package com.icofficeapp.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.icofficeapp.R;

public class WaveView extends View implements Runnable {

	private Paint paint1 = new Paint();
	private Paint paint2 = new Paint();
	private Paint paint3 = new Paint();
	private boolean isRun = false;
	private int angle = 0;
	private int angle2 = 0;
	private int angle3 = 0;
	private int angle4 = 0;
	private int angle5 = 0;
	private int angle6 = 0;
	private int angle7 = 0;
	private int imgId = 0;
	private int resArray[] ={R.drawable.loading2,R.drawable.loading3,R.drawable.loading4}; 
	public WaveView(Context context,int id) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public WaveView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	@SuppressLint("DrawAllocation") @Override
	public void onDraw(Canvas canvas) {
		Log.i("WaveView", "draw");
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if(imgId == 0)
			imgId = R.drawable.loading2;
		setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		int height = getHeight();
		int width = getWidth();
		paint1.setColor(Color.rgb(205, 243, 246));
		paint2.setAlpha(200);
		paint2.setColor(Color.rgb(150, 206, 231));
		paint3.setAlpha(150);
		paint3.setColor(Color.rgb(89, 186, 231));
		double lineX = 0;
		double lineY1 = 0;
		double lineY2 = 0;
		double lineY3 = 0;
		double lineY4 = 0;
		double lineY5 = 0;
		double lineY6 = 0;
		double lineY7 = 0;
		double lineY8 = 0;
		lineY2 = 150 * Math.sin((angle) * (Math.PI / 300)) ;
		lineY3 = 150 * Math.sin((angle2) * Math.PI / 300) ;
		lineY4 = 150 * Math.sin((angle3) * Math.PI / 300) ;
		lineY5 = 150 * Math.sin((angle4) * Math.PI / 300) ;
		lineY6 = 150 * Math.sin((angle5) * Math.PI / 300) ;
		lineY7 = 150 * Math.sin((angle6) * Math.PI / 300) ;
		lineY8 = 150 * Math.sin((angle7) * Math.PI / 300) ;
		if(isShow){
			canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), resArray[0]), (int) (angle * 0.8), (int) (lineY2 + height/1.5 -100), null);
		}
		if(isShow2){
			canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), resArray[1]), (int) (angle2* 0.8), (int) (lineY3 + height/1.5 -100), null);
		}if(isShow3){
			canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), resArray[2]), (int) (angle3* 0.8), (int) (lineY4 + height/1.5 -100), null);
		}
		if(isShow4){
			canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), resArray[0]), (int) (angle4* 0.8), (int) (lineY5 + height/1.5 -100), null);
		}
		if(isShow5){
			canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), resArray[1]), (int) (angle5* 0.8), (int) (lineY6 + height/1.5 -100), null);
		}if(isShow6){
			canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), resArray[2]), (int) (angle6* 0.8), (int) (lineY7 + height/1.5 -100), null);
		}if(isShow7){
			canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), resArray[0]), (int) (angle7* 0.8), (int) (lineY8 + height/1.5 -100), null);
		}
		
//			canvas.scale(0.8f, 0.8f);
		canvas.restore(); 
	}//2
	private boolean isShow = false;
	private boolean isShow2 = false;
	private boolean isShow3 = false;
	private boolean isShow4 = false;
	private boolean isShow5 = false;
	private boolean isShow6 = false;
	private boolean isShow7 = false;
	@Override
	public void run() {
		while (isRun) {
			isShow = true;
			angle++;
			if(isShow2){
				angle2++;	
			}else {
				if(angle==80){
					isShow2 = true;
				}
			}if(isShow3){
				angle3++;
			}else {
				if(angle==160){
					isShow3 = true;
				}
			}if(isShow4){
				angle4++;
			}else {
				if(angle==240){
					isShow4 = true;
				}
			}if(isShow5){
				angle5++;
			}else {
				if(angle==320){
					isShow5 = true;
				}
			}if(isShow6){
				angle6++;
			}else {
				if(angle==400){
					isShow6 = true;
				}
			}if(isShow7){
				angle7++;
			}else {
				if(angle==480){
					isShow7 = true;
				}
			}
			
			
			if (angle == 600) {
				angle = 0;
			}if(angle2 == 600){
				angle2 = 0;
			}if(angle3 == 600){
				angle3 = 0;
			}if(angle4 == 600){
				angle4 = 0;
			}if(angle5 == 600){
				angle5 = 0;
			}if(angle6 == 600){
				angle6 = 0;
			}if(angle7 == 600){
				angle7 = 0;
			
			}
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public void start() {
		isRun = true;
		new Thread(this).start();
	}

	public void stop() {
		isRun = false;
		isShow = false;
		isShow2 = false;
		isShow3= false;
		isShow4 = false;
		isShow5 = false;
		isShow6 = false;
		isShow7 = false;
		angle = 0;
		angle2 = 0;
		angle3 = 0;
		angle4 = 0;
		angle5 = 0;
		angle6 = 0;
		angle7 = 0;
		
		
	}

}
