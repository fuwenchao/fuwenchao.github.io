Java 泛型
===========

引言
-----


泛型是Java中一个非常重要的知识点，在Java集合类框架中泛型被广泛应用。本文我们将从零开始来看一下Java泛型的设计，将会涉及到通配符处理，以及让人苦恼的类型擦除。

**一个栗子**

.. code:: java

	List arrayList = new ArrayList();
	arrayList.add("aaaa");
	arrayList.add(100);

	for(int i = 0; i< arrayList.size();i++){
	    String item = (String)arrayList.get(i);
	    Log.d("泛型测试","item = " + item);
	}

毫无疑问，程序的运行结果会以崩溃结束：

	java.lang.ClassCastException: java.lang.Integer cannot be cast to java.lang.String

ArrayList可以存放任意类型，例子中添加了一个String类型，添加了一个Integer类型，再使用时都以String的方式使用，因此程序崩溃了。为了解决类似这样的问题（在编译阶段就可以解决），泛型应运而生。

我们将第一行声明初始化list的代码更改一下，编译器会在编译阶段就能够帮我们发现类似这样的问题。

.. code:: java

	List<String> arrayList = new ArrayList<String>();
	...
	//arrayList.add(100); 在编译阶段，编译器就会报错


特性
------------

泛型只在编译阶段有效。看下面的代码：

.. code:: java

	List<String> stringArrayList = new ArrayList<String>();
	List<Integer> integerArrayList = new ArrayList<Integer>();

	Class classStringArrayList = stringArrayList.getClass();
	Class classIntegerArrayList = integerArrayList.getClass();

	if(classStringArrayList.equals(classIntegerArrayList)){
	    Log.d("泛型测试","类型相同");
	}

输出结果：D/泛型测试: 类型相同。

通过上面的例子可以证明，在编译之后程序会采取去泛型化的措施。也就是说Java中的泛型，只在编译阶段有效。在编译过程中，正确检验泛型结果后，会将泛型的相关信息擦出，并且在对象进入和离开方法的边界处添加类型检查和类型转换的方法。也就是说，泛型信息不会进入到运行时阶段。

对此总结成一句话：泛型类型在逻辑上看以看成是多个不同的类型，实际上都是相同的基本类型。



泛型类
-----------

泛型类的最基本写法（这么看可能会有点晕，会在下面的例子中详解）：

.. code:: java

	class 类名称 <泛型标识：可以随便写任意标识号，标识指定的泛型的类型>{
	  private 泛型标识 /*（成员变量类型）*/ var; 
	  .....

	  }
	}



我们首先定义一个简单的Box类：

.. code:: java

	public class Box {
	    private String object;
	    public void set(String object) { this.object = object; }
	    public String get() { return object; }
	}

这是最常见的做法，这样做的一个坏处是Box里面现在只能装入String类型的元素，今后如果我们需要装入Integer等其他类型的元素，还必须要另外重写一个Box，代码得不到复用，使用泛型可以很好的解决这个问题。

.. code:: java

	public class Box<T> {
	    // T stands for "Type"
	    private T t;
	    public void set(T t) { this.t = t; }
	    public T get() { return t; }
	}

这样我们的Box类便可以得到复用，我们可以将T替换成任何我们想要的类型：

.. code:: java

	Box<Integer> integerBox = new Box<Integer>();
	Box<Double> doubleBox = new Box<Double>();
	Box<String> stringBox = new Box<String>();

**一个最普通的泛型类：**

.. code:: java

	//此处T可以随便写为任意标识，常见的如T、E、K、V等形式的参数常用于表示泛型
	//在实例化泛型类时，必须指定T的具体类型
	public class Generic<T>{ 
	    //key这个成员变量的类型为T,T的类型由外部指定  
	    private T key;

	    public Generic(T key) { //泛型构造方法形参key的类型也为T，T的类型由外部指定
	        this.key = key;
	    }

	    public T getKey(){ //泛型方法getKey的返回值类型为T，T的类型由外部指定
	        return key;
	    }
	}

