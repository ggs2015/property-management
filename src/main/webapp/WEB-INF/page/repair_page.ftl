<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
 <head>
  <title>物业维修</title>
  <meta charset="UTF-8">
 </head>
 <body>

 <h3>物业报修
 <#if residentialName??>
 	(小区：${residentialName})
 </#if></h3>
 
 &nbsp;维修状态：
<select id="repairStatus">
 	<option value="1">待处理</option>
 	<option value="3">已处理</option>
 </select>
 <input type="button" onclick="queryRepairInfo();" value="查询"/>
 
 </br>
 </br>
 <div id="repairInfo"></div>
 
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
 	function queryRepairInfo(){
 		var status = $('#repairStatus').val(); 
 		if(status == ''){
 			alert("请选择状态");
 			return;
 		}
 		$.post("/queryRepairInfo",
 				{
 					status:status
 				},
 				function(data,status){
 					if("0" == data.code){
 						var repairInfo = $('#repairInfo');
 						if(1 == data.status){
 							var repairTable = '<table border="1" cellspacing="0" cellpadding="0">'
 								+ '<tr><td>报修时间</td><td>报修人</td><td>报修人电话</td><td>报修人房间</td><td>说明</td><td>处理情况</td><td>操作</td></tr>';
 							repairInfo.empty();
 							for(var i in data.repairList){
 								repairTable += '<tr><td>' +data.repairList[i].createTime + '</td>'
 								+'<td>'+ data.repairList[i].userName +'</td>'
 								+'<td>'+ data.repairList[i].phone +'</td>'
 								+'<td>'+ data.repairList[i].building +'-'+ data.repairList[i].roomNo +'</td>'
 								+'<td>'+ data.repairList[i].content +'</td>'
 								+'<td>待处理</td>'
 								+'<td><input type="button" value="处理完成" onclick="repair('+ data.repairList[i].id +');"></td></tr>';
 	 						}
 	 						
 	 						repairInfo.append(repairTable + '</table>');
 						}
 						if(3 == data.status){
 							var repairTable = '<table border="1" cellspacing="0" cellpadding="0">'
 								+ '<tr><td>报修时间</td><td>报修人</td><td>报修人电话</td><td>报修人房间</td><td>说明</td><td>状态</td><td>处理人</td><td>处理人电话</td><td>处理时间</td></tr>';
 							repairInfo.empty();
 							for(var i in data.repairList){
 								repairTable += '<tr><td>' +data.repairList[i].createTime + '</td>'
 								+'<td>'+ data.repairList[i].userName +'</td>'
 								+'<td>'+ data.repairList[i].phone +'</td>'
 								+'<td>'+ data.repairList[i].building +'-'+ data.repairList[i].roomNo +'</td>'
 								+'<td>'+ data.repairList[i].content +'</td>'
 								+'<td>已处理</td>'
 								+'<td>' + data.repairList[i].operator + '</td>'
 								+'<td>' + data.repairList[i].operatorPhone + '</td>'
 								+'<td>' + data.repairList[i].updateTime + '</td></tr>';
 	 						}
 	 						
 	 						repairInfo.append(repairTable + '</table>');
 						}
				     }else{
 				    	alert("未知异常");
 				     }
 		        },
 		        "json"
 	   );
 	}
 	
 	function repair(id){
 		$.post("/repair",
 				{
 					id:id
 				},
 				function(data,status){
 					if("0" == data){
 						alert("处理成功");
 						queryRepairInfo();
				     }else{
 				    	alert("处理失败");
 				     }
 					
 		        },
 		        "json"
 	   );
 	}
 </script>
 </body>
</html>