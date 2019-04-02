通过反射动态创建对象
=======================


获取字节码对象的几种方式
--------------------------------

方式一：getClass()

.. code:: java

    public static void getObject_1() {
        Person p=new Person();
        Class<? extends Person> classz=p.getClass();
        System.out.println(classz);
        Person p1=new Person();
        Class<? extends Person> classz1=p.getClass();
        System.out.println(classz1);
        System.out.println(classz==classz1);
    }



方式二： .class

.. code:: java

    public  static void getObject_2() {
    
        Class classz=Person.class;
        Class classz1=Person.class;
        System.out.println(classz==classz1);
    }

方式三： forName()

.. code:: java

    public  static void getObject_3() throws ClassNotFoundException {
         String classname="Reflect.Person";
         Class classz=Class.forName(classname);
         System.out.println(classz);
     }

根据字节码对象初始化对象的几种方式
------------------------------------------

方式一： 调用空参数的构造函数：使用Class类中的newInstance()方法

.. code:: java

  //现在：
  String name = "cn.itcast.bean.Person";
  //找寻该名称类文件，并加载进内存，并产生Class对象。
  Class clazz = Class.forName(name);
  //如何产生该类的对象呢？
  Object obj  = clazz.newInstance();

方式二： 调用带参数的构造函数

先要获取指定参数列表的构造函数对象，然后通过该构造函数的对象的newInstance(实际参数)进行对象的初始化

.. code:: java


    //带参数初始化新创建的对象
    public  static void createNewObject_param() throws Exception {
        String classname="Reflect.Person";
        Class classz=Class.forName(classname);
        Constructor constructor=classz.getConstructor(String.class,int.class);
        constructor.newInstance("Java",30);
    }

方式三： Spring Bean提供的方法

调用：

::

    //org.springframework.web.servlet.FrameworkServlet
    ConfigurableWebApplicationContext wac =
                (ConfigurableWebApplicationContext) BeanUtils.instantiateClass(contextClass);

方法：

.. code:: java

    public static <T> T instantiateClass(Class<T> clazz) throws BeanInstantiationException {
        Assert.notNull(clazz, "Class must not be null");
        if (clazz.isInterface()) {
            throw new BeanInstantiationException(clazz, "Specified class is an interface");
        }
        try {
            return instantiateClass(clazz.getDeclaredConstructor());
        }
        catch (NoSuchMethodException ex) {
            throw new BeanInstantiationException(clazz, "No default constructor found", ex);
        }
    }


    // -------------
    public static <T> T instantiateClass(Constructor<T> ctor, Object... args) throws BeanInstantiationException {
        Assert.notNull(ctor, "Constructor must not be null");
        try {
            ReflectionUtils.makeAccessible(ctor);
            return ctor.newInstance(args);
        }
        catch (InstantiationException ex) {
            throw new BeanInstantiationException(ctor, "Is it an abstract class?", ex);
        }
        catch (IllegalAccessException ex) {
            throw new BeanInstantiationException(ctor, "Is the constructor accessible?", ex);
        }
        catch (IllegalArgumentException ex) {
            throw new BeanInstantiationException(ctor, "Illegal arguments for constructor", ex);
        }
        catch (InvocationTargetException ex) {
            throw new BeanInstantiationException(ctor, "Constructor threw exception", ex.getTargetException());
        }
    }
