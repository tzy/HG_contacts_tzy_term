package com.contacts.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;

public class AddItemBaseAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<String> mData;// 存储的EditText值

	public AddItemBaseAdapter(Context context, ArrayList<String> data) {
		mData = data;
		mInflater = LayoutInflater.from(context);
	} 

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private Integer index = -1;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		// convertView为null的时候初始化convertView。
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.additem, null);
			holder.btnDelete = (ImageButton) convertView
					.findViewById(R.id.ibtnAddNumber);
			holder.editText = (EditText) convertView
					.findViewById(R.id.editNumber);
			holder.editText.setTag(position);
			holder.editText.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					if (event.getAction() == MotionEvent.ACTION_UP) {
						index = (Integer) v.getTag();
					}
					return false;
				}
			});

			class MyTextWatcher implements TextWatcher {

				private ViewHolder mHolder;

				public MyTextWatcher(ViewHolder holder) {
					mHolder = holder;
				}

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					if (s != null) {
						int position = (Integer) mHolder.editText.getTag();
						mData.set(position, s.toString());// 当EditText数据发生改变的时候存到data变量中
					}
				}

				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1,
						int arg2, int arg3) {
					// TODO Auto-generated method stub
				}

				@Override
				public void onTextChanged(CharSequence arg0, int arg1,
						int arg2, int arg3) {
					// TODO Auto-generated method stub
				}
			}
			holder.editText.addTextChangedListener(new MyTextWatcher(holder));
			
			class DeleteItemListener implements View.OnClickListener{

				private ViewHolder mHolder;
				
				public DeleteItemListener(ViewHolder holder) {
					mHolder = holder;
				}
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int position = (Integer) mHolder.editText.getTag();
					mData.remove(position);
					AddItemBaseAdapter.this.notifyDataSetChanged();
				}
				
			}
			holder.btnDelete.setOnClickListener(new DeleteItemListener(holder));
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			holder.editText.setTag(position);
		}
		
		Object value = mData.get(position);
		if (value != null) {
			holder.editText.setText(value.toString());
		} 
		holder.editText.clearFocus();
		if (index != -1 && index == position) {
			holder.editText.requestFocus();
		}
		return convertView;
	}

	private final class ViewHolder {
		public EditText editText;
		public ImageButton btnDelete;
	}

	public void addItem() {
		// TODO Auto-generated method stub
		mData.add("");
		notifyDataSetChanged();
	}
	
	public List<String> getData(){
		return mData;
	}
}
