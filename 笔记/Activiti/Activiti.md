## Activiti 备忘录

--------------------目录--------------------

* 一、Activiti 简介
  * 1.1 什么是 Activiti
  * 1.2 HelloWorld 的实现
    - 1.2.1 Activiti 的 25 张表
    - 1.2.2 eclipse 安装 activiti 插件

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

* [离线安装](eclipse 安装activiti-designer)
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

