java 面试一
===============

**1、面向对象的特征有哪些方面？**

答：面向对象的特征主要有以下几个方面：

- 抽象：抽象是将一类对象的共同特征总结出来构造类的过程，包括数据抽象和行为抽象两方面。抽象只关注对象有哪些属性和行为，并不关注这些行为的细节是什么。

- 继承：继承是从已有类得到继承信息创建新类的过程。提供继承信息的类被称为父类（超类、基类）；得到继承信息的类被称为子类（派生类）。继承让变化中的软件系统有了一定的延续性，同时继承也是封装程序中可变因素的重要手段（如果不能理解请阅读阎宏博士的《Java与模式》或《设计模式精解》中关于桥梁模式的部分）。

- 封装：通常认为封装是把数据和操作数据的方法绑定起来，对数据的访问只能通过已定义的接口。面向对象的本质就是将现实世界描绘成一系列完全自治、封闭的对象。我们在类中编写的方法就是对实现细节的一种封装；我们编写一个类就是对数据和数据操作的封装。可以说，封装就是隐藏一切可隐藏的东西，只向外界提供最简单的编程接口（可以想想普通洗衣机和全自动洗衣机的差别，明显全自动洗衣机封装更好因此操作起来更简单；我们现在使用的智能手机也是封装得足够好的，因为几个按键就搞定了所有的事情）。

- 多态性：多态性是指允许不同子类型的对象对同一消息作出不同的响应。简单的说就是用同样的对象引用调用同样的方法但是做了不同的事情。多态性分为编译时的多态性和运行时的多态性。如果将对象的方法视为对象向外界提供的服务，那么运行时的多态性可以解释为：当A系统访问B系统提供的服务时，B系统有多种提供服务的方式，但一切对A系统来说都是透明的（就像电动剃须刀是A系统，它的供电系统是B系统，B系统可以使用电池供电或者用交流电，甚至还有可能是太阳能，A系统只会通过B类对象调用供电的方法，但并不知道供电系统的底层实现是什么，究竟通过何种方式获得了动力）。方法重载（overload）实现的是编译时的多态性（也称为前绑定），而方法重写（override）实现的是运行时的多态性（也称为后绑定）。

运行时的多态是面向对象最精髓的东西，要实现多态需要做两件事：

1. 方法重写（子类继承父类并重写父类中已有的或抽象的方法）；
2. 对象造型（用父类型引用引用子类型对象，这样同样的引用调用同样的方法就会根据子类对象的不同而表现出不同的行为）。

参考 `绑定`_


..  _`绑定`: 1_override_overload.html


**2、访问修饰符public,private,protected,以及不写（默认）时的区别？**

.. image:: ./images/access.png

**3、String 是最基本的数据类型吗？**

答：不是。Java中的基本数据类型只有8个：byte、short、int、long、float、double、char、boolean；除了基本类型（primitive type），剩下的都是引用类型（reference type），Java 5以后引入的枚举类型也算是一种比较特殊的引用类型。


**4、float f=3.4;是否正确？*

答:不正确。3.4是双精度数，将双精度型（double）赋值给浮点型（float）属于下转型（down-casting，也称为窄化）会造成精度损失，因此需要强制类型转换float f =(float)3.4; 或者写成float f =3.4f; 小数常量都是double，需要转换。

**5、short s1 = 1; s1 = s1 + 1;有错吗?short s1 = 1; s1 += 1;有错吗？**

答：对于short s1 = 1; s1 = s1 + 1;由于1是int类型，因此s1+1运算结果也是int 型，需要强制转换类型才能赋值给short型。而short s1 = 1; s1 += 1;可以正确编译，因为s1+= 1;相当于s1 = (short)(s1 + 1);其中有隐含的强制类型转换。


**6、Java有没有goto？**

答：goto 是Java中的保留字，在目前版本的Java中没有使用。（根据James Gosling（Java之父）编写的《The Java Programming Language》一书的附录中给出了一个Java关键字列表，其中有goto和const，但是这两个是目前无法使用的关键字，因此有些地方将其称之为保留字，其实保留字这个词应该有更广泛的意义，因为熟悉C语言的程序员都知道，在系统类库中使用过的有特殊意义的单词或单词的组合都被视为保留字）



**7、int和Integer有什么区别？**

答：Java是一个近乎纯洁的面向对象编程语言，但是为了编程的方便还是引入了基本数据类型，但是为了能够将这些基本数据类型当成对象操作，Java为每一个基本数据类型都引入了对应的包装类型（wrapper class），int的包装类就是Integer，从Java 5开始引入了自动装箱/拆箱机制，使得二者可以相互转换。

Java 为每个原始类型提供了包装类型：

