JAVA Reflect 反射专题
==========================

Class 类的使用
-------------------

在面向对象的世界里，万事万物皆对象
静态成员，基础数据类型不是对象

类是不是对象呢？

类是对象，类是java.lang.Class的对象

class Foo{}

::

    //Foo的实例对象如何表示呢
    Foo foo = new Foo();
    //Foo这个类也是实例对象，Class类的示例对象，如何表示呢
    //任何一个类都是Class类的实例对象，三种表示方式
    Class c1 = Foo.class
    Class c2 = foo.getClass()
    // 官网上说，c1，c2 表示类Foo类的类类型
    //类也是对象，是Class类的实例对象
    //这个对象我们称为该类的类类型

    c1 == c2 //ture

    Class c3  = Class.forName("me.wenchaofu.test.Test")

我们可以根据类的类类型，创建该类的实例对象

    Foo foo = (Foo)c1.newInstance(); //前提是需要有无参数的构造方法 




java类加载机制
------------------

Class.forName(...)
不仅表示了类的类类型，还代表的动态加载类

**区分编译和运行**

- 编译时刻加载类是静态加载类
- 运行时刻加载类是动态加载类

**静态**

.. code:: java

    public class reflection {
        public static void main(String[] args) {
            if("word".equals(args[0])){
                Word word = new Word();
                word.dosomething();
            }else if("excel".equals(args[0])){
                Excel excel = new Excel();
                excel.dosomething();
            }
        }
    }

编译失败

**思考**: word 一定用吗？ excel一定用吗？

Word 和 Excel 一定要先存在，

new 创建对象 是静态加载类，在编译时刻就需要加载所有可能用到的类


**动态**