.. code:: java

	//泛型的类型参数只能是类类型（包括自定义类），不能是简单类型
	//传入的实参类型需与泛型的类型参数类型相同，即为Integer.
	Generic<Integer> genericInteger = new Generic<Integer>(123456);

	//传入的实参类型需与泛型的类型参数类型相同，即为String.
	Generic<String> genericString = new Generic<String>("key_vlaue");
	Log.d("泛型测试","key is " + genericInteger.getKey());
	Log.d("泛型测试","key is " + genericString.getKey());


定义的泛型类，就一定要传入泛型类型实参么？并不是这样，在使用泛型的时候如果传入泛型实参，则会根据传入的泛型实参做相应的限制，此时泛型才会起到本应起到的限制作用。如果不传入泛型类型实参的话，在泛型类中使用泛型的方法或成员变量定义的类型可以为任何的类型。


看一个例子：

.. code:: java

	Generic generic = new Generic("111111");
	Generic generic1 = new Generic(4444);
	Generic generic2 = new Generic(55.55);
	Generic generic3 = new Generic(false);

	Log.d("泛型测试","key is " + generic.getKey());
	Log.d("泛型测试","key is " + generic1.getKey());
	Log.d("泛型测试","key is " + generic2.getKey());
	Log.d("泛型测试","key is " + generic3.getKey());


	D/泛型测试: key is 111111
	D/泛型测试: key is 4444
	D/泛型测试: key is 55.55
	D/泛型测试: key is false




**注意**

泛型的类型参数只能是类类型，不能是简单类型。

不能对确切的泛型类型使用instanceof操作。如下面的操作是非法的，编译时会出错。

.. code:: java

	if(ex_num instanceof Generic<Number>){   
	} 


泛型接口
--------------

泛型接口与泛型类的定义及使用基本相同。泛型接口常被用在各种类的生产器中，可以看一个例子：

.. code:: java

	//定义一个泛型接口
	public interface Generator<T> {
	    public T next();
	}


当实现泛型接口的类，未传入泛型实参时：

.. code:: java

	/**
	 * 未传入泛型实参时，与泛型类的定义相同，在声明类的时候，需将泛型的声明也一起加到类中
	 * 即：class FruitGenerator<T> implements Generator<T>{
	 * 如果不声明泛型，如：class FruitGenerator implements Generator<T>，编译器会报错："Unknown class"
	 */
	class FruitGenerator<T> implements Generator<T>{
	    @Override
	    public T next() {
	        return null;
	    }
	}

当实现泛型接口的类，传入泛型实参时：

.. code:: java

	/**
	 * 传入泛型实参时：
	 * 定义一个生产器实现这个接口,虽然我们只创建了一个泛型接口Generator<T>
	 * 但是我们可以为T传入无数个实参，形成无数种类型的Generator接口。
	 * 在实现类实现泛型接口时，如已将泛型类型传入实参类型，则所有使用泛型的地方都要替换成传入的实参类型
	 * 即：Generator<T>，public T next();中的的T都要替换成传入的String类型。
	 */
	public class FruitGenerator implements Generator<String> {

	    private String[] fruits = new String[]{"Apple", "Banana", "Pear"};

	    @Override
	    public String next() {
	        Random rand = new Random();
	        return fruits[rand.nextInt(3)];
	    }
	}


泛型方法
----------


看完了泛型类，接下来我们来了解一下泛型方法。声明一个泛型方法很简单，只要在返回类型前面加上一个类似<K, V>的形式就行了：

