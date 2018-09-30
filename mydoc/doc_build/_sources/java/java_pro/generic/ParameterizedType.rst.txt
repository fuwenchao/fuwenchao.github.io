ParameterizedType应用
=============================

java反射，获取参数化类型的class实例

**ParameterizedType是一个接口，这个类可以用来检验泛型是否被参数化**

比如:

.. code:: java

    class Dao<T>  
    {  
        public Dao(){  
              
        }  
    }  

上面的这个类，也就是泛型类，当有子类集成它的时候，子类也许会将其参数化，当然也可能不进行参数化

比如我们在这样的子类中:

.. code:: java

    class SubDao extends Dao<Student>

    {

    }

这个我们就是在子类集成的时候将Dao<T>这个泛型参数化了

我们在Dao层对数据库进行操作常常需要得到的是实体的class类，也就是上面的Student这个实体类，我们得到其class实例之后就可以利用反射获取到里面的实体类中各种方法和变量，进而我们可以为实体类进行操作

那么如何在Dao层获取到实体类的class实例成为了这一切操作的基础

下面这些方法我们可以获取到class实例，其实应该知道的是当我们在在SubDao层来调用Dao类中的方法时候，其实调用的已经被参数化的Dao，也就是这个方法是Dao<Student>中的，因为我们SubDao既然要调用Dao层的方法当然要继承Dao层，而且在继承的时候进行参数化，换个方式说:

.. code:: java

    class Dao<T>  
    {  
        public Dao()  
        {  
              
        }  
        public T get()  
        {  
            return null;  
        }  

我们要调用上面的get方法，其实我们在StudnetDao里面调用的已经不是上面的Dao<T>中的get方法了，而是 

.. code:: java

    class Dao<Student>  
    {  
        public Dao(){  
              
        }  
        public Student get()  
        {  
              
        }  
    }  

这个Dao<Student>里面的get方法，其实这个类我们是看不到的，因为继承关系，所以在我们子类的构造函数执行的时候就会生成这个类，这样我们只需要写一个泛型化的Dao<T>类，在StudnetDao，CourseDao对其实现继承并且参数化，我们的Dao里面的 方法就可以重复的使用了，进而实现了代码的复用。

我们现在就是遇到的问题更加的清晰了，我们现在需要在Dao<Student>类里面，或者是Dao<T>里面获得实体Student的一个class实例，那么我们需要记性判断，判断什么，在子类继承的时候到底有没有进行参数化

如何判断:有这样的一个方法我们可以获取到当前类的父类

也就是我们在子类中执行获取其父类superclass，判断superclass是Dao<T>还是Dao<Student>就可以知道是不是Dao<T>是不是被参数化了

其实这个地方有一个基础的，需要我们注意就是子类在执行自己的构造方法的时候，我们知道也会执行父类的构造方法，那么在执行父类的构造方法的时候，里面的 this表示的是父类还是自己

    Type superclass=this.getClass().getGenericSuperClass();

这个我们是放在Dao的构造方法中，但是在子类的构造方法执行的时候，调用super(),这个地方的this是指的子类，也就是StudentDao这个类，这样我们就获得了StudentDao的父类superclass

那么我们并不知道superclass到底是指的Dao<T>还是Dao<Student>，在判断superclass是否被参数化之前，我们有必要说一下Type这个接口 API文档中是这样写道:

"Type 是 Java 编程语言中所有类型的公共高级接口。它们包括原始类型、参数化类型、数组类型、类型变量和基本类型。"

不管怎样，Type是一个接口，表示类型，我们的superclass就是一个类型，

下面我们要岁superclass进行判断:

我们判断用到了另外的一个接口，通过判断superclass是否为这个接口的一个实例，来判断superclass是否被实例化

这个接口是:

    ParameterizedType 

如果superclass是该接口的一个实例化对象就表示superclass被参数化了，也就是superclass:Dao<Student>,当然如果不是这个接口的一个实例就表示是没有被参数化，superclass:Dao<T>

我们这样来进行判断:

.. code:: java

    if(superclass instanceof ParameterizedType)

    {

        当Dao<T> 这个泛型类被参数化之后执行。。。。。

    }

我们现在已经知道Dao<T>是否被参数化了，现在我们要做的事情就会获取到T，如何获取

在ParameterizedType中有一个方法:GetActualTypeArguments()这个方法的返回值是一个Type的数组

里面存的就是我们的要的T

我们可以这样创建一个参数化类型的变量:

.. code:: java

    ParameterizedType  parameterizedtype=null;

    parameterizedtype=(ParameterizedType)superclass;

    Type tys[]=parameterizedtype.getActualTypeArguments();

其实这个地方我们就获得到了T（Student）并且存储到了Type的数组中

.. code:: java

if(tys!=null && tys.length>0)

{

    clazz=(Class)type[0];

}

这样我们就获取到了T的类型，并且得到得到了一个class实例，我们下面就可以利用反射获取到里面的方法和属性并操作

，具体代码如下:


.. code:: java

    package me.wenchao.javapro.generic;

    import java.lang.reflect.ParameterizedType;
    import java.lang.reflect.Type;
    import java.sql.PreparedStatement;

    /**
     * @Author wenchaofu
     * @DATE 18:12 2018/5/9
     * @DESC
     */
    public class Dao<T> {
        private static PreparedStatement st = null;
        public Class clazz;

        public Dao() {
            Type superclass = getClass().getGenericSuperclass();
            ParameterizedType parameterizedType = null;
            if (superclass instanceof ParameterizedType) {
                parameterizedType = (ParameterizedType) superclass;
                Type[] typeArray = parameterizedType.getActualTypeArguments();
                if (typeArray != null && typeArray.length > 0) {
                    clazz = (Class) typeArray[0];

                }
            }
        }
    }

**使用**

.. code:: java

    public class UseDao extends Dao<Integer> {
    /*    public UseDao(){
            super();
        }*/

        public static void main(String[] args) {
            UseDao ud = new UseDao();
            System.out.println(ud.clazz);
        }
    }

**再来一个栗子**

.. code:: java

    package me.wenchao.javapro.generic;

    /**
     * @Author wenchaofu
     * @DATE 17:50 2018/5/9
     * @DESC
     */

    import java.lang.reflect.ParameterizedType;
    import java.lang.reflect.Type;

    public class ParamatrizedTypeTest {
        public static void main(String[] args) throws NoSuchMethodException {
            new Sub();
        }
    }

    class Parent {

    }

    class Child extends Parent {

    }

    class UpperGeneric {
        static class Generic<T, R> {
            T t;
            R r;

            protected Generic(T t, R r) {
                this.t = t;
                this.r = r;
            }

            void foo() {
                System.err.println(t + " " + r);
            }
        }
    }

    class Sub extends UpperGeneric.Generic<Child, Integer> {

        protected Sub() {
            super(new Child(), 1);
            Class clazz = getClass().getSuperclass();
            System.err.println("super clazz:" + clazz);

            Type type = getClass().getGenericSuperclass();
            System.err.println("generic super class type:" + type);

            Type trueType = ((ParameterizedType) type).getActualTypeArguments()[0];
            System.err.println("type 0:" + trueType);
            trueType = ((ParameterizedType) type).getActualTypeArguments()[1];
            System.err.println("type 1:" + trueType);

            trueType = ((ParameterizedType) type).getRawType();
            System.err.println("raw type:" + trueType);

            trueType = ((ParameterizedType) type).getOwnerType();
            System.err.println("owner type:" + trueType);
        }
    }
