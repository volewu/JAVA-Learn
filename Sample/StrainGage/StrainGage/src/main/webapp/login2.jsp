<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>LOGIN</title>
<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<!-- Custom Theme files -->
<link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css" media="all"/>
<!-- for-mobile-apps -->
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
<script src="${pageContext.request.contextPath}/static/jquery-easyui-1.5.1/jquery.easyui.min.js"></script>
<script type="text/javascript">
function submitData(){
	$("#fm").form("submit",{
		url:"${pageContext.request.contextPath}/user/login.do",
		onSubmit:function(){
			 if($("#userName").val()==null || $("#userName").val()==''){
				alert("请填写用户名！");
				return false;
			}
			if($("#password").val()==null || $("#password").val()==''){
					alert("请填写密码！");
					return false;
				}
			return $(this).form("validate");
		},
		success:function(result){
			var result=eval('('+result+')');
			if(result.success){
				window.location.href="${pageContext.request.contextPath}/main.jsp";
			}else{
				alert(result.errorInfo);
				return;
			}
		}
	});
}
</script>
</head>
<body>
<!--header start here-->
<div class="header">
		<div class="header-main">
		       <h1>StrainGage Manage System</h1>
			<div class="header-bottom">
				<div class="header-right w3agile">
					
					<div class="header-left-bottom agileinfo">
						
					 <form id="fm" action="#" method="post">
						<input type="text"  value="User name" name="userName" id="userName" onfocus="this.value = '';" onblur="if (this.value == '') {this.value = 'User name';}"/>
					    <input type="password"  value="Password" name="password" id="password" onfocus="this.value = '';" onblur="if (this.value == '') {this.value = 'password';}"/>
						<div class="remember">
						 <div class="forgot">
						 	<%-- <h6><a href="${pageContext.request.contextPath}/page/accountManage.jsp" target="_blank">Register?</a></h6> --%>
						 </div>
						<div class="clear"> </div>
					  </div>
					   
						<input type="button" onclick="submitData()" value="Login">
					</form>	
				</div>
				</div>
			  
			</div>
		</div>
</div>
</div>
</body>
</html>