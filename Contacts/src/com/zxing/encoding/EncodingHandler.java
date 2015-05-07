package com.zxing.encoding;

import java.util.Hashtable;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
/**
 * 
 *二维码编码以及图片的生成
 *reference:http://www.cnblogs.com/heiyue/p/3571761.html 二维码编码原理
 *
 */
public final class EncodingHandler {
	private static final int BLACK = 0xff000000,WHITE=0xffffffff;

	
	public static Bitmap createQRCode(String str,int widthAndHeight) throws WriterException {
		//设置QR二维码参数
		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();  
		
		//设置QR二维码的纠错级别为最高级别H
		//hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		
		//设置编码方式为utf-8
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); 
        
        //参数顺序：编码内容，编码类型，生成图片的宽度以及高度
		BitMatrix matrix = new MultiFormatWriter().encode(str,BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight);
		
		//截止此处得到的是一个由true和false组成的数组，将true的矩阵单元染色
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		int[] pixels = new int[width * height];
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width + x] = BLACK;//true单元染色为黑色
				}else {
					pixels[y * x+ x] = WHITE;//false单元染色为白色
				}
			}
		}
		//创建bitmap图片，采用最高效果显示
		Bitmap bitmap = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888);
		//将二维码颜色数组传入，生成图片颜色
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
		/**
		 * 进一步准备实现二维码上头像绘制
		 */
	}
}
