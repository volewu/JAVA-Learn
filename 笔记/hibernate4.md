## Hibernate4 笔记

#### 一、简介：

> 官网：http://hibernate.org/
> Hibernate 是一个开放源代码的对象关系映射框架，它对 JDBC 进行了非常轻量级的对象封装，使得 Java
> 程序员可以随心所欲的使用对象编程思维来操纵数据库。 Hibernate 可以应用在任何使用 JDBC 的场合，
> 既可以在Java的客户端程序使用，也可以在Servlet/JSP的Web应用中使用，最具革命意义的是，Hibernate
> 可以在应用 EJB 的 J2EE 架构中取代 CMP，完成数据持久化的重任。
> ORM 框架，对象关系映射（Object/Relation Mapping）

#### 二、简单使用

* 导入 hibernate4 相关 jar 包

* 导入 mysql-connector-java-3.1.12-bin.jar

* 配置 hibernate.cfg.xml (该文件在 src 目录下)

  ```xml
  <?xml version='1.0' encoding='utf-8'?>
  <!DOCTYPE hibernate-configuration PUBLIC
          "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
          "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">

  <hibernate-configuration>

      <session-factory>

          <!--数据库连接设置 -->
          <property name="connection.driver_class">com.mysql.jdbc.Driver</property>
          <property name="connection.url">jdbc:mysql://localhost:3306/hibernate</property>
          <property name="connection.username">root</property>
          <property name="connection.password">123456</property>

         
          <!-- 方言 -->
          <property name="dialect">org.hibernate.dialect.MySQL5Dialect</property>
  	
          <!-- 控制台显示SQL -->
          <property name="show_sql">true</property>

          <!-- 自动更新表结构 -->
          <property name="hbm2ddl.auto">update</property>
          
    		<mapping resource="com/vole/model/Student.hbm.xml"/>

      </session-factory>

  </hibernate-configuration>
  ```

* 创建 Student.java

  ```java
  package com.vole.model;

  public class Student {
  	private long id;
  	private String name;

  	public long getId() {
  		return id;
  	}

  	public void setId(long id) {
  		this.id = id;
  	}

  	public String getName() {
  		return name;
  	}

  	public void setName(String name) {
  		this.name = name;
  	}
  }
  ```

* 创建 Student.hbm.xml 文件（一般使用注解，会在下一节说明）

  ```xml
  <?xml version="1.0"?>
  <!DOCTYPE hibernate-mapping PUBLIC
          "-//Hibernate/Hibernate Mapping DTD 3.0//EN"
          "http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd">

  <hibernate-mapping package="com.vole.model">

  	<class name="Student" table="t_student">
  		<id name="id" column="stuId"> 
  		<!-- column:别名  ，native:自增-->
  			<generator class="native"></generator> 
  		</id>
  		
  		<property name="name"></property>
  	</class>

  </hibernate-mapping>
  ```

* 创建测试类 StudentTest.java

  ```java
  package com.vole.service;

  import org.hibernate.Session;
  import org.hibernate.SessionFactory;
  import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
  import org.hibernate.cfg.Configuration;
  import org.hibernate.service.ServiceRegistry;

  import com.vole.model.Student;

  public class StudentTest {

  	public static void main(String[] args) {
  		Configuration configuration = new Configuration().configure();// 实例化配置文件
  		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
  				.applySettings(configuration.getProperties()).build();// 实例化服务登记
  		SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);// 获取 session 工厂
  		Session session= sessionFactory.openSession();// 生成一個 session
  		session.beginTransaction();// 开启事务
  	
  		Student student = new Student();
  		student.setName("vole");
  		session.save(student);
  		
  		session.getTransaction().commit();//提交事务
  		session.close();// 关闭 session
  		sessionFactory.close();//关闭 session 工厂
  	}
  }
  ```

  ​


####  三、Hibernate4 CRUD（Create、Retrieve、Update、Delete）

* HibernateUtil 封装: 利用单例获得 session 工厂

  ```java
  public class HibernateUtil {
  	
  	public static final SessionFactory SESSION_FACTORY=buildSessionFactory();

  	public static SessionFactory buildSessionFactory() {
  		Configuration configuration = new Configuration().configure();// 实例化配置文件
  		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
  				.applySettings(configuration.getProperties()).build();// 实例化服务登记
  		return configuration.buildSessionFactory(serviceRegistry);// 获取 session	工厂
  	}
  	
  	public static SessionFactory getSessionFactory(){
  		return SESSION_FACTORY;
  	}
  }
  ```

* 注解式

  ```java
  package com.vole.model;

  import javax.persistence.Entity;
  import javax.persistence.GeneratedValue;
  import javax.persistence.Id;
  import javax.persistence.Table;

  import org.hibernate.annotations.GenericGenerator;

  @Entity
  @Table(name = "t_teacher")
  public class Teacher {
  	private long id;
  	private String name;

  	@Id
  	@GeneratedValue(generator="_native")
  	@GenericGenerator(name="_native",strategy="native")
  	public long getId() {
  		return id;
  	}

  	public void setId(long id) {
  		this.id = id;
  	}

  	public String getName() {
  		return name;
  	}

  	public void setName(String name) {
  		this.name = name;
  	}

  	@Override
  	public String toString() {
  		return "Teacher [id=" + id + ", name=" + name + "]";
  	}

  }

  ```

  ​

