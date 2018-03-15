

## Redis

--------------------目录--------------------

* 一、Redis 简介以及安装
  * 1.1 简介
  * 1.2 安装
* 二、 Jedis  入门实例


  * 2.1 使用 Jedis  连接 Redis
  * 2.2 Jedis  连接池使用
* 三、Redis 数据结构
  * 3.1 Redis  数据类型介绍
  * 3.2 Redis  数据结构之字符串类型
  * 3.3 Redis 数据结构之哈希类型
  * 3.4 Redis  数据结构之 List  类型
  * 3.5 Redis  数据结构之 Set  类型
  * 3.6 Redis  数据结构之 sorted-set 
  * 3.7 Redis  之 Keys  通用操作
* 四、Redis 持久化
  * 4.1 Redis  持久化介绍
  * 4.2 Redis  的持久化之 RDB 方式
  * 4.3 Redis  的持久化之 AOF  方式
* 五、Redis 集群
  * 5.1 Redis 集群概述
  * 5.2 Redis 单机多节点集群
  * 5.3 Redis 多机多节点集群


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

##### 3.5 Redis  数据结构之 Set 

> 存储Set： 和 List 类型不同的是，Set 集合中不允许出现重复的元素，且 Set 是无序的，Set 可包含的最大元素数量是 4294967295; 跟踪一些唯一性数据,用于维护数据对象之间的关联关系

存储set常用命令：

* 添加/删除元素：**sadd key member [member ...] **        删除： **srem key member [member ...]**
* 获取集合中的元素:  **smembers key**
* 集合中的差集运算
* 集合中的交集运算
* 集合中的并集元算
* 扩展命令

```properties
添加set元素
127.0.0.1:6379> sadd set1  a b c
(integer) 3
添加三个元素

smembers key
查看指定key集合元素
127.0.0.1:6379> smembers set1
1) "c"
2) "b"
3) "a"

继续添加元素 发现 重复元素不再添加
127.0.0.1:6379> sadd set1 a d e
(integer) 2
127.0.0.1:6379> smembers set1
1) "c"
2) "d"
3) "b"
4) "a"
5) "e"




srem key member [member ...]
删除元素
127.0.0.1:6379> srem set1 a d
(integer) 2
127.0.0.1:6379> smembers set1
1) "b"
2) "c"
3) "e"


sismember key member
判断某个元素是否存在 返回1 表示存在 返回0表示不存在
127.0.0.1:6379> sismember set1 a
(integer) 0
127.0.0.1:6379> sismember set1 b
(integer) 1



sdiff 计算差集
127.0.0.1:6379> sadd set2 a b c
(integer) 3
127.0.0.1:6379> sadd set3 b c d e
(integer) 4
127.0.0.1:6379> sdiff set2 set3
1) "a"
127.0.0.1:6379> sdiff set3 set2
1) "d"
2) "e"
我们发现 集合的顺序不同 结果不一样 根据前者参考



sinter 计算交集
127.0.0.1:6379> sinter set2 set3
1) "c"
2) "b"



sunion 计算并集
127.0.0.1:6379> sunion set2 set3
1) "e"
2) "a"
3) "b"
4) "c"
5) "d"



scard 计算元素总数
127.0.0.1:6379> smembers set1
1) "b"
2) "c"
3) "e"
127.0.0.1:6379> scard set1
(integer) 3


srandmember 随机取一个元素
127.0.0.1:6379> srandmember set1
"c"
127.0.0.1:6379> srandmember set1
"e"


sdiffstore 把差集结果存储到新集合中
127.0.0.1:6379> smembers set2
1) "c"
2) "b"
3) "a"

127.0.0.1:6379> smembers set3
1) "c"
2) "e"
3) "d"
4) "b"
127.0.0.1:6379> sdiffstore r1 set2 set3
(integer) 1
127.0.0.1:6379> smembers r1
1) "a"


sinterstore 把交集结果存储到新集合中
127.0.0.1:6379> sinterstore r2 set2 set3
(integer) 2
127.0.0.1:6379> smembers r2
1) "c"
2) "b"


sunionstore 把并集结果存储到新集合中
127.0.0.1:6379> sunionstore r3 set2 set3
(integer) 5
127.0.0.1:6379> smembers r3
1) "e"
2) "a"
3) "b"
4) "c"
5) "d"
```

##### 3.6 Redis  数据结构之 sorted-set  类型

> 存储Sorted-Set： 和Set 的区别 Sorted-Set 中的成员在集合中的位置是有序的，使用于大型在线游戏积分排行榜、构建索引数据等

存储Sorted-set常用命令

* 添加元素
* 获得元素
* 删除元素
* 范围查询
* 扩展命令

