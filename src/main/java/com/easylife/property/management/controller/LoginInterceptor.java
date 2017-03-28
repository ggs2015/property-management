package com.easylife.property.management.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.easylife.property.management.bean.ApplicationUserContext;
import com.easylife.property.management.model.EndUserInfo;

import net.rubyeye.xmemcached.MemcachedClient;

public class LoginInterceptor extends HandlerInterceptorAdapter{
	
	@Autowired
	private MemcachedClient memcachedClient;
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		boolean flag = false;
		/**
		HttpSession session = request.getSession();
		if(session == null || session.getAttribute("endUser") == null){
			//没有登陆信息，登陆验证失败
			return false;
		}
		*/
		Cookie[] cookies = request.getCookies();
		String sessionId = "";
		if((null != cookies) && (cookies.length > 0)){//从cookie中取值
			for (Cookie cookie : cookies) {
				if ((StringUtils.isNotBlank(cookie.getName())) && ("uid".equals(cookie.getName()))) {
					sessionId = cookie.getValue();
					break;
				}
			}
		}
		if(StringUtils.isNotEmpty(sessionId)){
			//从redis缓存去取用户名，如果不能匹配上，则需要重新登陆
			EndUserInfo endUserInfo = memcachedClient.get(sessionId);
			if(endUserInfo != null){
				//设置本地线程变量
				ApplicationUserContext.setUser(endUserInfo);
				flag = true;
			}
		}
		if(!flag){
			response.sendRedirect("/toLogin");
		}
		return flag;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}
}