- 原始类型: boolean，char，byte，short，int，long，float，double
- 包装类型：Boolean，Character，Byte，Short，Integer，Long，Float，Double

.. code:: java

    class AutoUnboxingTest {

        public static void main(String[] args) {
            Integer a = new Integer(3);
            Integer b = 3;                  // 将3自动装箱成Integer类型
            int c = 3;
            System.out.println(a == b);     // false 两个引用没有引用同一对象
            System.out.println(a == c);     // true a自动拆箱成int类型再和c比较
        }
    }

**8、&和&&的区别？**

答：&运算符有两种用法：(1)按位与；(2)逻辑与。&&运算符是短路与运算。逻辑与跟短路与的差别是非常巨大的，虽然二者都要求运算符左右两端的布尔值都是true整个表达式的值才是true。&&之所以称为短路运算是因为，如果&&左边的表达式的值是false，右边的表达式会被直接短路掉，不会进行运算。很多时候我们可能都需要用&&而不是&，例如在验证用户登录时判定用户名不是null而且不是空字符串，应当写为：username != null &&!username.equals("")，二者的顺序不能交换，更不能用&运算符，因为第一个条件如果不成立，根本不能进行字符串的equals比较，否则会产生NullPointerException异常。注意：逻辑或运算符（|）和短路或运算符（||）的差别也是如此。

**9、解释内存中的栈(stack)、堆(heap)和方法区(method area)的用法。**

答：通常我们定义一个基本数据类型的变量，一个对象的引用，还有就是函数调用的现场保存都使用JVM中的栈空间；而通过new关键字和构造器创建的对象则放在堆空间，堆是垃圾收集器管理的主要区域，由于现在的垃圾收集器都采用分代收集算法，所以堆空间还可以细分为新生代和老生代，再具体一点可以分为Eden、Survivor（又可分为From Survivor和To Survivor）、Tenured；方法区和堆都是各个线程共享的内存区域，用于存储已经被JVM加载的类信息、常量、静态变量、JIT编译器编译后的代码等数据；程序中的字面量（literal）如直接书写的100、"hello"和常量都是放在常量池中，常量池是方法区的一部分，。栈空间操作起来最快但是栈很小，通常大量的对象都是放在堆空间，栈和堆的大小都可以通过JVM的启动参数来进行调整，栈空间用光了会引发StackOverflowError，而堆和常量池空间不足则会引发OutOfMemoryError。

    String str = new String("hello");

上面的语句中变量str放在栈上，用new创建出来的字符串对象放在堆上，而"hello"这个字面量是放在方法区的。

    补充1：较新版本的Java（从Java 6的某个更新开始）中，由于JIT编译器的发展和"逃逸分析"技术的逐渐成熟，栈上分配、标量替换等优化技术使得对象一定分配在堆上这件事情已经变得不那么绝对了。

    补充2：运行时常量池相当于Class文件常量池具有动态性，Java语言并不要求常量一定只有编译期间才能产生，运行期间也可以将新的常量放入池中，String类的intern()方法就是这样的。


**10、Math.round(11.5) 等于多少？Math.round(-11.5)等于多少？**

答：Math.round(11.5)的返回值是12，Math.round(-11.5)的返回值是-11。

四舍五入的原理是在参数上加0.5然后进行下取整。

**11、switch 是否能作用在byte 上，是否能作用在long 上，是否能作用在String上？**

答：在Java 5以前，switch(expr)中，expr只能是byte、short、char、int。

从Java 5开始，Java中引入了枚举类型，expr也可以是enum类型，

从Java 7开始，expr还可以是字符串（String），但是长整型（long）在目前所有的版本中都是不可以的。


**12、用最有效率的方法计算2乘以8？**

答： 2 << 3（左移3位相当于乘以2的3次方，右移3位相当于除以2的3次方）。


**13、数组有没有length()方法？String有没有length()方法？**

答：数组没有length()方法，有length 的属性。

String 有length()方法。JavaScript中，获得字符串的长度是通过length属性得到的，这一点容易和Java混淆。


**14、在Java中，如何跳出当前的多重嵌套循环？**

答：在最外层循环前加一个标记如A，然后用break A;可以跳出多重循环。（Java中支持带标签的break和continue语句，作用有点类似于C和C++中的goto语句，但是就像要避免使用goto一样，应该避免使用带标签的break和continue，因为它不会让你的程序变得更优雅，很多时候甚至有相反的作用，所以这种语法其实不知道更好）


**15、构造器（constructor）是否可被重写（override）？**

答：构造器不能被继承，因此不能被重写，但可以被重载。



**16、两个对象值相同(x.equals(y) == true)，但却可有不同的hash code，这句话对不对？**

