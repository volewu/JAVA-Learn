## Activiti 备忘录

--------------------目录--------------------

* 一、Activiti 简介
  * 1.1 什么是 Activiti
  * 1.2 HelloWorld 的实现
    - 1.2.1 Activiti 的 25 张表
    - 1.2.2 eclipse 安装 activiti 插件
    - 1.2.3 初识 Activiti 流程设计工具
    - 1.2.4 Activiti HelloWorld 实现（代码层次）
    - 1.2.5 Activiti HelloWorld 实现（表数据发生的变化）
* 二、流程定义

  * 2.1 流程定义添加（部署）
  * 2.2 流程定义查询
  * 2.3 流程定义删除
  * 2.4 流程定义的 ‘ 修改‘
* 三、Activiti  流程实例
  * 3.1 构建学生请假审批流程
  * 3.2 执行对象概念
  * 3.3 判断流程实例状态
  * 3.4 历史流程实例查询
  * 3.5 历史活动查询
* 四、Activiti  流程变量

  * 4.1 流程变量的概念
  * 4.2 使用 TaskService  设置和获取流程变量
  * 4.3 局部流程变量
  * 4.4 使用 RuntimeService  设置和获取流程变量
  * 4.5 启动流程的时候设置流程变量
  * 4.6 完成任务的时候设置流程变量
* 五、Activiti  流程控制网关
  * 5.1 连线
  * 5.2 排它网关
  * 5.3 并行网关
* 六、Activit 任务分配
  * 6.1 个人任务分配
    * 6.1.1 直接流程图配置中写死
    * 6.1.2 使用流程变量
    * 61.3 TaskListener 监听实现
  * 6.2 多用户任务分配
    * 6.2.1 直接流程图配置中写死
    * 6.2.2 使用流程变量
    * 6.2.3 TaskListener 监听实现
  * 6.3 组任务分配
    * 6.3.1 直接流程图配置中写死
    * 6.3.2 使用流程变量

--------------------目录--------------------

#### 一、Activiti 简介

##### 1.1 什么是 Activiti

