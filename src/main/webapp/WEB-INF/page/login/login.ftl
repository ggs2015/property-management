<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="utf-8" />
		<title>登录</title>

		<meta name="description" content="User login page" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />

		<!-- basic styles -->

		<link href="/resources/ace1.2/assets/css/bootstrap.min.css" rel="stylesheet" />
		<link rel="stylesheet" href="/resources/ace1.2/assets/css/font-awesome.min.css" />

		<!--[if IE 7]>
		  <link rel="stylesheet" href="/resources/ace1.2/assets/css/font-awesome-ie7.min.css" />
		<![endif]-->

		<!-- page specific plugin styles -->

		<!-- fonts -->

		<!-- <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Open+Sans:400,300" /> -->

		<!-- ace styles -->

		<link rel="stylesheet" href="/resources/ace1.2/assets/css/ace.min.css" />
		<link rel="stylesheet" href="/resources/ace1.2/assets/css/ace-rtl.min.css" />

		<!--[if lte IE 8]>
		  <link rel="stylesheet" href="/resources/ace1.2/assets/css/ace-ie.min.css" />
		<![endif]-->

		<!-- inline styles related to this page -->

		<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->

		<!--[if lt IE 9]>
		<script src="/resources/ace1.2/assets/js/html5shiv.js"></script>
		<script src="/resources/ace1.2/assets/js/respond.min.js"></script>
		<![endif]-->
	</head>

	<body class="login-layout">
		<div class="main-container">
			<div class="main-content">
				<div class="row">
					<div class="col-sm-10 col-sm-offset-1">
						<div class="login-container">
							<div class="center">
								<h1>
									<i class="icon-leaf green"></i>
									<span class="red"></span>
									<span class="white">物业管理系统</span>
								</h1>
								<h4 class="blue">&copy; 中冠物业</h4>
							</div>

							<div class="space-6"></div>

							<div class="position-relative">
								<div id="login-box" class="login-box visible widget-box no-border">
									<div class="widget-body">
										<div class="widget-main">
											<h4 class="header blue lighter bigger">
												<i class="icon-coffee green"></i>
												请输入您的信息
											</h4>

											<div class="space-6"></div>

											<form>
												<fieldset>
													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="text" id="userName" class="form-control" placeholder="手机号码" />
															<i class="icon-user"></i>
														</span>
													</label>

													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="text" id="SMSVerificationCode" class="form-control" placeholder="验证码" />
															<i class="icon-lock"></i>
														</span>
													</label>

													<div class="space"></div>

													<div class="clearfix">
														<label class="inline">
															<input type="button" style="height:32px;width:130px;background:#A020F0;color:white" value="点击发送验证码" onclick="getCode(this)" />
														</label>

														<button type="button" onclick="login();" class="width-35 pull-right btn btn-sm btn-primary">
															<i class="icon-key"></i>
															登录
														</button>
													</div>

													<div class="space-4"></div>
												</fieldset>
											</form>

										</div><!-- /widget-main -->

									</div><!-- /widget-body -->
								</div><!-- /login-box -->

							</div><!-- /position-relative -->
						</div>
					</div><!-- /.col -->
				</div><!-- /.row -->
			</div>
		</div><!-- /.main-container -->

		<!-- basic scripts -->

		<!--[if !IE]>

		<script src="http://ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>

		<![endif]-->

		<!--[if IE]>
			<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
		<![endif]-->

		<!--[if !IE]>

		<script type="text/javascript">
			window.jQuery || document.write("<script src='/resources/ace1.2/assets/js/jquery-2.0.3.min.js'>"+"<"+"/script>");
		</script>

		<![endif]-->

		<!--[if IE]>
			<script type="text/javascript">
			 window.jQuery || document.write("<script src='/resources/ace1.2/assets/js/jquery-1.10.2.min.js'>"+"<"+"/script>");
			</script>
		<![endif]-->

		<script type="text/javascript">
			if("ontouchend" in document) document.write("<script src='/resources/ace1.2/assets/js/jquery.mobile.custom.min.js'>"+"<"+"/script>");
		</script>

		<!-- inline scripts related to this page -->

		<script type="text/javascript">
			function show_box(id) {
				return false;
			 jQuery('.widget-box.visible').removeClass('visible');
			 jQuery('#'+id).addClass('visible');
			}
		</script>
	<script type="text/javascript" src="../resources/js/jquery.js"></script>
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
 				     }else if("5" == data){
 				    	 alert("时间间隔小于35s");
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
	var nums = 35;
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
	</body>
</html>
