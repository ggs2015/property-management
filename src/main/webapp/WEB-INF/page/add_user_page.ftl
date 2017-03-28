<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
 <head>
  <title>添加用户</title>
  <meta charset="UTF-8">
 </head>
 <body>
 <script type="text/javascript" src="../resources/js/jquery.js"></script>
 <script type="text/javascript" src="../resources/js/validate/idCard_validate.js"></script>
 <script type="text/javascript" src="../resources/js/validate/phoneNo_validate.js"></script>
 <h3>添加用户
 <#if residentialName??>
 	(小区：${residentialName})
 </#if></h3>
 姓名：<input type="text" id="newUserName" name="endUser.userName" />
 电话：<input type="text" id="userPhone"  name="endUser.userPhone"/>
 身份证：<input type="text" id="identityCardId" name="endUser.identityCardId"/>
 性别：<select id="sex" name="endUser.sex">
 		<option value="">请选择</option>
 		<option value="0">女</option>
 		<option value="1">男</option>
 	</select>
 角色：<select id="roleId" name="endUser.roleId">
 		<option value="">请选择</option>
 		<option value="3">物业工作人员</option>
 		<option value="4">物业管理人员</option>
 		<option value="5">后台管理人员</option>
 	</select>
 </br>
 </br>
 <div>
 	<input type="button" value="保存信息" onclick="addUser();"/>
 </div>
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
		userTable();
	 </script>
 <script type="text/javascript">
 	function addUser(){
 		var userName = $('#newUserName').val();
 		var userPhone = $('#userPhone').val();
 		var identityCardId = $('#identityCardId').val();
 		var sex = $('#sex').val();
 		var roleId = $('#roleId').val();
 		//校验
 		if(userName == ''){
 			alert("姓名为空");
 			return;
 		}
 		if(userPhone == ''){
 			alert("电话为空");
 			return;
 		}
 		if(identityCardId == ''){
 			alert("身份证为空");
 			return;
 		}
 		if(sex == ''){
 			alert("性别为空");
 			return;
 		}
 		if(roleId == ''){
 			alert("角色为空");
 			return;
 		}
 		//身份证校验
 		if(!IdCardValidate(identityCardId)){
 			alert("身份证信息不对");
 			return;
 		}
 		//电话校验
 		if(!checkPhone(userPhone)){
 			alert("电话号码不对");
 			return;
 		}
 		$.post("/addUser",
 				{
 					userName:userName,
 					userPhone:userPhone,
 					identityCardId:identityCardId,
 					sex:sex,
 					roleId:roleId
 				},
 				function(data,status){
 					if("0" == data){
 						alert("添加成功");
 						location=location;
				     }else if("1" == data){
 				     	alert("没有权限");
 				     }else if("2" == data){
 				     	alert("管理员小区获取失败");
 				     }else{
 				    	alert("手机号已被使用");
 				     }
 					
 		        },
 		        "json"
 	   );
 		//location=location;
 	}
 </script>
 </body>
</html>