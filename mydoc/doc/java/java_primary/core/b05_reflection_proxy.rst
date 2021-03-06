谈谈java的反射机制，动态代理是基于什么原理
===============================================


反射
------

什么是java反射机制？我们又为什么要学它？
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

当程序运行时，允许改变程序结构或变量类型，这种语言称为动态语言。我们认为java并不是动态语言，
但是它却有一个非常突出的动态相关机制，俗称：反射。

IT行业里这么说，没有反射也就没有框架，现有的框架都是以反射为基础。
在实际项目开发中，用的最多的是框架，填的最多的是类，
反射这一概念就是将框架和类揉在一起的调和剂。所以，反射才是接触项目开发的敲门砖！


一、Class类
^^^^^^^^^^^^^

什么是Class类？

在面向对象的世界里，万事万物皆是对象。而在java语言中，static修饰的东西不是对象，但是它属于类。普通的数据类型不是对象，例如：int a = 5;它不是面向对象，但是它有其包装类 Integer 或者分装类来弥补了它。除了以上两种不是面向对象，其余的包括类也有它的面向对象，类是java.lang.Class的实例化对象（注意Class是大写）。也就是说：

    Class A{}

当我创建了A类，那么类A本身就是一个对象，谁的对象？java.lang.Class的实例对象。

那么这个对象又该怎么表示呢？

我们先看一下下面这段代码：

.. code:: java

    public class Demo(){
        F f=new F();
    }
    class F{}



这里的F的实例化对象就可以用f表达出来。同理F类也是一个实例化对象，Class类的实例化对象。我们可以理解为任何一个类都是Class类的实例化对象，这种实例化对象有三种表示方法：

.. code:: java

    public class Demo(){
    F f=new F();
    //第一种表达方式
    Class c1=F.class;//这种表达方式同时也告诉了我们任何一个类都有一个隐含的静态成员变量class
    //第二种表达方式
    Class c2=f.getClass();//这种表达方式在已知了该类的对象的情况下通过getClass方法获取
    //第三种表达方式
    Class c3 = null;
    try {
    c3 = Class.forName("com.text.F");//类的全称
    } catch (ClassNotFoundException e) {
    e.printStackTrace();
    }
    }
    class F{}



以上三种表达方式，c1,c2,c3都表示了F类的类类型，也就是官方解释的Class Type。
那么问题来了：


    System.out.println(c1 == c2)?  or  System.out.println(c1 == c3)?

答案是肯定的，返回值为ture。

这表明不论c1 or c2 or c3都代表了F类的类类型，也就是说一个类只可能是Class类的一个实例对象。
理解了Class的概念，我们也可以通过类的类类型创建该类的对象实例，用c1 or c2 or c3的newInstance()方法：


.. code:: java


    Public class Demo1{
    try {
            Foo foo = (Foo)c1.newInstance();//foo就表示F类的实例化对象
            foo.print();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }}
    class F{
        void print(){
        }
    }


这里需要注意的是，c1是F类的类类型，创建出来的就是F类的对象。如果a是A类的类类型，那么创建出来的对象也应该与之对应，属于A类的对象。

二、方法的反射
^^^^^^^^^^^^^^^^^^^^^^^^^


Class类有一个最简单的方法，getName()：

.. code:: java


    public class Demo2 {
        public static void main(String[] args) {
            Class c1 = int.class;//int 的类类型
            Class c2 = String.class;//String类的类类型
            Class c3 = void.class;
            System.out.println(c1.getName());
            System.out.println(c2.getName());
            System.out.println(c2.getSimpleName());
            System.out.println(c3.getName());
        }
    }


本的数据类型以及void关键字都是存在类类型的。

案例：

