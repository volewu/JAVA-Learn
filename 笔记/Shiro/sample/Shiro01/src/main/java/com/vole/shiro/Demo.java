package com.vole.shiro;

public class Demo {

	public static void main(String[] args) {
		Integer a = 1000, b = 1000;
		System.out.println(a.equals(b));
		Integer c = 100, d = 100;
		System.out.println(c == d);
		new Thread(() ->{
			System.err.println("5444");
		}).start();
	}
}
 