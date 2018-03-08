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
		// jdk实现 暴露webservice接口 :http://10.132.45.225/helloWorld?wsdl
		// Endpoint.publish(address, implementor);
		JaxWsServerFactoryBean factoryBean = new JaxWsServerFactoryBean();
		factoryBean.setAddress(address);// 设置暴露地址
		factoryBean.setServiceClass(HelloWorld.class);// 接口类
		factoryBean.setServiceBean(implementor); // 设置实现类
		factoryBean.getInInterceptors().add(new LoggingInInterceptor());// 添加 in 拦截器日志拦截器
		factoryBean.getOutInterceptors().add(new LoggingOutInterceptor());// 添加 out 拦截器日志拦截器
		//添加自定义拦截器
		factoryBean.getInInterceptors().add(new MyInInterceptor());
		
		factoryBean.create();// 创建webservice接口
		System.out.println("started");
	}
}