.. code:: java


    public class ClassUtil {
        public static void printClassMethodMessage(Object obj){
    //要获取类的信息》》首先我们要获取类的类类型
            Class c = obj.getClass();
    //我们知道Object类是一切类的父类，所以我们传递的是哪个子类的对象，c就是该子类的类类型。
    //接下来我们要获取类的名称
            System.out.println("类的名称是:"+c.getName());
            /*
             *我们知道，万事万物都是对象，方法也是对象，是谁的对象呢？
             * 在java里面，方法是Method类的对象
             *一个成员方法就是一个Method的对象，那么Method就封装了对这个成员
             *方法的操作
             */
    //如果我们要获得所有的方法，可以用getMethods()方法，这个方法获取的是所有的Public的函数，包括父类继承而来的。如果我们要获取所有该类自己声明的方法，就可以用getDeclaredMethods()方法，这个方法是不问访问权限的。
            Method[] ms = c.getMethods();//c.getDeclaredMethods()
    //接下来我们拿到这些方法之后干什么？我们就可以获取这些方法的信息，比如方法的名字。
    //首先我们要循环遍历这些方法
            for(int i = 0; i < ms.length;i++){
    //然后可以得到方法的返回值类型的类类型
                Class returnType = ms[i].getReturnType();
    //得到方法的返回值类型的名字
                System.out.print(returnType.getName()+" ");
    //得到方法的名称
                System.out.print(ms[i].getName()+"(");
    //获取参数类型--->得到的是参数列表的类型的类类型
                Class[] paramTypes = ms[i].getParameterTypes();
                for (Class class1 : paramTypes) {
                    System.out.print(class1.getName()+",");
                }
                System.out.println(")");
            }
        }
    }


总结思路：
通过方法的反射得到该类的名称步骤：

1. 获取该类的类类型
2. 通过类类型获取类的方法（getMethods()）
3. 循环遍历所获取到的方法
4. 通过这些方法的getReturnType()得到返回值类型的类类型，又通过该类类型得到返回值类型的名字
5. getName()得到方法的名称，getParameterTypes()获取这个方法里面的参数类型的类类型。

三、成员变量的反射
^^^^^^^^^^^^^^^^^^^^^^

首先我们需要认识到成员变量也是对象，是java.lang.reflect.Field类的对象，那么也就是说Field类封装了关于成员变量的操作。既然它封装了成员变量，我们又该如何获取这些成员变量呢？它有这么一个方法：