答：不对，如果两个对象x和y满足x.equals(y) == true，它们的哈希码（hash code）应当相同。

Java对于eqauls方法和hashCode方法是这样规定的：

(1)如果两个对象相同（equals方法返回true），那么它们的hashCode值一定要相同；
(2)如果两个对象的hashCode相同，它们并不一定相同。

当然，你未必要按照要求去做，但是如果你违背了上述原则就会发现在使用容器时，相同的对象可以出现在Set集合中，同时增加新元素的效率会大大下降（对于使用哈希存储的系统，如果哈希码频繁的冲突将会造成存取性能急剧下降）。

    补充：关于equals和hashCode方法，很多Java程序都知道，但很多人也就是仅仅知道而已，在Joshua Bloch的大作《Effective Java》（很多软件公司，《Effective Java》、《Java编程思想》以及《重构：改善既有代码质量》是Java程序员必看书籍，如果你还没看过，那就赶紧去亚马逊买一本吧）中是这样介绍equals方法的：首先equals方法必须满足自反性（x.equals(x)必须返回true）、对称性（x.equals(y)返回true时，y.equals(x)也必须返回true）、传递性（x.equals(y)和y.equals(z)都返回true时，x.equals(z)也必须返回true）和一致性（当x和y引用的对象信息没有被修改时，多次调用x.equals(y)应该得到同样的返回值），而且对于任何非null值的引用x，x.equals(null)必须返回false。

    实现高质量的equals方法的诀窍包括：

    1. 使用==操作符检查"参数是否为这个对象的引用"；
    2. 使用instanceof操作符检查"参数是否为正确的类型"；
    3. 对于类中的关键属性，检查参数传入对象的属性是否与之相匹配；
    4. 编写完equals方法后，问自己它是否满足对称性、传递性、一致性；
    5. 重写equals时总是要重写hashCode；
    6. 不要将equals方法参数中的Object对象替换为其他的类型，在重写时不要忘掉@Override注解。


**17、是否可以继承String类？**

答：String 类是final类，不可以被继承。


**18、当一个对象被当作参数传递到一个方法后，此方法可改变这个对象的属性，并可返回变化后的结果，那么这里到底是值传递还是引用传递？**

答：是值传递。

Java语言的方法调用只支持参数的值传递。当一个对象实例作为一个参数被传递到方法中时，参数的值就是对该对象的引用。对象的属性可以在被调用过程中被改变，但对对象引用的改变是不会影响到调用者的。C++和C#中可以通过传引用或传输出参数来改变传入的参数的值。在C#中可以编写如下所示的代码，但是在Java中却做不到。

    说明：Java中没有传引用实在是非常的不方便，这一点在Java 8中仍然没有得到改进，正是如此在Java编写的代码中才会出现大量的Wrapper类（将需要通过方法调用修改的引用置于一个Wrapper类中，再将Wrapper对象传入方法），这样的做法只会让代码变得臃肿，尤其是让从C和C++转型为Java程序员的开发者无法容忍。


参考： https://blog.csdn.net/zzp_403184692/article/details/8184751

**19、String和StringBuilder、StringBuffer的区别？**

答：Java平台提供了两种类型的字符串：String和StringBuffer/StringBuilder，它们可以储存和操作字符串。其中String是只读字符串，也就意味着String引用的字符串内容是不能被改变的。而StringBuffer/StringBuilder类表示的字符串对象可以直接进行修改。StringBuilder是Java 5中引入的，它和StringBuffer的方法完全相同，区别在于它是在单线程环境下使用的，因为它的所有方面都没有被synchronized修饰，因此它的效率也比StringBuffer要高。


面试题1 - 什么情况下用+运算符进行字符串连接比调用StringBuffer/StringBuilder对象的append方法连接字符串性能更好？

面试题2 - 请说出下面程序的输出。

.. code:: java

    class StringEqualTest {

        public static void main(String[] args) {
            String s1 = "Programming";
            String s2 = new String("Programming");
            String s3 = "Program";
            String s4 = "ming";
            String s5 = "Program" + "ming";
            String s6 = s3 + s4;
            System.out.println(s1 == s2);
            System.out.println(s1 == s5);
            System.out.println(s1 == s6);
            System.out.println(s1 == s6.intern());
            System.out.println(s2 == s2.intern());
        }
    }

补充：解答上面的面试题需要清楚两点：

1. String对象的intern方法会得到字符串对象在常量池中对应的版本的引用（如果常量池中有一个字符串与String对象的equals结果是true），如果常量池中没有对应的字符串，则该字符串将被添加到常量池中，然后返回常量池中字符串的引用；