* XML 版（是上面的 Student）与注解版 CRUD 实现

  ```java
  public class TeacherTest {
  	private static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

  	public static void main(String[] args) {
  		TeacherTest test = new TeacherTest();
  		test.add();
  		// test.delete();
  		// test.update();
  		// test.getAllTeacher();
  	}

  	public void add() {
  		Session session = sessionFactory.openSession();// 生成一個 session
  		session.beginTransaction();// 开启事务

  		Teacher teacher = new Teacher();
  		teacher.setName("vole");
  		session.save(teacher);

  		session.getTransaction().commit();// 提交事务
  		session.close();// 关闭 session
  	}

  	public void delete() {
  		Session session = sessionFactory.openSession();// 生成一個 session
  		session.beginTransaction();// 开启事务

  		Teacher teacher = (Teacher) session.get(Teacher.class, Long.valueOf(1));// 得到对象
  		session.delete(teacher);

  		session.getTransaction().commit();// 提交事务
  		session.close();// 关闭 session
  	}

  	public void update() {
  		Session session = sessionFactory.openSession();// 生成一個 session
  		session.beginTransaction();// 开启事务

  		Teacher teacher = (Teacher) session.get(Teacher.class, Long.valueOf(2));// 得到对象
  		teacher.setName("kong");
  		session.save(teacher);

  		session.getTransaction().commit();// 提交事务
  		session.close();// 关闭 session

  	}
    
    	public void getAllTeacher() {
  		Session session = sessionFactory.openSession();// 生成一個 session
  		session.beginTransaction();// 开启事务

        	//通过面向对象的方法获得所有数据
  		String hql = "from Teacher";
  		Query query = session.createQuery(hql);
  		List<Teacher> teachers = query.list();
  		for (Teacher teacher : teachers) {
  			System.out.println(teacher);
  		}

  		session.getTransaction().commit();// 提交事务
  		session.close();// 关闭 session
  	}

  }
  ```




#### 四、映射对象标识符(OID)

*  **Hibernate  用对象标识符(OID)**

```java
public static void main(String[] args) {
	SessionFactory sessionFactory = HibernateUtil.getSessionFactory();	
  	Session session = sessionFactory.openSession();// 生成一個 session
	session.beginTransaction();// 开启事务

	Student s1 = (Student) session.get(Student.class, Long.valueOf(1));// 得到对象
	Student s2 = (Student) session.get(Student.class, Long.valueOf(2));
  	Student s3 = (Student) session.get(Student.class, Long.valueOf(1));
  	System.out.println(s1==s2);//---false
  	System.out.println(s1==s3);//---true

	session.getTransaction().commit();// 提交事务
	session.close();// 关闭 session
	}
```

![OID(对象标示符)区分对象](E:\资料\java\JAVAWEB\OID(对象标示符)区分对象.png)

* **Hibernate   对象标识符生成策略：**

> 主键的分类 业务主键 VS 代理主键 **代理主键是不具有业务性的**；
>
> 1. increment 由 Hibernate 自动以递增的方式生成标识符，适用代理主键；
> 2. identity 由底层数据库生成标识符；适用代理主键；
> 3. sequcence 由 Hibernate 根据底层数据库的序列来生成标识符；适用代理主键；（mysql 没有）
> 4. hilo Hibernate 根据 high/low 算法来生成标识符。适用代理主键
> 5. native 根据底层数据库对自动生成标识符的支持能力， 来选择 identity,sequence 或 hilo；适用代理主键(一般使用这个)



#### 五、关联关系一对多映射

* **简单实现班级学生一对多映射实现（单向--多个学生对应一个班级）**

主要在 model 层中的 Student.java 添加一个 Class 属性，并 get/set 它 (Class.java 类与之类似，就不写了)

```java
public class Student {
	private long id;
	private String name;
	private Class c;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Class getC() {
		return c;
	}

	public void setC(Class c) {
		this.c = c;
	}

	@Override
	public String toString() {
		return "Student [id=" + id + ", name=" + name + "]";
	}
}
```

然后在 Student.hbm.xml 中添加下面的属性：

```xml
<many-to-one name="c" column="classId" class="com.vole.model.Class"></many-to-one>
```

最后测试：(成功关联)

```java
public static void main(String[] args) {
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();// 生成一個 session
		session.beginTransaction();// 开启事务
		
		Class c = new Class();
		c.setName("17应电");
		session.save(c);
		
		Student s1 = new Student();
		s1.setName("vole");
		s1.setC(c);
		
		Student s2 =new Student();
		s2.setName("kong");
		s2.setC(c);
		
		session.save(s1);
		session.save(s2);

		session.getTransaction().commit();// 提交事务
		session.close();// 关闭 session
	}
```
* **级联保存更新**

如果在创建 Clsss 对象是没有对它进行 **session.save(c)**，则它会变成**临时对象**，然后无法生成数据，在**<many-to-one>**这端，**cascade** 默认是**”none”**，假如我们希望在持久化多的一端的时候，自动级联保存和更新一的一端，我们可以把 cascade 设置成**”save-update”**；

