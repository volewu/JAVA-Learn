<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>账号申请-页面</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.5.1/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.5.1/themes/icon.css">
<link href="${pageContext.request.contextPath}/static/bootstrap/css/bootstrap.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/static/bootstrap/css/bootstrap-responsive.css" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/bootstrap/css/bootstrap-theme.min.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.5.1/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.5.1/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.5.1/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
	
	
	function findKjZg() {
		$("#dlg").dialog("open").dialog("setTitle","查找主管");
	}
	function formatAction(val,row,rowIndex){
		   
		   return "<a href='javascript:select(\""+row.firstName+"\")'>选择</a>";
	}
		
	function select(firstName){
		$('#kjzgip').val(firstName);
		$("#dlg").dialog("close");
	}

	
	function findBjZg() {
		$("#dlg1").dialog("open").dialog("setTitle","查找主管");
	}
	function formatAction1(val,row,rowIndex){
		   
		   return "<a href='javascript:select1(\""+row.firstName+"\")'>选择</a>";  //转义字符"
	}
		
	function select1(firstName){
		$('#bjzgip').val(firstName);
		$("#dlg1").dialog("close");
	}

 	function searchKjZg(){
		$("#dg").datagrid('load',{
			"kjname":$("#kjname").val()
		});
	}
	
	function searchBjZg(){
		$("#dg1").datagrid('load',{
			"bjname":$("#bjname").val()
		});
	} 
	
	function openAddKjUserDiglog() {
		$("#dlg2").dialog("open").dialog("setTitle","添加签核人");
	}
	
	function openAddBjUserDiglog() {
		$("#dlg3").dialog("open").dialog("setTitle","添加签核人");
	}
	
	function addKjUser(){
		$("#addKj").form("submit",{
			url:'${pageContext.request.contextPath}/apply/addKjZg.do',
			onSubmit:function(){
				return $(this).form("validate");
			},
			success:function(result){
				var result=eval('('+result+')');
				if(result.success){
					$.messager.alert("系统提示","添加成功！");
					
					$("#dlg2").dialog("close");
					$("#dg").datagrid("reload");
				}else{
					$.messager.alert("系统提示","保存失败！");
					return;
				}
			}
		});
	}
	
	function addBjUser(){
		$("#addBj").form("submit",{
			url:'${pageContext.request.contextPath}/apply/addBjZg.do',
			onSubmit:function(){
				return $(this).form("validate");
			},
			success:function(result){
				var result=eval('('+result+')');
				if(result.success){
					$.messager.alert("系统提示","添加成功！");
					
					$("#dlg3").dialog("close");
					$("#dg1").datagrid("reload");
				}else{
					$.messager.alert("系统提示","保存失败！");
					return;
				}
			}
		});
	}
	
	
	function applyAccount(){
		$("#fm").form("submit",{
			url:'${pageContext.request.contextPath}/apply/applyAccount.do',
			onSubmit:function(){
				if ($("#appUserName").val()=='' || $("#appUserName").val()==null) {
					return false;
				}if ($("#appUserName2").val()=='' || $("#appUserName2").val()==null) {
					return false;
				}if ($("#appPassword").val()=='' || $("#appPassword").val()==null) {
					return false;
				}if ($("#appEmail").val()=='' || $("#appEmail").val()==null) {
					return false;
				}if ($("#groupId").combobox('getValue')=='-1') {
					$.messager.alert("系统提示","请选择角色！");
					return false;
				}
				if ($("#groupId").combobox('getValue')=='gcs') {
					
					if ($("#kjzgip").val()=='' || $("#kjzgip").val()==null) {
						$.messager.alert("系统提示","课级主管没有选择！");
						return false;
					}
					if ($("#bjzgip").val()=='' || $("#bjzgip").val()==null) {
						$.messager.alert("系统提示","部级主管没有选择！");
						return false;
					}
				}if ($("#groupId").combobox('getValue')=='kjzg') {
					if ($("#bjzgip").val()=='' || $("#bjzgip").val()==null) {
						$.messager.alert("系统提示","部级主管没有选择！");
						return false;
					}
				}
				
				return $(this).form("validate");
			},
			success:function(result){
				var result=eval('('+result+')');
				if(result.success){
					$.messager.alert("系统提示","添加成功，等待核准人审核。");
					resutReset();
				}else{
					$.messager.alert("系统提示","保存失败！");
					return;
				}
			}
		});
	}
	
	
	function resutReset(){
		$("#appUserName").val('');
		$("#appUserName2").val('');
		$("#appPassword").val('');
		$("#appEmail").val('');
		$("#groupId").combobox('setValue','-1');
		$("#kjzgip").val('');
		$("#bjzgip").val('');
	}
	
