package com.contacts.activity;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ikimuhendis.ldrawer.ActionBarDrawerToggle;
import com.ikimuhendis.ldrawer.DrawerArrowDrawable;

public class ContactsMainActivity extends FragmentActivity implements OnClickListener,
		ActionBar.TabListener, ActionBar.OnNavigationListener {
	
	/** 
     * 用于展示标签的Fragment 
     */ 
	private TagsFragment flagsFragment;

	/** 
     * 用于展示联系人的Fragment 
     */ 
	private ContactsFragment contactsFragment;

	/** 
     * 联系人按钮
     */  
	private ImageView contactsImage;

	/** 
     * 标签按钮 
     */  
	private ImageView flagsImage;
	
	private static final int contactsIndex = 0;
	private static final int flagsIndex = 1;

	 /** 
     * 用于对Fragment进行管理 
     */  
	private FragmentManager fragmentManager;

	private DrawerLayout mDrawerLayout;
	private RelativeLayout mDrawerRL;
	//private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private DrawerArrowDrawable drawerArrow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initUI();

		initActionBar();

		fragmentManager = getSupportFragmentManager();
		// 第一次启动时选择第一个tab
		setTabSelection(contactsIndex);

	}

	private void initUI() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		//mDrawerList = (ListView) findViewById(R.id.navdrawer);
		mDrawerRL = (RelativeLayout) findViewById(R.id.navdrawer);
		
		contactsImage = (ImageView)findViewById(R.id.iv_contacts);
		flagsImage = (ImageView)findViewById(R.id.iv_flags);
		contactsImage.setOnClickListener(this);
		flagsImage.setOnClickListener(this);

	}

	private void initActionBar() {
		ActionBar ab = getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setHomeButtonEnabled(true);

		drawerArrow = new DrawerArrowDrawable(this) {
			@Override
			public boolean isLayoutRtl() {
				return false;
			}
		};
		Drawable colorDrawable = new ColorDrawable(Color.parseColor("#FF0099CC"
				.toString()));
		ab.setBackgroundDrawable(colorDrawable);

		// Ldrawer侧边栏

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				drawerArrow, R.string.drawer_open, R.string.drawer_close) {

			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerToggle.syncState();

		//String[] values = new String[] { "1", "2", "3", "4", "5", "6", "7" };

		//ArrayAdapter<String> LDadapter = new ArrayAdapter<String>(this,
		//		android.R.layout.simple_list_item_1, android.R.id.text1, values);
		//mDrawerList.setAdapter(LDadapter);

		/*mDrawerList
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						switch (position) {
						case 0:

							break;
						case 1:

							break;
						case 2:

							break;
						case 3:

							break;
						case 4:

							break;
						case 5:

							break;
						case 6:

							break;
						}

					}
				});*/
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_contacts:
			 // 当点击了消息tab时，选中第1个tab
			setTabSelection(contactsIndex);
			break;
		case R.id.iv_flags:
			 // 当点击了消息tab时，选中第1】2个tab
			setTabSelection(flagsIndex);
			break;
		default:
			break;
		}
	}
	
	/** 
     * 根据传入的index参数来设置选中的tab页。 
     *  
     * @param index 
     *            每个tab页对应的下标。0表示消息，1表示联系人，2表示动态，3表示设置。 
     */  
	private void setTabSelection(int index) {
		// 每次选中之前先清楚掉上次的选中状态
		clearSelection();
		// 开启一个Fragment事务 
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况  
		hideFragments(transaction);
		switch (index) {
		case contactsIndex:
			// 当点击了联系人tab时，改变控件的图片和文字颜色  
			if (contactsFragment == null) {
				// 如果ContactsFragment为空，则创建一个并添加到界面上  
				contactsFragment = new ContactsFragment();
				transaction.add(R.id.content, contactsFragment);
			} else {
				// 如果ContactsFragment不为空，则直接将它显示出来  
				transaction.show(contactsFragment);
			}
			break;
		case flagsIndex:
		default:
			// 当点击了消息tab时，改变控件的图片和文字颜色  
			if (flagsFragment == null) {
				 // 如果MessageFragment为空，则创建一个并添加到界面上
				flagsFragment = new TagsFragment();
				transaction.add(R.id.content, flagsFragment);
			} else {
				 // 如果MessageFragment不为空，则直接将它显示出来 
				transaction.show(flagsFragment);
			}
			break;
		
		}
		transaction.commit();
	}

	 /** 
     * 清除掉所有的选中状态。 
     */
	private void clearSelection() {
		
	}

	/** 
     * 将所有的Fragment都置为隐藏状态。 
     *  
     * @param transaction 
     *            用于对Fragment执行操作的事务 
     */ 
	private void hideFragments(FragmentTransaction transaction) {
		if (flagsFragment != null) {
			transaction.hide(flagsFragment);
		}
		if (contactsFragment != null) {
			transaction.hide(contactsFragment);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		switch (item.getItemId()) {

		case R.id.search_icon:

			Intent intent = new Intent(this,SearchActivity.class); 
            startActivity(intent); 
            overridePendingTransition(R.anim.push_up_in, R.anim.none);
			break;
			
		case R.id.additem_icon:
			
			Intent intent1 = new Intent(this,AddContact.class); 
            startActivity(intent1); 
            overridePendingTransition(R.anim.push_up_in, R.anim.none);
			break;
			
		case android.R.id.home:
			if (mDrawerLayout.isDrawerOpen(mDrawerRL)) {
				mDrawerLayout.closeDrawer(mDrawerRL);
			} else {
				mDrawerLayout.openDrawer(mDrawerRL);
			}
			break;

		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}


	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		return false;
	}


	@Override
	public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

}