```properties
zadd 添加元素 里面包括评分和值
127.0.0.1:6379> zadd sort1 5 a 4 b 6 c
(integer) 3
我们添加集合 sort1 元素是 a,b,c 评分分别是 5,4,6
集合里的排序是根据评分从小到大排序的；


zrange 是查找元素 -1 代表是最后一个
127.0.0.1:6379> zrange sort1 0 -1
1) "b"
2) "a"
3) "c"


假如我们继续添加元素
这里分两种情况
假如次元素集合里面已经有了，则覆盖
我们继续添加 b 此时评分改成 7 
127.0.0.1:6379> zadd sort1 7 b
(integer) 0

通过 zscore 获取 b 的评分
127.0.0.1:6379> zscore sort1 b
"7"
发现已经修改了；
127.0.0.1:6379> zrange sort1 0 -1
1) "a"
2) "c"
3) "b"

假如添加的元素不在集合里，则添加进去
127.0.0.1:6379> zadd sort1 9 d
(integer) 1
127.0.0.1:6379> zrange sort1 0 -1
1) "a"
2) "c"
3) "b"
4) "d"


删除元素 zrem
127.0.0.1:6379> zrem sort1 b
(integer) 1
127.0.0.1:6379> zrange sort1 0 -1
1) "a"
2) "c"
3) "d"


zcard 查看集合里的元素个数
127.0.0.1:6379> zcard sort1
(integer) 3


withscores  把评分也显示出来
127.0.0.1:6379> zrange sort1 0 -1 withscores 
1) "a"
2) "5"
3) "c"
4) "6"
5) "d"
6) "9"


zrevrange 降序排列
127.0.0.1:6379> zrevrange sort1 0 -1 withscores
1) "d"
2) "9"
3) "c"
4) "6"
5) "a"
6) "5"


我们再加两个元素
127.0.0.1:6379> zadd sort1 10 e  5 f
(integer) 2


zremrangebyrank 根据排名来删除元素  删除 3 个
127.0.0.1:6379> zremrangebyrank sort1 0 2
(integer) 3
127.0.0.1:6379> zrange sort1 0 -1 withscores
1) "d"
2) "9"
3) "e"
4) "10"


再添加元素
127.0.0.1:6379> zadd sort1 11 f 16 g 18 h
(integer) 3
127.0.0.1:6379> zrange sort1 0 -1 withscores
 1) "d"
 2) "9"
 3) "e"
 4) "10"
 5) "f"
 6) "11"
 7) "g"
 8) "16"
 9) "h"
10) "18"


zremrangebyscore 根据具体评分范围来删除元素
127.0.0.1:6379> zremrangebyscore sort1 10 16
(integer) 3
127.0.0.1:6379> zrange sort1 0 -1 withscores
1) "d"
2) "9"
3) "h"
4) "18"


再添加元素
127.0.0.1:6379> zadd sort1 20 i 23 j 30 k
(integer) 3
127.0.0.1:6379> zrange sort1 0 -1 withscores
 1) "d"
 2) "9"
 3) "h"
 4) "18"
 5) "i"
 6) "20"
 7) "j"
 8) "23"
 9) "k"
10) "30"


zrangebyscore 根据评分范围来查找元素
127.0.0.1:6379> zrangebyscore sort1 18 23 withscores 
1) "h"
2) "18"
3) "i"
4) "20"
5) "j"
6) "23"


limit 限定查找起始 类似分页
127.0.0.1:6379> zrangebyscore sort1 18 23 withscores limit 0 2
1) "h"
2) "18"
3) "i"
4) "20"


zincrby 给指定元素加分
127.0.0.1:6379> zincrby sort1 5 h
"23"


127.0.0.1:6379> zrange sort1 0 -1 withscores
 1) "d"
 2) "9"
 3) "i"
 4) "20"
 5) "h"
 6) "23"
 7) "j"
 8) "23"
 9) "k"
10) "30"


zcount 查找指定评分范围的元素个数
127.0.0.1:6379> zcount sort1 20 23
(integer) 3
```

##### 3.7 Redis  之 Keys 通用操作

* **keys * ---- 显示所有 key**
* **keys s* ----查找所有以 s 开头的 key**
* **keys s?----查找所有 s 开头 后面紧跟任意一个字符的 key**
* **exists ----判断 key 是否存在 1表示存在  0表示不存在** 
* **rename----对 key 重命名**
* **expire ----设置有效时间**
* **ttl----查看剩余时间**
* **type显示类型**

