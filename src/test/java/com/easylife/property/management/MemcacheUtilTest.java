package com.easylife.property.management;

import java.util.concurrent.TimeoutException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

public class MemcacheUtilTest {

	private static ApplicationContext ctx;

	public static void main(String[] args) {
		
		ctx = new FileSystemXmlApplicationContext(new String[]{"src/main/resource/applicationContext.xml"});  
        
		MemcachedClient mc = (MemcachedClient) ctx.getBean("memcachedClient");  
         
		//开始设值  
		try {
			System.out.println(mc.add("1",0,"222"));
			System.out.println(mc.add("1111", 100,"2222222"));
			mc.set("int", 100,5);  
			System.out.println(mc.get("name"));
			System.out.println(mc.get("int"));
			System.out.println(mc.get("double"));
			System.out.println(mc.get("1111"));
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MemcachedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