> [Activiti](https://www.activiti.org/) 项目是一项新的基于Apache许可的开源BPM平台，从基础开始构建，旨在提供支持新的BPMN 2.0标准，包括支持对象管理组（OMG），面对新技术的机遇，诸如互操作性和云架构，提供技术实现。简单来说就是一个业务流程签核框架。

##### 1.2 HelloWorld 的实现

* 使用 Maven 创建 Activiti01 项目，然后在 pom.xml 中依赖相关 jar

```xml
 		<dependency>
			<groupId>org.activiti</groupId>
			<artifactId>activiti-engine</artifactId>
			<version>5.19.0.2</version>
		</dependency>

		<dependency>
			<groupId>org.activiti</groupId>
			<artifactId>activiti-spring</artifactId>
			<version>5.19.0.2</version>
		</dependency>

		<dependency>
			<groupId>org.activiti</groupId>
			<artifactId>activiti-bpmn-model</artifactId>
			<version>5.19.0.2</version>
		</dependency>

		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<version>5.1.38</version>
		</dependency>
```

######   1.2.1 Activiti 的 25 张表

```java
public class ActivitiTest01 {

	/**
	 * 生成 Activiti 需要的 25 表
	 */
	@Test
	public void testActivitiCreateTable() {
		// 引擎配置
		ProcessEngineConfiguration pec = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
		pec.setJdbcDriver("com.mysql.jdbc.Driver");
		pec.setJdbcUrl("jdbc:mysql://localhost:3306/db_activiti");
		pec.setJdbcUsername("root");
		pec.setJdbcPassword("123456");

		/**
		 * false 不能自动创建表 
		 * create-drop 先删除表再创建表 
		 * true 自动创建和更新表
		 */
		pec.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

		// 获取流程引擎对象
		ProcessEngine processEngine = pec.buildProcessEngine();
	}

	/**
	 * 使用xml配置 简化
	 */
	@Test
	public void testCreateTableWithXml() {
		// 引擎配置
		ProcessEngineConfiguration pec = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
		// 获取流程引擎对象
		ProcessEngine processEngine = pec.buildProcessEngine();
	}

}
```

* 配置 activiti.cfg.xml 一般我们采用这种方式来生成需要的 25 表

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans   http://www.springframework.org/schema/beans/spring-beans.xsd">
 
  <bean id="processEngineConfiguration" class="org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration">
 
    <property name="jdbcUrl" value="jdbc:mysql://localhost:3306/db_activiti" />
    <property name="jdbcDriver" value="com.mysql.jdbc.Driver" />
    <property name="jdbcUsername" value="root" />
    <property name="jdbcPassword" value="123456" />
 
    <property name="databaseSchemaUpdate" value="true" />
 
  </bean>
 
</beans>
```

* 运行测试项目就会在 db_activiti 中生成需要的 25 张表

![activiti_25](E:\githubSample\JAVA-Learn\笔记\Activiti\photo\activiti_25.PNG)



> **ACT_RE_***: 'RE'表示repository。 这个前缀的表包含了流程定义和流程静态资源 （图片，规则，等等）。
>
> **ACT_RU_***: 'RU'表示runtime。 这些运行时的表，包含流程实例，任务，变量，异步任务，等运行中的数据。 Activiti只在流程实例执行过程中保存这些数据， 在流程结束时就会删除这些记录。 这样运行时表可以一直很小速度很快。
>
> **ACT_ID_***: 'ID'表示identity。 这些表包含身份信息，比如用户，组等等。
>
> **ACT_HI_***: 'HI'表示history。 这些表包含历史数据，比如历史流程实例， 变量，任务等等。
>
> **ACT_GE_***: 'GE'表示general。通用数据， 用于不同场景下，如存放资源文件。


###### 1.2.2 eclipse 安装 activiti 插件

* [离线安装](http://blog.csdn.net/jenyzhang/article/details/76598824)
* [在线安装](http://blog.java1234.com/blog/articles/74.html)


###### 1.2.3 初识 Activiti 流程设计工具

![activiti_tool](E:\githubSample\JAVA-Learn\笔记\Activiti\photo\activiti_tool.gif)

###### 1.2.4 Activiti HelloWorld 实现（代码层次）

* 获取流程实例 --------> 部署流程定义-------->启动流程实例-------->查看任务(有)-------->完成任务-------->查看任务(无)
* HelloWorldProcess.java

```java
public class HelloWorldProcess {
	
	/**
	 * 获取默认流程引擎实例，会自动读取 activiti.cfg.xml 文件
	 */
	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**
	 * 部署流程定义
	 */
	@Test
	public void deploy(){
		Deployment deployment= processEngine.getRepositoryService()// 获取部署相关Service
					.createDeployment()// 创建部署
					.addClasspathResource("diagrams/HelloWorld.bpmn") // 加载资源文件
					.addClasspathResource("diagrams/HelloWorld.png")
					.name("HelloWorld 流程")// 定义流程名称
					.deploy();// 部署
		System.out.println("流程部署ID:"+deployment.getId()); //流程部署ID:1
		System.out.println("流程部署Name:"+deployment.getName());//流程部署Name:HelloWorld 流程
	}
	
	/**
	 * 启动流程实例
	 */
	@Test
	public void start(){
		ProcessInstance pi=processEngine.getRuntimeService() // 运行时Service
				.startProcessInstanceByKey("myFirstProcess"); // 流程定义表的KEY字段值
			System.out.println("流程实例ID:"+pi.getId());//流程实例ID:2501
			System.out.println("流程定义ID:"+pi.getProcessDefinitionId()); //流程定义ID:myFirstProcess:1:4
	}
	
	/**
	 * 查看任务
	 */
	@Test
	public void findTask(){
		System.out.println("开始查看任务");
		List<Task> taskList = processEngine.getTaskService() // 任务相关Service
			.createTaskQuery()// 创建任务查询
			.taskAssignee("vole")// 指定某个人
			.list();
		for(Task task:taskList){
			System.out.println("任务ID:"+task.getId()); 
			System.out.println("任务名称:"+task.getName());
			System.out.println("任务创建时间:"+task.getCreateTime());
			System.out.println("任务委派人:"+task.getAssignee());
			System.out.println("流程实例ID:"+task.getProcessInstanceId());
		}
		/*
		任务ID:2504
		任务名称:用户节点
		任务创建时间:Fri Mar 09 16:31:19 CST 2018
		任务委派人:vole
		流程实例ID:2501
		*/
	}
	
	/**
	 * 完成任务
	 */
	@Test
	public void completeTask(){
		processEngine.getTaskService() // 任务相关Service
			.complete("2504");
		System.out.println("完成任务");
	}
}
```

###### 1.2.5 Activiti HelloWorld 实现（表数据发生的变化）

```sql
################################
# 部署流程定义涉及到的表

# 流程部署表
SELECT * FROM `act_re_deployment`

# 流程定义表
SELECT * FROM `act_re_procdef`

# 资源文件表
SELECT * FROM `act_ge_bytearray`

# 系统配置表 
SELECT * FROM `act_ge_property`


################################
# 启动流程实例涉及到的表

# 流程实例运行时 执行对象表
SELECT * FROM `act_ru_execution`

# 流程实例运行时 身份联系表
SELECT * FROM `act_ru_identitylink`

# 流程实例运行时 用户任务表
SELECT * FROM `act_ru_task`

# 活动节点历史表
SELECT * FROM `act_hi_actinst`

# 身份联系表 历史
SELECT * FROM `act_hi_identitylink`

# 流程实例表 历史
SELECT * FROM `act_hi_procinst`

# 历史任务表 
SELECT * FROM `act_hi_taskinst`



################################
# 结束流程实例涉及到的表
# 运行时  表数据全部清空
# 历史表  表数据修改 或者增加了数据


```



#### 二、流程定义

##### 2.1 流程定义添加（部署）

> 两种方式：
>
> Classpath 加载方式：
>
> Zip 加载方式：把资源文件打包成 zip，并放在资源目录下

```java
public class DeployProcdef {

	/**
	 * 获取默认流程引擎实例，会自动读取activiti.cfg.xml文件
	 */
	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**
	 * 部署流程定义
	 */
	@Test
	public void deployWithClassPath() {
		Deployment deployment = processEngine.getRepositoryService() // 获取部署相关Service
				.createDeployment() // 创建部署
				.addClasspathResource("diagrams/HelloWorld.bpmn") // 加载资源文件
				.addClasspathResource("diagrams/HelloWorld.png") // 加载资源文件
				.name("HelloWorld流程") // 流程名称
				.deploy(); // 部署
		System.out.println("流程部署ID:" + deployment.getId());
		System.out.println("流程部署Name:" + deployment.getName());
	}

	/**
	 * 部署流程定义使用zip方式
	 */
	@Test
	public void deployWithZip() {
		InputStream inputStream = this.getClass()// 取得当前class对象
				.getClassLoader()// 获取类加载器
				.getResourceAsStream("diagrams/helloWorld.zip"); // 获取指定文件资源流
		ZipInputStream zipInputStream = new ZipInputStream(inputStream);
		Deployment deployment=processEngine.getRepositoryService() // 获取部署相关Service
				.createDeployment() // 创建部署
				.addZipInputStream(zipInputStream) // 添加zip输入流
				.name("HelloWorld流程") // 流程名称
				.deploy(); // 部署
		System.out.println("流程部署ID:"+deployment.getId()); 
		System.out.println("流程部署Name:"+deployment.getName());
	}

}
```

##### 2.2 流程定义查询

> 查询流程定义；
> 查询某个流程定义的流程设计图片；
> 查询最新版本的流程定义集合；

```java
/**
 * 流程定义查询
 * 流程定义删除
 * @User: vole
 * @date: 2018年3月12日上午9:55:22
 * @Function:
 */
public class ProcessDefinitionTest {

	/**
	 * 获取默认流程引擎实例，会自动读取 activiti.cfg.xml 文件
	 */
	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**
	 * 查询流程定义 返回流程定义集合 对应表 act_re_procdef
	 */
	@Test
	public void list() {
		String processDefinitionKey = "myFirstProcess";
		List<ProcessDefinition> pdList = processEngine.getRepositoryService() // 获取 service
				.createProcessDefinitionQuery() // 创建流程定义查询
				.processDefinitionKey(processDefinitionKey) // 通过 key 查询
				.list(); // 返回一个集合
		for (ProcessDefinition pd : pdList) {
			System.out.println("ID_：" + pd.getId());
			System.out.println("NAME_：" + pd.getName());
			System.out.println("KEY_：" + pd.getKey());
			System.out.println("VERSION_：" + pd.getVersion());
			System.out.println("=========");
		}
	}

	/**
	 * 通过 ID 查询某个流程定义
	 */
	@Test
	public void getById() {
		String processDefinitionId = "myFirstProcess:2:7504";
		ProcessDefinition pd = processEngine.getRepositoryService() // 获取 service
				.createProcessDefinitionQuery() // 创建流程定义查询
				.processDefinitionId(processDefinitionId) // 通过 id 查询
				.singleResult();

		System.out.println("ID_：" + pd.getId());
		System.out.println("NAME_：" + pd.getName());
		System.out.println("KEY_：" + pd.getKey());
		System.out.println("VERSION_：" + pd.getVersion());
	}

	/**
	 * 根据流程部署 id 和资源文件名称来查询流程图片
	 * 
	 * @throws Exception
	 */
	@Test
	public void getImageById() throws Exception {
		InputStream inputStream = processEngine.getRepositoryService()
				.getResourceAsStream("10001","helloWorld/HelloWorld.png");
		FileUtils.copyInputStreamToFile(inputStream, new File("D:/helloWorld.png"));
		System.out.println("success");
	}
	
	/**
	 * 查询最新版本的流程定义
	 * 第一步：我们通过 Activiti 接口来获取根据流程定义 Version 升序排序的流程定义的集合；
	 * 第二步：定义一个有序的 Map， Map 的 key 就是我们流程定义的 Key，Map 的值就是流程定义对象；
     * 第三步：我们遍历第一步的集合，put(key,value)  假如 Key 相同，后者会覆盖前者；
     * 第四步：我们获取 Map 的 values。即我们需要的最新版本的流程定义的集合；
	 * @throws Exception
	 */
	@Test
	public void listLastVersion()throws Exception{
		List<ProcessDefinition> listAll = processEngine.getRepositoryService()// 获取service
				.createProcessDefinitionQuery()// 创建流程定义查询
				.orderByProcessDefinitionVersion().asc()// 根据流程定义版本升序
				.list();// 返回一个集合
		
		// 定义有序 Map，相同的 Key，假如添加 map 的值  后者的值会覆盖前面相同的 key 的值
		Map<String,ProcessDefinition> map=new LinkedHashMap<String,ProcessDefinition>();
		// 遍历集合，根据 key 来覆盖前面的值，来保证最新的 key 覆盖前面所有老的 key 的值
		for(ProcessDefinition pd:listAll){
			map.put(pd.getKey(), pd);
		}
		
		List<ProcessDefinition> pdList=new LinkedList<ProcessDefinition>(map.values());
		for(ProcessDefinition pd:pdList){
			System.out.println("ID_："+pd.getId());
			System.out.println("NAME_："+pd.getName());
			System.out.println("KEY_："+pd.getKey());
			System.out.println("VERSION_："+pd.getVersion());
			System.out.println("=========");
		}
	}

}
```

##### 2.3 流程定义删除

```java
	/**
	 * 删除所有 key 相同的流程定义
	 * @throws Exception
	 */
	@Test
	public void deleteByKey()throws Exception{
		String processDefinitionKey="FirstProcess";
		List<ProcessDefinition> pdList=processEngine.getRepositoryService() // 获取service
				.createProcessDefinitionQuery() // 创建流程定义查询
				.processDefinitionKey(processDefinitionKey) // 根据key查询
				.list();  // 返回一个集合
		for(ProcessDefinition pd:pdList){
			processEngine.getRepositoryService()
				.deleteDeployment(pd.getDeploymentId(),true);
		}
	}
```

##### 2.4 流程定义的 ‘ 修改‘

> 实际情况下，流程定义是不支持修改的，如果想要修改，就重新定义一个新版本用于修改。



#### 三、Activiti  流程实例

##### 3.1 构建学生请假审批流程

![StudentLeaveProcess](F:\JavaDemo\Activiti01\src\main\resources\diagrams\StudentLeaveProcess.png)

##### 3.2 执行对象概念

```java
public class StudentLeaveProcess {

	/**
	 * 获取默认流程引擎实例，会自动读取 activiti.cfg.xml 文件
	 */
	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**
	 * 部署流程定义
	 */
	@Test
	public void deploy() {
		Deployment deployment = processEngine.getRepositoryService()// 获取部署相关Service
				.createDeployment()// 创建部署
				.addClasspathResource("diagrams/StudentLeaveProcess.bpmn") // 加载资源文件
				.addClasspathResource("diagrams/StudentLeaveProcess.png").name("学生请假流程")// 定义流程名称
				.deploy();// 部署
		System.out.println("流程部署ID:" + deployment.getId()); // 流程部署ID:12501
		System.out.println("流程部署Name:" + deployment.getName());// 流程部署Name:学生请假流程
	}

	/**
	 * 启动流程实例
	 */
	@Test
	public void start() {
		ProcessInstance pi = processEngine.getRuntimeService() // 运行时Service
				.startProcessInstanceByKey("StudentLeaveProcess"); // 流程定义表的KEY字段值
		System.out.println("流程实例ID:" + pi.getId());// 流程实例ID:15001
		System.out.println("流程定义ID:" + pi.getProcessDefinitionId()); // 流程定义ID:StudentLeaveProcess:1:12504
	}

	/**
	 * 查看任务
	 */
	@Test
	public void findTask() {
		System.out.println("开始查看任务");
		List<Task> taskList = processEngine.getTaskService() // 任务相关Service
				.createTaskQuery()// 创建任务查询
				.taskAssignee("王五")// 指定某个人
				.list();
		for (Task task : taskList) {
			System.out.println("任务ID:" + task.getId());
			System.out.println("任务名称:" + task.getName());
			System.out.println("任务创建时间:" + task.getCreateTime());
			System.out.println("任务委派人:" + task.getAssignee());
			System.out.println("流程实例ID:" + task.getProcessInstanceId());
		}
		/*
		 * 任务ID:15004 
		 * 任务名称:学生请假 
		 * 任务创建时间:Mon Mar 12 15:22:47 CST 2018
		 * 任务委派人:张三      \李四           \王五
		 * 流程实例ID:15001 \17502  \20002
		 */
	}
	
	/**
	 * 完成任务
	 */
	@Test
	public void completeTask(){
		processEngine.getTaskService() // 任务相关Service
			.complete("20002");//任务 ID
		System.out.println("complete task");
	}

}
```

##### 3.3 判断流程实例状态

```java
	/**
	 * 查询流程状态（正在执行 or 已经执行结束）--> 结束流程实例后会清空所有运行时的表数据，以此来判断
	 */
	@Test
	public void processState() {
		ProcessInstance pi = processEngine.getRuntimeService()// 获取运行时Service
				.createProcessInstanceQuery() // 创建流程实例查询
				.processInstanceId("35001")// 用流程实例id查询
				.singleResult();

		System.out.println(pi != null ? "流程正在执行！" : "流程已经执行结束！");
	}
```

##### 3.4 历史流程实例查询

```java
	/**
	 * 历史任务查询--相关的表 act_hi_taskinst
	 */
	@Test
	public void historyTaskList(){
		List<HistoricTaskInstance> list=processEngine.getHistoryService() // 历史相关Service
			.createHistoricTaskInstanceQuery() // 创建历史任务实例查询
			.processInstanceId("15001") // 用流程实例id查询
			.finished() // 查询已经完成的任务
			.list(); 
		for(HistoricTaskInstance hti:list){
			System.out.println("任务ID:"+hti.getId());
			System.out.println("流程实例ID:"+hti.getProcessInstanceId());
			System.out.println("任务名称："+hti.getName());
			System.out.println("办理人："+hti.getAssignee());
			System.out.println("开始时间："+hti.getStartTime());
			System.out.println("结束时间："+hti.getEndTime());
			System.out.println("=================================");
		}
	}
```

##### 3.5 历史活动查询

```java
	/**
	 * 历史活动查询（一般用这种方式查询）----相关表 act_hi_actinst
	 */
	@Test
	public void historyActInstanceList(){
		List<HistoricActivityInstance>  list=processEngine.getHistoryService() // 历史相关Service
			.createHistoricActivityInstanceQuery() // 创建历史活动实例查询
			.processInstanceId("15001") // 执行流程实例id
			.finished()
			.list();
		for(HistoricActivityInstance hai:list){
			System.out.println("活动ID:"+hai.getId());
			System.out.println("流程实例ID:"+hai.getProcessInstanceId());
			System.out.println("活动名称："+hai.getActivityName());
			System.out.println("办理人："+hai.getAssignee());
			System.out.println("开始时间："+hai.getStartTime());
			System.out.println("结束时间："+hai.getEndTime());
			System.out.println("=================================");
		}
	}
```



#### 四、Activiti  流程变量

##### 4.1 流程变量的概念

> 在流程实例运行过程中，难免要记录或者保存一些数据，然后运行到某个节点的时候，取数据查看，或者是后面学到流程分支的时候判断流程走向，都要用到一些数据存储。
>
> 这里就引入了一个新的概念，流程变量，顾名思义，就是流程中用来存储数据的变量

##### 4.2  使用 TaskService 设置和获取流程变量

```sql
################################
# 设置流程变量涉及到的表

# 运行时流程变量表
SELECT * FROM `act_ru_variable`

# 历史流程变量表
SELECT * FROM `act_hi_varinst`
```

* 新建一个 VariableTest.java 测试类

```java
public class VariableTest {

	/**
	 * 获取默认流程引擎实例，会自动读取 activiti.cfg.xml 文件
	 */
	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**
	 * 部署流程定义
	 */
	@Test
	public void deploy() {
		Deployment deployment = processEngine.getRepositoryService()// 获取部署相关Service
				.createDeployment()// 创建部署
				.addClasspathResource("diagrams/StudentLeaveProcess.bpmn") // 加载资源文件
				.addClasspathResource("diagrams/StudentLeaveProcess.png").name("学生请假流程")// 定义流程名称
				.deploy();// 部署
		System.out.println("流程部署ID:" + deployment.getId()); // 流程部署ID:12501
		System.out.println("流程部署Name:" + deployment.getName());// 流程部署Name:学生请假流程
	}

	/**
	 * 启动流程实例
	 */
	@Test
	public void start() {
		ProcessInstance pi = processEngine.getRuntimeService() // 运行时Service
				.startProcessInstanceByKey("StudentLeaveProcess"); // 流程定义表的KEY字段值
		System.out.println("流程实例ID:" + pi.getId());// 流程实例ID:15001
		System.out.println("流程定义ID:" + pi.getProcessDefinitionId()); // 流程定义ID:StudentLeaveProcess:1:12504
	}

	/**
	 * 查看任务
	 */
	@Test
	public void findTask() {
		System.out.println("开始查看任务");
		List<Task> taskList = processEngine.getTaskService() // 任务相关Service
				.createTaskQuery()// 创建任务查询
				.taskAssignee("李四")// 指定某个人
				.list();
		for (Task task : taskList) {
			System.out.println("任务ID:" + task.getId());
			System.out.println("任务名称:" + task.getName());
			System.out.println("任务创建时间:" + task.getCreateTime());
			System.out.println("任务委派人:" + task.getAssignee());
			System.out.println("流程实例ID:" + task.getProcessInstanceId());
		}
	}

	/**
	 * 完成任务
	 */
	@Test
	public void completeTask() {
		processEngine.getTaskService() // 任务相关Service
				.complete("32502");// 任务 ID
		System.out.println("complete task");
	}

	/**
	 * 设置流程变量数据
	 */
	@Test
	public void setVariableValues() {
		TaskService taskService = processEngine.getTaskService();
		System.out.println("设置流程变量");
		String taskId = "25004";
		taskService.setVariable(taskId, "days", 2);
		taskService.setVariable(taskId, "date", new Date());
		//taskService.setVariableLocal(taskId,"date", new Date()); 局部变量
		taskService.setVariable(taskId, "reason", "发烧");
		Student student = new Student();
		student.setId(1);
		student.setName("张三");
		taskService.setVariable(taskId, "student", student); // 存序列化对象
	}

	/**
	 * 获取流程变量数据
	 */
	@Test
	public void getVariableValues() {
		TaskService taskService = processEngine.getTaskService();
		String taskId = "32502";
		Integer days = (Integer) taskService.getVariable(taskId, "days");
		Date date = (Date) taskService.getVariable(taskId, "date");
		//Date date=(Date) taskService.getVariableLocal(taskId, "date");局部变量
		String reason = (String) taskService.getVariable(taskId, "reason");
		Student student = (Student) taskService.getVariable(taskId, "student");
		System.out.println("请假天数：" + days);
		System.out.println("请假日期：" + date);
		System.out.println("请假原因：" + reason);
		System.out.println("请假对象：" + student.getId() + "," + student.getName());
	}
	
	/**
	 * 设置流程变量数据
	 */
	@Test
	public void setVariableValues2(){
		TaskService taskService=processEngine.getTaskService(); // 任务Service
		String taskId="60004";
		Student student=new Student();
		student.setId(1);
		student.setName("张三");

		Map<String, Object> variables=new HashMap<String,Object>();
		variables.put("days", 2);
		variables.put("date", new Date());
		variables.put("reason", "发烧");
		variables.put("student", student);
		taskService.setVariables(taskId, variables);
	}
	
	/**
	 * 获取流程变量数据
	 */
	@Test
	public void getVariableValues2(){
		TaskService taskService=processEngine.getTaskService(); // 任务Service
		String taskId="65002";
		Map<String,Object> variables=taskService.getVariables(taskId);
		Integer days=(Integer) variables.get("days");
		Date date=(Date) variables.get("date");
		String reason=(String) variables.get("reason");
		Student student=(Student)variables.get("student"); 
		System.out.println("请假天数："+days);
		System.out.println("请假日期："+date);
		System.out.println("请假原因："+reason);
		System.out.println("请假对象："+student.getId()+","+student.getName());
	}
}
```

* 序列化 Student 

```java
public class Student implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String name;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
```

##### 4.3 局部流程变量

> 只存在当前的流程变量

```java
taskService.getVariableLocal(taskId, "date");//设置局部变量

taskService.getVariableLocal(taskId, "date");//获取局部变量
```

##### 4.4 使用 RuntimeService 设置和获取流程变量

* VariableTest2.java

```java
/**
 * 流程变量测试
 * @author user
 *
 */
public class VariableTest2 {

	/**
	 * 获取默认流程引擎实例，会自动读取activiti.cfg.xml文件
	 */
	private ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
	
	/**
	 * 部署流程定义
	 */
	@Test
	public void deploy(){
		Deployment deployment=processEngine.getRepositoryService() // 获取部署相关Service
				.createDeployment() // 创建部署
				.addClasspathResource("diagrams/StudentLeaveProcess.bpmn") // 加载资源文件
				.addClasspathResource("diagrams/StudentLeaveProcess.png") // 加载资源文件
				.name("学生请假流程") // 流程名称
				.deploy(); // 部署
		System.out.println("流程部署ID:"+deployment.getId()); 
		System.out.println("流程部署Name:"+deployment.getName());
	}
	
	/**
	 * 启动流程实例
	 */
	@Test
	public void start(){
		ProcessInstance pi=processEngine.getRuntimeService() // 运行时Service
			.startProcessInstanceByKey("studentLevaeProcess"); // 流程定义表的KEY字段值
		System.out.println("流程实例ID:"+pi.getId());
		System.out.println("流程定义ID:"+pi.getProcessDefinitionId()); 
	}
	
	
	/**
	 * 查看任务
	 */
	@Test
	public void findTask(){
		List<Task> taskList=processEngine.getTaskService() // 任务相关Service
			.createTaskQuery() // 创建任务查询
			.taskAssignee("李四") // 指定某个人
			.list();
		for(Task task:taskList){
			System.out.println("任务ID:"+task.getId()); 
			System.out.println("任务名称:"+task.getName());
			System.out.println("任务创建时间:"+task.getCreateTime());
			System.out.println("任务委派人:"+task.getAssignee());
			System.out.println("流程实例ID:"+task.getProcessInstanceId());
		}
	}
	
	
	/**
	 * 完成任务
	 */
	@Test
	public void completeTask(){
		processEngine.getTaskService() // 任务相关Service
			.complete("97502");
	}
	
	/**
	 * 设置流程变量数据
	 */
	@Test
	public void setVariableValues(){
		RuntimeService runtimeService=processEngine.getRuntimeService(); // 任务Service
		String executionId="90001";
		runtimeService.setVariable(executionId, "days", 2);
		runtimeService.setVariable(executionId, "date", new Date());
		runtimeService.setVariable(executionId, "reason", "发烧");
		Student student=new Student();
		student.setId(1);
		student.setName("张三");
		runtimeService.setVariable(executionId, "student", student); // 存序列化对象
	}
	
	/**
	 * 获取流程变量数据
	 */
	@Test
	public void getVariableValues(){
		RuntimeService runtimeService=processEngine.getRuntimeService(); // 任务Service
		String executionId="90001";
		Integer days=(Integer) runtimeService.getVariable(executionId, "days");
		Date date=(Date) runtimeService.getVariable(executionId, "date");
		String reason=(String) runtimeService.getVariable(executionId, "reason");
		Student student=(Student) runtimeService.getVariable(executionId, "student"); 
		System.out.println("请假天数："+days);
		System.out.println("请假日期："+date);
		System.out.println("请假原因："+reason);
		System.out.println("请假对象："+student.getId()+","+student.getName());
	}
	
}
```

##### 4.5 启动流程的时候设置流程变量

> 在启动流程实例的时候设置变量，很少使用

* VariableTest3 .java

```java
public class VariableTest3 {

	/**
	 * 获取默认流程引擎实例，会自动读取activiti.cfg.xml文件
	 */
	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**
	 * 部署流程定义
	 */
	@Test
	public void deploy() {
		Deployment deployment = processEngine.getRepositoryService() // 获取部署相关Service
				.createDeployment() // 创建部署
				.addClasspathResource("diagrams/StudentLeaveProcess.bpmn") // 加载资源文件
				.addClasspathResource("diagrams/StudentLeaveProcess.png") // 加载资源文件
				.name("学生请假流程") // 流程名称
				.deploy(); // 部署
		System.out.println("流程部署ID:" + deployment.getId());
		System.out.println("流程部署Name:" + deployment.getName());
	}

	/**
	 * 启动流程实例
	 */
	@Test
	public void start() {
		Student student = new Student();
		student.setId(1);
		student.setName("张三");
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("days", 2);
		variables.put("date", new Date());
		variables.put("reason", "发烧");
		variables.put("student", student);

		ProcessInstance pi = processEngine.getRuntimeService() // 运行时Service
				.startProcessInstanceByKey("studentLevaeProcess", variables); // 启动流程的时候，设置流程变量

		System.out.println("流程实例ID:" + pi.getId());
		System.out.println("流程定义ID:" + pi.getProcessDefinitionId());
	}

	/**
	 * 查看任务
	 */
	@Test
	public void findTask() {
		List<Task> taskList = processEngine.getTaskService() // 任务相关Service
				.createTaskQuery() // 创建任务查询
				.taskAssignee("王五") // 指定某个人
				.list();
		for (Task task : taskList) {
			System.out.println("任务ID:" + task.getId());
			System.out.println("任务名称:" + task.getName());
			System.out.println("任务创建时间:" + task.getCreateTime());
			System.out.println("任务委派人:" + task.getAssignee());
			System.out.println("流程实例ID:" + task.getProcessInstanceId());
		}
	}

	/**
	 * 完成任务
	 */
	@Test
	public void completeTask() {
		processEngine.getTaskService() // 任务相关Service
				.complete("107502");
	}

	/**
	 * 设置流程变量数据
	 */
	@Test
	public void setVariableValues() {
		RuntimeService runtimeService = processEngine.getRuntimeService(); // 任务Service
		String executionId = "90001";
		runtimeService.setVariable(executionId, "days", 2);
		runtimeService.setVariable(executionId, "date", new Date());
		runtimeService.setVariable(executionId, "reason", "发烧");
		Student student = new Student();
		student.setId(1);
		student.setName("张三");
		runtimeService.setVariable(executionId, "student", student); // 存序列化对象
	}

	/**
	 * 获取流程变量数据
	 */
	@Test
	public void getVariableValues() {
		RuntimeService runtimeService = processEngine.getRuntimeService(); // 任务Service
		String executionId = "102501";
		Integer days = (Integer) runtimeService.getVariable(executionId, "days");
		Date date = (Date) runtimeService.getVariable(executionId, "date");
		String reason = (String) runtimeService.getVariable(executionId, "reason");
		Student student = (Student) runtimeService.getVariable(executionId, "student");
		System.out.println("请假天数：" + days);
		System.out.println("请假日期：" + date);
		System.out.println("请假原因：" + reason);
		System.out.println("请假对象：" + student.getId() + "," + student.getName());
	}
}
		ProcessInstance pi = processEngine.getRuntimeService() // 运行时Service
				.startProcessInstanceByKey("studentLevaeProcess", variables); // 启动流程的时候，设置流程变量

		System.out.println("流程实例ID:" + pi.getId());
		System.out.println("流程定义ID:" + pi.getProcessDefinitionId());
	}
