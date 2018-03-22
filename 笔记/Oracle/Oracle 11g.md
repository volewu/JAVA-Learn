## Oracle 11g







##### 1. Oracle  简介

> Oracle Database （Oracle RDBMS），是甲骨文公司的一款关系数据库管理系统。系统可移植性好、使用方便、功能强，是一种高效、可靠性好的、适应高吞量的数据库解决方案。

##### 2. Oracle  安装及工具简介

> Oracle 11g 32 位下载：http://www.java1234.com/a/javaziliao/kfgj/2015/0817/4787.html
> Oracle 11g 64 位下载：http://www.java1234.com/a/javaziliao/kfgj/2015/0817/4786.html
>
> **sys : 最高权限**
>
> **system : 次要权限**
>
> **scott: 学习权限**

##### 3. PLSQLDeveloper  安装

> Plsql developer 10 下载：http://www.java1234.com/a/javaziliao/kfgj/2015/0815/4776.html
>
> Plsql developer 没有开发 64 位版本，所以导致了 64 位用户没法直接连接 oracle 数据库；

##### 4. Oracle 11g  卸载

> Oracle 卸载：http://jingyan.baidu.com/article/922554468d4e6b851648f4e3.html

##### 5. 表空间

> 一个数据库可以有多个表空间，一个表空间里可以有多个表。表空间就是存多个表的物理空间；
> 可以指定表空间的大小位置等。
>
> **表空间目录：Tablespaces**
>
> **用户的表空间配置目录：View/DBA_USERS**
>
> * 创建表空间：create tablespace ts1 datafile 'C:\tablespace\ts1.dbf' size 50M;
> * 自动扩展大小：create tablespace ts2 datafile 'C:\tablespace\ts2.dbf' size 50M autoextend on next 10M;
> * 设置最大空间：create tablespace ts3 datafile 'C:\tablespace\ts3.dbf' size 50M autoextend on next 10M maxsize 1024M;
> * 更改用户默认表空间：alter database default tablespace ts1;
> * 表空间改名：alter tablespace ts1 rename to tss1;
> * 删除表空间：drop tablespace ts2 including contents and datafiles;

##### 6. 数据库表

**数据库表目录：Tables**

```sql
select * from A1 t for update  //查找并更新
```

##### 7. 虚拟表 dual 

>  **Dual 表是 sys 用户下的一张虚表；提供一些运算和日期操作时候用到；**

```sql
select sysdate from dual;
```

##### 8. 序列

> 序列作为数据库里的对象，主要作用是生成唯一的主键值；在 **Sequences** 目录

```sql
create sequence user_sq; //创建序列

insert into t_user values(user_sq.nextval,'gakki','123');//为表的主键插入序列

select user_sq.currval from dual; //当前序列值

select user_sq.nextval from dual; //下一个序列值

create sequence user_sq2 start with 100;//为序列指定初始化值

create sequence user_sq3 minvalue 5 maxvalue 100; //指定最小值与最大值

create sequence user_sq4 increment by 3; //指定增长值

create sequence user_sq5 cache 100;//一次获取序列的多个连续值，默认 20 ，放到内存中，方便下次快速获取
```

##### 9. 表操作（增删改查）

```sql
/*给指定列插入数据：*/
insert into dept(deptno,dname) values(50,'xx');
/* 插入全部列数据：*/
insert into dept(deptno,dname,loc) values(60,'xx','lll'); 简写 insert into dept values(70,'xxx','llll');

/*更新指定数据：*/
update dept set dname='司法部' where deptno=50;
update dept set dname='司法部' ,loc='china' where deptno=50;

/*删除指定数据：*/
delete from dept where deptno=70;
/*删除指定条件的数据：*/
delete from dept where deptno>40;


查询所有：select * from emp;
指定字段查询：select ename,sal from emp;
加 where 条件：select * from emp where sal>=800; select * from emp where sal>=1500 and job='SALESMAN';
Distinct 去重复记录；
Group by 分组查询：select job,count(ename) as num from EMP t group by job;
Having 过滤分组：select job,count(ename) as num from EMP t group by job having count(ename)>=2;
Order by 排序：select * from emp order by sal desc;
子查询：查询出基本工资大于平均工资的员工：select * from emp where sal>(select avg(sal) from emp)

联合查询：
并集（去重复）：
select * from t_user1
union
select * from t_user2;

并集：
select * from t_user1
union all
select * from t_user2;

交集：
select * from t_user1
intersect
select * from t_user2;

差集：
select * from t_user1
minus
select * from t_user2;

内连接：
select * from emp t,dept d where t.deptno=d.deptno;
类似：select * from emp e inner join dept d on e.deptno=d.deptno; inner 可以省略；

外连接：
左外连接：select * from emp e left join dept d on e.deptno=d.deptno;
右外连接：select * from emp e right join dept d on e.deptno=d.deptno;
```

