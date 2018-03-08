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
 * 搜索功能
 * @User: vole
 * @date: 2018年3月7日上午10:18:57
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
	 * 对特定项搜索
	 * @throws Exception
	 */
	@Test
	public void testTermQuery()throws Exception{
		String searchField="contents";
		String q = "java";
		Term t = new Term(searchField, q);
		Query query = new TermQuery(t);
		TopDocs hits = is.search(query, 10);
		System.out.println("匹配 '"+q+"'，总共查询到"+hits.totalHits+"个文档");
		for(ScoreDoc scoreDoc:hits.scoreDocs){
			Document doc=is.doc(scoreDoc.doc);
			System.out.println(doc.get("fullPath"));
		}
	}
	
	/**
	 * 解析查询表达式
	 * @throws Exception
	 */
	@Test
	public void testQueryParser()throws Exception{
		Analyzer analyzer=new StandardAnalyzer(); // 标准分词器
		String searchField="contents";
		String q = "particular AND Robert";// 包含 particular 和 Robert 两个关键字的文档
		//String q = "particular  java"; //有 particular 或 java 的文档
		//String q = "abc~"; //abc 相近的单词文档
		QueryParser parser = new QueryParser(searchField, analyzer);
		Query query = parser.parse(q);
		TopDocs hits = is.search(query, 10);
		System.out.println("匹配 '"+q+"'，总共查询到"+hits.totalHits+"个文档");
		for(ScoreDoc scoreDoc:hits.scoreDocs){
			Document doc=is.doc(scoreDoc.doc);
			System.out.println(doc.get("fullPath"));
		}
	}

}
