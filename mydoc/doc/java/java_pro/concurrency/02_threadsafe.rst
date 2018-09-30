线程安全性
============

- 原子性

提供了互斥访问，同一时刻只能有一个线程对他进行访问

- 可见性

一个线程对主内存的修改可以及时的被其他线程看到

- 有序性

指令重排序


原子性
-----------------


- Atomic包

AtomicXXX : CAS

compareAndSwap

AtomicLong 和 LongAddr 区别！

LongAddr 高并发下性能更好，但是不稳定


- 锁

synchronized 依赖JVM。

不可中断锁，适合竞争不激烈，可读性好

::

    修饰代码块：大括号括起来的代码，作用于调用对象
    修饰方法：整个方法，作用于调用对象
    修饰静态方法：整个静态方法，作用于所有对象
    修饰类：括号括起来的部分，作用于所有对象

详见代码 me.wenchao.javapro.concurrency.sync.SyncBlockCode2

https://www.cnblogs.com/beiyetengqing/p/6213437.html

.. code:: java

    class Foo implements Runnable {
           private byte[] lock = new byte[0]; // 特殊的instance变量    
           Public void methodA() {      
             synchronized(lock) { //… }
           }
           //…..
    }

注：零长度的byte数组对象创建起来将比任何对象都经济――查看编译后的字节码：生成零长度的byte[]对象只需3条操作码，而Object lock = new Object()则需要7行操作码。

lock 依赖特殊的CPU指令，代码实现 ， ReentrantLock

可中断锁，多样化同步，竞争激烈时能维持常态

Atomic 竞争激烈是能维持常态，比Lock性能好，只能同步一个值


可见性
------------

导致共享变量在线程间不可见的原因

- 线程交叉执行

- 重排序结合线程交叉执行

- 共享变量更新后的值没有在工作内存于主存间即时更新


sychronized

::

    JMM 关于Synchorized的两条规定：
        1：线程解锁前，必须把共享变量的的最新值刷新到主存中
        2： 线程加锁时，将清空工作内存中共享变量的值，从而使用共享变量时需要从主存中重新读取最新值（加锁和解锁是同一把锁）


volatile

通过内存屏障 和 禁止重排序优化 来实现

- 对volatile进行写操作时，会在写操作后加入一条store屏障指令，将本地内存中的共享变量刷新到主内存中
- 对volatile进行读操作时，会在读操作前加入一条load屏障指令，从主内存中读取共享变量

不能保证线程安全，不适合计数场景，但是适合
状态标记量


有序性
---------

java内存模型中，允许编译器和处理器对指令进行重排序，重排序不会影响到单线程的执行，但是会影响到多线程的执行

volatile synchronized lock

保证有序性

**happens-before原则**

- 程序次序规则： 一个线程内，按照代码顺序，书写在前面的操作先行发生于书写在后面的操作（看起来）
- 锁定规则：一个unlock操作先行发生于一个锁的lock操作
- volatile变量规则： 对一个变量的写操作先行发生于后面对这个变量的读操作（先写后读）
- 传递规则： 传递性
- 线程启动原则： Thread对象的start() 方法要先行发生于此线程的每一个动作
- 线程中断原则： 对线程interrup()方法的调用先行发生于被中断线程的代码检测到中断事件的发生
- 线程终结规则： 线程中所有的操作都先行发生于对线程的终止检查，我们可以通过Thread.join() 方法结束，Thread.isAlive() 的返回
                值手段检测到线程已经终止执行
- 对象终结规则： 一个对象的初始化完成先行发生于他的finalize() 方法的开始











