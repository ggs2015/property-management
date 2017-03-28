function unwroteInfo(){
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
 		$.post("/unwroteInfo",
 				{
 					buildingId:buildingId,
 					roomId:roomId
 				},
 				function(data,status){
 					var paidInfoDiv = $('#paidInfo');
 					paidInfoDiv.empty();
 					if("0" == data.code){
 						var tableHtml = '<table>';
 						for(var timeStr in data.dateMonth){
 							tableHtml += '<tr><td>' + data.dateMonth[timeStr] 
 										+ '</td><td><input type="checkbox" name="checkbox" value="' + data.dateMonth[timeStr] 
 										+ '"></td><tr>';
 						} 
 						
 						tableHtml += '<tr><td><input type="button" value="确认开票" onclick="writeReceipt();"/></td></tr>';
 						tableHtml += '</table>';
 						paidInfoDiv.append(tableHtml);
				     }else if("1" == data.code){
 				     	alert("楼栋为空");
 				     }else if("2" == data.code){
 				     	alert("房间号为空");
 				     }else if("3" == data.code){
 				     	alert("管理的小区没有此房间信息");
 				     }else if("4" == data.code){
 				     	alert("已经全部开票");
 				     }else{
 				    	alert("未知异常");
 				     }
 					
 		        },
 		        "json"
 	   );
 	}
 	
 	function writeReceipt(){
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
 		var dateMonthArray = new Array();
        $('input[name="checkbox"]:checked').each(function(){ 
        	dateMonthArray.push($(this).val()); 
        	}); 
 	    if(dateMonthArray.length == 0){
 	    	alert("请选择月份");
 	    	return;
 	    }
 		$.post("/writeReceipt",
 				{
 					buildingId:buildingId,
 					roomId:roomId,
 					dateMonthArray:dateMonthArray
 				},
 				function(data,status){
 					if("0" == data){
 						alert("开票成功");
// 						location=location;
 						unwroteInfo();
				     }else if("1" == data){
 				     	alert("没有权限");
 				     }else if("2" == data){
 				     	alert("管理员小区获取失败");
 				     }else{
 				    	alert("添加失败");
 				     }
 					
 		        },
 		        "json"
 	   );
 		//location=location;
 	}
 	
 	function showUnpaidInfo(){
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
 		$.post("/unpaidInfo",
 				{
 					buildingId:buildingId,
 					roomId:roomId
 				},
 				function(data,status){
 					var paidInfoDiv = $('#paidInfo');
 					paidInfoDiv.empty();
 					if("0" == data.code){
 						var tableHtml = '<table>';
 						for(var timeStr in data.dateMonth){
// 							tableHtml += '<tr><td>' + data.dateMonth[timeStr] 
// 										+ '</td><td><input type="checkbox" name="checkbox" value="' + data.dateMonth[timeStr] 
// 										+ '"></td><tr>';
 							tableHtml += '<tr><td>' + data.dateMonth[timeStr] 
								+ '</td><td>' + data.monthUnpaidFeeList[timeStr] + '元'
								+ '</td><tr>';
 						} 
 						tableHtml += '<tr><td>缴费月数：<input type="text" style="width:30px;" id="payTotalMonthes" /></td>';
 						tableHtml += '<td><input type="button" value="确认缴费" onclick="payFeeByMonthes();"/></td></tr>';
 						tableHtml += '</table>';
 						paidInfoDiv.append(tableHtml);
				     }else if("1" == data.code){
 				     	alert("楼栋为空");
 				     }else if("2" == data.code){
 				     	alert("房间号为空");
 				     }else if("3" == data.code){
 				     	alert("管理的小区没有此房间信息");
 				     }else if("4" == data.code){
 				     	alert("已经交清");
 				     }else{
 				    	alert("未知异常");
 				     }
 					
 		        },
 		        "json"
 	   );
 	}
 	
 	function payFee(){
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
 		var dateMonthArray = new Array();
        $('input[name="checkbox"]:checked').each(function(){ 
        	dateMonthArray.push($(this).val()); 
        	}); 
 	    if(dateMonthArray.length == 0){
 	    	alert("请选择月份");
 	    	return;
 	    }
 		$.post("/payFee",
 				{
 					buildingId:buildingId,
 					roomId:roomId,
 					dateMonthArray:dateMonthArray
 				},
 				function(data,status){
 					if("0" == data){
 						alert("缴费成功");
// 						location=location;
 						showUnpaidInfo();
 						queryManagementInfo();
				     }else if("1" == data){
 				     	alert("没有权限");
 				     }else if("2" == data){
 				     	alert("管理员小区获取失败");
 				     }else if("3" == data){
 				     	alert("请选择一个月份进行缴费");
 				     }else{
 				    	alert("缴费失败");
 				     }
 					
 		        },
 		        "json"
 	   );
 		//location=location;
 	}
 	
 	
 	function payFeeByMonthes(){
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
 		var monthes = $('#payTotalMonthes').val();
 	    if(isNaN(monthes) || monthes <= 0 || monthes > 12){
 	    	alert("请输入1-12之间的数字");
 	    	return;
 	    }
 		$.post("/payFeeByMonthes",
 				{
 					buildingId:buildingId,
 					roomId:roomId,
 					monthes:monthes
 				},
 				function(data,status){
 					if("0" == data){
 						alert("缴费成功");
// 						location=location;
 						showUnpaidInfo();
 						queryManagementInfo();
				     }else if("1" == data){
 				     	alert("没有权限");
 				     }else if("2" == data){
 				     	alert("管理员小区获取失败");
 				     }else if("3" == data){
 				     	alert("月数错误");
 				     }else if("8" == data){
 				     	alert("缴费月数太多");
 				     }else{
 				    	alert("缴费失败");
 				     }
 					
 		        },
 		        "json"
 	   );
 		//location=location;
 	}