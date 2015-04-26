package com.contacts.activity;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

public class AddContact extends Activity {
	
	private ImageButton addPhoneItem;

	private ListView phoneLV; // ListView控件 
	private ListView emailLV;
	
	private AddItemBaseAdapter addPhoneItemAdapter;
	private AddItemBaseAdapter addEmailItemAdapter;
	
	public void fixListViewHeight(ListView listView) {

		ListAdapter listAdapter = listView.getAdapter();
		int totalHeight = 0;
		if (listAdapter == null) {
			return;
		} else {
			for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
				View listViewItem = listAdapter.getView(i, null, listView);
				// 计算子项View 的宽高
				listViewItem.measure(0, 0);
				// 计算所有子项的高度和
				totalHeight += listViewItem.getMeasuredHeight();
			}
			ViewGroup.LayoutParams params = listView.getLayoutParams();
			// listView.getDividerHeight()获取子项间分隔符的高度
			// params.height设置ListView完全显示需要的高度
			params.height = totalHeight
					+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
			System.out.println(params.height);
			listView.setLayoutParams(params);
			listView.requestLayout();
		}
	}

	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_additem_layout); 
		
		addPhoneItemAdapter=new AddItemBaseAdapter(getApplicationContext());
		addEmailItemAdapter=new AddItemBaseAdapter(getApplicationContext());
		
		phoneLV = (ListView) findViewById(R.id.lv_phonenumber);
		fixListViewHeight(phoneLV);
		phoneLV.setAdapter(addPhoneItemAdapter);
		
		emailLV = (ListView) findViewById(R.id.lv_email);
		fixListViewHeight(emailLV);
		emailLV.setAdapter(addEmailItemAdapter);
		
		addPhoneItem = (ImageButton)findViewById(R.id.bt_add_phone_item);
		addPhoneItem.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				addPhoneItemAdapter.addItem();
			}
		});

	}
}