2. 字符串的+操作其本质是创建了StringBuilder对象进行append操作，然后将拼接后的StringBuilder对象用toString方法处理成String对象，这一点可以用javap -c StringEqualTest.class命令获得class文件对应的JVM字节码指令就可以看出来。

::

    false
    true
    false
    true
    false

**20、重载（Overload）和重写（Override）的区别。重载的方法能否根据返回类型进行区分？**

答：方法的重载和重写都是实现多态的方式，区别在于前者实现的是编译时的多态性，而后者实现的是运行时的多态性。

重载发生在一个类中，同名的方法如果有不同的参数列表（参数类型不同、参数个数不同或者二者都不同）则视为重载；重写发生在子类与父类之间，重写要求子类被重写方法与父类被重写方法有相同的返回类型，比父类被重写方法更好访问，不能比父类被重写方法声明更多的异常（里氏代换原则）。重载对返回类型没有特殊的要求。

    面试题：华为的面试题中曾经问过这样一个问题 - "为什么不能根据返回类型来区分重载"，快说出你的答案吧！



声明抽象方法不可写出大括号

数组是一种对象，不是原生类

只有  public 、 abstract 可以修饰外部接口


按引用传参不能改变实际参数的参考地址

类方法中可以用this调用本类的类方法 错误

在类方法中绝对不能调用实例方法 错误  new 之后可以调用


引用不是指正，引用本本身是 primitive


数组的创建，一维数组，二维数组


默认构造方法有和他所在类相同的访问修饰符

main是java的关

package在同一个类中只能出现一次，且需要在第一行（不含注释）

SortedMap 是接口，TreeMap的key不能为null，value可以为null，HashTable都不能为null，HashMap都可以为null。但是可以为空（""）

注意Thread类的启动方式，run() 启动的话为非线程方式启动，start()线程方式启动 

不同列表选择不同发的遍历方式 建议67



**21. 哪些能继承，哪些能重写**

::

    When you do this, you automatically get all the fields and methods in the base class
    ---摘自《Thinking in Java》 Reusing class一章。
    当继承时，会自动得到基类中的所有的域和方法。
    关于这句话如何解读？
    1、private修饰的成员能够继承下来吗？
    2、final修饰的成员能继承下来吗？
    3、static修饰的成员能继承下来吗？

final 能继承，不能被重写

static能继承，能重写，但是不能用override修饰

private 不能被继承（又有人说能被继承，只是不能被访问而已）

构造函数不可以被继承，因此不能被重写，但可以被重载

**总结： private、构造方法 不能被继承**

`请看详细介绍1`_

`请看详细介绍2`_

.. _`请看详细介绍1`: https://www.cnblogs.com/cbs-writing/p/7088450.html
.. _`请看详细介绍2`: https://www.zhihu.com/question/51345942/answer/145388196



**22. JAVA静态方法是否可以被继承？**

结论：java中静态属性和静态方法可以被继承，但是没有被重写(overwrite)而是被隐藏.
原因：

::

    1). 静态方法和属性是属于类的，调用的时候直接通过类名.方法名完成对，不需要继承机制及可以调用。如果子类里面定义了静态
        至于是否继承一说，子类是有继承静态方法和属性，但是跟实例方法和属性不太一样，存在"隐藏"的这种情况。
    2). 多态之所以能够实现依赖于继承、接口和重写、重载（继承和重写最为关键）。有了继承和重写就可以实现父类的引用指向不同子类的对象。
        重写的功能是："重写"后子类的优先级要高于父类的优先级，但是“隐藏”是没有这个优先级之分的。
    3). 静态属性、静态方法和非静态的属性都可以被继承和隐藏而不能被重写，因此不能实现多态，不能实现父类的引用可以指向不同子类的对象。
        非静态方法可以被继承和重写，因此可以实现多态。


23。 下面程序为什么输出12，而不是-6，


.. code:: java

    public class A {
    int i = 12;
    }

    class B extends A {
    int i = -6;
    public static void main(String[] args) {
    A xx = new B();
    System.out.println(xx.i);
    }
    }

方法是动态绑定，变量不会

**24. TreeSet**

加入时（add）排序，修改时不排序


**25. Map**

HashMap:

线程不安全，fast-fail 机制，key value 允许为空 ; containsvalue containskey； Iterator 遍历 ; 16

HashTable:

线程安全，key,value 不允许为null， contains ； Enumenation遍历 ; 11 2*old + 1; 对象的hashcode


SortedMap 是接口，TreeMap的key不能为null，value可以为null，HashTable都不能为null，HashMap都可以为null。但是可以为空（""）


子类方法比父类方法抛出的异常**更窄**；但是构造函数相反；

子类的访问限制比父类需要**更宽**