<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
 <head>
  <title>缴物业费</title>
  <meta charset="UTF-8">
 </head>
 
 <body>
 <h3>缴物业费
 <#if residentialName??>
 	(小区：${residentialName})
 </#if></h3>
	<!-- <select id="buildingId" onchange="getHouseIdByBuilding();" >
 		<option value="">请选择</option>
 		<#if buildingIdList?? >
 			<#list buildingIdList as buildingId>
 				<option value="${buildingId}">${buildingId}</option>
 			</#list>
 		</#if>
 	</select>栋 -->
	<!-- <select id="roomId" onchange="showUnpaidInfo();"> -->
	<!-- <select id="roomId">
 		<option value="">请选择</option>
 	</select>号 -->
 	
 	<select id="buildingId">
 		<option value="">请选择</option>
 		<#if buildingIdList?? >
 			<#list buildingIdList as buildingId>
 				<option value="${buildingId}">${buildingId}</option>
 			</#list>
 		</#if>
 	</select>栋
 	<input type="text" id="roomId" placeholder="输入房间号码"/>号
 	<input type="button" value="查询未缴月份" onclick="showUnpaidInfo();">
 </br>
 
 <div id="paidInfo"></div>
 <!-- <div><input type="button" value="确认缴费" onclick="payFee();"></div> -->
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
		if(roleId == 3 || roleId == 5){
			$('#write').show();
		}else{
			$('#write').hide();
		} 
		//支付缴费
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
 	function getHouseIdByBuilding(){
 		var buildingId = $('#buildingId').val();
 		if(buildingId == ''){
 			alert("请选楼栋");
 			return;
 		}
 		var roomSelect = $('#roomId');
 		roomSelect.empty();
 		roomSelect.append('<option value="">请选择</option>');
 		$.post("/getHouseIdByBuilding",
 				{
 					buildingId:buildingId
 				},
 				function(data,status){
 					if("success" == status){
 						var roomSelect = $('#roomId');
 						for(var roomId in data){
 							roomSelect.append("<option value='"+ data[roomId] +"'>"+ data[roomId] +"</option>"); 
 						} 
				     }else{
 				    	alert("获取房间号失败");
 				     }
 					
 		        },
 		        "json"
 	   );
 	}
 	
 </script>
 </body>
</html>