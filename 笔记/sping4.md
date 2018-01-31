# Sping4 

#### 一 、Spring  简介

>  Spring 作者：Rod Johnson；官方网站：http://spring.io/ 最新开发包及文档下载地址：http://repo.springsource.org/libs-release-local/org/springframework/spring/ 核心思想：**IOC 控制反转**；**AOP 面向切面**；介绍：百度百科

##### 1.1简单使用 

1. 导入相关核心 jar 包

![sping4_jar](E:\资料\java\JAVAWEB\笔记\sping4_jar.png)



2. 创建一个 HelloWorld.java 

```java
public class HelloWorld {
	public void say(){
		System.out.println("Hello World");
	}
}
```

3. 创建 beans.xml 

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd">

	<bean id="helloWorld" class="com.vole.test.HelloWorld" ></bean>
  
</beans>
```

4. 测试

```java
public class Test {

	public static void main(String[] args) {
		 ApplicationContext ac =new ClassPathXmlApplicationContext("beans.xml");// 得到 beans.xml 
		 HelloWorld helloWorld = (HelloWorld)ac.getBean("helloWorld"); //根据 id 得到 HelloWorld 类
		 helloWorld.say();
	}
}
```



### 二、ICO 

##### 2.1简介 

IOC（控制反转：Inverse of Control ），又称作 依赖注入，是一种重要的面向对象编程的法则来削减计算机程
序的耦合问题，也是轻量级的 Spring 框架的核心。

控制反转——Spring通过一种称作控制反转（IoC）的技术促进了松耦合。当应用了IoC，一个对象依赖的其它对象会通过被动的方式传递进来，而不是这个对象自己创建或者查找依赖对象。你可以认为IoC与JNDI相反——不是对象从容器中查找依赖，而是容器在对象初始化时不等对象请求就主动将依赖传递给它。

##### 2.2实例

* 创建一个 Tester 接口

```java
public interface Tester {
	void test();
}
```

* Vole 与 Kong 两个人 分别实现 Tester

```java
public class Vole implements Tester {

	@Override
	public void test() {
		System.out.println("vole--working");
	}
}


public class Kong implements Tester{

	@Override
	public void test() {
		System.out.println("kong--working");
	}
}
```

* 创建一个 JavaWork 工作类

```java
public class JavaWork {
	
	private Tester tester;

	public void setTester(Tester tester) {
		this.tester = tester;
	}
	
	public void doTest() {
		tester.test();
	}

}
```

* 把 Vole、Kong、JavaWork 装配到 beans.xml 中

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd">

	<bean id="vole" class="com.vole.bean.Vole" ></bean>
	
	<bean id="kong" class="com.vole.bean.Kong" ></bean>
	
	<bean id="javaWork" class="com.vole.bean.JavaWork" >
		<property name="tester" ref="kong"></property> <--! ref：调用值，可变 -->
	</bean>  
  
</beans>
```

* 测试

```java
public class Test {

	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext("beans.xml");
		JavaWork javaWork = (JavaWork)ac.getBean("javaWork");
		javaWork.doTest();
	}
}

//----kong--working
```



##### 2.3 依赖注入

> 1，属性注入；
> 2，构造函数注入；(通过类型；通过索引；联合使用)
> 3，工厂方法注入；(非静态工厂，静态工厂)
> 4，泛型依赖注入；(Spring4 整合 Hibernate4 的时候在说)

* 创建 Peopel 类

```java
public class People {
	private int id;
	private String name;
	private int age;

	public People() {
		super();
		// TODO Auto-generated constructor stub
	}

	public People(int id, String name, int age) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "People [id=" + id + ", name=" + name + ", age=" + age + "]";
	}
}
```

