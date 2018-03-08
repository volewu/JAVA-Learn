package com.vole.lucene;

import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * ��������
 * @User: vole
 * @date: 2018��3��7������10:18:57
 * @Function:
 */
public class SearchTest {

	private IndexReader reader;
	private IndexSearcher is;
	private Directory dir;

	@Before
	public void setUp() throws Exception {
		dir = FSDirectory.open(Paths.get("D:\\lucene4"));
		reader = DirectoryReader.open(dir);
		is = new IndexSearcher(reader);
	}

	@After
	public void tearDown() throws Exception {
		reader.close();
	}
	
	/**
	 * ���ض�������
	 * @throws Exception
	 */
	@Test
	public void testTermQuery()throws Exception{
		String searchField="contents";
		String q = "java";
		Term t = new Term(searchField, q);
		Query query = new TermQuery(t);
		TopDocs hits = is.search(query, 10);
		System.out.println("ƥ�� '"+q+"'���ܹ���ѯ��"+hits.totalHits+"���ĵ�");
		for(ScoreDoc scoreDoc:hits.scoreDocs){
			Document doc=is.doc(scoreDoc.doc);
			System.out.println(doc.get("fullPath"));
		}
	}
	
	/**
	 * ������ѯ���ʽ
	 * @throws Exception
	 */
	@Test
	public void testQueryParser()throws Exception{
		Analyzer analyzer=new StandardAnalyzer(); // ��׼�ִ���
		String searchField="contents";
		String q = "particular AND Robert";// ���� particular �� Robert �����ؼ��ֵ��ĵ�
		//String q = "particular  java"; //�� particular �� java ���ĵ�
		//String q = "abc~"; //abc ����ĵ����ĵ�
		QueryParser parser = new QueryParser(searchField, analyzer);
		Query query = parser.parse(q);
		TopDocs hits = is.search(query, 10);
		System.out.println("ƥ�� '"+q+"'���ܹ���ѯ��"+hits.totalHits+"���ĵ�");
		for(ScoreDoc scoreDoc:hits.scoreDocs){
			Document doc=is.doc(scoreDoc.doc);
			System.out.println(doc.get("fullPath"));
		}
	}

}