```

##### 4.6 完成任务的时候设置流程变量

> 完成某个任务后设置流程变量，经常使用

* VariableTest4.java

```java
public class VariableTest4 {

	/**
	 * 获取默认流程引擎实例，会自动读取activiti.cfg.xml文件
	 */
	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**
	 * 部署流程定义
	 */
	@Test
	public void deploy() {
		Deployment deployment = processEngine.getRepositoryService() // 获取部署相关Service
				.createDeployment() // 创建部署
				.addClasspathResource("diagrams/StudentLeaveProcess.bpmn") // 加载资源文件
				.addClasspathResource("diagrams/StudentLeaveProcess.png") // 加载资源文件
				.name("学生请假流程") // 流程名称
				.deploy(); // 部署
		System.out.println("流程部署ID:" + deployment.getId());
		System.out.println("流程部署Name:" + deployment.getName());
	}

	/**
	 * 启动流程实例
	 */
	@Test
	public void start() {
		ProcessInstance pi = processEngine.getRuntimeService() // 运行时Service
				.startProcessInstanceByKey("studentLevaeProcess"); // 流程定义表的KEY字段值
		System.out.println("流程实例ID:" + pi.getId());
		System.out.println("流程定义ID:" + pi.getProcessDefinitionId());
	}

	/**
	 * 查看任务
	 */
	@Test
	public void findTask() {
		List<Task> taskList = processEngine.getTaskService() // 任务相关Service
				.createTaskQuery() // 创建任务查询
				.taskAssignee("王五") // 指定某个人
				.list();
		for (Task task : taskList) {
			System.out.println("任务ID:" + task.getId());
			System.out.println("任务名称:" + task.getName());
			System.out.println("任务创建时间:" + task.getCreateTime());
			System.out.println("任务委派人:" + task.getAssignee());
			System.out.println("流程实例ID:" + task.getProcessInstanceId());
		}
	}