##### 10. Oracle  数据类型及函数 

> * 字符串类型
> * 数值类型
> * 日期类型

* **字符串类型及函数**

> 字符类型分 3 种，**char(n)** 、**varchar(n)**、**varchar2(n)**；
> **char(n)**: 固定长度字符串，假如长度不足 n，右边空格补齐；
> **varchar(n)**: 可变长度字符串，假如长度不足 n，右边不会补齐；
> **varchar2(n)**: 可变长度字符串，Oracle 官方推荐使用，向后兼容性好；
> **char(n) VS varchar2(n)  --- char(n)查询效率相对较高，varchar2(n) 存储空间相对较小；**

```sql
lpad() 向左补全字符串：
select lpad(stuno,6,'0') from t_user3;

rpad() 向右补全字符串：
select rpad(stuno,6,'0') from t_user3;

lower() 返回字符串小写：
select lower(userName) from t_user3;

upper() 返回字符串大写：
select upper(userName) from t_user3;

initcap() 单词首字符大写：
select initcap(userName) from t_user3;

length() 返回字符串长度：
select length(password) from t_user3;

substr() 截取字符串：(列名称，索引开始位置，索引结束位置)   /*索引都是从 1 开始，与 java 不同*/
select substr(userName,1,2) from t_user3;

instr() 获取字符串出现的位置：（列名称，"哪个数字"，索引位置，出现频率）
select instr(password,'23',2,2) from t_user3;

ltrim() 删除左侧空格:
select ltrim(userName) from t_user3;

rtrim() 删除右侧空格:
select rtrim(userName) from t_user3;

trim() 删除两侧空格:
select trim(userName) from t_user3;

concat() 串联字符串：
select concat(userName,password) from t_user3;

reverse() 反转字符串：
select reverse(userName) from t_user3;
```

* **数值类型及函数**

> **number** 是 oracle 中的数据类型；number(precision,scale)；
> Precision，scale 均可选；
> **Precision** 代表**精度**，**sacle** 代表**小数位的位数**；**Precision 范围[1,38]**, ** scale 范围[-84,127]**
> 举例： 12345.678 Precision 是 8 scale 是 3；

```sql
abs() 求绝对值；
select abs(n1) from t_number where id=1;

round() 四舍五入：(列名称，小数位数)
select round(n1,2) from t_number where id=1;

ceil() 向上取整：
select ceil(12.3) from dual;------  13

floor 向下取整：
select floor(12.3) from dual;----- 12

Mod()取模：求余数
select mod(5,3) from dual; --- 2

Sign()正负性：正数为 1  ；0 为 0 ；负数为 -1 
select sign(100) from dual;--- 1

Sqrt() 求平方根：
select sqrt(9) from dual;---- 3

Power()求乘方：
select power(2,3) from dual; --- 8

Trunc()截取：(数字，小数位的第几位)不会四舍五入
select trunc(123.456,2) from dual; -----  123.45

To_char() 格式化数值：常见的字符匹配有 0、9、，、$、FM、L、C
select to_char(123.45,'0000.000') from dual;------- \0123.450
select to_char(123.45,'9999.999') from dual;------- \ 123.450  整数位不补 0 
select to_char(123123,'99,999,999.99') from dual; ---- \ 123,123.00
select to_char(123123.3,'FM99,999,999.99') from dual;----- \123,123.3  去掉空格
select to_char(123123.3,'$99,999,999.99') from dual;------\ $123,123.30
select to_char(123123.3,'L99,999,999.99') from dual;-------\ ￥123，123.30
select to_char(123123.3,'99,999,999.99C') from dual;------- \ 123,123.3CNY
```

* **日期类型及函数**

> * **Date** 包含信息 century（世纪信息） year 年 month 月 day 日 hour 小时 minute 分钟 second 秒
> * **timestamp（时间戳）一般用于日期时间要求非常精确的情况，精确到毫秒级；**
>
>    insert into t_date values(1,sysdate,systimestamp);

```sql
select sysdate from dual;
select systimestamp from dual;


Add_months 添加月份 ：
select add_months(d1,2) from t_date where id=1;

Last_day 返回指定日期月份的最后一天 ：
select last_day(d1) from t_date where id=1;

Months_between 返回两个日期的相差月数 ：
select months_between(d1,d3) from t_date where id=1;

next_day 返回特定日期之后的一周内的日期：
select next_day(d1,2) from t_date where id=1;

Trunc 截取日期：
select trunc(d1,'YYYY') from t_date where id=1;
select trunc(d1,'MM') from t_date where id=1;
select trunc(d1,'DD') from t_date where id=1;
select trunc(d1,'HH') from t_date where id=1;
select trunc(d1,'MI') from t_date where id=1;

Extract 返回日期的某个域：
select extract(year from sysdate) from dual;
select extract(month from sysdate) from dual;
select extract(day from sysdate) from dual;
select extract(Hour from systimestamp) from dual;
select extract(minute from systimestamp) from dual;
select extract(second from systimestamp) from dual;

To_char 将日期转换成字符串：
select to_char(d1,'YYYY-MM-DD') from t_date where id=1;
select to_char(d1,'YYYY-MM-DD HH24:MI:SS') from t_date where id=1;
```

