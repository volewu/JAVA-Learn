package com.vole.webservice.impl;

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;

import com.vole.Interceptor.MyInInterceptor;
import com.vole.webservice.HelloWorld;

public class Server {
	public static void main(String[] args) {
		System.out.println("start");
		HelloWorld implementor = new HelloWorldImpl();
		String address = "http://10.132.45.225/helloWorld";
		// jdkʵ�� ��¶webservice�ӿ� :http://10.132.45.225/helloWorld?wsdl
		// Endpoint.publish(address, implementor);
		JaxWsServerFactoryBean factoryBean = new JaxWsServerFactoryBean();
		factoryBean.setAddress(address);// ���ñ�¶��ַ
		factoryBean.setServiceClass(HelloWorld.class);// �ӿ���
		factoryBean.setServiceBean(implementor); // ����ʵ����
		factoryBean.getInInterceptors().add(new LoggingInInterceptor());// ��� in ��������־������
		factoryBean.getOutInterceptors().add(new LoggingOutInterceptor());// ��� out ��������־������
		//����Զ���������
		factoryBean.getInInterceptors().add(new MyInInterceptor());
		
		factoryBean.create();// ����webservice�ӿ�
		System.out.println("started");
	}
}
