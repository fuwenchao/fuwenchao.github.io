对象发布
==========


发布对象： 是一个对象能够被当前范围之外的代码所使用
 
对象逸出： 一种错误的发布，当一个对象还没有构造完成时，就是他被其他线程看见


安全发布对象
---------------


- 在静态初始化函数中初始化一个对象的引用
- 将对象的引用保存到volatile类型域或者AtomicReference对象中
- 将对象的引用保存到某个正确构造对象的final类型域中
- 将对象的引用保存到一个由锁保护的域中



**单例模式**


**不可变对象**

不可变对象需要满足的条件

1. 对象创建以后其状态就不能被更改
2. 对象的所有域都是final类型的
3. 对象是正确创建的（在对象创建期间，this引用没有逸出）

final关键字（修饰类，修饰方法，修饰变量）

Collections.unmodifiableXXX : Collection List Set Map

Guava ImmutableXXX : Collection Set Map List

**线程封闭**

将对象封装到一个线程中

- Ad-hoc 线程封闭：程序控制实现，最糟糕，忽略
- 堆栈封闭： 局部变量，无并发问题
- ThreadLocal 线程封闭： 特别好的封闭方法

filter intercepter 


**线程不安全的类与写法**

StringBuilder -> StringBuffer

SimpleDateFormat -> JodaTime

ArrayList HashSet HashMap 等 Collection

先检查后执行 if(condition){dosomething}

