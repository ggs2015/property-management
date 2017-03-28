<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
 <head>
  <title>登录</title>
  <meta charset="UTF-8">
 </head>
 <script type="text/javascript" src="../resources/js/jquery.js"></script>
 <body>
	 <table>
	 	用户名：<input type="text" id="userName" name="phoneNo"/>
	 	</br>
	 	短信验证码：<input type="text" id="SMSVerificationCode" name="verificationCode"/>
	 	<!-- <input type="button" value="获取验证码" onclick="getCode();"/> -->
	 	<input type="button" style="height:32px;width:130px;" value="点击发送验证码" onclick="getCode(this)" />
	 	</br>
	 	<input type="button" value="login" onclick="login();"/>
	 </table>
 </body>
 <script type="text/javascript">
 	function getCode(thisBtn){
 		var phoneNo = $("#userName").val();
 		if(phoneNo == ''){
 			alert("请输入手机号");
 			return;
 		}
 		$.post("/getVerificationCode",
 				{
 			      phoneNo:phoneNo
 				},
 				function(data,status){
 				     if("1" == data){
 				     	alert("电话为空");
 				     } else if("2" == data){
 				    	 alert("用户不存在");
 				     }else if("3" == data) {
 				    	 alert("发短信失败");
 				     }else if("4" == data){
 				    	 alert("电话号码不对");
 				     }else{
 				    	 
 				     	/* $("#lockedApplications").empty();
 				     	var fileListHtml = "";
 				     	if(data == ''){
 				     		fileListHtml = "未找到加锁应用！";
 				     	}else{
 					     	for(var i=0;i<data.length;i++){
 					     		var applicationName = data[i];
 					     		fileListHtml+=applicationName + "<a href='#' onclick='unlockApplicationName(this)' applicationName='"+applicationName+"'>[解锁]</a></div></br>";
 					     	}
 				     	}
 				     	$("#lockedApplications").append(fileListHtml); */
 				     }
 		        },
 		        "json"
 	   );
 		btn = thisBtn;
		btn.disabled = true; //将按钮置为不可点击
		btn.value = nums+'秒后可重新获取';
		clock = setInterval(doLoop, 1000); //一秒执行一次
 		
 	}
 	function login(){
 		var phoneNo = $("#userName").val();
 		var verificationCode = $('#SMSVerificationCode').val();
 		if(phoneNo=='' || verificationCode==''){
 			alert("电话或验证码为空");
 			return;
 		}
 		$.post("/login",
 				{
 			      phoneNo:phoneNo,
 			     verificationCode:verificationCode
 				},
 				function(data,status){
 					if("0" == data){
 						window.location.href = "/queryFee";
				     }else if("1" == data){
 				     	alert("电话或验证码为空");
 				     }else if("2" == data){
 				    	 alert("用户不存在");
 				     }else if("3" == data){
 				    	 alert("验证码错误");
 				     }else{
 				    	 alert("未知错误");
 				     }
 		        },
 		        "json"
 	   );
 	}
 	
 	
	var clock = '';
	var nums = 120;
	var btn;
	function sendCode(thisBtn)
	{ 
		btn = thisBtn;
		btn.disabled = true; //将按钮置为不可点击
		btn.value = nums+'秒后可重新获取';
		clock = setInterval(doLoop, 1000); //一秒执行一次
		getCode();
	}
	function doLoop()
	{
		nums--;
		if(nums > 0){
		 btn.value = nums+'秒后可重新获取';
		}else{
		 clearInterval(clock); //清除js定时器
		 btn.disabled = false;
		 btn.value = '点击发送验证码';
		 nums = 10; //重置时间
		}
	}
 </script>
</html>