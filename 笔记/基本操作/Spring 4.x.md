* 什么是无状态或者状态不可变的类？







* 默认情况下，Spring 在 ApplicationContext 容器在启动时，自动化实例所有 singleton 的 Bean 并缓存与容器中，虽然启动时会费些时间，但它有两点好处：
  * 对 Bean 提前进行实例化操作会及时发现一些潜在的配置问题
  * Bean 以缓存的方式保存，当运行时用到该 Bean ，就无须实例化



* aop  增强类型

> * 前置增强
> * 后置增强
> * 环绕增强
> * 异常抛出增强
> * 引介增强（是类级别的）



* 缓存（spring-cache）

```properties
缓存注解
* @Cacheable 指定被注解方法的返回值是可被缓存的
@Cacheable(cacheNames = "cache1")  ----单个缓存
@Cacheable(cacheNames = "cache1,cache2")  ----多个缓存

缓存的 key，可以为空，如果指定，则按 SpEL 表达式编写，如果不指定，则默认按照方法的所有参数进行组合
@Cacheable(cacheNames = "cacheName" key="#userName")

带条件的
@Cacheable(cacheNames = "cache1" condition = "user.age < 35")
年龄小于 35 岁的用户启动缓存
@Cacheable(cacheNames = "cache1" unless = "user.age >= 35")
拒绝对年龄大于或等于 35 岁的用户进行缓存

* @CachePut 
与 @Cacheable 的效果一样，它首先执行方法，然后将返回值放入缓存。

* @CacheEvict
是 @Cacheable 的反向操作，它负责移除缓存
allEntries 是否要清楚缓存中的所有元素，默认 false
beforeInvocation  定义了在调用方法之前还是在调用方法之后完成移除操作，默认调用方法之后 false

* Caching
是一个组注解，可以提供 @Cacheable、@CachePut、@CacheEvict 三张方法

* CacheConfig
类级别的全局缓存注解

```

