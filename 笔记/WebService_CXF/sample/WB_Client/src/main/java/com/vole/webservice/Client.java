package com.vole.webservice;

import java.util.List;

import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;

import com.vole.Interceptor.AddHeaderInterceptor;

public class Client {

	public static void main(String[] args) {
		HelloWorldService service = new HelloWorldService();
		HelloWorld helloWorld = service.getHelloWorldPort();
		org.apache.cxf.endpoint.Client client = ClientProxy.getClient(helloWorld);
		
		client.getOutInterceptors().add(new AddHeaderInterceptor("gakki","123456")); // 添加自定义拦截器
		client.getInInterceptors().add(new LoggingInInterceptor()); // 添加In拦截器 日志拦截器
		client.getOutInterceptors().add(new LoggingOutInterceptor()); // 添加Out拦截器 日志拦截器

		
		// System.out.println(helloWorld.say("Gakki"));
		/*
		 * User user = new User(); user.setUserName("gakki");
		 * user.setPassword("123456"); List<Role> roles =
		 * helloWorld.getRoleByUser(user); for (Role role : roles) {
		 * System.out.println(role.getId() + "," + role.getRoleName()); }
		 */

		MyRoleArray array = helloWorld.getRole();
		List<MyRole> roleList = array.item;
		for (int i = 0; i < roleList.size(); i++) {
			MyRole my = roleList.get(i);
			System.out.print(my.key + ": ");
			for (Role r : my.value) {
				System.out.print(r.getId() + "," + r.getRoleName());
			}
			System.out.println("-------");
		}
	}

}
