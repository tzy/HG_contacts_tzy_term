package com.contacts.contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.contacts.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class ContactSelectAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<Contact> contacts;
	private HashMap<Integer, Boolean> isSelected;

	public ContactSelectAdapter(Context context, List<Contact> contacts) {
		// TODO Auto-generated constructor stub
		mInflater = LayoutInflater.from(context);
		this.contacts = contacts;
		isSelected = new HashMap<Integer, Boolean>();
		for (int i = 0; i < contacts.size(); i++) {
			isSelected.put(i, false);
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return contacts.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return contacts.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		// convertView为null的时候初始化convertView。
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.select_contact_item, null);
			holder.name = (TextView) convertView
					.findViewById(R.id.tv_select_name);
			holder.cBox = (CheckBox) convertView.findViewById(R.id.cb);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(contacts.get(position).getName());
		holder.cBox.setChecked(isSelected.get(position));
		return convertView;
	}

	public final class ViewHolder {
		public TextView name;
		public CheckBox cBox;
	}

	public void setSelected(int position, boolean isChecked) {
		isSelected.put(position, isChecked);
	}

	public List<Contact> getSelectedContacts() {
		List<Contact> selectedList = new ArrayList<Contact>();
		Iterator<Entry<Integer, Boolean>> iter = isSelected.entrySet()
				.iterator();
		while (iter.hasNext()) {
			Map.Entry<Integer, Boolean> entry = (Map.Entry<Integer, Boolean>) iter
					.next();
			Integer position = (Integer) entry.getKey();
			Boolean isSelected = (Boolean) entry.getValue();
			if (isSelected) {
				selectedList.add(contacts.get(position));
			}
		}
		return selectedList;
	}
}