![级联保存更新](E:\资料\java\JAVAWEB\级联保存更新.png)

  ​

* **班级学生一对多映射实现（双向）**:

在一的一端加入多的集合属性：并提供 get/set 方法：

```java
  private Set<Student>
  public Set<Student> getStudents() {
		return students;
	}

	public void setStudents(Set<Student> students) {
		this.students = students;
	}
```

配置 Class.hbm.xml ，加入下面的属性

```xml
<set name="students" cascade="save-update">
			<key column="classId"></key>
			<one-to-many class="com.vole.model.Student"/>
</set>
```

然后只要保存 Class 对象就行

```java
	@Test
	public void testGetAllStudent() {
		Class c = new Class();
		c.setName("17应电");

		Student s1 = new Student();
		s1.setName("vole");

		Student s2 = new Student();
		s2.setName("kong");

		c.getStudents().add(s1);
		c.getStudents().add(s2);
		session.save(c);
	}

	
	//通过班级得到学生
	@Test
	public void getStudentsByClass() {
		Class c = (Class) session.get(Class.class, Long.valueOf(1));
		Set<Student> students = c.getStudents();
		Iterator it = students.iterator();
		while (it.hasNext()) {
			Student s = (Student) it.next();
			System.out.println(s);
		}
	}
```
* **inverse 属性：作用--维护主键，管理两个没有关联的对象，一般是多的一端设置一的端，并且交给多的一端处理，节约内存，便于维护**

配置 Class.hbm.xml ，加入 **inverse="true"**的属性

```xml
		<set name="students" cascade="save-update" inverse="true">
			<key column="classId"></key>
			<one-to-many class="com.vole.model.Student"/>
		</set>
```

测试：

```java
@Test
	public void addData() {
		Class c = new Class();
		c.setName("16机本");

		Student s = new Student();
		s.setName("wu");

		session.save(c);
		session.save(s);
	}

	@Test
	public void testInverse() {
		Class c = (Class) session.get(Class.class, Long.valueOf(1));
		Student s = (Student) session.get(Student.class, Long.valueOf(1));
		
		s.setC(c);
	}
```
* **级联删除：一般不推荐这么做，只要在 Class.hbm.xml 把 cascade="delete" 即可**

**一对多双向自身关联关系映射** （有点懵比）

Node.java

```java
public class Node {
	
	private long id;
	private String name;
	
	private Node parentNode;
	
	private Set<Node> childNodes=new HashSet<Node>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Node getParentNode() {
		return parentNode;
	}

	public void setParentNode(Node parentNode) {
		this.parentNode = parentNode;
	}

	public Set<Node> getChildNodes() {
		return childNodes;
	}

	public void setChildNodes(Set<Node> childNodes) {
		this.childNodes = childNodes;
	}
}

```

Node.hbm.xml

```xml
<?xml version="1.0"?>
<!DOCTYPE hibernate-mapping PUBLIC
        "-//Hibernate/Hibernate Mapping DTD 3.0//EN"
        "http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd">

<hibernate-mapping package="com.vole.model">

	<class name="Node" table="t_node">
		<id name="id" column="nodeId">
			<generator class="native"></generator>
		</id>

		<property name="name" column="nodeName"></property>

		<many-to-one name="parentNode" column="parentId"
			class="com.vole.model.Node" cascade="save-update"/>
			
		<set name="childNodes"  inverse="true">
			<key column="parentId"></key>
			<one-to-many class="com.vole.model.Student" />
		</set>
	</class>

</hibernate-mapping>
```

test

```java
	@Test
	public void testNode() {
		Node node = new Node();
		node.setName("根");
		
		Node subNode1=new Node();
		subNode1.setName("子节点1");
		
		Node subNode2=new Node();
		subNode2.setName("子节点2");
		
		subNode1.setParentNode(node);
		subNode2.setParentNode(node);
		
		session.save(subNode1);
		session.save(subNode2);
	}
```



#### 六、Hibernate 操作对象

* **Hibernate 中四种对象状态**

  > **临时状态(transient)**：刚用 new 语句创建，还没有被持久化，并且不处于 Sesssion 的缓存中。处于临时状态的 Java 对象被称为临时对象。
  > **持久化状态(persistent)**：已经被持久化，并且加入到 Session 的缓存中。处于持久化状态的 Java 对象被称为持久化对象。
  > **删除状态(removed)**：不再处于 Session 的缓存中，并且 Session 已经计划将其从数据库中删除。处于删除状态的 Java 对象被称为删除对象。
  > **游离状态(detached)**：已经被持久化，但不再处于 Session 的缓存中。处于游离状态的 Java 对象被称为游离对象。

![Hibernate 四种状态](E:\资料\java\JAVAWEB\Hibernate 四种状态.png)

