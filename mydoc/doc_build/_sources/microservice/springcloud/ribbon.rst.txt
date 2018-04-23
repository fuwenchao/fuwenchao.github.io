服务消费者的负载均衡器
=========================

1. 新建工程
-----------------------------

    ribbon-service

 application.yml如下

.. code:: java

    server:
      port: 20000
    spring:
      application:
        name: ribbon-service
    eureka:
      client:
        serviceUrl:
          defaultZone: http://localhost:8889/eureka/

2. pom.xml 如下
-------------------------

.. code:: java

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-eureka</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-ribbon</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>  <!-- 下一步演示断路器的时候会用到，这里没有用-->
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-hystrix</artifactId>
        </dependency>
    <!--    <dependency>
            <groupId>com.netflix.hystrix</groupId>
            <artifactId>hystrix-javanica</artifactId>
        </dependency>-->
    </dependencies>

3. 向服务中心注册
-------------------------

    在入口类中增加 @EnableDiscoveryClient

4. 开启负载均衡
-----------------------

在主函数中增加

.. code:: java

    @Bean
    @LoadBalanced
    RestTemplate restTemplate(){
        return new RestTemplate();
    }


并且向程序的ioc注入一个bean: restTemplate;
并通过@LoadBalanced注解表明这个 restRemplate
开启负载均衡的功能。

5. 调用服务
-----------------

.. code:: java

    public String sayHello(String name){
        return restTemplate.getForObject("http://EUREKA-CLIENT-SERVICE-PROVIDER/hello/sayhello?name="+name,String.class);
    }

注意 EUREKA-CLIENT-SERVICE-PROVIDER 是向服务注册中心注册的服务名，
如果注册了多个服务，
会采取某种策略的负载均衡，ribbon默认轮询



6. 此时架构图如下
-----------------------

网下找的，端口不一样

.. image:: ./images/arch-ribbon.png

-----

7. 具体参考 代码_
-----------------------







.. _代码: https://github.com/fuwenchao/myspringclouddemo
