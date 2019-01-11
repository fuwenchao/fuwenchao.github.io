服务链路追踪 sleuth zipkin
================================

- 新建工程 zipkin-server

1. 入口方法增加注解 @EnableZipkinServer

2. pom.xml增加依赖如下


.. code:: java

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-eureka</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-eureka</artifactId>
        </dependency>

        <dependency>
            <groupId>io.zipkin.java</groupId>
            <artifactId>zipkin-server</artifactId>
        </dependency>

        <dependency>
            <groupId>io.zipkin.java</groupId>
            <artifactId>zipkin-autoconfigure-ui</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>


3. 配置文件applicatioin.yml如下

.. code:: java

    server:
      port: 40000

详见工程

- 新建工程 service-called

1. 配置文件 application.yml如下

.. code:: java

    server:
      port: 2010

    spring:
      application:
        name: service-hello-zipkin
      zipkin:
        base-url: http://localhost:40000

    eureka:
      client:
        serviceUrl:
          defaultZone: http://localhost:8889/eureka/

2. pom.xml文件如下

.. code:: java

    <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-web</artifactId>
            </dependency>

            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-starter-zipkin</artifactId>
            </dependency>
            
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-starter-eureka</artifactId>
            </dependency>

            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-test</artifactId>
                <scope>test</scope>
            </dependency>
    </dependencies>

3. controller调用eureka-client服务

    详见工程


- 使用原工程 eureka-client

    配置步骤同 sercice-called

依次启动 eureka-server -> zipkin-server -> eureka-client -> service-called

查看 zipkin 监控页面 http://localhost:40000

.. image:: ./images/zipkin.png





