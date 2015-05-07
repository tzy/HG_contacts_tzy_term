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
			//��ͼƬ����Ϊ��400*400��,Ӧ�Ӿ����������
			try {
				Bitmap qrCodeBitmap = EncodingHandler.createQRCode(contentString, 400);
				qrImageView.setImageBitmap(qrCodeBitmap);
			} catch (WriterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			//�˴�Ӧ�öԿ����ݽ��д���
		}
		
		//ΪbackButton ���Ӽ�����
		backButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//��ת��ĳһ����
			}
		});
	}
}
