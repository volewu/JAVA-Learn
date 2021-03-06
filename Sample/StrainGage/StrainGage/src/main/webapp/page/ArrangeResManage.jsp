<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>StrainGage管理</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/jquery-easyui-1.5.1/themes/default/easyui.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/jquery-easyui-1.5.1/themes/icon.css">
<script src="${pageContext.request.contextPath}/static/jquery-easyui-1.5.1/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/static/jquery-easyui-1.5.1/jquery.easyui.min.js"></script>
<script src="${pageContext.request.contextPath}/static/jquery-easyui-1.5.1/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">

function searchTask(){
	$("#dg").datagrid('load',{
		"projectname":$("#projectname").val()
	});
}
	
$(document).ready(function () {
	$('#dg').datagrid({
		columns:[[
			
			{field:'cb',checkbox:'true'},
			{field:'arrid',title:'编号', width:50, align:'center'},
			{field:'applybu',title:'申请部门', width:100, align:'center'},
			{field:'applydate',title:'申请日期', width:100, align:'center'},
			{field:'projectname',title:'项目名称', width:100, align:'center'},
			{field:'starttime',title:'预计开始处理时间', width:100, align:'center'},
			{field:'endtime',title:'预计结束处理时间', width:100, align:'center'},
			
			{field:'handlerstate',title:'处理状态', width:100, align:'center',
				styler: function(value,row,index){
				if (value == '处理结束') {
						return 'background-color:green;color:black;';
					}
				}
			},
			
			
		]]
	});
	});	
	
</script>
</head>
<body style="margin: 1px">
<table id="dg" title="排配管理" class="easyui-datagrid"
  fitColumns="true" pagination="true" rownumbers="true"
  url="${pageContext.request.contextPath}/arrangeRes/list.do" fit="true" toolbar="#tb">
</table>

<div id="tb">
 <div>
 	&nbsp;项目名称&nbsp;<input type="text" id="projectname" size="20" onkeydown="if(event.keyCode==13) searchTask()"/>
 	<a href="javascript:searchTask()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
 </div>
</div>
 

</body>
</html>