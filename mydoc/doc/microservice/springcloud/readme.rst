项目简介
===================



简介
    概括springcloud各组件功能

环境信息
    - springboot 1.5.10
    - springcloud Edgware.SR2

项目代码

        源代码_    





注册中心 与 服务提供者
----------------------------

搭建步骤详见 服务注册中心高可用环境搭建_



服务消费者 rest+ribbon
-------------------------

搭建步骤详见 服务消费者的负载均衡器_


配置中心服务端 config-server
------------------------------

配置中心客户端 config-client
---------------------------------


网关服务 zuul-service
----------------------------



消息总线 spring cloud bus
-------------------------------

搭建步骤详见 消息总线_



服务链路追踪 sleuth zipkin
-----------------------------------

搭建步骤详见 服务链路追踪_




启动顺序
---------------

1. eureka-server  查看注册中心是否启动 localhost:8889
#. eureka-client  查看注册中心是否注册有该服务，访问服务 http://localhost:2001/hello/sayhello?name=wenchaofu
#. ribbon-service 查看注册中心首付注册有该服务，访问服务 http://localhost:20000/hi?name=wenchao
#. zuul-service   查看注册中心首付注册有该服务，访问服务 http://localhost:30000/api-a/hi?name=forezp&token=22
#. config-server  查看注册中心首付注册有该服务，使用git
#. config-service 查看注册中心首付注册有该服务，http://localhost:9010/hi?name=wenchaofu
#. spring cloud bus 改造config-client 增加依赖 spring-cloud-starter-bus-amqp 。安装好rabbitMq并启动
   修改git 中的配置文件，刷新 http://localhost:9010/bus/refresh




------



.. _服务注册中心高可用环境搭建: eureka.html

.. _服务消费者的负载均衡器: ribbon.html

.. _消息总线: bus.html

.. _服务链路追踪: zipkin.html

.. _源代码: https://github.com/fuwenchao/myspringclouddemo

