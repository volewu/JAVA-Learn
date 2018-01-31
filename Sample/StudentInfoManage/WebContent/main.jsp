<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>学生信息管理系统界面</title>
<%
	if (session.getAttribute("currenUser") == null) {
		response.sendRedirect("index.jsp");
	}
%>

<link rel="stylesheet" type="text/css"
	href="easyui-1.3.3/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="easyui-1.3.3/themes/icon.css">
<script type="text/javascript" src="easyui-1.3.3/jquery.min.js"></script>
<script type="text/javascript" src="easyui-1.3.3/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
	$(function() {
		var treeData = [ {
			text : "资料",
			children : [ {
				text : "班级信息管理",
				attributes : {
					url : "gradeInfoManage.jsp"
				}
			}, {
				text : " 学生信息管理",
				attributes : {
					url : "StudentInfoManage.jsp"
				}
			} ]
		} ];

		// 实例化树菜单
		$("#tree").tree({
			data : treeData,
			lines : true,
			onClick : function(node) {
				if (node.attributes) {
					openTab(node.text, node.attributes.url);
				}
			}
		});

		// 新增Tab
		function openTab(text, url) {
			if ($("#tabs").tabs('exists', text)) {
				$("#tabs").tabs('select', text);
			} else {
				var content = "<iframe frameborder='0' scrolling='auto' style='width:100%;height:100%' src="
						+ url + "></iframe>"
				$("#tabs").tabs('add', {
					title : text,
					closable : true,
					content : content
				});
			}
		}

	});
</script>

</head>
<body class="easyui-layout">
	<div region="north" style="height: 80px; background-color: #E0EDFF">
		<div align="left" style="width: 80%; float: left">
			<img src="image/main.jpg">
		</div>
		<div style="padding-top: 50px; padding-right: 20px">
			当前用户:&nbsp;<font color="red">${currenUser.userName}</font>
		</div>
	</div>
	<div region="west" style="width: 150px" title="导航菜单" split="true">
		<ul id="tree"></ul>
	</div>
	<div region="center">
		<div class="easyui-tabs" fit="true" border="false" id="tabs">
			<div title="首页">
				<div align="center">
					<font size="10" color="red">欢迎使用系统</font>
				</div>
			</div>
		</div>
	</div>

	<div region="south" style="height: 25px" align="center">版权所有:wuvole@gmail.com</div>
</body>
</html>