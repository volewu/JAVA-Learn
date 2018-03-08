## 问候 Shiro（1.2.4） 大爷

#### 一、简介与简单实现

##### 1.1 Shiro  简介

> Apache Shiro是一个强大且易用的Java安全框架,执行身份验证、授权、密码学和会话管理。使用Shiro的易于理解的API,您可以快速、轻松地获得任何应用程序,从最小的移动应用程序到最大的网络和企业应用程序。
>
> Shiro 官方主页：http://shiro.apache.org/download.html

##### 1.2 Shiro HelloWorld 实现

* 本次项目都是采用 Maven 来实现的 ，下图好似项目结构

![Shiro_helloworld](https://raw.githubusercontent.com/volewu/JAVA-Learn/master/笔记/Shiro/photo/Shiro_helloworld.PNG)

* 在 pom.xml 中配置需要的 jar

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>com.vole.Shiro</groupId>
	<artifactId>Shiro01</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>Shiro01</name>
	<description>Shiro01</description>

	<dependencies>
		<dependency>
			<groupId>org.apache.shiro</groupId>
			<artifactId>shiro-core</artifactId>
			<version>1.2.4</version>
		</dependency>

		<dependency>
			<groupId>org.slf4j</groupId>
			<artifactId>slf4j-log4j12</artifactId>
			<version>1.7.12</version>
		</dependency>
	</dependencies>

</project>
```

* 配置 log4j.properties

```properties
log4j.rootLogger=INFO, stdout

log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%d %p [%c] - %m %n

# General Apache libraries
log4j.logger.org.apache=WARN

# Spring
log4j.logger.org.springframework=WARN

# Default Shiro logging
log4j.logger.org.apache.shiro=TRACE

# Disable verbose logging
log4j.logger.org.apache.shiro.util.ThreadContext=WARN
log4j.logger.org.apache.shiro.cache.ehcache.EhCache=WARN

```

* 创建 shiro.ini 

```ini
[users]
vole=123456
gakki=520
```

* 创建 HelloWorld.java

```java
public class HelloWorld {

	public static void main(String[] args) {
		// 读取配置文件，初始化 SecurityManager 仓库
		Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
		// 获取 SecurityManager 实例
		SecurityManager securityManager = factory.getInstance();
		// 把 SecurityManager 绑定到 SecurityUtils
		SecurityUtils.setSecurityManager(securityManager);
		// 得到当前执行的用户
		Subject currentUser = SecurityUtils.getSubject();
		// 创建 token 令牌，用户名/密码
		UsernamePasswordToken token = new UsernamePasswordToken("vole", "123456");
		try {
			currentUser.login(token);
			System.out.println("success");
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}
		currentUser.logout();
	}
}
```

#### 二、身份认证

##### 2.1 Subject 认证主体

>Subject 认证主体包含两个信息：
>Principals：身份，可以是用户名，邮件，手机号码等等，用来标识一个登录主体身份；
>Credentials：凭证，常见有密码，数字证书等等

##### 2.2 身份认证流程

![shiro_身份认证流程](https://raw.githubusercontent.com/volewu/JAVA-Learn/master/笔记/Shiro/photo/shiro_身份认证流程.PNG)

##### 2.3 Realm&JDBC Reaml

> Realm：意思是域，Shiro 从 Realm 中获取验证数据；
> Realm 有很多种类，例如常见的 jdbc realm，jndi realm，text realm。

* 现在来简单的实现  jdbc realm
* 在 pom.xml 中加入相关 jar

```xml
		<!-- 数据源 -->
		<dependency>
			<groupId>c3p0</groupId>
			<artifactId>c3p0</artifactId>
			<version>0.9.1.2</version>
		</dependency>

		<!-- 官方日志 -->
		<dependency>
			<groupId>commons-logging</groupId>
			<artifactId>commons-logging</artifactId>
			<version>1.2</version>
		</dependency>

		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<version>5.1.37</version>
		</dependency>

		<dependency>
			<groupId>junit</groupId>
			<artifactId>junit</artifactId>
			<version>4.12</version>
			<scope>test</scope>
		</dependency>
```

* 创建 jdbc_realm.ini ,

```ini
[main]
jdbcRealm=org.apache.shiro.realm.jdbc.JdbcRealm
dataSource=com.mchange.v2.c3p0.ComboPooledDataSource
dataSource.driverClass=com.mysql.jdbc.Driver
dataSource.jdbcUrl=jdbc:mysql://localhost:3306/db_shiro
dataSource.user=root
dataSource.password=123456
jdbcRealm.dataSource=$dataSource
securityManager.realms=$jdbcRealm
;此时的数据表和字段的名称都是 shiro 框架规定好的，表名与段名必须这么起，严重耦合
;CREATE TABLE `users` (
;  `id` int(11) NOT NULL AUTO_INCREMENT,
;  `userName` varchar(22) DEFAULT NULL,
;  `password` varchar(22) DEFAULT NULL,
;  PRIMARY KEY (`id`)
;) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
;
;/*Data for the table `users` */
;
;insert  into `users`(`id`,`userName`,`password`) values (1,'gakki','123456');
```

* 创建 JdbcRealmTest.java

```java
public class JdbcRealmTest {

	public static void main(String[] args) {
		// 读取配置文件，初始化 SecurityManager 仓库
		Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:jdbc_realm.ini");
		// 获取 SecurityManager 实例
		SecurityManager securityManager = factory.getInstance();
		// 把 SecurityManager 绑定到 SecurityUtils
		SecurityUtils.setSecurityManager(securityManager);
		// 得到当前执行的用户
		Subject currentUser = SecurityUtils.getSubject();
		// 创建 token 令牌，用户名/密码
		UsernamePasswordToken token = new UsernamePasswordToken("gakki", "123456");
		try {
			currentUser.login(token);
			System.out.println("身份认证成功");
		} catch (AuthenticationException e) {
			e.printStackTrace();
			System.out.println("身份认证失败");
		}
		currentUser.logout();
	}
}
```

#### 三、 权限认证(授权)

##### 3.1 权限认证核心要素

> **权限认证**：也就是访问控制，即在应用中控制谁能访问哪些资源。
>
> 在权限认证中，最核心的三个要素是：权限，角色和用户；
> **权限**：即操作资源的权利，比如访问某个页面，以及对某个模块的数据的添加，修改，删除，查看的权利；
> **角色**：是权限的集合，一中角色可以包含多种权限；
> **用户**：在 Shiro 中，代表访问系统的用户，即 Subject；

##### 3.2 授权

* 在此之前先创建一个 ShiroUtil 工具类便于调用

```java
public class ShiroUtil {

	public static Subject login(String configFile, String userName, String password) {
		// 读取配置文件，初始化 SecurityManager 仓库
		Factory<SecurityManager> factory = new IniSecurityManagerFactory(configFile);
		// 获取 SecurityManager 实例
		SecurityManager securityManager = factory.getInstance();
		// 把 SecurityManager 绑定到 SecurityUtils
		SecurityUtils.setSecurityManager(securityManager);
		// 得到当前执行的用户
		Subject currentUser = SecurityUtils.getSubject();
		// 创建 token 令牌，用户名/密码
		UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
		try {
			currentUser.login(token);
			System.out.println("身份认证成功");
		} catch (AuthenticationException e) {
			e.printStackTrace();
			System.out.println("身份认证失败");
		}
		return currentUser;
	}
}
```

###### 3.2.1 编程式授权

*  基于角色的访问控制

  * 创建 shiro_role.ini

  ```ini
  [users]
  vole=123456,role1,role2
  gakki=520,role1
  ;基于角色的访问控制
  ```

  * 创建 ShiroRoleTest.java

  ```java
  public class ShiroRoleTest {

  	@Test
  	public void testHasRole() {
  		Subject currentUser = ShiroUtil.login("classpath:shiro_role.ini", "vole", "123456");
  		System.out.println(currentUser.hasRole("role1")?"有 role1 权限":"没有 role1 权限");
  		
  		boolean [] result= currentUser.hasRoles(Arrays.asList("role1","role2","role3"));
  		System.out.println(result[0]?"有 role1 权限":"没有 role1 权限");
  		System.out.println(result[1]?"有 role2 权限":"没有 role2权限");
  		System.out.println(result[2]?"有 role3 权限":"没有 role3权限");
  		
  		System.out.println(currentUser.hasAllRoles(Arrays.asList("role1","role2","role3"))?
  				"包含所有权限":"具有部分权限");
  		currentUser.logout();
  	}
  	
  	/**
  	 * 调用 checkRole 时，如果有该权限，则不返回值，直接通过，反之会报 AuthorizationException 异常
  	 */
  	@Test
  	public void testCheckRole(){
  		Subject currentUser = ShiroUtil.login("classpath:shiro_role.ini", "vole", "123456");
  		currentUser.checkRole("role1");
  		currentUser.checkRoles("role1","role2");
  		currentUser.logout();
  	}

  }
  ```


* 基于权限的访问控制

  * 创建 shiro_permission.ini

  ```ini
  [users]
  vole=123456,role1,role2
  gakki=520,role1
  [roles]
  role1=user:select
  role2=user:add,user:update,user:delete
  ;基于权限的访问控制
  ```

  * 创建 ShiroPermissionTest.java 测试类

  ```java
  public class ShiroPermissionTest {

  	@Test
  	public void testHasPermission() {
  		Subject currentUser = ShiroUtil.login("classpath:shiro_permission.ini", "vole", "123456");
  		//Subject currentUser = ShiroUtil.login("classpath:shiro_permission.ini", "gakki", "520");
  		System.out.println(currentUser.isPermitted("user:select")?"Have user:select":"No user:select");
  		boolean [] result= currentUser.isPermitted("user:select","user:add","user:update");
  		System.out.println(result[0]?"有 user:select 权限":"没有 user:select 权限");
  		System.out.println(result[1]?"有 user:add 权限":"没有 user:add权限");
  		System.out.println(result[2]?"有 user:update 权限":"没有 user:update权限");
  		System.out.println(currentUser.isPermittedAll("user:select","user:add","user:update")?
  				"包含所有权限":"具有部分权限");
  		currentUser.logout();
  	}

  	/**
  	 * 调用 checkPermission 时，如果有该权限，则不返回值，直接通过，反之会报 AuthorizationException 异常
  	 */
  	@Test
  	public void testCheckPermission(){
  		Subject currentUser = ShiroUtil.login("classpath:shiro_permission.ini", "vole", "123456");
  		currentUser.checkPermission("user:select");
  		currentUser.checkPermissions("user:select","user:add","user:update");
  		currentUser.logout();
  	}
  }
  ```



###### 3.2.2 注解式授权(一般是集成到 web 项目中，现在只是简单的了解)

  > @RequiresAuthentication 要求当前 Subject 已经在当前的 session 中被验证通过才能被访问或调用。
  > @RequiresGuest 要求当前的 Subject 是一个"guest"，也就是说，他们必须是在之前的 session 中没有被验证或被记住才
  > 能被访问或调用。
  > @RequiresPermissions("account:create") 要求当前的 Subject 被允许一个或多个权限，以便执行注解的方法。
  > @RequiresRoles("administrator") 要求当前的 Subject 拥有所有指定的角色。如果他们没有，则该方法将不会被执行，而
  > 且 AuthorizationException 异常将会被抛出。
  > @RequiresUser RequiresUser 注解需要当前的 Subject 是一个应用程序用户才能被注解的类/实例/方法访问或调用。一个“应
  > 用程序用户”被定义为一个拥有已知身份，或在当前 session 中由于通过验证被确认，或者在之前 session 中的'RememberMe'
  > 服务被记住。
  > 3，Jsp 标签授权
  > <%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
  > Guest 标签：用户没有身份验证时显示相应信息，即游客访问信息；
  > User 标签：用户已经身份验证/记住我登录后显示相应的信息；
  > Authenticated 标签：用户已经身份验证通过，即 Subject.login 登录成功，不是记住我登录的。
  > notAuthenticated 标签：用户没有身份验证通过，即没有调用 Subject.login 进行登录，包括记住我自动登录
  > 的也属于未进行身份验证。
  > principal 标签 显示用户身份信息，默认调用 Subject.getPrincipal()获取，即 Primary Principal。
  > hasRole 标签 如果当前 Subject 有角色将显示 body 体内容。
  > lacksRole 标签 如果当前 Subject 没有角色将显示 body 体内容。
  > hasAnyRoles 标签 如果当前 Subject 有任意一个角色（或的关系）将显示 body 体内容。
  > hasPermission 标签 如果当前 Subject 有权限将显示 body 体内容。
  > lacksPermission 标签 如果当前 Subject 没有权限将显示 body 体内容。

  

##### 3.3 Permissions 

> 单个权限 query
> 单个资源多个权限 user:query user:add 多值 user:query,add
> 单个资源所有权限 user:query,add,update,delete user:*
> 所有资源某个权限 *:view
>
> 实例级别的权限控制
> 单个实例的单个权限 printer:query:lp7200 printer:print:epsoncolor
> 所有实例的单个权限 printer:print:*
> 所有实例的所有权限 printer:*:*
> 单个实例的所有权限 printer:*:lp7200
> 单个实例的多个权限 printer:query,print:lp7200



##### 3.4 授权流程 

![shiro_授权流程](https://raw.githubusercontent.com/volewu/JAVA-Learn/master/笔记/Shiro/photo/shiro_授权流程.PNG)



#### 四、集成 Web

##### 4.1 Shiro  集成 Web 配置

* 项目案例截图

![shiro_web](https://raw.githubusercontent.com/volewu/JAVA-Learn/master/笔记/Shiro/photo/shiro_web.PNG)

* 在 pom.xml 中配置相关 Jar 

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>com.vole.shiro</groupId>
	<artifactId>ShiroWeb</artifactId>
	<packaging>war</packaging>
	<version>0.0.1-SNAPSHOT</version>
	<name>ShiroWeb Maven Webapp</name>
	<url>http://maven.apache.org</url>
	<dependencies>
		<dependency>
			<groupId>junit</groupId>
			<artifactId>junit</artifactId>
			<version>3.8.1</version>
			<scope>test</scope>
		</dependency>
		
		<!-- 添加servlet支持 -->
		<dependency>
			<groupId>javax.servlet</groupId>
			<artifactId>javax.servlet-api</artifactId>
			<version>3.1.0</version>
		</dependency>

		<dependency>
			<groupId>javax.servlet.jsp</groupId>
			<artifactId>javax.servlet.jsp-api</artifactId>
			<version>2.3.1</version>
		</dependency>

		<!-- 添加jstl支持 -->
		<dependency>
			<groupId>javax.servlet</groupId>
			<artifactId>jstl</artifactId>
			<version>1.2</version>
		</dependency>

		<!-- 添加日志支持 -->
		<dependency>
			<groupId>log4j</groupId>
			<artifactId>log4j</artifactId>
			<version>1.2.17</version>
		</dependency>


		<dependency>
			<groupId>commons-logging</groupId>
			<artifactId>commons-logging</artifactId>
			<version>1.2</version>
		</dependency>

		<!-- 添加shiro支持 -->
		<dependency>
			<groupId>org.apache.shiro</groupId>
			<artifactId>shiro-core</artifactId>
			<version>1.2.4</version>
		</dependency>

		<dependency>
			<groupId>org.apache.shiro</groupId>
			<artifactId>shiro-web</artifactId>
			<version>1.2.4</version>
		</dependency>

		<dependency>
			<groupId>org.slf4j</groupId>
			<artifactId>slf4j-api</artifactId>
			<version>1.7.12</version>
		</dependency>

	</dependencies>
	<build>
		<finalName>ShiroWeb</finalName>
	</build>
</project>
```

* 创建 log4j.properties

```properties
log4j.rootLogger=DEBUG, Console  
  
#Console  
log4j.appender.Console=org.apache.log4j.ConsoleAppender  
log4j.appender.Console.layout=org.apache.log4j.PatternLayout  
log4j.appender.Console.layout.ConversionPattern=%d [%t] %-5p [%c] - %m%n  
  
log4j.logger.java.sql.ResultSet=INFO  
log4j.logger.org.apache=INFO  
log4j.logger.java.sql.Connection=DEBUG  
log4j.logger.java.sql.Statement=DEBUG  
log4j.logger.java.sql.PreparedStatement=DEBUG  
```

* 配置 web.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://java.sun.com/xml/ns/javaee" xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd" id="WebApp_ID" version="2.5">
  <display-name>ShrioWeb</display-name>
  <welcome-file-list>
    <welcome-file>index.html</welcome-file>
    <welcome-file>index.htm</welcome-file>
    <welcome-file>index.jsp</welcome-file>
    <welcome-file>default.html</welcome-file>
    <welcome-file>default.htm</welcome-file>
    <welcome-file>default.jsp</welcome-file>
  </welcome-file-list>
  
  <listener>
	    <listener-class>org.apache.shiro.web.env.EnvironmentLoaderListener</listener-class>
  </listener>
   
	<!-- 添加shiro支持 -->
	<filter>
	    <filter-name>ShiroFilter</filter-name>
	    <filter-class>org.apache.shiro.web.servlet.ShiroFilter</filter-class>
	</filter>
	
	<filter-mapping>
	    <filter-name>ShiroFilter</filter-name>
	    <url-pattern>/*</url-pattern>
	</filter-mapping>
	
	
	<servlet>
		<servlet-name>loginServlet</servlet-name>
		<servlet-class>com.vole.servlet.LoginServlet</servlet-class>
	</servlet>
	
	<servlet-mapping>
		<servlet-name>loginServlet</servlet-name>
		<url-pattern>/login</url-pattern>
	</servlet-mapping>
	
	<servlet>
		<servlet-name>adminServlet</servlet-name>
		<servlet-class>com.vole.servlet.AdminServlet</servlet-class>
	</servlet>
	
	<servlet-mapping>
		<servlet-name>adminServlet</servlet-name>
		<url-pattern>/admin</url-pattern>
	</servlet-mapping>
</web-app>
```

* 创建 shiro.ini (本项目只是测试，所有用的是本地数据，以后的数据都来至于数据库)

```ini
[main]
authc.loginUrl=/login
;认证没有通过，跳转到 login 请求
roles.unauthorizedUrl=/unauthorized.jsp
perms.unauthorizedUrl=/unauthorized.jsp
;没登入时，要先进行身份认证，再进行角色认证
[users]
gakki=123456,admin
vole=123,teacher
marry=234
json=345
[roles]
admin=user:*
teacher=student:*
[urls]
/login=anon
/admin=authc
/student=roles[teacher]
/teacher=perms["user:create"]
```

* 创建 AdminServlet.java 与 LoginServlet.java

```java
public class AdminServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("admin do get");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("admin do post");
	}
	
}
```

```java
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("login doget");
		request.getRequestDispatcher("login.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("login dopost");
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
		try {
			subject.login(token);
			response.sendRedirect("success.jsp");
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorInfo", "用户名或者密码错误");
			request.getRequestDispatcher("login.jsp").forward(request, response);
		}
	}

}
```

* 创建 index.jsp 、login.jsp 、success.jsp 、unauthorzed.jsp

```jsp
<!-- index-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
hello World!
</body>
</html>

<!--login-->
<form action="login" method="post">
	userName:<input type="text" name="userName"/><br/>
	password:<input type="password" name="password"/><br/>
	<input type="submit" value="登录"/>
</form>

<!--success-->
<body>
成功登入!
</body>

<!--unauthorized-->
<body>
认证未通过，或者权限不足
</body>
```

##### 4.2 Shiro  集成 Web 具体使用

* Url 匹配方式

> ? 匹配一个字符 /admin? 可以匹配/admin1 /admin2 但是不能匹配/admin12 /admin
>
> \* 匹配零个或者一个或者多个字符 /admin* 可以匹配 /admin /admin1 /admin12 但是不能匹配/admin/abc
>
> ** 匹配零个或者多个路径 /admin/** 可以匹配/admin /admin/a /admin/a/b

```ini
;eg:
[main]
....
[urls]
/login=anon
/admin*=authc
;/admin?=authc
;/admin**=authc
/student=roles[teacher]
/teacher=perms["user:create"]
```

 ##### 4.3 Shiro 标签使用

> Jsp 标签授权
> <%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
> Guest 标签：用户没有身份验证时显示相应信息，即游客访问信息；
> User 标签：用户已经身份验证/记住我登录后显示相应的信息；
> Authenticated 标签：用户已经身份验证通过，即 Subject.login 登录成功，不是记住我登录的。
> notAuthenticated 标签：用户没有身份验证通过，即没有调用 Subject.login 进行登录，包括记住我自动登录
> 的也属于未进行身份验证。
> principal 标签 显示用户身份信息，默认调用 Subject.getPrincipal()获取，即 Primary Principal。
> hasRole 标签 如果当前 Subject 有角色将显示 body 体内容。
> lacksRole 标签 如果当前 Subject 没有角色将显示 body 体内容。
> hasAnyRoles 标签 如果当前 Subject 有任意一个角色（或的关系）将显示 body 体内容。
> hasPermission 标签 如果当前 Subject 有权限将显示 body 体内容。
> lacksPermission 标签 如果当前 Subject 没有权限将显示 body 体内容。

  ```jsp
<!--eg:-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
成功登入!
<shiro:hasRole name="admin">
	欢迎 admin 权限用户 <shiro:principal></shiro:principal>
</shiro:hasRole>
<shiro:hasPermission name="student:create">
    欢迎有 student:create 权限用户  <shiro:principal></shiro:principal>
</shiro:hasPermission>
</body>
</html>
  ```

##### 4.4 Shiro Session 机制

* eg:

```java
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("login doget");
		request.getRequestDispatcher("login.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("login dopost");
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
		try {
			subject.login(token);
			Session session = subject.getSession();
			System.out.println("SessionId: "+session.getId());
			System.out.println("SessionHost: "+session.getHost());
			System.out.println("SessionTimeout: "+session.getTimeout());
			session.setAttribute("info", "session 的值");
			response.sendRedirect("success.jsp");
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorInfo", "用户名或者密码错误");
			request.getRequestDispatcher("login.jsp").forward(request, response);
		}
	}
}

/*success.jsp
*${info }
*/
//SessionId: 59407D70C3EFFECEE54431C0E0A49CDF
//SessionHost: 0:0:0:0:0:0:0:1
//SessionTimeout: 1800000  (halfhour)
```

##### 4.5 自定义 Realm

> 该案例是在 ShiroWeb 的基础上加的

* 项目结构

![shiro_myRealm](https://raw.githubusercontent.com/volewu/JAVA-Learn/master/笔记/Shiro/photo/shiro_myRealm.PNG)

* 创建数据库与相关的表 db_shiro.sql

```sql
/*
SQLyog Ultimate v11.33 (64 bit)
MySQL - 5.1.49-community : Database - db_shiro
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`db_shiro` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `db_shiro`;

/*Table structure for table `t_permission` */

DROP TABLE IF EXISTS `t_permission`;

CREATE TABLE `t_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `permissionName` varchar(50) DEFAULT NULL,
  `roleId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `roleId` (`roleId`),
  CONSTRAINT `t_permission_ibfk_1` FOREIGN KEY (`roleId`) REFERENCES `t_role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Data for the table `t_permission` */

insert  into `t_permission`(`id`,`permissionName`,`roleId`) values (1,'user:*',1),(2,'student:*',2);

/*Table structure for table `t_role` */

DROP TABLE IF EXISTS `t_role`;

CREATE TABLE `t_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `roleName` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Data for the table `t_role` */

insert  into `t_role`(`id`,`roleName`) values (1,'admin'),(2,'teacher');

/*Table structure for table `t_user` */

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userName` varchar(20) DEFAULT NULL,
  `password` varchar(20) DEFAULT NULL,
  `roleId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `roleId` (`roleId`),
  CONSTRAINT `t_user_ibfk_1` FOREIGN KEY (`roleId`) REFERENCES `t_role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Data for the table `t_user` */

insert  into `t_user`(`id`,`userName`,`password`,`roleId`) values (1,'gakki','123456',1),(2,'vole','123',2),(3,'marry','234',NULL),(4,'json','345',NULL);

/*Table structure for table `users` */

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userName` varchar(20) DEFAULT NULL,
  `password` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `users` */

insert  into `users`(`id`,`userName`,`password`) values (1,'java1234','123456');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
```

* 在 pom.xml 中导入 mysql 支持 Jar

```xml
	<dependency>
		<groupId>mysql</groupId>
		<artifactId>mysql-connector-java</artifactId>
		<version>5.1.37</version>
	</dependency>
```

* 创建数据库工具类 DbUtil.java

```java
/**
 * 数据库工具类
 * @author 
 *
 */
public class DbUtil {

	/**
	 * 获取数据库连接
	 * @return
	 * @throws Exception
	 */
	public Connection getCon() throws Exception{
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/db_shiro", "root", "123456");
		return con;
	}
	
	/**
	 * 关闭数据库连接
	 * @param con
	 * @throws Exception
	 */
	public void closeCon(Connection con)throws Exception{
		if(con!=null){
			con.close();
		}
	}
	
	public static void main(String[] args) {
		DbUtil dbUtil=new DbUtil();
		try {
			dbUtil.getCon();
			System.out.println("数据库连接成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("数据库连接失败");
		}
	}
}
```

* 创建 User 实体类

```java
public class User {

	private Integer id;
	private String userName;
	private String password;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
```

* 创建 UserDao.java

```java
public class UserDao {

	public User getByUserName(Connection con, String userName) throws Exception {
		User resultUser = null;
		String sql = "select * from t_user where userName=?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, userName);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			resultUser = new User();
			resultUser.setId(rs.getInt("id"));
			resultUser.setUserName(rs.getString("userName"));
			resultUser.setPassword(rs.getString("password"));
		}
		return resultUser;
	}

	public Set<String> getRoles(Connection con, String userName) throws Exception {
		Set<String> roles = new HashSet<String>();
		String sql = "select * from t_user u,t_role r where u.roleId=r.id and u.userName=?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, userName);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			roles.add(rs.getString("roleName"));
		}
		return roles;
	}

	public Set<String> getPermissions(Connection con, String userName) throws Exception {
		Set<String> permissions=new HashSet<String>();
		String sql="select * from t_user u,t_role r,t_permission p where u.roleId=r.id and p.roleId=r.id and u.userName=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, userName);
		ResultSet rs=pstmt.executeQuery();
		while(rs.next()){
			permissions.add(rs.getString("permissionName"));
		}
		return permissions;
	}
}
```

* 创建 MyReaml.java

```java
public class MyRealm extends AuthorizingRealm {

	private UserDao userDao = new UserDao();
	private DbUtil dbUtil = new DbUtil();

	/**
	 * 为当前登入的用户授予角色与权限
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String userName = (String) principals.getPrimaryPrincipal();
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		Connection con = null;
		try {
			con = dbUtil.getCon();
			authorizationInfo.setRoles(userDao.getRoles(con, userName));
			authorizationInfo.setStringPermissions(userDao.getPermissions(con, userName));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				dbUtil.closeCon(con);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 验证当前登入的用户
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String userName = (String) token.getPrincipal();
		Connection con = null;
		try {
			con = dbUtil.getCon();
			User user = userDao.getByUserName(con, userName);
			if (user != null) {
				AuthenticationInfo authcInfo = new SimpleAuthenticationInfo(user.getUserName(), user.getPassword(),"xx");
				return authcInfo;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				dbUtil.closeCon(con);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}

```

* 配置 shiro.ini

```ini
[main]
authc.loginUrl=/login
;认证没有通过，跳转到 login 请求
roles.unauthorizedUrl=/unauthorized.jsp
perms.unauthorizedUrl=/unauthorized.jsp
;没登入时，要先进行身份认证，再进行角色认证
;[users]
;gakki=123456,admin
;vole=123,teacher
;marry=234
;json=345
;[roles]
;admin=user:*
;teacher=student:*

myRealm=com.vole.realm.MyRealm
securityManager.realms=$myRealm

[urls]
/login=anon
/admin=authc
/student=roles[teacher]
/teacher=perms["user:create"]
```



#### 五、Shiro 加密

##### 5.1 shiro  加密解密

* 一般的加密方法有 Base64 (加密与解密)、MD5 (不可逆，推荐使用)......等
* eg: 

```java
public class CryptographyUtil {

	/**
	 * base64 encryption
	 * @param str
	 * @return
	 */
	public static String encBase64(String str){
		return Base64.encodeToString(str.getBytes());
	}
	
	/**
	 * base64 deciphering
	 * @param str
	 * @return
	 */
	public static String decBase64(String str){
		return Base64.decodeToString(str);
	}
	
	/**
	 * md5 encrytion：不可逆
	 * @param str
	 * @param salt :提高保密性，避免碰撞检测出密码，理论上该参数要放在配置文件中去
	 * @return
	 */
	public static String md5(String str,String salt){
		return new Md5Hash(str, salt).toString();
	}
	
	
	public static void main(String[] args) {
		String password = "123456";
		System.out.println("Base64 encryption : "+CryptographyUtil.encBase64(password));
		System.out.println("Base64 deciphering : "+CryptographyUtil.decBase64("MTIzNDU2"));
		System.out.println("MD5 encryption : "+CryptographyUtil.md5(password,"vole"));
	}
} 
//Base64 encryption : MTIzNDU2
//Base64 deciphering : 123456
//MD5 encryption : 57f5ec7a7bf1b68dbe79423f06145750

//LoginServlet.java 一般用在 token 里
UsernamePasswordToken token=new UsernamePasswordToken(userName, CryptographyUtil.md5(password, "vole"));
```



#### 六、Shiro 集成 Spring（4.1.7）

**eg: 项目目录**(报错并没影响)

![Shiro_Spring](https://raw.githubusercontent.com/volewu/JAVA-Learn/master/笔记/Shiro/photo/Shiro_Spring.PNG)

* 在 pom.xml 中导入相关的 jar

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>com.vole.com</groupId>
	<artifactId>ShiroSpring</artifactId>
	<packaging>war</packaging>
	<version>0.0.1-SNAPSHOT</version>
	<name>ShiroSpring Maven Webapp</name>
	<url>http://maven.apache.org</url>
	<dependencies>
		<dependency>
			<groupId>junit</groupId>
			<artifactId>junit</artifactId>
			<version>3.8.1</version>
			<scope>test</scope>
		</dependency>

		<!-- 添加Servlet支持 -->
		<dependency>
			<groupId>javax.servlet</groupId>
			<artifactId>javax.servlet-api</artifactId>
			<version>3.1.0</version>
		</dependency>

		<dependency>
			<groupId>javax.servlet.jsp</groupId>
			<artifactId>javax.servlet.jsp-api</artifactId>
			<version>2.3.1</version>
		</dependency>

		<!-- 添加jtl支持 -->
		<dependency>
			<groupId>javax.servlet</groupId>
			<artifactId>jstl</artifactId>
			<version>1.2</version>
		</dependency>

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

		<dependency>
			<groupId>org.mybatis</groupId>
			<artifactId>mybatis-spring</artifactId>
			<version>1.2.3</version>
		</dependency>

		<!-- 添加日志支持 -->
		<dependency>
			<groupId>log4j</groupId>
			<artifactId>log4j</artifactId>
			<version>1.2.17</version>
		</dependency>

		<!-- 添加mybatis支持 -->
		<dependency>
			<groupId>org.mybatis</groupId>
			<artifactId>mybatis</artifactId>
			<version>3.3.0</version>
		</dependency>

		<!-- jdbc驱动包 -->
		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<version>5.1.37</version>
		</dependency>

		<dependency>
			<groupId>org.apache.shiro</groupId>
			<artifactId>shiro-core</artifactId>
			<version>1.2.4</version>
		</dependency>

		<dependency>
			<groupId>org.slf4j</groupId>
			<artifactId>slf4j-log4j12</artifactId>
			<version>1.7.12</version>
		</dependency>

		<dependency>
			<groupId>org.apache.shiro</groupId>
			<artifactId>shiro-web</artifactId>
			<version>1.2.4</version>
		</dependency>

		<dependency>
			<groupId>org.apache.shiro</groupId>
			<artifactId>shiro-spring</artifactId>
			<version>1.2.4</version>
		</dependency>

	</dependencies>
	<build>
		<finalName>ShiroSpring</finalName>
	</build>
</project>
```

* 配置 web.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance http://www.springmodules.org/schema/cache/springmodules-cache.xsd "  
	xmlns="http://java.sun.com/xml/ns/javaee"
	xsi:schemaLocation="http://java.sun.com/xml/ns/javaee 
	http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"
	id="WebApp_ID" version="2.5">
	<display-name>ShiroSpring</display-name>
	<welcome-file-list>
		<welcome-file>index.jsp</welcome-file>
	</welcome-file-list>

	<!-- shiro过滤器定义 -->
	<filter>
		<filter-name>shiroFilter</filter-name>
		<filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
		<init-param>
			<!-- 该值缺省为false,表示生命周期由SpringApplicationContext管理,设置为true则表示由ServletContainer管理 -->
			<param-name>targetFilterLifecycle</param-name>
			<param-value>true</param-value>
		</init-param>
	</filter>
	<filter-mapping>
		<filter-name>shiroFilter</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>


	<!-- Spring配置文件 -->
	<context-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>classpath:applicationContext.xml</param-value>
	</context-param>
	<!-- 编码过滤器 -->
	<filter>
		<filter-name>encodingFilter</filter-name>
		<filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
		<async-supported>true</async-supported>
		<init-param>
			<param-name>encoding</param-name>
			<param-value>UTF-8</param-value>
		</init-param>
	</filter>
	<filter-mapping>
		<filter-name>encodingFilter</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>
	<!-- Spring监听器 -->
	<listener>
		<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
	</listener>

	<!-- 添加对springmvc的支持 -->
	<servlet>
		<servlet-name>springMVC</servlet-name>
		<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
		<init-param>
			<param-name>contextConfigLocation</param-name>
			<param-value>classpath:spring-mvc.xml</param-value>
		</init-param>
		<load-on-startup>1</load-on-startup>
		<async-supported>true</async-supported>
	</servlet>
	<servlet-mapping>
		<servlet-name>springMVC</servlet-name>
		<url-pattern>*.do</url-pattern>
	</servlet-mapping>

</web-app>
```

* 配置 applicationContext.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>    
<beans xmlns="http://www.springframework.org/schema/beans"    
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"   
    xmlns:p="http://www.springframework.org/schema/p"  
    xmlns:aop="http://www.springframework.org/schema/aop"   
    xmlns:context="http://www.springframework.org/schema/context"  
    xmlns:jee="http://www.springframework.org/schema/jee"  
    xmlns:tx="http://www.springframework.org/schema/tx"  
    xsi:schemaLocation="    
        http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-4.0.xsd  
        http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.0.xsd  
        http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.0.xsd  
        http://www.springframework.org/schema/jee http://www.springframework.org/schema/jee/spring-jee-4.0.xsd  
        http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-4.0.xsd">    
        
	<!-- 自动扫描 -->
	<context:component-scan base-package="com.vole.service" />
	
	<!-- 配置数据源 -->
	<bean id="dataSource"
		class="org.springframework.jdbc.datasource.DriverManagerDataSource">
		<property name="driverClassName" value="com.mysql.jdbc.Driver"/>
		<property name="url" value="jdbc:mysql://localhost:3306/db_shiro"/>
		<property name="username" value="root"/>
		<property name="password" value="123456"/>
	</bean>

	<!-- 配置mybatis的sqlSessionFactory -->
	<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
		<property name="dataSource" ref="dataSource" />
		<!-- 自动扫描mappers.xml文件 -->
		<property name="mapperLocations" value="classpath:com/vole/mappers/*.xml"></property>
		<!-- mybatis配置文件 -->
		<property name="configLocation" value="classpath:mybatis-config.xml"></property>
	</bean>

	<!-- DAO接口所在包名，Spring会自动查找其下的类 -->
	<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
		<property name="basePackage" value="com.vole.dao" />
		<property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"></property>
	</bean>

	<!-- (事务管理)transaction manager, use JtaTransactionManager for global tx -->
	<bean id="transactionManager"
		class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
		<property name="dataSource" ref="dataSource" />
	</bean>
	
	<!-- 自定义Realm -->
	<bean id="myRealm" class="com.vole.realm.MyRealm"/>  
	
	<!-- 安全管理器 -->
	<bean id="securityManager" class="org.apache.shiro.web.mgt.DefaultWebSecurityManager">  
  	  <property name="realm" ref="myRealm"/>  
	</bean>  
	
	<!-- Shiro过滤器 -->
	<bean id="shiroFilter" class="org.apache.shiro.spring.web.ShiroFilterFactoryBean">  
	    <!-- Shiro的核心安全接口,这个属性是必须的 -->  
	    <property name="securityManager" ref="securityManager"/>
	    <!-- 身份认证失败，则跳转到登录页面的配置 -->  
	    <property name="loginUrl" value="/index.jsp"/>
	    <!-- 权限认证失败，则跳转到指定页面 -->  
	    <property name="unauthorizedUrl" value="/unauthor.jsp"/>  
	    <!-- Shiro连接约束配置,即过滤链的定义 -->  
	    <property name="filterChainDefinitions">  
	        <value>  
	             /login=anon
				/admin*=authc
				/student=roles[teacher]
				/teacher=perms["user:create"]
	        </value>  
	    </property>
	</bean>  
	
	<!-- 保证实现了Shiro内部lifecycle函数的bean执行 -->  
	<bean id="lifecycleBeanPostProcessor" class="org.apache.shiro.spring.LifecycleBeanPostProcessor"/>  
	
	<!-- 开启Shiro注解 -->
	<bean class="org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator" depends-on="lifecycleBeanPostProcessor"/>  
  		<bean class="org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor">  
  	  <property name="securityManager" ref="securityManager"/>  
    </bean>  
  
	<!-- 配置事务通知属性 -->  
    <tx:advice id="txAdvice" transaction-manager="transactionManager">  
        <!-- 定义事务传播属性 -->  
        <tx:attributes>  
            <tx:method name="insert*" propagation="REQUIRED" />  
            <tx:method name="update*" propagation="REQUIRED" />  
            <tx:method name="edit*" propagation="REQUIRED" />  
            <tx:method name="save*" propagation="REQUIRED" />  
            <tx:method name="add*" propagation="REQUIRED" />  
            <tx:method name="new*" propagation="REQUIRED" />  
            <tx:method name="set*" propagation="REQUIRED" />  
            <tx:method name="remove*" propagation="REQUIRED" />  
            <tx:method name="delete*" propagation="REQUIRED" />  
            <tx:method name="change*" propagation="REQUIRED" />  
            <tx:method name="check*" propagation="REQUIRED" />  
            <tx:method name="get*" propagation="REQUIRED" read-only="true" />  
            <tx:method name="find*" propagation="REQUIRED" read-only="true" />  
            <tx:method name="load*" propagation="REQUIRED" read-only="true" />  
            <tx:method name="*" propagation="REQUIRED" read-only="true" />  
        </tx:attributes>  
    </tx:advice>  
  
    <!-- 配置事务切面 -->  
    <aop:config>  
        <aop:pointcut id="serviceOperation"  
            expression="execution(* com.vole.service.*.*(..))" />  
        <aop:advisor advice-ref="txAdvice" pointcut-ref="serviceOperation" />  
    </aop:config>  
    
</beans>
```

* 配置 log4j.properties

```properties
log4j.rootLogger=DEBUG, Console  
  
#Console  
log4j.appender.Console=org.apache.log4j.ConsoleAppender  
log4j.appender.Console.layout=org.apache.log4j.PatternLayout  
log4j.appender.Console.layout.ConversionPattern=%d [%t] %-5p [%c] - %m%n  
  
log4j.logger.java.sql.ResultSet=INFO  
log4j.logger.org.apache=INFO  
log4j.logger.java.sql.Connection=DEBUG  
log4j.logger.java.sql.Statement=DEBUG  
log4j.logger.java.sql.PreparedStatement=DEBUG  
```

* 配置 mybatis-config.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
	<!-- 别名 -->
	<typeAliases>
		<package name="com.vole.entity"/>
	</typeAliases>
</configuration>
```

* 配置 spring-mvc.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>    
<beans xmlns="http://www.springframework.org/schema/beans"    
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"   
    xmlns:p="http://www.springframework.org/schema/p"  
    xmlns:aop="http://www.springframework.org/schema/aop"   
    xmlns:context="http://www.springframework.org/schema/context"  
    xmlns:jee="http://www.springframework.org/schema/jee"  
    xmlns:tx="http://www.springframework.org/schema/tx"  
    xsi:schemaLocation="    
        http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-4.0.xsd  
        http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.0.xsd  
        http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.0.xsd  
        http://www.springframework.org/schema/jee http://www.springframework.org/schema/jee/spring-jee-4.0.xsd  
        http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-4.0.xsd">    

	<!-- 使用注解的包，包括子集 -->
	<context:component-scan base-package="com.vole.controller" />

	<!-- 视图解析器 -->
	<bean id="viewResolver"
		class="org.springframework.web.servlet.view.InternalResourceViewResolver">
		<property name="prefix" value="/" />
		<property name="suffix" value=".jsp"></property>
	</bean>

</beans>  
```

* 创建 User.java

```java
package com.vole.entity;

public class User {
	private Integer id;
	private String userName;
	private String password;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
```

* 创建 UserDao.java

```java
package com.vole.dao;

import java.util.Set;

import com.vole.entity.User;

public interface UserDao {

	/**
	 * 通过用户名查询用户
	 * @param userName
	 * @return
	 */
	public User getByUserName(String UserName);

	/**
	 * 通过用户名查询角色信息
	 * @param userName
	 * @return
	 */
	public Set<String> getRoles(String userName);

	/**
	 * 通过用户名查询权限信息
	 * @param userName
	 * @return
	 */
	public Set<String> getPermissions(String userName);

}
```

* 创建 UserService.java

```java
package com.vole.service;

import java.util.Set;

import com.vole.entity.User;

public interface UserService {

	/**
	 * 通过用户名查询用户
	 * 
	 * @param userName
	 * @return
	 */
	public User getByUserName(String userName);

	/**
	 * 通过用户名查询角色信息
	 * 
	 * @param userName
	 * @return
	 */
	public Set<String> getRoles(String userName);

	/**
	 * 通过用户名查询权限信息
	 * 
	 * @param userName
	 * @return
	 */
	public Set<String> getPermissions(String userName);
}
```

* 创建 UserMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.vole.dao.UserDao">

	<resultMap type="User" id="UserResult">
		<result property="id" column="id"/>
		<result property="userName" column="userName"/>
		<result property="password" column="password"/>
	</resultMap>
	
	<select id="getByUserName" parameterType="String" resultMap="UserResult">
		select * from t_user where userName=#{userName}
	</select>
	
	<select id="getRoles" parameterType="String" resultType="String">
		select r.roleName from t_user u,t_role r where u.roleId=r.id and u.userName=#{userName}
	</select>
	
	<select id="getPermissions" parameterType="String" resultType="String">
		select p.permissionName from t_user u,t_role r,t_permission p where u.roleId=r.id and p.roleId=r.id and u.userName=#{userName}
	</select>

</mapper> 
```

* 创建 UserServiceImpl.java

```java
package com.vole.service.impl;

import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.vole.dao.UserDao;
import com.vole.entity.User;
import com.vole.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Resource
	private UserDao userDao;

	public User getByUserName(String userName) {
		return userDao.getByUserName(userName);
	}

	public Set<String> getRoles(String userName) {
		return userDao.getRoles(userName);
	}

	public Set<String> getPermissions(String userName) {
		return userDao.getPermissions(userName);
	}

}
```

* 创建 UserController.java

```java
package com.vole.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vole.entity.User;

