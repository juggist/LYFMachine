package com.icoffice.library.utils;

import java.util.Hashtable;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRService {

	private static QRService instance = null;

	private QRService() {

	}

	public static QRService getInstance() {
		if (instance == null) {
			synchronized (QRService.class) {
				if (instance == null) {
					instance = new QRService();
				}
			}
		}
		return instance;
	}

	// 生成QR图
	public Bitmap createImage(String content, int width, int height) {
		Bitmap bitmap = null;
		try {
			int QR_WIDTH = width;
			int QR_HEIGHT = height;
			// 需要引入core包
			QRCodeWriter writer = new QRCodeWriter();
			String text = content;
			System.out.println("createImagecontent++++++++++++" + content);
			// String text = qr_text.getText().toString();

			if (text == null || "".equals(text) || text.length() < 1) {
				return null;
			}

			// 把输入的文本转为二维码
			BitMatrix martix = writer.encode(text, BarcodeFormat.QR_CODE,
					QR_WIDTH, QR_HEIGHT);

			System.out.println("w:" + martix.getWidth() + "h:"
					+ martix.getHeight());

			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			BitMatrix bitMatrix = new QRCodeWriter().encode(text,
					BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
			int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
			for (int y = 0; y < QR_HEIGHT; y++) {
				for (int x = 0; x < QR_WIDTH; x++) {
					if (bitMatrix.get(x, y)) {
						pixels[y * QR_WIDTH + x] = 0xff000000;//黑色
					} else {
						pixels[y * QR_WIDTH + x] = 0x00ffffff;//白色
					}

				}
			}

			bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
					Bitmap.Config.ARGB_8888);

			bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
}
