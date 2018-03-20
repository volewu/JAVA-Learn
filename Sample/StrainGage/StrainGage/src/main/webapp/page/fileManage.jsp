<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>文件管理</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/jquery-easyui-1.5.1/themes/default/easyui.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/jquery-easyui-1.5.1/themes/icon.css">
<script src="${pageContext.request.contextPath}/static/jquery-easyui-1.5.1/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/static/jquery-easyui-1.5.1/jquery.easyui.min.js"></script>
<script src="${pageContext.request.contextPath}/static/jquery-easyui-1.5.1/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">

	var url;
	
	function searchFile(){
		$("#dg").datagrid('load',{
			"filename":$("#filename").val()
		});
	}
	
	function formatAction(val,row){
	   return "<a href='javascript:FileCheck("+row.fileid+")'>下载</a>";
	}
	
	function FileCheck(fileid){
		 $.post("${pageContext.request.contextPath}/file/fileCheck.do",{fileid:fileid},function(result){
			if(result.success){
				$.messager.alert("系统提示","文件已不存在！");
			}else{
				window.location.href="${pageContext.request.contextPath}/file/fileDownload.do?fileid="+fileid;
			}
		},"json"); 
	}
	
	function openAddDiglog(){
		$("#dlg").dialog("open").dialog("setTitle","上传文件");
		url="${pageContext.request.contextPath}/file/fileupload.do"
	}
	
	function saveFile(){
		$("#fm").form("submit",{
			url:url,
			onSubmit:function(){
				return $(this).form("validate");
			},
			success:function(result){
				var result=eval('('+result+')');
				if(result.success){
					$.messager.alert("系统提示","上传成功！");
					$("#dlg").dialog("close");
					$("#dg").datagrid("reload");
					 resetValue();
				}else{
					$.messager.alert("系统提示","上传失败！");
					return;
				}
			}
		});
	}
	
	function closeDialog(){
		$("#dlg").dialog("close");
		 resetValue();
	}
	
	function deleteFile(){
		var selectRows=$("#dg").datagrid("getSelections");
		if(selectRows.length==0){
			$.messager.alert("系统提示","请选择要删除的数据！");
			return;
		}
		var strIds=[];
		for(var i=0;i<selectRows.length;i++){
			strIds.push(selectRows[i].fileid);
		}
		var ids=strIds.join(",");
		$.messager.confirm("系统提示","您确定要删除这<font color=red>"+selectRows.length+"</font>条数据吗?",function(r){
			if(r){
				$.post("${pageContext.request.contextPath}/file/deletefile.do",{ids:ids},function(result){
					if(result.success){
						$.messager.alert("系统提示","文件已经成功删除！");
						$("#dg").datagrid("reload");
					}else{
						$.messager.alert("系统提示","文件删除失败！");
					}
				},"json");
			}
		});
	}

	function resetValue(){
		
		$("#file").val("");
		
	}
	
</script>
</head>
<body style="margin: 1px">
<table id="dg" title="文件管理" class="easyui-datagrid"
  fitColumns="true" pagination="true" rownumbers="true"
  url="${pageContext.request.contextPath}/file/list.do" fit="true" toolbar="#tb">
 <thead>
 	<tr>
 		<th field="cb" checkbox="true" align="center"></th>
 		<th field="fileid" width="100" align="center" hidden="true">编号</th>
 		<th field="filename" width="100" align="center">文件名称</th>
 		<th field="realname" width="100" align="center">文件真实名称</th>
 		<th field="action" width="100" align="center" formatter="formatAction">操作</th>
 	</tr>
 </thead>
</table>
<div id="tb">
 <div>
	<a href="javascript:openAddDiglog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加</a>
	<a href="javascript:deleteFile()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
 </div>
 <div>
 	&nbsp;文件名称&nbsp;<input type="text" id="filename" size="20" onkeydown="if(event.keyCode==13) searchFile()"/>
 	<a href="javascript:searchFile()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
 </div>
</div>
<div id="dlg" class="easyui-dialog" style="width: 320px;height: 150px;padding: 10px 20px" closed="true" buttons="#dlg-buttons">
 
 	<form id="fm" method="post" enctype="multipart/form-data">
 		<input type="file" name="file" id="file" class="easyui-validatebox" required="true"/>
 	</form>
 
</div>

<div id="dlg-buttons">
	<a href="javascript:saveFile()" class="easyui-linkbutton" iconCls="icon-ok">上传</a>
	<a href="javascript:closeDialog()" class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
</div>
</body>
</html>