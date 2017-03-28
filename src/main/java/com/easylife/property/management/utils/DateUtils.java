package com.easylife.property.management.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	public static Boolean monthBeforeNow(Integer year, Integer month){
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		Integer nowYear = c.get(Calendar.YEAR);
		Integer nowMonth = c.get(Calendar.MONTH) + 1;
		if(year.intValue() < nowYear.intValue()){
			return true;
		}
		if(year.intValue() > nowYear.intValue()){
			return false;
		}
		if(year.intValue() == nowYear.intValue()){
			if(month.intValue() >= nowMonth.intValue()){
				return false;
			}else{
				return true;
			}
		}
		return true;
	}
	
	/**
	 * @Title: monthCompare  
	 * @Description: 不用
	 * @author liujun
	 * @param year
	 * @param month
	 * @return
	 */
	public static Integer monthCompare(Integer year, Integer month){
		Integer result = 0;
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		Integer nowYear = c.get(Calendar.YEAR);
		Integer nowMonth = c.get(Calendar.MONTH);
		if(year.intValue() < nowYear.intValue()){
			result = (nowYear - year - 2) * 12 + (12 - month + 1) + nowMonth;
		}
		if(year.intValue() > nowYear.intValue()){
		}
		if(year.intValue() == nowYear.intValue()){
			if(month.intValue() >= nowMonth.intValue()){
			}else{
				return nowMonth - month + 1;
			}
		}else{
		}
		return result;
	}
	
	public static String fromatDate(Integer year, Integer month){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month-1);
		return format.format(c.getTime());
	}
}
