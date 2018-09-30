java 核心36讲
===============

模块一：基础
-----------------------

`1. 谈谈你对java体系的理解，“java是解释执行”这句话对吗？`_

`2. 请对比 Exception 和 Error ，运行时异常和一般异常有什么区别`_

`3. 谈谈 finally  final finalize 有什么不同`_

`4. 强引用，软引用，弱引用，幻想引用有什么区别，他们的使用场景是什么`_

`5. 谈谈java的反射机制，动态代理是基于什么原理`_

`6. String 之 理解java的字符串，String，StringBuilder,StringBuffer有什么区别`_

`7. String 之 new String()究竟创建几个对象?`_

`8. 对比Vector，ArrayList ， LindedList有何区别`_

9. 对比 HashTable HashMap HashTree ，谈谈你对HashMap的掌握

10. 如何保证集合是线程安全的，ConcurrentHashMap 做了什么

11. Java　NIO 提供了哪些IO方式，看过NIO的源码吗？如果让你来改进NIO，会做什么改进？

12. 面向对象基础，抽象类，接口的区别是什么？

13. 说说你知道的设计模式，请动手实现单例模式，Spring，Mybaties使用了哪些模式

`14. byte的最大值为什么是127`_

`15. 静态方法与非静态方法的区别`_

JVM
---------


`1. Java内存区域`_

`2. 内存溢出异常`_

`3. 谈谈 JVM 的垃圾回收`_
 
`4. 内存分配与回收策略`_

`5. java 内存泄漏与内存溢出的异同`_

`6. Java中基本数据类型和引用数据类型的存放位置`_

`7. JVM中的常量池和运行时常量池有什么区别`_

`8. 探究 Java 虚拟机栈`_



模块二：进阶
----------------

1. 理解Java锁实现，Synchronized 和 ReentrantLock 有什么区别，有人说 Synchronized 最慢，这话靠谱吗？

2. 一个线程连着调用两次start会出现什么情况，谈谈线程的生命周期和状态转移

3. 什么情况下java会产生死锁，如何排除？

4. Java并发包提供了哪些并发类，使用这些数据结构解决过什么并发问题

5. AtomicInteger 底层实现原理是什么，如何在自己的代码中应用CAS操作

6. java 并发类库提供的线程池有哪几种，如何选择？

7. 什么是类加载过程、双亲委派模型

8. 谈谈JVM内存区域划分，如何监控和诊断JVM堆内和堆外内存使用，OOM常见排错思路有哪些

9. GC 收集器有哪些，常见的调优方法有哪些

10. 谈谈 Java 内存排序模型（JMM） ，原子性，可见性，有序性是什么，现在很多都在谈 HAPPEN-Before
    之类概念，那么如何用程序证明比如Volatile的行为是否正确？

11. 我的Java程序似乎被Docker欺负了，有什么建议？





模块三：java应用开发扩展
-------------------------------

1. 谈谈 MYSQL 支持的隔离级别（读未提交，读已提交，可重复读，可序列化读），以及悲观锁和乐观锁的原理和使用场景

2. 谈谈Redis典型使用场景和实现策略

3. 消息队列的使用场景是什么，谈谈你所用过的消息队列的架构，比如Kafka是如何保证多个partitions之间数据一致性的

4. Spring框架概览，谈谈Spring Bean 的生命周期和作用域，Spring AOP解决什么问题

5. 什么场景下需要用到 **Netty** ，对比 JAVA 的 NIO 库，你知道 Netty 如何实现更高新能吗？

6. 常见的分布式ID的设计方案是什么，SnowFlake 会收冬令时切换影响吗？

7. 处理过分布式事务吗，谈谈常见解决方案

8. 微服务 和 SOA 的区别是什么，请比较构建微服务的常见方案

 


模块四：java安全基础
-----------------------

1. java应用程序中常见的安全攻击有哪些，如何规避哪些臭名昭著的攻击

2. 如何写出安全的java代码，最佳时间与反实践有哪些？


模块五：java性能基础
-----------------------------

1. 后台服务明显变慢，谈谈你的诊断思路

2. 有人说 “Lambda能让Java程序慢30倍，你怎么看？理解JVM优化Java代码是都做了什么，怎样才能实现靠谱的Benchmark？

-----

.. _`1. 谈谈你对java体系的理解，“java是解释执行”这句话对吗？`: b01_java_compiler.html

.. _`2. 请对比 Exception 和 Error ，运行时异常和一般异常有什么区别`: ../exception/exception.html

.. _`3. 谈谈 finally  final finalize 有什么不同`: b03_final.html

.. _`4. 强引用，软引用，弱引用，幻想引用有什么区别，他们的使用场景是什么`: b04_reference.html

.. _`5. 谈谈java的反射机制，动态代理是基于什么原理`: b05_reflection_proxy.html

.. _`6. String 之 理解java的字符串，String，StringBuilder,StringBuffer有什么区别`: b06_String.html

.. _`8. 对比Vector，ArrayList ， LindedList有何区别`: b08_arraylist_linedlist.html

.. _`14. byte的最大值为什么是127`: b14_byte127.html

.. _`15. 静态方法与非静态方法的区别`: b15_staticMethod_nonStaticMethod.html




.. _`1. Java内存区域`: j01_java_memory.html

.. _`2. 内存溢出异常`: j02_oom.html

.. _`3. 谈谈 JVM 的垃圾回收`: j03_gc.html

.. _`4. 内存分配与回收策略`: j04_memory_allocation_recyle_policy.html

.. _`5. java 内存泄漏与内存溢出的异同`: j05_memory_out_leak.html

.. _`6. Java中基本数据类型和引用数据类型的存放位置`: j06_field_location.html

.. _`7. JVM中的常量池和运行时常量池有什么区别`: j07_constant_pool_runtime_constant_pool.html

.. _`8. 探究 Java 虚拟机栈`: j08_java_stack.html