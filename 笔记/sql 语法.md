> **删除多个字段**：`delete from table_name where field in(1,3,5)`

> **分页查询**：`select * from table_name limit start,rows`

> **更新数据**：`update table_name set 属性1='新值1',属性2='新值1' where 属性3=值3`

> **查询出生日期**： `select * from table_name where TO_DAYS(s.birthday)>=TO_DAYS('值')`

> 1-->true     0-->false 