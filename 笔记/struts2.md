



# Struts2 笔记

##### 一、简介：

> Struts2 是一个基于 MVC 设计模式的 Web 应用框架，它的本质相当于一个 servlet ，在 MVC 设计模式中，Struts2 作为控制器（Controller）来建立模型与视图的数据交互。相对于传统的 Jsp+Servlet 模式，Struts2 更适合企业级团队开发，方便系统的维护；

1. 、简单使用

   导入 Struts2 核心 jar 包

在 web.xml 下配置：

```xml

<filter>
	<filter-name>Struts2</filter-name>
	<filter-class>
		org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter
	</filter-class>
</filter>

<filter-mapping>
	<filter-name>Struts2</filter-name>
	<url-pattern>/*</url-pattern>
</filter-mapping>
```

2. 添加 struts.xml 文件：

   ```xml
   <?xml version="1.0" encoding="UTF-8" ?>
   <!DOCTYPE struts PUBLIC
       "-//Apache Software Foundation//DTD Struts Configuration 2.0//EN"
       "http://struts.apache.org/dtds/struts-2.0.dtd">

   <struts>
   	<package name="helloWorld" extends="struts-default"> 
   		<action name="hello" class="com.vole.action.HelloWorld">
   			<result name="success">helloWorld.jsp</result>
   		</action>
   	</package>
      

   </struts>
   ```

3. 实现 Action 接口：

   ```java
   import com.opensymphony.xwork2.Action;

   public class HelloWorld implements Action {

   	@Override
   	public String execute() throws Exception {
   		System.out.println("执行了 Action 方法");
   		return SUCCESS;
   	}
   }
   ```

   4. 在以后的开发中，一般不用实现 Action 接口，而是 继承 ActionSupport 类，因为该类实现了多种接口，具有很强的扩展性。

   5. Action 设置数据：

      第一种方式：属性驱动(FieldDriven)  A、基本数据类型属性 B、JavaBean 类型属性（**一般用这种**）
      第二种方式：模型驱动(ModelDriven)

      ```java
       //属性驱动--->基本数据类型属性
      public class UserAction extends ActionSupport{

      	private static final long serialVersionUID = 1L;

      	private String username;
      	private String password;
      	private User user;

      	public String getUsername() {
      		return username;
      	}

      	public void setUsername(String username) {
      		this.username = username;
      	}

      	public String getPassword() {
      		return password;
      	}

      	public void setPassword(String password) {
      		this.password = password;
      	}
      	
      	@Override
      	public String execute() throws Exception {
      		user = new User();
      		user.setPassword(password);
      		user.setUsername(username);
      		System.out.println(username+password);
      		if(LoginService.loginUser(user)){
      			return SUCCESS;
      		}else{
      			return ERROR;
      		}
      	}
      }
      ```


```java
  //属性驱动--->JavaBean 类型属性
  public class UserAction2 extends ActionSupport{

  	private static final long serialVersionUID = 1L;

  	private User user;

  	public User getUser() {
  		return user;
  	}

  	public void setUser(User user) {
  		this.user = user;
  	}

  	@Override
  	public String execute() throws Exception {
  		System.out.println("执行了 Action 方法");
  		if(LoginService.loginUser(user)){
  			return SUCCESS;
  		}else{
  			return ERROR;
  		}
  	}
  }
  
  //jsp
    <form action="user2" method="post">
		用户名：<input type="text" name="user.username" /> 
		密码：<input type="text" name="user.password" /> 
		<input type="submit" value="登录2" />
	</form>
```

```java
  //模型驱动(ModelDriven)
  public class UserAction3 extends ActionSupport implements ModelDriven<User>{

  	private static final long serialVersionUID = 1L;
  	
  	private UserService userService=new UserService();
  	
  	private User user=new User();
  	
  	@Override
  	public String execute() throws Exception {
  		System.out.println("执行了UserAction3的默认方法");
  		if(userService.login(user)){
  			return SUCCESS;
  		}else{
  			return ERROR;
  		}
  	}

  	@Override
  	public User getModel() {
  		// TODO Auto-generated method stub
  		return user;
  	}

  }

```


6. Struts2 处理传入多个值:

     a. 处理数目不定的字符串:

```java
         
         //java
         public class hobby extends ActionSupport{

         	private static final long serialVersionUID = 1L;
         	
         	private String[] hobby;
         	
         	public String[] getHobby() {
         		return hobby;
         	}

         	public void setHobby(String[] hobby) {
         		this.hobby = hobby;
         	}

         	@Override
         	public String execute() throws Exception {
         		if(hobby!=null){
         			for (String string : hobby) {
         				System.out.println(string);
         			}
         		}
         		return SUCCESS;
         	}
         }

          //jsp
           <form action="hobby" method="post">
           	爱好：
           	<input type="checkbox" name="hobby" value="唱歌"/>唱歌
           	<input type="checkbox" name="hobby" value="跳舞"/>跳舞
           	<input type="checkbox" name="hobby" value="睡觉"/>睡觉
           	<input type="checkbox" name="hobby" value="玩CF"/>玩CF
           	<input type="submit" value="提交"/>
           </form>
           
```



​      b.处理数目不定的 JavaBean 对


```java
 public class AddStudent extends ActionSupport{
  	private static final long serialVersionUID = 1L;
  	private List<Student> students;

  	public List<Student> getStudents() {
  		return students;
  	}

  	public void setStudents(List<Student> students) {
  		this.students = students;
  	}

  	@Override
  	public String execute() throws Exception {
  		for (Student s : students) {
  			System.out.println(s);
  		}
  		return super.execute();
  	}
  }

  //jsp
  <form action="student" method="post">
  	<table>
  		<tr>
  			<th>姓名</th>
  			<th>性别</th>
  			<th>年龄</th>
  		</tr>
  		<tr>
  			<td><input type="text" name="students[0].name"/></td>
  			<td><input type="text" name="students[0].sex"/></td>
  			<td><input type="text" name="students[0].age"/></td>
  		</tr>
  		<tr>
  			<td><input type="text" name="students[1].name"/></td>
  			<td><input type="text" name="students[1].sex"/></td>
  			<td><input type="text" name="students[1].age"/></td>
  		</tr>
  		<tr>
  			<td colspan="3">
  				<input type="submit" value="提交"/>
  			</td>
  		</tr>
  	</table>
  </form>
```


7. 使用通配符:

```java
      //struts.xml 文件
      <package name="manage" namespace="/" extends="struts-default">
      <action name="student_*" class="com.vole.action.StudentManage"method="{1}">
      		<result name="success">success.jsp</result>
      </action>

      //StudentManage.java
      public class StudentManage extends ActionSupport {

      	private static final long serialVersionUID = 1L;
      	private String name;

      	public String getName() {
      		return name;
      	}

      	public void setName(String name) {
      		this.name = name;
      	}

      	public String list() throws Exception {
      		System.out.println("学生查找");
      		name = "学生查找";
      		return SUCCESS;
      	}

      	public String add() throws Exception {
      		System.out.println("学生添加");
      		name = "学生添加";
      		return SUCCESS;
      	}

      	public String update() throws Exception {
      		System.out.println("学生修改");
      		name = "学生修改";
      		return SUCCESS;
      	}

      	public String delete() throws Exception {
      		System.out.println("学生删除");
      		name = "学生删除";
      		return SUCCESS;
      	}
      }
      //indext.jsp
      <a href="student_list" target="_blank">学生信息查询</a>&nbsp;
      <a href="student_add" target="_blank">学生信息添加</a>&nbsp;
      <a href="student_update" target="_blank">学生信息修改</a>&nbsp;
      <a href="student_delete" target="_blank">学生信息删除</a>&nbsp;
```



8. Action  生命周期: 每次请求都会生成一个实例。



9. result  配置: 
     * type 默认是 dispatcher 内部转发；能带值
     * type 为 redirect 重定向；不能带值
     * type 为 chain 链条；能带值
     * type 为 redirectAction 重定向到 action；不能带值
       undefined拦截器（interceptor）


