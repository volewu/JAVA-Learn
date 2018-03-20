<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/jquery-easyui-1.5.1/themes/default/easyui.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/jquery-easyui-1.5.1/themes/icon.css">
<script src="${pageContext.request.contextPath}/static/jquery-easyui-1.5.1/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/static/jquery-easyui-1.5.1/jquery.easyui.min.js"></script>
<script src="${pageContext.request.contextPath}/static/jquery-easyui-1.5.1/locale/easyui-lang-zh_CN.js"></script>
<style>
  table tr th{
	 text-align: center;
}
  table tr td{
	 text-align: center;
}
table tr td input{
	 height: 20px;
	 border: 0;
	 text-align: center;
}
</style>
<script type="text/javascript">

	function submit(state){
		$("#fm").form("submit",{
			url:'${pageContext.request.contextPath}/task/audit_kj.do?state='+state,
			onSubmit:function(){
				return $(this).form("validate");
			},
			success:function(result){
				var result=eval('('+result+')');
				if(result.success){
					$.messager.alert("系统系统","提交成功！");
					resetValue();
				}else{
					$.messager.alert("系统系统","提交失败，请联系管理员！");
					return;
				}
			}
		});
	}


	$(function(){
		
		$.post("${pageContext.request.contextPath}/straingage/getStrainGageByTaskId.do",{taskId:'${param.taskId}'},function(result){
			var strainGage=result.strainGage;
			$("#BU").val(strainGage.BU);
			$("#APPLYMAN").val(strainGage.APPLYMAN);
			$("#ICNUM").val(strainGage.ICNUM);
			$("#APPLYDATE").val(strainGage.APPLYDATE);
			$("#PHONE").val(strainGage.PHONE);
			$("#EXPENSESCODE").val(strainGage.EXPENSESCODE);
			$("#REASON").val(strainGage.REASON);
			$("#PROJECTNAME").val(strainGage.PROJECTNAME);
			$("#PROJECTNAME2").val(strainGage.PROJECTNAME2);
			$("#LOC1").val(strainGage.LOC1);
			$("#LOC2").val(strainGage.LOC2);
			$("#LOC3").val(strainGage.LOC3);
			$("#LOC4").val(strainGage.LOC4);
			$("#LOC5").val(strainGage.LOC5);
			$("#LOC6").val(strainGage.LOC6);
			$("#LOC7").val(strainGage.LOC7);
			$("#LOC8").val(strainGage.LOC8);
			$("#LOC9").val(strainGage.LOC9);
			$("#LOC10").val(strainGage.LOC10);
			$("#LOC11").val(strainGage.LOC11);
			$("#LOC12").val(strainGage.LOC12);
			$("#LOC13").val(strainGage.LOC13);
			$("#LOC14").val(strainGage.LOC14);
			$("#SENSORQUANTITY").val(strainGage.SENSORQUANTITY);
			$("#SENSORAMOUNT").numberbox('setValue', strainGage.SENSORAMOUNT);
			$("#SURPORTQUANTITY").val(strainGage.SURPORTQUANTITY);
			$("#SURPORTAMOUNT").numberbox('setValue', strainGage.SURPORTAMOUNT);
			$("#LOANQUANTITY").val(strainGage.LOANQUANTITY);
			$("#LOANAMOUNT").numberbox('setValue', strainGage.LOANAMOUNT);
			$("#EMAIL").val(strainGage.EMAIL);
			$("#TOTALAMOUNT").numberbox('setValue', strainGage.TOTALAMOUNT);
			
			
			
		},"json");
		
		
	});

	
	function resetValue() {
		$("#BU").val("");
		$("#APPLYMAN").val("");
		$("#ICNUM").numberbox('setValue', "");
		$("#APPLYDATE").datebox('setValue', "");
		$("#PHONE").val("");
		$("#EXPENSESCODE").val("");
		$("#REASON").val("");
		$("#PROJECTNAME").val("");
		$("#PROJECTNAME2").val("");
		$("#LOC1").val("");
		$("#LOC2").val("");
		$("#LOC3").val("");
		$("#LOC4").val("");
		$("#LOC5").val("");
		$("#LOC6").val("");
		$("#LOC7").val("");
		$("#LOC8").val("");
		$("#LOC9").val("");
		$("#LOC10").val("");
		$("#LOC11").val("");
		$("#LOC12").val("");
		$("#LOC13").val("");
		$("#LOC14").val("");
		$("#SENSORQUANTITY").val("");
		$("#SENSORAMOUNT").numberbox('setValue', "");
		$("#SURPORTQUANTITY").val("");
		$("#SURPORTAMOUNT").numberbox('setValue', "");
		$("#LOANQUANTITY").val("");
		$("#LOANAMOUNT").numberbox('setValue', "");
		$("#EMAIL").val("");
		$("#TOTALAMOUNT").numberbox('setValue', "");
		
	}
