refresh
===============

什么是refresh?
----------------

现在来解决上一篇的遗留问题，这个问题在svn版本中依然存在。Spring Cloud Config分服务端和客户端，服务端负责将git（svn）中存储的配置文件发布成REST接口，客户端可以从服务端REST接口获取配置。但客户端并不能主动感知到配置的变化，从而主动去获取新的配置。客户端如何去主动获取新的配置信息呢，springcloud已经给我们提供了解决方案，每个客户端通过POST方法触发各自的/refresh。

修改spring-cloud-config-client项目已到达可以refresh的功能。


步骤
--------

1. pom.xml增加依赖

.. code:: java

    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>

增加了spring-boot-starter-actuator包，
spring-boot-starter-actuator是一套监控的功能，
可以监控程序在运行时状态，其中就包括/refresh的功能。


2. 开启更新机制

controller类上增加注解

    @RefreshScope

使用该注解的类，会在接到SpringCloud配置中心配置刷新的时候，自动将新的配置更新到该类对应的字段中。