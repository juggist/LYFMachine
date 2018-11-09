package com.icoffice.library.utils;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.media.ThumbnailUtils;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class BitmapUtil {
	// 展示drawable图片
	public static void displayBackground2Drawable(int resId, Button btn) {
		try {
			btn.setBackgroundResource(resId);
		} catch (OutOfMemoryError ex) {
			System.gc();
		}
	}

	// 展示drawable图片
	public static void displayBackground2Drawable(int resId, View view) {
		try {
			view.setBackgroundResource(resId);
		} catch (OutOfMemoryError ex) {
			System.gc();
		}
	}

	/**
	 * 根据后缀名判断是否是图片文件
	 * 
	 * @param type
	 * @return 是否是图片结果true or false
	 */
	public static boolean isImage(String fileName) {
		String type = getFileType(fileName);
		if (type != null
				&& (type.equals("jpg") || type.equals("gif")
						|| type.equals("png") || type.equals("jpeg")
						|| type.equals("bmp") || type.equals("wbmp")
						|| type.equals("ico") || type.equals("jpe"))) {
			return true;
		}
		return false;
	}
	/**
	 * 根据后缀名判断是否是视频文件
	 * 
	 * @param type
	 * @return 是否是图片结果true or false
	 */
	public static boolean isMedia(String fileName) {
		String type = getFileType(fileName);
		if (type != null
				&& (type.equals("mp4") || type.equals("3gp"))) {
			return true;
		}
		return false;
	}
	/**
	 * 获取文件后缀名
	 * 
	 * @param fileName
	 * @return 文件后缀名
	 */
	public static String getFileType(String fileName) {
		if (fileName != null) {
			int typeIndex = fileName.lastIndexOf(".");
			if (typeIndex != -1) {
				String fileType = fileName.substring(typeIndex + 1).toLowerCase();
				return fileType;
			}
		}
		return "";
	}
	/**
	 * 获取文件后缀名
	 * 
	 * @param fileName
	 * @return 文件后缀名
	 */
	public static String getLastName(String fileName) {
		if (fileName != null) {
			int typeIndex = fileName.lastIndexOf("-");
			if (typeIndex != -1) {
				String fileType = fileName.substring(typeIndex + 1).toLowerCase();
				return fileType;
			}
		}
		return "";
	}
	/**
	 * 获取视频缩略图
	 * @param filePath
	 * @return
	 */
	public static Bitmap createVideoThumbnail(String filePath) {   
		Bitmap bit = null;
		if(new File(filePath).exists()){
			try{
				bit = ThumbnailUtils.createVideoThumbnail(filePath, Images.Thumbnails.MICRO_KIND);
			}catch(Exception e){
				
			}
		}
        return bit;   
    }   

		// 展示本地sdcard图片
		public static void displayBitmap2Sdcard(String pathName, ImageView iv) {
			Bitmap bit = null;
			try {
				bit = BitmapFactory.decodeFile(pathName);
				iv.setImageBitmap(bit);
			} catch (OutOfMemoryError ex) {
				if (bit != null) {
					bit.recycle();
					bit = null;
					System.gc();
				}
			}
		}

		// 展示drawable图片
		public static void displayBitmap2Drawable(Context mContext, int resId, ImageView iv) {
			Bitmap bit = null;
			try {
				bit = BitmapFactory.decodeResource(mContext.getResources(), resId);
				iv.setImageBitmap(bit);
			} catch (OutOfMemoryError ex) {
				if (bit != null) {
					bit.recycle();
					bit = null;
					System.gc();
				}
			}
		}

		/**
		 * 将彩色图转换为灰度图(透明度不处理)
		 * 
		 * @param img
		 *            位图
		 * @return 返回转换好的位图
		 */
		public static Bitmap convertGreyImg(String imgUrl) {
			Bitmap bit = null;
			if (new File(imgUrl).exists()) {
				try {
					bit = BitmapFactory.decodeFile(imgUrl);
					int width = bit.getWidth(); // 获取位图的宽
					int height = bit.getHeight(); // 获取位图的高
					int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组
					bit.getPixels(pixels, 0, width, 0, 0, width, height);
					for (int i = 0; i < pixels.length; i++) {
						int grey = pixels[i];
						int alpha = grey & 0xFF000000;
						if (alpha != 0) {
							int red = ((grey & 0x00FF0000) >> 16);
							int green = ((grey & 0x0000FF00) >> 8);
							int blue = grey & 0x000000FF;
							grey = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
							grey = alpha | (grey << 16) | (grey << 8) | grey;
							pixels[i] = grey;
						}
					}
					bit.recycle();
					bit = null;
					bit = Bitmap.createBitmap(pixels, width, height,
							Config.ARGB_8888);
				} catch (OutOfMemoryError e) {
					Log.i("ex", e.toString());
					if (bit != null) {
						bit.recycle();
						bit = null;
					}
				}catch(Exception e){
					
				}
			}
			return bit;
		}


}
