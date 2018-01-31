package com.vole.util;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 
 * @User: vole
 * @date: 2017年9月22日下午5:08:25
 * @Function: date ---> string
 */
public class DateUtil {

	public static String formatDate(Date date, String format) {
		String result = "";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		if (date != null) {
			 result = sdf.format(date);
		}
		return result;

	}
	
	public static Date formatString(String str,String format) throws Exception{
		SimpleDateFormat sdf=new SimpleDateFormat(format);
		return sdf.parse(str);
	}
}
