<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
 <head>
  <title>缴费全量图</title>
  <meta charset="UTF-8">
 </head>
 
 <body>
  <link rel="stylesheet" type="text/css" href="../resources/page/css/page.css" /> 
 <h3>缴费统计
 <#if residentialName??>
 	(小区：${residentialName})
 </#if></h3>
	<div>${houseCount !''}户,每月应收${totalFeePerMonth !''}元</div>
	<div>${year?c !''}年01月至${month !''}月，应收物业费${needGetFee !''}元，实收物业费${paidFee !''}元，待收物业费${needToFee !''}元，完成率${payRate !'0'}%</div>
	</br>
	<div>
		<table border="1" cellspacing="0" cellpadding="0">
			<thead>
				<tr>
					<td>时间</td>
					<td>应缴</td>
					<td>实收</td>
					<td>微信实收</td>
					<td>现金实收</td>
					<td>待收</td>
					<td>完成率</td>
				</tr>
			</thead>
			<tbody>
				<#if monthFeeStatisticsVoList ??>
					<#list monthFeeStatisticsVoList as item>
					<tr>
						<td>${item.yearAndMonth !''}</td>
						<td>${item.totalToPay !''}</td>
						<td>${item.paidFee !''}</td>
						<td>${item.weiChartPaid !''}</td>
						<td>${item.cashPaid !''}</td>
						<td>${item.needToPay !''}</td>
						<td>${item.payRate !''}%</td>
					</tr>
					</#list>
				</#if>
			</tbody>
		</table>
	</div>
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
		if(roleId == 3 || roleId == 5){
			$('#write').show();
		}else{
			$('#write').hide();
		} 
		//缴费
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
 </body>
 <script type="text/javascript">
 </script>
</html>