.. code:: java


    public class ClassUtil {
        public static void printFieldMessage(Object obj){
            Class c = obj.getClass();
    //Field[] fs = c.getFields();
        }

这里的getFields()方法获取的所有的public的成员变量的信息。和方法的反射那里public的成员变量，也有一个获取所有自己声明的成员变量的信息：

    Field[] fs = c.getDeclaredFields();

我们得到它之后，可以进行遍历（既然封装了Field的信息，那么我们就可以得到Field类型）

.. code:: java

        for (Field field : fs) {
            //得到成员变量的类型的类类型
            Class fieldType = field.getType();
            String typeName = fieldType.getName();
            //得到成员变量的名称
            String fieldName = field.getName();
            System.out.println(typeName+" "+fieldName);
        }

四、构造函数的反射
^^^^^^^^^^^^^^^^^^^^^^^^


不论是方法的反射、成员变量的反射、构造函数的反射，我们只需要知道：要想获取类的信息，首先得获取类的类类型。


.. code:: java


        public static void printConMessage(Object obj){
            Class c = obj.getClass();
            /*
             * 首先构造函数也是对象，是java.lang.Constructor类的对象
             * 也就是java.lang. Constructor中封装了构造函数的信息
             * 和前面说到的一样，它也有两个方法：
             * getConstructors()方法获取所有的public的构造函数
             * getDeclaredConstructors()方法得到所有的自己声明的构造函数
             */
            //Constructor[] cs = c.getConstructors();
            Constructor[] cs = c.getDeclaredConstructors();
            for (Constructor constructor : cs) {
                //我们知道构造方法是没有返回值类型的，但是我们可以：
                System.out.print(constructor.getName()+"(");
                //获取构造函数的参数列表》》得到的是参数列表的类类型
                Class[] paramTypes = constructor.getParameterTypes();
                for (Class class1 : paramTypes) {
                    System.out.print(class1.getName()+",");
                }
                System.out.println(")");
            }
        }



五、Class类的动态加载类
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

如何动态加载一个类呢？



首先我们需要区分什么是动态加载？什么是静态加载？我们普遍认为编译时刻加载的类是静态加载类，运行时刻加载的类是动态加载类。我们举一个例子：

.. code:: java


    class A{
        public static void main(String[] args){
            if("B".equal(args[0])){
                B b=new B();
                b.start();
            }
            if("C".equal(args[0])){
                C c=new C();
                C.start();
            }
        }
    }


上面这一段代码，当我们在用eclipse或者myeclipse的时候我们并不关心是否能够通过编译，当我们直接在cmd使用javac访问A.java类的时候，就会抛出问题:

::

    A.java：7：错误：找不到符号
    B b=new B();
    符号:  类B
    位置： 类A
    A.java：7：错误：找不到符号
    B b=new B();
    符号:  类B
    位置： 类A
    A.java：12：错误：找不到符号
    C c=new C();
    符号:  类C
    位置： 类A
    A.java：12：错误：找不到符号
    C c=new C();
    符号:  类C
    位置： 类A
    4个错误


或许我们理所当然的认为这样应该是错，类B根本就不存在。但是如果我们多思考一下，就会发现B一定用吗？不一定。C一定用吗？也不一定。那么好，现在我们就让B类存在

.. code:: java


    class B{
        Public static void start(){
            System.out.print("B...satrt");
        }
    }

现在我们就先 javac B.class,让B类先开始编译。然后在运行javac A.class。结果是：

::

    A.java：12：错误：找不到符号
    C c=new C();
    符号:  类C
    位置： 类A
    A.java：12：错误：找不到符号
    C c=new C();
    符号:  类C
    位置： 类A
    2个错误


我们再想，这个程序有什么问题。如果你说没有什么问题？

C类本来就不存在啊！那么问题来了B类已经存在了，假设我现在就想用B，我们这个程序用得了吗？

答案是肯定的，用不了。

那用不了的原因是什么？因为我们这个程序是做的类的静态加载，也就是说new创建对象是静态加载类，在编译时刻就需要加载所有的，可能使用到的类。所以不管你用不用这个类。

现在B类是存在的，但是我们这个程序仍然用不了，因为会一直报C类有问题，所以B类我也用不了。那么在实际应用当中，我们肯定需要如果B类存在，B类我就能用，当用C类的时候，你再告诉我错了。如果说将来你有100个类，只要其中一个类出现问题，其它99个类你都用不了。所以这并不是我们想要的。

我们想要的就是我用那个类就加载那个类，也就是常说的运行时刻加载，动态加载类。如何实现动态加载类呢？我们可以建这么一个类：


.. code:: java


    class All{
        Public static void start(){
            try{
                Class cl= Class.forName(args[0]);
    //通过类类型，创建该类的对象
                cl.newInstance();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }



前面我们在分析Class实例化对象的方式的时候，Class.forName(“类的全称”)，它不仅仅表示了类的类类型，还表示了动态加载类。当我们javac All.java的时候，它不会报任何错误，也就是说在编译的时候是没有错误的。
只有当我们具体用某个类的时候，那个类不存在，它才会报错。
如果加载的类是B类，就需要：


    B bt = (B) cl.newInstance();

万一加载的是C类呢，可以改成

    C ct = (C) cl.newInstance();

但是如果我想用很多的类或者加载很多的类，该怎么办？我们可以统一一个标准，不论C类还是B类或者其他的类，比如定义一个标准

    Stand s = (Stand) cl.newInstance();

只要B类和C类都是这个标准的就行了。

.. code:: java


    class All{
        Public static void start(){
            try{
                Class cl= Class.forName(args[0]);
    //通过类类型，创建该类的对象
                Stand s = (Stand) cl.newInstance();
                s.start();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    interface Stand {
        Public void start();
    }



现在如果我想要用B类，我们只需要：


.. code:: java


    class B implements Stand{
        Public void start(){
            System.out.print("B...satrt");
        }
    }


加载B类,编译运行。

如果以后想用某一个类，不需要重新编译，只需要实现这个标准的接口即可。只需要动态的加载新的东西就行了。

这就是动态加载类。

动态代理
---------------


代理分为 静态代理 和 动态代理

关于两者介绍，查看 `设计模式之代理`_

有段代码详见 `my github`_


这里再讲见动态代理的实现原理

自己看源码，Proxy.newProxyInstance

关注缓存的设计，引用设计





.. _`设计模式之代理`: ../../../designpattern/proxy.html

.. _`my github`: https://github.com/fuwenchao/javalearning/tree/master/src/main/java/me/wenchao/designschema/proxy