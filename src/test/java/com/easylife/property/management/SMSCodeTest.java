package com.easylife.property.management;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.easylife.property.management.sms.client.AbsRestClient;
import com.easylife.property.management.sms.client.JsonReqClient;
import com.easylife.property.management.sms.client.XmlReqClient;

public class SMSCodeTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		boolean json=true;
		String accountSid="5a7568156d8c87a504449f7e52f54d76";
		String token="b7e67a09b9e634211ea03574cfa44e5b";
		String appId="3070415766274901a31f9a186ead3ecd";
//		String appId="17e5d3727e3346fc91805ddb7d105b56";
//		String templateId="1264";
		String templateId="29495";
		String to="18086036230";
		String para="222333";
		testTemplateSMS(json, accountSid,token,appId, templateId,to,para);

	}
	
	public static void testTemplateSMS(boolean json,String accountSid,String authToken,String appId,String templateId,String to,String param){
		try {
			Integer code = (int) ((Math.random()*9+1)*100000);
			System.out.println(code);
			param = code.toString();
			String result=InstantiationRestAPI(json).templateSMS(accountSid, authToken, appId, templateId, to, param);
			System.out.println("Response content is: " + result);
			JSONObject resultObj = JSON.parseObject(result);
//			String resp = resultObj.getString("resp");
//			JSONObject respObj = JSON.parseObject(resp);
//			System.out.println(respObj.getString("respCode"));
			System.out.println(resultObj.getJSONObject("resp").getString("respCode"));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	static AbsRestClient InstantiationRestAPI(boolean enable) {
		if(enable) {
			return new JsonReqClient();
		} else {
			return new XmlReqClient();
		}
	}

}
