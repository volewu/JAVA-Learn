## WebService 之  CXF

[TOC]

[1.2 CXF 简介](#1.2 CXF 简介 )

#### 一、简介 

##### 1.1 WebService 简介

>Webservice 是一种跨平台，跨语言的规范，用于不同平台，不同语言开发的应用之间的交互。
>
>这里具体举个例子，比如在 Windows Server 服务器上有个 C#.Net 开发的应用 A，在 Linux 上有个 Java 语言开发的应用 B，B 应用要调用A应用，或者是互相调用。用于查看对方的业务数据。
>
>再举个例子，天气预报接口。无数的应用需要获取天气预报信息；这些应用可能是各种平台，各种技术实现；而气象局的项目，估计也就一两种，要对外提供天气预报信息，这个时候，如何解决呢？
>
>Webservice就是出于以上类似需求而定义出来的规范；
>
>开发人员一般就是在具体平台开发webservice接口，以及调用webservice接口；每种开发语言都有自己的webservice实现框架。比如Java 就有 Apache Axis1、Apache Axis2、Codehaus XFire、**Apache CXF**、Apache Wink、Jboss  RESTEasyd等等...

![Webservice](https://raw.githubusercontent.com/volewu/JAVA-Learn/master/%E7%AC%94%E8%AE%B0/WebService_CXF/photo/Webservice.jpg)

##### 1.2 CXF 简介 

> [Apache CXF](www.cxf.apache.org) 是 Apache 旗下的开源 Service 框架， 利用 Frontend 编程 API 来构建和开发 Service。



#### 二、使用 CXF 开发应用

##### 2.1 使用 CXF  开发 WebService  服务器端接口

* eg：使用 Maven 创建项目案例，在 pom.xml 中导入相关 jar

```xml
		<dependency>
			<groupId>org.apache.cxf</groupId>
			<artifactId>cxf-core</artifactId>
			<version>3.1.5</version>
		</dependency>
	
		<dependency>
			<groupId>org.apache.cxf</groupId>
			<artifactId>cxf-rt-frontend-jaxws</artifactId>
			<version>3.1.5</version>
		</dependency>
	
		<dependency>
			<groupId>org.apache.cxf</groupId>
			<artifactId>cxf-rt-transports-http-jetty</artifactId>
			<version>3.1.5</version>
		</dependency>
```

* 创建 HelloWorld.java 接口

```java
import javax.jws.WebService;

@WebService
public interface HelloWorld {

	public String say(String str);
}
```

* 创建 HelloWorldImpl.java 实现类

```java
@WebService
public class HelloWorldImpl implements HelloWorld {

	public String say(String str) {
		return "Hello" + str;
	}

}
```

* 创建服务器 Server.java ,然后在浏览器中输入相关的地址即可

```java
public class Server {
	public static void main(String[] args) {
		System.out.println("start");
		HelloWorld implementor = new HelloWorldImpl();
		String address = "http://10.132.45.225/helloWorld";
		// jdk实现 暴露webservice接口 :http://10.132.45.225/helloWorld?wsdl
		// Endpoint.publish(address, implementor);
		JaxWsServerFactoryBean factoryBean = new JaxWsServerFactoryBean();
		factoryBean.setAddress(address);// 设置暴露地址
		factoryBean.setServiceClass(HelloWorld.class);// 接口类
		factoryBean.setServiceBean(implementor); // 设置实现类
		factoryBean.create();// 创建webservice接口
		System.out.println("started");
	}
}
```

##### 2.2 使用 CXF  开发 WebService 

> 这里开发 Client 主要依靠 **[apache-cxf-3.1.5](http://cxf.apache.org/download.html)** 工具，需要在 Path  下配置 bin 目录的环境变量

* 上面的 Server 不变，然后创建 WB_Client 客户端，用 cmd 进入 该项目的代码地址，然后利用 apache-cxf-3.1.5 工具输入指令，动态生成代码

![WB_cmd](https://github.com/volewu/JAVA-Learn/blob/master/%E7%AC%94%E8%AE%B0/WebService_CXF/photo/WB_cmd.PNG?raw=true)

* 生成的代码目录

![WB_code](https://github.com/volewu/JAVA-Learn/blob/master/%E7%AC%94%E8%AE%B0/WebService_CXF/photo/WB_code.PNG?raw=true)

* 创建 Client.java 

```java
public class Client {
	
	public static void main(String[] args) {
		HelloWorldService service = new HelloWorldService();
		HelloWorld helloWorld = service.getHelloWorldPort();
		System.out.println(helloWorld.say("Gakki"));
	}

}
// Hello: Gakki
```



##### 2.3 CXF  处理 JavaBean 以及复合类型

* 在上面的基础上 在 WS_Server 中创建 User & Role 实体

```java
public class User {

	private Integer id;
	private String userName;
	private String password;
   ...
   
}

public class Role {

	private Integer id;
	private String roleName;
	
	public Role(Integer id, String roleName) {
		super();
		this.id = id;
		this.roleName = roleName;
	}
 .... 
}
```

* 在 HelloWorld 接口中创建根据 User 获取 role 方法

```java
@WebService
public interface HelloWorld {

	public String say(String str);
	
	public List<Role> getRoleByUser(User user);
}
```

* 实现接口类 HelloWorldImpl 方法重写

```java
@WebService
public class HelloWorldImpl implements HelloWorld {

	public String say(String str) {
		return "Hello: " + str;
	}

	public List<Role> getRoleByUser(User user) {
		List<Role> roles = new ArrayList<Role>();
		// 模拟写死
		if (user != null) {
			if(user.getUserName().equals("gakki")&&user.getPassword().equals("123456")){
				roles.add(new Role(1, "wife"));
				roles.add(new Role(2, "lover"));
			}else if (user.getUserName().equals("vole")&&user.getPassword().equals("123456")) {
				roles.add(new Role(3, "程序员"));
			}
			return roles;
		} else
			return null;
	}

}
```

* Server 就这样写好了，然后根据 apache-cxf 工具在 Client 中生成动态代码，然后 Client 实现

```java
public class Client {
	
	public static void main(String[] args) {
		HelloWorldService service = new HelloWorldService();
		HelloWorld helloWorld = service.getHelloWorldPort();
		System.out.println(helloWorld.say("Gakki"));
		
		User user = new User();
		user.setUserName("gakki");
		user.setPassword("123456");
		List<Role> roles =  helloWorld.getRoleByUser(user);
		for (Role role : roles) {
			System.out.println(role.getId()+","+role.getRoleName());
		}
	}

}

// Hello: Gakki
// 1,wife
// 2,lover
```

##### 2.4 CXF  处理一些 Map 等复杂类型

> CXF 是无法直接处理 Map 等复杂类型的，它需要适配转换成其他类型就可以使用了 

* 还是同一个案例，在这基础上 Server 中添加 **<获取用户相对应的所有角色>**  功能，在 HelloWorld 接口中添加该功能

```java
@WebService
public interface HelloWorld {

	public String say(String str);
	
	public List<Role> getRoleByUser(User user);
	
	/**
	 * 获取用户相对应的所有角色,无法直接处理，需要添加注释并适配
	 * @return
	 */
	@XmlJavaTypeAdapter(MapAdapter.class)
	public Map<String, List<Role>> getRole();
}
```

* 创建 MapAdapter.java 适配器

```java
public class MapAdapter extends XmlAdapter<MyRole[], Map<String, List<Role>>> {

	/**
	 * 适配转换 MyRole[] -> Map<String, List<Role>>
	 */
	@Override
	public Map<String, List<Role>> unmarshal(MyRole[] v) throws Exception {
		Map<String, List<Role>> map = new HashMap<String, List<Role>>();
		for (int i = 0; i < v.length; i++) {
			MyRole r = v[i];
			map.put(r.getKey(), r.getValue());
		}
		return map;
	}

	/**
	 * 适配转换 Map<String, List<Role>> -> MyRole[]
	 */
	@Override
	public MyRole[] marshal(Map<String, List<Role>> v) throws Exception {
		MyRole[] myRoles = new MyRole[v.size()];
		int i = 0;
		for (String key : v.keySet()) {
			myRoles[i] = new MyRole();
			myRoles[i].setKey(key);
			myRoles[i].setValue(v.get(key));
			i++;
		}
		return myRoles;
	}

}
```

* 创建自定义实体（cxf 能接受） MyRole.java

```java
public class MyRole {

	private String key;
	private List<Role> value;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<Role> getValue() {
		return value;
	}

	public void setValue(List<Role> value) {
		this.value = value;
	}

}
```

* 在 HelloWorldImpl.java 中实现该功能

```java
@WebService
public class HelloWorldImpl implements HelloWorld {

   .......

	public Map<String, List<Role>> getRole() {
		Map<String, List<Role>> map = new HashMap<String, List<Role>>();
		List<Role> roleList1 = new ArrayList<Role>();
		roleList1.add(new Role(1, "wife"));
		roleList1.add(new Role(2, "lover"));
		map.put("gakki", roleList1);
		
		List<Role> roleList2 = new ArrayList<Role>();
		roleList2.add(new Role(3, "程序员"));
		map.put("vole", roleList2);
		
		return map;
	}

}
```

* 根据 apache-cxf 工具在 Client 中生成动态代码，然后 Client 实现

```java
public class Client {

	public static void main(String[] args) {
		HelloWorldService service = new HelloWorldService();
		HelloWorld helloWorld = service.getHelloWorldPort();
		System.out.println(helloWorld.say("Gakki"));

		/*
		 * User user = new User(); user.setUserName("gakki");
		 * user.setPassword("123456"); List<Role> roles =
		 * helloWorld.getRoleByUser(user); for (Role role : roles) {
		 * System.out.println(role.getId() + "," + role.getRoleName()); }
		 */

		MyRoleArray array = helloWorld.getRole();
		List<MyRole> roleList = array.item;
		for (int i = 0; i < roleList.size(); i++) {
			MyRole my = roleList.get(i);
			System.out.print(my.key + ": ");
			for (Role r : my.value) {
				System.out.print(r.getId() + "," + r.getRoleName());
			}
			System.out.println("-------");
		}
	}

}

/* Hello: Gakki
 * vole: 3,程序员-------
 * gakki: 1,wife2,lover-------
 */
```

##### 2.5 CXF  添加拦截器(自带的)

* Server 上的拦截器

```java
public class Server {
	public static void main(String[] args) {
		System.out.println("start");
		HelloWorld implementor = new HelloWorldImpl();
		String address = "http://10.132.45.225/helloWorld";
		// jdk实现 暴露webservice接口 :http://10.132.45.225/helloWorld?wsdl
		// Endpoint.publish(address, implementor);
		JaxWsServerFactoryBean factoryBean = new JaxWsServerFactoryBean();
		factoryBean.setAddress(address);// 设置暴露地址
		factoryBean.setServiceClass(HelloWorld.class);// 接口类
		factoryBean.setServiceBean(implementor); // 设置实现类
		factoryBean.getInInterceptors().add(new LoggingInInterceptor());// 添加 in 拦截器日志拦截器
		factoryBean.getOutInterceptors().add(new LoggingOutInterceptor());// 添加 out 拦截器日志拦截器

		factoryBean.create();// 创建webservice接口
		System.out.println("started");
	}
}

* 先进后出
```

* Client 上的拦截器

```java
public class Client {

	public static void main(String[] args) {
		HelloWorldService service = new HelloWorldService();
		HelloWorld helloWorld = service.getHelloWorldPort();
		org.apache.cxf.endpoint.Client client = ClientProxy.getClient(helloWorld);
		client.getInInterceptors().add(new LoggingInInterceptor()); // 添加In拦截器 日志拦截器
		client.getOutInterceptors().add(new LoggingOutInterceptor()); // 添加Out拦截器 日志拦截器

		System.out.println(helloWorld.say("Gakki"));
		/*
		 * User user = new User(); user.setUserName("gakki");
		 * user.setPassword("123456"); List<Role> roles =
		 * helloWorld.getRoleByUser(user); for (Role role : roles) {
		 * System.out.println(role.getId() + "," + role.getRoleName()); }
		 */

		MyRoleArray array = helloWorld.getRole();
		List<MyRole> roleList = array.item;
		for (int i = 0; i < roleList.size(); i++) {
			MyRole my = roleList.get(i);
			System.out.print(my.key + ": ");
			for (Role r : my.value) {
				System.out.print(r.getId() + "," + r.getRoleName());
			}
			System.out.println("-------");
		}
	}

}

//先进后出
```

##### 2.6 CXF  添加自定义拦截器

* 自定义拦截器是在一般用在权限认证上面，
* Server.java

```java
public class Server {
	public static void main(String[] args) {
		System.out.println("start");
		HelloWorld implementor = new HelloWorldImpl();
		String address = "http://10.132.45.225/helloWorld";
		// jdk实现 暴露webservice接口 :http://10.132.45.225/helloWorld?wsdl
		// Endpoint.publish(address, implementor);
		JaxWsServerFactoryBean factoryBean = new JaxWsServerFactoryBean();
		factoryBean.setAddress(address);// 设置暴露地址
		factoryBean.setServiceClass(HelloWorld.class);// 接口类
		factoryBean.setServiceBean(implementor); // 设置实现类
		factoryBean.getInInterceptors().add(new LoggingInInterceptor());// 添加 in 拦截器日志拦截器
		factoryBean.getOutInterceptors().add(new LoggingOutInterceptor());// 添加 out 拦截器日志拦截器
		//添加自定义拦截器
		factoryBean.getInInterceptors().add(new MyInInterceptor());
		
		factoryBean.create();// 创建webservice接口
		System.out.println("started");
	}
}
```

* MyInInterceptor.java

```java
import java.util.List;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class MyInInterceptor extends AbstractPhaseInterceptor<SoapMessage> {

	public MyInInterceptor() {
		super(Phase.PRE_INVOKE);// 在调用方法之前调用自定拦截器
	}

	@SuppressWarnings("null")
	public void handleMessage(SoapMessage message) throws Fault {
		List<Header> headers = message.getHeaders();
		if (headers == null && headers.size() == 0)
			throw new Fault(new IllegalArgumentException("没有Header，拦截器实施拦截"));
		Header firstHeader = headers.get(0);
		Element ele = (Element) firstHeader.getObject();
		NodeList uList = ele.getElementsByTagName("userName");
		NodeList pList = ele.getElementsByTagName("password");
		if (uList.getLength() != 1)
			throw new Fault(new IllegalArgumentException("用户名格式不对"));
		if (pList.getLength() != 1)
			throw new Fault(new IllegalArgumentException("密码格式不对"));
		String userName = uList.item(0).getTextContent();
		String password = pList.item(0).getTextContent();
		if (!userName.equals("gakki") || !password.equals("123456"))
			throw new Fault(new IllegalArgumentException("用户名或者密码错误！"));
	}

}
```

* Client.java

```java
public class Client {

	public static void main(String[] args) {
		HelloWorldService service = new HelloWorldService();
		HelloWorld helloWorld = service.getHelloWorldPort();
		org.apache.cxf.endpoint.Client client = ClientProxy.getClient(helloWorld);
		
		 // 添加自定义拦截器
		client.getOutInterceptors().add(new AddHeaderInterceptor("gakki","123456"));
		// 添加In拦截器 日志拦截器
		client.getInInterceptors().add(new LoggingInInterceptor()); 
		// 添加Out拦截器 日志拦截器
		client.getOutInterceptors().add(new LoggingOutInterceptor()); 
		
		// System.out.println(helloWorld.say("Gakki"));
		/*
		 * User user = new User(); user.setUserName("gakki");
		 * user.setPassword("123456"); List<Role> roles =
		 * helloWorld.getRoleByUser(user); for (Role role : roles) {
		 * System.out.println(role.getId() + "," + role.getRoleName()); }
		 */

		MyRoleArray array = helloWorld.getRole();
		List<MyRole> roleList = array.item;
		for (int i = 0; i < roleList.size(); i++) {
			MyRole my = roleList.get(i);
			System.out.print(my.key + ": ");
			for (Role r : my.value) {
				System.out.print(r.getId() + "," + r.getRoleName());
			}
			System.out.println("-------");
		}
	}

}
```

* AddHeaderInterceptor.java

```java
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class AddHeaderInterceptor extends AbstractPhaseInterceptor<SoapMessage> {
	
	private String userName;
	private String password;

	public AddHeaderInterceptor(String userName,String password) {
		super(Phase.PREPARE_SEND); // 准备发送SOAP消息的时候调用拦截器onstructor stub
		this.userName=userName;
		this.password=password;
	}

	public void handleMessage(SoapMessage message) throws Fault {
		List<Header> headers = message.getHeaders();
		
		Document doc=DOMUtils.createDocument();
		Element ele=doc.createElement("authHeader");
		Element uElement=doc.createElement("userName");
		uElement.setTextContent(userName);
		Element pElement=doc.createElement("password");
		pElement.setTextContent(password);
		
		ele.appendChild(uElement);
		ele.appendChild(pElement);
		
		headers.add(new Header(new QName("gakki"),ele));
		
	}

}
```



#### 三、Spring  整合 CXF

* 使用 Maven 创建 WB_Spring_CXF 项目

![WB_Spring_CXF](https://raw.githubusercontent.com/volewu/JAVA-Learn/master/%E7%AC%94%E8%AE%B0/WebService_CXF/photo/WB_Spring_CXF.PNG)

* 在 pom.xml 在导入相关 jar

```xml
		<!-- 添加Spring支持 -->
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-core</artifactId>
			<version>4.1.7.RELEASE</version>
		</dependency>

		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-beans</artifactId>
			<version>4.1.7.RELEASE</version>
		</dependency>

		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-tx</artifactId>
			<version>4.1.7.RELEASE</version>
		</dependency>

		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-context</artifactId>
			<version>4.1.7.RELEASE</version>
		</dependency>

		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-context-support</artifactId>
			<version>4.1.7.RELEASE</version>
		</dependency>

		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-web</artifactId>
			<version>4.1.7.RELEASE</version>
		</dependency>

		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-webmvc</artifactId>
			<version>4.1.7.RELEASE</version>
		</dependency>

		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-aop</artifactId>
			<version>4.1.7.RELEASE</version>
		</dependency>

		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-aspects</artifactId>
			<version>4.1.7.RELEASE</version>
		</dependency>

		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-jdbc</artifactId>
			<version>4.1.7.RELEASE</version>
		</dependency>

		<!-- 添加cxf支持 -->
		<dependency>
			<groupId>org.apache.cxf</groupId>
			<artifactId>cxf-core</artifactId>
			<version>3.1.5</version>
		</dependency>

		<dependency>
			<groupId>org.apache.cxf</groupId>
			<artifactId>cxf-rt-frontend-jaxws</artifactId>
			<version>3.1.5</version>
		</dependency>

		<dependency>
			<groupId>org.apache.cxf</groupId>
			<artifactId>cxf-rt-transports-http</artifactId>
			<version>3.1.5</version>
		</dependency>
```

* 添加 applicationContext.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>    
<beans xmlns="http://www.springframework.org/schema/beans"    
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"   
    xmlns:p="http://www.springframework.org/schema/p"  
    xmlns:aop="http://www.springframework.org/schema/aop"   
    xmlns:context="http://www.springframework.org/schema/context"  
    xmlns:jee="http://www.springframework.org/schema/jee"  
    xmlns:tx="http://www.springframework.org/schema/tx"  
    xmlns:jaxws="http://cxf.apache.org/jaxws"
    xsi:schemaLocation="    
        http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-4.0.xsd  
        http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.0.xsd  
        http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.0.xsd  
        http://www.springframework.org/schema/jee http://www.springframework.org/schema/jee/spring-jee-4.0.xsd  
        http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-4.0.xsd
        http://cxf.apache.org/jaxws http://cxf.apache.org/schemas/jaxws.xsd">    
        
	<import resource="classpath:META-INF/cxf/cxf.xml"/>
    <import resource="classpath:META-INF/cxf/cxf-servlet.xml"/>
    
    <!-- 自动扫描 -->
	<context:component-scan base-package="com.vole.webservice" />
	
	<!-- 定义服务提供者 -->
	<jaxws:endpoint implementor="#helloWorld" address="/HelloWorld">
		<!-- 添加in拦截器 -->
		<jaxws:inInterceptors>
			<bean class="org.apache.cxf.interceptor.LoggingInInterceptor" />
			<bean class="com.vole.interceptor.MyInterceptor" />
		</jaxws:inInterceptors>
		<!-- 添加out拦截器 -->
		<jaxws:outInterceptors>
			<bean class="org.apache.cxf.interceptor.LoggingInInterceptor" />
		</jaxws:outInterceptors>
	</jaxws:endpoint>
</beans>
```

* 添加 web.xml 

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance http://www.springmodules.org/schema/cache/springmodules-cache.xsd "
	xmlns="http://java.sun.com/xml/ns/javaee"
	xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"
	id="WebApp_ID" version="2.5">
	<display-name>WS_Spring_CXF</display-name>

	<welcome-file-list>
		<welcome-file>index.html</welcome-file>
		<welcome-file>index.htm</welcome-file>
		<welcome-file>index.jsp</welcome-file>
		<welcome-file>default.html</welcome-file>
		<welcome-file>default.htm</welcome-file>
		<welcome-file>default.jsp</welcome-file>
	</welcome-file-list>
	<!-- Spring配置文件 -->

	<context-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>classpath:applicationContext.xml</param-value>
	</context-param>
	<!-- Spring监听器 -->

	<listener>
		<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
	</listener>

	<servlet>
		<servlet-name>CXFServlet</servlet-name>
		<servlet-class>org.apache.cxf.transport.servlet.CXFServlet</servlet-class>
	</servlet>

	<servlet-mapping>
		<servlet-name>CXFServlet</servlet-name>
		<url-pattern>/webservice/*</url-pattern>
	</servlet-mapping>
</web-app>
```

* 运行---

![WB_Spring_CXF_run](https://raw.githubusercontent.com/volewu/JAVA-Learn/master/%E7%AC%94%E8%AE%B0/WebService_CXF/photo/WB_Spring_CXF_run.PNG)

* Client 启动时，需要重新用 wdls 工具动态生成代码。有些方法会改变

```java
		HelloWorldImplService service=new HelloWorldImplService();
		HelloWorld helloWorld=service.getHelloWorldImplPort();
		org.apache.cxf.endpoint.Client client=ClientProxy.getClient(helloWorld);
```

