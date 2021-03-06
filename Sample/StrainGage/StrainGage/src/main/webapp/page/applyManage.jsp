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

	function formatAction(val,row){
		if(row.STATE=='未提交'){
			return "<a href='javascript:startApply("+row.STRAINID+","+row.ZG+")'>提交申请</a>";
		}else if(row.STATE=='审核通过' || row.STATE=='审核未通过'){
			return "<a href='javascript:openListCommentDialog("+row.PROCESSINSTANCEID+")'>查看历史批注</a>&nbsp;<a target='_blank' href='history.jsp?STRAINID="+row.STRAINID+"&processInstanceId="+row.PROCESSINSTANCEID+"'>签核详情</a>";
		}
	}
	
	function openListCommentDialog(processInstanceId){
		$("#dg2").datagrid("load",{
			processInstanceId:processInstanceId
		});
		$("#dlg2").dialog("open").dialog("setTitle","查看历史批注");
	}
	
	function openAddTabs(){
		 window.parent.openTab('StrainGage申请表','StrainGageApply.jsp','icon-task');
	}
	
	function saveLeave(){
		$("#fm").form("submit",{
			url:'${pageContext.request.contextPath}/leave/save.do',
			onSubmit:function(){
				return $(this).form("validate");
			},
			success:function(result){
				var result=eval('('+result+')');
				if(result.success){
					$.messager.alert("系统系统","保存成功！");
					resetValue();
					$("#dlg").dialog("close");
					$("#dg").datagrid("reload");
				}else{
					$.messager.alert("系统系统","保存失败！");
					return;
				}
			}
		});
	}

	
	function resetValue(){
		$("#leaveDays").val("");
		$("#leaveReason").val("");
	}
	
	function closeLeaveDialog(){
		$("#dlg").dialog("close");
		resetValue();
	}
	
	function startApply(STRAINID,ZG){
		$.post("${pageContext.request.contextPath}/straingage/startApply.do",{STRAINID:STRAINID,ZG:ZG},function(result){
			if(result.success){
				$.messager.alert("系统系统","请假申请提交成功，目前审核中，请耐心等待！");
				$("#dg").datagrid("reload");
			}else{
				$.messager.alert("系统系统","请假申请提交失败，请联系管理员！");
			}
		},"json");
	}
	
</script>
</head>
<body style="margin: 1px">
<table id="dg" title="申请管理" class="easyui-datagrid"
  fitColumns="true" pagination="true" rownumbers="true"
  url="${pageContext.request.contextPath}/straingage/list.do" fit="true" toolbar="#tb">
 <thead>
 	<tr>
 		<th field="cb" checkbox="true" align="center"></th>
 		<th field="STRAINID" width="30" align="center">编号</th>
 		<th field="BU" width="50" align="center">申请部门</th>
 		<th field="APPLYDATE" width="50" align="center">申请日期</th>
 		<th field="ZG" width="50" align="center" hidden="true">主管</th>
 		<th field="PROJECTNAME" width="50" align="center">项目名称</th>
 		<th field="STATE" width="50" align="center">审核状态</th>
 		<th field="PROCESSINSTANCEID" width="50" hidden="true" align="center">流程实例Id</th>
 		<th field="action" width="50" align="center" formatter="formatAction">操作</th>
 	</tr>
 </thead>
</table>
<div id="tb">
 <div>
	<a href="javascript:openAddTabs()" class="easyui-linkbutton" iconCls="icon-add" plain="true">新增申请单</a>
 </div>
</div>



<div id="dlg2" class="easyui-dialog" style="width: 750px;height: 250px;padding: 4px" closed="true" >
 
 	<table id="dg2" title="批注列表" class="easyui-datagrid"
  fitColumns="true"  
  url="${pageContext.request.contextPath}/task/listHistoryCommentWithProcessInstanceId.do" style="width: 100%;height: 100%;">
 <thead>
 	<tr>
 		<th field="time" width="120" align="center">批注时间</th>
 		<th field="userId" width="100" align="center">批注人</th>
 		<th field="message" width="200" align="center">批注信息</th>
 	</tr>
 </thead>
</table>
 
</div>

</body>
</html>