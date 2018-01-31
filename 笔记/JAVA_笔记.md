###### 简单问题

* this：强调对象本身，就是当前对象

* super：调用父类的属性与方法，主要用于调用父类的构造方法

* static：

  1. 静态变量-----> 用于声明变量时，该变量是公共的,可以用类名直接访问
  2. 静态方法------> 可以用类名直接访问，在方法中只能调用 static 声明的属性，反之会报错



* final：

  1. final 声明的类不能被继承
  2. final 声明的方法不能被覆盖
  3. final 声明的变量，为常量，一般配合 static 一起使用

* 抽象类：

  1. 包含一个抽象方法的类，类与方法都用 abstract 声明
  2. 不能被实例化，子类必须重写父类的抽象方法

* 接口：

  1. 接口中的属性必须是静态常量并要赋值，书写时可以省略 final static
  2. 接口中的方法必须是抽象的，用 abstract 声明 ，书写时可以省略 abstract

* 多态：

  1. 用于方法的重载与重写
  2. 可以用父类的引用指向子类的具体实现，且可以随时更换为其他子类的实现
  3. 向上转型 ：子类对象---->父类对象    安全
  4. 向下转型 ：父类对象---->子类对象   不安全

* 异常
  1. 运行时异常，编译时不检查，可以不使用 try...catch
  2. Exception异常，编译好似会检查，必须使用 try...catch

* 泛型：
  1. 限制泛型类型  ` class Demo<T extends 类型>`
  2. 通配符泛型 `public static void take(Demo<?> demo){}`
  3. 泛型方法  `public static T void run(T t){}`

* 集合：

  1. ArrayList 、LinkedList 有序集合

  2. Hashset 无序集合     

  3. HashMap 键值对集合

     		LinkedList<Student> aList = new LinkedList<>();
     		aList.add(new Student("vole",22));
     		aList.add(new Student("kong",99));
     		Iterator<Student> i =  aList.iterator();//遍历
     		while(i.hasNext()){
     			Student s = i.next();
     			System.out.println(s);
     		} 
     		
     		HashMap<String, Student> hashMap = new HashMap<>();
     			hashMap.put("1", new Student("vole",25));
     			hashMap.put("2", new Student("kong",99));
     			hashMap.put("3", new Student("wuji",3));
     			
     			Iterator<String> it=hashMap.keySet().iterator();
     			while(it.hasNext()){
     				System.out.println(hashMap.get(it.next()));
     			}

* ​