```java
Session session = sessionFactory.openSession();// 生成一個 session
		session.beginTransaction();// 开启事务
		
		Student s1 = new Student();//临时状态
		s1.setName("vole");
		
		Student s2 =new Student();//临时状态
		s2.setName("kong");
		
		session.save(s1);//持久化状态
		session.save(s2);//持久化状态

		session.delete(s2);

		session.getTransaction().commit();// 提交事务
		session.close();// 关闭 session
		System.out.println(s1.getName);//游离状态
		System.out.println(s2.getName);//删除状态
```

* **Session 常用方法讲解**

  1. **save()------将一个临时对象转变成持久化对象**

  2. **load() VS get()-----都是根据 OID 从数据库中加载一个持久化对象。**

     > **区别 1：假如数据库中不存在与 OID 对应的记录，Load()方法会抛出异常，而 get()方法返回 null;**
     >
     > **区别 2：load 方法默认采用延迟加载策略，get 方法采用立即检索策略**

  3. **update()------将一个游离对象转变成持久化对象**

  4. **saveOrUpdate()------包含了 save() 和 upda() 方法**

  5. **merge()-----合并对象**

  6. **delete()-----删除对象（一般用 loda() 来获取对象，节约内存，显得很 6）**

```java
	@Test
	public void testSaveClassAndStudent() {
		Class c=new Class();
	    c.setName("08计本");
	   
	    Student s1=new Student();
	    s1.setName("张三");
	    s1.setC(c);
	    
	    Student s2=new Student();
	    s2.setName("李四");
	    s2.setC(c);
	   
	    session.save(s1);
	    session.save(s2);
	}

	@Test
	public void testLoadClass(){
		// Class c=(Class)session.load(Class.class, Long.valueOf(2));
		Class c=(Class)session.load(Class.class, Long.valueOf(1));
		System.out.println(c.getStudents());
	}

	@Test
	public void testGetClass(){
		// Class c=(Class)session.get(Class.class, Long.valueOf(2));
		Class c=(Class)session.get(Class.class, Long.valueOf(1));
		System.out.println(c.getStudents());
	}

	@Test
	public void testUpdateClass(){
		Session session1=sessionFactory.openSession();
		session1.beginTransaction();
		Class c=(Class)session1.get(Class.class, Long.valueOf(1));
		session1.getTransaction().commit(); // 提交事务
		session1.close();
		
		Session session2=sessionFactory.openSession();
		session2.beginTransaction();
		c.setName("08计算机本科2");
		session2.update(c);
		session2.getTransaction().commit(); // 提交事务
		session2.close();
	}

	@Test
	public void testSaveOrUpdateClass(){
		Session session1=sessionFactory.openSession();
		session1.beginTransaction();
		Class c=(Class)session1.get(Class.class, Long.valueOf(1));
		session1.getTransaction().commit(); // 提交事务
		session1.close();
		
		Session session2=sessionFactory.openSession();
		session2.beginTransaction();
		c.setName("08计算机本科3");
		
		Class c2=new Class();
		c2.setName("09计算机本科3");
		session2.saveOrUpdate(c);
		session2.saveOrUpdate(c2);
		session2.getTransaction().commit(); // 提交事务
		session2.close();
	}
	
	@Test
	public void testMergeClass(){
		Session session1=sessionFactory.openSession();
		session1.beginTransaction();
		Class c=(Class)session1.get(Class.class, Long.valueOf(1));
		session1.getTransaction().commit(); // 提交事务
		session1.close();
		
		Session session2=sessionFactory.openSession();
		session2.beginTransaction();
		
		Class c2=(Class)session2.get(Class.class, Long.valueOf(1));
		c.setName("08计算机本科4");
	
		session2.merge(c);

		session2.getTransaction().commit(); // 提交事务
		session2.close();
	}
	
	@Test
	public void testDeleteStudent(){
		Student student=(Student)session.load(Student.class, Long.valueOf(1));
		session.delete(student);
	}
```



#### 七、Hibernate 映射类型

![Hibernate 基本类型映射](E:\资料\java\JAVAWEB\Hibernate 基本类型映射.png)

​        **clob: 大文本类型**                    **blob: 图片类型**

```xml
		<property name="bookName" column="bookName"></property>
		<property name="price" column="price" type="float"></property>
		<property name="specialPrice" column="specialPrice" type="boolean"></property>
		<property name="publishDate" column="publishDate" type="date"></property>
		<property name="author" column="author" length="20"></property>
		<property name="introduction" column="introduction" type="text"></property>
		<property name="bookPic" column="bookPic" type="blob"></property>
```



```java
		book.setPublishDate(new SimpleDateFormat("yyyy-MM-dd").parse("2017-10-16"));//时间转换

		//图片转换成 Blob
		LobHelper lobHelper = session.getLobHelper();
		InputStream in = new FileInputStream("F://die.jpg");
		Blob bookPic = lobHelper.createBlob(in, in.available());
```

* **集合类型映射**

1. Set 无序 元素不可重复

```java
//Student.java
private Set<String> images;
  
//Student.hbm.xml
 <set name="images" table="t_image">
		<key column="studentId"></key>
		<element column="imageName" type="string"></element>
</set>
   
  	@Test
	public void testSet() {
		Set<String> imageSet = new HashSet<>();
		imageSet.add("image1.png");
		imageSet.add("image2.png");
		imageSet.add("image3.png");
		imageSet.add("image3.png");
		Student s = new Student();
		s.setName("vole");
		s.setImages(imageSet);
		session.save(s);
	}

	@Test
	public void testSetFetch() {
		Student s = (Student) session.get(Student.class, Long.valueOf(1));
		Set<String> imageSet = s.getImages();
		Iterator it = imageSet.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}
  
```



