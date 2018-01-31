##### JSP(Java Server Page)

* 全局变量、方法、类  : `<%! 变量、方法、类 %>`

* 局部变量、编写语句: `<% 变量、语句 %>`

* 输出变量: `<%=变量%>`

* 包含指令:

  * 静态包含: `<%@include file="要包含的文件"%>`  ---包含 html 时中文乱码为解决
  * 动态包含: `<jsp:include page="要包含的文件">`  ----开发一般用**动态包含**

* Jsp 跳转指令:

  ```jsp
  <!--demo1.jsp-->
  <jsp:forward>
  	<jsp:param value=”wuvole” name=”usernamae”>
  </jsp:forward>
    
   <!--demo2.jsp-->
    user:<%=request.getParameter("username")%>
  ```

  > 加载类`<%@ page import="javax.servlet.http.*"%> `

* 九大内置对象 :**(pageContext, request, response, session, application, config, out, page, exception)**

* 四大作用域: **page, request, session, application**


| 范围          | 说明                    |
| :---------- | --------------------- |
| Page        | 只在一个页面保存数据            |
| Request     | 只在一个请求保存数据            |
| Session     | 在一次会话范围中保存数据，仅供单个用户使用 |
| Application | 在整个服务器上保存数据，所有用户共享    |

* **response 对象**
  1. 自动刷新页面  -------`response.setStatus(1);`
  2. 页面重定向应用 ------- `response.sendRedirect("Demo.jsp");`
  3. 操作 cookie 应用 post get 方法比较 post 放数据包里 get 放 Url 后面 get 数据量小，不安全
  4. cookie 和 session 的比较 cookie 信息是存客户端的，session 信息是存服务器的