@Controller
@RequestMapping("/user")
public class UserController {

	@RequestMapping("/login")
	public String login(User user, HttpServletRequest request) {
		Subject subject=SecurityUtils.getSubject();
		UsernamePasswordToken token=new UsernamePasswordToken(user.getUserName(), user.getPassword());
		try{
			subject.login(token);
			Session session=subject.getSession();
			System.out.println("sessionId:"+session.getId());
			System.out.println("sessionHost:"+session.getHost());
			System.out.println("sessionTimeout:"+session.getTimeout());
			session.setAttribute("info", "session的数据");
			return "redirect:/success.jsp";
		}catch(Exception e){
			e.printStackTrace();
			request.setAttribute("user", user);
			request.setAttribute("errorMsg", "用户名或密码错误！");
			return "index";
		}
	}

}
```

* index.jsp & success.jsp

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<form action="${pageContext.request.contextPath }/user/login.do" method="post">
	userName:<input type="text" name="userName" value="${user.userName }"/><br/>
	password:<input type="password" name="password" value="${user.password }"><br/>
	<input type="submit" value="login"/><font color="red">${errorMsg }</font>
</form>  
</body>
</html>

<!--success-->
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<body>
${info }
欢迎你!
<shiro:hasRole name="admin">
	欢迎有admin角色的用户！<shiro:principal/>
</shiro:hasRole>
<shiro:hasPermission name="student:create">
	欢迎有student:create权限的用户！<shiro:principal/>
</shiro:hasPermission>
</body>
```