```java
//struts.xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE struts PUBLIC
    "-//Apache Software Foundation//DTD Struts Configuration 2.0//EN"
    "http://struts.apache.org/dtds/struts-2.0.dtd">

<struts>
	<!--开启动态方法调用 -->
	<constant name="struts.enable.DynamicMethodInvocation" value="true" />

	<package name="helloWorld" namespace="/" extends="struts-default">

		<!-- 拦截器配置 -->
		<interceptors>
			<interceptor name="myInterceptor" class="com.vole.interceptor.MyInterceptor">			</interceptor>
			<interceptor name="loginInterceptor" class="com.vole.interceptor.LoginInterceptor"></interceptor>

			<interceptor-stack name="myStack">
				<interceptor-ref name="loginInterceptor"></interceptor-ref>
				<interceptor-ref name="defaultStack"></interceptor-ref>
			</interceptor-stack>
		</interceptors>

		<default-interceptor-ref name="myStack"></default-interceptor-ref>

		<!-- 全局错误配置 -->
		<global-results>
			<result name="error">error.jsp</result>
		</global-results>

		<action name="hello" class="com.vole.action.HelloWorld">
			<result name="success">helloWorld.jsp</result>
			<!-- 拦截器配置 -->
			<interceptor-ref name="myInterceptor"></interceptor-ref>
			<interceptor-ref name="defaultStack"></interceptor-ref>
		</action>

		<action name="user3" class="com.vole.action.UserAction3">
			<result name="success">success.jsp</result>
			<!-- 拦截器配置 -->
			<interceptor-ref name="defaultStack"></interceptor-ref>
		</action>

		<action name="gril" class="com.vole.action.GrilAction">
			<result name="success">success.jsp</result>
		</action>
	</package>

</struts>

//LoginInterceptor.java
public class LoginInterceptor implements Interceptor{

	private static final long serialVersionUID = 1L;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		System.out.println("MyInterceptor销毁");
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		System.out.println("MyInterceptor初始化");
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("在Action执行之前");
		ActionContext actionContext = invocation.getInvocationContext();
		Map<String, Object> session = actionContext.getSession();
		Object currentUser= session.get("currentUser");
		String result=null;
		if(currentUser!=null){
			result=invocation.invoke();
		}else{
			HttpServletRequest request = (HttpServletRequest) invocation.getInvocationContext()
					.get(ServletActionContext.HTTP_REQUEST);
			request.setAttribute("error", "请先登录！");
			result="error";
		}
		System.out.println("result:"+result);
		System.out.println("在Action执行之后");
		return result;
	}
}

//UserAction3.java
public class UserAction3 extends ActionSupport {

	private static final long serialVersionUID = 1L;

	private String error;
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@Override
	public String execute() throws Exception {
		System.out.println("执行了 Action 方法");
		System.out.println("user" + user);
		if (LoginService.loginUser(user)) {
			ActionContext actionContext = ActionContext.getContext();
			Map<String, Object> session = actionContext.getSession();
			session.put("currentUser", user);
			return SUCCESS;
		} else {
			this.error = "用户名或密码错误";
			return ERROR;
		}
	}
} 
```




11. 值栈与 OGNL

> 值栈是对应每个请求对象的一套内存数据的封装，Struts2 会给每个请求创建一个新的值栈。
> 值栈能够线程安全地为每个请求提供公共的数据存取服务。

> OGNL 是对象图导航语言 Object-Graph Navigation Language 的缩写，它是一种功能强大的表达式语言。
> OGNL 访问 ValueStack 数据
> <s:property value=”account” />

OGNL 访问 ActionContext 数据
访问某个范围下的数据要用#

\#parameters 请求参数 request.getParameter(...);

\#request 请求作用域中的数据 request.getAttribute(...);

\#session 会话作用域中的数据 session.getAttribute(...);

\#application 应用程序作用域中的数据 application.getAttribute(...);

\#attr 按照 page request session application 顺序查找值.

```java
//HelloAction.java
public class HelloAction extends ActionSupport{

	private static final long serialVersionUID = 1L;

	@Override
	public String execute() throws Exception {
		ActionContext actionContext = ActionContext.getContext();
		//获取狭义上带值栈
		ValueStack valueStack = actionContext.getValueStack();
		valueStack.set("name", "张三(valueStack)");
		valueStack.set("age", 11);
		
		Map<String, Object> session=actionContext.getSession();
		session.put("name", "王五(session)");
		session.put("age", 13);
		
		Map<String, Object> application=actionContext.getApplication();
		application.put("name", "赵六(application)");
		application.put("age", 14);
		return SUCCESS;
	}
}

```