1. List 有序 元素可重复

```java
	//Student2.java
	private List<String> images;

	//Student2.hbm.xml
	<list name="images" table="t_image2">
		<key column="studentId"></key>
		<list-index column="imageIndex"></list-index>
		<element column="imageName" type="string"></element>
	</list>
    
   	@Test
	public void testList() {
		List<String> imageSet = new ArrayList<>();
		imageSet.add("image1.png");
		imageSet.add("image2.png");
		imageSet.add("image3.png");
		imageSet.add("image3.png");

		Student2 s2 = new Student2();
		s2.setName("vole");
		s2.setImages(imageSet);

		session.save(s2);
	}

	@Test
	public void testListFetch() {
		Student2 s2 = (Student2) session.get(Student2.class, Long.valueOf(2));
		List<String> imageSet = s2.getImages();
		Iterator it = imageSet.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}
```



1. Bag 无序 元素可重复

```java
//Student2.hbm.xml
<idbag name="images" table="t_image3">
			<collection-id type="long" column="imageId">
				<generator class="increment"></generator>
			</collection-id>
			<key column="studentId"></key>
			<element column="imageName" type="string"></element>
</idbag>
/* 其他与 List 一样，只是配置不同*/
```

1. Map 键值对

```java
	//Student4.java
	private Map<String, String> images;
	
	//Student2.hbm.xml
	<map name="images" table="t_image4">
		<key column="studentId"></key>
		<map-key column="imageKey" type="string"></map-key>
		<element column="imageName" type="string"></element>
	</map>
      
     @Test
	public void testMap() {
		Map<String, String> imageSet = new HashMap<>();
		imageSet.put("i1", "image1.png");
		imageSet.put("i2", "image2.png");
		imageSet.put("i3", "image3.png");
		imageSet.put("i4", "image4.png");

		Student4 s4 = new Student4();
		s4.setName("vole3");
		s4.setImages(imageSet);

		session.save(s4);
	}

	@Test
	public void testMapFetch() {
		Student4 s4 = (Student4) session.get(Student4.class, Long.valueOf(4));
		Map<String, String> images = s4.getImages();
		Set keys = images.keySet();
		Iterator it = keys.iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			System.out.println(key + ": " + images.get(key));
		}
	}
```



#### 八、Hibernate 映射继承

##### 8.1 每个具体类对应一个表：

**Image.class(为抽象类)**

```java
public abstract class Image {
	private long id;
	private String imageName;
	private Student student;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}
}

```

**LifeImage.class 与 WorkImage.class 都继承 Image.class**

**Student.class**

```java
public class Student {
	private long id;
	private String name;
	private Set<Image> images;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Image> getImages() {
		return images;
	}

	public void setImages(Set<Image> images) {
		this.images = images;
	}

	@Override
	public String toString() {
		return "Student [id=" + id + ", name=" + name + "]";
	}
	
}

```

**Student.hbm.xml**

```xml
<class name="Student" table="tt_student">
		<id name="id" column="stuId">
			<generator class="native"></generator>
		</id>

		<property name="name" column="stuName"></property>
	</class>
```

**LifeImage.hbm.xml 与 WorkImage.hbm.xml**(名字更改即可)

```xml
<class name="LifeImage" table="t_lifeImage">
		<id name="id" column="lifeImageId">
			<generator class="native"></generator>
		</id>

		<property name="imageName" column="imageName"></property>
		<many-to-one name="student" column="stuId" class="com.vole.model.Student"></many-to-one>
	</class
```

**Test 类**

```java
	@Test
	public void testGetAllStudent() {
		List<Image> images = new ArrayList<>();
		int stuId=1;
		List<Image> lifeImage=session.createQuery("from LifeImage l where l.student.id="+stuId).list();
		images.addAll(lifeImage);
		List<Image> workImage=session.createQuery("from WorkImage w where w.student.id="+stuId).list();
		images.addAll(workImage);
		
		Iterator iterator= images.iterator();
		while (iterator.hasNext()){
			 Image image =(Image) iterator.next();
			 System.out.println(image.getImageName());
		}
	}
```



##### 8.2 根类对应一个表

**Image2.class  去掉抽象，在原来的基础上添加一个属性 imageType  并 get/set**

```java
private String imageType;
```

**Image2.hbm.xml**

```xml
	<class name="Image2" table="t_Image2">
		<id name="id" column="imageId">
			<generator class="native"></generator>
		</id>

		<discriminator column="imageType" type="string"></discriminator>
		<property name="imageName" column="imageName"></property>
		
		<many-to-one name="student" column="stuId" class="com.vole.model.Student2"></many-to-one>
		
		<subclass name="com.vole.model.LifeImage2" discriminator-value="li"></subclass>
		<subclass name="com.vole.model.WorkImage2" discriminator-value="wi"></subclass>
	</class>
```

