Java 泛型
===========

引言
-----


泛型是Java中一个非常重要的知识点，在Java集合类框架中泛型被广泛应用。本文我们将从零开始来看一下Java泛型的设计，将会涉及到通配符处理，以及让人苦恼的类型擦除。





泛型类
-----------

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


泛型方法
----------


看完了泛型类，接下来我们来了解一下泛型方法。声明一个泛型方法很简单，只要在返回类型前面加上一个类似<K, V>的形式就行了：

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

边界符
------

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

通配符
------



在了解通配符之前，我们首先必须要澄清一个概念，还是借用我们上面定义的Box类，假设我们添加一个这样的方法：



参考

http://www.importnew.com/24029.html
