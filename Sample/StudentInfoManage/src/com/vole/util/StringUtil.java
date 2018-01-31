package com.vole.util;

/**
 * 
 * User: vole
 * date: 2017年9月21日上午10:46:24
 * Function: 判断字符是否为空或者不为空
 */
public class StringUtil {

	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str))
			return true;
		else
			return false;
	}
	
	public static boolean isNotEmpty(String str) {
		if (str != null && !"".equals(str))
			return true;
		else
			return false;
	}

}
