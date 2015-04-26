package com.contacts.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.contacts.index.IndexConfig;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class TestSearchActivity extends Activity {

	EditText searchEditText;
	Button searchButton;
	ListView resultListView;

	List<String> resultList;
	ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_search_layout);

		searchEditText = (EditText) findViewById(R.id.edt_query);
		searchButton = (Button) findViewById(R.id.bt_search);
		resultListView = (ListView) findViewById(R.id.lv_result);

		resultList = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, resultList);
		resultListView.setAdapter(adapter);

		searchButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String queryString = searchEditText.getText().toString();
				Directory director = null;
				IndexReader reader = null;
				try {
					director = FSDirectory.open(new File(
							TestSearchActivity.this.getApplicationContext()
									.getFilesDir().getAbsolutePath()
									+ IndexConfig.INDEXDIR));
					reader = IndexReader.open(director);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				IndexSearcher indexSearcher = new IndexSearcher(reader);
				Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
				QueryParser queryParser = new QueryParser(Version.LUCENE_36,
						IndexConfig.CONTENT_FILED, analyzer);
				try {
					Query query = queryParser.parse(queryString + "*");
					TopDocs topDocs = indexSearcher.search(query, 100);
					ScoreDoc[] scoreDocs = topDocs.scoreDocs;

					// 高亮
					Formatter formatter = new SimpleHTMLFormatter(
							"<font color='red'>", "</font>");
					Scorer score = new QueryScorer(query);
					Fragmenter fragmenter = new SimpleFragmenter(100);
					Highlighter highlighter = new Highlighter(formatter, score);
					highlighter.setTextFragmenter(fragmenter);
					for (int i = 0; i < scoreDocs.length; i++) {
						int docnum = scoreDocs[i].doc;
						Document doc = indexSearcher.doc(docnum);
						String id = doc.get(IndexConfig.ID_FILED);
						String content = doc.get(IndexConfig.CONTENT_FILED);
						if (content != null) {
							System.out.println(id);
							System.out.println(content);// 原内容
							TokenStream tokenStream = TokenSources
									.getAnyTokenStream(reader, docnum,
											IndexConfig.CONTENT_FILED, null);
							String str = highlighter.getBestFragment(
									tokenStream, content);// 得到高亮显示后的内容
							resultList.add(str);
						}
					}
					adapter.notifyDataSetChanged();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (CorruptIndexException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidTokenOffsetsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

	}
}