```properties
keys * 显示所有 key
127.0.0.1:6379> keys *
 1) "sort1"
 2) "l2"
 3) "set2"
 4) "r1"
 5) "h1"
 6) "n2"
 7) "l3"
 8) "r2"
 9) "s1"
10) "set3"
11) "set1"
12) "r3"
13) "n"
14) "n3"
15) "nn"
16) "l1"


查找所有以 s 开头的 key
用 s*  * 代表任意字符
127.0.0.1:6379> keys s*
1) "sort1"
2) "set2"
3) "s1"
4) "set3"
5) "set1"


查找所有 s 开头 后面紧跟任意一个字符的 key
127.0.0.1:6379> keys s?
1) "s1"


del 删除 key
127.0.0.1:6379> del n2 n3 nn
(integer) 3


exists 判断 key 是否存在 1 表示存在  0 表示不存在
127.0.0.1:6379> exists n2
(integer) 0
127.0.0.1:6379> exists l1
(integer) 1


get 获取元素
127.0.0.1:6379> get n
"1"


rename 对 key 重命名
127.0.0.1:6379> rename n n2
OK
127.0.0.1:6379> get n
(nil)
127.0.0.1:6379> get n2
"1"


expire 设置 n2 有效时间
127.0.0.1:6379> expire n2 120
(integer) 1


ttl 查看剩余时间
127.0.0.1:6379> ttl n2
(integer) 116


type 显示类型
127.0.0.1:6379> type n2
string
127.0.0.1:6379> type l1
list
127.0.0.1:6379> type s1
string
127.0.0.1:6379> type sort1
zset
```

#### 四、 Redis  持久化

##### 4.1 Redis  持久化介绍

> 所有的数据都存在内存中，从内存当中同步到硬盘上，这个过程叫做持久化过程。
>
> 持久化操作，两种方式：rdb方式、aof方式，可以单独使用或者结合使用。
>
> 使用方法：
>
> rdb持久化方法：在指定的时间间隔写入硬盘；
>
> aof方式：将以日志，记录每一个操作，服务器启动后就构建数据库；
>
>  配置可以禁用 持久化功能。也可以同时使用两种方式

##### 4.2 Redis 的持久化之 RDB 方式

* **优点：只有一个文件，时间间隔的数据，可以归档为一个文件，方便压缩转移（就一个文件）**
* **劣势：如果宕机，数据损失比较大，因为它是没一个时间段进行持久化操作的。也就是积攒的数据比较多，一旦懵逼，就彻底懵逼了**
* 配置：查看 redis.conf

```properties
[root@localhost redis]# vi redis.conf 
```

