package com.contacts.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.contacts.contact.Contact;

public class AddContact extends Activity {

	private ImageButton addPhoneItem;
	private ImageButton addEmailItem;

	private Button btnSubmit;

	private EditText edtName;
	private EditText edtAdress;
	private EditText edtNote;

	private ListView phoneLV; // ListView控件
	private ListView emailLV;

	private AddItemBaseAdapter addPhoneItemAdapter;
	private AddItemBaseAdapter addEmailItemAdapter;

	private ArrayList<String> phones;
	private ArrayList<String> emails;

	Bundle bundle;

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
			listView.setLayoutParams(params);
			listView.requestLayout();
		}
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_additem_layout);

		initUI();
		setListener();

		bundle = this.getIntent().getExtras();
		if (bundle == null) {
			initData();
		} else {
			initUpdataData();
		}

	}

	private void initData() {
		phones = new ArrayList<String>();
		emails = new ArrayList<String>();
		phones.add("");
		emails.add("");
		addPhoneItemAdapter = new AddItemBaseAdapter(getApplicationContext(),
				phones);
		addEmailItemAdapter = new AddItemBaseAdapter(getApplicationContext(),
				emails);
		phoneLV.setAdapter(addPhoneItemAdapter);
		emailLV.setAdapter(addEmailItemAdapter);
	}

	private void initUpdataData() {

	}

	private void initUI() {
		// TODO Auto-generated method stub
		addPhoneItem = (ImageButton) findViewById(R.id.bt_add_phone_item);
		addEmailItem = (ImageButton) findViewById(R.id.bt_add_email_item);
		btnSubmit = (Button) findViewById(R.id.bt_submit);
		edtName = (EditText) findViewById(R.id.edt_username);
		edtAdress = (EditText) findViewById(R.id.edt_address);
		edtNote = (EditText) findViewById(R.id.edt_note);

		phoneLV = (ListView) findViewById(R.id.lv_phonenumber);
		fixListViewHeight(phoneLV);

		emailLV = (ListView) findViewById(R.id.lv_email);
		fixListViewHeight(emailLV);

	}

	private void setListener() {
		// TODO Auto-generated method stub
		addPhoneItem.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				addPhoneItemAdapter.addItem();
			}
		});

		addEmailItem.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				addEmailItemAdapter.addItem();
			}
		});

		btnSubmit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				InsertContactTask insertTask = new InsertContactTask(
						AddContact.this, getContact());
				insertTask.execute();
			}
		});
	}

	private Contact getContact() {
		Contact contact = new Contact();
		String name = edtName.getText().toString();
		String address = edtAdress.getText().toString();
		String note = edtNote.getText().toString();
		contact.setName(name).setAddress(address).setNote(note)
				.setPhones(phones).setEmails(emails);
		return contact;
	}

}