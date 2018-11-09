package com.icoffice.library.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.os.Environment;

public class FileUtil {
	public static final String ROOT_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/iCofficeApp";
	public static final String DATA_PATH = Environment.getDataDirectory()
			.getAbsolutePath() + "/data/";// data目录
	public static final String SECOND_MEDIA_PATH = "/MEDIA/";// 流媒体
	public static final String SECOND_APP_PATH = "/APP/";// apk下载
	public static final String SECOND_IMAGE_PATH = "/images/";// 商品图片
	public static final String SECOND_AD_PATH = "/ads/";// 首页广告
	public static final String SECOND_OLD_AD_PATH = "/AD/";//老首页广告
	public static final String SECOND_ACTICITY_AD_PATH = "/ACTICITY_AD/";// 活动广告
	public static final String SECOND_WECHAT_AD_PATH = "/WECHAT_AD/";// 微信广告
	public static final String SECOND_CACH_PATH = "/CACH/";// 记录缓存文件
	private static FileUtil mFileUtil;

	private FileUtil() {

	}

	public static FileUtil getInstance() {
		if (mFileUtil == null)
			mFileUtil = new FileUtil();
		return mFileUtil;
	}

	// TODO
	/**
	 * 获取SD卡上所有图片
	 * 
	 * @param fileList
	 * @param PATH
	 * @return
	 */
	public ArrayList<String> getImgFromSDcard(ArrayList<String> fileList,
			String PATH) {
		File file = new File(PATH);
		File[] files = file.listFiles();

		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				String filename = files[i].getName();
				// 获取bmp,jpg,png格式的图片
				if (filename.endsWith(".jpg") || filename.endsWith(".png")
						|| filename.endsWith(".bmp")) {
					String filePath = files[i].getAbsolutePath();
					fileList.add(filePath);
				}
			} else if (files[i].isDirectory()) {
				PATH = files[i].getAbsolutePath();
				getImgFromSDcard(fileList, PATH);
			}
		}
		return fileList;
	}
	/**
	 * 获取SD卡所有广告（图片，视频）
	 * 
	 * @param fileList
	 * @param PATH
	 * @return
	 */
	public ArrayList<String> getAdFromSDcard(ArrayList<String> fileList,
			String PATH) {
		File file = new File(PATH);
		File[] files = file.listFiles();

		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				String filename = files[i].getName();
				// 获取bmp,jpg,png格式的图片
				if (filename.endsWith(".jpg") || filename.endsWith(".png")
						|| filename.endsWith(".bmp") ||  filename.endsWith(".mp4") ||  filename.endsWith(".3gp")) {
					String filePath = files[i].getAbsolutePath();
					fileList.add(filePath);
				}
			} else if (files[i].isDirectory()) {
				PATH = files[i].getAbsolutePath();
				getAdFromSDcard(fileList, PATH);
			}
		}
		return fileList;
	}
	/**
	 * 获取txt log文件
	 * @param fileList
	 * @param PATH
	 * @return
	 */
	public ArrayList<String> getTxtFromSDcard(ArrayList<String> fileList,
			String PATH) {
		File file = new File(PATH);
		File[] files = file.listFiles();

		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				String filename = files[i].getName();
				// 获取bmp,jpg,png格式的图片
				if (filename.endsWith(".txt")) {
					String filePath = files[i].getAbsolutePath();
					fileList.add(filePath);
				}
			} else if (files[i].isDirectory()) {
				PATH = files[i].getAbsolutePath();
				getAdFromSDcard(fileList, PATH);
			}
		}
		return fileList;
	}

	/**
	 * 保存文件时创建文件夹
	 */
	void sdCardFileExist(String file) {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
			return;
		File rootFile = new File(ROOT_PATH);
		if (!rootFile.exists())
			rootFile.mkdirs();
		File saveFile = new File(rootFile, file);
		if (!saveFile.exists())
			saveFile.mkdirs();
	}

	// 创建文件夹
	public void createFile() {
		ArrayList<String> file_list = new ArrayList<String>();
		file_list.add(SECOND_MEDIA_PATH);
		file_list.add(SECOND_APP_PATH);
		file_list.add(SECOND_IMAGE_PATH);
		file_list.add(SECOND_AD_PATH);
		file_list.add(SECOND_ACTICITY_AD_PATH);
		file_list.add(SECOND_WECHAT_AD_PATH);
		file_list.add(SECOND_CACH_PATH);
		for (int i = 0; i < file_list.size(); i++) {
			sdCardFileExist(file_list.get(i));
		}
	}

	/**
	 * 写入本地文件内容(进程pid号)
	 * 
	 * @return
	 */

	public static void setText2File(String str) {
		try {
			String fileName = "config.txt";
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				String path = ROOT_PATH + SECOND_CACH_PATH;
				File dir = new File(path);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(path + fileName);
				fos.write(str.getBytes());
				fos.close();
			}
		} catch (Exception e) {
			// Log.e(TAG, "an error occured while writing file...", e);

		}
	}

	public static void CopyAssetsBin(Context context, String assetDir) {

		File filesDir = context.getFilesDir();
		String dataFiles = filesDir.getAbsolutePath();

		String[] files;
		try {
			files = context.getResources().getAssets().list(assetDir);
		} catch (IOException e1) {
			return;
		}
		File workingPath = new File(dataFiles);
		// if this directory does not exists, make one.
		if (!workingPath.exists()) {
			if (!workingPath.mkdirs()) {
				CommonUtils.showLog(CommonUtils.TAG,"--CopyAssets--cannot create directory.");
			}
		}
		for (int i = 0; i < files.length; i++) {
			try {
				String fileName = files[i];
				File outFile = new File(workingPath, fileName);
				if (outFile.exists())
					outFile.delete();
				InputStream in = null;
				if (0 != assetDir.length())
					in = context.getAssets().open(assetDir + "/" + fileName);
				else
					in = (InputStream) context.getAssets().open(fileName);
				OutputStream out = new FileOutputStream(outFile);
				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;
				while ((len = ((InputStream) in).read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				out.close();

				// 赋予权限
				try {
					/* Missing read/write permission, trying to chmod the file */
					Process su;
					su = Runtime.getRuntime().exec("su");
					String cmd = "chmod 755 " + outFile.getAbsolutePath()
							+ "\n" + "exit\n";
					su.getOutputStream().write(cmd.getBytes());
					if ((su.waitFor() != 0) || !outFile.canRead()
							|| !outFile.canWrite()) {
						throw new SecurityException();
					}
				} catch (Exception e) {
					e.printStackTrace();
					throw new SecurityException();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void CopyAssets(Context context, String assetDir, String dir) {
		String[] files;
		try {
			files = context.getResources().getAssets().list(assetDir);
		} catch (IOException e1) {
			return;
		}
		File workingPath = new File(dir);
		// if this directory does not exists, make one.
		if (!workingPath.exists()) {
			if (!workingPath.mkdirs()) {
				CommonUtils.showLog(CommonUtils.TAG,"--CopyAssets--cannot create directory.");
			}
		}
		for (int i = 0; i < files.length; i++) {
			try {
				String fileName = files[i];
				File temp = new File(fileName);
				// we make sure file name not contains '.' to be a folder.
				if (temp.isDirectory()) {
					if (0 == assetDir.length()) {
						CopyAssets(context, fileName, dir + fileName + "/");
					} else {
						CopyAssets(context, assetDir + "/" + fileName, dir
								+ fileName + "/");
					}
					continue;
				}
				File outFile = new File(workingPath, fileName);
				if (outFile.exists())
					outFile.delete();
				InputStream in = null;
				if (0 != assetDir.length())
					in = context.getAssets().open(assetDir + "/" + fileName);
				else
					in = context.getAssets().open(fileName);
				OutputStream out = new FileOutputStream(outFile);
				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
    /**
     *  根据路径删除指定的目录或文件，无论存在与否
     *@param sPath  要删除的目录或文件
     *@return 删除成功返回 true，否则返回 false。
     */
    public boolean DeleteFolder(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 判断目录或文件是否存在
        if (!file.exists()) {  // 不存在返回 false
            return flag;
        } else {
            // 判断是否为文件
            if (file.isFile()) {  // 为文件时调用删除文件方法
                return deleteFile(sPath);
            } else {  // 为目录时调用删除目录方法
                return deleteDirectory(sPath);
            }
        }
    }
    /**
     * 删除单个文件
     * @param   sPath    被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }
    /**
     * 删除目录（文件夹）以及目录下的文件
     * @param   sPath 被删除目录的文件路径
     * @return  目录删除成功返回true，否则返回false
     */
    public boolean deleteDirectory(String sPath) {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            //删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } //删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }
    /**
	 * 保存信息到文件中
	 * 
	 * @param infoStr log信息
	 * 
	 */
	public static void saveCrashInfo2File(String infoStr) {
		
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat formatter_02 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			String time = formatter.format(new Date());
			String time_02 = formatter_02.format(new Date());
			String fileName = time + "_log_cach.txt";
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				String path = FileUtil.ROOT_PATH + FileUtil.SECOND_CACH_PATH;
				File dir = new File(path);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(path + fileName,true);
				infoStr = time_02 + "_"+ infoStr + "\n";
				fos.write(infoStr.getBytes());
				fos.close();
			}
		} catch (Exception e) {
			// Log.e(TAG, "an error occured while writing file...", e);
			
		}
	}
	/**
	 * 文件夹重命名
	 */
	public static void renameFile(){
		File rootFile = new File(ROOT_PATH);
		if (!rootFile.exists())
			rootFile.mkdirs();
		File saveFile = new File(rootFile, SECOND_OLD_AD_PATH);
		if (saveFile.exists())
			saveFile.renameTo(new File(rootFile,SECOND_AD_PATH));
	}
	/**
	 * 文件是否存在
	 * @param path
	 * @return
	 */
	public boolean isFileExist(String path){
		boolean isExist = false;
		try{
			if(new File(path).exists()){
				isExist = true;
			}else{
				isExist = false;
			}
		}catch(Exception e){
			isExist = false;
		}
		return isExist;
	}
}
