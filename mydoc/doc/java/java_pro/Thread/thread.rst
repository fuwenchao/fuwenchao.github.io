java 多线程
=================

线程间状态转换
---------------------

.. image:: ./images/Thread_status.jpg


1. 新建(new)：新创建了一个线程对象。

2. 可运行(runnable)：线程对象创建后，其他线程(比如main线程）调用了该对象的start()方法。该状态的线程位于可运行线程池中，等待被线程调度选中，获取cpu 的使用权 。

3. 运行(running)：可运行状态(runnable)的线程获得了cpu 时间片（timeslice） ，执行程序代码。

4. 阻塞(block)：阻塞状态是指线程因为某种原因放弃了cpu 使用权，也即让出了cpu timeslice，暂时停止运行。直到线程进入可运行(runnable)状态，才有机会再次获得cpu timeslice 转到运行(running)状态。阻塞的情况分三种： 

    - 等待阻塞：运行(running)的线程执行o.wait()方法，JVM会把该线程放入等待队列(waitting queue)中。
    - 同步阻塞：运行(running)的线程在获取对象的同步锁时，若该同步锁被别的线程占用，则JVM会把该线程放入锁池(lock pool)中。
    - 其他阻塞：运行(running)的线程执行Thread.sleep(long ms)或t.join()方法，或者发出了I/O请求时，JVM会把该线程置为阻塞状态。当sleep()状态超时、join()等待线程终止或者超时、或者I/O处理完毕时，线程重新转入可运行(runnable)状态。

5. 死亡(dead)：线程run()、main() 方法执行结束，或者因异常退出了run()方法，则该线程结束生命周期。死亡的线程不可再次复生。 



上下文切换
--------------

对于单核CPU来说（对于多核CPU，此处就理解为一个核），CPU在一个时刻只能运行一个线程，当在运行一个线程的过程中转去运行另外一个线程，这个叫做线程上下文切换（对于进程也是类似）。

由于可能当前线程的任务并没有执行完毕，所以在切换时需要保存线程的运行状态，以便下次重新切换回来时能够继续切换之前的状态运行。举个简单的例子：比如一个线程A正在读取一个文件的内容，正读到文件的一半，此时需要暂停线程A，转去执行线程B，当再次切换回来执行线程A的时候，我们不希望线程A又从文件的开头来读取。

因此需要记录线程A的运行状态，那么会记录哪些数据呢？因为下次恢复时需要知道在这之前当前线程已经执行到哪条指令了，所以需要记录程序计数器的值，另外比如说线程正在进行某个计算的时候被挂起了，那么下次继续执行的时候需要知道之前挂起时变量的值时多少，因此需要记录CPU寄存器的状态。所以一般来说，线程上下文切换过程中会记录程序计数器、CPU寄存器状态等数据。

说简单点的：对于线程的上下文切换实际上就是 存储和恢复CPU状态的过程，它使得线程执行能够从中断点恢复执行。

虽然多线程可以使得任务执行的效率得到提升，但是由于在线程切换时同样会带来一定的开销代价，并且多个线程会导致系统资源占用的增加，所以在进行多线程编程时要注意这些因素。


Thread实现多线程
----------------------

.. code:: java

    public class Test {
        public static void main(String[] args)  {
            MyThread thread = new MyThread();
            System.out.println(Thread.currentThread().getId())
            thread.start();
        }
    }
    class MyThread extends Thread{
        private static int num = 0;
        public MyThread(){
            num++;
        }
        @Override
        public void run() {
            System.out.println("主动创建的第"+num+"个线程");
        }
    }

Runnable接口实现多线程
--------------------------------

.. code:: java

    public class Test {
        public static void main(String[] args)  {
            System.out.println("主线程ID："+Thread.currentThread().getId());
            MyRunnable runnable = new MyRunnable();
            Thread thread = new Thread(runnable);
            thread.start();
        }
    } 
    class MyRunnable implements Runnable{
        public MyRunnable() {
        }
     
        @Override
        public void run() {
            System.out.println("子线程ID："+Thread.currentThread().getId());
        }
    }

通过实现Runnable接口，我们定义了一个子任务，然后将子任务交由Thread去执行。注意，这种方式必须将Runnable作为Thread类的参数，然后通过Thread的start方法来创建一个新线程来执行该子任务。如果调用Runnable的run方法的话，是不会创建新线程的，这根普通的方法调用没有任何区别。


事实上，查看Thread类的实现源代码会发现Thread类是实现了Runnable接口的。



在Java中，这2种方式都可以用来创建线程去执行子任务，具体选择哪一种方式要看自己的需求。直接继承Thread类的话，可能比实现Runnable接口看起来更加简洁，但是由于Java只允许单继承，所以如果自定义类需要继承其他类，则只能选择实现Runnable接口。



Thread 和 Runnable 这件的区别
----------------------------------

1、实现Runnable的类更具有健壮性，避免了单继承的局限。

2、Runnable更容易实现资源共享，能多个线程同时处理一个资源。

**案例：**

**继承Thread的卖票例子：**

.. code:: java

    public static void main(String[] args) {
            // TODO Auto-generated method stub
                
                new MyThread().start();
                new MyThread().start();
                
        }


    class MyThread extends Thread{  
        private int ticket = 5;  
        public void run(){  
             while(true){
                 System.out.println("Thread ticket = " + ticket--);  
                 if(ticket < 0){  
                    break;
                 }  
             }  
        }  
    }  


**输出结果**

::


    Thread ticket = 5  
    Thread ticket = 5  
    Thread ticket = 4  
    Thread ticket = 4  
    Thread ticket = 3  
    Thread ticket = 2  
    Thread ticket = 3  
    Thread ticket = 1  
    Thread ticket = 2  
    Thread ticket = 0  
    Thread ticket = 1  
    Thread ticket = 0 

从以上输出结果可以看出，我们创造了2个多线程对象，他们分别实现了买票任务，也就是一共卖了12张票。 



**实现Runnable接口的卖票例子：**

.. code:: java

    public static void main(String[] args) {
            // TODO Auto-generated method stub
            MyThread2 m=new MyThread2();
                new Thread(m).start();
                new Thread(m).start();
            
                
        }
    class MyThread2 implements Runnable{  
        private int ticket = 5;  
        public void run(){  
             while(true){
                 System.out.println("Runnable ticket = " + ticket--);  
                 if(ticket < 0){  
                    break;
                 }  
             } 
        }  
    }  


**输出结果：**

::

    Runnable ticket = 5  
    Runnable ticket = 4  
    Runnable ticket = 3  
    Runnable ticket = 2  
    Runnable ticket = 1  
    Runnable ticket = 0 

从结果我们可以看到，虽然我们声明了两个线程，但是一共卖了6张票。他们实现了资源共享。PS：在实际开发中，一定要注意命名规范，其次上面实现Runable接口的例子由于同时操作一个资源，会出现线程不安全的情况，如果情况需要，我们需要进行同步操作。 

**另一种写法**

.. code:: java


    new Thread(new Runnable() {

                      public void run() {
                           // try {
                           // Thread.sleep(10);
                           // } catch (InterruptedException e) {
                           // e.printStackTrace();
                           // }
                           for (int i = 0; i < 5; i++) {
                              ot.main();
                          }
                     }
                 }).start();


Callable
---------------

future模式：并发模式的一种，可以有两种形式，即无阻塞和阻塞，分别是isDone和get。其中Future对象用来存放该线程的返回值以及状态

.. code:: java

    ExecutorService e = Executors.newFixedThreadPool(3); //submit方法有多重参数版本，及支持callable也能够支持runnable接口类型. Future future = e.submit(new myCallable()); 
    future.isDone() //return true,false 无阻塞 
    future.get() // return 返回值，阻塞直到该线程运行结束


作者：纳达丶无忌
链接：https://www.jianshu.com/p/40d4c7aebd66
來源：简书
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。



yield() 静态方法
--------------------

调用yield方法会让当前线程交出CPU权限，让CPU去执行其他的线程。它跟sleep方法类似，同样不会释放锁。但是yield不能控制具体的交出CPU的时间，另外，yield方法只能让拥有相同优先级的线程有获取CPU执行时间的机会。

注意，调用yield方法并不会让线程进入阻塞状态，而是让线程重回就绪状态，它只需要等待重新获取CPU执行时间，这一点是和sleep方法不一样的。


.. code:: java

    public class MyThread  extends Thread{
        @Override
        public void run() {
            long beginTime=System.currentTimeMillis();
            int count=0;
            for (int i=0;i<50000000;i++){
                count=count+(i+1);
                //Thread.yield();
            }
            long endTime=System.currentTimeMillis();
            System.out.println("用时："+(endTime-beginTime)+" 毫秒！");
        }
    }
     
    public class Run {
        public static void main(String[] args) {
            MyThread t= new MyThread();
            t.start();
        }
    }

执行结果：

    用时：3 毫秒！

如果将 //Thread.yield();的注释去掉，执行结果如下：

    用时：16080 毫秒！


join()方法
----------------------

在很多情况下，主线程创建并启动了线程，如果子线程中药进行大量耗时运算，主线程往往将早于子线程结束之前结束。这时，如果主线程想等待子线程执行完成之后再结束，比如子线程处理一个数据，主线程要取得这个数据中的值，就要用到join()方法了。方法join()的作用是等待线程对象销毁。

.. code:: java

    public class Thread4 extends Thread{
        public Thread4(String name) {
            super(name);
        }
        public void run() {
            for (int i = 0; i < 5; i++) {
                System.out.println(getName() + "  " + i);
            }
        }
        public static void main(String[] args) throws InterruptedException {
            // 启动子进程
            new Thread4("new thread").start();
            for (int i = 0; i < 10; i++) {
                if (i == 5) {
                    Thread4 th = new Thread4("joined thread");
                    th.start();
                    th.join();
                }
                System.out.println(Thread.currentThread().getName() + "  " + i);
            }
        }
    }



线程间通信
-------------------

`如何在 Java 中正确使用 wait, notify 和 notifyAll – 以生产者消费者模型为例`_



.. _如何在 Java 中正确使用 wait, notify 和 notifyAll – 以生产者消费者模型为例: notify.html


其他高级类
-----------------

TODO
