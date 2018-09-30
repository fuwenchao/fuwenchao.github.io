spring Boot 安装相关
=========================

简介
--------------

首先声明，Spring Boot不是一门新技术，所以不用紧张。从本质上来说，Spring Boot就是Spring,它做了那些没有它你也会去做的Spring Bean配置。它使用“习惯优于配置”（项目中存在大量的配置，此外还内置了一个习惯性的配置，让你无需手动进行配置）的理念让你的项目快速运行起来。使用Spring Boot很容易创建一个独立运行（运行jar,内嵌Servlet容器）、准生产级别的基于Spring框架的项目，使用Spring Boot你可以不用或者只需要很少的Spring配置。

四个核心
^^^^^^^^^^^


    - 自动配置：针对很多Spring应用程序常见的应用功能，Spring Boot能自动提供相关配置
    - 起步依赖：告诉Spring Boot需要什么功能，它就能引入需要的库。
    - 命令行界面：这是Spring Boot的可选特性，借此你只需写代码就能完成完整的应用程序，无需传统项目构建。
    - Actuator：让你能够深入运行中的Spring Boot应用程序，一探究竟。



神奇之处
^^^^^^^^^^^

没有比较就没有伤害，让我们先看看传统Spring MVC开发一个简单的Hello World Web应用程序，你应该做什么，我能想到一些基本的需求。


    - 一个项目结构，其中有一个包含必要依赖的Maven或者Gradle构建文件，最起码要有Spring MVC和Servlet API这些依赖。
    - 一个web.xml文件（或者一个WebApplicationInitializer实现），其中声明了Spring的DispatcherServlet。
    - 一个启动了Spring MVC的Spring配置
    - 一控制器类，以“hello World”相应HTTP请求。
    - 一个用于部署应用程序的Web应用服务器，比如Tomcat。

最让人难以接受的是，这份清单里面只有一个东西是和Hello World功能相关的，即控制器，剩下的都是Spring开发的Web应用程序必需的通用模板。



- 你没有做任何的web.xml配置。
- 你没有做任何的sping mvc的配置; springboot为你做了。
- 你没有配置tomcat ;springboot内嵌tomcat.


接下来看看Spring Boot如何搞定？

很简单，我仅仅只需要非常少的几个配置就可以迅速方便的搭建起来一套web项目



怎么在idea中创建一个SpringBoot工程
-----------------------------------


略



启动springboot 方式
-----------------------

cd到项目主目录:

::

    mvn clean  
    mvn package  编译项目的jar


1. mvn spring-boot:run  启动
2. cd 到target目录，java -jar 项目.jar


springboot在启动的时候为我们注入了哪些bean
-------------------------------------------------


**在程序入口加入：**

.. code:: java

    @SpringBootApplication
    public class SpringbootFirstApplication {

        public static void main(String[] args) {
            SpringApplication.run(SpringbootFirstApplication.class, args);
        }

        @Bean
        public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
            return args -> {

                System.out.println("Let's inspect the beans provided by Spring Boot:");

                String[] beanNames = ctx.getBeanDefinitionNames();
                Arrays.sort(beanNames);
                for (String beanName : beanNames) {
                    System.out.println(beanName);
                }

            };
        }

    }

**在程序启动的时候，springboot自动诸如注入了40-50个bean.**


单元测试
--------------

通过@RunWith() @SpringBootTest开启注解：

.. code:: java

    /**
     * @Author wenchaofu
     * @DATE 9:50 2018/5/10
     * @DESC
     */
    @RunWith(SpringRunner.class)
    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    public class HelloControllerTest {
        
        @Autowired
        TestRestTemplate testRestTemplate;

        private URL base;

        @LocalServerPort
        private int port;
        
        @Before
        public void setup() throws MalformedURLException {
            this.base = new URL("http://localhost:" + port + "/hello/hi");
        }

        @Test
        public void helloTest(){
            ResponseEntity<String> forEntity = testRestTemplate.getForEntity(base.toString(), String.class);
            assertThat(forEntity.getBody(),equalTo("hello wenchaofu"));
        }
    }

运行它会先开启sprigboot工程，然后再测试，测试通过 ^.^



-----

回到 springboot索引页_


源代码： https://github.com/fuwenchao/sprintbootdemo.git




.. _springboot索引页: readme.html

