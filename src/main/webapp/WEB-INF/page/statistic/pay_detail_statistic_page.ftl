<!DOCTYPE html>
<html dir="ltr" lang="en-US">
   <head>
      <meta charset="UTF-8" />
      <title>物业费支付详情</title>
      
   </head>
   <body>
   <!-- 
   <link href="../resources/date/bootstrap.min.css" rel="stylesheet"> -->
   <link href="../resources/date/font-awesome.min.css" rel="stylesheet"> 
   <link rel="stylesheet" type="text/css" media="all" href="../resources/date/daterangepicker-bs3.css" />
   <link rel="stylesheet" type="text/css" href="../resources/page/css/qunit-1.11.0.css" />
   <link rel="stylesheet" type="text/css" href="../resources/page/css/bootstrap-responsive.min.css" />
   <!-- <link rel="stylesheet" type="text/css" href="../resources/page/css/bootstrap.css" /> -->
   <link rel="stylesheet" type="text/css" href="../resources/page/css/page.css" /> 
   <script type="text/javascript" src="../resources/date/jquery-1.8.3.min.js"></script> 
   <!-- <script type="text/javascript" src="../resources/page/jquery-1.9.1.min.js"></script> -->
   <script type="text/javascript" src="../resources/date/bootstrap.min.js"></script>
   <script type="text/javascript" src="../resources/date/moment.js"></script>
   <script type="text/javascript" src="../resources/date/daterangepicker.js"></script>
   <script type="text/javascript" src="../resources/page/bootstrap-paginator.js"></script>
   <script type="text/javascript" src="../resources/page/qunit-1.11.0.js"></script>
		<h3>缴费明细
		 <#if residentialName??>
		 	(小区：${residentialName})
		 </#if></h3>
     <div class="input-prepend input-group">
      	缴费时间： <!-- <span class="add-on input-group-addon">
      	<i class="glyphicon glyphicon-calendar fa fa-calendar"></i>
      </span> -->
      <input type="text" readonly style="width: 200px" name="reservation" id="reservation" class="form-control" value="2016-09-10 - 2016-09-20" /> 
	 <script type="text/javascript">
      $(document).ready(function() {
         $('#reservation').daterangepicker(null, function(start, end, label) {
           console.log(start.toISOString(), end.toISOString(), label);
         });
      });
     </script>
     	&nbsp;
		支付方式：<select id="payMethodType">
			<option value="">全部</option>
			<option value="0">支付宝</option>
			<option value="1">微信</option>
			<option value="2">现金</option>
		</select>&nbsp;
		<input type="button" value="查询" onclick="showPaidInfo();"/>
     </div>
	</br>
	<div id="showResult"></div>
	<div id="paginator-test"></div>
	
	<script type="text/javascript" src="../resources/js/local/leftButtonStyle.js"></script>
	<!--  
	<script type="text/javascript">
	$(function(){
        var container = $('#paginator-test');
        var options = null;
        //container.bootstrapPaginator(options);
        module("getPages functionality test, 11 pages, maximum 3 pages visible",{
            setup: function(){
                 options = {
                    containerClass:"pagination"
                    , currentPage:1
                    , numberOfPages: 3
                    , totalPages:11
                    , pageUrl:function(type,page){
                        return null;
                    }
                    , onPageClicked:null
                    , onPageChanged:null
                };
                container.bootstrapPaginator(options);
            },
            teardown: function(){
                //container.bootstrapPaginator('destroy');
            }
        });
        })
	 </script>
	 
	 -->
	<script type="text/javascript">
		var time2 = new Date().Format("yyyy-MM-dd"); 	
		var year = new Date().getFullYear(); 
		var str = year + "-01-01"+" - "+ time2;
		$('#reservation').val(str);
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
		statisticsTable();
	 </script>
   
   <script type="text/javascript">
   function showDate(){
	   var dateStr = $('#reservation').val();
	   alert(dateStr);
   }
   function showPaidInfo(){
		var dateStr = $('#reservation').val();
		var payMethodType = $('#payMethodType').val();
		if(dateStr == ''){
			alert("请选择时间");
			return;
		}
		$.post("/statisticsPaidInfo",
				{
					dateStr:dateStr,
					payMethodType:payMethodType
				},
				function(data,status){
					var paidInfoDiv = $('#showResult');
					paidInfoDiv.empty();
					if("0" == data.code){
						var tableHtml = '<table border="1" cellspacing="0" cellpadding="0"><tr><td>房间号</td><td>缴费时间</td><td>缴费方式</td><td>缴费金额</td><td>操作员</td></tr>';
						for(var paidInfo in data.paidList){
							var payMethod = "";
							var payAmount = "";
							if(data.paidList[paidInfo].payMethodType == 0){
								payMethod = "支付宝";
								payAmount = data.paidList[paidInfo].payAmount;
							}
							if(data.paidList[paidInfo].payMethodType == 1){
								payMethod = "微信";
								payAmount = data.paidList[paidInfo].payAmount;
							}
							if(data.paidList[paidInfo].payMethodType == 2){
								payMethod = "现金";
								payAmount = data.paidList[paidInfo].needPayManagementFee;
							}
							var roomNo = data.paidList[paidInfo].building + "-" + data.paidList[paidInfo].houseNumber;
							
							tableHtml += '<tr><td>' + roomNo + '</td>'
										+ '<td>' + data.paidList[paidInfo].paidTime + '</td>' 
										+ '<td>' + payMethod + '</td>' 
										+ '<td>' + payAmount + '</td>' 
										+ '<td>' + data.paidList[paidInfo].endUserName + '</td>'
										+ '<tr>';
						} 
						tableHtml += '</table>';
						paidInfoDiv.append(tableHtml);
				     }else if("1" == data.code){
				     	alert("时间为空");
				     }else if("2" == data.code){
				     	alert("时间不对");
				     }else if("3" == data.code){
				     	alert("时间太长");
				     }else if("4" == data.code){
				     	alert("没有权限");
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
