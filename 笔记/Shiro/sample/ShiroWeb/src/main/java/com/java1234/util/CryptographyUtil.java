package com.java1234.util;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * Shiro 的加密
 * @User: vole
 * @date: 2018年3月5日上午8:30:26
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
	 * md5 encrytion：不可逆
	 * @param str
	 * @param salt :提高保密性，避免碰撞检测出密码，理论上该参数要放在配置文件中去
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
