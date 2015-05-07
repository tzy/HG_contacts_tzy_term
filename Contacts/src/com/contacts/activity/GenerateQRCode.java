package com.contacts.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.google.zxing.WriterException;
import com.zxing.encoding.EncodingHandler;

public class GenerateQRCode extends Activity{

	private ImageView qrImageView ;
	private Button backButton ;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generate_qrcode);
		
		qrImageView =(ImageView)this.findViewById(R.id.iv_qr_image);
		backButton =(Button)this.findViewById(R.id.btn_back);
		
		Bundle bundle = getIntent().getExtras();
		String contentString = bundle.getString("String_for_pic");
		if( contentString !=null &&!contentString.equals("")) {
			//将图片设置为（400*400）,应视具体情况调节
			try {
				Bitmap qrCodeBitmap = EncodingHandler.createQRCode(contentString, 400);
				qrImageView.setImageBitmap(qrCodeBitmap);
			} catch (WriterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			//此处应该对空内容进行处理
		}
		
		//为backButton 增加监听器
		backButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//跳转会某一界面
			}
		});
	}
}
