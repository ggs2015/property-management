function loginOut(){
	var phoneNo = $("#phoneNo").val();
	if(phoneNo==''){
		alert("电话为空");
		return;
	}
	$.post("/loginOut",
			{
		      phoneNo:phoneNo
			},
			function(data,status){
				if("0" == data){
					window.location.href = "/toLogin";
		     }else if("1" == data){
			    	 alert("用户不存在");
			     }else{
			    	 alert("未知错误");
			     }
	        },
	        "json"
   );
}