package com.contacts.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.search.ScoreDoc;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.contacts.contact.ContactForSearch;
import com.contacts.contact.ContactForSearchAdapter;
import com.contacts.index.HighLighter;
import com.contacts.index.IndexConfig;
import com.contacts.index.IndexSearchHelper;
import com.contacts.util.ContactsUnionSet;

public class SearchActivity extends Activity {

	EditText eSearch;
	ImageView ivDeleteText;
	ListView mListView;

	ContactForSearchAdapter adapter;
	ArrayList<ContactForSearch> contacts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_layout);

		setTextChangListener();// 设置搜索框的文本改变时监听器

		setDeleteIvOnClickListener();// 设置叉叉的监听器

		set_mListView_adapter();// 给listview控件添加一个adapter

	}

	/**
	 * 设置ListView的Adapter
	 */
	private void set_mListView_adapter() {
		mListView = (ListView) findViewById(R.id.lv_search_result);
		contacts = new ArrayList<ContactForSearch>();
		adapter = new ContactForSearchAdapter(this, contacts);
		mListView.setAdapter(adapter);
	}

	/**
	 * 设置搜索框的文本更改时的监听器
	 */
	private void setTextChangListener() {
		eSearch = (EditText) findViewById(R.id.etSearch);

		eSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (s.length() == 0) {
					ivDeleteText.setVisibility(View.GONE);// 当文本框为空时，则叉叉消失
					contacts.clear();
					adapter.notifyDataSetChanged();
					return;
				} else {
					ivDeleteText.setVisibility(View.VISIBLE);// 当文本框不为空时，出现叉叉

					final String oldString = eSearch.getText().toString();
					new Handler().postDelayed(new Runnable() {
						public void run() {
							String newString = eSearch.getText().toString();
							if (oldString.equals(newString)) {
								SearchTask searchTask = new SearchTask();
								searchTask.execute(newString);
							}
						}
					}, 300);

				}

			}
		});

	}

	/**
	 * 设置叉叉的点击事件，即清空功能
	 */

	private void setDeleteIvOnClickListener() {
		ivDeleteText = (ImageView) findViewById(R.id.ivDeleteText);
		ivDeleteText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				eSearch.setText("");
			}
		});
	}

	private class SearchTask extends
			AsyncTask<String, Void, Collection<ContactForSearch>> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			contacts.clear();
			adapter.notifyDataSetChanged();
		}

		@Override
		protected Collection<ContactForSearch> doInBackground(
				String... queryStrings) {
			// TODO Auto-generated method stub
			IndexSearchHelper ish = new IndexSearchHelper(SearchActivity.this
					.getApplicationContext().getFilesDir().getAbsolutePath());
			ScoreDoc[] scoreDocs = ish.query(queryStrings[0]);
			if (scoreDocs != null && scoreDocs.length > 0) {

				// 结果集
				ContactsUnionSet contactSet = new ContactsUnionSet();

				for (int i = 0; i < scoreDocs.length; i++) {

					try {
						// 取出匹配的记录的信息
						int docnum = scoreDocs[i].doc;
						Document doc = ish.getSearcher().doc(docnum);
						String id = doc.get(IndexConfig.ID_FILED);
						String name = doc.get(IndexConfig.NAME_FILED);
						String content = doc.get(IndexConfig.CONTENT_FILED);
						String dataType = doc.get(IndexConfig.TYPE_FILED);

						// 高亮的类
						HighLighter highLighter = new HighLighter(
								queryStrings[0], ish.getQuery());

						// 信息封装
						ContactForSearch contact = new ContactForSearch(id);
						String highlightInfo;
						switch (Integer.parseInt(dataType)) {
						case IndexConfig.NAME_TYPE:
							highlightInfo = highLighter.defaultHighlight(
									ish.getReader(), docnum, content);
							contact.setName(highlightInfo);
							break;
						case IndexConfig.FIRST_PINYIN_TYPE:
						case IndexConfig.FULL_PINYIN_TYPE:
							highlightInfo = highLighter.highlightByPinyin(name,
									content);
							contact.setName(highlightInfo);
							break;
						case IndexConfig.PHONE_TYPE:
						case IndexConfig.EMAIL_TYPE:
							highlightInfo = highLighter
									.highlightPrefix(content);
							contact.setName(name);
							contact.setDate(dataType, highlightInfo);
							break;
						default:
							highlightInfo = highLighter.defaultHighlight(
									ish.getReader(), docnum, content);
							contact.setName(name);
							contact.setDate(dataType, highlightInfo);
							break;
						}
						contactSet.addContact(contact);

					} catch (CorruptIndexException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return contactSet.getSet();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Collection<ContactForSearch> result) {
			// TODO Auto-generated method stub
			if (result != null)
				contacts.addAll(result);
			adapter.notifyDataSetChanged();
		}

	}

}
