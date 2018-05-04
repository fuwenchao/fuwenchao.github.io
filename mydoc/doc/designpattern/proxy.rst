代理模式
=============

- 远程代理
- 虚拟代理
- 保护代理
- 智能代理


静态代理
---------------

代理之前确定

**继承方式**

*接口*

.. code:: java

    public interface MoveAble {
        void move();
    }


*目标类*

.. code:: java

    public class Car implements MoveAble{
        public void move(){

            try {
                Thread.sleep(1000);
                System.out.println("汽车行驶中......");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

**静态代理**

.. code:: java

CarPorxy.java

注入 Car

两种方式注入

- 继承类

CarProxy extends Car
调用super.move

- 继承接口

CarProxy implements MoveAble
Car car = new Car()

- 聚合方式

CarProxy.java中

private Car car;
public CarProxy(Car car){
    this.car = car
}



通过继承和聚合都能实现代理，那种方式好呢

聚合比继承更适合代理模式



JDK动态代理
------------------

增加一个时间代理

TimeHandle.java

.. code:: java

    public class TimeHandler implements InvocationHandler {

        private Object target;


        public TimeHandler(Object target) {
            this.target = target;
        }

        /**
         *
         * @param proxy 被代理对象
         * @param method 被代理对象的方法
         * @param args 方法的参数
         * @return
         * @throws Throwable
         */
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("开始代理....");
            method.invoke(target);
            System.out.println("结束代理....");
            return null;
        }
    }

调用代理

.. code:: java

    public class MainApp {
        public static void main(String[] args) throws IllegalAccessException, InstantiationException {
            Car car = new Car();
            InvocationHandler handler = new TimeHandler(car);
            Class<? extends Car> cls = car.getClass();
            MoveAble moveAble = (MoveAble) Proxy.newProxyInstance(cls.getClassLoader(), cls.getInterfaces(), handler);
            moveAble.move();
        }
    }




确定，必须继承接口


cglib动态代理
----------------------

增加被代理类，不实现任何接口

.. code:: java

    public class Train {
        public void move(){
            System.out.println("火车行驶中......");
        }
    }

增加日期处理处理类

.. code:: java


    public class CglibProxy implements MethodInterceptor {

        private Enhancer enhancer = new Enhancer();

        //创建代理类
        public Object getProxy(Class clz){
            enhancer.setSuperclass(clz);
            enhancer.setCallback(this);
            return enhancer.create();

        }


        /**
         * 拦截所有目标类方法的调用
         * @param o 目标类的实例
         * @param method 目标类的方法
         * @param args 方法的参数
         * @param methodProxy 代理类的实例
         * @return
         * @throws Throwable
         */
        @Override
        public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            System.out.println("begin to logging ...");
            // 代理类调用父类的方法
            methodProxy.invokeSuper(o,args);
            System.out.println("end logging.....");
            return null;
        }
    }


调用 

.. code:: java

    public class MainApp {
        public static void main(String[] args) {
            CglibProxy cglibProxy = new CglibProxy();
            Train t = (Train) cglibProxy.getProxy(Train.class);
            t.move();

            CglibProxy cglibProxy1 = new CglibProxy();
            SubTrain st = (SubTrain)cglibProxy1.getProxy(Train.class);
            st.stop();
        }
    }

动态代理的实现思路
--------------------------





https://www.cnblogs.com/best/p/5679656.html