```jsp
//请求地址：http://localhost:8080/Struts2Demo/hello?name=vole&age=25
//jsp
<%@taglib prefix="s" uri="/struts-tags"%>
<%
	request.setAttribute("name", "李四(request)");
	request.setAttribute("age", "12");
%>
<body>
	获取狭义上的值栈数据：
	<s:property value="name" />
	<s:property value="age" />
	<br /> 请求参数(parameters)：
	<s:property value="#parameters.name" />
	<s:property value="#parameters.age" />
	<br /> request：
	<s:property value="#request.name" />
	<s:property value="#request.age" />
	<br /> session：
	<s:property value="#session.name" />
	<s:property value="#session.age" />
	<br /> application：
	<s:property value="#application.name" />
	<s:property value="#application.age" />
	<br /> attr取值：
	<s:property value="#attr.name" />
	<s:property value="#attr.age" />
	<br />
</body>
```

```java
//OGNL  访问复杂对象

public class HelloAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	
	private Student student;
	private List<Student> students;
	private Map<String,Student> studentMap;

	public Map<String, Student> getStudentMap() {
		return studentMap;
	}

	public void setStudentMap(Map<String, Student> studentMap) {
		this.studentMap = studentMap;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	@Override
	public String execute() throws Exception {
		ActionContext actionContext = ActionContext.getContext();
      	//访问 javaBean 对象
		student = new Student("李七", 15);
      	//访问集合对象
		students = new ArrayList<Student>();
		students.add(new Student("老九",13));
		students.add(new Student("老十",14));
		//访问 Map 对象
		studentMap=new HashMap<String,Student>();
		studentMap.put("goodStudent", new Student("学霸",20));
		studentMap.put("badStudent", new Student("学渣",19));
		return SUCCESS;
		
	}
}

//MyStatic.java 静态属性与静态方法类
public class MyStatic {
	public static final String str="Struts 访问静态方法和属性权限"; 
	
	public static String printUrl(){
		System.out.println("http://www.java1234.com");
		return "http://www.java1234.com";
	}
}

//访问静态方法和属性时需要在 struts.xml 中加入下面的权
//<constant name="struts.ognl.allowStaticMethodAccess" value="true"></constant>

//jsp
	ognl访问javaBean对象：
	<s:property value="student.name" />
	<s:property value="student.age" />
	<br />ognl访问List集合：
	<s:property value="students[0].name" />
	<s:property value="students[0].age" />
	<br />
	<s:property value="students[1].name" />
	<s:property value="students[1].age" />
	<br /> ognl访问Map：
	<s:property value="studentMap['goodStudent'].name" />
	<s:property value="studentMap['goodStudent'].age" />
	<br />
	<s:property value="studentMap['badStudent'].name" />
	<s:property value="studentMap['badStudent'].age" />
	<br />访问静态方法和属性权限: 
	<s:property value="@com.vole.common.MyStatic@str"/>
	<s:property value="@com.vole.common.MyStatic@printUrl()"/>
```

12. 标签：大致的看了一遍，不很很熟悉，需要加强；

13. 国际化（Internationlization）

    * 在 struts.xml 中加入权限  :

      `<constant name="struts.custom.i18n.resources" value="vole"></constant>`

    * 创建 vole.properties  、vole_zh_CN.properties 、vole_en_US.properties 文件

      ```properties
      //vole.properties :是中文 Unicode 
      userName=\u7528\u6237\u540d
      password=\u5bc6\u7801
      login=\u767b\u5f55
      welcomeInfo=\u6b22\u8fce{0}

      //vole_zh_CN.properties ：是中文 Unicode 
      userName=\u7528\u6237\u540d
      password=\u5bc6\u7801
      login=\u767b\u5f55
      welcomeInfo=\u6b22\u8fce{0}

      //vole_en_US.properties ：就是英文
      userName=userName
      password=password
      login=login
      welcomeInfo=welcome{0}
      ```

    * 调用 ：`<s:text name=""></s:text>`

      ```jsp
      <s:text name="userName"></s:text>
      <s:text name="password"></s:text>
      <s:text name="login"></s:text>
      <s:text name="welcomeInfo">
      	<s:param>Vole</s:param>
      </s:text>
      ```

    ​

14. Struts2  验证框架: Struts2 基于 Struts2 拦截器，为开发者提供了一套易用的验证框架，并可扩展；一般的验证都支持


* 内置验证:

  14.1: 添加 RegisterAction-validation.xml 

