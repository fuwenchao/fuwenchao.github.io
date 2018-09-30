java的多态以及重载,重写,前期绑定,后期绑定
================================================


多态的定义: 一个类实例的相同方法在不同情形有不同表现形式。多态机制使具有不同内部结构的对象可以共享相同的外部接口。这意味着，虽然针对不同对象的具体操作不同，但通过一个公共的类，它们（那些操作）可以通过相同的方式予以调用

在java中多态是通过动态绑定实现的. java的多态体现在动态绑定和父类引用指向子类对象实例(这也包括了协变), 而逆变则不属于多态!(这个也是我做题时, 滴滴的一个大牛告诉我的, 强制类型转换仅仅是rtti的一种体现, 而非多态).

多态的作用是: 消除了类型之间的耦合性,  也就是实际不同的类型可以对同一个调用做出相应(比如A,B,C类, B,C类是A的子类, A中定义了一个方法x(), B,C分别对x()进行了重写, 那么实际调用时, B和C都可以对x()的调用做出相应, 如: 存在方法void doX(A a){a.x() ; }

多态存在的三个必要条件

1. 要有继承；
2. 要有重写；
3. 父类引用指向子类对象。


之前学基础的时候只是了解重载, 重写的概念, 但是具体是什么, 涉及到父子类继承就不知所措了, 今天抽了一下午时间看了许多的blog, 顺带复习一下tij的这部分内容, 记一下以便今后使用吧. 

1. 重载和重写的区别
-------------------------

在说明这一点之前要知道的是子类是可以继承父类的静态方法的, 具体子类可以继承父类的哪些方面, 还得再看看tij, 抽空总结吧.

**重载overload**

方法名相同, 参数列表不同的方法之间构成重载; 参数列表不同又包括参数数量, 参数类型, 参数顺序的不同.

重载的判断只有这两条, 与方法的修饰符, 返回值类型都无关, 比如 static void x(){} 与void x(int a){}这也叫重载; 子类继承父类的方法与子类的方法也可以构成重载.如:

.. code:: java

    class A
    {
        public void a() {}
    }

    class B extends A
    {
        public void a(int x){}
    }

    class A
    {
        public static void a() {}
    }

    class B extends A
    {
        public static void a(int x){}
    }

.. code:: java

    class FatherClass
    {
        static void getA()
        {
            System.out.println("facther...");
        }
    }
    class SubClass extends FatherClass
    {
        static void getA()        //对于这个getA()并不能说它覆盖了父类的getA(), 只能说它将父类的getA()隐藏了
        {
            System.out.println("sub ..");
        }
        static void getA(int x)
        {
            System.out.println("child");
        }
    }

重写override

也叫覆盖, 它是指子类重写改写父类中某个方法的实现, 也就是被改写的方法名与参数列表一定是与父类的某个类相同的. 

重写有以下几个原则:

1. 三同一大: 返回值类型, 方法名, 参数列表必须与父类相同(返回值类型在java1.5中允许协变返回类型, 就是重写之后的方法的返回值可以是重写之前方法的子类); 一大是指访问权限大于等于父类的访问权限

2. 子类方法只能抛出比父类方法更小的异常或者不抛出异常

3. 被重写的方法不能有final,private, static修饰符. 因为final不允许被子类继承; 而private方法隐含是final类型, 且只能在类中被访问, 子类是无权访问的. 子类中只能定义一个与父类完全相同的方法, 而不能称之为重写; 至于static方法, 后面会提到

 

绑定
-----------


将一个方法调用与一个方法主体关联起来被称为绑定, 如果这个绑定是发生在程序执行之前(如: 由编译器或链接程序实现的), 那么这种绑定成为前期绑定; 如果这个绑定是发生在程序运行之后, 根据对象的具体类型进行绑定的, 那么这种绑定就是动态绑定.

**后期绑定**

又叫动态绑定或运行时绑定, java的多态就是通过后期绑定实现的, 它是指编译器在编译时不能确定这个引用调用的是哪一个方法, 这个方法具体是什么要到运行时才能确定, 比如:

.. code:: java

    public class Main
    {
        public static void main(String[] args)
        {
            A a = new A() ;
            A b = new B() ;

            a.a() ;
            b.a() ;
        }
    }

    class A
    {
        public void a(){
            System.out.println("a");
        }
    }

    class B extends A{
        public void a()
        {
            System.out.println("b");
        }
    }

a和b的调用的a方法在编译时是无法确定的, 不知道是父类的a还是子类的a, 要到运行时才能通过具体的类型得知.

因此方法的动态绑定是基于对象的实际类型而非对象的引用类型的.

在java中动态绑定发生在除了static方法和final方法之外的所有方法绑定(private方法默认是final类型),构造函数也不是动态绑定(构造函数其实是一种特殊的static方法, 是隐式的), 因此不具有多态性.

**前期绑定**

前期绑定又叫编译器绑定, 是指编译器在编译阶段就完成的绑定.

如果一个方法有static, private,final修饰或者是构造函数, 那么就是前期绑定

所有的静态方法都是前期绑定, 因为静态方法可以通过类名进行访问, 而不会用到引用的对象的实际类型信息, 因此在编译时就可以通过类型信息确定是哪一个具体的方法, 这也就揭示了为什么静态方法不能重写了, 因为重写的目的是为了实现多态.

与静态方法相同, 成员变量也属于前期绑定, 如:

.. code:: java

    public class Main
    {
        public static void main(String[] args)
        {
            A a = new A() ;
            A b = new B() ;

            a.a() ;
            b.a() ;

            System.out.println(a.a);
            System.out.println(b.a);
        }
    }

    class A
    {
        int a = 22 ;
        public void a(){
            System.out.println("a");
        }
    }

    class B extends A{
        int  a = 33 ;
        public void a()
        {
            System.out.println("b");
        }
    }

在java中重载方法的选择是静态绑定, 也就是一个方法的参数选择是静态绑定的, 如:调用了一个重载的方法, 在编译时根据参数列表就可以确定方法, 如果这个方法是非静态方法, 那么具体调用的是父类的方法还是子类的方法就需要动态绑定来确定;



.. code:: java

    public class Main
    {
        public static void main(String[] args)
        {
            Base b = new Base();
            Derived d = new Derived();

            whichFoo(b,b);
            whichFoo(b,d);
            whichFoo(d,b);
            whichFoo(d,d);
        }
        public static void whichFoo(Base arg1, Base arg2){
            arg1.foo(arg2);
        }
    }

    class Base{
        public void foo(Base x){
            System.out.println("Base.Base");
        }

        public void foo(Derived x){
            System.out.println("Base.Derived");
        }
    }



    class Derived extends Base{
        public void foo(Base x){
            System.out.println("Derived.Base");
        }

        public void foo(Derived x){
            System.out.println("Derived.Derived");
        }
    }


对于whichFoo中的foo方法构成了重载(无论是base还是derived), 由于是前期绑定, 所以无论arg2的实际类型是什么, 都会选择public void foo(Base), 具体是父类的foo(base)还是子类的foo(base) 这就需要运行时动态绑定来确定了.

在编译阶段，最佳方法名依赖于参数的静态和控制引用的静态类型所适合的方法。在这一点上，设置方法的名称，这一步叫静态重载。
决定方法是哪一个类的版本，这通过由虚拟机推断出这个对象的运行时类型来完成，一旦知道运行时类型，虚拟机就唤起继承机制，寻找方法的最终版本。这叫做动态绑定。

---

由于对象既可以当作它自己本身的类型使用, 也可以当作它的基类类型使用, 因此将对某个对象的引用是其基类型的引用的行为称为向上转型, 也就是通俗讲为java的向上转型是指子类对象赋值给父类引用; 父类引用调用方法表现出不同的形态, 叫多态. 多态是有动态绑定实现的

前面已经说了对于java当中的方法而言，除了final，static，private和构造方法是前期绑定外，其他的方法全部为动态绑定。而动态绑定的典型发生在父类和子类的转换声明之下：

比如：

    Parent p = new Children();

其具体过程细节如下：

1. 编译器检查对象的声明类型和方法名。假设我们调用x.f(args)方法，并且x已经被声明为C类的对象，那么编译器会列举出C类中所有的名称为f的方法和从C类的超类继承过来的f方法
2. 接下来编译器检查方法调用中提供的参数类型。如果在所有名称为f 的方法中有一个参数类型和调用提供的参数类型最为匹配，那么就调用这个方法，这个过程叫做“重载解析”  
3. 当程序运行并且使用动态绑定调用方法时，虚拟机必须调用同x所指向的对象的实际类型相匹配的方法版本。假设实际类型为D(C的子类)，如果D类定义了f(String)那么该方法被调用，否则就在D的超类中搜寻方法f(String),依次类推

域域静态方法也不具有多态性, 只有普通方法具有多态性, 所有的域访问操作都是由编译器解析的, 因此不具有多态性, 其实类似于静态绑定, 但是绑定这个概念只是针对方法的, 对于域而言不具有这个概念.

能力有限, 不能很好的表达, 原文地址如下:

//http://www.cnblogs.com/yyyyy5101/archive/2011/08/02/2125324.html(这个是写的最好的)

//http://www.importnew.com/7751.html

//http://blog.csdn.net/fsz521/article/details/8739236

多态//http://www.cnblogs.com/jack204/archive/2012/10/29/2745150.html

暂时就记录到这儿吧, 现在拿来实际的例子可以说明白, 但是理论还是讲不来, 记录也写的歪七扭八的.. 整理下思路改天再写吧