</script>
</head>
<body style="margin: 5px">
<div id="p" class="easyui-panel" title="主管审批" style="width: 830px;height: auto;padding: 10px">
			<form id="fm" method="post">
			<table border="2" cellpadding="0px" bordercolor="black" cellspacing="0">
				<thead>
					<tr>
						<th style="text-align: center;" colspan="8"><font size="4"><strong> Strain Gage 申 請 表</strong></font></th>
					</tr>				
				</thead>
				<tr>
					<td >申 請 部 門</td>
					<td colspan="2"  ><input id="BU" name="BU" type="text" style="width: 98%" class="easyui-validatebox" editable="false"></td>
					<td  >申  請  人</td>
					<td colspan="2"  ><input id="APPLYMAN" name="APPLYMAN" type="text" style="width: 98%" class="easyui-validatebox" editable="false"></td>
					<td  >需測IC數量</td>
					<td  ><input type="text" id="ICNUM" name="ICNUM" class="easyui-validatebox" style="width: 99%" editable="false"></td>
				</tr>	
				<tr>
					<td >申 請 日 期</td> 
					<td colspan="2"    style="text-align: left;">  <input id="APPLYDATE" name="APPLYDATE"  class="easyui-validatebox" type="text" editable="false"></td>
					<td  >分 機 號 碼</td>
					<td colspan="2"  ><input id="PHONE" name="PHONE" type="text" class="easyui-validatebox" style="width: 98%" editable="false"></td>
					<td  >費 用 代 碼</td>
					<td  ><input type="text" id="EXPENSESCODE" name="EXPENSESCODE" class="easyui-validatebox" style="width: 98%" editable="false"></td>
				</tr>
				<tr>
					<td >需测<br/>StrainGage<br/>原因</td>
					<td colspan="7" ><textarea id="REASON" name="REASON" rows="4" cols="3" style="width: 99%" editable="false" class="easyui-validatebox" editable="false"></textarea></td>
				</tr>
				<tr>
					<td >Project Name</td>
					<td colspan="3" ><input id="PROJECTNAME"  name="PROJECTNAME" style="width: 98%" type="text" editable="false"></td>
					<td  >Project Name</td>
					<td colspan="3"  ><input id="PROJECTNAME2" name="PROJECTNAME2" style="width: 98%" type="text" editable="false"></td>
				</tr>
				<tr>
					<td  rowspan="7">IC Location</td>
					<td  width="60px">1</td>
					<td  colspan="2"> <input id="LOC1" name="LOC1" type="text" style="width: 98%" editable="false"></td>
					<td  rowspan="7" width="106px">IC Location</td>
					<td  width="63px">1</td>
					<td  colspan="2"><input id="LOC8" name="LOC8" type="text" style="width: 98%" editable="false"></td>
				</tr>
				<tr>
					<td >2</td>
					<td  colspan="2"> <input id="LOC2" name="LOC2" type="text" style="width: 98%" editable="false"></td>
					<td >2</td>
					<td  colspan="2"><input id="LOC9" name="LOC9" type="text" style="width: 98%" editable="false"></td>
				</tr>
				<tr>
					<td >3</td>
					<td  colspan="2"> <input id="LOC3" name="LOC3" type="text" style="width: 98%" editable="false"></td>
					<td >3</td>
					<td  colspan="2"><input id="LOC10" name="LOC10" type="text" style="width: 98%" editable="false"></td>
				</tr>
				<tr>
					<td >4</td>
					<td  colspan="2"> <input id="LOC4" name="LOC4" type="text" style="width: 98%" editable="false"></td>
					<td >4</td>
					<td  colspan="2"><input id="LOC11" name="LOC11" type="text" style="width: 98%" editable="false"></td>
				</tr>
				<tr>
					<td >5</td>
					<td  colspan="2"> <input id="LOC5" name="LOC5" type="text" style="width: 98%" editable="false"></td>
					<td >5</td>
					<td  colspan="2"><input id="LOC12" name="LOC12" type="text" style="width: 98%" editable="false"></td>
				</tr>
				<tr>
					<td >6</td>
					<td  colspan="2"> <input id="LOC6" name="LOC6" type="text" style="width: 98%" editable="false"></td>
					<td >6</td>
					<td  colspan="2"><input id="LOC13" name="LOC13" type="text" style="width: 98%" editable="false"></td>
				</tr>
				<tr>
					<td >7</td>
					<td  colspan="2"> <input id="LOC7" name="LOC7" type="text" style="width: 98%" editable="false"></td>
					<td >7</td>
					<td  colspan="2"><input id="LOC14" name="LOC14" type="text" style="width: 98%" editable="false"></td>
				</tr>
				<tr>
					<td  rowspan="7">費 用 評 估</td>
					<td >Item</td>
					<td  colspan="2">Description </td>
					<td  colspan="2">Quantity</td>
					<td >Unit price</td>
					<td  >Amount</td>
				</tr>
								<tr>
					<td >1</td>
					<td  colspan="2">Sensor </td>
					<td  colspan="2" style="text-align: left;"><input type="text" id="SENSORQUANTITY" name="SENSORQUANTITY" style="width: 70%" class="easyui-validatebox"  editable="false" />(個)</td>
					<td >$50</td>
					<td  ><input type="text" id="SENSORAMOUNT" name="SENSORAMOUNT" editable="false" class="easyui-numberbox" suffix="$"></td>
				</tr>
				<tr>
					<td >2</td>
					<td  colspan="2">Engineer surport </td>
					<td  colspan="2" style="text-align: left;"><input type="text"  id="SURPORTQUANTITY" name="SURPORTQUANTITY" style="width: 70%" class="easyui-validatebox"  editable="false"/>(小時)</td>
					<td >$15</td>
					<td  ><input type="text" id="SURPORTAMOUNT" name="SURPORTAMOUNT"  editable="false" class="easyui-numberbox" suffix="$"></td>
				</tr>
				<tr>
					<td >3</td>
					<td  colspan="2">Equipment Loan </td>
					<td  colspan="2" style="text-align: left;"><input type="text" id="LOANQUANTITY" name="LOANQUANTITY" style="width: 70%" class="easyui-validatebox"  editable="false" />(小時)</td>
					<td >$8</td>
					<td  ><input type="text" id="LOANAMOUNT" name="LOANAMOUNT"  class="easyui-numberbox" suffix="$" editable="false"></td>
				</tr>
				<tr>
					<td colspan="7">（注﹕一般每個IC需用4個Sensor)</td>
				</tr>
				<tr>
					<td colspan="2">申請人郵箱（必填）：</td>
					<td  colspan="5"><input type="text" id="EMAIL" name="EMAIL"  style="width: 99%" class="easyui-validatebox" editable="false"></td>
				</tr>
				<tr>
					<td colspan="2">Total Amount:  (Unit: USD)</td>
					<td  colspan="5"><input type="text" id="TOTALAMOUNT"  name="TOTALAMOUNT" editable="false" style="width: 100%" class="easyui-numberbox" suffix="$"></td>
				</tr>
			</table>
			
			<table cellspacing="8px" style="width: 800px;">
			<tr>
				<td valign="top">批注：</td>
				<td colspan="4">
					<textarea id="comment" name="comment" rows="2" cols="80" class="easyui-validatebox" required="true"></textarea>
				</td>
			</tr>
			<tr>
				<td>
					<input type="hidden" name="taskId" value="${param.taskId}"/>
				</td>
				<td colspan="4">
					<a href="javascript:submit(1)" class="easyui-linkbutton" iconCls="icon-ok" >批准</a>&nbsp;&nbsp;
					<a href="javascript:submit(2)" class="easyui-linkbutton" iconCls="icon-no" >驳回</a>
				</td>
			</tr>
		</table>
         
         
         
		</form>
</div>
<div style="padding-top: 5px">
<table id="dg" title="批注列表" class="easyui-datagrid" 
  fitColumns="true"
  url="${pageContext.request.contextPath}/task/listHistoryComment.do?taskId=${param.taskId}" style="width: 830px;height: 200px;">
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