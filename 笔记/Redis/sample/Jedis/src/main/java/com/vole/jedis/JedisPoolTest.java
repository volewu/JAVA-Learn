package com.vole.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 连接池的使用
 * 
 * @User: vole
 * @date: 2018年3月14日下午2:40:37
 * @Function:
 */
public class JedisPoolTest {

	public static void main(String[] args) {
		JedisPoolConfig config = new JedisPoolConfig(); // 连接池的配置对象
		config.setMaxTotal(100);// 设置最大连接数
		config.setMaxIdle(10);// 设置最大空闲连接数

		JedisPool jedisPool = new JedisPool(config, "192.168.42.77", 6379);
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.auth("123456"); // 设置密码
			jedis.set("name", "gakki");// 设置值
			String value = jedis.get("name"); // 获取值
			System.out.println(value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null)
				jedis.close();// 释放连接资源
			if (jedisPool != null)
				jedisPool.close();
		}

	}
}