	/**
	 * 完成任务
	 */
	@Test
	public void completeTask() {
		processEngine.getTaskService() // 任务相关Service
				.complete("117502");
	}

	/**
	 * 完成任务
	 */
	@Test
	public void completeTask2() {
		Student student = new Student();
		student.setId(1);
		student.setName("张三");
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("days", 2);
		variables.put("date", new Date());
		variables.put("reason", "发烧");
		variables.put("student", student);
		processEngine.getTaskService() // 任务相关Service
				.complete("112504", variables); // 完成任务的时候，设置流程变量
	}

	/**
	 * 设置流程变量数据
	 */
	@Test
	public void setVariableValues() {
		RuntimeService runtimeService = processEngine.getRuntimeService(); // 任务Service
		String executionId = "90001";
		runtimeService.setVariable(executionId, "days", 2);
		runtimeService.setVariable(executionId, "date", new Date());
		runtimeService.setVariable(executionId, "reason", "发烧");
		Student student = new Student();
		student.setId(1);
		student.setName("张三");
		runtimeService.setVariable(executionId, "student", student); // 存序列化对象
	}

	/**
	 * 获取流程变量数据
	 */
	@Test
	public void getVariableValues() {
		RuntimeService runtimeService = processEngine.getRuntimeService(); // 任务Service
		String executionId = "112501";
		Integer days = (Integer) runtimeService.getVariable(executionId, "days");
		Date date = (Date) runtimeService.getVariable(executionId, "date");
		String reason = (String) runtimeService.getVariable(executionId, "reason");
		Student student = (Student) runtimeService.getVariable(executionId, "student");
		System.out.println("请假天数：" + days);
		System.out.println("请假日期：" + date);
		System.out.println("请假原因：" + reason);
		System.out.println("请假对象：" + student.getId() + "," + student.getName());
	}

}
```

####  五、Activiti  流程控制网关

##### 5.1 连线

* 重新绘制流程定义图，改变业务，当是一般情况的请假审批，班长审批就行，假如是特殊情况的请假

  审批，我们需要让班主任也审批下。 所以在班长审批的时候，就会有分支；

![StudentLeaveProcess2](F:\JavaDemo\Activiti01\src\main\resources\diagrams\StudentLeaveProcess2.png)

* 设置连线表达式，类似 el 表达式

![连线表达式](E:\githubSample\JAVA-Learn\笔记\Activiti\photo\连线表达式.PNG)

* 新建 LineTest.java 测试类

```java
public class LineTest {

