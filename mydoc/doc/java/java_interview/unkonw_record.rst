关于二叉树相关（满二叉树，完全二叉树等）

堆 数据结构是什么二叉树  （完全二叉树，平衡二叉树）

排序算法有哪些（冒泡，希尔，插入，快速，归并，选择，堆排序 等）

方法名可以和类名相同

java程序可以有多个main方法

java程序中类名必须和文件名相同（这句话是错误的）


Thread suspend() 停止运行线程，resume() 恢复线程

HashMap HashTable
HashMap 采用快速失败机制， HashTable 不是；HashMap非同步，HashTable同步；

java 子类能继承除构造方法外所有的成员变量和方法，包括private

写一个观察者模式


当常量池中没有该字符串时，JDK7的intern（）方法的实现不再是在常量池中创建与此String内容相同的字符串，而改为在常量池中记录Java Heap中首次出现的该字符串的引用，并返回该引用。


::

    String b = "计算机";
    String a = b + "软件";
    System.out.println(a.intern() == a);
    //JDK1.6:false
    //JDK1.7:true


TreeSet TreeMap 与其他的区别 set HashMap

精度小于int的数值运算的时候都回被自动转换为int后进行计算。 short + short = int byte + byte = int

所以这种写法是错误的

::

    short a = 1;
    short b = 1;
    a = a + b;  // 编译错误，需要int型
    a = (short)(a+b)  // 显式转换
    a += b     // 隐式转换



