BeanDefinitionRegistryPostProcessor实现动态添加到spring容器
---------------------------------------------------------------



.. code:: java

  package com.bocd.spring;

  import com.bocd.model.Person;
  import org.springframework.beans.BeansException;
  import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
  import org.springframework.beans.factory.support.BeanDefinitionBuilder;
  import org.springframework.beans.factory.support.BeanDefinitionRegistry;
  import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
  import org.springframework.stereotype.Component;

  /**
   * @author fuwc
   * @version 1.0, 2019/9/18
   * @desc 即实现postProcessBeanDefinitionRegistry方法，可以修改增加BeanDefinition。
   *       此特性可以用来动态生成bean，比如读取某个配置项，然后根据配置项动态生成bean。
   *       以上postProcessBeanDefinitionRegistry方法中可以通过env来读取配置项，
   *       根据配置项来进行datasoure注册过程，此处代码未实现此种逻辑。
   *       https://www.jianshu.com/p/b4bec64ada70
   *       https://www.cnblogs.com/songfahzun/p/9236656.html
   */
  @Component
  public class MyBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
      @Override
      public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
          for (int i = 0; i < 10; i++) {
              BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(Person.class);
              builder.addPropertyValue("name", "fuwc" + i);
              registry.registerBeanDefinition("person" + i, builder.getBeanDefinition());
          }
      }

      @Override
      public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

      }
  }

**模型**

.. code:: java

  package com.bocd.model;

  /**
   * @author fuwc
   * @version 1.0, 2019/9/18
   */
  public class Person {

      private  String name;

      public String getName() {
          return name;
      }

      public void setName(String name) {
          this.name = name;
      }

      @Override
      public String toString() {
          return "Person{" +
                  "name='" + name + '\'' +
                  '}';
      }
  }


**主类**


.. code:: java


  package com.bocd.boot;

  import com.bocd.model.Person;
  import org.springframework.context.annotation.AnnotationConfigApplicationContext;
  import org.springframework.context.support.ClassPathXmlApplicationContext;

  /**
   * @author fuwc
   * @version 1.0, 2019/9/18
   */
  public class App {
      public static void main(String[] args) {
          AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("com.bocd");
          context.getBeansOfType(Person.class).values().forEach(System.out::println);

          context.close();
  //        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
      }
  }



加载配置文件
--------------


.. code:: java

  package com.bocd.spring;

  import org.springframework.beans.BeansException;
  import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
  import org.springframework.beans.factory.support.BeanDefinitionRegistry;
  import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
  import org.springframework.core.io.ClassPathResource;
  import org.springframework.core.io.Resource;
  import org.springframework.stereotype.Component;

  import java.io.IOException;
  import java.util.Properties;

  /**
   * @author fuwc
   * @version 1.0, 2019/9/18
   * @desc 加载配置文件
   */
  @Component
  public class MyBeanDefinitionRegistryPostProcessor2 implements BeanDefinitionRegistryPostProcessor {
      @Override
      public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
          try {
              initPerson(registry);
          } catch (IOException e) {
              e.printStackTrace();
          }
      }

      private void initPerson(BeanDefinitionRegistry registry) throws IOException {
          Resource resource = new ClassPathResource("person.properties");
          Properties p = new Properties();
          System.out.println("=========");
          p.load(resource.getInputStream());
          p.entrySet().forEach(System.out::println);

      }

      @Override
      public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
          System.out.println("----MyBeanDefinitionRegistryPostProcessor2.postProcessBeanFactory");

      }
  }


resource下面增加配置文件person.properties

::

  fuwe-123
  dfa=323


控制台输出

::

  十月 10, 2019 11:11:16 上午 org.springframework.context.annotation.AnnotationConfigApplicationContext prepareRefresh
  信息: Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@7daf6ecc: startup date [Thu Oct 10 11:11:16 CST 2019]; root of context hierarchy
  =========
  dfa=323
  fuwe-123=
  ----MyBeanDefinitionRegistryPostProcessor2.postProcessBeanFactory
  Person{name='fuwc0'}
  Person{name='fuwc1'}
  Person{name='fuwc2'}
  Person{name='fuwc3'}
  Person{name='fuwc4'}
  Person{name='fuwc5'}
  Person{name='fuwc6'}
  Person{name='fuwc7'}
  Person{name='fuwc8'}
  Person{name='fuwc9'}
  十月 10, 2019 11:11:16 上午 org.springframework.context.annotation.AnnotationConfigApplicationContext doClose
  信息: Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@7daf6ecc: startup date [Thu Oct 10 11:11:16 CST 2019]; root of context hierarchy
