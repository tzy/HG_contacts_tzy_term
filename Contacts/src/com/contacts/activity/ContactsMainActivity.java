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
     * ����չʾ��ǩ��Fragment 
     */ 
	private TagsFragment flagsFragment;

	/** 
     * ����չʾ��ϵ�˵�Fragment 
     */ 
	private ContactsFragment contactsFragment;

	/** 
     * ��ϵ�˰�ť
     */  
	private ImageView contactsImage;

	/** 
     * ��ǩ��ť 
     */  
	private ImageView flagsImage;
	
	private static final int contactsIndex = 0;
	private static final int flagsIndex = 1;

	 /** 
     * ���ڶ�Fragment���й��� 
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
		// ��һ������ʱѡ���һ��tab
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

		// Ldrawer�����

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
			 // ���������Ϣtabʱ��ѡ�е�1��tab
			setTabSelection(contactsIndex);
			break;
		case R.id.iv_flags:
			 // ���������Ϣtabʱ��ѡ�е�1��2��tab
			setTabSelection(flagsIndex);
			break;
		default:
			break;
		}
	}
	
	/** 
     * ���ݴ����index����������ѡ�е�tabҳ�� 
     *  
     * @param index 
     *            ÿ��tabҳ��Ӧ���±ꡣ0��ʾ��Ϣ��1��ʾ��ϵ�ˣ�2��ʾ��̬��3��ʾ���á� 
     */  
	private void setTabSelection(int index) {
		// ÿ��ѡ��֮ǰ��������ϴε�ѡ��״̬
		clearSelection();
		// ����һ��Fragment���� 
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// �����ص����е�Fragment���Է�ֹ�ж��Fragment��ʾ�ڽ����ϵ����  
		hideFragments(transaction);
		switch (index) {
		case contactsIndex:
			// ���������ϵ��tabʱ���ı�ؼ���ͼƬ��������ɫ  
			if (contactsFragment == null) {
				// ���ContactsFragmentΪ�գ��򴴽�һ������ӵ�������  
				contactsFragment = new ContactsFragment();
				transaction.add(R.id.content, contactsFragment);
			} else {
				// ���ContactsFragment��Ϊ�գ���ֱ�ӽ�����ʾ����  
				transaction.show(contactsFragment);
			}
			break;
		case flagsIndex:
		default:
			// ���������Ϣtabʱ���ı�ؼ���ͼƬ��������ɫ  
			if (flagsFragment == null) {
				 // ���MessageFragmentΪ�գ��򴴽�һ������ӵ�������
				flagsFragment = new TagsFragment();
				transaction.add(R.id.content, flagsFragment);
			} else {
				 // ���MessageFragment��Ϊ�գ���ֱ�ӽ�����ʾ���� 
				transaction.show(flagsFragment);
			}
			break;
		
		}
		transaction.commit();
	}

	 /** 
     * ��������е�ѡ��״̬�� 
     */
	private void clearSelection() {
		
	}

	/** 
     * �����е�Fragment����Ϊ����״̬�� 
     *  
     * @param transaction 
     *            ���ڶ�Fragmentִ�в��������� 
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
