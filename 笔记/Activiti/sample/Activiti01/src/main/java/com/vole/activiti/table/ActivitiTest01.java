package com.vole.activiti.table;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;

public class ActivitiTest01 {

	/**
	 * ���� Activiti ��Ҫ�� 25 ��
	 */
	@Test
	public void testActivitiCreateTable() {
		// ��������
		ProcessEngineConfiguration pec = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
		pec.setJdbcDriver("com.mysql.jdbc.Driver");
		pec.setJdbcUrl("jdbc:mysql://localhost:3306/db_activiti");
		pec.setJdbcUsername("root");
		pec.setJdbcPassword("123456");

		/**
		 * false �����Զ������� 
		 * create-drop ��ɾ�����ٴ����� 
		 * true �Զ������͸��±�
		 */
		pec.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

		// ��ȡ�����������
		ProcessEngine processEngine = pec.buildProcessEngine();
	}

	/**
	 * ʹ��xml���� ��
	 */
	@Test
	public void testCreateTableWithXml() {
		// ��������
		ProcessEngineConfiguration pec = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
		// ��ȡ�����������
		ProcessEngine processEngine = pec.buildProcessEngine();
	}

}
