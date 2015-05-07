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
 *��ά������Լ�ͼƬ������
 *reference:http://www.cnblogs.com/heiyue/p/3571761.html ��ά�����ԭ��
 *
 */
public final class EncodingHandler {
	private static final int BLACK = 0xff000000,WHITE=0xffffffff;

	
	public static Bitmap createQRCode(String str,int widthAndHeight) throws WriterException {
		//����QR��ά�����
		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();  
		
		//����QR��ά��ľ�����Ϊ��߼���H
		//hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		
		//���ñ��뷽ʽΪutf-8
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); 
        
        //����˳�򣺱������ݣ��������ͣ�����ͼƬ�Ŀ���Լ��߶�
		BitMatrix matrix = new MultiFormatWriter().encode(str,BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight);
		
		//��ֹ�˴��õ�����һ����true��false��ɵ����飬��true�ľ���ԪȾɫ
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		int[] pixels = new int[width * height];
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width + x] = BLACK;//true��ԪȾɫΪ��ɫ
				}else {
					pixels[y * x+ x] = WHITE;//false��ԪȾɫΪ��ɫ
				}
			}
		}
		//����bitmapͼƬ���������Ч����ʾ
		Bitmap bitmap = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888);
		//����ά����ɫ���鴫�룬����ͼƬ��ɫ
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
		/**
		 * ��һ��׼��ʵ�ֶ�ά����ͷ�����
		 */
	}
}