</script>
<style type="text/css">
  font {
	color: #333333;
}
 h4 {
    color: #333333;
    text-align: center;
    
}
</style>
<body>
  <div style="margin-left: 20px;">
	<form id="fm"  method="post">
		<table cellpadding="10px" style="margin-top: 6px">
			<tr>
				<td colspan="2"><h4>申请账号</h4></td>
			</tr>
			<tr>
				<td width="80px"><font size="4">工号:</font></td>
				<td>
					<input type="text" id="appUserName" name="appUserName" class="easyui-validatebox" required="true" style="width: 294px;height: 28px;" placeholder="工号"/>
				</td>
			</tr>
			<tr>
				<td width="80px"><font size="4">用户名:</font></td>
				<td>
					<input type="text" id="appUserName2" name="appUserName2" class="easyui-validatebox" required="true" style="width: 294px;height: 28px;" placeholder="用户名"/>
				</td>
			</tr>
			<tr>
				<td><font size="4">密码:</font></td>
				<td>
					<input type="password" id="appPassword" name="appPassword" class="easyui-validatebox" required="true" style="width: 294px;height: 28px;" placeholder="密码"/>
				</td>
			</tr>
			<tr>
				<td><font size="4">邮箱:</font></td>
				<td>
					<input type="text" id="appEmail" name="appEmail" class="easyui-validatebox" required="true" style="width: 294px;height: 28px;" placeholder="邮箱"/>
				</td>
			</tr>
			<tr>
				<td><font size="4">角色:</font></td>
				<td>
					<input  id="groupId" style="width: 304px;height: 40px; -webkit-border-radius:5px;-moz-border-radius:5px; border-radius: 5px;" name="groupId" class="easyui-combobox" editable="false" data-options="panelHeight:'auto',valueField:'id',textField:'name',url:'${pageContext.request.contextPath}/group/groupComboList.do',onChange: function(newValue,oldValue) {
					 if(newValue=='kjzg'){
						 $('#kjtr').css('display','none');
						 $('#bjtr').css('display','');
						 $('#kjzgip').removeattr('required');
					     $('#bjzgip').attr('required','true');
					 }if(newValue=='bjzg'){
						 $('#kjtr').css('display','none');
						 $('#bjtr').css('display','none');
						 $('#kjzgip').removeattr('required');
					     $('#bjzgip').removeattr('required');
					 }if(newValue=='gcs'){
						 $('#kjtr').css('display','');
						 $('#bjtr').css('display','');
						 $('#kjzgip').attr('required','true');
					     $('#bjzgip').attr('required','true');
					 }if(newValue=='-1'){
						 $('#kjtr').css('display','');
						 $('#bjtr').css('display','');
						 $('#kjzgip').attr('required','true');
					     $('#bjzgip').attr('required','true');
					 }
	   			 }" value="-1"/>
				</td>
			</tr>
			<tr id="kjtr">
				<td ><font size="4">课级主管:</font></td>
				<td><input type="text" id="kjzgip" name="kjzgip" class="easyui-validatebox" editable="false" style="width: 240px;height: 28px;" placeholder="请选择课级主管"/><button type="button" id="select1" style="height: 38px;margin-bottom: 10px;" onclick="findKjZg()" class="btn btn-default">选择</button></td>
			</tr>
			<tr id="bjtr">
				<td><font size="4">部级主管:</font></td>
				<td>
				<input type="text" id="bjzgip" name="bjzgip" class="easyui-validatebox" editable="false" style="width: 240px;height: 28px;" placeholder="请选择部级主管"/><button type="button" id="select2" style="height: 38px;margin-bottom: 10px;"  onclick="findBjZg()" class="btn btn-default">选择</button>
		   
			</td>	
			</tr>
			<tr >
				<td></td>
				<td>
					<button type="button" class="btn btn-primary" style="width: 304px;height: 35px;" onclick="applyAccount()">申请</button>
				</td>
			</tr>
		</table>
	</form>
	</div>
 <div id="dlg" class="easyui-dialog" style="width: 850px;height: 450px;padding: 4px;" closed="true" >
 <table id="dg" title="课级主管列表" class="easyui-datagrid"
  fitColumns="true" pagination="true" rownumbers="true"
  url="${pageContext.request.contextPath}/user/selectKjZg.do" fit="true" toolbar="#tb">
 <thead>
 	<tr>
 		<th field="id" width="100" align="center">工号</th>
 		<th field="firstName" width="100" align="center">姓名</th>
 		<th field="rev" width="100" align="center">标记</th>
 		<th field="email" width="100" align="center">邮箱</th>
 		<th field="action" width="100" align="center" formatter="formatAction">操作</th>
 	</tr>
 </thead>