**Student2.hbm.xml**

```xml
	<class name="Student2" table="tt_student2">
		<id name="id" column="stuId">
			<generator class="native"></generator>
		</id>

		<property name="name" column="stuName"></property>
		
		<set name="images">
			<key column="stuId"></key>
			<one-to-many class="com.vole.model.Image2"></one-to-many>
		</set>
	</class>
```

**Test**

```java
	@Test
	public void testGetAllStudent2() {
		Student2 student2 = (Student2) session.get(Student2.class, (long)1);
		Set<Image2> image2s = student2.getImages();
		Iterator it= image2s.iterator();
		while(it.hasNext()){
			Image2 image2 = (Image2) it.next();
			System.out.println(image2.getImageName());
		}
	}
```



##### 8.3 每个类对应一个表

**Image3.class 与 2 一样，去掉 ImageType 属性**

**Image3.hbm.xml**

```xml
<class name="Image3" table="tt_Image3">
		<id name="id" column="imageId">
			<generator class="native"></generator>
		</id>
		
		<property name="imageName" column="imageName"></property>
		
		<many-to-one name="student" column="stuId" class="com.vole.model.Student3"></many-to-one>
		
		<joined-subclass name="com.vole.model.LifeImage3" table="t_lifeImage3">
			<key column="lifeImageId"></key>
		</joined-subclass>
		
		<joined-subclass name="com.vole.model.WorkImage3" table="t_workImage3">
			<key column="workImageId"></key>
		</joined-subclass>
		
	</class>
```

**Student3.hbm.xml 与 2 一样**

**Test 和 2 一样**



#### 九 映射关系

#####  9.1一对一映射关系实现

###### 9.1.1 按照主键映射(两个表的主键相关联)

* **创建 User.class 与 Address.class 对象**

```java
public class User {

	private long id;
	private String name;
	private Address address;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

}

----------------------------------------
public class Address {

	private long id;
	private String address;
	private String zipcode;
	private User user;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
```

* **User.hbm.xml**

```xml
	<class name="User" table="t_user">
		<id name="id" column="userId">
			<generator class="native"></generator>
		</id>

		<property name="name" column="userName"></property>

		<one-to-one name="address" class="com.vole.model.Address" cascade="all"></one-to-one>
	</class>
```

* **Address.hbm.xml**

```xml
<class name="Address" table="t_address">
		<id name="id" column="addresId">
			<generator class="foreign">
				<param name="property">user</param>
			</generator>
		</id>

		<property name="address" column="address"></property>
		<property name="zipcode" column="zipcode"></property>

		<one-to-one name="user" class="com.vole.model.User" constrained="true"></one-to-one>
	</class>
```

* **Test**

```java
	@Test
	public void testSave() {
		User user = new User();
		user.setName("vole");
		
		Address address = new Address();
		address.setAddress("beijing");
        address.setZipcode("45678");
		address.setUser(user);
		
		user.setAddress(address);
		
		session.save(user);
	}
```





###### 9.1.2 按照外键映射（一个主键关联一个外键）

**与上面的区别主要在于配置文件**

* **User2.hbm.xml**

```xml
<class name="User2" table="t_user2">
		<id name="id" column="userId">
			<generator class="native"></generator>
		</id>

		<property name="name" column="userName"></property>

		<many-to-one name="address" class="com.vole.model.Address2"
			cascade="all" column="addressId" unique="true"></many-to-one>
	</class>
```

* **Address2.hbm.xml**

```xml
<class name="Address2" table="t_address2">
		<id name="id" column="addresId">
			<generator class="native">
			</generator>
		</id>

		<property name="address" column="address"></property>
		<property name="zipcode" column="zipcode"></property>

		<one-to-one name="user" class="com.vole.model.User2"
			property-ref="address"></one-to-one>
	</class>
```

##### 9.2



#### 10.Hibernate 查询方式 

##### 10.1 HQL（Hibernate Query Language）是面向对象的查询语言；是使用最广的一种查询方式

* **普通查询**

```java
	@Test
	public void testHQLQuery3() {
		String hql = "from Student";
		Query query = session.createQuery(hql);
		List<Student> list = (List<Student>) query.list();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Student s = (Student) it.next();
			System.out.println(s);
		}
	}
```

* **带条件查询(包含链式写法、使用别名)**

```java
	@Test
	public void testHQLQuery4() {
		String hql = "from Student where name like :stuName and age=:stuAge";
		// String hql = "from Student as s where s.name like :stuName and s.age=:stuAge"; 使用别名
		Query query = session.createQuery(hql);
		/*
		 * query.setString("stuName", "v%"); query.setInteger("stuAge", 15);
		 * List<Student> list = (List<Student>) query.list();
		 */
		List<Student> list = (List<Student>) query
          					.setString("stuName", "v%")
          					.setInteger("stuAge", 15)
          					.list();// 链式写法
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Student s = (Student) it.next();
			System.out.println(s);
		}
	}
```

* **对结果排序**

```java
	@Test
	public void testHQLQuery5() {
		String hql = "from Student order by age desc";// 有 des：从大到小，没 des 从小到大
		Query query = session.createQuery(hql);
		List<Student> list = (List<Student>) query.list();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Student s = (Student) it.next();
			System.out.println(s);
		}
	}
```