* **其他常用处理函数**

```sql
常用的聚合函数：
Max 求最大值：select max(sal) from emp ;

Min 求最小值：select min(sal) from emp ;

Avg 求平均值：select avg(sal) from emp ;

Sum 求和：select sum(sal) from emp ;

Count 统计记录数：select count(ename) from emp ;

Nvl 空值处理：(字段，赋值的参数)
select ename,nvl(sal,0) from emp;

rownum
Oracle 分页：select * from (select a.*,rownum rn from (select * from emp) Awhere rownum<=10) where rn>5;

Oracle 中的运算：
select 2+1 from dual;
select 2-1 from dual;
select 2*1 from dual;
select 2/1 from dual;

条件判断式：
Between and 范围查询：
select * from emp where sal between 900 and 1500;
select * from emp where sal>=900 and sal<=1500;

In 集合范围：
select ename,hiredate from emp where ename in (select distinct ename from bonus)

Like 模糊查询：
select * from emp where ename like '%M%'
select * from emp where ename like 'M%'
select * from emp where ename like '_M%'
```

##### 11. Oracle  视图

> 视图是虚表，没有具体物理数据，是通过实体表的一种计算映射逻辑。主要就是为了方便和数据安全；并且试图都是只读视图；

```sql
创建视图：Create view 视图名称 as 查询语句
create view v_emp1 as select ename,job from emp;

有些用户没有权限创建视图，使用需要赋予权限
查看用户的角色: select * from user_role_privs;
查看角色对应的权限: SELECT * FROM DBA_SYS_PRIVS WHERE GRANTEE='DBA'; 
授予用户dba权限: grant dba to scott;

修改视图
create or replace view v_emp1 as
select ename,job,sal,可增加字段 from emp;

使用视图: 和普通 sql 语句一样
查询：select * from v_emp1; select * from v_emp1 where ename like '%M%';
修改：update v_emp1 set job='销售' where ename='sb';
添加：insert into v_emp2 values('2222','sb2','技术');
删除：delete from v_emp2 where empno=2222

只读视图：在后面加上 with read only
Create view 视图名称 as 查询语句 with read only ;
```

##### 12. Oracle  约束

> * 主键约束 (Primary)
> * 外键约束 (Foreign)
> * 唯一性约束 (Unique)
> * 检查约束 (Condition)
> * 默认值约束 (Default)
> * 非空约束 (Nullable)

##### 13. Oracle  控制语句

