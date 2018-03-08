package com.vole.licene;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * ����
 * @User: vole
 * @date: 2018��3��5������4:33:06
 * @Function:
 */
public class Indexer {

	private IndexWriter writer;// д����ʵ��

	/**
	 * ���췽�� ʵ����IndexWriter
	 * @param indexDir
	 * @throws Exception
	 */
	public Indexer(String indexDir) throws Exception {
		Directory dir = FSDirectory.open(Paths.get(indexDir));
		Analyzer analyzer = new StandardAnalyzer(); // ��׼�ִ���
		IndexWriterConfig conf = new IndexWriterConfig(analyzer);
		writer = new IndexWriter(dir, conf);
	}

	/**
	 * �ر�д����
	 */
	public void close() throws Exception {
		writer.close();
	}

	/**
	 * ����ָ��Ŀ¼�������ļ�
	 * @param dataDir
	 * @throws Exception
	 */
	public int index(String dataDir) throws Exception {
		File[] files = new File(dataDir).listFiles();
		for (File file : files) {
			indexFile(file);
		}
		return writer.numDocs();
	}

	/**
	 * ����ָ���ļ�
	 * @param file
	 */
	private void indexFile(File file) throws Exception {
		System.out.println("�����ļ���" + file.getCanonicalPath());
		Document doc = getDocument(file);
		writer.addDocument(doc);

	}

	/**
	 * ��ȡ�ĵ����ĵ���������ÿ���ֶ�
	 * @param file
	 */
	private Document getDocument(File file) throws Exception {
		Document doc = new Document();
		doc.add(new TextField("contents", new FileReader(file)));
		doc.add(new TextField("fileName", file.getName(), Field.Store.YES));
		doc.add(new TextField("fullPath", file.getCanonicalPath(), Field.Store.YES));
		return doc;
	}
	
	public static void main(String[] args) {
		String indexDir="F:\\lucene";
		String dataDir="F:\\lucene\\data";
		Indexer indexer = null;
		int numIndexed=0;
		long start=System.currentTimeMillis();
		try {
			indexer = new Indexer(indexDir);
			numIndexed = indexer.index(dataDir);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				indexer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		long end=System.currentTimeMillis();
		System.out.println("������"+numIndexed+" ���ļ� ������"+(end-start)+" ����");
	}

}