* **分页查询**

```java
	@Test
	public void testHQLQuery6() {
		String hql = "from Student";
		Query query = session.createQuery(hql);
		query.setFirstResult(1);//起始数据
		query.setMaxResults(2);//多少数据
		List<Student> list = (List<Student>) query.list();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Student s = (Student) it.next();
			System.out.println(s);
		}
	}
```

* **查询单个对象**

```java
	@Test
	public void testHQLQuery7() {
		String hql = "from Student";
		Query query = session.createQuery(hql);
		query.setFirstResult(1);
		query.setMaxResults(1);
		Student s = (Student) query.uniqueResult();
		System.out.println(s);
	}
}
```



##### 10.2 QBC 查询方式(Query By Criteria)是用一套接口来实现的查询方式

```java
	// 普通查询
	@Test
	public void testQBcQuery1() {
		Criteria criteria = session.createCriteria(Student.class);
		List<Student> list = (List<Student>) criteria.list();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Student s = (Student) it.next();
			System.out.println(s);
		}
	}

	// 带条件查询
	@Test
	public void testQBcQuery2() {
		Criteria criteria = session.createCriteria(Student.class);
		Criterion c1 = Restrictions.like("name", "v%");
		Criterion c2 = Restrictions.eq("age", 15);
		criteria.add(c1);
		criteria.add(c2);
		List<Student> list = (List<Student>) criteria.list();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Student s = (Student) it.next();
			System.out.println(s);
		}
	}

	// 對結果查询
	@Test
	public void testQBcQuery3() {
		Criteria criteria = session.createCriteria(Student.class);
		criteria.addOrder(Order.desc("age"));
		List<Student> list = (List<Student>) criteria.list();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Student s = (Student) it.next();
			System.out.println(s);
		}
	}

	// 分页查询
	@Test
	public void testQBcQuery4() {
		Criteria criteria = session.createCriteria(Student.class);
		/*
		 * criteria.setFirstResult(0); criteria.setMaxResults(2); 
		 * List<Student> list = (List<Student>) criteria.list();
		 */
		criteria.setFirstResult(0);
		criteria.setMaxResults(2);
		List<Student> list = (List<Student>)criteria
          		.setFirstResult(0)
          		.setMaxResults(2)
          		.list();// 链式
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Student s = (Student) it.next();
			System.out.println(s);
		}
	}

	// 单个查询
	@Test
	public void testQBcQuery5() {
		Criteria criteria = session.createCriteria(Student.class);
		criteria.setFirstResult(1);
		criteria.setMaxResults(1);
		Student s = (Student) criteria.uniqueResult();
		System.out.println(s);

	}
}
```



#### 11.Hibernate  高级配置

##### 11.1配置数据库连接池

> 反问数据库，需要不断的创建和释放连接，假如访问量大的话，效率比较低级，服务器消耗大；使用数据库连接池，我们可以根据实际项目的情况，定义连接池的连接个数，从而可以实现从连接池获取连接，用户放回到连接池。从而有效的提高的系统的执行效率；Hibernate 自带的连接池不是很好，有 bug；推荐使用 C3P0,proxool 等
>
> ```markdown
> * C3P0 使用
> 1. 导入相关 jar 包 （c3p0-0.9.2.1.jar，hibernate-c3p0-4.3.5.Final.jar，mchange-commons-java-0.2.3.4.jar。缺一不可）
> 2. 在 hibernate.cfg.xml 配置属性
>  		<!-- 最小连接数 -->
> 		<property name="c3p0.min_size">7</property>
> 		<!-- 最大连接数 -->
> 		<property name="c3p0.max_size">42</property>
> 		<!-- 获得连接的超时时间,如果超过这个时间,会抛出异常，单位毫秒 -->
> 		<property name="c3p0.timeout">1800</property>
> 		<!-- 最大的PreparedStatement的数量 -->
> 		<property name="c3p0.max_statements">50</property>
> 以上是基本属性，如有要求，可去官网获取，
> ```

##### 11.2配置日志框架 Log4J

> Log4J 作为一个开源的优秀日志框架，被广泛使用，Hibernate4 必须包中直接支持 Log4J 日志框架；我们只需
> 要引入 Log4j jar 包，即可使用