	/**
	 * 获取默认流程引擎实例，会自动读取 activiti.cfg.xml 文件
	 */
	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**
	 * 部署流程定义
	 */
	@Test
	public void deploy() {
		Deployment deployment = processEngine.getRepositoryService()// 获取部署相关Service
				.createDeployment()// 创建部署
				.addClasspathResource("diagrams/StudentLeaveProcess2.bpmn") // 加载资源文件
				.addClasspathResource("diagrams/StudentLeaveProcess2.png")
				.name("学生请假流程2")// 定义流程名称
				.deploy();// 部署
		System.out.println("流程部署ID:" + deployment.getId()); // 流程部署ID:12501
		System.out.println("流程部署Name:" + deployment.getName());// 流程部署Name:学生请假流程
	}

	/**
	 * 启动流程实例
	 */
	@Test
	public void start() {
		ProcessInstance pi = processEngine.getRuntimeService() // 运行时Service
				.startProcessInstanceByKey("StudentLeaveProcess2"); // 流程定义表的KEY字段值
		System.out.println("流程实例ID:" + pi.getId());
		System.out.println("流程定义ID:" + pi.getProcessDefinitionId()); 
	}

	/**
	 * 查看任务--act_ru_task
	 */
	@Test
	public void findTask() {
		System.out.println("开始查看任务");
		List<Task> taskList = processEngine.getTaskService() // 任务相关Service
				.createTaskQuery()// 创建任务查询
				.taskAssignee("王五")// 指定某个人
				.list();
		for (Task task : taskList) {
			System.out.println("任务ID:" + task.getId());
			System.out.println("任务名称:" + task.getName());
			System.out.println("任务创建时间:" + task.getCreateTime());
			System.out.println("任务委派人:" + task.getAssignee());
			System.out.println("流程实例ID:" + task.getProcessInstanceId());
		}
	}

	/**
	 * 完成任务
	 */
	@Test
	public void completeTask() {
		processEngine.getTaskService() // 任务相关Service
				.complete("57503");// 任务 ID
		System.out.println("complete task");
	}
	
	/**
	 * 完成任务
	 */
	@Test
	public void completeTask2() {
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("msg", "特殊情况");
		processEngine.getTaskService() // 任务相关Service
				.complete("55002", variables); // 完成任务的时候，设置流程变量
	}
}
```

* 当流程执行到班长审批的时候，通过设置流程变量，来执行具体的流程走向。

##### 5.2 排它网关

>  执行到该网关，根据条件只能走一条执行线路；

* 绘制新的流程图

![StudentLeaveProcess3](F:\JavaDemo\Activiti01\src\main\resources\diagrams\StudentLeaveProcess3.png)

* 添加表达式

![排它网关_2](E:\githubSample\JAVA-Learn\笔记\Activiti\photo\排它网关_2.PNG)

* 选择排他网关，设置默认执行的线路为校长那条线路；

![排它网关_3](E:\githubSample\JAVA-Learn\笔记\Activiti\photo\排它网关_3.PNG)

* 测试 类 ExclusiveGatewayTest.java

```java
public class ExclusiveGatewayTest {

	/**
	 * 获取默认流程引擎实例，会自动读取 activiti.cfg.xml 文件
	 */
	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**
	 * 部署流程定义
	 */
	@Test
	public void deploy() {
		Deployment deployment = processEngine.getRepositoryService()// 获取部署相关Service
				.createDeployment()// 创建部署
				.addClasspathResource("diagrams/StudentLeaveProcess3.bpmn") // 加载资源文件
				.addClasspathResource("diagrams/StudentLeaveProcess3.png")
				.name("学生请假流程3")// 定义流程名称
				.deploy();// 部署
		System.out.println("流程部署ID:" + deployment.getId()); 
		System.out.println("流程部署Name:" + deployment.getName());
	}

	/**
	 * 启动流程实例
	 */
	@Test
	public void start() {
		ProcessInstance pi = processEngine.getRuntimeService() // 运行时Service
				.startProcessInstanceByKey("StudentLeaveProcess3"); // 流程定义表的KEY字段值
		System.out.println("流程实例ID:" + pi.getId());
		System.out.println("流程定义ID:" + pi.getProcessDefinitionId()); 
	}