.. code:: java

	/**
	 * 泛型方法的基本介绍
	 * @param tClass 传入的泛型实参
	 * @return T 返回值为T类型
	 * 说明：
	 *     1）public 与 返回值中间<T>非常重要，可以理解为声明此方法为泛型方法。
	 *     2）只有声明了<T>的方法才是泛型方法，泛型类中的使用了泛型的成员方法并不是泛型方法。
	 *     3）<T>表明该方法将使用泛型类型T，此时才可以在方法中使用泛型类型T。
	 *     4）与泛型类的定义一样，此处T可以随便写为任意标识，常见的如T、E、K、V等形式的参数常用于表示泛型。
	 */
	public <T> T genericMethod(Class<T> tClass)throws InstantiationException ,
	  IllegalAccessException{
	        T instance = tClass.newInstance();
	        return instance;
	}

	Object obj = genericMethod(Class.forName("com.test.test"));




**一个栗子**

.. code:: java

	public class Util {
	    public static <K, V> boolean compare(Pair<K, V> p1, Pair<K, V> p2) {
	        return p1.getKey().equals(p2.getKey()) &&
	               p1.getValue().equals(p2.getValue());
	    }
	}
	public class Pair<K, V> {
	    private K key;
	    private V value;
	    public Pair(K key, V value) {
	        this.key = key;
	        this.value = value;
	    }
	    public void setKey(K key) { this.key = key; }
	    public void setValue(V value) { this.value = value; }
	    public K getKey()   { return key; }
	    public V getValue() { return value; }
	}

我们可以像下面这样去调用泛型方法：

.. code:: java

	Pair<Integer, String> p1 = new Pair<>(1, "apple");
	Pair<Integer, String> p2 = new Pair<>(2, "pear");
	boolean same = Util.<Integer, String>compare(p1, p2);

或者在Java1.7/1.8利用type inference，让Java自动推导出相应的类型参数：

.. code:: java

	Pair<Integer, String> p1 = new Pair<>(1, "apple");
	Pair<Integer, String> p2 = new Pair<>(2, "pear");
	boolean same = Util.compare(p1, p2);



**泛型方法的基本用法**

光看上面的例子有的同学可能依然会非常迷糊，我们再通过一个例子，把我泛型方法再总结一下。

.. code:: java

	public class GenericTest {
	   //这个类是个泛型类，在上面已经介绍过
	   public class Generic<T>{     
	        private T key;

	        public Generic(T key) {
	            this.key = key;
	        }

	        //我想说的其实是这个，虽然在方法中使用了泛型，但是这并不是一个泛型方法。
	        //这只是类中一个普通的成员方法，只不过他的返回值是在声明泛型类已经声明过的泛型。
	        //所以在这个方法中才可以继续使用 T 这个泛型。
	        public T getKey(){
	            return key;
	        }

	        /**
	         * 这个方法显然是有问题的，在编译器会给我们提示这样的错误信息"cannot reslove symbol E"
	         * 因为在类的声明中并未声明泛型E，所以在使用E做形参和返回值类型时，编译器会无法识别。
	        public E setKey(E key){
	             this.key = keu
	        }
	        */
	    }

	    /** 
	     * 这才是一个真正的泛型方法。
	     * 首先在public与返回值之间的<T>必不可少，这表明这是一个泛型方法，并且声明了一个泛型T
	     * 这个T可以出现在这个泛型方法的任意位置.
	     * 泛型的数量也可以为任意多个 
	     *    如：public <T,K> K showKeyName(Generic<T> container){
	     *        ...
	     *        }
	     */
	    public <T> T showKeyName(Generic<T> container){
	        System.out.println("container key :" + container.getKey());
	        //当然这个例子举的不太合适，只是为了说明泛型方法的特性。
	        T test = container.getKey();
	        return test;
	    }

	    //这也不是一个泛型方法，这就是一个普通的方法，只是使用了Generic<Number>这个泛型类做形参而已。
	    public void showKeyValue1(Generic<Number> obj){
	        Log.d("泛型测试","key value is " + obj.getKey());
	    }

	    //这也不是一个泛型方法，这也是一个普通的方法，只不过使用了泛型通配符?
	    //同时这也印证了泛型通配符章节所描述的，?是一种类型实参，可以看做为Number等所有类的父类
	    public void showKeyValue2(Generic<?> obj){
	        Log.d("泛型测试","key value is " + obj.getKey());
	    }

	     /**
	     * 这个方法是有问题的，编译器会为我们提示错误信息："UnKnown class 'E' "
	     * 虽然我们声明了<T>,也表明了这是一个可以处理泛型的类型的泛型方法。
	     * 但是只声明了泛型类型T，并未声明泛型类型E，因此编译器并不知道该如何处理E这个类型。
	    public <T> T showKeyName(Generic<E> container){
	        ...
	    }  
	    */

	    /**
	     * 这个方法也是有问题的，编译器会为我们提示错误信息："UnKnown class 'T' "
	     * 对于编译器来说T这个类型并未项目中声明过，因此编译也不知道该如何编译这个类。
	     * 所以这也不是一个正确的泛型方法声明。
	    public void showkey(T genericObj){

	    }
	    */

	    public static void main(String[] args) {


	    }
	}

