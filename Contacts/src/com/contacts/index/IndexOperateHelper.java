package com.contacts.index;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.standard.parser.ParseException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.contacts.contact.Contact;
import com.contacts.util.PinyinUtil;

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

	/**
	 * 添加新的联系人记录
	 * 
	 * @param contactId
	 * @param name
	 * @param content
	 * @param dataType
	 */
	public void addCommonDocument(String contactId, String name,
			String content, String dataType) {
		Document doc = newDocument(contactId, name, content, dataType);// 创建文档

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

	/**
	 * 添加新的标签记录
	 * 
	 * @param contactId
	 * @param name
	 * @param content
	 * @param tagId
	 */
	public void addTagDocument(String contactId, String name, String content,
			String tagId) {
		Document doc = new Document();// 创建文档

		Field idField = new Field(IndexConfig.ID_FILED, contactId, Store.YES,
				Index.NOT_ANALYZED_NO_NORMS);
		Field nameField = new Field(IndexConfig.NAME_FILED, name, Store.YES,
				Index.NO);
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

	/**
	 * 新建记录
	 * 
	 * @param contactId
	 * @param name
	 * @param content
	 * @param dataType
	 * @return
	 */
	private Document newDocument(String contactId, String name, String content,
			String dataType) {
		Document doc = new Document();// 创建文档

		Field idField = new Field(IndexConfig.ID_FILED, contactId, Store.YES,
				Index.NOT_ANALYZED_NO_NORMS);
		Field nameField = new Field(IndexConfig.NAME_FILED, name, Store.YES,
				Index.NO);
		Field contentField = new Field(IndexConfig.CONTENT_FILED, content,
				Store.YES, Index.ANALYZED_NO_NORMS,
				Field.TermVector.WITH_POSITIONS_OFFSETS);
		Field typeField = new Field(IndexConfig.TYPE_FILED, dataType,
				Store.YES, Index.NO);

		doc.add(idField);
		doc.add(nameField);
		doc.add(contentField);
		doc.add(typeField);

		return doc;
	}

	/**
	 * 更新记录
	 * 
	 * @throws IOException
	 * @throws ParseException
	 */
	public void updateIndex(Contact contact){

		String contactId = contact.getId();
		if (contactId != null && contactId != "-1") {

			List<Document> docs = new ArrayList<Document>();

			String name = contact.getName();
			if (name != null) {
				if (!"".equals(name)){
					docs.add(newDocument(contactId, name, name,
							IndexConfig.TYPE_NAME));
					String fullPinyin = PinyinUtil.getFullPinYin(name);
					String firstPinyin = PinyinUtil.getFirstPinYin(name);
					docs.add(newDocument(contactId, name, fullPinyin, IndexConfig.TYPE_FULL_PINYIN));
					docs.add(newDocument(contactId, name, firstPinyin, IndexConfig.TYPE_FIRST_PINYIN));
				}

				String address = contact.getAddress();
				if (address != null && !"".equals(address))
					docs.add(newDocument(contactId, name, address,
							IndexConfig.TYPE_ADRESS));

				String note = contact.getNote();
				if (note != null && !"".equals(note))
					docs.add(newDocument(contactId, name, note,
							IndexConfig.TYPE_NOTE));

				List<String> phones = contact.getPhones();
				if (phones != null) {
					Iterator<String> it = phones.iterator();
					while (it.hasNext()) {
						String phoneNum = it.next();
						if (!"".equals(phoneNum)) {
							docs.add(newDocument(contactId, name, phoneNum,
									IndexConfig.TYPE_PHONE));
						}
					}
				}
				
				List<String> emails = contact.getEmails();
				if (emails != null) {
					Iterator<String> it = emails.iterator();
					while (it.hasNext()) {
						String email = it.next();
						if (!"".equals(email)) {
							docs.add(newDocument(contactId, name, email,
									IndexConfig.TYPE_EMAIL));
						}
					}
				}

			}

			try {
				indexWriter.updateDocuments(new Term(IndexConfig.ID_FILED,
						contactId), docs);
				indexWriter.commit();
			} catch (CorruptIndexException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	/**
	 * 删除联系人记录
	 * 
	 * @param id
	 */
	public void deleteContact(String id) {
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

	/**
	 * 删除标签里的联系人
	 * 
	 * @param contactId
	 * @param tagId
	 */
	public void deleteContactInTag(String contactId, String tagId) {

	}

	/**
	 * 删除标签
	 * 
	 * @param tagId
	 */
	public void deleteTag(String tagId) {

	}

	/**
	 * 关闭
	 */
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
