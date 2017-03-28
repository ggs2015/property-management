<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
 <head>
  <title>欠费明细图</title>
  <meta charset="UTF-8">
 </head>
 
 <body>
  <link rel="stylesheet" type="text/css" href="../resources/page/css/page.css" /> 
 <h3>欠费明细
 <#if residentialName??>
 	(小区：${residentialName})
 </#if></h3>
	<div>${userTotal !'0'}户,累计欠费${totalUnpaid !'0'}元</div>
	<div>${year?c !''}年01月至${month !''}月，有${userTotal !'0'}户未缴清物业费，累计欠费${totalUnpaid !'0'}元</div>
	</br>
	<div>
		<table border="1" cellspacing="0" cellpadding="0">
			<thead>
				<tr>
					<td>房间号</td>
					<td>欠缴月数</td>
					<td>欠缴金额</td>
				</tr>
			</thead>
			<tbody>
				<#if unpaidListMap?? && unpaidListMap?size gt 0>
          			<#list unpaidListMap?keys as key>
          				<tr>
							<td>${key}</td>
							<td>${unpaidListMap[key].months !''}</td>
							<td>${unpaidListMap[key].needPayManagementFee !''}</td>
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
		statisticsTable();
	 </script>
 </body>
 <script type="text/javascript">
 </script>
</html>