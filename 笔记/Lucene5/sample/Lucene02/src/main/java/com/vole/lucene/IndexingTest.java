package com.vole.lucene;

import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Before;
import org.junit.Test;

public class IndexingTest {

	private String ids[] = { "1", "2", "3" };
	private String citys[] = { "qingdao", "nanjing", "shanghai" };
	private String descs[] = { "Qingdao is a beautiful city.", "Nanjing is a city of culture.",
			"Shanghai is a bustling city." };

	private Directory dir;

	@Before
	public void setUp() throws Exception {
		dir = FSDirectory.open(Paths.get("D:\\lucene2"));
		IndexWriter writer = getWrite();
		for (int i = 0; i < ids.length; i++) {
			Document doc = new Document();
			doc.add(new StringField("id", ids[i], Field.Store.YES));
			doc.add(new StringField("citys", citys[i], Field.Store.YES));
			doc.add(new TextField("descs", descs[i], Field.Store.NO));
			writer.addDocument(doc);
		}
		writer.close();
	}

	/**
	 * 获取 IndexWriter 实例
	 * @return
	 * @throws Exception
	 */
	private IndexWriter getWrite() throws Exception {
		Analyzer analyzer = new StandardAnalyzer(); // 标准分词器
		IndexWriterConfig conf = new IndexWriterConfig(analyzer);
		IndexWriter writer = new IndexWriter(dir, conf);
		return writer;
	}

	/**
	 * 测试写了几个文档
	 * @throws Exception
	 */
	@Test
	public void testIndexWrite() throws Exception {
		IndexWriter writer = getWrite();
		System.out.println("写入了" + writer.numDocs() + "个文档");
		writer.close();
	}

	/**
	 * 测试读取文档
	 * @throws Exception
	 */
	@Test
	public void testIndexReader() throws Exception {
		IndexReader reader = DirectoryReader.open(dir);
		System.out.println("最大文档数：" + reader.maxDoc());
		System.out.println("实际文档数：" + reader.numDocs());
		reader.close();
	}

	/**
	 * 测试删除 在合并前
	 * @throws Exception
	 */
	@Test
	public void testDeleteBeforeMerge() throws Exception {
		IndexWriter writer = getWrite();
		System.out.println("删除前：" + writer.numDocs());
		writer.deleteDocuments(new Term("id", "1"));
		writer.commit();
		System.out.println("writer.maxDoc()：" + writer.maxDoc());
		System.out.println("writer.numDocs()：" + writer.numDocs());
		writer.close();
	}

	/**
	 * 测试删除 在合并后
	 * @throws Exception
	 */
	@Test
	public void testDeleteAfterMerge() throws Exception {
		IndexWriter writer = getWrite();
		System.out.println("删除前：" + writer.numDocs());
		writer.deleteDocuments(new Term("id", "1"));
		writer.forceMergeDeletes(); // 强制删除
		writer.commit();
		System.out.println("writer.maxDoc()：" + writer.maxDoc());
		System.out.println("writer.numDocs()：" + writer.numDocs());
		writer.close();
	}

	/**
	 * 测试更新 : 先删除后写入
	 * @throws Exception
	 */
	@Test
	public void testUpdate() throws Exception {
		IndexWriter writer = getWrite();
		Document doc = new Document();
		doc.add(new StringField("id", "1", Field.Store.YES));
		doc.add(new StringField("city", "qingdao", Field.Store.YES));
		doc.add(new TextField("desc", "dsss is a city.", Field.Store.NO));
		writer.updateDocument(new Term("id", "1"), doc);
		writer.close();
	}

}
