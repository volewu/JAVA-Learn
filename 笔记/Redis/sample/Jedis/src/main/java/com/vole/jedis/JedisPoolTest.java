package com.vole.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * ���ӳص�ʹ��
 * 
 * @User: vole
 * @date: 2018��3��14������2:40:37
 * @Function:
 */
public class JedisPoolTest {

	public static void main(String[] args) {
		JedisPoolConfig config = new JedisPoolConfig(); // ���ӳص����ö���
		config.setMaxTotal(100);// �������������
		config.setMaxIdle(10);// ����������������

		JedisPool jedisPool = new JedisPool(config, "192.168.42.77", 6379);
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.auth("123456"); // ��������
			jedis.set("name", "gakki");// ����ֵ
			String value = jedis.get("name"); // ��ȡֵ
			System.out.println(value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null)
				jedis.close();// �ͷ�������Դ
			if (jedisPool != null)
				jedisPool.close();
		}

	}
}