```xml
  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <!DOCTYPE validators PUBLIC "-//Apache Struts//XWork Validator 1.0.2//EN" "http://struts.apache.org/dtds/xwork-validator-1.0.2.dtd">

  <validators>
  	<field name="user.userName">
  		<field-validator type="requiredstring">
  			<message>请输入用户名</message>
  		</field-validator>
  		<field-validator type="stringlength">
  			<param name="minLength">6</param>
  			<param name="maxLength">10</param>
  			<message>用户名必须在${minLength}和${maxLength}之间</message>
  		</field-validator>
  	</field>
  	<field name="user.name">
  		<field-validator type="requiredstring">
  			<message>请输入姓名</message>
  		</field-validator>
  	</field>
  	<field name="user.age">
  		<field-validator type="int">
  			<param name="min">18</param>
  			<message>年龄必须满18周岁</message>
  		</field-validator>
  	</field>
  	<field name="user.email">
  		<field-validator type="requiredstring">
  			<message>请输入邮件</message>
  		</field-validator>
  		<field-validator type="email">
  			<message>邮件格式不对</message>
  		</field-validator>
  	</field>
  	<field name="user.homePage">
  		<field-validator type="requiredstring">
  			<message>请输入主页</message>
  		</field-validator>
  		<field-validator type="url">
  			<message>主页格式不对</message>
  		</field-validator>
  	</field>
    <validator type="expression">
  		<param name="expression"><![CDATA[!user.name.equals(user.userName)]]></param>
  		<message>用户名和真实姓名不能相同</message>
  	</validator>
  </validators>
```



14.2: 创建 User.java model

```java
     public class User {
     	private String userName;
     	private String name;
     	private int age;
     	private String email;
     	private String homePage;

     	public String getUserName() {
     		return userName;
     	}

     	public void setUserName(String userName) {
     		this.userName = userName;
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

     	public String getEmail() {
     		return email;
     	}

     	public void setEmail(String email) {
     		this.email = email;
     	}

     	public String getHomePage() {
     		return homePage;
     	}

     	public void setHomePage(String homePage) {
     		this.homePage = homePage;
     	}

     	@Override
     	public String toString() {
     		return "User [userName=" + userName + ", name=" + name + ", age=" + age + ", email=" + email + ", homePage="
     				+ homePage + "]";
     	}

     }
```

​

14.3: 创建 RegisterAction.java 

```java

    public class RegisterAction extends ActionSupport {

    	private static final long serialVersionUID = 1L;

    	private User user;

    	public User getUser() {
    		return user;
    	}

    	public void setUser(User user) {
    		this.user = user;
    	}

    	@Override
    	public String execute() throws Exception {
    		System.out.println("user: "+user);
    		return SUCCESS;

    	}
    }
```
14.4: 创建 struts.xml

```xml
     <?xml version="1.0" encoding="UTF-8" ?>
     <!DOCTYPE struts PUBLIC
         "-//Apache Software Foundation//DTD Struts Configuration 2.0//EN"
         "http://struts.apache.org/dtds/struts-2.0.dtd">

     <struts>

     	<package name="manager" extends="struts-default">

     		<action name="registerAction" class="com.vole.action.RegisterAction">
     			<result name="input">/register.jsp</result>
     			<result name="success">/success.jsp</result>
     		</action>
     	</package>

     </struts>
```
14.5: 创建 register.jsp

```jsp
    <%@ page language="java" contentType="text/html; charset=UTF-8"
    	pageEncoding="UTF-8"%>
    <%@taglib prefix="s" uri="/struts-tags"%>
    <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
    <html lang="zh-CN">
    <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>注册</title>
    </head>
    <body>
    	<s:actionerror />
    	用户注册
    	<s:form action="/registerAction" method="post">
    		<s:textfield name="user.userName" label="用户名"></s:textfield>
    		<s:textfield name="user.name" label="真实姓名"></s:textfield>
    		<s:textfield name="user.age" label="年龄"></s:textfield>
    		<s:textfield name="user.email" label="邮件"></s:textfield>
    		<s:textfield name="user.homePage" label="主页"></s:textfield>
    		<s:submit value="注册"></s:submit>
    	</s:form>
    </body>
    </html>
```

