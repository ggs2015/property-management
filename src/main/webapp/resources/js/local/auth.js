	var inputHtml = '<small>欢迎,</small>' + userName;
	$("#userName").html(inputHtml); 
	$('#phoneNo').val(phoneNo);
	//用户权限
	if(roleId == 5){
		$('#userManage').show();
	}else{
		$('#userManage').hide();
	}
	//缴费
	//开票
	if(roleId == 5 || roleId == 3){
		$('#write').show();
	}else{
		$('#write').hide();
	} 