</table>
</div>

<div id="tb">
<div>
 	&nbsp;工号：&nbsp;<input type="text" id="kjname" size="20" onkeydown="if(event.keyCode==13) searchKjZg()"/>
 	<button class="btn btn-primary" onclick="searchKjZg()" >搜索</button>&nbsp;&nbsp;
 	<button class="btn btn-primary" onclick="openAddKjUserDiglog()" > 添加签核人</button>
 	
 </div>
</div>

 <div id="dlg1" class="easyui-dialog" style="width: 850px;height: 450px;padding: 4px;" closed="true" >
 <table id="dg1" title="部级主管列表" class="easyui-datagrid"
  fitColumns="true" pagination="true" rownumbers="true"
  url="${pageContext.request.contextPath}/user/selectBjZg.do" fit="true" toolbar="#tb1">
 <thead>
 	<tr>
 		<th field="id" width="100" align="center">工号</th>
 		<th field="firstName" width="100" align="center">姓名</th>
 		<th field="rev" width="100" align="center">标记</th>
 		<th field="email" width="100" align="center">邮箱</th>
 		<th field="action" width="100" align="center" formatter="formatAction1">操作</th>
 	</tr>
 </thead>
</table>
</div>

<div id="tb1">
<div>
 	&nbsp;工号：&nbsp;<input type="text" id="bjname" size="20" onkeydown="if(event.keyCode==13) searchBjZg()"/>
 	<button class="btn btn-primary" onclick="searchBjZg()" >搜索</button>&nbsp;&nbsp;
 	<button class="btn btn-primary" onclick="openAddBjUserDiglog()"> 添加签核人</button>
 </div>
</div>

<!-- 添加课级主管对话框 -->
<div id="dlg2" class="easyui-dialog" style="width: 550px;height: 300px;padding: 4px;" closed="true" >
	<form id="addKj" action="${pageContext.request.contextPath}/user/addKjZg.do" method="post">
 	<table>
 		<tr>
 			<td>工号：</td>
 			<td><input type="text" id="jobnumber" name="jobnumber" class="easyui-validatebox" required="true"  placeholder="工号"/></td>
 		</tr>
 		<tr>
 			<td>用户名：</td>
 			<td><input type="text" id="userName" name="userName" class="easyui-validatebox" required="true" placeholder="用户名"/></td>
 		</tr>
 		<tr>
 			<td>邮箱：</td>
 			<td><input type="email" id="email" name="email" class="easyui-validatebox" required="true" placeholder="邮箱"/></td>
 		</tr>
 		<tr>
 			<td></td>
 			<td><button type="button" style="width: 220px;"  class="btn btn-primary" onclick="addKjUser()">添加</button>&nbsp;</td>
 		</tr>
 	</table>
  </form>	
</div>


<!-- 添加部级主管对话框 -->
<div id="dlg3" class="easyui-dialog" style="width: 550px;height: 300px;padding: 4px;" closed="true" >
	<form id="addBj" action="${pageContext.request.contextPath}/user/addBjZg.do" method="post">
 	<table>
 		<tr>
 			<td>工号：</td>
 			<td><input type="text" id="jobnumber" name="jobnumber" class="easyui-validatebox" required="true"  placeholder="工号"/></td>
 		</tr>
 		<tr>
 			<td>用户名：</td>
 			<td><input type="text" id="userName" name="userName" class="easyui-validatebox" required="true" placeholder="用户名"/></td>
 		</tr>
 		<tr>
 			<td>邮箱：</td>
 			<td><input type="email" id="email" name="email" class="easyui-validatebox" required="true" placeholder="邮箱"/></td>
 		</tr>
 		<tr>
 			<td></td>
 			<td><button type="button" style="width: 220px;"  class="btn btn-primary" onclick="addBjUser()">添加</button>&nbsp;</td>
 		</tr>
 	</table>
  </form>	
</div>
</body>
</html>