* 自定义验证

  a. 创建 SensitiveWordValidators.java

  ```java
       public class SensitiveWordValidators extends FieldValidatorSupport {

       	@Override
       	public void validate(Object object) throws ValidationException {
       		String fieldName = this.getFieldName();
       		String value = this.getFieldValue(fieldName, object).toString();
       		if (!check(value)) {
       			this.addFieldError(fieldName, object);
       		}

       	}

       	public boolean check(String value) {
       		String sensitiveWords[] = { "操", "你妈" };
       		for (int i = 0; i < sensitiveWords.length; i++) {
       			if (value.indexOf(sensitiveWords[i]) > -1) {
       				return false;
       			}
       		}
       		return true;
       	}
       }
  ```



  2.  导入 validation.xml

      ```xml
           <?xml version="1.0" encoding="UTF-8"?>
           <!DOCTYPE validators PUBLIC
                   "-//Apache Struts//XWork Validator Config 1.0//EN"
                   "http://struts.apache.org/dtds/xwork-validator-config-1.0.dtd">
           <validators>
               <validator name="required" class="com.opensymphony.xwork2.validator.validators.RequiredFieldValidator"/>
               <validator name="requiredstring" class="com.opensymphony.xwork2.validator.validators.RequiredStringValidator"/>
               <validator name="int" class="com.opensymphony.xwork2.validator.validators.IntRangeFieldValidator"/>
               <validator name="double" class="com.opensymphony.xwork2.validator.validators.DoubleRangeFieldValidator"/>
               <validator name="date" class="com.opensymphony.xwork2.validator.validators.DateRangeFieldValidator"/>
               <validator name="expression" class="com.opensymphony.xwork2.validator.validators.ExpressionValidator"/>
               <validator name="fieldexpression" class="com.opensymphony.xwork2.validator.validators.FieldExpressionValidator"/>
               <validator name="email" class="com.opensymphony.xwork2.validator.validators.EmailValidator"/>
               <validator name="url" class="com.opensymphony.xwork2.validator.validators.URLValidator"/>
               <validator name="visitor" class="com.opensymphony.xwork2.validator.validators.VisitorFieldValidator"/>
               <validator name="conversion" class="com.opensymphony.xwork2.validator.validators.ConversionErrorFieldValidator"/>
               <validator name="stringlength" class="com.opensymphony.xwork2.validator.validators.StringLengthFieldValidator"/>
               <validator name="regex" class="com.opensymphony.xwork2.validator.validators.RegexFieldValidator"/>
               <validator name="conditionalvisitor" class="com.opensymphony.xwork2.validator.validators.ConditionalVisitorFieldValidator"/>
               
               <validator name="sensitive" class="com.vole.validators.SensitiveWordValidators"/>
           </validators>
      ```



3. 创建 ValidationAction.java

    ```java
        public class ValidationAction extends ActionSupport{

        	private static final long serialVersionUID = 1L;
        	
        	public String name;

        	public String getName() {
        		return name;
        	}

        	public void setName(String name) {
        		this.name = name;
        	}
        	
        	@Override
        	public String execute() throws Exception {
        		System.out.println("姓名："+name);
        		return SUCCESS;
        	}

        }
    ```



4. 创建 ValidationAction-validation.xml

    ```xml
        <?xml version="1.0" encoding="UTF-8"?>
        <!DOCTYPE validators PUBLIC "-//Apache Struts//XWork Validator 1.0.2//EN" "http://struts.apache.org/dtds/xwork-validator-1.0.2.dtd">

        <validators>
        	 <field name="name">
                <field-validator type="requiredstring">
                    <message>请输入姓名</message>
                </field-validator>
                
                <field-validator type="sensitive">
                    <message>有敏感词汇</message>
                </field-validator>
            </field>
        </validators>
    ```



5. 在 struts.xml 加入请求

   ```xml
    <action name="validationAction" class="com.vole.action.ValidationAction">
    			<result name="input">/validation.jsp</result>
    			<result name="success">/success.jsp</result>
    </action>
   ```



6. 创建 validation.jsp

    ```jsp
        <s:form action="/validationAction" method="post">
        		<s:textfield name="name" label="用户名"></s:textfield>
        		<s:submit value="提交"></s:submit>
        </s:form>
    ```