* 创建 people.xml 

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd">

	<bean id="people1" class="com.vole.entity.People">
		<property name="id" value="1"></property>
		<property name="name" value="伍一"></property>
		<property name="age" value="22"></property>
	</bean>

	<bean id="people2" class="com.vole.entity.People">
		<constructor-arg type="int" value="2"></constructor-arg>
		<constructor-arg type="String" value="伍二"></constructor-arg>
		<constructor-arg type="int" value="23"></constructor-arg>
	</bean>

	<bean id="people3" class="com.vole.entity.People">
		<constructor-arg index="0" value="3"></constructor-arg>
		<constructor-arg index="1" value="伍三"></constructor-arg>
		<constructor-arg index="2" value="24"></constructor-arg>
	</bean>

	<bean id="people4" class="com.vole.entity.People">
		<constructor-arg index="0" type="int" value="4"></constructor-arg>
		<constructor-arg index="1" type="String" value="伍四"></constructor-arg>
		<constructor-arg index="2" type="int" value="25"></constructor-arg>
	</bean>

	<bean id="peopleFactory" class="com.vole.factory.PeopleFactory"></bean>

	<bean id="people5" factory-bean="peopleFactory" factory-method="createPeople"></bean>

	<bean id="people6" class="com.vole.factory.PeopleFactory2" factory-method="createPeople"></bean>

</beans>
```

* 创建非静态工厂和静态工厂

```java
/*
 * 非静态工厂
 */
public class PeopleFactory {
	
	public People createPeople(){
		People people = new People();
		people.setId(5);
		people.setName("伍伍");
		people.setAge(26);
		return people;
	}
}

/*
 * 静态工厂
 */
public class PeopleFactory2 {
	
	public static People createPeople(){
		People people = new People();
		people.setId(6);
		people.setName("伍六");
		people.setAge(27);
		return people;
	}
}
```

* 测试 

```java
public class Test3 {

	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext("people.xml");

		// 属性注入
		People people1 = (People) ac.getBean("people1");
		System.out.println(people1);

		// 通过类型注入
		People people2 = (People) ac.getBean("people2");
		System.out.println(people2);

		// 通过索引注入
		People people3 = (People) ac.getBean("people3");
		System.out.println(people3);

		// 联合注入
		People people4 = (People) ac.getBean("people4");
		System.out.println(people4);

		// 工厂方法注入--非静态工厂
		People people5 = (People) ac.getBean("people5");
		System.out.println(people5);
		
		// 工厂方法注入--静态工厂
		People people6 = (People) ac.getBean("people6");
		System.out.println(people6);
	}
}
```

##### 2.4注入参数

> 1，基本类型值；
> 2，注入 bean；
> 3，内部 bean；
> 4，null 值；
> 5，级联属性；
> 6，集合类型属性；

* 创建 People 类与 Dog 类

```java
public class People {
	private int id;
	private String name;
	private int age;
	//private Dog dog =new Dog(); // 级联 注入是必须 new 一个实例，不然会报空指针异常
	private Dog dog;
	private List<String> hobbies = new ArrayList<>();
	private Set<String> loves = new HashSet<>();
	private Map<String, String> works=new HashMap<String, String>();
	private Properties pops;//一般用于系统配置
  ....
  ....
}


public class Dog {
	private String name;
	....
}
```

* 创建 people2.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd">

	<bean id="people1" class="com.vole.entity.People">
		<property name="id" value="1"></property>
		<property name="name" value="伍一"></property>
		<property name="age" value="22"></property>
	</bean>

	<bean id="dog1" class="com.vole.entity.Dog">
		<property name="name" value="jack"></property>
	</bean>

	<bean id="people2" class="com.vole.entity.People">
		<property name="id" value="1"></property>
		<property name="name" value="伍一"></property>
		<property name="age" value="22"></property>
		<property name="dog" ref="dog1"></property>
	</bean>

	<bean id="people3" class="com.vole.entity.People">
		<property name="id" value="1"></property>
		<property name="name" value="伍一"></property>
		<property name="age" value="22"></property>
		<property name="dog">
			<bean class="com.vole.entity.Dog">
				<property name="name" value="tom"></property>
			</bean>
		</property>
	</bean>

	<bean id="people4" class="com.vole.entity.People">
		<property name="id" value="1"></property>
		<property name="name" value="伍一"></property>
		<property name="age" value="22"></property>
		<property name="dog">
			<null></null>
		</property>
	</bean>

	<!-- <bean id="people5" class="com.vole.entity.People"> <property name="id" 
		value="1"></property> <property name="name" value="伍一"></property> <property 
		name="age" value="22"></property> <property name="dog.name" value="json"></property> 
		</bean> -->

	<bean id="people6" class="com.vole.entity.People">
		<property name="id" value="1"></property>
		<property name="name" value="伍一"></property>
		<property name="age" value="22"></property>
		<property name="dog" ref="dog1"></property>
		<property name="hobbies">
			<list>
				<value>sing</value>
				<value>song</value>
			</list>
		</property>
		<property name="loves">
			<set>
				<value>sing1</value>
				<value>song1</value>
			</set>
		</property>
		<property name="works">
			<map>
				<entry>
					<key><value>上午</value></key>
					<value>写代码</value>
				</entry>
				<entry>
					<key><value>下午</value></key>
					<value>TEst 代码</value>
				</entry>
			</map>
		</property>
		<property name="pops">
			<props>
				<prop key="address1">aaa</prop>
				<prop key="address2">bbb</prop>
			</props>
		</property>
	</bean>

</beans>
```

