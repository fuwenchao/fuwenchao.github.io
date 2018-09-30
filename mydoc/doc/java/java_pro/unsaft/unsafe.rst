关于Unsafe类的一点研究
==========================


Unsafe类是java中非常特别的一个类。它名字就叫做“不安全”，提供的操作可以直接读写内存、获得地址偏移值、锁定或释放线程。

通过正常途径是无法获得Unsafe实例的，首先它的构造方法是私有的，然后，即使你调用它的getUnsafe方法，也会抛出SecurityException。


任何关于Unsafe类的文章都不会推荐我们在代码中使用它，但这并不妨碍我们了解它可以做什么。下面我们来看下利用Unsafe类我们是否可以做点有趣的事情。

**获取Unsafe实例**

.. code:: java

    //绕过安全检查
    public static Unsafe getUnsafeInstance() throws Exception {
        Field unsafeStaticField = Unsafe.class.getDeclaredField("theUnsafe");
        unsafeStaticField.setAccessible(true);
        return (Unsafe) unsafeStaticField.get(Unsafe.class);
    }

通过java反射机制，我们跳过了安全检测，拿到了一个unsafe类的实例。

我找遍了Unsafe类的API，没有发现可以直接获取对象地址的方法，Unsafe中操作地址相关的方法都要求提供一个Object类型的参数，用来获取对象的初始地址。


**修改变量值**

.. code:: java

    /**
     * @Author wenchaofu
     * @DATE 9:35 2018/6/26
     * @DESC
     */
    public class UnsafeTest {

        private   int a ;

        public static void main(String[] args) throws Exception {

    //        Unsafe unsafe1 = Unsafe.getUnsafe();  //抛出异常 安全检查
            Unsafe unsafe = getUnsafeInstance();
            UnsafeTest unsafeTest = new UnsafeTest();
            unsafeTest.a = 100;
            long offset = unsafe.objectFieldOffset(UnsafeTest.class.getDeclaredField("a"));
            System.out.println(unsafe.getInt(unsafeTest, offset));


        }

        //绕过安全检查
        public static Unsafe getUnsafeInstance() throws Exception {
            Field unsafeStaticField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeStaticField.setAccessible(true);
            return (Unsafe) unsafeStaticField.get(Unsafe.class);
        }
    }


**获取操作系统的位数**

::

    //  Report the size in bytes of a native pointer.  
    //  返回4或8,代表是32位还是64位操作系统。  
    System.out.println(unsafe.addressSize());  
    // 返回32或64,获取操作系统是32位还是64位  
    System.out.println(System.getProperty("sun.arch.data.model"));  


**修改和读取数组中的值**

.. code:: java


    /**
     * @Author wenchaofu
     * @DATE 9:59 2018/6/26
     * @DESC
     */
    public class UnsafeArrayTest {

        public static void main(String[] args) throws Exception {
            Unsafe u = UnsafeTool.getUnsafeInstance();
            int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
            int b = u.arrayBaseOffset(int[].class);
            int s = u.arrayIndexScale(int[].class);
            u.putInt(arr,(long)b+s*9,1);
            for(int i = 0;i<10;i++) {
                int v = u.getInt(arr, (long) b + s * i);
                System.out.print(v + " ");
            }
        }
    }

- arrayBaseOffset: 返回当前数组第一个元素地址相对于数组起始地址的偏移值，在本例中返回6。

- arrayIndexScale: 返回当前数组一个元素占用的字节数,在本例中返回4。

- putInt(obj,offset,intval): 获取数组对象obj的起始地址，加上偏移值，得到对应元素的地址，将intval写入内存。

- getInt(obj,offset): 获取数组对象obj的起始地址，加上偏移值，得到对应元素的地址，从而获得元素的值。

- 偏移值: 数组元素偏移值 = arrayBaseOffset + arrayIndexScalse * i。


获取对象实例
-------------

.. code:: java

    /** Allocate an instance but do not run any constructor. Initializes the class if it has not yet been. */ 
    public native Object allocateInstance(Class cls) throws InstantiationException;

allocateInstance: 在不执行构造方法的前提下，获取一个类的实例，即使这个类的构造方法是私有的。

**修改静态变量和实例变量的值**

先定义一个Test类

.. code:: java

    /**
     * @Author wenchaofu
     * @DATE 10:05 2018/6/26
     * @DESC
     */
    public class Test {
        public int intfield;
        public static int staticIntField;
        public static int[] arr;

        private Test() {
            System.out.println("constructor called");
        }

    }

操作类中的变量

.. code:: java


    /**
     * @Author wenchaofu
     * @DATE 10:06 2018/6/26
     * @DESC
     */
    public class UnsafeUseTest {
        public static void main(String[] args) throws Exception {
            Unsafe unsafe = UnsafeTool.getUnsafeInstance();
            Test t = (Test)unsafe.allocateInstance(Test.class);

            // 修改实例变量
            long intField = unsafe.objectFieldOffset(Test.class.getDeclaredField("intfield"));
            unsafe.putInt(t,intField,2);
            System.out.println("----" + t.intfield);

            System.out.println("=================");

            //  修改static变量

            Field staticFiled = Test.class.getDeclaredField("staticIntField");
            Object o = unsafe.staticFieldBase(staticFiled);
            System.out.println(o == Test.class);
            long b = unsafe.staticFieldOffset(staticFiled);
            unsafe.putInt(o,b,10);
            System.out.println("static Filed is " + Test.staticIntField);
            System.out.println(unsafe.getInt(Test.class,b));

        }
    }

静态变量与实例变量不同之处在于，静态变量位于于方法区中，它的地址偏移值与Test类在方法区的地址相关，与Test类的实例无关。

staticFieldBase: 获取静态变量所属的类在方法区的首地址。可以看到，返回的对象就是Test.class。

staticFieldOffset: 获取静态变量地址偏移值。

**调戏String.intern()**

在jdk7中，String.intern不再拷贝string对象实例，而是保存第一次出现的对象的引用。在下面的代码中，通过Unsafe修改被引用对象s的私有属性value达到间接修改s1的效果！


.. code:: java



    /**
     * @Author wenchaofu
     * @DATE 10:26 2018/6/26
     * @DESC
     */
    public class UnsafeString {
        public static void main(String[] args) throws Exception {
            String s = "aabc"; //保存s的引用
            s.intern(); //此时s1==s，地址相同
            String s1 = "aabc";
            Unsafe u = UnsafeTool.getUnsafeInstance();
            // 获取s的实例变量value
            Field valueInString = String.class.getDeclaredField("value");
            // 获取value的变量偏移值
            long offset = u.objectFieldOffset(valueInString);
            // value本身是一个char[],要修改它元素的值，仍要获取baseOffset和indexScale
            long base = u.arrayBaseOffset(char[].class);
            long scale = u.arrayIndexScale(char[].class);
            System.out.println("scale is " + scale);
            // 获取value
            char[] values = (char[]) u.getObject(s, offset);
            // 为value赋值
            u.putChar(values, base + scale, 'c');
            System.out.println("s:" + s + " s1:" + s1);
            // 将s的值改为abc
                s = "aabc";
            String s2 = "aabc";
            String s3 = "aabc";
            System.out.println("s:" + s + " s1:" + s1 + " s2: " + s2 + " s3: " + s3);
        }
    }


结果如下:

::

    scale is 2
    s:acbc s1:acbc
    s:acbc s1:acbc s2: abc s3: abc


Unsafe类果然不安全！！！