	/**
	 * 查看任务--act_ru_task
	 */
	@Test
	public void findTask() {
		System.out.println("开始查看任务");
		List<Task> taskList = processEngine.getTaskService() // 任务相关Service
				.createTaskQuery()// 创建任务查询
				.taskAssignee("李四")// 指定某个人
				.list();
		for (Task task : taskList) {
			System.out.println("任务ID:" + task.getId());
			System.out.println("任务名称:" + task.getName());
			System.out.println("任务创建时间:" + task.getCreateTime());
			System.out.println("任务委派人:" + task.getAssignee());
			System.out.println("流程实例ID:" + task.getProcessInstanceId());
		}
	}

	/**
	 * 完成任务
	 */
	@Test
	public void completeTask() {
		processEngine.getTaskService() // 任务相关Service
				.complete("67504");// 任务 ID
		System.out.println("complete task");
	}
	
	/**
	 * 完成任务--添加流程变量
	 */
	@Test
	public void completeTask2() {
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("days", 2);
		processEngine.getTaskService() // 任务相关Service
				.complete("65004", variables); // 完成任务的时候，设置流程变量
		System.out.println("complete task");
	}
}
```

##### 5.3 并行网关

> 执行到该网关，会有多条线路同时并行执行，当都执行完才继续执行后面的

* 创建新的流程图，并行执行，不用添加条件

![StudentLeaveProcess4](F:\JavaDemo\Activiti01\src\main\resources\diagrams\StudentLeaveProcess4.png)

* 执行测试

```java
public class ExclusiveGatewayTest {

	/**
	 * 获取默认流程引擎实例，会自动读取 activiti.cfg.xml 文件
	 */
	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**
	 * 部署流程定义
	 */
	@Test
	public void deploy() {
		Deployment deployment = processEngine.getRepositoryService()// 获取部署相关Service
				.createDeployment()// 创建部署
				.addClasspathResource("diagrams/StudentLeaveProcess4.bpmn") // 加载资源文件
				.addClasspathResource("diagrams/StudentLeaveProcess4.png")
				.name("学生请假流程4")// 定义流程名称
				.deploy();// 部署
		System.out.println("流程部署ID:" + deployment.getId()); 
		System.out.println("流程部署Name:" + deployment.getName());
	}

	/**
	 * 启动流程实例
	 */
	@Test
	public void start() {
		ProcessInstance pi = processEngine.getRuntimeService() // 运行时Service
				.startProcessInstanceByKey("StudentLeaveProcess4"); // 流程定义表的KEY字段值
		System.out.println("流程实例ID:" + pi.getId());
		System.out.println("流程定义ID:" + pi.getProcessDefinitionId()); 
	}

	/**
	 * 查看任务--act_ru_task
	 */
	@Test
	public void findTask() {
		System.out.println("开始查看任务");
		List<Task> taskList = processEngine.getTaskService() // 任务相关Service
				.createTaskQuery()// 创建任务查询
				.taskAssignee("张三")// 指定某个人
				.list();
		for (Task task : taskList) {
			System.out.println("任务ID:" + task.getId());
			System.out.println("任务名称:" + task.getName());
			System.out.println("任务创建时间:" + task.getCreateTime());
			System.out.println("任务委派人:" + task.getAssignee());
			System.out.println("流程实例ID:" + task.getProcessInstanceId());
		}
	}

	/**
	 * 完成任务
	 */
	@Test
	public void completeTask() {
		processEngine.getTaskService() // 任务相关Service
				.complete("82503");// 任务 ID
		System.out.println("complete task");
	}
	
}
```

#### 六、Activiti  任务分配

##### 6.1 个人任务分配

###### 6.1.1 直接流程图配置中写死

* 流程图 StudentLeaveProcess5.bpmn

![个人任务_直接写死](E:\githubSample\JAVA-Learn\笔记\Activiti\photo\个人任务_直接写死.PNG)

* 测试 AssignTest1.java

```java
/**
 * 个人任务--直接流程图配置中写死
 * @User: vole
 * @date: 2018年3月13日上午11:37:58
 * @Function:
 */
public class AssignTest1 {

	/**
	 * 获取默认流程引擎实例，会自动读取 activiti.cfg.xml 文件
	 */
	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**
	 * 部署流程定义
	 */
	@Test
	public void deploy() {
		Deployment deployment = processEngine.getRepositoryService()// 获取部署相关Service
				.createDeployment()// 创建部署
				.addClasspathResource("diagrams/StudentLeaveProcess5.bpmn") // 加载资源文件
				.addClasspathResource("diagrams/StudentLeaveProcess5.png")
				.name("学生请假流程5")// 定义流程名称
				.deploy();// 部署
		System.out.println("流程部署ID:" + deployment.getId()); 
		System.out.println("流程部署Name:" + deployment.getName());
	}

	/**
	 * 启动流程实例
	 */
	@Test
	public void start() {
		ProcessInstance pi = processEngine.getRuntimeService() // 运行时Service
				.startProcessInstanceByKey("StudentLeaveProcess5"); // 流程定义表的KEY字段值
		System.out.println("流程实例ID:" + pi.getId());
		System.out.println("流程定义ID:" + pi.getProcessDefinitionId()); 
	}

	/**
	 * 查看任务--act_ru_task
	 */
	@Test
	public void findTask() {
		System.out.println("开始查看任务");
		List<Task> taskList = processEngine.getTaskService() // 任务相关Service
				.createTaskQuery()// 创建任务查询
				.taskAssignee("张三")// 指定某个人
				.list();
		for (Task task : taskList) {
			System.out.println("任务ID:" + task.getId());
			System.out.println("任务名称:" + task.getName());
			System.out.println("任务创建时间:" + task.getCreateTime());
			System.out.println("任务委派人:" + task.getAssignee());
			System.out.println("流程实例ID:" + task.getProcessInstanceId());
		}
	}

	/**
	 * 完成任务
	 */
	@Test
	public void completeTask() {
		processEngine.getTaskService() // 任务相关Service
				.complete("90004");// 任务 ID
		System.out.println("complete task");
	}
	
}
```

###### 6.1.2 使用流程变量

* 流程图 StudentLeaveProcess6.bpmn

![个人任务_使用流程变量](E:\githubSample\JAVA-Learn\笔记\Activiti\photo\个人任务_使用流程变量.PNG)

* 测试 AssignTest2.java

```java
/**
 * 个人任务--使用流程变量
 * @User: vole
 * @date: 2018年3月13日上午11:37:58
 * @Function:
 */
public class AssignTest2 {

	/**
	 * 获取默认流程引擎实例，会自动读取 activiti.cfg.xml 文件
	 */
	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**
	 * 部署流程定义
	 */
	@Test
	public void deploy() {
		Deployment deployment = processEngine.getRepositoryService()// 获取部署相关Service
				.createDeployment()// 创建部署
				.addClasspathResource("diagrams/StudentLeaveProcess6.bpmn") // 加载资源文件
				.addClasspathResource("diagrams/StudentLeaveProcess6.png")
				.name("学生请假流程6")// 定义流程名称
				.deploy();// 部署
		System.out.println("流程部署ID:" + deployment.getId()); 
		System.out.println("流程部署Name:" + deployment.getName());
	}

	/**
	 * 启动流程实例
	 */
	@Test
	public void start() {
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("userId", " 李四");
		ProcessInstance pi = processEngine.getRuntimeService() // 运行时Service
				.startProcessInstanceByKey("StudentLeaveProcess6",variables); // 流程定义表的KEY字段值
		System.out.println("流程实例ID:" + pi.getId());
		System.out.println("流程定义ID:" + pi.getProcessDefinitionId()); 
	}

	/**
	 * 查看任务--act_ru_task
	 */
	@Test
	public void findTask() {
		System.out.println("开始查看任务");
		List<Task> taskList = processEngine.getTaskService() // 任务相关Service
				.createTaskQuery()// 创建任务查询
				.taskAssignee("李四")// 指定某个人
				.list();
		for (Task task : taskList) {
			System.out.println("任务ID:" + task.getId());
			System.out.println("任务名称:" + task.getName());
			System.out.println("任务创建时间:" + task.getCreateTime());
			System.out.println("任务委派人:" + task.getAssignee());
			System.out.println("流程实例ID:" + task.getProcessInstanceId());
		}
	}

	/**
	 * 完成任务
	 */
	@Test
	public void completeTask() {
		processEngine.getTaskService() // 任务相关Service
				.complete("97505");// 任务 ID
		System.out.println("complete task");
	}
	
}
```

###### 6.1.3 TaskListener 监听实现

* 创建 MyTaskListener 并继承 TaskListener

```java
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class MyTaskListener implements TaskListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void notify(DelegateTask delegateTask) {
		delegateTask.setAssignee("王五");//指定办理人
	}

}
```

* 流程图 StudentLeaveProcess7.bpmn

![个人任务_TaskListener 监听实现](E:\githubSample\JAVA-Learn\笔记\Activiti\photo\个人任务_TaskListener 监听实现.PNG)

* 测试 AssignTest3.java

```java
/**
 * 个人任务--TaskListener 监听实现
 * @User: vole
 * @date: 2018年3月13日上午11:37:58
 * @Function:
 */