![RDB_save1](https://raw.githubusercontent.com/volewu/JAVA-Learn/master/笔记/Redis/photo/RDB_save1.PNG)

> * save 900 1 表示 每900秒内至少有1个kery发生变化，就持久化
> * save 300 10表示 每300秒内至少有10个key发生变化，就持久化
> * save 60 10000表示 每60秒内至少有10000个key发生变化，就持久化

往下拉：

![RDB_save2](https://raw.githubusercontent.com/volewu/JAVA-Learn/master/笔记/Redis/photo/RDB_save2.PNG)

> dbfilename dump.rdb : 存储的文件名
>
> dir ./  :  存储的路径

* 模拟测试

```properties
shutdown关闭下redis
127.0.0.1:6379> shutdown
not connected> exit
[root@localhost redis]# ll
总用量 52
drwxr-xr-x. 2 root root   134 7月   6 09:18 bin
-rw-r--r--. 1 root root    99 7月  18 10:41 dump.rdb
-rw-r--r--. 1 root root 46697 7月  18 10:41 redis.conf

然后删除掉rdb文件，再启动redis
[root@localhost redis]# rm -rf dump.rdb 
[root@localhost redis]# ./bin/redis-server ./redis.conf 
[root@localhost redis]# ./bin/redis-cli
127.0.0.1:6379> keys *
(empty list or set)

我们搞几个key，然后shutdown save 保存退出
127.0.0.1:6379> set n1 1
OK
127.0.0.1:6379> set n2 2
OK
127.0.0.1:6379> set n3 3
OK
127.0.0.1:6379> shutdown save
not connected> exit


假如这时候 我们再重启 redis 这时候启动过程会进程 rdb check 验证 然后加载redis目录下 rdb 文件；加载数据；
再次启动
[root@localhost redis]# ./bin/redis-server ./redis.conf 
[root@localhost redis]# ./bin/redis-cli
127.0.0.1:6379> keys *
1) "n1"
2) "n3"
3) "n2"
说明是数据加载进来了；


恢复数据的话 我们只需要把备份文件搞到redis下即可
再复制过来即可：
[root@localhost redis]# cp /root/dump.rdb /usr/local/redis/
cp：是否覆盖"/usr/local/redis/dump.rdb"？ y

[root@localhost redis]# ./bin/redis-server ./redis.conf 
[root@localhost redis]# ./bin/redis-cli
127.0.0.1:6379> keys *
1) "n2"
2) "n1"
3) "n3"


这时候 数据就有了 这就是恢复过程；
```

* **平时我们可以定期把 rdb 文件备份到指定地方 需要恢复的时候 直接把 rdb 搞到 redis 下即可；有工具提供的**

##### 4.3 Redis  的持久化之 AOF  方式

 * **AOF方式：将以日志，记录每一个操作**
 * **优势：安全性相对RDB方式高很多；**
 * **劣势：效率相对RDB方式低很多；**
 * 配置：查看 redis.conf

```properties
[root@localhost redis]# vi redis.conf 
```
![AOF](https://raw.githubusercontent.com/volewu/JAVA-Learn/master/笔记/Redis/photo/AOF.PNG)

* appendonly no 默认关闭 aof 方式 我们修改成 yes 就开启

* 这里是三种同步策略：

  **always 是 只要发生修改，立即同步 （推荐实用 安全性最高）**

  **everysec 是 每秒同步一次**

  **no是不同步 **

* 模拟测试

```properties
先重置数据
[root@localhost redis]# rm -rf dump.rdb 
[root@localhost redis]# ll
总用量 48
drwxr-xr-x. 2 root root   134 7月  18 11:05 bin
-rw-r--r--. 1 root root 46698 7月  18 12:14 redis.conf

启动redis
[root@localhost redis]# ./bin/redis-server ./redis.conf 
[root@localhost redis]# ./bin/redis-cli
127.0.0.1:6379> keys *
(empty list or set)
目前数据库是空

添加数据
127.0.0.1:6379> set n gakki
OK
127.0.0.1:6379> set n2 vole
OK
127.0.0.1:6379> shutdown nosave
not connected> exit

[root@localhost redis]# ll
总用量 56
-rw-r--r--. 1 root root    85 3月  15 18:23 appendonly.aof
drwxr-xr-x. 2 root root  4096 3月  14 18:21 bin
-rw-r--r--. 1 root root 46696 3月  15 18:20 redis.conf

重启 redis
[root@localhost redis]# bin/redis-server redis.conf
[root@localhost redis]# bin/redis-cli
127.0.0.1:6379> auth 123456
OK
127.0.0.1:6379> keys *
1) "n2"
2) "n"
127.0.0.1:6379>




我们把aof文件剪切到其他地方去 然后启动试下
[root@localhost redis]# mv appendonly.aof /root/
[root@localhost redis]# ll
总用量 48
drwxr-xr-x. 2 root root   134 7月  18 11:05 bin
-rw-r--r--. 1 root root 46698 7月  18 12:14 redis.conf
[root@localhost redis]# ./bin/redis-server ./redis.conf 
[root@localhost redis]# ./bin/redis-cli
127.0.0.1:6379> keys *
(empty list or set)
没数据；


我们再把aof文件复制回来；
[root@localhost redis]# cp /root/appendonly.aof /usr/local/redis/
cp：是否覆盖"/usr/local/redis/appendonly.aof"？ y
[root@localhost redis]# ll
总用量 52
-rw-r--r--. 1 root root   107 7月  18 12:22 appendonly.aof
drwxr-xr-x. 2 root root   134 7月  18 11:05 bin
-rw-r--r--. 1 root root 46698 7月  18 12:14 redis.conf
[root@localhost redis]# ./bin/redis-server ./redis.conf 
[root@localhost redis]# ./bin/redis-cli
127.0.0.1:6379> keys *
1) "n1"
2) "n3"
3) "n2"

我们发现 以及有数据了
```

* **小结： 我们平时可以把 aof 文件定期备份 然后需要的时候 拷贝到 redis下 重启即可；**

#### 五、Redis 集群

##### 5.1 Redis 集群概述

>Redis Cluster 与Redis3.0.0 同时发布，以此结束了Redis 无官方集群方案的时代，目前，我用的 Redis 是 3.2.11。
>
>Redis cluster 是去中心化，去中间件的，也就是说，集群中的每个节点都是平等的关系，都是对等的，每个节点都保存各自的数据和整个集群的状态。每个节点都和其他所有节点连接，而且这些连接保持活跃，这样就保证了我们只需要连接集群中的任意一个节点，就可以获取到其他节点的数据。
>
>那么 redis 是如何合理分配这些节点和数据的呢？
>
>Redis 集群没有并使用传统的一致性哈希来分配数据，而是采用另外一种叫做哈希槽 (hash slot)的方式来分配的。redis cluster  默认分配了 16384 个 slot，当我们 set 一个 key  时，会用 CRC16 算法来取模得到所属的slot，然后将这个 key  分到哈希槽区间的节点上，具体算法就是：CRC16(key) % 16384。
>
>注意的是：必须要 3 个以后的主节点，否则在创建集群时会失败，我们在后续会实践到。
>
>所以，我们假设现在有 3 个节点已经组成了集群，分别是：A, B, C 三个节点，它们可以是一台机器上的三个端口，也可以是三台不同的服务器。那么，采用哈希槽 (hash slot)的方式来分配 16384 个 slot 的话，它们三个节点分别承担的 slot  区间是：
>
>节点 A 覆盖 0－5460;
>
>节点 B 覆盖 5461－10922;
>
>节点 C 覆盖 10923－16383.
>
>那么，现在我想设置一个 key ,比如叫 my_name:set my_name wind
>
>按照 redis cluster 的哈希槽算法：CRC16(‘my_name’)%16384 = 2412。 那么就会把这个 key 的存储分配到 A 上了。
>
>同样，当我连接(A,B,C)任何一个节点想获取 my_name 这个 key 时，也会这样的算法，然后内部跳转到 B 节点上获取数据。
>
>这种哈希槽的分配方式有好也有坏，好处就是很清晰，比如我想新增一个节点 D，redis cluster 的这种做法是从各个节点的前面各拿取一部分 slot 到 D 上，我会在接下来的实践中实验。大致就会变成这样：
>
>节点 A 覆盖 1365-5460
>
>节点 B 覆盖 6827-10922
>
>节点 C 覆盖 12288-16383
>
>节点 D 覆盖 0-1364,5461-6826,10923-12287
>
>同样删除一个节点也是类似，移动完成后就可以删除这个节点了。
>
>Redis Cluster 主从模式
>
>redis cluster 为了保证数据的高可用性，加入了主从模式，一个主节点对应一个或多个从节点，主节点提供数据存取，从节点则是从主节点拉取数据备份，当这个主节点挂掉后，就会有这个从节点选取一个来充当主节点，从而保证集群不会挂掉。
>
>上面那个例子里, 集群有 ABC 三个主节点, 如果这 3 个节点都没有加入从节点，如果 B 挂掉了，我们就无法访问整个集群了。 A 和 C 的 slot 也无法访问。
>
>所以我们在集群建立的时候，一定要为每个主节点都添加了从节点, 比如像这样, 集群包含主节点 A、B、C, 以及从节点 A1、B1、C1, 那么即使 B 挂掉系统也可以继续正确工作。
>
>B1 节点替代了 B 节点，所以 Redis 集群将会选择 B1 节点作为新的主节点，集群将会继续正确地提供服务。 当B重新开启后，它就会变成 B1 的从节点。
>
>不过需要注意，如果节点 B 和 B1 同时挂了，Redis 集群就无法继续正确地提供服务了。
>
>集群的时候，我们可以单机集群也可以多机集群，后面我们分别实验；

##### 5.2 Redis 单机多节点集群

* 第一步：**Reids 安装包里有个集群工具，要复制到 /usr/local/bin 里去**

 ```properties
[root@localhost ~]# cp redis-3.2.11/src/redis-trib.rb /usr/local/redis/bin/
 ```

* 第二步：**修改配置，创建节点**

  >  我们现在要搞六个节点，三主三从，
  >
  >  端口规定分别是 7001，7002，7003，7004，7005，7006
  >
  >  我们先在 root 目录下新建一个 redis_cluster 目录，然后该目录下再创建6个目录，
  >
  >  分别是7001，7002，7003，7004，7005，7006，用来存在 redis 配置文件；
  >
  >  这里我们要使用 redis 集群，要先修改 redis 的配置文件 redis.conf

```properties
[root@localhost ~]# mkdir redis_cluster
[root@localhost ~]# cd redis_cluster/
[root@localhost redis_cluster]# mkdir 7001 7002 7003 7004 7005 7006
[root@localhost redis_cluster]# ll
总用量 0
drwxr-xr-x. 2 root root 6 3月  15 22:02 7001
drwxr-xr-x. 2 root root 6 3月  15 22:02 7002
drwxr-xr-x. 2 root root 6 3月  15 22:02 7003
drwxr-xr-x. 2 root root 6 3月  15 22:02 7004
drwxr-xr-x. 2 root root 6 3月  15 22:02 7005
drwxr-xr-x. 2 root root 6 3月  15 22:02 7006

[root@localhost redis_cluster]# cd
[root@localhost ~]# cp redis-3.2.11/redis.conf redis_cluster/7001
[root@localhost ~]# cd redis_cluster/7001
[root@localhost 7001]# ll
总用量 48
-rw-r--r--. 1 root root 46695 3月  15 22:02 redis.conf
[root@localhost 7001]# vi redis.conf

```

修改一下几个

**port 7001  //六个节点配置文件分别是7001-7006**

**daemonize yes        //redis后台运行**

**pidfile /var/run/redis_7001.pid   //pidfile文件对应7001-7006**

**cluster-enabled yes   //开启集群**

**cluster-config-file nodes_7001.conf  //保存节点配置，自动创建，自动更新对应7001-7006**

**cluster-node-timeout 5000    //集群超时时间，节点超过这个时间没反应就断定是宕机**

**appendonly yes   //存储方式，aof，将写操作记录保存到日志中**

7001 下的修改完后，我们把 7001 下的配置分别复制到 7002-7006 然后对应的再修改下配置即可；

```properties
[root@localhost ~]# cp redis_cluster/7001/redis.conf redis_cluster/7002/
[root@localhost ~]# cp redis_cluster/7001/redis.conf redis_cluster/7003/
[root@localhost ~]# cp redis_cluster/7001/redis.conf redis_cluster/7004/
[root@localhost ~]# cp redis_cluster/7001/redis.conf redis_cluster/7005/
[root@localhost ~]# cp redis_cluster/7001/redis.conf redis_cluster/7006/
[root@localhost ~]# vi redis_cluster/7002/redis.conf
[root@localhost ~]# vi redis_cluster/7003/redis.conf
[root@localhost ~]# vi redis_cluster/7004/redis.conf
[root@localhost ~]# vi redis_cluster/7005/redis.conf
```

* 第三步：**启动六个节点的redis**

```properties
[root@localhost ~]# vi redis_cluster/7005/redis.conf
[root@localhost ~]# vi redis_cluster/7006/redis.conf
[root@localhost ~]# vi redis_cluster/7006/redis.conf
[root@localhost ~]# vi redis_cluster/7005/redis.conf
[root@localhost ~]# vi redis_cluster/7004/redis.conf
[root@localhost ~]# vi redis_cluster/7003/redis.conf
[root@localhost ~]# vi redis_cluster/7002/redis.conf
[root@localhost ~]# vi redis_cluster/7001/redis.conf
[root@localhost ~]# /usr/local/redis/bin/redis-server redis_cluster/7001/redis.conf
[root@localhost ~]# /usr/local/redis/bin/redis-server redis_cluster/7002/redis.conf
[root@localhost ~]# /usr/local/redis/bin/redis-server redis_cluster/7003/redis.conf
[root@localhost ~]# /usr/local/redis/bin/redis-server redis_cluster/7004/redis.conf
[root@localhost ~]# /usr/local/redis/bin/redis-server redis_cluster/7005/redis.conf
[root@localhost ~]# /usr/local/redis/bin/redis-server redis_cluster/7006/redis.conf
[root@localhost ~]# ps -ef | grep redis
root      4861     1  0 22:19 ?        00:00:00 /usr/local/redis/bin/redis-server 127.0.0.1:7001 [  cluster]
root      4865     1  0 22:19 ?        00:00:00 /usr/local/redis/bin/redis-server 127.0.0.1:7002 [  cluster]
root      4869     1  0 22:19 ?        00:00:00 /usr/local/redis/bin/redis-server 127.0.0.1:7003 [  cluster]
root      4873     1  0 22:19 ?        00:00:00 /usr/local/redis/bin/redis-server 127.0.0.1:7004 [  cluster]
root      4877     1  0 22:19 ?        00:00:00 /usr/local/redis/bin/redis-server 127.0.0.1:7005 [  cluster]
root      4881     1  0 22:19 ?        00:00:00 /usr/local/redis/bin/redis-server 127.0.0.1:7006 [  cluster]
root      4885  4777  0 22:20 pts/0    00:00:00 grep --color=auto redis
```

* **第四步：创建集群**

> redis 官方提供了 redis-trib.rb 工具，第一步里已经放到里 bin 下 ；
>
> 但是在使用之前 需要安装 ruby，以及 redis 和 ruby 连接

```properties
[root@localhost ~]# yum -y install ruby ruby-devel rubygems rpm-build
已加载插件：fastestmirror
base                                                                       | 3.6 kB  00:00:00
extras                                                                     | 3.4 kB  00:00:00
updates 
......

[root@localhost ~]# gem install redis
Fetching: redis-4.0.1.gem (100%)
ERROR:  Error installing redis:
        redis requires Ruby version >= 2.2.2.
[root@localhost ~]# ruby --version
ruby 2.0.0p648 (2015-12-16) [x86_64-linux]

// 因 Centos 中的 ruby 版本是 2.0.0 ， gem install redis xuyao需要 >=2.2.2
// 所以我们需要安装 >=2.2.2 版本的 ruby
// 这里是教程 https://www.cnblogs.com/PatrickLiu/p/8454579.html

//安装完成后，我们需要删除 Redis 中所有的缓存数据（appendonly.aof & dump.rdb ），然后创建集群

[root@localhost ~]# /usr/local/redis/bin/redis-trib.rb create --replicas 1  127.0.0.1:7001 127.0.0.1:7002 127.0.0.1:7003 127.0.0.1:7004 127.0.0.1:7005 127.0.0.1:7006
>>> Creating cluster
>>> Performing hash slots allocation on 6 nodes...
Using 3 masters:
127.0.0.1:7001
127.0.0.1:7002
127.0.0.1:7003
Adding replica 127.0.0.1:7004 to 127.0.0.1:7001
Adding replica 127.0.0.1:7005 to 127.0.0.1:7002
Adding replica 127.0.0.1:7006 to 127.0.0.1:7003
M: decb6a502e73af0bedb2020162c2271b31b32e22 127.0.0.1:7001
   slots:0-5460 (5461 slots) master
M: 7541be5dfe47d85fb3edb2dc7dd22748fab054c2 127.0.0.1:7002
   slots:5461-10922 (5462 slots) master
M: 7418767d6edd70207d46fadca0d0c615c5dc8261 127.0.0.1:7003
   slots:10923-16383 (5461 slots) master
S: b5d85306632a0369f0b51a4f518fa6a39ca88c17 127.0.0.1:7004
   replicates decb6a502e73af0bedb2020162c2271b31b32e22
S: 17fa54f602d2564531bd73a1794409ae7276f3e6 127.0.0.1:7005
   replicates 7541be5dfe47d85fb3edb2dc7dd22748fab054c2
S: 84cdc087e27d7581f6281014b3eedc114b91409f 127.0.0.1:7006
   replicates 7418767d6edd70207d46fadca0d0c615c5dc8261
Can I set the above configuration? (type 'yes' to accept): yes
>>> Nodes configuration updated
>>> Assign a different config epoch to each node
>>> Sending CLUSTER MEET messages to join the cluster
Waiting for the cluster to join...
>>> Performing Cluster Check (using node 127.0.0.1:7001)
M: decb6a502e73af0bedb2020162c2271b31b32e22 127.0.0.1:7001
   slots:0-5460 (5461 slots) master
   1 additional replica(s)
M: 7541be5dfe47d85fb3edb2dc7dd22748fab054c2 127.0.0.1:7002
   slots:5461-10922 (5462 slots) master
   1 additional replica(s)
S: b5d85306632a0369f0b51a4f518fa6a39ca88c17 127.0.0.1:7004
   slots: (0 slots) slave
   replicates decb6a502e73af0bedb2020162c2271b31b32e22
S: 17fa54f602d2564531bd73a1794409ae7276f3e6 127.0.0.1:7005
   slots: (0 slots) slave
   replicates 7541be5dfe47d85fb3edb2dc7dd22748fab054c2
S: 84cdc087e27d7581f6281014b3eedc114b91409f 127.0.0.1:7006
   slots: (0 slots) slave
   replicates 7418767d6edd70207d46fadca0d0c615c5dc8261
M: 7418767d6edd70207d46fadca0d0c615c5dc8261 127.0.0.1:7003
   slots:10923-16383 (5461 slots) master
   1 additional replica(s)
[OK] All nodes agree about slots configuration.
>>> Check for open slots...
>>> Check slots coverage...
[OK] All 16384 slots covered.

//从运行结果看 主节点就是7001 7002 7003 从节点分别是7004 7005 7006 
```

* **第五步：集群数据测试**

```properties
//连接任意一个节点端口：
[root@localhost ~]# /usr/local/redis/bin/redis-cli -c -p 7002
127.0.0.1:7002> 
127.0.0.1:7002> set xxx  'fdafda'
-> Redirected to slot [4038] located at 127.0.0.1:7001
OK
# 前面说过Redis Cluster值分配规则，所以分配key的时候，它会使用CRC16(‘my_name’)%16384算法，来计 # 算，将这个key 放到哪个节点，这里分配到了4038slot 就分配到了7001(0-5460)这个节点上。所以有：

# 我们从其他集群节点 ，都可以获取到数据
127.0.0.1:7001> exit
[root@localhost ~]# /usr/local/redis/bin/redis-cli -c -p 7005
127.0.0.1:7005> get xxx
-> Redirected to slot [4038] located at 127.0.0.1:7001
"fdafda"
127.0.0.1:7001>
```

* **第六步：集群宕机测试**

```properties
假如我们干掉一个节点，比如7002 这个主节点

[root@localhost ~]#  ps -ef | grep redis
root       9501      1  0 17:38 ?        00:00:02 /usr/local/redis/bin/redis-server 127.0.0.1:7001 [cluster]
root       9512      1  0 17:45 ?        00:00:01 /usr/local/redis/bin/redis-server 127.0.0.1:7002 [cluster]
root       9516      1  0 17:45 ?        00:00:01 /usr/local/redis/bin/redis-server 127.0.0.1:7003 [cluster]
root       9520      1  0 17:45 ?        00:00:02 /usr/local/redis/bin/redis-server 127.0.0.1:7004 [cluster]
root       9524      1  0 17:45 ?        00:00:01 /usr/local/redis/bin/redis-server 127.0.0.1:7005 [cluster]
root       9528      1  0 17:45 ?        00:00:01 /usr/local/redis/bin/redis-server 127.0.0.1:7006 [cluster]
root       9601   2186  0 18:12 pts/0    00:00:00 grep --color=auto redis
[root@localhost ~]# kill -9 9512
[root@localhost ~]#  ps -ef | grep redis
root       9501      1  0 17:38 ?        00:00:02 /usr/local/redis/bin/redis-server 127.0.0.1:7001 [cluster]
root       9516      1  0 17:45 ?        00:00:01 /usr/local/redis/bin/redis-server 127.0.0.1:7003 [cluster]
root       9520      1  0 17:45 ?        00:00:02 /usr/local/redis/bin/redis-server 127.0.0.1:7004 [cluster]
root       9524      1  0 17:45 ?        00:00:01 /usr/local/redis/bin/redis-server 127.0.0.1:7005 [cluster]
root       9528      1  0 17:45 ?        00:00:01 /usr/local/redis/bin/redis-server 127.0.0.1:7006 [cluster]
root       9603   2186  0 18:12 pts/0    00:00:00 grep --color=auto redis
[root@localhost ~]# 


然后再来看下集群的情况
redis-trib.rb check 127.0.0.1:7001
>>> Performing Cluster Check (using node 127.0.0.1:7001)
M: bfcfcdc304b011023fa568e044ea23ea6bc03c3c 127.0.0.1:7001
   slots:0-5460 (5461 slots) master
   1 additional replica(s)
S: f25b35f208dc96605ee4660994d2ac52f39ac870 127.0.0.1:7006
   slots: (0 slots) slave
   replicates aa6bc3f1e1174c3a991c01882584707c2408ec18
M: 1d2341fd3b79ef0fccb8e3a052bba141337c6cdd 127.0.0.1:7005
   slots:5461-10922 (5462 slots) master
   0 additional replica(s)
M: aa6bc3f1e1174c3a991c01882584707c2408ec18 127.0.0.1:7003
   slots:10923-16383 (5461 slots) master
   1 additional replica(s)
S: 7908a60306333c5d7c7c5e7ffef44bdf947ef0a4 127.0.0.1:7004
   slots: (0 slots) slave
   replicates bfcfcdc304b011023fa568e044ea23ea6bc03c3c
[OK] All nodes agree about slots configuration.
>>> Check for open slots...
>>> Check slots coverage...
[OK] All 16384 slots covered.
我们发现 7005本来是从节点，由于他对应的主节点挂了，就自动变成主节点master，所有会有最后一个说明
 All 16384 slots covered. 所有哈希槽都可覆盖了； 集群可以正常使用；


假如我们把7005也干掉，试试看
[root@localhost ~]# kill -9 9524
[root@localhost ~]#  ps -ef | grep redis
root       9501      1  0 17:38 ?        00:00:03 /usr/local/redis/bin/redis-server 127.0.0.1:7001 [cluster]
root       9516      1  0 17:45 ?        00:00:02 /usr/local/redis/bin/redis-server 127.0.0.1:7003 [cluster]
root       9520      1  0 17:45 ?        00:00:03 /usr/local/redis/bin/redis-server 127.0.0.1:7004 [cluster]
root       9528      1  0 17:45 ?        00:00:02 /usr/local/redis/bin/redis-server 127.0.0.1:7006 [cluster]
root       9610   2186  0 18:16 pts/0    00:00:00 grep --color=auto redis
[root@localhost ~]# 


查看下集群情况
[root@localhost ~]# /usr/local/redis/bin/redis-trib.rb check 127.0.0.1:7001
>>> Performing Cluster Check (using node 127.0.0.1:7001)
M: 7541be5dfe47d85fb3edb2dc7dd22748fab054c2 127.0.0.1:7001
   slots:5461-10922 (5462 slots) master
   1 additional replica(s)
S: 84cdc087e27d7581f6281014b3eedc114b91409f 127.0.0.1:7006
   slots: (0 slots) slave
   replicates 7418767d6edd70207d46fadca0d0c615c5dc8261
M: 7418767d6edd70207d46fadca0d0c615c5dc8261 127.0.0.1:7003
   slots:10923-16383 (5461 slots) master
   1 additional replica(s)
S: 17fa54f602d2564531bd73a1794409ae7276f3e6 127.0.0.1:7004
   slots: (0 slots) slave
   replicates 7541be5dfe47d85fb3edb2dc7dd22748fab054c2
[OK] All nodes agree about slots configuration.
>>> Check for open slots...
>>> Check slots coverage...
[ERR] Not all 16384 slots are covered by nodes.

# 这里我们发现 出事了，因为主从节点都挂了 所以有一部分哈希槽没得分配，最后一句
# [ERR] Not all 16384 slots are covered by nodes.  没有安全覆盖；
# 所以不能正常使用集群；
```

##### 5.3 Redis 多机多节点集群

> 与单机多节点类似，只不过是需要多台机器，一台集群控制端，其他随意，
>
> bind 需要绑定的是本机地址

[**Redis多机多节点集群实验**](http://blog.java1234.com/blog/articles/328.html)



