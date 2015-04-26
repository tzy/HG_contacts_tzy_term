package com.contacts.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AlphabetIndexer;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.contacts.contact.Contact;
import com.contacts.contact.ContactAdapter;
import com.contacts.db.DbHelper;
import com.contacts.db.SqliteQueryLoader;
  
public class ContactsFragment extends Fragment implements  
        LoaderManager.LoaderCallbacks<Cursor> {
	/**
	 * 数据库
	 */
	DbHelper dbHelper = null;
	SQLiteDatabase db = null;
	
	/**
	 * 缓冲布局
	 */
	private View rootView;
	
	/**
	 * 上下文对象
	 */
	private Context context;
	
	/**
	 * 联系人ListView
	 */
	private ListView contactsListView;
	//private SwipeMenuListView contactsListView;
	
	/**
	 * 存储联系人
	 */
	private List<Contact> contacts;
	
	/**
	 * 联系人列表适配器
	 */
	private ContactAdapter adapter;
	
	/**
	 * 用于进行字母表分组
	 */
	private AlphabetIndexer indexer;
	
	/**
	 * 分组字母的布局
	 */
	private LinearLayout titleLayout;
	
	/**
	 * 每一个联系人显示的分组字母
	 */
	private TextView title;
	
	/**
	 * 右侧可滑动字母表
	 */
	private Button alphabetButton;
	
	/**
	 * 弹出字母分组的布局
	 */
	private RelativeLayout sectionToastLayout;
	
	/**
	 * 弹出字母上的字母
	 */
	private TextView sectionToastText;
	
	/**
	 * 上次第一个可见元素，用于滚动时记录标识。
	 */
	private int lastFirstVisibleItem = -1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
		dbHelper = new DbHelper(context, DbHelper.DB_NAME);
		db = dbHelper.getReadableDatabase();
		
		contacts = new ArrayList<Contact>();
		adapter = new ContactAdapter(context, R.layout.item_contact, contacts);
	}
	
	/**
	 * 定义字母表的排序规则
	 */
	private String alphabet = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_contacts, null);
			initUI();
		}
		 /*缓存的rootView需要判断是否已经被加过parent，
		 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。*/
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;
	}
	
	private void initUI() {
		// TODO Auto-generated method stub
		titleLayout = (LinearLayout) rootView.findViewById(R.id.title_layout);
		title = (TextView) rootView.findViewById(R.id.title);

		contactsListView = (ListView) rootView.findViewById(
				R.id.listView);
		contactsListView.setAdapter(adapter);

		sectionToastLayout = (RelativeLayout) rootView.findViewById(
				R.id.section_toast_layout);
		sectionToastText = (TextView) rootView.findViewById(
				R.id.section_toast_text);
		alphabetButton = (Button) rootView.findViewById(R.id.alphabetButton);

	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getLoaderManager().initLoader(0, null, this);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		db.close();
		dbHelper.close();
	}
	

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		String[] projection = {DbHelper.NAME, DbHelper.SORT_KEY, DbHelper.CONTACT_ID};
		String sortOder = DbHelper.SORT_KEY +  " COLLATE LOCALIZED asc";
		return new SqliteQueryLoader(context, db, DbHelper.CONTACT_TABLE, projection, null, null, sortOder);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		// TODO Auto-generated method stub
		if (cursor != null && cursor.getCount() > 0) {
			
			if (cursor.moveToFirst()) {
				do {
					String name = cursor.getString(0);
					String sortKey = getSortKey(cursor.getString(1));
					String contactId = cursor.getString(2);
					Contact contact = new Contact(contactId);
					contact.setName(name);
					contact.setSortKey(sortKey);
					contacts.add(contact);
				} while (cursor.moveToNext());
			}
			indexer = new AlphabetIndexer(cursor, 1, alphabet);
			adapter.setIndexer(indexer);
			
			setupContactsListView();
			setAlpabetListener();
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		contacts.clear();
	}
	
	private void setupContactsListView() {

		/**
		 * 为联系人ListView设置监听事件，根据当前的滑动状态来改变分组的显示位置，从而实现挤压动画的效果。
		 */
		//OnItemClickListener
		contactsListView.setOnItemClickListener(new OnItemClickListener() {
		
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),ContactLookUpActivity.class); 
				Contact contact  = contacts.get(position);
				String contactId = contact.getId();
				String name = contact.getName();
				Bundle bundle = new Bundle();
				bundle.putString(DbHelper.CONTACT_ID, contactId);
				bundle.putString(DbHelper.NAME, name);
				intent.putExtras(bundle);
	            startActivity(intent); 
	            getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}});
		// onScrollListener
		contactsListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int section = indexer.getSectionForPosition(firstVisibleItem);
				int nextSecPosition = indexer
						.getPositionForSection(section + 1);
				if (firstVisibleItem != lastFirstVisibleItem) {
					MarginLayoutParams params = (MarginLayoutParams) titleLayout
							.getLayoutParams();
					params.topMargin = 0;
					titleLayout.setLayoutParams(params);
					title.setText(String.valueOf(alphabet.charAt(section)));
				}
				if (nextSecPosition == firstVisibleItem + 1) {
					View childView = view.getChildAt(0);
					if (childView != null) {
						int titleHeight = titleLayout.getHeight();
						int bottom = childView.getBottom();
						MarginLayoutParams params = (MarginLayoutParams) titleLayout
								.getLayoutParams();
						if (bottom < titleHeight) {
							float pushedDistance = bottom - titleHeight;
							params.topMargin = (int) pushedDistance;
							titleLayout.setLayoutParams(params);
						} else {
							if (params.topMargin != 0) {
								params.topMargin = 0;
								titleLayout.setLayoutParams(params);
							}
						}
					}
				}
				lastFirstVisibleItem = firstVisibleItem;
			}
		});
		
	}
  
	/**
	 * 设置字母表上的触摸事件，根据当前触摸的位置结合字母表的高度，计算出当前触摸在哪个字母上。
	 * 当手指按在字母表上时，展示弹出式分组。手指离开字母表时，将弹出式分组隐藏。
	 */
	@SuppressLint("ClickableViewAccessibility")
	private void setAlpabetListener() {
		alphabetButton.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				float alphabetHeight = alphabetButton.getHeight();
				float y = event.getY();
				int sectionPosition = (int) ((y / alphabetHeight) / (1f / 27f));
				if (sectionPosition < 0) {
					sectionPosition = 0;
				} else if (sectionPosition > 26) {
					sectionPosition = 26;
				}
				String sectionLetter = String.valueOf(alphabet
						.charAt(sectionPosition));
				int position = indexer.getPositionForSection(sectionPosition);
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					alphabetButton.setBackgroundResource(R.drawable.a_z_click);
					sectionToastLayout.setVisibility(View.VISIBLE);
					sectionToastText.setText(sectionLetter);
					contactsListView.setSelection(position);
					break;
				case MotionEvent.ACTION_MOVE:
					sectionToastText.setText(sectionLetter);
					contactsListView.setSelection(position);
					break;
				default:
					alphabetButton.setBackgroundResource(R.drawable.a_z);
					sectionToastLayout.setVisibility(View.GONE);
				}
				return true;
			}
		});
		
	}
	
	/**
	 * 获取sort key的首个字符，如果是英文字母就直接返回，否则返回#。
	 * 
	 * @param sortKeyString
	 *            数据库中读取出的sort key
	 * @return 英文字母或者#
	 */
	private String getSortKey(String sortKeyString) {
		if(sortKeyString.equals("")) return "#";
		String key = sortKeyString.substring(0, 1).toUpperCase();
		if (key.matches("[A-Z]")) {
			return key;
		}
		return "#";
	}
}  
