消息总线 spring cloud bus
===============================

改造 config-client

1. pom.xml 中增加依赖

.. code:: java

        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-bus-amqp</artifactId>
        </dependency>


2.  controller 类上增加注解

.. code:: java

    @RefreshScope

没有验证在启动类上增加该注解是否管用


3. 修改配置文件

bootstrap.yml 配置如下

.. code:: java

    spring:
      cloud:
        config:
          profile: dev
          label: master
          name: config-client
          discovery:
            enabled: true
            serviceId: config-server
        bus:
          trace:
            enabled: true
    eureka:
      client:
        serviceUrl:
          defaultZone: http://localhost:8889/eureka

application.yml 配置如下

.. code:: java

    spring:
      application:
        name: config-client
      rabbitmq:
        host: localhost
        port: 5672
        username: guest
        password: guest
    management:
      security:
        enabled: false
    server:
      port: 9010