```sql
/*IF  条件语句*/
SQL> set serverout on;
SQL> declare n number:=1;
  2          v varchar2(20):='world';
  3  begin
  4     dbms_output.put_line('hello'||n||v);
  5  end;
  6  /
hello1world
PL/SQL procedure successfully completed

SQL> set serverout on;
SQL> declare emp_count number;
  2  begin
  3    select count(*) into emp_count from emp where sal>=3000;
  4    if emp_count>0 then
  5      dbms_output.put_line('有'||emp_count||'个员工的基本薪资大于等于3000');
  6    else
  7      dbms_output.put_line('没有员工的基本薪资大于等于3000');
  8    end if;
  9  end;
 10  /
有3个员工的基本薪资大于等于3000
PL/SQL procedure successfully completed

SQL> set serverout on;
SQL> declare emp_count number;
  2  begin
  3    select count(*) into emp_count from emp where sal>=3000;
  4    if emp_count=1 then
  5      dbms_output.put_line('有1个员工的基本薪资大于等于3000');
  6    else if emp_count>1 then
  7      dbms_output.put_line('有超过1个员工的基本薪资大于等于3000');
  8    else
  9      dbms_output.put_line('没有员工的基本薪资大于等于3000');
 10    end if;
 11    end if;
 12  end;
 13  
 14  /
有超过1个员工的基本薪资大于等于3000
PL/SQL procedure successfully completed

/*CASE WHEN 流程控制语句*/
SQL> set serverout on;
SQL> declare emp_count number;
  2  begin
  3    select count(*) into emp_count from emp where sal>=3000;
  4    case emp_count
  5      when 0 then dbms_output.put_line('没有员工的基本薪资大于等于3000');
  6      when 1 then dbms_output.put_line('有1个员工的基本薪资大于等于3000');
  7      when 2 then dbms_output.put_line('有2个员工的基本薪资大于等于3000');
  8      when 3 then dbms_output.put_line('有3个员工的基本薪资大于等于3000');
  9      else dbms_output.put_line('超过3个员工的基本薪资大于等于3000');
 10    end case;
 11  end;
 12  /
有3个员工的基本薪资大于等于3000
PL/SQL procedure successfully completed

/*无条件循环 loop*//
SQL> set serverout on;
SQL> declare g_id number:=2;
  2          g_losal number;
  3          g_hisal number;
  4  begin
  5    loop
  6      if(g_id>4) then
  7        exit;
  8      end if;
  9  
 10      select losal,hisal into g_losal,g_hisal from salgrade where grade=g_id;
 11      dbms_output.put_line(g_id || '等级的最低薪资'|| g_losal || '，最高薪资：' || g_hisal);
 12  
 13      g_id:=g_id+1;
 14  
 15    end loop;
 16  end;
 17  /
2等级的最低薪资1201，最高薪资：1400
3等级的最低薪资1401，最高薪资：2000
4等级的最低薪资2001，最高薪资：3000
PL/SQL procedure successfully completed

/*while 循环*/ 
SQL> set serverout on;
SQL> declare g_id number:=2;
  2          g_losal number;
  3          g_hisal number;
  4  begin
  5  
  6    while g_id<5 loop
  7  
  8       select losal,hisal into g_losal,g_hisal from salgrade where grade=g_id;
  9       dbms_output.put_line(g_id || '等级的最低薪资'|| g_losal || '，最高薪资：' || g_hisal);
 10       g_id:=g_id+1;
 11    end loop;
 12  
 13  end;
 14  /
2等级的最低薪资1201，最高薪资：1400
3等级的最低薪资1401，最高薪资：2000
4等级的最低薪资2001，最高薪资：3000
PL/SQL procedure successfully completed

/*for 循环*/
SQL> set serverout on;
SQL> declare g_losal number;
  2          g_hisal number;
  3  begin
  4    for g_id in 2..4 loop
  5      select losal,hisal into g_losal,g_hisal from salgrade where grade=g_id;
  6      dbms_output.put_line(g_id || '等级的最低薪资'|| g_losal || '，最高薪资：' || g_hisal);
  7    end loop;
  8  end;
  9  /
2等级的最低薪资1201，最高薪资：1400
3等级的最低薪资1401，最高薪资：2000
4等级的最低薪资2001，最高薪资：3000
PL/SQL procedure successfully completed
```

##### 14. Oracle  游标

> 使用游标，我们可以对具体操作数据，比如查询的结果，对行，列数据进行更加细致的处理。以及对其他 DML操作进行判断等操作.

* 显示游标
* 隐式游标
* 动态游标

##### 15. Oracle  触发器

> 具备某些条件，由数据库自动执行的一些 DML 操作行为。

* 语句触发器
* 行触发器
* 触发器禁用和开启

##### 16. Oracle    函数与存储过程

* Oracle  自定义函数
* Oracle   存储过程
* 程序包

##### 17. Oracle  用户，角色和权限

* **Oracle  用户**

> Oracle 用户分两种，一种是系统用户 sys system ；另外一种是普通用户；
> 视图 **dba_users** 存储着所有用户信息；

```sql
创建用户：
Create user 用户名 identified by 密码 default tablespace 表空间

授予 session 权限：
grant create session to TEST;

锁定和开启帐号：
alter user TEST account lock / unlock ;

修改用户密码：
alter user TEST identified by 123 ;

删除用户：
drop user TEST cascade ;
删除用户，并且把用户下的对象删除，比如表，视图，触发器等。
```

* **Oracle  权限**

> Oracle 权限分为**系统权限**和**对象权限**；
> 系统权限是 Oracle 内置的，与具体对象无关的权限，比如创建表的权限，连接数据库权限；
> 对象权限就是对具体对象，比如表，视图，触发器等的操作权限；

```properties
系统权限视图：system_privilege_map
权限分配视图：dba_sys_privs
回收系统权限：revoke 权限 from 用户
```

```properties
对象权限分配
用户表权限视图：dba_tab_privs
给对象授权： grant 权限 on 对象 to 用户 with grant option;
回收权限：revoke 对象权限 on 对象 from 用户；
```

* **Oracle  角色**

> 角色是**权限的集合**；可以给用户直接分配角色，不需要一个一个分配权限；
> 语法：使用视图 **dba_roles** 可以查找角色信息

```properties
创建角色：Create role 角色名称

给用户赋予角色：grant 角色 to 用户

eg:
Create user W_vole identified by 123456 default tablespace 表空间
grant DBA TO W_vole
```