public class AssignTest3 {

	/**
	 * 获取默认流程引擎实例，会自动读取 activiti.cfg.xml 文件
	 */
	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**
	 * 部署流程定义
	 */
	@Test
	public void deploy() {
		Deployment deployment = processEngine.getRepositoryService()// 获取部署相关Service
				.createDeployment()// 创建部署
				.addClasspathResource("diagrams/StudentLeaveProcess7.bpmn") // 加载资源文件
				.addClasspathResource("diagrams/StudentLeaveProcess7.png")
				.name("学生请假流程7")// 定义流程名称
				.deploy();// 部署
		System.out.println("流程部署ID:" + deployment.getId()); 
		System.out.println("流程部署Name:" + deployment.getName());
	}

	/**
	 * 启动流程实例
	 */
	@Test
	public void start() {
		ProcessInstance pi = processEngine.getRuntimeService() // 运行时Service
				.startProcessInstanceByKey("StudentLeaveProcess7"); // 流程定义表的KEY字段值
		System.out.println("流程实例ID:" + pi.getId());
		System.out.println("流程定义ID:" + pi.getProcessDefinitionId()); 
	}

	/**
	 * 查看任务--act_ru_task
	 */
	@Test
	public void findTask() {
		System.out.println("开始查看任务");
		List<Task> taskList = processEngine.getTaskService() // 任务相关Service
				.createTaskQuery()// 创建任务查询
				.taskAssignee("王五")// 指定某个人
				.list();
		for (Task task : taskList) {
			System.out.println("任务ID:" + task.getId());
			System.out.println("任务名称:" + task.getName());
			System.out.println("任务创建时间:" + task.getCreateTime());
			System.out.println("任务委派人:" + task.getAssignee());
			System.out.println("流程实例ID:" + task.getProcessInstanceId());
		}
	}

	/**
	 * 完成任务
	 */
	@Test
	public void completeTask() {
		processEngine.getTaskService() // 任务相关Service
				.complete("117504");// 任务 ID
		System.out.println("complete task");
	}
	
}
```

##### 6.2 多用户任务分配

###### 6.2.1 直接流程图配置中写死

* 流程图 MultiUserProcess.bpmn

![多用户_直接写死](E:\githubSample\JAVA-Learn\笔记\Activiti\photo\多用户_直接写死.PNG)

* 测试类 AssignTest1.java

```java
/**
 * 多用户任务--直接流程图配置中写死
 * @User: vole
 * @date: 2018年3月13日上午11:37:58
 * @Function:
 */
public class AssignTest1 {

	/**
	 * 获取默认流程引擎实例，会自动读取 activiti.cfg.xml 文件
	 */
	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**
	 * 部署流程定义
	 */
	@Test
	public void deploy() {
		Deployment deployment = processEngine.getRepositoryService()// 获取部署相关Service
				.createDeployment()// 创建部署
				.addClasspathResource("diagrams/MultiUserProcess.bpmn") // 加载资源文件
				.addClasspathResource("diagrams/MultiUserProcess.png")
				.name("学生请假流程_多用户")// 定义流程名称
				.deploy();// 部署
		System.out.println("流程部署ID:" + deployment.getId()); 
		System.out.println("流程部署Name:" + deployment.getName());
	}

	/**
	 * 启动流程实例
	 */
	@Test
	public void start() {
		ProcessInstance pi = processEngine.getRuntimeService() // 运行时Service
				.startProcessInstanceByKey("MultiUserProcess"); // 流程定义表的KEY字段值
		System.out.println("流程实例ID:" + pi.getId());
		System.out.println("流程定义ID:" + pi.getProcessDefinitionId()); 
	}

	/**
	 * 查看任务--act_ru_task
	 */
	@Test
	public void findTask() {
		System.out.println("开始查看任务");
		List<Task> taskList = processEngine.getTaskService() // 任务相关Service
				.createTaskQuery()// 创建任务查询
				//.taskAssignee("张三")// 指定某个人
				.taskCandidateUser("gakki")
				.list();
		for (Task task : taskList) {
			System.out.println("任务ID:" + task.getId());
			System.out.println("任务名称:" + task.getName());
			System.out.println("任务创建时间:" + task.getCreateTime());
			System.out.println("任务委派人:" + task.getAssignee());
			System.out.println("流程实例ID:" + task.getProcessInstanceId());
		}
	}

	/**
	 * 完成任务
	 */
	@Test
	public void completeTask() {
		processEngine.getTaskService() // 任务相关Service
				.complete("125004");// 任务 ID
		System.out.println("complete task");
	}
	
}
```

###### 6.2.2 使用流程变量

* 流程图 MultiUserProcess2.bpmn

![多用户_流程变量](E:\githubSample\JAVA-Learn\笔记\Activiti\photo\多用户_流程变量.PNG)

* 测试 AssignTest2.java

```java
**
 * 多用户任务--使用流程变量
 * @User: vole
 * @date: 2018年3月13日上午11:37:58
 * @Function:
 */
public class AssignTest2 {

	/**
	 * 获取默认流程引擎实例，会自动读取 activiti.cfg.xml 文件
	 */
	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**
	 * 部署流程定义
	 */
	@Test
	public void deploy() {
		Deployment deployment = processEngine.getRepositoryService()// 获取部署相关Service
				.createDeployment()// 创建部署
				.addClasspathResource("diagrams/MultiUserProcess2.bpmn") // 加载资源文件
				.addClasspathResource("diagrams/MultiUserProcess2.png")
				.name("学生请假流程_多用户2")// 定义流程名称
				.deploy();// 部署
		System.out.println("流程部署ID:" + deployment.getId()); 
		System.out.println("流程部署Name:" + deployment.getName());
	}

	/**
	 * 启动流程实例
	 */
	@Test
	public void start() {
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("userIds", "vole,gakki,wuji");
		ProcessInstance pi = processEngine.getRuntimeService() // 运行时Service
				.startProcessInstanceByKey("MultiUserProcess2",variables); // 流程定义表的KEY字段值
		System.out.println("流程实例ID:" + pi.getId());
		System.out.println("流程定义ID:" + pi.getProcessDefinitionId()); 
	}

	/**
	 * 查看任务--act_ru_task
	 */
	@Test
	public void findTask() {
		System.out.println("开始查看任务");
		List<Task> taskList = processEngine.getTaskService() // 任务相关Service
				.createTaskQuery()// 创建任务查询
				.taskCandidateUser("gakki")
				.list();
		for (Task task : taskList) {
			System.out.println("任务ID:" + task.getId());
			System.out.println("任务名称:" + task.getName());
			System.out.println("任务创建时间:" + task.getCreateTime());
			System.out.println("任务委派人:" + task.getAssignee());
			System.out.println("流程实例ID:" + task.getProcessInstanceId());
		}
	}

	/**
	 * 完成任务
	 */
	@Test
	public void completeTask() {
		processEngine.getTaskService() // 任务相关Service
				.complete("132505");// 任务 ID
		System.out.println("complete task");
	}
	
}
```

###### 6.2.3 TaskListener 监听实现

* 创建 MyTaskListener 并继承 TaskListener

```java
public class MyTaskListener implements TaskListener{

	private static final long serialVersionUID = 1L;

	public void notify(DelegateTask delegateTask) {
		delegateTask.addCandidateUser("vole");
		delegateTask.addCandidateUser("gakki");
		delegateTask.addCandidateUser("wuji");//指定办理人
	}

}
```

* 流程图 MultiUserProcess3.bpmn

![多用户_TaskListener](E:\githubSample\JAVA-Learn\笔记\Activiti\photo\多用户_TaskListener.PNG)

* 测试 AssignTest3.java

```java
/**
 * 多用户任务--TaskListener 监听实现
 * @User: vole
 * @date: 2018年3月13日上午11:37:58
 * @Function:
 */
public class AssignTest3 {

	/**
	 * 获取默认流程引擎实例，会自动读取 activiti.cfg.xml 文件
	 */
	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**
	 * 部署流程定义
	 */
	@Test
	public void deploy() {
		Deployment deployment = processEngine.getRepositoryService()// 获取部署相关Service
				.createDeployment()// 创建部署
				.addClasspathResource("diagrams/MultiUserProcess3.bpmn") // 加载资源文件
				.addClasspathResource("diagrams/MultiUserProcess3.png")
				.name("学生请假流程_多用户3")// 定义流程名称
				.deploy();// 部署
		System.out.println("流程部署ID:" + deployment.getId()); 
		System.out.println("流程部署Name:" + deployment.getName());
	}

