<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
 <head>
  <title>物业查询</title>
  <meta charset="UTF-8">
 </head>
 <body>
 
 <!-- 
 <#assign base=request.contextPath />
  -->
 <h3>物业查询
 <#if residentialName??>
 	(小区：${residentialName})
 </#if></h3>
 <select id="buildingId">
 	<option value="">请选择楼栋</option>
 	<#if buildingIdList??>
 		<#list buildingIdList as buildingId>
 			<option value="${buildingId}">${buildingId}</option>
 		</#list>
 	</#if>
 </select>栋
 <input type="text" id="roomId" placeholder="输入房间号码"/>号
 <input type="button" onclick="queryManagementInfo();" value="查询"/>
 </br>
 <!-- <div>
 	<#if roleId == 5>
 		<input type="button" value="添加用户" onclick="toAddUser();"/>
 	</#if>
 </div> -->
 <div id="feeInfo"></div>
 <div>
 	<div id="paidInfo"></div>
 	<div id="wroteReceived"></div>
 </div>
 <script type="text/javascript" src="../resources/js/jquery.js"></script>
 <script type="text/javascript" src="../resources/js/local/queryPage.js"></script>
 <script type="text/javascript">
 	var userName = "${userName !''}";
 	var phoneNo = "${phoneNo !''}";
 	var roleId = "${roleId !''}";
	$(document).ready(function(){
		var inputHtml = '<small>欢迎,</small>' + userName;
		$("#userName").html(inputHtml); 
	});
	$('#phoneNo').val(phoneNo);
	//用户权限
	if(roleId == 5){
		$('#userManage').show();
	}else{
		$('#userManage').hide();
	}
	//开票
	if(roleId == 5 || roleId == 3){
		$('#write').show();
	}else{
		$('#write').hide();
	} 
	//支付
	if(roleId == 5 || roleId == 3){
		$('#pay').show();
	}else{
		$('#pay').show();
	} 
	//数据权限
	if(roleId == 4 || roleId == 5){
		$('#statistics').show();
	}else{
		$('#statistics').hide();
	} 
	//缴费明细
	if(roleId == 4 || roleId == 5){
		$('#historyPaid').show();
	}else{
		$('#historyPaid').hide();
	}
	
 </script> 
 <script type="text/javascript">
 	function toAddUser(){
 		window.location.href = "/toAddUser";
 	}
 	
 	function queryManagementInfo(){
 		var buildingId = $('#buildingId').val();
 		var roomId = $('#roomId').val(); 
 		if(buildingId == ''){
 			alert("请选择楼栋");
 			return;
 		}
 		if(roomId == ''){
 			alert("请填写房间号");
 			return;
 		}
 		$('#paidInfo').empty();
 		$('#wroteReceived').empty();
 		$.post("/queryInfo",
 				{
 					buildingId:buildingId,
 					roomId:roomId
 				},
 				function(data,status){
 					if("0" == data.code){
 						var feeInfo = $('#feeInfo');
 						var toPay = '';
 						if(data.time != null){
 							toPay = 
 								'<tr><td>未缴费时间:</td><td>' + data.time + '</td></tr>'
 								+ '<tr><td>未缴费用:</td><td>' + data.needPay + '元</td></tr>';
 						}else{
 							toPay = 
 								'<tr><td>未缴费时间:</td><td>无</td></tr>'
 								+ '<tr><td>未缴费用:</td><td>0元</td></tr>';
 						}
 						if(data.roleId == 3 || data.roleId == 5){
 							toPay += '<tr><td><input type="button" value="缴费" onclick="showUnpaidInfo();"/></td>'
 								+'<td><input type="button" value="开票" onclick="unwroteInfo();"/></td></tr>';
 						}
 						if(data.roleId == 4){
 							toPay += '<tr><td><input type="button" value="缴费" onclick="showUnpaidInfo();"/></td></tr>';
 						} 
 						feeInfo.empty();
 						feeInfo.append('<table><tr><td>用户房间号:</td><td>' + data.buildingId + '幢' + data.roomId + '</td></tr>'
 								+ '<tr><td>房间面积:</td><td>' + data.houseArea + '平米</td></tr>'
 								+ '<tr><td>物业费单价:</td><td>' + data.unitPrice+ '元/平米</td></tr>'
 								+ toPay + '</table>');
				     }else if("1" == data.code){
 				     	alert("楼栋为空");
 				     }else if("2" == data.code){
 				     	alert("房间号为空");
 				     }else if("3" == data.code){
 				     	alert("房间不存在");
 				     }else{
 				    	alert("未知异常");
 				     }
 					
 		        },
 		        "json"
 	   );
 	}
 	
 function wroteReceipt(){
	 window.location.href = "/toWroteReceiptInfo";
 }
 
 function payManagementFee(){
	 window.location.href = "/toUnpaidInfo";
 }
 </script>
 </body>
</html>