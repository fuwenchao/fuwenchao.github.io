springcloud 服务注册中心高可用环境搭建
===============================

Eureka是Netflix开源的一款提供服务注册和发现的产品，它提供了完整的Service Registry和Service Discovery实现。也是springcloud体系中最重要最核心的组件之一。

背景介绍
---------

服务中心
************
服务中心又称注册中心，管理各种服务功能包括服务的注册、发现、熔断、负载、降级等，比如dubbo admin后台的各种功能。

有了服务中心调用关系会有什么变化，画几个简图来帮忙理解

项目A调用项目B

正常调用项目A请求项目B

.. image:: ./images/ab.jpg

有了服务中心之后，任何一个服务都不能直接去掉用，都需要通过服务中心来调用

.. image:: ./images/a2b.jpg


项目A调用项目B，项目B在调用项目C

.. image:: ./images/abc.jpg

这时候调用的步骤就会为两步：第一步，项目A首先从服务中心请求项目B服务器，然后项目B在从服务中心请求项目C服务。

.. image:: ./images/a2b2c.jpg


上面的项目只是两三个相互之间的简单调用，但是如果项目超过20个30个呢，在15年底的时候我司分布式的项目就达到了二十几个，画一张图来描述几十个项目之间的相互调用关系全是线条，任何其中的一个项目改动，就会牵连好几个项目跟着重启，巨麻烦而且容易出错。通过服务中心来获取服务你不需要关注你调用的项目IP地址，由几台服务器组成，每次直接去服务中心获取可以使用的服务去调用既可。

由于各种服务都注册到了服务中心，就有了去做很多高级功能条件。比如几台服务提供相同服务来做均衡负载；监控服务器调用成功率来做熔断，移除服务列表中的故障点；监控服务调用时间来对不同的服务器设置不同的权重等等。


EUREKA
***********

Eureka由两个组件组成：Eureka服务器和Eureka客户端。Eureka服务器用作服务注册服务器。Eureka客户端是一个java客户端，用来简化与服务器的交互、作为轮询负载均衡器，并提供服务的故障切换支持。Netflix在其生产环境中使用的是另外的客户端，它提供基于流量、资源利用率以及出错状态的加权负载均衡。

用一张图来认识以下：

.. image:: ./images/eureka-architecture-overview.png

上图简要描述了Eureka的基本架构，由3个角色组成：

1、Eureka Server

提供服务注册和发现

2、Service Provider 服务提供方

将自身服务注册到Eureka，从而使服务消费方能够找到

3、Service Consumer 服务消费方

从Eureka获取注册服务列表，从而能够消费服务



高可用的注册中心
------------------

.. image:: ./images/arch-simple.jpg

刚开始搭建的时候，由于版本号没有对应，废了一写时间！

* spring 1.5.x 对应 springcloud 的 d 和 e 版本
* spring 2.0.x 对应 springcloud 的 f 版本

具体详见官方文档


1. 搭建一个spring boot应用，这里以2.0.0版本为例
*****************************************************

修改pom.xml

.. code::

 <?xml version="1.0" encoding="UTF-8"?>
 <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.spdb.cdu</groupId>
    <artifactId>springcloud</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <packaging>jar</packaging>

    <name>springcloud</name>
    <description>springcloud</description>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.0.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
        <spring-cloud.version>Finchley.M8</spring-cloud.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!-- <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-gateway</artifactId>
        </dependency> -->
        <!-- <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency> -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>
        <!-- <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
        </dependency> -->
        <!-- <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
        </dependency> -->
        <!-- <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency> -->

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

    <repositories>
        <repository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/milestone</url>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
        </repository>
    </repositories>


 </project>


2. 使得项目工程具备EurekaServer注册中心功能
***********************************************

修改主函数，类上面增加注解@EnableEurekaServer

.. code:: java

    @EnableEurekaServer
    @SpringBootApplication
    public class SpringcloudApplication {

        public static void main(String[] args) {
            SpringApplication.run(SpringcloudApplication.class, args);
        }
    }


3. 修改配置文件 
***************

增加两个配置文件
application-peer1.properties
application-peer2.properties

.. code ::

    #application-peer1.properties
    server.port = 11111

    spring.application.name=eureka-service
    eureka.instance.hostname =  peer1
    eureka.client.register-with-eureka = true
    eureka.client.fetch-registry = true
    eureka.client.serviceUrl.defaultZone = http://peer2:11112/eureka/


    #application-peer2.properties
    server.port = 11112

    spring.application.name=eureka-service
    eureka.instance.hostname =  peer2
    eureka.client.register-with-eureka = true
    eureka.client.fetch-registry = true
    eureka.client.serviceUrl.defaultZone = http://peer1:11111/eureka/



4. 修改hosts文件
******************

增加如下行

 | 127.0.0.1 peer1  
 | 127.0.0.1 peer1  





5. 启动服务注册中心
**********************

分别启动两个注册服务中心 

| java -jar springcloud-0.0.1-SNAPSHOT.jar --spring.profiles.active=peer1
| java -jar springcloud-0.0.1-SNAPSHOT.jar --spring.profiles.active=peer2 


--------------------------



高可用的服务提供者
-------------------

1. 修改原springboot 的 pom.xml文件
**************************************

.. code::

 <?xml version="1.0" encoding="UTF-8"?>
 <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.spdb.cdu</groupId>
    <artifactId>spring-server1</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <packaging>jar</packaging>

    <name>spring-server1</name>
    <description>springcloud</description>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.0.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
        <spring-cloud.version>Finchley.M8</spring-cloud.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

    <repositories>
        <repository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/milestone</url>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
        </repository>
    </repositories>


 </project>


2. 修改主入口
*******************

增加 @EnableDiscoveryClient 注解

.. code:: java

    @EnableDiscoveryClient
    @SpringBootApplication
    public class SpringServer1Application {

        public static void main(String[] args) {
            SpringApplication.run(SpringServer1Application.class, args);
        }
    }

3. 发布helloworld服务
***********************

.. code:: java

    @RestController
    public class HelloWorldContrller {
        @Autowired
       private DiscoveryClient client;
        @RequestMapping(value="/hellWorld",method = RequestMethod.GET)
        public String hellWorld(String content) {
            List<ServiceInstance> instanceLst=client.getInstances("eureka-service");
            System.out.println("=====================================");
            for( ServiceInstance s:instanceLst) {
                System.out.println(s.getPort()+":"+s.getHost());
            }
            return "helloWold " +content;
        }
    }



4. 配置application.properties 
**********************************

.. code:: java

    server.port = 22223  
    spring.application.name=eureka-helloWorld  
    eureka.client.serviceUrl.defaultZone = http://peer1:11111/eureka,http://peer1:11112/eureka  




5. 启动服务
**********************

.. code:: java

  java -jar spring-server1-0.0.1-SNAPSHOT.jar --server.port=22223
  java -jar spring-server1-0.0.1-SNAPSHOT.jar --server.port=22222 


6. 访问服务
*************

 | http://localhost:22223/hellWorld?content=123


---------------------



参考：
------

* http://dick1305.iteye.com/blog/2412519
* https://www.cnblogs.com/woshimrf/p/springclout-eureka.html
* 注册中心Eureka(纯洁的微笑) http://www.ityouknow.com/springcloud/2017/05/10/springcloud-eureka.html