.. code:: java

    public class DynamicLoad {
        public static void main(String[] args) {
            try{
                //动态加载类，在运行时刻加载类
                Class c = Class.forName(args[0]);
                //通过类类型，创建该类对象,但是这里我又不知道传进来的是哪个对象，所有不能像下面这种方法用
                //Excel excel =(Excel)c.newInstance();
                //统一标准
                OfficeAble oa = (OfficeAble)c.newInstance();
                oa.dosomething();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

新类实现 OfficeAble 接口就可以了


Class的基本API
-----------------

.. code:: java

    public class ClassUtil {
        public static void printObjectMessage(Object obj){
            Class<?> c = obj.getClass();
            String classNameStr = c.getName();
            System.out.println("类的名称是" + classNameStr);

            /**
             * Method 方法对象
             * getMethods() 获取public方法，包括父类继承而来的
             * getDeclaredMethods() 获取所有方法,但是不包括父类的private方法，包括父类的public方法
             */
            Method[] declaredMethods = c.getDeclaredMethods();
            for (Method declaredMethod : declaredMethods) {
                //得到方法返回值类型的类类型
                Class mc = declaredMethod.getReturnType();
                System.out.println("得到方法的返回值类型: " + mc.getName());
                System.out.print("得到方法的签名是 --" + declaredMethod.getName());
                // 获取参数类型 --> 得到参数列表类型的类类型
                System.out.print("(");
                Class<?>[] parameterTypes = declaredMethod.getParameterTypes();
                for (Class<?> parameterType : parameterTypes) {
                    System.out.print(parameterType.getName() + ",");
                }
                System.out.println(")");

            }

        }
    }




反射的使用实例
---------------------

如何获取方法

方法的名称和参数列表唯一确认一个方法

方法的反射操作

    method.invoke(对象，参数列表)

.. code:: java

    package me.wenchao.javapro.reflection;

    import java.lang.reflect.Method;

    /**
     * @Author wenchaofu
     * @DATE 18:17 2018/4/30
     * @DESC
     */
    public class MethodReflectCase {

        public static void main(String[] args) throws NoSuchMethodException {
            //获取print（int,int)方法
            A a = new A();
            Class<? extends A> aClass = a.getClass();
            try {
                Method m = aClass.getMethod("print", new Class[]{int.class, int.class});
    //            aClass.getMethod("print",int.class,int.class)
                //方法如果没有返回值，返回null，如果有返回具体返回值
                Object o = m.invoke(a, new Object[]{10, 20});
            }catch (Exception e){
                System.out.println("......");
                e.printStackTrace();
            }

        }

        static class A{
            public void print(int a , int  b){
                System.out.println(a+b);

            }
            public void print(String a ,String b){
                System.out.println(a+b );

            }
        }
    }



通过反射了解集合泛型的本质
----------------------------------

.. cdoe:: java

    public class GenericReflact {
        public static void main(String[] args) {
            List list1 = new ArrayList<>();
            List list2 = new ArrayList<String>();
            Class c1 = list1.getClass();
            Class c2 = list2.getClass();
            System.out.println(c1 == c2);
        }
    }

c1 == c2 记过为ture说明编译之后集合的泛型是去泛型化的

java中集合的泛型，是防止错误输入的，只在编译阶段有效

绕过编译就无效了

.. code:: java

    /**
     * @Author wenchaofu
     * @DATE 18:33 2018/4/30
     * @DESC
     */
    public class GenericReflact {
        public static void main(String[] args) {
            List list1 = new ArrayList<>();
            List list2 = new ArrayList<String>();
            Class c1 = list1.getClass();
            Class c2 = list2.getClass();
            System.out.println(c1 == c2);
            try{
                //绕过了编译就绕过了泛型
                Method add = c2.getMethod("add", Object.class);
                Object invoke = add.invoke(list2, 10);

            }catch (Exception e){

            }
            System.out.println(list2.size());
            //这里不能用for String 去遍历，否则抛出异常 String o : list2
            for (Object o : list2) {
                System.out.println("for 遍历" + o);
            }
            System.out.println(list2);
        }
    }


一个ESB报文对象转换的例子

.. code:: java


   /**
     * 递归转换成对象
     * <p/>
     * CompositeData、Field转换成T
     * 不支持继承的属性复制
     *
     * @param atomData 完整报文xml映射的对象
     * @param clz 要解析的其中某一个List的类
     * @param <T>
     * @return
     */
    public static <T> T atomDataToClassByField(AtomData atomData, Class<T> clz) throws InstantiationException, IllegalAccessException, ExecutionException, InvocationTargetException {
        if (atomData == null) {
            return null;
        }
        T t = null;
        if (atomData instanceof CompositeData) {
            t = clz.newInstance();
            CompositeData struct = (CompositeData) atomData;
            Iterator it = struct.iterator();
            Map<String, java.lang.reflect.Field> stringFieldMap = cache.get(clz);
            while (it.hasNext() && stringFieldMap != null) {
                String name = it.next().toString();  // [name APP_HEADER] ...
                AtomData atom = struct.getObject(name);

                java.lang.reflect.Field field = stringFieldMap.get(name);
                if (field == null) {  // 如果找不到，不赋值，进入下次循环
                    continue;
                }
                field.setAccessible(true);
                if (atom instanceof Array) {
                    Type genericType = field.getGenericType();
                    //处理异常
                    if (genericType instanceof ParameterizedType) {  // 泛型类型是否已经实例化，如果不是泛型类型，比如String，则返回false。List<String> 返回true
                        ParameterizedType pt = (ParameterizedType) genericType;
                        Class<?> genericClazz = (Class<?>) pt.getActualTypeArguments()[0];
                        List<T> tList = (List<T>) atomArrayToListByField((Array) atom, genericClazz);
                        field.set(t, tList);
                    }
                } else {
                    Object obj = atomDataToClassByField(atom, field.getType());
                    field.set(t, obj);
                }
            }
        } else if (atomData instanceof Field) {
            return (T) fieldToObj((Field) atomData);
        }
        return t;
    }

    /**
     * 使用 【method】 进行赋值操作
     *
     * @param atomData
     * @param clz
     * @param <T>
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ExecutionException
     */
    public static <T> T atomDataToClass(AtomData atomData, Class<T> clz) throws InstantiationException, IllegalAccessException, ExecutionException, InvocationTargetException {
        if (atomData == null) {
            return null;
        }
        T t = null;
        if (atomData instanceof CompositeData) {
            t = clz.newInstance();
            CompositeData struct = (CompositeData) atomData;
            Iterator it = struct.iterator();
            Map<String, java.lang.reflect.Method> jsonMethonMap = cache2(clz);  // 换成google的缓存 cacheMethod.get(clz);
            while (it.hasNext() && jsonMethonMap != null) {
                String name = it.next().toString();
                AtomData atom = struct.getObject(name);
                java.lang.reflect.Method method = jsonMethonMap.get(name);
                if (method == null) {  // 如果找不到，不赋值，进入下次循环
                    continue;
                }
                method.setAccessible(true);
                Type[] p = null;
                if (atom instanceof Array) {
                    Type[] genericParameterTypes = method.getGenericParameterTypes();
                    Type p0 = genericParameterTypes[0];
                    if (p0 instanceof ParameterizedType) {
                        Class<?> genericClazz = (Class<?>) ((ParameterizedType) p0).getActualTypeArguments()[0];
                        List<T> tList = (List<T>) atomArrayToList((Array) atom, genericClazz);
                        method.invoke(t, tList);
                    }

                } else {
                    Type[] genericParameterTypes = method.getGenericParameterTypes();
                    Type p0 = genericParameterTypes[0];
                    Class<?> genericClazz = null;
                    if (p0 instanceof ParameterizedType) {
                        genericClazz = (Class<?>) ((ParameterizedType) p0).getActualTypeArguments()[0];
                    } else {
                        genericClazz = p0.getClass();
                    }
                    Object obj = atomDataToClass(atom, genericClazz);
                    method.invoke(t, obj);
                }
            }
        } else if (atomData instanceof Field) {
            return (T) fieldToObj((Field) atomData);
        }
        return t;
    }












项目案例
---------------

参照 tomcat 源码的启动类 Bootstrap.java