**类中的泛型方法**

当然这并不是泛型方法的全部，泛型方法可以出现杂任何地方和任何场景中使用。但是有一种情况是非常特殊的，当泛型方法出现在泛型类中时，我们再通过一个例子看一下

.. code:: java

	public class GenericFruit {
	    class Fruit{
	        @Override
	        public String toString() {
	            return "fruit";
	        }
	    }

	    class Apple extends Fruit{
	        @Override
	        public String toString() {
	            return "apple";
	        }
	    }

	    class Person{
	        @Override
	        public String toString() {
	            return "Person";
	        }
	    }

	    class GenerateTest<T>{
	        public void show_1(T t){
	            System.out.println(t.toString());
	        }

	        //在泛型类中声明了一个泛型方法，使用泛型E，这种泛型E可以为任意类型。可以类型与T相同，也可以不同。
	        //由于泛型方法在声明的时候会声明泛型<E>，因此即使在泛型类中并未声明泛型，编译器也能够正确识别泛型方法中识别的泛型。
	        public <E> void show_3(E t){
	            System.out.println(t.toString());
	        }

	        //在泛型类中声明了一个泛型方法，使用泛型T，注意这个T是一种全新的类型，可以与泛型类中声明的T不是同一种类型。
	        public <T> void show_2(T t){
	            System.out.println(t.toString());
	        }
	    }

	    public static void main(String[] args) {
	        Apple apple = new Apple();
	        Person person = new Person();

	        GenerateTest<Fruit> generateTest = new GenerateTest<Fruit>();
	        //apple是Fruit的子类，所以这里可以
	        generateTest.show_1(apple);
	        //编译器会报错，因为泛型类型实参指定的是Fruit，而传入的实参类是Person
	        //generateTest.show_1(person);

	        //使用这两个方法都可以成功
	        generateTest.show_2(apple);
	        generateTest.show_2(person);

	        //使用这两个方法也都可以成功
	        generateTest.show_3(apple);
	        generateTest.show_3(person);
	    }
	}


**泛型方法与可变参数**

再看一个泛型方法和可变参数的例子：

.. code:: java

	public <T> void printMsg( T... args){
	    for(T t : args){
	        Log.d("泛型测试","t is " + t);
	    }
	}

	printMsg("111",222,"aaaa","2323.4",55.55);

**静态方法与泛型**

静态方法有一种情况需要注意一下，那就是在类中的静态方法使用泛型：静态方法无法访问类上定义的泛型；如果静态方法操作的引用数据类型不确定的时候，必须要将泛型定义在方法上。

即：如果静态方法要使用泛型的话，必须将静态方法也定义成泛型方法 。

