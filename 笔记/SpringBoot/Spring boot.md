## Spring boot

##### 1. HelloWorld

> 在 Eclipse 上 安装 STS 插件，IDEA 不需要

```java
@RestController
public class HelloWorld {
	
	@RequestMapping("/helloworld")
	public String hello() {
		return "Sqring Boot helloWorld";
	}
```

> 然后直接启动 HelloWorldApplication.java 

##### 2.自定义属性

* application.properties

```properties
# 改变端口
server.port=8888
# 增加请求路径
server.context-path=/HelloWorld

# 自定义属性
helloWorld=spring boot \u4F60\u597D\u5440

mysql.jdbcName=com.mysql.jdbc.Driver
mysql.dbUrl=jdbc:mysql://localhost:3306/db_boot
mysql.userName=root
mysql.password=123456
```

* MysqlProperties.java

```java
/**
 * Mysql属性配置文件
 * @author Administrator
 *
 */
@Component
@ConfigurationProperties(prefix = "mysql")
public class MysqlProperties {

	private String jdbcName;
	private String dbUrl;
	private String userName;
	private String password;

	public String getJdbcName() {
		return jdbcName;
	}

	public void setJdbcName(String jdbcName) {
		this.jdbcName = jdbcName;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
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

* HelloWorld.java

```java
@RestController
public class HelloWorld {
	
	@Value("${helloWorld}")
	private String helloWorld;
	
	@Resource
	private MysqlProperties mysqlProperties;
	
	@RequestMapping("/helloworld")
	public String hello() {
		return helloWorld;
	}
	
	@RequestMapping("/showJdbc")
	public String showJdbc() {
		return "mysql.jdbcName:"+mysqlProperties.getJdbcName()+"<br/>"
				  +"mysql.dbUrl:"+mysqlProperties.getDbUrl()+"<br/>"
				  +"mysql.userName:"+mysqlProperties.getUserName()+"<br/>"
				  +"mysql.password:"+mysqlProperties.getPassword()+"<br/>";
	}

}
```

#####  2. SpringBoot 之  MVC 支持

> 导入 **freemarker** 模板，放入  templates 目录下

* helloWorld.ftl

```html
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
message: ${message}

</body>
</html>
```

* HelloWorldFreeMarker.java

```java
@Controller
@RequestMapping("/freemarker")
public class HelloWorldFreeMarker {

	@RequestMapping("/say")
	public ModelAndView freeMarker() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("message","spring boot");
		mav.setViewName("helloWorld");
		return mav;
	}
}
```

* @RestController  处理 ajax 请求

```java
@RestController
@RequestMapping("/ajax")
public class HelloWolrdAjaxController {

	@RequestMapping("/hello")
	public String say() {
		return "{‘vole’：‘wu’，‘gakki’：‘ji’}";
	}
	
}
```

* 第四节 ：@PathVariable  获取 url  参数 和 @RequestParam  获取请求参数

```java
@Controller
@RequestMapping("/blog")
public class BlogController {

	@RequestMapping("/{id}")
	public ModelAndView show(@PathVariable("id") Integer id) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("id",id);
		mav.setViewName("blog");
		return mav;
	}
	
	@RequestMapping("/query")
	public ModelAndView query(@RequestParam(value="q",required=false) String q) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("q",q);
		mav.setViewName("query");
		return mav;
	}

}
```

* blog.ftl  & query.ftl

```
id: ${id}

q:{q}
```

* index.html

```html
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="http://www.java1234.com/jquery-easyui-1.3.3/jquery.min.js"></script>
<script type="text/javascript">
	function show(){
		$.post("ajax/hello",{},function(result){
			alert(result)
		})
	}

</script>
</head>
<body>

<button onclick="show()">点我</button></br>
<a href="/HelloWorld/blog/21">Blog</a></br>
<a href="/HelloWorld/blog/query?q=123456">搜索</a>

</body>
</html>
```

##### 3. aop

```java
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
* @author vole
* @version 2018年4月11日 下午10:21:35
* 类说明: aop
*/
@Aspect
@Component
public class RequestAspect {
	
	private Logger logger = Logger.getLogger(RequestAspect.class);
	
	@Pointcut("execution(public * com.vole.controller.*.*(..))")
	public void log() {}
	
	@Before("log()")
	public void doBefore(JoinPoint joinpoint) {
		logger.info("方法执行前");
		ServletRequestAttributes sra=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request=sra.getRequest();
		logger.info("url:"+request.getRequestURI());
		logger.info("ip:"+request.getRemoteHost());
		logger.info("method:"+request.getMethod());
		logger.info("class_method:"+joinpoint.getSignature().getDeclaringTypeName()+"."+joinpoint.getSignature().getName());
		logger.info("args:"+joinpoint.getArgs());
	}
	
	@After("log()")
	public void doAfter(JoinPoint joinPoint){
		logger.info("方法执行后...");
	}
	
	@AfterReturning(returning="result",pointcut="log()")
	public void doAfterReturning(Object result){
		logger.info("方法返回值："+result);
	}
}
```

##### 4. 静态文件

```java
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 编写者： Wu
 * Time： 2018/4/11.17:02
 * 内容：配置静态资源映射
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter{

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //将所有/static/** 访问都映射到classpath:/static/ 目录下
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }
}
```

