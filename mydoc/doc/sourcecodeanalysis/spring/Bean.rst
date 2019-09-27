Spring中使用两种Aware接口自定义获取bean
==========================================


在使用spring编程时，常常会遇到想根据bean的名称来获取相应的bean对象
这时候，就可以通过实现BeanFactoryAware来满足需求，代码很简单：

.. code:: java

  @Service
  public class BeanFactoryHelper implements BeanFactoryAware {
      
      private static BeanFactory beanFactory;

      @Override
      public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
          this.beanFactory = beanFactory;
      }
      
      public static Object getBean(String beanName){

  　　　　 if(beanFactory == null){
              throw new NullPointerException("BeanFactory is null!");
          }
  　　　　 return beanFactory.getBean(beanName); 
  　　} 
  }

还有一种方式是实现ApplicationContextAware接口，代码也很简单：

.. code:: java 


  @Service
  public class ApplicationContextHelper implements ApplicationContextAware {
      
      private static ApplicationContext applicationContext;

      @Override
      public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
          this.applicationContext = applicationContext;
      }
      
      public static Object getBean(String beanName){
          if(applicationContext == null){
              throw new NullPointerException("ApplicationContext is null!");
          }
          return applicationContext.getBean(beanName);
      }

  }

上面两种方法，只有容器启动的时候，才会把BeanFactory和ApplicationContext注入到自定义的helper类中，如果在本地junit测试的时候，如果需要根据bean的名称获取bean对象，则可以通过ClassPathXmlApplicationContext来获取一个ApplicationContext，代码如下：

.. code:: java

    @Test
    public void test() throws SQLException {
        //通过从classpath中加载spring-mybatis.xml实现bean的获取
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-mybatis.xml");
        IUserService userService = (IUserService) context.getBean("userService");

        User user = new User();
        user.setName("test");
        user.setAge(20);
        userService.addUser(user);
    }