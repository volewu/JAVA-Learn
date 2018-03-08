## Lucene5

[TOC]

--------------------目录--------------------

* 一、Lucene 的概述
  * 1.1 lucene  简介
  * 1.2 Lucene HelloWorld 实现
* 二、构建索引
  * 2.1 添加文档、删除文档、修改文档
  * 2.2 文档域加权
* 三、搜索功能
  * 3.1 对特定项搜索（TermQuery）、查询表达式（QueryParser）
  * 3.2 分页实现
  * 3.3 其他查询方式
* 四、中文分词& 高亮显示

--------------------目录--------------------

#### 一、Lucene 的概述

##### 1.1 lucene  简介

> [Lucene ](http://lucene.apache.org/)是 apache 软件基金会 4 jakarta 项目组的一个子项目，是一个**开放源代码**的全文检索引擎工具包，但不是一个完整的全文检索引擎，而是一个全文检索引擎的架构，提供完整的查询引擎和索引引擎，部分文本分析引擎。Lucene 的目的是为软件开发人员提供一个简单易用的工具包，已方便的在目标系统中实现全文检索的功能，或者是以此为基础建立起完整的全文检索引擎。

##### 1.2 Lucene HelloWorld 实现 

* 利用 Maven 构建项目，在 pom.xml 中添加相应 jar

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>com.vole.lucene</groupId>
	<artifactId>Lucene</artifactId>
	<version>0.0.1-SNAPSHOT</version>

	<dependencies>
		<dependency>
			<groupId>org.apache.lucene</groupId>
			<artifactId>lucene-core</artifactId>
			<version>5.3.1</version>
		</dependency>

		<dependency>
			<groupId>org.apache.lucene</groupId>
			<artifactId>lucene-queryparser</artifactId>
			<version>5.3.1</version>
		</dependency>

		<dependency>
			<groupId>org.apache.lucene</groupId>
			<artifactId>lucene-analyzers-common</artifactId>
			<version>5.3.1</version>
		</dependency>

	</dependencies>

</project>
```

* 创建索引案例 Indexer.java

```java
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
 * 索引
 * @User: vole
 * @date: 2018年3月5日下午4:33:06
 * @Function:
 */
public class Indexer {

	private IndexWriter writer;// 写索引实例

	/**
	 * 构造方法 实例化IndexWriter
	 * @param indexDir
	 * @throws Exception
	 */
	public Indexer(String indexDir) throws Exception {
		Directory dir = FSDirectory.open(Paths.get(indexDir)); //保存索引的位置
		Analyzer analyzer = new StandardAnalyzer(); // 标准分词器
		IndexWriterConfig conf = new IndexWriterConfig(analyzer);
		writer = new IndexWriter(dir, conf);
	}

	/**
	 * 关闭写索引
	 */
	public void close() throws Exception {
		writer.close();
	}

	/**
	 * 索引指定目录的所有文件
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
	 * 索引指定文件
	 * @param file
	 */
	private void indexFile(File file) throws Exception {
		System.out.println("索引文件：" + file.getCanonicalPath());
		Document doc = getDocument(file);
		writer.addDocument(doc);

	}

	/**
	 * 获取文档，文档里再设置每个字段
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
		System.out.println("索引："+numIndexed+" 个文件 花费了"+(end-start)+" 毫秒");
	}

}
```

* 创建搜索案例 Indexer.java

```java
package com.vole.licene;

import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * 搜索
 * @User: vole
 * @date: 2018年3月5日下午5:09:28
 * @Function:
 */
public class Seracher {

	public static void search(String indexDir, String q) throws Exception {
		Directory dir = FSDirectory.open(Paths.get(indexDir));
		IndexReader reader = DirectoryReader.open(dir); //读取文档
		IndexSearcher is = new IndexSearcher(reader);
		Analyzer analyzer = new StandardAnalyzer(); // 标准分词器
		QueryParser parser = new QueryParser("contents", analyzer);
		Query query = parser.parse(q);
		long start = System.currentTimeMillis();
		TopDocs hits = is.search(query, 10);
		long end = System.currentTimeMillis();
		System.out.println("匹配 " + q + " ，总共花费" + (end - start) + "毫秒" + "查询到" + hits.totalHits + "个记录");
		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = is.doc(scoreDoc.doc);
			System.out.println(doc.get("fullPath"));
		}
		reader.close();
	}

	public static void main(String[] args) {
		String indexDir = "F:\\lucene";
		String q = "Zygmunt Saloni";
		try {
			search(indexDir, q);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
```



####  二、构建索引

##### 2.1 添加文档、删除文档、修改文档

> StringField ：不分词
>
> TestField ：分词

* eg : 创建 Lucene02 项目，并添加相应的 jar ，然后 test

```java
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
```

##### 2.2 文档域加权

> 作用：Search 的排名结果提高

* IndexingTest2.java

```java
package com.vole.lucene;

import static org.junit.Assert.*;

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
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

public class IndexingTest2 {

	private String ids[] = { "1", "2", "3", "4" };
	private String authors[] = { "Jack", "Marry", "John", "Json" };
	private String positions[] = { "accounting", "technician", "salesperson", "boss" };
	private String titles[] = { "Java is a good language.", "Java is a cross platform language", "Java powerful",
			"You should learn java" };
	private String contents[] = { "If possible, use the same JRE major version at both index and search time.",
			"When upgrading to a different JRE major version, consider re-indexing. ",
			"Different JRE major versions may implement different versions of Unicode,",
			"For example: with Java 1.4, `LetterTokenizer` will split around the character U+02C6," };

	private Directory dir;

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
	 * 生成索引
	 * @throws Exception
	 */
	@Test
	public void testIndexer() throws Exception {
		dir = FSDirectory.open(Paths.get("D:\\lucene2"));
		IndexWriter writer = getWrite();

		for (int i = 0; i < ids.length; i++) {
			Document doc = new Document();
			doc.add(new StringField("id", ids[i], Field.Store.YES));
			doc.add(new StringField("author", authors[i], Field.Store.YES));
			doc.add(new StringField("position", positions[i], Field.Store.YES));
			// 加权操作
			TextField field = new TextField("title", titles[i], Field.Store.YES);
			if ("boss".equals(positions[i])) {
				field.setBoost(1.5f);
			}
			doc.add(field);
			doc.add(new TextField("content", contents[i], Field.Store.NO));
			writer.addDocument(doc);
		}
		writer.close();
	}

	/**
	 * 查询
	 * @throws Exception
	 */
	@Test
	public void testSearch() throws Exception {
		dir = FSDirectory.open(Paths.get("D:\\lucene2"));
		IndexReader reader = DirectoryReader.open(dir);
		IndexSearcher is = new IndexSearcher(reader);
		String searchField = "title";
		String q = "java";
		Term t = new Term(searchField, q);
		Query query = new TermQuery(t);
		TopDocs hits = is.search(query, 10);
		System.out.println("匹配 '" + q + "'，总共查询到" + hits.totalHits + "个文档");
		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = is.doc(scoreDoc.doc);
			System.out.println(doc.get("author"));
		}
		reader.close();
	}
}
//不加权的搜索结果
//匹配 'java'，总共查询到4个文档
//John
//Jack
//Marry
//json

/**
* 加权的搜索结果
* 匹配 'java'，总共查询到4个文档
* Json
* John
* Jack
* Marry
**/
```



#### 三、搜索功能

##### 3.1 对特定项搜索（TermQuery）、查询表达式（QueryParser）

* eg：该案例的数据采用的是上面 HelloWorld 项目的资历，只是改变了存储地址
* 生成对应的索引 Indexer.java

```java
package com.vole.lucene;

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
 * 索引
 * @User: vole
 * @date: 2018年3月5日下午4:33:06
 * @Function:
 */
public class Indexer {

	private IndexWriter writer;// 写索引实例

	/**
	 * 构造方法 实例化IndexWriter
	 * @param indexDir
	 * @throws Exception
	 */
	public Indexer(String indexDir) throws Exception {
		Directory dir = FSDirectory.open(Paths.get(indexDir));
		Analyzer analyzer = new StandardAnalyzer(); // 标准分词器
		IndexWriterConfig conf = new IndexWriterConfig(analyzer);
		writer = new IndexWriter(dir, conf);
	}

	/**
	 * 关闭写索引
	 */
	public void close() throws Exception {
		writer.close();
	}

	/**
	 * 索引指定目录的所有文件
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
	 * 索引指定文件
	 * @param file
	 */
	private void indexFile(File file) throws Exception {
		System.out.println("索引文件：" + file.getCanonicalPath());
		Document doc = getDocument(file);
		writer.addDocument(doc);

	}

	/**
	 * 获取文档，文档里再设置每个字段
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
		String indexDir="D:\\lucene4";
		String dataDir="D:\\lucene4\\data";
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
		System.out.println("索引："+numIndexed+" 个文件 花费了"+(end-start)+" 毫秒");
	}

}
```

* 测试 SearchTest.java

```java
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
```

##### 3.2 分页实现

* 方法一：**TopDocs hits = is.search(query, 100)** ，取出一百条数据放入内存中，然后创建一个集合并把数据放入集合中，然后分条取出数据
* 方法二： 同样取出 一百条数据放入内存中，然后根据 **hits.scoreDocs（它是一个集合）** 判断索引，在分条去取出。**推荐使用**



##### 3.3 其他查询方式

* **指定项范围查询 TermRangeQuery  （不常用，了解即可）**
* **指定数字范围查询 NumericRangeQuery **
* **指定字符串开头搜索 PrefixQuery**
* **组合查询 BooleanQuery**
* eg:
* 生成对应的索引 Indexer.java


```java
public class Indexer {

	private Integer ids[]={1,2,3};
	private String citys[]={"aingdao","banjing","changhai"};
	private String descs[]={
			"Qingdao is b beautiful city.",
			"Nanjing is c city of culture.",
			"Shanghai is d bustling city."
	};
	
	private Directory dir;
	
	/**
	 * 获取IndexWriter实例
	 * @return
	 * @throws Exception
	 */
	private IndexWriter getWriter()throws Exception{
		Analyzer analyzer=new StandardAnalyzer(); // 标准分词器
		IndexWriterConfig iwc=new IndexWriterConfig(analyzer);
		IndexWriter writer=new IndexWriter(dir, iwc);
		return writer;
	}
	
	/**
	 * 生成索引
	 * @param indexDir
	 * @throws Exception
	 */
	private void index(String indexDir)throws Exception{
		dir=FSDirectory.open(Paths.get(indexDir));
		IndexWriter writer=getWriter();
		for(int i=0;i<ids.length;i++){
			Document doc=new Document();
			doc.add(new IntField("id", ids[i], Field.Store.YES));
			doc.add(new StringField("city",citys[i],Field.Store.YES));
			doc.add(new TextField("desc", descs[i], Field.Store.YES));
			writer.addDocument(doc); // 添加文档
		}
		writer.close();
	}
	
	
	public static void main(String[] args) throws Exception {
		new Indexer().index("D:\\lucene5");
	}
	
}
```

* SearchTest.java

```java
public class SearchTest {

	private Directory dir;
	private IndexReader reader;
	private IndexSearcher is;
	
	@Before
	public void setUp() throws Exception {
		dir=FSDirectory.open(Paths.get("D:\\lucene5"));
		reader=DirectoryReader.open(dir);
		is=new IndexSearcher(reader);
	}

	@After
	public void tearDown() throws Exception {
		reader.close();
	}

	/**
	 * 指定项范围搜索
	 * @throws Exception
	 */
	@Test
	public void testTermRangeQuery()throws Exception{
		TermRangeQuery query=new TermRangeQuery("desc", new BytesRef("b".getBytes()), new BytesRef("c".getBytes()), true, true);
		TopDocs hits=is.search(query, 10);
		for(ScoreDoc scoreDoc:hits.scoreDocs){
			Document doc=is.doc(scoreDoc.doc);
			System.out.println(doc.get("id"));
			System.out.println(doc.get("city"));
			System.out.println(doc.get("desc"));
		}		
	}
	
	/**
	 * 指定数字范围
	 * @throws Exception
	 */
	@Test
	public void testNumericRangeQuery()throws Exception{
		NumericRangeQuery<Integer> query=NumericRangeQuery.newIntRange("id", 1, 2, true, true);
		TopDocs hits=is.search(query, 10);
		for(ScoreDoc scoreDoc:hits.scoreDocs){
			Document doc=is.doc(scoreDoc.doc);
			System.out.println(doc.get("id"));
			System.out.println(doc.get("city"));
			System.out.println(doc.get("desc"));
		}		
	}
	
	/**
	 * 指定字符串开头搜索
	 * @throws Exception
	 */
	@Test
	public void testPrefixQuery()throws Exception{
		PrefixQuery query=new PrefixQuery(new Term("city","a"));
		TopDocs hits=is.search(query, 10);
		for(ScoreDoc scoreDoc:hits.scoreDocs){
			Document doc=is.doc(scoreDoc.doc);
			System.out.println(doc.get("id"));
			System.out.println(doc.get("city"));
			System.out.println(doc.get("desc"));
		}	
	}
	
	/**
	 * 多条件查询
	 * @throws Exception
	 */
	@Test
	public void testBooleanQuery()throws Exception{
		NumericRangeQuery<Integer> query1=NumericRangeQuery.newIntRange("id", 1, 2, true, true);
		PrefixQuery query2=new PrefixQuery(new Term("city","a"));
		BooleanQuery.Builder booleanQuery=new BooleanQuery.Builder();
		booleanQuery.add(query1,BooleanClause.Occur.MUST);
		booleanQuery.add(query2,BooleanClause.Occur.MUST);
		TopDocs hits=is.search(booleanQuery.build(), 10);
		for(ScoreDoc scoreDoc:hits.scoreDocs){
			Document doc=is.doc(scoreDoc.doc);
			System.out.println(doc.get("id"));
			System.out.println(doc.get("city"));
			System.out.println(doc.get("desc"));
		}	
	}

}
```



#### 四、中文分词& 高亮显示

* **中文分词 smartcn**、**检索结果高亮显示实现**
* 在 pom.xml 中导入相关 jar

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>com.vole.lucene</groupId>
	<artifactId>Lucene02</artifactId>
	<version>0.0.1-SNAPSHOT</version>

	<dependencies>
		<dependency>
			<groupId>junit</groupId>
			<artifactId>junit</artifactId>
			<version>4.12</version>
		</dependency>

		<dependency>
			<groupId>org.apache.lucene</groupId>
			<artifactId>lucene-core</artifactId>
			<version>5.3.1</version>
		</dependency>

		<dependency>
			<groupId>org.apache.lucene</groupId>
			<artifactId>lucene-queryparser</artifactId>
			<version>5.3.1</version>
		</dependency>

		<dependency>
			<groupId>org.apache.lucene</groupId>
			<artifactId>lucene-analyzers-common</artifactId>
			<version>5.3.1</version>
		</dependency>

		<dependency>
			<groupId>org.apache.lucene</groupId>
			<artifactId>lucene-analyzers-smartcn</artifactId>
			<version>5.3.1</version>
		</dependency>
	
		<dependency>
			<groupId>org.apache.lucene</groupId>
			<artifactId>lucene-highlighter</artifactId>
			<version>5.3.1</version>
		</dependency>

	</dependencies>

</project>
```

* 生成索引 IndexerChinese.java

```java
public class IndexerChinese {

	private Integer ids[]={1,2,3};
	private String citys[]={"青岛","南京","上海"};
	private String descs[]={
			"青岛是一个美丽的城市。",
			"南京是一个有文化的城市。南京是一个文化的城市南京，简称宁，是江苏省会，地处中国东部地区，长江下游，濒江近海。全市下辖11个区，总面积6597平方公里，2013年建成区面积752.83平方公里，常住人口818.78万，其中城镇人口659.1万人。[1-4] “江南佳丽地，金陵帝王州”，南京拥有着6000多年文明史、近2600年建城史和近500年的建都史，是中国四大古都之一，有“六朝古都”、“十朝都会”之称，是中华文明的重要发祥地，历史上曾数次庇佑华夏之正朔，长期是中国南方的政治、经济、文化中心，拥有厚重的文化底蕴和丰富的历史遗存。[5-7] 南京是国家重要的科教中心，自古以来就是一座崇文重教的城市，有“天下文枢”、“东南第一学”的美誉。截至2013年，南京有高等院校75所，其中211高校8所，仅次于北京上海；国家重点实验室25所、国家重点学科169个、两院院士83人，均居中国第三。[8-10] 。",
			"上海是一个繁华的城市。"
	};
	
	private Directory dir;
	
	/**
	 * 获取IndexWriter实例
	 * @return
	 * @throws Exception
	 */
	private IndexWriter getWriter()throws Exception{
		//Analyzer analyzer=new StandardAnalyzer(); // 标准分词器
		SmartChineseAnalyzer analyzer=new SmartChineseAnalyzer();
		IndexWriterConfig iwc=new IndexWriterConfig(analyzer);
		IndexWriter writer=new IndexWriter(dir, iwc);
		return writer;
	}
	
	/**
	 * 生成索引
	 * @param indexDir
	 * @throws Exception
	 */
	private void index(String indexDir)throws Exception{
		dir=FSDirectory.open(Paths.get(indexDir));
		IndexWriter writer=getWriter();
		for(int i=0;i<ids.length;i++){
			Document doc=new Document();
			doc.add(new IntField("id", ids[i], Field.Store.YES));
			doc.add(new StringField("city",citys[i],Field.Store.YES));
			doc.add(new TextField("desc", descs[i], Field.Store.YES));
			writer.addDocument(doc); // 添加文档
		}
		writer.close();
	}
	
	public static void main(String[] args) throws Exception {
		new IndexerChinese().index("D:\\lucene6");
	}
}
```

* 生成搜索 SearchChinese.java

```java
public class SearchChinese {

	public static void search(String indexDir,String q)throws Exception{
		Directory dir=FSDirectory.open(Paths.get(indexDir));
		IndexReader reader=DirectoryReader.open(dir);
		IndexSearcher is=new IndexSearcher(reader);
		// Analyzer analyzer=new StandardAnalyzer(); // 标准分词器
		SmartChineseAnalyzer analyzer=new SmartChineseAnalyzer();
		QueryParser parser=new QueryParser("desc", analyzer);
		Query query=parser.parse(q);
		long start=System.currentTimeMillis();
		TopDocs hits=is.search(query, 10);
		long end=System.currentTimeMillis();
		System.out.println("匹配 "+q+" ，总共花费"+(end-start)+"毫秒"+"查询到"+hits.totalHits+"个记录");
		
		QueryScorer scorer=new QueryScorer(query);
		Fragmenter fragmenter=new SimpleSpanFragmenter(scorer);
		SimpleHTMLFormatter simpleHTMLFormatter=new SimpleHTMLFormatter("<b><font color='red'>","</font></b>");
		Highlighter highlighter=new Highlighter(simpleHTMLFormatter, scorer);
		highlighter.setTextFragmenter(fragmenter);
		for(ScoreDoc scoreDoc:hits.scoreDocs){
			Document doc=is.doc(scoreDoc.doc);
			System.out.println(doc.get("city"));
			System.out.println(doc.get("desc"));
			String desc=doc.get("desc");
			if(desc!=null){
				TokenStream tokenStream=analyzer.tokenStream("desc", new StringReader(desc));
				System.out.println(highlighter.getBestFragment(tokenStream, desc));
			}
		}
		reader.close();
	}
	
	public static void main(String[] args) {
		String indexDir="D:\\lucene6";
		String q="南京文明";
		try {
			search(indexDir,q);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
```

