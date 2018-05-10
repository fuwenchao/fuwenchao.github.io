配置文件
===============

Value注入
---------------

当我们创建一个springboot项目的时候，系统默认会为我们在src/main/java/resources目录下创建一个application.properties。个人习惯，我会将application.properties改为application.yml文件，两种文件格式都支持。

在application.yml自定义一组属性：

.. code:: java

    server:
      port: 8081
    girl:
      cupSize: B
      age: 18


或者

.. code:: java

    my:
     name: forezp
     age: 12
     number:  ${random.int}
     uuid : ${random.uuid}
     max: ${random.int(10)}
     value: ${random.value}
     greeting: hi,i'm  ${my.name}


如果你需要读取配置文件的值只需要加@Value(“${属性名}”)：

.. code:: java

    @RestController
    public class MiyaController {

        @Value("${girl.cupSize}")
        private String cupSize;

        @Value("${my.age}")
        private int age;

        @RequestMapping(value = "/miya")
        public String miya(){
            return cupSize+":"+age;
        }

    }

将配置文件的属性赋给实体类
------------------------------

.. code:: java

    @Component
    @ConfigurationProperties(prefix = "girl")  //获取前缀是girl的配置
    public class GirlProperties {
        private String cupSize;  //需与配置文件中名字一直，这样就不需要每个属性属用到@value注解了
        private Integer age;

        public String getCupSize() {
            return cupSize;
        }

        public void setCupSize(String cupSize) {
            this.cupSize = cupSize;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }


另一个

.. code:: java

    ConfigurationProperties(prefix = "my")
    @Component
    public class ConfigBean {

        private String name;
        private int age;
        private int number;
        private String uuid;
        private int max;
        private String value;
        private String greeting;

        省略了getter setter....


另外@Component可加可不加

另外spring-boot-configuration-processor依赖可加可不加，具体原因不详。

.. code:: java

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
            <optional>true</optional>
        </dependency>

另外需要在应用类或者application类，加EnableConfigurationProperties注解。[经验证，可以不加]

.. code:: java

    @RestController
    @EnableConfigurationProperties({ConfigBean.class})
    public class LucyController {
        @Autowired
        ConfigBean configBean;

        @RequestMapping(value = "/lucy")
        public String miya(){
            return configBean.getGreeting()+" >>>>"+configBean.getName()+" >>>>"+ configBean.getUuid()+" >>>>"+configBean.getMax();
        }

.. code:: java

    @RestController
    public class GirlController {

        @Autowired
        private GirlRepository girlRepository;


自定义配置文件
-------------------

上面介绍的是我们都把配置文件写到application.yml中。有时我们不愿意把配置都写到application配置文件中，这时需要我们自定义配置文件，比如test.properties:

::

    com.forezp.name=wenchaofu
    com.forezp.age=12

怎么将这个配置文件信息赋予给一个javabean呢？

.. code:: java

    @Configuration
    @PropertySource(value = "classpath:test.properties")
    @ConfigurationProperties(prefix = "com.forezp")
    public class User {
        private String name;
        private int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }


在最新版本的springboot，需要加这三个注解。

- @Configuration
- @PropertySource(value = “classpath:test.properties”)
- @ConfigurationProperties(prefix = “com.forezp”);

这里要注意哦，有一个问题，如果你使用的是1.5以前的版本，那么可以通过locations指定properties文件的位置，这样：

::

    @ConfigurationProperties(prefix = "config2",locations="classpath:test.properties")

但是1.5版本后就没有这个属性了，找了半天发现添加@Configuration和@PropertySource(“classpath:test.properties”)后才可以读取。


.. code:: java

    @RestController
    @EnableConfigurationProperties({ConfigBean.class,User.class})
    public class LucyController {
        @Autowired
        ConfigBean configBean;

        @RequestMapping(value = "/lucy")
        public String miya(){
            return configBean.getGreeting()+" >>>>"+configBean.getName()+" >>>>"+ configBean.getUuid()+" >>>>"+configBean.getMax();
        }

        @Autowired
        User user;
        @RequestMapping(value = "/user")
        public String user(){
            return user.getName()+user.getAge();
        }

    }


参数间引用
---------------

在application.properties中的各个参数之间也可以直接引用来使用，就像下面的设置：

.. code:: java

com.dudu.name="wenchaofu"
com.dudu.want="happy"
com.dudu.yearhope=${com.dudu.name}hahah${com.dudu.want}


随机值配置
-------------------

配置文件中${random} 可以用来生成各种不同类型的随机值，从而简化了代码生成的麻烦，例如 生成 int 值、long 值或者 string 字符串。

.. code:: java

    wen.secret=${random.value}
    wen.number=${random.int}
    wen.bignumber=${random.long}
    wen.uuid=${random.uuid}
    wen.number.less.than.ten=${random.int(10)}
    wen.number.in.range=${random.int[1024,65536]}



多个环境配置文件
----------------------

在现实的开发环境中，我们需要不同的配置环境；格式为application-{profile}.properties，其中{profile}对应你的环境标识，比如：

    - application-test.properties：测试环境
    - application-dev.properties：开发环境
    - application-prod.properties：生产环境

怎么使用？只需要我们在application.yml中加：

.. code:: java

    spring:
      profiles:
        active: dev

当然你也可以用命令行启动的时候带上参数：

    java -jar xxx.jar --spring.profiles.active=dev


除了可以用profile的配置文件来分区配置我们的环境变量，在代码里，我们还可以直接用@Profile注解来进行配置，例如数据库配置，这里我们先定义一个接口

.. code:: java

    public  interface DBConnector { public  void  configure(); }

.. code:: java

    /**
      * 测试数据库
      */
    @Component
    @Profile("testdb")
    public class TestDBConnector implements DBConnector {
        @Override
        public void configure() {
            System.out.println("testdb");
        }
    }
    /**
     * 生产数据库
     */
    @Component
    @Profile("devdb")
    public class DevDBConnector implements DBConnector {
        @Override
        public void configure() {
            System.out.println("devdb");
        }
    }

通过在配置文件激活具体使用哪个实现类

    spring.profiles.active=testdb

然后就可以这么用了

.. code:: java

    @RestController
    @RequestMapping("/task")
    public class TaskController {

        @Autowired DBConnector connector ;

        @RequestMapping(value = {"/",""})
        public String hellTask(){

            connector.configure(); //最终打印testdb     
            return "hello task !! myage is " + myage;
        }
    }

除了spring.profiles.active来激活一个或者多个profile之外，还可以用spring.profiles.include来叠加profile

::

    spring.profiles.active: testdb  
    spring.profiles.include: proddb,prodmq

外部配置-命令行参数配置
-----------------------------

Spring Boot是基于jar包运行的，打成jar包的程序可以直接通过下面命令运行：

    java -jar xx.jar

可以以下命令修改tomcat端口号：

    java -jar xx.jar --server.port=9090

可以看出，命令行中连续的两个减号--就是对application.properties中的属性值进行赋值的标识。
所以java -jar xx.jar --server.port=9090等价于在application.properties中添加属性server.port=9090。
如果你怕命令行有风险，可以使用SpringApplication.setAddCommandLineProperties(false)禁用它。

实际上，Spring Boot应用程序有多种设置途径，Spring Boot能从多重属性源获得属性，包括如下几种：



    - 根目录下的开发工具全局设置属性(当开发工具激活时为~/.spring-boot-devtools.properties)。
    - 测试中的@TestPropertySource注解。
    - 测试中的@SpringBootTest#properties注解特性。
    - 命令行参数
    - SPRING_APPLICATION_JSON中的属性(环境变量或系统属性中的内联JSON嵌入)。
    - ServletConfig初始化参数。
    - ServletContext初始化参数。
    - java:comp/env里的JNDI属性
    - JVM系统属性
    - 操作系统环境变量
    - 随机生成的带random.* 前缀的属性（在设置其他属性时，可以应用他们，比如${random.long}）
    - 应用程序以外的application.properties或者appliaction.yml文件
    - 打包在应用程序内的application.properties或者appliaction.yml文件
    - 通过@PropertySource标注的属性源
    - 默认属性(通过SpringApplication.setDefaultProperties指定).

这里列表按组优先级排序高到低，也就是说，任何在高优先级属性源里设置的属性都会覆盖低优先级的相同属性，列如我们上面提到的命令行属性就覆盖了application.properties的属性。

配置文件的优先级
-------------------

application.properties和application.yml文件可以放在以下四个位置：


    - 外置，在相对于应用程序运行目录的/congfig子目录里。
    - 外置，在应用程序运行的目录里
    - 内置，在config包内
    - 内置，在Classpath根目录

同样，这个列表按照优先级排序，也就是说，src/main/resources/config下application.properties覆盖src/main/resources下application.properties中相同的属性，如图：

.. image:: ./images/properties-1.jpg


此外，如果你在相同优先级位置同时有application.properties和application.yml，那么application.properties里的属性里面的属性就会覆盖application.yml。



-----

**参考**

- http://tengj.top/2017/02/28/springboot2/



回到 springboot索引页_


源代码： https://github.com/fuwenchao/sprintbootdemo.git




.. _springboot索引页: readme.html