.. code:: java

	public class StaticGenerator<T> {
	    ....
	    ....
	    /**
	     * 如果在类中定义使用泛型的静态方法，需要添加额外的泛型声明（将这个方法定义成泛型方法）
	     * 即使静态方法要使用泛型类中已经声明过的泛型也不可以。
	     * 如：public static void show(T t){..},此时编译器会提示错误信息：
	          "StaticGenerator cannot be refrenced from static context"
	     */
	    public static <T> void show(T t){

	    }
	}



边界符
------


在使用泛型的时候，我们还可以为传入的泛型类型实参进行上下边界的限制，如：类型实参只准传入某种类型的父类或某种类型的子类。



现在我们要实现这样一个功能，查找一个泛型数组中大于某个特定元素的个数，我们可以这样实现：

.. code:: java

	public static <T> int countGreaterThan(T[] anArray, T elem) {
	    int count = 0;
	    for (T e : anArray)
	        if (e > elem)  // compiler error
	            ++count;
	    return count;
	}


但是这样很明显是错误的，因为除了short, int, double, long, float, byte, char等原始类型，其他的类并不一定能使用操作符>，所以编译器报错，那怎么解决这个问题呢？答案是使用边界符。


.. code:: java

	public interface Comparable<T> {
	    public int compareTo(T o);
	}

做一个类似于下面这样的声明，这样就等于告诉编译器类型参数T代表的都是实现了Comparable接口的类，这样等于告诉编译器它们都至少实现了compareTo方法。

.. code:: java

	public static <T extends Comparable<T>> int countGreaterThan(T[] anArray, T elem) {
	    int count = 0;
	    for (T e : anArray)
	        if (e.compareTo(elem) > 0)
	            ++count;
	    return count;
	}


**上边界**  即传入的类型实参必须是指定类型的子类型

.. code:: java

	public void showKeyValue1(Generic<? extends Number> obj){
	    Log.d("泛型测试","key value is " + obj.getKey());
	}


.. code:: java

	Generic<String> generic1 = new Generic<String>("11111");
	Generic<Integer> generic2 = new Generic<Integer>(2222);
	Generic<Float> generic3 = new Generic<Float>(2.4f);
	Generic<Double> generic4 = new Generic<Double>(2.56);

	//这一行代码编译器会提示错误，因为String类型并不是Number类型的子类
	//showKeyValue1(generic1);

	showKeyValue1(generic2);
	showKeyValue1(generic3);
	showKeyValue1(generic4);


如果我们把泛型类的定义也改一下

.. code:: java

	public class Generic<T extends Number>{
	    private T key;

	    public Generic(T key) {
	        this.key = key;
	    }

	    public T getKey(){
	        return key;
	    }
	}

这一行代码也会报错，因为String不是Number的子类

	Generic<String> generic1 = new Generic<String>("11111");


**再来一个泛型方法的例子：**

.. code:: java

	//在泛型方法中添加上下边界限制的时候，必须在权限声明与返回值之间的<T>上添加上下边界，即在泛型声明的时候添加
	//public <T> T showKeyName(Generic<T extends Number> container)，编译器会报错："Unexpected bound"
	public <T extends Number> T showKeyName(Generic<T> container){
	    System.out.println("container key :" + container.getKey());
	    T test = container.getKey();
	    return test;
	}


通过上面的两个例子可以看出：泛型的上下边界添加，必须与泛型的声明在一起 。

通配符
------


我们知道Ingeter是Number的一个子类，同时在特性章节中我们也验证过Generic<Ingeter>与Generic<Number>实际上是相同的一种基本类型。那么问题来了，在使用Generic<Number>作为形参的方法中，能否使用Generic<Ingeter>的实例传入呢？在逻辑上类似于Generic<Number>和Generic<Ingeter>是否可以看成具有父子关系的泛型类型呢？

为了弄清楚这个问题，我们使用Generic<T>这个泛型类继续看下面的例子：

