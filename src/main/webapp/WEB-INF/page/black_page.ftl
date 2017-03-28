<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
 <head>
  <title>黑名单管理</title>
  <meta charset="UTF-8">
 </head>
 <body>

 <h3>黑名单管理
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
 
 <input type="button" onclick="queryHouseUserInfo();" value="查询"/>
 &nbsp;&nbsp;
 <input type="button" onclick="queryBlackUserInfo();" value="查询黑名单"/>
 </br>
 </br>
 <div id="blackInfo"></div>
 
 <script type="text/javascript" src="../resources/js/jquery.js"></script>
 <script type="text/javascript" src="../resources/js/local/leftButtonStyle.js"></script>
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
 	function queryHouseUserInfo(){
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
 		$.post("/queryHouseUser",
 				{
 					building:buildingId,
 					houseNumber:roomId
 				},
 				function(data,status){
 					if("0" == data.code){
 						var blackInfo = $('#blackInfo');
						var blackTable = '<table border="1" cellspacing="0" cellpadding="0">'
							+ '<tr><td>楼层</td><td>房间号</td><td>绑定人</td><td>电话</td><td>微信号</td><td>微信昵称</td><td>操作</td></tr>';
						blackInfo.empty();
						for(var i in data.houseUserList){
							blackTable += '<tr><td>' +data.houseUserList[i].building + '</td>'
							+'<td>'+ data.houseUserList[i].houseNumber +'</td>'
							+'<td>'+ data.houseUserList[i].userName +'</td>'
							+'<td>'+ data.houseUserList[i].userPhone +'</td>'
							+'<td>'+ data.houseUserList[i].weixinNo +'</td>'
							+'<td>'+ data.houseUserList[i].nickName +'</td>'
							+'<td><input type="button" value="加入黑名单" onclick="addBlack('+ data.houseUserList[i].endUserId +');"></td></tr>';
 						}
 						
						blackInfo.append(blackTable + '</table>');
				     }else if("1" == data.code){
				    	 alert("没有小区权限");
				     }else if("2" == data.code){
				    	 alert("必填参数为空");
				     }else{
 				    	alert("未知异常");
 				     }
 		        },
 		        "json"
 	   );
 	}
 	
 	function queryBlackUserInfo(){
 		$.post("/queryBlackUser",
 				{
 				},
 				function(data,status){
 					if("0" == data.code){
 						var blackInfo = $('#blackInfo');
						var blackTable = '<table border="1" cellspacing="0" cellpadding="0">'
							+ '<tr><td>微信昵称</td><td>电话</td><td>微信号</td><td>操作</td></tr>';
						blackInfo.empty();
						for(var i in data.blackUserList){
							blackTable += '<tr><td>' +data.blackUserList[i].nickName + '</td>'
							+'<td>'+ data.blackUserList[i].userPhone +'</td>'
							+'<td>'+ data.blackUserList[i].weixinNo +'</td>'
							+'<td><input type="button" value="解除黑名单" onclick="removeBlack('+ data.blackUserList[i].id +');"></td></tr>';
 						}
 						
						blackInfo.append(blackTable + '</table>');
				     }else if("1" == data.code){
				    	 alert("没有小区权限");
				     }else{
 				    	alert("未知异常");
 				     }
 		        },
 		        "json"
 	   );
 	}
 	
 	function addBlack(id){
 		$.post("/addBlack",
 				{
 					userId:id
 				},
 				function(data,status){
 					if("0" == data){
 						alert("处理成功");
				     }else{
 				    	alert("处理失败");
 				     }
 					queryHouseUserInfo();
 		        },
 		        "json"
 	   );
 	}
 	
 	function removeBlack(id){
 		$.post("/removeBlack",
 				{
 					userId:id
 				},
 				function(data,status){
 					if("0" == data){
 						alert("处理成功");
				     }else{
 				    	alert("处理失败");
 				     }
 					queryBlackUserInfo();
 		        },
 		        "json"
 	   );
 	}
 </script>
 </body>
</html>