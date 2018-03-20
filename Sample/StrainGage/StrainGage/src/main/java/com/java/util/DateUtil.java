package com.java.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public static String formatDate(Date date, String format){
		  
		String result="";
		SimpleDateFormat sdf=new SimpleDateFormat(format);
		if (date!=null) {
			result=sdf.format(date);
		}
		return result;
	}
	
	public static Date formatString(String str, String format) throws ParseException{
		
		SimpleDateFormat sdf=new SimpleDateFormat(format);
		
		return sdf.parse(str);
	}
	
  
  public static String formatTimeStamp(Timestamp timestamp, String format){
	  
		String result="";
		SimpleDateFormat sdf=new SimpleDateFormat(format);
		if (timestamp!=null) {
			result=sdf.format(timestamp);
		}
		return result;
	}
}
