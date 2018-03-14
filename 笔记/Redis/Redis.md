## Redis

--------------------目录--------------------

* 一、Redis 简介以及安装
  * 1.1 简介
  * 1.2 安装
* 二、 Jedis  入门实例



--------------------目录--------------------

#### 一、Redis 简介以及安装

##### 1.1 简介

> Redis 是一个开源的使用 ANSI [C语言](https://baike.baidu.com/item/C%E8%AF%AD%E8%A8%80) 编写、支持网络、可基于内存亦可持久化的日志型、Key-Value [数据库](https://baike.baidu.com/item/%E6%95%B0%E6%8D%AE%E5%BA%93)，并提供多种语言的 API。是一种非关系型数据库（NoSQL: Not noly SQL）。
>
> Redis的应用场景：
>
> 缓存
>
> 任务队列
>
> 网站访问统计
>
> 数据过期处理
>
> 应用排行榜
>
> 分布式集群架构中的session分离
>
 -----


> NoSQL 应对的是以下问题：
>
> High performance -高并发读写
>
> Huge Storage-海量数据的高效率存储和访问
>
> High Scalablility && High Availability 高可扩展性和高可用性

> 特点：
>
> 易扩展
>
> 灵活的数据模型
>
> 大数据量，高性能
>
> 高可用

> 高性能键值对数据库，支持的键值数据类型
>
> 字符串类型
>
> 列表类型
>
> 有序集合类型
>
> 散列类型
>
> 集合类型

##### 1.2 安装

* **安装 gcc  , 因为 redis 是 c 编写的**

```properties
[root@localhost ~]# yun -y install gcc-c++
```

* **安装 wget，并用 wget 方式下载 redis 压缩包，并解压以及编译**

```properties
[root@localhost ~]# yun -y install wget
[root@localhost ~]# wget http://download.redis.io/releases/redis-3.2.11.tar.gz
# 解压
[root@localhost ~]# tar -zxvf redis-3.2.11.tar.gz

[root@localhost ~]# ll
总用量 1524
-rw-------. 1 root root     961 3月  14 18:01 anaconda-ks.cfg
drwxrwxr-x. 6 root root    4096 9月  21 22:20 redis-3.2.11
-rw-r--r--. 1 root root 1550452 9月  21 22:20 redis-3.2.11.tar.gz

# cd 进入目录
[root@localhost ~]# cd redis-3.2.11
# 编译
[root@localhost redis-3.2.11]# make
```

* **安装 redis 到 /usr/local/redis/**

```properties
[root@localhost redis-3.2.11]# make PREFIX=/usr/local/redis install
# 进入目录
[root@localhost redis-3.2.11]# cd /usr/local/redis/
[root@localhost redis]# ll
总用量 56
drwxr-xr-x. 2 root root  4096 3月  14 18:21 bin
 
# cd 回到 root 并进入 redis-3.2.11，把 redis.conf 配置文件 复制到 redis 下， 后台启动需用
[root@localhost redis]# cd
[root@localhost ~]# ls
anaconda-ks.cfg  redis-3.2.11  redis-3.2.11.tar.gz
[root@localhost ~]# cd redis-3.2.11
[root@localhost redis-3.2.11]# ll
总用量 216
-rw-rw-r--.  1 root root 92766 9月  21 22:20 00-RELEASENOTES
-rw-rw-r--.  1 root root    53 9月  21 22:20 BUGS
-rw-rw-r--.  1 root root  1805 9月  21 22:20 CONTRIBUTING
-rw-rw-r--.  1 root root  1487 9月  21 22:20 COPYING
drwxrwxr-x.  7 root root  4096 3月  14 18:18 deps
-rw-rw-r--.  1 root root    11 9月  21 22:20 INSTALL
-rw-rw-r--.  1 root root   151 9月  21 22:20 Makefile
-rw-rw-r--.  1 root root  4223 9月  21 22:20 MANIFESTO
-rw-rw-r--.  1 root root  6834 9月  21 22:20 README.md
-rw-rw-r--.  1 root root 46695 9月  21 22:20 redis.conf
-rwxrwxr-x.  1 root root   271 9月  21 22:20 runtest
-rwxrwxr-x.  1 root root   280 9月  21 22:20 runtest-cluster
-rwxrwxr-x.  1 root root   281 9月  21 22:20 runtest-sentinel
-rw-rw-r--.  1 root root  7606 9月  21 22:20 sentinel.conf
drwxrwxr-x.  2 root root  4096 3月  14 18:20 src
drwxrwxr-x. 10 root root  4096 9月  21 22:20 tests
drwxrwxr-x.  7 root root  4096 9月  21 22:20 utils

[root@localhost redis-3.2.11]# cp redis-3.2.11/redis.conf /usr/local/redis/
```

* **启动和关闭 redis 服务**

```properties
[root@localhost redis-3.2.11]# cd /usr/local/redis/
# 此为前台启动
[root@localhost redis]# bin/redis-server

# 后台启动需要配置 redis.conf 文件
[root@localhost redis]# vi redis.conf
# 找到 daemonise no ，把 no 改为 yes ，wq 保存退出

# 关闭 redis
[root@localhost redis]# bin/redis-cli shutdown
# 后台启动
[root@localhost redis]# bin/redis-server redis.conf
[root@localhost redis]# ps -ef | grep -i redis
root      2482     1  0 19:05 ?        00:00:00 bin/redis-server 127.0.0.1:6379
root      2486  2430  0 19:05 pts/0    00:00:00 grep --color=auto -i redis
```

* **redis 基本使用**

```properties
# 进入 redis-cli 
[root@localhost redis]# bin/redis-cli
127.0.0.1:6379> set name vole
OK
127.0.0.1:6379> get name
"vole"
127.0.0.1:6379> keys *
1) "name"
127.0.0.1:6379> del name
(integer) 1
127.0.0.1:6379> keys *
(empty list or set)
127.0.0.1:6379>
```

#### 二、 Jedis  入门实例

##### 2.1 使用 Jedis 连接 Redis

* 创建一个 Maven 项目，并在 pom.xml 中添加依赖

```xml
		<dependency>
			<groupId>redis.clients</groupId>
			<artifactId>jedis</artifactId>
			<version>2.9.0</version>
		</dependency>
```

* 在 Centos 中配置防火墙，为 6379 端口开权限,并配置 **redis.conf**

```properties
# 配置防火墙
[root@localhost redis]# firewall-cmd --zone=public --add-port=6379/tcp --permanent
[root@localhost redis]# firewall-cmd --reload

# 配置 redis.conf
[root@localhost redis]# vi redis.conf
# 找到 bind 127.0.0.1 ，并注释掉

# 进入客户端，设置密码--此次的是不重启 Redis 设置密码（即重启后消失），如需要 redis 重启后密码依然有效，需在配置文件中找到 requirepass 项，并配置密码重启 redis
[root@localhost redis]# ./bin/redis-cli
127.0.0.1:6379> config set requirepass 123456
ok
127.0.0.1:6379> quit
[root@localhost redis]# ./bin/redis-cli
127.0.0.1:6379> auth 123456
OK
```

* 测试 JedisTest.java

```java
public class JedisTest {

	public static void main(String[] args) {
		Jedis jedis = new Jedis("192.168.42.77", 6379);// 创建客户端 设置IP和端口
		jedis.auth("123456"); // 设置密码
		jedis.set("name", "gakki");// 设置值
		String value = jedis.get("name"); // 获取值
		System.out.println(value);
		jedis.close();// 释放连接资源
	}

}
// 打印结果----gakki
```
##### 2.2 Jedis 连接池的使用

>  连接池: 是创建和管理一个连接的缓冲池的技术，这些连接准备好被任何需要它们的[线程](https://baike.baidu.com/item/%E7%BA%BF%E7%A8%8B)使用.

```java
/**
 * 连接池的使用 
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
```

#### 三、Redis 数据结构

##### 3.1 Redis  数据类型介绍

五种数据类型：

* 字符串（String）
* 字符串列表（list）
* 有序字符串集合（sorted set）
* 哈希（hash）
* 字符串集合（set）

> Key 定义的注意点：不要过长与果断，统一的命名规范



##### 3.2 Redis  数据结构之字符串类型（String）

> 存储String：二进制安全的，存入和获取的数据相同，Value最多可以容纳的数据长度是512M

存储 String 常用指令：

* 赋值:    **set key value**

```properties
127.0.0.1:6379> set name gakki
OK
```

* 取值:   **get key**

```properties
127.0.0.1:6379> get name
"gakki"
127.0.0.1:6379> getset name vole  # 获取并设置值
"gakki"
127.0.0.1:6379> get name
"vole"
```

* 删除:    **del key**

```properties
127.0.0.1:6379> keys *
1) "name"
127.0.0.1:6379> del name
(integer) 1
127.0.0.1:6379> keys *
(empty list or set)
```

* 数值增减:   假如没有定义 则默认0 ,假如非数值类型 则报错, **自增 --> incr key**    ;**自减--> decr key**

```properties
127.0.0.1:6379> incr n
(integer) 1
127.0.0.1:6379> get n
"1"
127.0.0.1:6379> set nn 2
OK
127.0.0.1:6379> incr nn
(integer) 3
127.0.0.1:6379> get nn
"3"
127.0.0.1:6379> incr name
(error) ERR value is not an integer or out of range
127.0.0.1:6379> decr nn
(integer) 2
127.0.0.1:6379> get nn
"2"
```

* 扩展命令:   **指定增量值-->incrby key value**  ; **指定减量值--> decrby key vaule**    ; **追加字符-->append key value**    假如没定义直接赋值

```properties
127.0.0.1:6379> incrby nn 8
(integer) 10
127.0.0.1:6379> get nn
"10"
127.0.0.1:6379> decrby nn 5
(integer) 5
127.0.0.1:6379> append name gakki
(integer) 9
127.0.0.1:6379> get name
"volegakki"
```

##### 3.3 Redis  数据结构之哈希类型

> 存储Hash：String key 和String Value 的 Map 容器，每一个 Hash 可以存储 4294967295 个键值对

存储 Hash 常用命令：

* 赋值 : **单个--> hset key k v**  ; **多个--> hmset key k1 v1 k2 v2**
* 取值：**单个-->hget key k** ;**多个-->hmget key k1 k2**  ;  **取 Hash 中使用 k&v -->hgetall key**     

```properties
127.0.0.1:6379> hset h1 username gakki
(integer) 1
127.0.0.1:6379> hset h1 password 123456
(integer) 1
127.0.0.1:6379> hget h1 username
"gakki"
127.0.0.1:6379> hget h1 password
"123456"
127.0.0.1:6379> hmset h2 username vole password 123
OK
127.0.0.1:6379> hmget h2 username password  
1) "vole"
2) "123"
127.0.0.1:6379> hgetall h2 
1) "username"
2) "vole"
3) "password"
4) "123"
```

* 删除： **删除一个--> hedl key k1 k2**  

```properties
127.0.0.1:6379> hdel h2 username password
(integer) 2
127.0.0.1:6379> hgetall h2
(empty list or set)
```

* 增加数字: **增加--> hset key k3 v3**    ; **在原基础上 +v --hincrby key k3 v**

```properties
127.0.0.1:6379> hset h1 age 20
(integer) 1
127.0.0.1:6379> hincrby h1 age 5
(integer) 25
127.0.0.1:6379> hgetall h1
1) "username"
2) "gakki"
3) "password"
4) "123456"
5) "age"
6) "25"
```

* 判断字段是否存在: **hexists key k1--------1(true)&0(false)**

```properties
127.0.0.1:6379> hexists h1 age
(integer) 1
127.0.0.1:6379> hexists h1 age2
(integer) 0
```

* 获取hash属性个数: **hlen key**

```properties
127.0.0.1:6379> hlen h1
(integer) 3
```

* 获取 hash 所有属性名称: **hkeys key**  & **hvals key**

```properties
127.0.0.1:6379> hkeys h1
1) "username"
2) "password"
3) "age"
127.0.0.1:6379> hvals h1
1) "gakki"
2) "123456"
3) "25"
```

##### 3.4 **Redis的数据结构之List**

> 存储 list：ArrayList 使用数组方式；LinkedList使用双向链接方式
>
> 双向链接表中增加数据
>
> 双向链接表中删除数据

存储list常用命令：

* 两端添加
* 两端弹出
* 扩展命令

```properties
push 方式添加

从左边开始添加
127.0.0.1:6379> lpush l1 a b c d
(integer) 4
127.0.0.1:6379> lpush l1 1 2 3 4
(integer) 8


lrange 获取指定方位的集合元素
从第1个开始 到倒数第一个 也就是最后一个 也就是 所有数据
127.0.0.1:6379> lrange l1 0 -1
1) "4"
2) "3"
3) "2"
4) "1"
5) "d"
6) "c"
7) "b"
8) "a"


rpush 从右端开始添加（一般人比较习惯这种方式）
127.0.0.1:6379> rpush l2 a b c d
(integer) 4
127.0.0.1:6379> rpush l2 1 2 3 4
(integer) 8
127.0.0.1:6379> lrange l2 0 -1
1) "a"
2) "b"
3) "c"
4) "d"
5) "1"
6) "2"
7) "3"
8) "4"


lpop 左侧弹出集合元素
rpop 右侧弹出集合元素
127.0.0.1:6379> lrange l2 0 -1
1) "b"
2) "c"
3) "d"
4) "1"
5) "2"
6) "3"
7) "4"
127.0.0.1:6379> rpop l2
"4"
127.0.0.1:6379> lrange l2 0 -1
1) "b"
2) "c"
3) "d"
4) "1"
5) "2"
6) "3"


llen 查看元素个数
127.0.0.1:6379> llen l2
(integer) 6


lpushx 集合头部插入元素
127.0.0.1:6379> lpushx l2 xx
(integer) 7
127.0.0.1:6379> lrange l2 0 -1
1) "xx"
2) "b"
3) "c"
4) "d"
5) "1"
6) "2"
7) "3"


rpushx 集合尾部插入元素
127.0.0.1:6379> rpushx l2 yy
(integer) 8
127.0.0.1:6379> lrange l2 0 -1
1) "xx"
2) "b"
3) "c"
4) "d"
5) "1"
6) "2"
7) "3"
8) "yy"


lpush 集合头部插入多个元素
127.0.0.1:6379> lpush l2 a1 a2 
(integer) 10
127.0.0.1:6379> lrange l2 0 -1
 1) "a2"
 2) "a1"
 3) "xx"
 4) "b"
 5) "c"
 6) "d"
 7) "1"
 8) "2"
 9) "3"
10) "yy"


127.0.0.1:6379> rpush l2 a3 a4
(integer) 12
127.0.0.1:6379> lrange l2 0 -1
 1) "a2"
 2) "a1"
 3) "xx"
 4) "b"
 5) "c"
 6) "d"
 7) "1"
 8) "2"
 9) "3"
10) "yy"
11) "a3"
12) "a4"


lrem 从指定方向删除指定个数的指定元素
先加点数据搞个新集合l3
127.0.0.1:6379> lpush l3 1 3 2 3 2 1 2 1 3
(integer) 9
127.0.0.1:6379> lrange l3 0 -1
1) "3"
2) "1"
3) "2"
4) "1"
5) "2"
6) "3"
7) "2"
8) "3"
9) "1"


从左边开始删除2个1
127.0.0.1:6379> lrem l3 2 1
(integer) 2
127.0.0.1:6379> lrange l3 0 -1
1) "3"
2) "2"
3) "2"
4) "3"
5) "2"
6) "3"
7) "1"


从右边开始删除2个3
127.0.0.1:6379> lrem l3 -2 3
(integer) 2
127.0.0.1:6379> lrange l3 0 -1
1) "3"
2) "2"
3) "2"
4) "2"
5) "1"


删除所有2
127.0.0.1:6379> lrem l3 0 2
(integer) 3
127.0.0.1:6379> lrange l3 0 -1
1) "3"
2) "1"


lset 设置集合指定索引的值
127.0.0.1:6379> lrange l1 0 -1
1) "4"
2) "3"
3) "2"
4) "1"
5) "d"
6) "c"
7) "b"
8) "a"


索引从0开始
127.0.0.1:6379> lset l1 3 xxxx
OK
127.0.0.1:6379> lrange l1 0 -1
1) "4"
2) "3"
3) "2"
4) "xxxx"
5) "d"
6) "c"
7) "b"
8) "a"


linsert 在集合里插入指定元素
在xxxx元素之前插入aa
127.0.0.1:6379> linsert l1 before xxxx aa
(integer) 9
127.0.0.1:6379> lrange l1 0 -1
1) "4"
2) "3"
3) "2"
4) "aa"
5) "xxxx"
6) "d"
7) "c"
8) "b"
9) "a"


在xxxx元素之后插入bb
127.0.0.1:6379> linsert l1 after xxxx bb
(integer) 10
127.0.0.1:6379> lrange l1 0 -1
 1) "4"
 2) "3"
 3) "2"
 4) "aa"
 5) "xxxx"
 6) "bb"
 7) "d"
 8) "c"
 9) "b"
10) "a"


rpoplpush 把A集合尾部元素弹出并插入到B集合头部
127.0.0.1:6379> rpush l4 a b c
(integer) 3
127.0.0.1:6379> rpush l5 1 2 3
(integer) 3
127.0.0.1:6379> lrange l4 0 -1
1) "a"
2) "b"
3) "c"
127.0.0.1:6379> lrange l5 0 -1
1) "1"
2) "2"
3) "3"
127.0.0.1:6379> rpoplpush l4 l5
"c"
127.0.0.1:6379> lrange l4 0 -1
1) "a"
2) "b"
127.0.0.1:6379> lrange l5 0 -1
1) "c"
2) "1"
3) "2"
4) "3"
```

* ​