```markdown
1. 导入 jar 包 （log4j-1.2.17.jar）
2. 配置 log4j.properties 文件 ，在 src 目录xia
> 	log4j.rootLogger=info,appender1,appender2
  	log4j.appender.appender1=org.apache.log4j.ConsoleAppender 
	log4j.appender.appender2=org.apache.log4j.FileAppender 
	log4j.appender.appender2.File=C:/logFile.txt
	log4j.appender.appender1.layout=org.apache.log4j.TTCCLayout
	log4j.appender.appender2.layout=org.apache.log4j.TTCCLayout 
	以上相关配置信息可以百度得到
3. 创建 Logger 对象
public class StudentTest {

	private SessionFactory sessionFactory=HibernateUtil.getSessionFactory();
	private Session session;
	private Logger logger=Logger.getLogger(StudentTest.class);
	
	@Before
	public void setUp() throws Exception {
		session=sessionFactory.openSession(); // 生成一个session
	    session.beginTransaction(); // 开启事务
	}

	@After
	public void tearDown() throws Exception {
		 session.getTransaction().commit(); // 提交事务
		 session.close(); // 关闭session
	}

	@Test
	public void testSQLQuery() {
		String sql="select * from t_student";
		Query query=session.createSQLQuery(sql).addEntity(Student.class);
		List studentList=query.list();
		Iterator it=studentList.iterator();
		while(it.hasNext()){
			Student s=(Student)it.next();
			System.out.println(s);
		}	
		logger.debug("这是一个debug信息");
		logger.info("这是一个info信息");
		logger.error("这是一个错误信息");
	}
}
```



##### 11.3配置 Hibernate  二级缓存

**11.3.1缓存的概念：**

**缓存是介于物理数据源与应用程序之间，是对数据库中的数据复制一份临时放在内存或者硬盘中的容器，其作用是为了减少应用程序对物理数据源访问的次数，从而提高了应用程序的运行性能。Hibernate 在进行读取数据的时候，根据缓存机制在相应的缓存中查询，如果在缓存中找到了需要的数据(我们把这称做“缓存命 中")，则就直接把命中的数据作为结果加以利用，避免了大量发送 SQL 语句到数据库查询的性能损耗。**

**11.3.2 Hibernate 缓存的分类：**

1. Session 缓存（又称作事务缓存）：

   Hibernate 内置的，不能卸除。缓存范围：缓存只能被当前 Session 对象访问。缓存的生命周期依赖于 Session 的生命周期，当 Session 被关闭后，缓存也就结束生命周期。


2. SessionFactory 缓存（又称作应用缓存）：

   使用第三方插件，可插拔。缓存范围：缓存被应用范围内的所有 session 共享,不同的 Session 可以共享。这些 session 有可能是并发访问缓存，因此必须对缓存进行更新。缓存的生命周期依赖于应用的生命周期，应用结束时，缓存也就结束了生命周期，二级缓存存在于应用程序范围。

3. 二级缓存策略提供商：

   提供了 HashTable 缓存，EHCache，OSCache，SwarmCache，jBoss Cathe2，这些缓存机制，其中 EHCache，OSCache 是不能用于集群环境（Cluster Safe）的，而 SwarmCache，jBoss Cathe2 是可以的。HashTable 缓存主要是用来测试的，只能把对象放在内存中，EHCache，OSCache 可以把对象放在内存（memory）中，也可以把对象放在硬盘（disk）上（为什么放到硬盘上？上面解释了）。

4. 什么数据适合放二级缓存中：

   （1）经常被访问

   （2）改动不大

   （3）数量有限

   （4）不是很重要的数据，允许出现偶尔并发的数据。比如组织机构代码，列表信息等；

5. 配置 EHCache 二级缓存；

 ```markdown
1. 导入相关 jar 包（ehcache-core-2.4.3.jar、hibernate-ehcache-4.3.5.Final.jar、slf4j-api-1.6.1.jar）

2. 配置 echcahe 文件
	<ehcache>
   
   	<!-- 指定一个文件目录，当EHCache把数据写到硬盘上时，将把数据写到这个目录下 -->
    <diskStore path="c:\\ehcache"/>
    
    <!--  
    	设置缓存的默认数据过期策略 
    -->    
    <defaultCache
        maxElementsInMemory="10000"
        eternal="false"
        timeToIdleSeconds="120"
        timeToLiveSeconds="120"
        overflowToDisk="true"
        />

	<!-- 
		name 设置缓存的名字，他的取值为类的完整名字或者类的集合的名字；
		maxElementsInMemory 设置基于内存的缓存可存放的对象的最大数目
		eternal 如果为true，表示对象永远不会过期，此时会忽略timeToIdleSeconds和timeToLiveSeconds，默认为false;
		timeToIdleSeconds 设定允许对象处于空闲状态的最长时间，以秒为单位；
		timeToLiveSeconds 设定对象允许存在于缓存中的最长时间，以秒为单位；
		overflowToDisk 如果为true,表示当基于内存的缓存中的对象数目达到maxElementsInMemory界限，会把溢出的对象写到基于硬盘的缓存中；
	 -->


	<!-- 设定具体的第二级缓存的数据过期策略 -->
    <cache name="com.java1234.model.Class"
        maxElementsInMemory="1"
        eternal="false"
        timeToIdleSeconds="300"
        timeToLiveSeconds="600"
        overflowToDisk="true"
        />
 

</ehcache>


3.在 hibernat.cfg.xml 加入配置信息
		<!-- 启用二级缓存 -->
		<property name="cache.use_second_level_cache">true</property>
		<!-- 配置使用的二级缓存的产品 -->
		<property name="hibernate.cache.region.factory_class">org.hibernate.cache.ehcache.EhCacheRegionFactory</property>
		<!-- 配置启用查询缓存 -->
		<property name="cache.use_query_cache">true</property>

4.在相关的 model 中加入配置信息
	<cache usage="read-only"/>
 ```

