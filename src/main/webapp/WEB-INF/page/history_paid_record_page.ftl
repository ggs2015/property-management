<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
 <head>
  <title>缴费历史记录</title>
  <meta charset="UTF-8">
 </head>
 
 <body>
 <h3>缴费历史记录
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
 	</select>栋
	<select id="roomId">
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
 	<input type="button" value="查询" onclick="showHistoryPayInfo();">
 </br>
 </br>
 <!-- <div><input type="button" value="查询" onclick="showHistoryPayInfo();"></div> -->
 <div id="paidInfo"></div>
 <script type="text/javascript" src="../resources/js/jquery.js"></script>
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
 	
 	function showHistoryPayInfo(){
 		var buildingId = $('#buildingId').val();
 		var roomId = $('#roomId').val();
 		if(buildingId == ''){
 			alert("请选楼栋");
 			return;
 		}
 		if(roomId == ''){
 			alert("请选房间");
 			return;
 		}
 		$.post("/historyPayRecord",
 				{
 					buildingId:buildingId,
 					roomId:roomId
 				},
 				function(data,status){
 					var paidInfoDiv = $('#paidInfo');
 					paidInfoDiv.empty();
 					if("0" == data.code){
 						var tableHtml = '<table border="1" cellspacing="0" cellpadding="0"><tr><td>年-月</td><td>缴费方式</td><td>缴费时间</td><td>缴费人名称</td><td>电话</td></tr>';
 						for(var paidInfo in data.paidInfoList){
 							var payMethod = "";
 							if(data.paidInfoList[paidInfo].payMethodType == 0){
 								payMethod = "支付宝";
 							}
 							if(data.paidInfoList[paidInfo].payMethodType == 1){
 								payMethod = "微信";
 							}
 							if(data.paidInfoList[paidInfo].payMethodType == 2){
 								payMethod = "现金";
 							}
 							
 							tableHtml += '<tr><td>' +data.paidInfoList[paidInfo].year + '-'
 										 + data.paidInfoList[paidInfo].month + '</td>' 
 										+ '<td>' + payMethod + '</td>' 
 										+ '<td>' +data.paidInfoList[paidInfo].timeStr + '</td>' 
 										+ '<td>' +data.paidInfoList[paidInfo].userName + '</td>'
 										+ '<td>' +data.paidInfoList[paidInfo].userPhone + '</td>'
 										+ '<tr>';
 						} 
 						tableHtml += '</table>';
 						paidInfoDiv.append(tableHtml);
				     }else if("1" == data.code){
 				     	alert("楼栋为空");
 				     }else if("2" == data.code){
 				     	alert("房间号为空");
 				     }else if("3" == data.code){
 				     	alert("管理的小区没有此房间信息");
 				     }else{
 				    	alert("未知异常");
 				     }
 					
 		        },
 		        "json"
 	   );
 	}
 	
 </script>
 </body>
</html>