	/**
	 * 启动流程实例
	 */
	@Test
	public void start() {
		ProcessInstance pi = processEngine.getRuntimeService() // 运行时Service
				.startProcessInstanceByKey("MultiUserProcess3"); // 流程定义表的KEY字段值
		System.out.println("流程实例ID:" + pi.getId());
		System.out.println("流程定义ID:" + pi.getProcessDefinitionId()); 
	}

	/**
	 * 查看任务--act_ru_task
	 */
	@Test
	public void findTask() {
		System.out.println("开始查看任务");
		List<Task> taskList = processEngine.getTaskService() // 任务相关Service
				.createTaskQuery()// 创建任务查询
				//.taskAssignee("张三")// 指定某个人
				.taskCandidateUser("gakki")
				.list();
		for (Task task : taskList) {
			System.out.println("任务ID:" + task.getId());
			System.out.println("任务名称:" + task.getName());
			System.out.println("任务创建时间:" + task.getCreateTime());
			System.out.println("任务委派人:" + task.getAssignee());
			System.out.println("流程实例ID:" + task.getProcessInstanceId());
		}
	}

	/**
	 * 完成任务
	 */
	@Test
	public void completeTask() {
		processEngine.getTaskService() // 任务相关Service
				.complete("140004");// 任务 ID
		System.out.println("complete task");
	}
	
}
```

##### 6.3 组任务分配

* 组任务主要靠 **act_id_user**、**act_id_group**、**act_id_membership** 这三张表，用 **IdentityService** 来控制。

![组任务_表](E:\githubSample\JAVA-Learn\笔记\Activiti\photo\组任务_表.PNG)

* IdentityTest.java

```java
public class IdentityTest {

	/**
	 * 获取默认流程引擎实例，会自动读取activiti.cfg.xml文件
	 */
	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**
	 * 添加用户测试
	 */
	@Test
	public void testSaveUser() {
		IdentityService identityService = processEngine.getIdentityService();
		User user = new UserEntity();
		user.setId("wuji");
		user.setPassword("123456");
		user.setEmail("wuvole@gmail.com");
		identityService.saveUser(user );
		System.out.println("添加 success");
	}
	
	/**
	 * 删除用户
	 */
	@Test
	public void testDeleteUser(){
		IdentityService indentityService=processEngine.getIdentityService();
		indentityService.deleteUser("gakki");
		System.out.println("删除 success");
	}
	
	/**
	 * 添加组
	 */
	@Test
	public void testSaveGroup(){
		IdentityService indentityService=processEngine.getIdentityService();
		Group group=new GroupEntity();
		group.setId("test");
		indentityService.saveGroup(group);
		System.out.println("添加 success");
	}
	
	/**
	 * 测试删除组
	 */
	@Test
	public void testDeleteGroup(){
		IdentityService indentityService=processEngine.getIdentityService();
		indentityService.deleteGroup("test");
		System.out.println("删除 success");
	}
	
	/**
	 * 测试添加用户和组的关联关系
	 */
	@Test
	public void testSaveMembership(){
		IdentityService indentityService=processEngine.getIdentityService();
		indentityService.createMembership("gakki", "dev");
		System.out.println("添加 success");
	}
	
	/**
	 * 测试删除用户和组的关联关系
	 */
	@Test
	public void testDeleteMembership(){
		IdentityService indentityService=processEngine.getIdentityService();
		indentityService.deleteMembership("lisi", "dev");
		System.out.println("删除 success");
	}

}
```

###### 6.3.1 直接流程图配置中写死

* 流程图 GroupProcess.bpmn

![组任务_直接流程图](E:\githubSample\JAVA-Learn\笔记\Activiti\photo\组任务_直接流程图.PNG)

* AssignTest1.java

```java
/**
 * 组任务分配--直接流程图配置中写死
 * @User: vole
 * @date: 2018年3月13日上午11:37:58
 * @Function:
 */
public class AssignTest1 {

	/**
	 * 获取默认流程引擎实例，会自动读取 activiti.cfg.xml 文件
	 */
	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**
	 * 部署流程定义
	 */
	@Test
	public void deploy() {
		Deployment deployment = processEngine.getRepositoryService()// 获取部署相关Service
				.createDeployment()// 创建部署
				.addClasspathResource("diagrams/GroupProcess.bpmn") // 加载资源文件
				.addClasspathResource("diagrams/GroupProcess.png")
				.name("学生请假流程_组用户")// 定义流程名称
				.deploy();// 部署
		System.out.println("流程部署ID:" + deployment.getId()); 
		System.out.println("流程部署Name:" + deployment.getName());
	}

	/**
	 * 启动流程实例
	 */
	@Test
	public void start() {
		ProcessInstance pi = processEngine.getRuntimeService() // 运行时Service
				.startProcessInstanceByKey("GroupProcess"); // 流程定义表的KEY字段值
		System.out.println("流程实例ID:" + pi.getId());
		System.out.println("流程定义ID:" + pi.getProcessDefinitionId()); 
	}

	/**
	 * 查看任务--act_ru_task
	 */
	@Test
	public void findTask() {
		System.out.println("开始查看任务");
		List<Task> taskList = processEngine.getTaskService() // 任务相关Service
				.createTaskQuery()// 创建任务查询
				//.taskAssignee("张三")// 指定某个人
				.taskCandidateUser("gakki")
				.list();
		for (Task task : taskList) {
			System.out.println("任务ID:" + task.getId());
			System.out.println("任务名称:" + task.getName());
			System.out.println("任务创建时间:" + task.getCreateTime());
			System.out.println("任务委派人:" + task.getAssignee());
			System.out.println("流程实例ID:" + task.getProcessInstanceId());
		}
	}

	/**
	 * 完成任务
	 */
	@Test
	public void completeTask() {
		processEngine.getTaskService() // 任务相关Service
				.complete("155004");// 任务 ID
		System.out.println("complete task");
	}
	
}
```

###### 6.3.2 使用流程变量

* 流程图 GroupProcess2.bpmn

![组任务_使用流程变量](E:\githubSample\JAVA-Learn\笔记\Activiti\photo\组任务_使用流程变量.PNG)



* AssignTest2.java

```java
/**
 * 组任务分配--使用流程变量
 * @User: vole
 * @date: 2018年3月13日上午11:37:58
 * @Function:
 */
public class AssignTest2 {

	/**
	 * 获取默认流程引擎实例，会自动读取 activiti.cfg.xml 文件
	 */
	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**
	 * 部署流程定义
	 */
	@Test
	public void deploy() {
		Deployment deployment = processEngine.getRepositoryService()// 获取部署相关Service
				.createDeployment()// 创建部署
				.addClasspathResource("diagrams/GroupProcess2.bpmn") // 加载资源文件
				.addClasspathResource("diagrams/GroupProcess2.png")
				.name("学生请假流程_组用户2")// 定义流程名称
				.deploy();// 部署
		System.out.println("流程部署ID:" + deployment.getId()); 
		System.out.println("流程部署Name:" + deployment.getName());
	}

	/**
	 * 启动流程实例
	 */
	@Test
	public void start() {
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("groupId", "test");
		ProcessInstance pi = processEngine.getRuntimeService() // 运行时Service
				.startProcessInstanceByKey("GroupProcess2",variables); // 流程定义表的KEY字段值
		System.out.println("流程实例ID:" + pi.getId());
		System.out.println("流程定义ID:" + pi.getProcessDefinitionId()); 
	}

	/**
	 * 查看任务--act_ru_task
	 */
	@Test
	public void findTask() {
		System.out.println("开始查看任务");
		List<Task> taskList = processEngine.getTaskService() // 任务相关Service
				.createTaskQuery()// 创建任务查询
				.taskCandidateUser("wuji")
				.list();
		for (Task task : taskList) {
			System.out.println("任务ID:" + task.getId());
			System.out.println("任务名称:" + task.getName());
			System.out.println("任务创建时间:" + task.getCreateTime());
			System.out.println("任务委派人:" + task.getAssignee());
			System.out.println("流程实例ID:" + task.getProcessInstanceId());
		}
	}

	/**
	 * 完成任务
	 */
	@Test
	public void completeTask() {
		processEngine.getTaskService() // 任务相关Service
				.complete("167505");// 任务 ID
		System.out.println("complete task");
	}
	
}
```

