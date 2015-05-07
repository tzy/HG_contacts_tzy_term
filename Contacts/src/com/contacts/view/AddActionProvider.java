package com.contacts.view;

import android.content.Context;
import android.content.Intent;
import android.view.ActionProvider;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.SubMenu;
import android.view.View;

import com.contacts.activity.AddContact;
import com.contacts.activity.CaptureActivity;
import com.contacts.activity.NewTagActivity;

public class AddActionProvider extends ActionProvider {
	private Context mContext;

	public AddActionProvider(Context context) {
		super(context);
		mContext = context;
	}

	@Override
	public View onCreateActionView() {
		return null;
	}

	@Override
	public void onPrepareSubMenu(SubMenu subMenu) {
		subMenu.clear();
		subMenu.add("添加新联系人").setOnMenuItemClickListener(
				new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						Intent addContactIntent = new Intent(mContext, AddContact.class);
						mContext.startActivity(addContactIntent);
						return true;
					}
				});
		subMenu.add("扫一扫").setOnMenuItemClickListener(
				new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						Intent openCameraIntent = new Intent(mContext ,CaptureActivity.class);
						mContext.startActivity(openCameraIntent);
						return true;
					}
				});
		subMenu.add("新建标签").setOnMenuItemClickListener(
				new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						Intent newTagIntent = new Intent(mContext,
								NewTagActivity.class);
						mContext.startActivity(newTagIntent);
						return true;
					}
				});
	}

	@Override
	public boolean hasSubMenu() {
		return true;
	}

}
