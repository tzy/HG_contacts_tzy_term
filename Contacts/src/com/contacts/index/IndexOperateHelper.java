package com.contacts.index;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class IndexOperateHelper {

	Analyzer mAnalyzer = null;
	IndexWriter indexWriter = null;

	public IndexOperateHelper(String dir) {
		mAnalyzer = AnalyzerFactory.getAnalyzer();

		try {
			Directory director = FSDirectory.open(new File(dir
					+ IndexConfig.INDEXDIR));// 创建Directory关联源文件
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(
					IndexConfig.VERSION, mAnalyzer);// 创建索引的配置信息
			indexWriter = new IndexWriter(director, indexWriterConfig);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Analyzer getAnalyzer() {
		return mAnalyzer;
	}

	public void addCommonDocument(String contactId, String name, String content,
			String dataType) {
		Document doc = new Document();// 创建文档

		Field idField = new Field(IndexConfig.ID_FILED, contactId, Store.YES,
				Index.NOT_ANALYZED_NO_NORMS);
		Field nameField = new Field(IndexConfig.NAME_FILED, name,
				Store.YES, Index.NO);
		Field contentField = new Field(IndexConfig.CONTENT_FILED, content, Store.YES,
				Index.ANALYZED_NO_NORMS,
				Field.TermVector.WITH_POSITIONS_OFFSETS);
		Field typeField = new Field(IndexConfig.TYPE_FILED, dataType,
				Store.YES, Index.NO);

		doc.add(idField);
		doc.add(nameField);
		doc.add(contentField);
		doc.add(typeField);

		try {
			indexWriter.addDocument(doc);
			indexWriter.commit();
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addTagDocument(String contactId, String name, String content, String tagId) {
		Document doc = new Document();// 创建文档

		Field idField = new Field(IndexConfig.ID_FILED, contactId, Store.YES,
				Index.NOT_ANALYZED_NO_NORMS);
		Field nameField = new Field(IndexConfig.NAME_FILED, name,
				Store.YES, Index.NO);
		Field contentField = new Field(IndexConfig.CONTENT_FILED, content,
				Store.YES, Index.ANALYZED_NO_NORMS,
				Field.TermVector.WITH_POSITIONS_OFFSETS);
		Field typeField = new Field(IndexConfig.TYPE_FILED,
				IndexConfig.TYPE_TAG, Store.YES, Index.NO);
		Field tagIdFild = new Field(IndexConfig.TAG_ID_FILED, tagId, Store.YES,
				Index.NOT_ANALYZED_NO_NORMS);

		doc.add(idField);
		doc.add(nameField);
		doc.add(contentField);
		doc.add(typeField);
		doc.add(tagIdFild);

		try {
			indexWriter.addDocument(doc);
			indexWriter.commit();
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void deleteContact(String id){
		try {
			indexWriter.deleteDocuments(new Term(IndexConfig.ID_FILED, id));
			indexWriter.commit();
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void deleteContactInTag(String contactId, String tagId){
		
	}

	public void deleteTag(String tagId){
		
	}
	
	public void close() {
		if (indexWriter != null)
			try {
				indexWriter.close();
			} catch (CorruptIndexException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}
