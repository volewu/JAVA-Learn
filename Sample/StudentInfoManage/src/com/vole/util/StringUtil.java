package com.vole.util;

/**
 * 
 * User: vole
 * date: 2017��9��21������10:46:24
 * Function: �ж��ַ��Ƿ�Ϊ�ջ��߲�Ϊ��
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
