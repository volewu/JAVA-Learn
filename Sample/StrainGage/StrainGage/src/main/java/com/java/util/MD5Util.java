package com.java.util;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.apache.shiro.crypto.hash.Md5Hash;

public class MD5Util {

	/**
	 * Md5º”√‹
	 * @param str
	 * @param salt
	 * @return
	 */
	public static String EncoderPwdByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		
		return new Md5Hash(str,"java").toString();
	}
	
	
	public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		System.out.println(EncoderPwdByMd5("123"));
	}
}
   