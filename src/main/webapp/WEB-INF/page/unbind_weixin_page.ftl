<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
 <head>
  <title>微信解绑</title>
  <meta charset="UTF-8">
 </head>
 <body>

 <h3>微信解绑
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
 <input type="button" onclick="queryUserBindInfo();" value="查询"/>
 </br>
 </br>
 <div id="userInfo"></div>
 
 <script type="text/javascript" src="../resources/js/jquery.js"></script>
 <script type="text/javascript" src="../resources/js/local/leftButtonStyle.js"></script>
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
	userTable();
 </script> 
 <script type="text/javascript">
 	function queryUserBindInfo(){
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
 		$.post("/queryUserBindInfo",
 				{
 					buildingId:buildingId,
 					roomId:roomId
 				},
 				function(data,status){
 					if("0" == data.code){
 						var userInfo = $('#userInfo');
 						var bindUser = '<table border="1" cellspacing="0" cellpadding="0"><tr><td>昵称</td><td>微信号</td><td>操作</td></tr>';
 						userInfo.empty();
 						for(var i in data.userList){
 							bindUser += '<tr><td>' +data.userList[i].nickName + '</td><td>'+ data.userList[i].weixinNo +'</td><td><input type="button" value="解绑" onclick="unbindUser('+ data.userList[i].id +');"></td></tr>';
 						}
 						
 						userInfo.append(bindUser + '</table>');
				     }else if("1" == data.code){
 				     	alert("楼栋为空");
 				     }else if("2" == data.code){
 				     	alert("房间号为空");
 				     }else if("3" == data.code){
 				     	alert("用户没有权限");
 				     }else if("4" == data.code){
 				     	alert("房间不存在");
 				     }else{
 				    	alert("未知异常");
 				     }
 					
 		        },
 		        "json"
 	   );
 	}
 	
 	function unbindUser(id){
 		$.post("/unbindUser",
 				{
 					id:id
 				},
 				function(data,status){
 					if("0" == data){
 						alert("解绑成功");
 						queryUserBindInfo();
				     }else if("2" == data.code){
 				     	alert("用户没有权限");
 				     }else{
 				    	alert("解绑失败");
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