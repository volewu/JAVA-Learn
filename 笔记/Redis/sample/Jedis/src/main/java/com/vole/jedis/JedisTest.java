package com.vole.jedis;

import redis.clients.jedis.Jedis;

public class JedisTest {

	public static void main(String[] args) {
		Jedis jedis = new Jedis("192.168.42.77", 6379);// �����ͻ��� ����IP�Ͷ˿�
		jedis.auth("123456"); // ��������
		jedis.set("name", "gakki");// ����ֵ
		String value = jedis.get("name"); // ��ȡֵ
		System.out.println(value);
		jedis.close();// �ͷ�������Դ
	}

}
