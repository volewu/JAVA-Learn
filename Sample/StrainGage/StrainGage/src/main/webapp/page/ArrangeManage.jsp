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

function openModifyDiglog(){
	var selectRows=$("#dg").datagrid("getSelections");
	if(selectRows.length!=1){
		$.messager.alert("系统提示","请选择一条要编辑的数据！");
		return;
	}
	var row=selectRows[0];
	$("#dlg").dialog("open").dialog("setTitle","编辑排配信息");
	$("#fm").form("load",row);
}

function resetValue(){
	$("#starttime1").datetimebox('setValue', '');
	$("#endtime1").datetimebox('setValue', '');
}

function closeDialog(){
	$("#dlg").dialog("close");
	resetValue();
}
	
function saveData(){
	if ($('#handlerstate').combobox("getValue")=="处理结束") {
	 $.messager.confirm("系统提示","您确认已经<font color='red'>处理结束</font>吗？<br/> 确认后本条记录将删除！",function(r){
		if (r) {
			$("#fm").form("submit",{
				url:'${pageContext.request.contextPath}/arrange/update.do',
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
		}else {
			return null;
		}
		
	})  
	
  }else {
	  $("#fm").form("submit",{
			url:'${pageContext.request.contextPath}/arrange/update.do',
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
					if (value == '待处理'){
						return 'background-color:red;color:black;';
					}if (value == '处理中') {
						return 'background-color:yellow;color:black;';
					}if (value == '处理结束') {
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
  url="${pageContext.request.contextPath}/arrange/list.do" fit="true" toolbar="#tb">
</table>

<div id="tb">
 <div>
	<a href="javascript:openModifyDiglog()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">编辑</a>
 </div>
 </div>
 
 <div id="dlg" class="easyui-dialog" style="width: 720px;height: 200px;padding: 10px 20px" closed="true" buttons="#dlg-buttons">
 
 	<form id="fm" method="post">
 		<table cellpadding="8px">
 			<tr>
 				<td>预计开始处理时间：</td>
 				<td>
 					<input type="text" id="starttime" name="starttime" class="easyui-datetimebox" required="true" showSeconds="false"/>
 				</td>
 				<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
 				<td>预计结束处理时间：</td>
 				<td>
 					<input type="text" id="endtime" name="endtime" class="easyui-datetimebox" required="true" showSeconds="false"/>
 				</td>
 			</tr>
 			<tr style="display: none;">
 				<td> <input type="hidden" id="applybu" name="applybu"/></td>
 				<td>
 					<input type="hidden" id="applydate" name="applydate"/>
 				</td>
 				<td><input type="hidden" id="projectname" name="projectname"/></td>
 				<td></td>
 				<td></td>
 			</tr>
 			
 			<tr>	
 				<td>处理状态</td>
 				<td>
 					<select class="easyui-combobox" id="handlerstate" name="handlerstate" panelHeight="auto" editable="false" style="width: 155px">
			  	   		<option value="待处理">待处理</option>
			  	   		<option value="处理中">处理中</option>
			  	   		<option value="处理结束">处理结束</option>
			  	   </select>
 				</td>
 				<td></td>
 				<td>
 					<input type="hidden" id="arrid" name="arrid" class="easyui-validatebox" required="true" />
 				</td>
 			</tr>
 		</table>
 	</form>
 
</div>

<div id="dlg-buttons">
	<a href="javascript:saveData()" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
	<a href="javascript:closeDialog()" class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
</div>
</body>
</html>