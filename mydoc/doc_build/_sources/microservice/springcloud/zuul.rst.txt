路由网关服务
=============

简介
--------

在Spring Cloud微服务系统中，一种常见的负载均衡方式是，客户端的
请求首先经过负载均衡（zuul、Ngnix），再到达服务网关（zuul集群），
然后再到具体的服。，服务统一注册到高可用的服务注册中心集群，
服务的所有的配置文件由配置服务管理 config_ ，配置服务
的配置文件放在git仓库，方便开发人员随时改配置。

Zuul的主要功能是路由转发和过滤器。路由功能是微服务的一部分，
比如／api/user转发到到user服务，/api/shop转发到到shop服务。
zuul默认和Ribbon结合实现了负载均衡的功能。


架构图
------------

.. image:: ./images/arch-includezuul.png

**注意：A服务和B服务是可以相互调用的，作图的时候忘记了。并且配置服务也是注册到服务注册中心的。**

搭建步骤
----------


- 新建项目 zuul-service
- 主入口中增加注解开启路由服务
    - @EnableZuulProxy
    - @EnableEurekaClient
- 配置application.yml
    .. code:: java

        server:
          port: 30000
        spring:
          application:
            name: zuul-service
        eureka:
          client:
            serviceUrl:
              defaultZone: http://localhost:8889/eureka
        zuul:
          routes:
            api-a:
              path: /api-a/**
              serviceId: ribbon-service
            api-b:
              path: /api-b/**
              serviceId: ribbon-service

- 此时访问 localhost:30000/api-a/hello/sayhello?name=wenchaofu 可以访问到服务

过滤功能
------------

zuul 的filter 实现 ZuulFilter ，不仅能够路由，还能过滤，做一些安全验证

详见 代码_

.. _代码: https://github.com/fuwenchao/myspringclouddemo

.. _config: config.html
