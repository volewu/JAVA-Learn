package com.java1234.util;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * Shiro �ļ���
 * @User: vole
 * @date: 2018��3��5������8:30:26
 * @Function:
 */
public class CryptographyUtil {

	/**
	 * base64 encryption
	 * @param str
	 * @return
	 */
	public static String encBase64(String str){
		return Base64.encodeToString(str.getBytes());
	}
	
	/**
	 * base64 deciphering
	 * @param str
	 * @return
	 */
	public static String decBase64(String str){
		return Base64.decodeToString(str);
	}
	
	/**
	 * md5 encrytion��������
	 * @param str
	 * @param salt :��߱����ԣ�������ײ�������룬�����ϸò���Ҫ���������ļ���ȥ
	 * @return
	 */
	public static String md5(String str,String salt){
		return new Md5Hash(str, salt).toString();
	}
	
	public static void main(String[] args) {
		String password = "123456";
		System.out.println("Base64 encryption : "+CryptographyUtil.encBase64(password));
		System.out.println("Base64 deciphering : "+CryptographyUtil.decBase64("MTIzNDU2"));
		System.out.println("MD5 encryption : "+CryptographyUtil.md5(password,"vole"));
	}
}