.. code:: java

	public void showKeyValue1(Generic<Number> obj){
	    Log.d("泛型测试","key value is " + obj.getKey());
	}

	Generic<Integer> gInteger = new Generic<Integer>(123);
	Generic<Number> gNumber = new Generic<Number>(456);

	showKeyValue(gNumber);

	// showKeyValue这个方法编译器会为我们报错：Generic<java.lang.Integer> 
	// cannot be applied to Generic<java.lang.Number>
	// showKeyValue(gInteger);

通过提示信息我们可以看到Generic<Integer>不能被看作为`Generic<Number>的子类。由此可以看出:同一种泛型可以对应多个版本（因为参数类型是不确定的），不同版本的泛型类实例是不兼容的。

回到上面的例子，如何解决上面的问题？总不能为了定义一个新的方法来处理Generic<Integer>类型的类，这显然与java中的多台理念相违背。因此我们需要一个在逻辑上可以表示同时是Generic<Integer>和Generic<Number>父类的引用类型。由此类型通配符应运而生。

我们可以将上面的方法改一下：

.. code:: java

	public void showKeyValue1(Generic<?> obj){
	    Log.d("泛型测试","key value is " + obj.getKey());
	}


类型通配符一般是使用？代替具体的类型实参，注意了，此处’？’是类型实参，而不是类型形参 。重要说三遍！此处’？’是类型实参，而不是类型形参 ！ 此处’？’是类型实参，而不是类型形参 ！再直白点的意思就是，此处的？和Number、String、Integer一样都是一种实际的类型，可以把？看成所有类型的父类。是一种真实的类型。

可以解决当具体类型不确定的时候，这个通配符就是 ?  ；当操作类型时，不需要使用类型的具体功能时，只使用Object类中的功能。那么可以用 ? 通配符来表未知类型。




在了解通配符之前，我们首先必须要澄清一个概念，还是借用我们上面定义的Box类，假设我们添加一个这样的方法：


关于泛型数组要提一下
---------------------------


看到了很多文章中都会提起泛型数组，经过查看sun的说明文档，在java中是”不能创建一个确切的泛型类型的数组”的。

也就是说下面的这个例子是不可以的：

	List<String>[] ls = new ArrayList<String>[10];  

而使用通配符创建泛型数组是可以的，如下面这个例子：

	List<?>[] ls = new ArrayList<?>[10];  

这样也是可以的：

	List<String>[] ls = new ArrayList[10];

下面使用Sun的一篇文档的一个例子来说明这个问题：

.. code:: java


	List<String>[] lsa = new List<String>[10]; // Not really allowed.    
	Object o = lsa;    
	Object[] oa = (Object[]) o;    
	List<Integer> li = new ArrayList<Integer>();    
	li.add(new Integer(3));    
	oa[1] = li; // Unsound, but passes run time store check    
	String s = lsa[1].get(0); // Run-time error: ClassCastException.




这种情况下，由于JVM泛型的擦除机制，在运行时JVM是不知道泛型信息的，所以可以给oa[1]赋上一个ArrayList而不会出现异常，但是在取出数据的时候却要做一次类型转换，所以就会出现ClassCastException，如果可以进行泛型数组的声明，上面说的这种情况在编译期将不会出现任何的警告和错误，只有在运行时才会出错。

而对泛型数组的声明进行限制，对于这样的情况，可以在编译期提示代码有类型安全问题，比没有任何提示要强很多。

下面采用通配符的方式是被允许的:数组的类型不可以是类型变量，除非是采用通配符的方式，因为对于通配符的方式，最后取出数据是要做显式的类型转换的。

.. code:: java

	List<?>[] lsa = new List<?>[10]; // OK, array of unbounded wildcard type.    
	Object o = lsa;    
	Object[] oa = (Object[]) o;    
	List<Integer> li = new ArrayList<Integer>();    
	li.add(new Integer(3));    
	oa[1] = li; // Correct.    
	Integer i = (Integer) lsa[1].get(0); // OK 



----

参考

http://www.importnew.com/24029.html