15. **struts2 文件上传**: 也是基于 Struts2 拦截器实现

    15.1: 创建 fileupload.jsp

    ```jsp
    <!-- 上传失败信息 -->
    <s:fielderror></s:fielderror>
    文件上传
    	<form action="fileuploadAction" method="post"
    		enctype="multipart/form-data">
    		<input type="file" name="vole" /> <input type="submit" value="提交" />
    	</form>
    ```

    15.2: 创建 FileuploadAction.java

    ```java
    public class FileuploadAction extends ActionSupport{

    	private static final long serialVersionUID = 1L;
    	
    	private File vole;
    	private String voleFileName; // 文件名
    	private String voleContentType; // 文件类型
    	public File getVole() {
    		return vole;
    	}
    	public void setVole(File vole) {
    		this.vole = vole;
    	}
    	public String getVoleFileName() {
    		return voleFileName;
    	}
    	public void setVoleFileName(String voleFileName) {
    		this.voleFileName = voleFileName;
    	}
    	public String getVoleContentType() {
    		return voleContentType;
    	}
    	public void setVoleContentType(String voleContentType) {
    		this.voleContentType = voleContentType;
    	}
    	
    	@Override
    	public String execute() throws Exception {
    		System.out.println("fileName"+voleFileName);
    		System.out.println("fileType"+voleContentType);
    		String filePath="F:/"+voleFileName;
    		File saveFile= new File(filePath);
    		FileUtils.copyFile(vole, saveFile);
    		return SUCCESS;
    	}
    }
    ```

    15.3 : 配置 struts.xml

    ```xml
    <!-- 大文件上传配置 默认 2M-->
    <constant name="struts.multipart.maxSize" value="20000000"></constant>
    	<package name="manager" extends="struts-default">
          
    		<action name="fileuploadAction" class="com.vole.action.FileuploadAction">
    			<result name="success">/success.jsp</result>
    			<result name="input">/fileupload.jsp</result>

    			<!-- 配置文件的大小及类型 -->
    			<!-- <interceptor-ref name="fileUpload"> <param name="allowedTypes">image/bmp,image/x-png,image/gif,image/jpg,image/jpeg</param> 
    				<param name="maximumSize">81101</param> </interceptor-ref> <interceptor-ref 
    				name="defaultStack"></interceptor-ref> -->
    		</action>

    	</package>
    ```

    15.4 ： 多文件上传----->把 文件、文件名和文件类型改为数组就行，然后在遍历；

    15.5 ：文件的下载------>主要是已二进制的方式下载

    15.5.1: struts.xml 配置

    ```xml
    <action name="download" class="com.vole.action.FileDownloadAction">
    			<result type="stream">
    				<param name="contentDisposition">attachment;filename=${fileName}</param>
    			</result>
    </action>
    ```

    15.5.2: FileDownloadAction.java

    ```java
    public class FileDownloadAction extends ActionSupport {

    	private static final long serialVersionUID = 1L;

    	private String fileName;

    	public String getFileName() throws Exception {
    		//防止文件名乱码
    		fileName = new String(fileName.getBytes(), "ISO8859-1");
    		return fileName;
    	}

    	public void setFileName(String voleFileName) {
    		this.fileName = voleFileName;
    	}
    	//文件下载用的是这个方法
    	public InputStream getInputStream() throws Exception {
    		File file = new File("F:/die.jpg");
    		this.fileName = "die.jpg";
    		return new FileInputStream(file);
    	}
    }
    ```

    15.5.3：filedownload.jsp

    ```jsp
    	<a href="download">文件下载</a>
    ```

16. **Struts2 防重复提交 **

    16.1:  ：使用<s:token/> 标签防重复提交

    * <s:token></s:token> ：加在 form 里；
    * 使用 token 拦截器：
      <interceptor-ref name="token"></interceptor-ref>
      <interceptor-ref name="defaultStack"></interceptor-ref>
      <result name="invalid.token">/student.jsp</result> ：在 struts.xml 里配置，假如出现重复提交，则直接回到页面；
    * <s:actionerror/>：在页面上显示错误信息；

    16.2: 使用 tokenSession  拦截器防重复提交 **一般使用这个方法**

    * tokenSesssion 拦截器直接无视重复提交的请求；
      <interceptor-ref name="tokenSession"></interceptor-ref>
      <interceptor-ref name="defaultStack"></interceptor-ref>

    ​