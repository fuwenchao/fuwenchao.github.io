Java 内存溢出异常 OOM
=============================

除程序计数器外，其他内存区域都有可能OOM

异常代码演示
-------------------


参数设置
    
    -Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError

将堆的最小值—Xms参数与最大值-Xmx参数设为相等可避免自动扩展

**-XX:+HeapDumpOnOutOfMemoryError** 可以让虚拟机在出现内存异常是Dump出当前的内存堆转储快照以便分析

.. code:: java

    public class HeapOOM {
        static class OOMObject{

        }
        public static void main(String[] args) {
            List<OOMObject> oom = new ArrayList<OOMObject>();
            int count = 0 ;
            while(true){
                System.out.println(count++);
                oom.add(new OOMObject());
            }

        }
    }



返回值

.. code:: java

    java.lang.OutOfMemoryError: GC overhead limit exceeded
    Dumping heap to java_pid8696.hprof ...
    Heap dump file created [28080282 bytes in 0.142 secs]
    Exception in thread "main" java.lang.OutOfMemoryError: GC overhead limit exceeded
        at java.nio.CharBuffer.wrap(CharBuffer.java:373)
        at sun.nio.cs.StreamEncoder.implWrite(StreamEncoder.java:265)
        at sun.nio.cs.StreamEncoder.write(StreamEncoder.java:125)
        at java.io.OutputStreamWriter.write(OutputStreamWriter.java:207)
        at java.io.BufferedWriter.flushBuffer(BufferedWriter.java:129)
        at java.io.PrintStream.write(PrintStream.java:526)
        at java.io.PrintStream.print(PrintStream.java:597)
        at java.io.PrintStream.println(PrintStream.java:736)
        at me.wenchao.javabasic.oom.HeapOOM.main(HeapOOM.java:19)


当前项目路径找到 java_pid8696.hprof，使用 Jprofile 打开



虚拟机栈和本地方法栈溢出
-------------------------------


    -Xss 参数减少栈内存容量

.. code:: java

    /**
     * @Author wenchaofu
     * @DATE 16:12 2018/5/17
     * @DESC
     * -Xss128K 限制栈容量
     */
    public class StackOOM {

        private int statcLength = 1;
        public void stackLeak(){
            statcLength++;
            stackLeak();
        }

        public static void main(String[] args)  {
            StackOOM soom = new StackOOM();
            try {
                soom.stackLeak();
            }catch (Throwable e){
                System.out.println("stack Length is " + soom.statcLength);
                throw e;
            }
        }

    }

返回结果如下：

::

    stack Length is 993
    Exception in thread "main" java.lang.StackOverflowError
        at me.wenchao.javabasic.oom.StackOOM.stackLeak(StackOOM.java:13)
        at me.wenchao.javabasic.oom.StackOOM.stackLeak(StackOOM.java:14)
        ......

结果表明，在单个线程下无论是栈帧太大还是虚拟机栈容量太小，当内存无法分配的时候，虚拟机抛出的都是StackOverflowError异常

如果是多线程，通过不断建立线程方式倒是可以产生内存溢出异常，如：


.. code:: java

    package me.wenchao.javabasic.oom;

    /**
     * @Author wenchaofu
     * @DATE 13:22 2018/5/18
     * @DESC -Xss2M (虚拟机栈设置大一些)
     */
    public class StackOOM2 {
        private void noStop() {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void stackLeakByThread() {
            while (true) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        noStop();
                    }
                });
                thread.start();
            }
        }

        public static void main(String[] args) {
            StackOOM2 stackOOM2 = new StackOOM2();
            stackOOM2.stackLeakByThread();
        }
    }

返回结果如下

::

    Exception in thread "main" java.lang.OutOfMemoryError: unable to create new native thread
        at java.lang.Thread.start0(Native Method)
        at java.lang.Thread.start(Thread.java:717)
        at me.wenchao.javabasic.oom.StackOOM2.stackLeakByThread(StackOOM2.java:27)
        at me.wenchao.javabasic.oom.StackOOM2.main(StackOOM2.java:33)

-----

提示：上述代码可能会导致操作系统假死

----


但是这样产生的 OOM 与 栈空间是否足够大并不存在直接联系。或者准确地说，在这种情况下，为每个线程的栈分配
的内存越大，反而越容易产生内存溢出异常

其实原因不难理解，操作系统为每个进程分配的内存是有限的。譬如 32 位的windows限制为 2G，虚拟机提供了参数来控制Java堆和
方法区的这两部分内存的最大值，剩余的内存为2G（操作系统限制）减去Xmx（最大堆容量），再减去MaxPermSize（最大栈方法区，java8无此参数），程序计数器消耗内存较小，可以忽略。如果虚拟机本身消耗的内存不计算在内，剩余的内存就有虚拟机栈和本地方法栈“瓜分”了（线程私有）。每个线程分配到的栈容量越大可以建立的线程数量越小，建立线程时就越容易把剩下的内存耗尽。

**这一点在开发多线程时需要特别注意** ，出现StackOverflowError异常时有错误的堆栈可以阅读，相对来说，比较容易找到问题所在。而且，如果使用虚拟机默认的参数，栈深度在大多数情况下（因为每个方法压入栈的栈帧大小并不一样，所以只能说大多数情况下）达到1000 ~ 2000 完全没有任何问题，对于正常的方法调用（包括递归），这个深度应该完全够用了。但是，如果是通过建立多线程导致的内存溢出，再不能减少线程数或者更换64位虚拟机的情况下，就只能通过 **减少最大堆** 和 **减少栈容量** 来换取更多的线程。如果没有这方面的处理经验，这种通过**“减少内存”**的手段来解决内存溢出的方式比较难以想到。

    注解：减少栈容量很容易理解，减少 **最大堆** 是因为可以让其他内存使用到更多的内存，如方法区

方法区和运行时常量池溢出
-----------------------------


JDK 1.7开始逐渐 “去永久代”

String.intern() 是一个 Native 方法，它的作用是：如果字符串常量池中已经包含一个等于一个此String对象的字符串，则返回池中这个字符串的String对象；否则将此String对象包含的字符串添加到常量池中，并且返回此String对象的引用。在JDK1.6 及之前的版本中，由于字符串常量池分配在永久代中，我们可以通过 -XX:PermSize 和 -XX:MaxPermSize 限制方法区大小，从而间接限制其中的常量池大小

.. code:: java

    package me.wenchao.javabasic.oom;

    import java.util.ArrayList;
    import java.util.List;

    /**
     * @Author wenchaofu
     * @DATE 14:59 2018/5/18
     * @DESC
     * -XX:PermSize=5M -XX:MaxPermSize=5M
     */
    public class PermOOM {
        public static void main(String[] args) {
            List<String> list = new ArrayList<>();
            int i = 0 ;
            while (true) {

                list.add(String.valueOf(i++).intern());
                System.out.println(i);
            }


        }
    }

jdk1.8 不报错，在1.7之前的版本中
报错

    OutOfMemroyError: PermGen space


在jdk8中，移除了方法区，转而用Metaspace区域替代，所以我们需要使用新的jvm参数：-XX:MaxMetaspaceSize=2M，依然运行如上代码，抛出：java.lang.OutOfMemoryError: Metaspace异常。同理说明运行时常量池是划分在Metaspace区域中。具体关于Metaspace区域的知识，请读者自行搜索。

本机直接内存溢出
-----------------------

略