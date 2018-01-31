#### JDBC(Java Data Base Connectivity)

1. 基本连接：

   ```
   private String dbUrl="jdbc:mysql://localhost:3306/数据库名称";
   private String dbUser="root";
   private String dbPassword="123456";
   private String jdbcName="com.mysql.jdbc.Dervic";

   public Connection getCon()throws Exception{
     	Class.form(jdbcName);
     	return DervicManager.getConnection(dbUrl,dbUser,dbPassword);
   }

   public void closeCon(Connection con)throws Exception{
     	if(con!=null)
     		con.close();
   }



   ```

   ​

