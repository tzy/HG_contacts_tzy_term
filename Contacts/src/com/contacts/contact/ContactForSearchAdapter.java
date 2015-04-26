package com.contacts.contact;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.contacts.activity.R;

public class ContactForSearchAdapter extends BaseAdapter{
	private List<ContactForSearch> contacts;
	private Context mContext;
	
	public  ContactForSearchAdapter(Context context, List<ContactForSearch> list){
		mContext = context;
		contacts = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return contacts.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return contacts.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = new ViewHolder();
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.test_searchitem_layout, null);
			holder.nameText = (TextView) convertView
					.findViewById(R.id.tv_result_name);
			holder.infoText = (TextView) convertView
					.findViewById(R.id.tv_result_info);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ContactForSearch contact = contacts.get(position);
		String name = contact.getName();
		if(name != null){
			holder.nameText.setText(Html.fromHtml(name));
		}
		String info = contact.getInfo();
		if(info != null){
			holder.infoText.setText(Html.fromHtml(info));
			holder.infoText.setVisibility(View.VISIBLE);
		}else{
			holder.infoText.setVisibility(View.GONE);
		}
		return convertView;
	}
	
	private final class ViewHolder {
		public TextView nameText;
		public TextView infoText;
	}

}