* Test

```java
public class Test4 {
	private ApplicationContext ac;
  
	@Before
	public void setUp() throws Exception {
		ac = new ClassPathXmlApplicationContext("people2.xml");
	}

	@After
	public void tearDown() throws Exception {
	}

	// 基本类型值
	@Test
	public void Test1() {
		People p = (People) ac.getBean("people1");
		System.out.println(p);
	}

	// 注入 bean
	@Test
	public void Test2() {
		People p = (People) ac.getBean("people2");
		System.out.println(p);
	}

	// 内部 bean
	@Test
	public void Test3() {
		People p = (People) ac.getBean("people3");
		System.out.println(p);
	}

	// null 值
	@Test
	public void Test4() {
		People p = (People) ac.getBean("people4");
		System.out.println(p);
	}

	// 级联属性
	@Test
	public void Test5() {
		People p = (People) ac.getBean("people5");
		System.out.println(p);
	}

	// 集合类型属
	@Test
	public void Test6() {
		People p = (People) ac.getBean("people6");
		System.out.println(p);
	}
}
```

##### 2.5 Spring 自动装配(不常用，了解即可)

> 通过配置 default-autowire 属性，Spring IOC 容器可以自动为程序注入 bean；默认是 no，不启用自动装配；
> default-autowire 的类型有 byName,byType,constructor；
> byName：通过名称进行自动匹配；
> byType：根据类型进行自动匹配；
> constructor：和 byType 类似，只不过它是根据构造方法注入而言的，根据类型，自动注入；
> 建议：自动装配机制慎用，它屏蔽了装配细节，容易产生潜在的错误；

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd"
	default-autowire="constructor"><!-- byName、byType  -->

	<!--<bean id="dog" class="com.vole.entity.Dog"> 
			<property name="name" value="jack"></property> 
		</bean> -->

	<bean id="dog1" class="com.vole.entity.Dog">
		<property name="name" value="tom"></property>
	</bean>

	<bean id="people2" class="com.vole.entity.People">
		<property name="id" value="1"></property>
		<property name="name" value="伍一"></property>
		<property name="age" value="22"></property>
	</bean>

</beans>
```

```java
public class People {
	private int id;
	private String name;
	private int age;
	private Dog dog;
  
	public People(Dog dog) {
		super();
		System.out.println("constructor");
		this.dog = dog;
	}
  ....
}
```







##### 2.6 方法注入（不常用，了解即可）

> Spring bean 作用域默认是 单例 singleton； 可以通过配置 prototype ，实现多例；
> 方法注入 lookup-method

实现每次都调用一个新的 Dog

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd">
  
	<bean id="dog1" class="com.vole.entity.Dog" scope="prototype">
		<property name="name" value="tom"></property>
	</bean>

	<bean id="people2" class="com.vole.entity.People">
		<property name="id" value="1"></property>
		<property name="name" value="伍一"></property>
		<property name="age" value="22"></property>
		<lookup-method name="getDog" bean="dog1"/>
	</bean>

</beans>
```

```java
public abstract class People {
	private int id;
	private String name;
	private int age;
	private Dog dog;
	
	public People() {
		super();
	}

	public abstract Dog getDog() ;
  ...
}
```

```java
@Test
	public void Test1() {
		People p = (People) ac.getBean("people2");
		People p2 = (People) ac.getBean("people2");
		System.out.println(p.getDog()==p2.getDog());//false
		System.out.println(p);
		
		System.out.println(ac.getBean("dog1")==ac.getBean("dog1"));//false
	}
```

##### 2.7 方法替换（即动态替换）了解就行