package com.contacts.activity;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.search.ScoreDoc;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
								Log.d("myDebug", newString);
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

	private class SearchTask extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			contacts.clear();
			adapter.notifyDataSetChanged();
		}

		@Override
		protected Void doInBackground(String... queryStrings) {
			// TODO Auto-generated method stub
			IndexSearchHelper ish = new IndexSearchHelper(SearchActivity.this
					.getApplicationContext().getFilesDir().getAbsolutePath());
			ScoreDoc[] scoreDocs = ish.query(queryStrings[0]);
			if (scoreDocs != null) {
				for (int i = 0; i < scoreDocs.length; i++) {

					try {
						int docnum = scoreDocs[i].doc;
						Document doc = ish.getSearcher().doc(docnum);
						String id = doc.get(IndexConfig.ID_FILED);
						String content = doc.get(IndexConfig.CONTENT_FILED);
						String dataType = doc.get(IndexConfig.TYPE_FILED);
						HighLighter highLighter = new HighLighter(
								ish.getQuery());
						content = highLighter.highlighterText(ish.getReader(),
								docnum, content);
						
						ContactForSearch contact = new ContactForSearch(id);
						if (dataType.equals(IndexConfig.TYPE_NAME)) {
							contact.setName(content);
							contacts.add(contact);
						} else {
							String name = doc.get(IndexConfig.NAME_FILED);
							
							if (dataType.equals(IndexConfig.TYPE_FULL_PINYIN) ||
									dataType .equals(IndexConfig.TYPE_FIRST_PINYIN)){
								String highlightName = HighLighter.highlightString(name);
								contact.setName(highlightName); 
								contacts.add(contact);
							}else{
								contact.setName(name); 
								contact.setDate(dataType, content); 
								contacts.add(contact);
							}
						}
					} catch (CorruptIndexException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			adapter.notifyDataSetChanged();
		}
		
	}

}
