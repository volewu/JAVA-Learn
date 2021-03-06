<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>StrainGage申请表</title>
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
<script>

 function CalcNum() {
	var SENSORQUANTITY=$("#SENSORQUANTITY").val();
	$("#SENSORAMOUNT").numberbox('setValue', SENSORQUANTITY*50);
	var SURPORTQUANTITY=$("#SURPORTQUANTITY").val();
	var LOANQUANTITY=$("#LOANQUANTITY").val();
	$("#TOTALAMOUNT").numberbox('setValue', SENSORQUANTITY*50+SURPORTQUANTITY*15+LOANQUANTITY*8);
}

  function CalcNum1() {

		var SURPORTQUANTITY=$("#SURPORTQUANTITY").val();
		var SENSORQUANTITY=$("#SENSORQUANTITY").val();
		var LOANQUANTITY=$("#LOANQUANTITY").val();
		$("#SURPORTAMOUNT").numberbox('setValue', SURPORTQUANTITY*15);
		$("#TOTALAMOUNT").numberbox('setValue', SENSORQUANTITY*50+SURPORTQUANTITY*15+LOANQUANTITY*8);
	}
 
 function CalcNum2() {

		var LOANQUANTITY=$("#LOANQUANTITY").val();
		var SURPORTQUANTITY=$("#SURPORTQUANTITY").val();
		var SENSORQUANTITY=$("#SENSORQUANTITY").val();
		$("#LOANAMOUNT").numberbox('setValue', LOANQUANTITY*8);
		$("#TOTALAMOUNT").numberbox('setValue', SENSORQUANTITY*50+SURPORTQUANTITY*15+LOANQUANTITY*8);
	} 
 
 
 function saveApply(){
		$("#fm").form("submit",{
			url:'${pageContext.request.contextPath}/straingage/save.do',
			onSubmit:function(){
				
				var zg=document.all['zg'];
				var flag=false;
				for (var i = 0; i < zg.length; i++) {
				if (zg[i].checked) {
					flag=true;
					}
				}
				if (flag==false) {
					$.messager.alert("系统提示","是否需要部级主管签核没有选择！");
					return false;
				}
				
				var APPLYDATE=$("#APPLYDATE").datebox('getValue', '');
				if (APPLYDATE==null || APPLYDATE=="") {
					$.messager.alert("系统提示","申请日期没有选择！");
					return false;
				}
				
				return $(this).form("validate");
			},
			success:function(result){
				var result=eval('('+result+')');
				if(result.success){
					$.messager.alert("系统系统","提交成功！");
					resetValue();
				}else{
					$.messager.alert("系统系统","提交失败！");
					return;
				}
				
			}
		});
	}
 
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
<body style="padding: 20px;">
	
	<div style="width:95%;height: auto;">
		<form id="fm" method="post">
		
			<input type="button" value="添加" onclick="saveApply()" style="width:60px;height:30px ; margin-bottom: 20px;background-color: blue;border-color:green;color: white;border-bottom-left-radius: 5px;border-bottom-right-radius: 5px;border-top-left-radius: 5px;border-top-right-radius: 5px;">	
		          &nbsp; &nbsp;是否需要申请人的部级主管签核：是<input type="radio" name="zg" value="true">&nbsp;否<input type="radio" name="zg" value="false"> <font color="red">（选否为只需要课级主管签核）</font>
			<table border="2" cellpadding="0px" bordercolor="black" cellspacing="0">
				<thead>
					<tr>
						<th style="text-align: center;" colspan="8"><font size="4"><strong> Strain Gage 申 請 表</strong></font></th>
					</tr>				
				</thead>
				<tr>
					<td >申 請 部 門</td>
					<td colspan="2"  ><input id="BU" name="BU" type="text" style="width: 98%" class="easyui-validatebox" required="true"></td>
					<td  >申  請  人</td>
					<td colspan="2"  ><input id="APPLYMAN" name="APPLYMAN" type="text" style="width: 98%" class="easyui-validatebox" required="true" editable="false" value="${currentMemberShip.user.firstName}"></td>
					<td  >需測IC數量</td>
					<td  ><input type="text" id="ICNUM" name="ICNUM" class="easyui-numberbox" style="width: 99%" required="true"></td>
				</tr>	
				<tr>
					<td >申 請 日 期</td>
					<td colspan="2"    style="text-align: left;">  <input id="APPLYDATE" name="APPLYDATE" style="width: 100%" class="easyui-datebox" type="text" editable="false"></td>
					<td  >分 機 號 碼</td>
					<td colspan="2"  ><input id="PHONE" name="PHONE" type="text" class="easyui-validatebox" style="width: 98%" required="true"></td>
					<td  >費 用 代 碼</td>
					<td  ><input type="text" id="EXPENSESCODE" name="EXPENSESCODE" class="easyui-validatebox" style="width: 98%" required="true"></td>
				</tr>
				<tr>
					<td >需测<br/>StrainGage<br/>原因</td>
					<td colspan="7" ><textarea id="REASON" name="REASON" rows="4" cols="3" style="width: 99%" class="easyui-validatebox" required="true"></textarea></td>
				</tr>
				<tr>
					<td >Project Name</td>
					<td colspan="3" ><input id="PROJECTNAME"  name="PROJECTNAME" class="easyui-validatebox" required="true" style="width: 98%" type="text" ></td>
					<td  >Project Name</td>
					<td colspan="3"  ><input id="PROJECTNAME2" name="PROJECTNAME2" style="width: 98%" type="text" ></td>
				</tr>
				<tr>
					<td  rowspan="7">IC Location</td>
					<td  width="60px">1</td>
					<td  colspan="2"> <input id="LOC1" name="LOC1" type="text" style="width: 98%" ></td>
					<td  rowspan="7" width="106px">IC Location</td>
					<td  width="63px">1</td>
					<td  colspan="2"><input id="LOC8" name="LOC8" type="text" style="width: 98%"></td>
				</tr>
				<tr>
					<td >2</td>
					<td  colspan="2"> <input id="LOC2" name="LOC2" type="text" style="width: 98%"></td>
					<td >2</td>
					<td  colspan="2"><input id="LOC9" name="LOC9" type="text" style="width: 98%"></td>
				</tr>
				<tr>
					<td >3</td>
					<td  colspan="2"> <input id="LOC3" name="LOC3" type="text" style="width: 98%"></td>
					<td >3</td>
					<td  colspan="2"><input id="LOC10" name="LOC10" type="text" style="width: 98%"></td>
				</tr>
				<tr>
					<td >4</td>
					<td  colspan="2"> <input id="LOC4" name="LOC4" type="text" style="width: 98%"></td>
					<td >4</td>
					<td  colspan="2"><input id="LOC11" name="LOC11" type="text" style="width: 98%"></td>
				</tr>
				<tr>
					<td >5</td>
					<td  colspan="2"> <input id="LOC5" name="LOC5" type="text" style="width: 98%"></td>
					<td >5</td>
					<td  colspan="2"><input id="LOC12" name="LOC12" type="text" style="width: 98%"></td>
				</tr>
				<tr>
					<td >6</td>
					<td  colspan="2"> <input id="LOC6" name="LOC6" type="text" style="width: 98%"></td>
					<td >6</td>
					<td  colspan="2"><input id="LOC13" name="LOC13" type="text" style="width: 98%"></td>
				</tr>
				<tr>
					<td >7</td>
					<td  colspan="2"> <input id="LOC7" name="LOC7" type="text" style="width: 98%"></td>
					<td >7</td>
					<td  colspan="2"><input id="LOC14" name="LOC14" type="text" style="width: 98%"></td>
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
					<td  colspan="2" style="text-align: left;"><input type="text" id="SENSORQUANTITY" name="SENSORQUANTITY" style="width: 70%" class="easyui-validatebox" required="true" onblur="CalcNum()" />(個)</td>
					<td >$50</td>
					<td  ><input type="text" id="SENSORAMOUNT" name="SENSORAMOUNT" required="true" editable="false" class="easyui-numberbox" suffix="$"></td>
				</tr>
				<tr>
					<td >2</td>
					<td  colspan="2">Engineer surport </td>
					<td  colspan="2" style="text-align: left;"><input type="text"  id="SURPORTQUANTITY" name="SURPORTQUANTITY" style="width: 70%" class="easyui-validatebox" required="true" onblur="CalcNum1()" />(小時)</td>
					<td >$15</td>
					<td  ><input type="text" id="SURPORTAMOUNT" name="SURPORTAMOUNT" required="true" editable="false" class="easyui-numberbox" suffix="$"></td>
				</tr>
				<tr>
					<td >3</td>
					<td  colspan="2">Equipment Loan </td>
					<td  colspan="2" style="text-align: left;"><input type="text" id="LOANQUANTITY" name="LOANQUANTITY" style="width: 70%" class="easyui-validatebox" required="true" onblur="CalcNum2()" />(小時)</td>
					<td >$8</td>
					<td  ><input type="text" id="LOANAMOUNT" name="LOANAMOUNT" required="true" editable="false" class="easyui-numberbox" suffix="$"></td>
				</tr>
				<tr>
					<td colspan="7">（注﹕一般每個IC需用4個Sensor)</td>
				</tr>
				<tr>
					<td colspan="2">申請人郵箱（必填）：</td>
					<td  colspan="5"><input type="text" id="EMAIL" name="EMAIL" required="true" style="width: 99%" class="easyui-validatebox" validType="email"></td>
				</tr>
				<tr>
					<td colspan="2">Total Amount:  (Unit: USD)</td>
					<td  colspan="5"><input type="text" id="TOTALAMOUNT"  name="TOTALAMOUNT" editable="false" style="width: 100%" class="easyui-numberbox" required="true" suffix="$"></td>
				</tr>
			</table>
         
		</form>
	</div>
</body>
</html>