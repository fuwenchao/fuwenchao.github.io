启动顺序
====================


1. eureka-server  查看注册中心是否启动 localhost:8889
#. eureka-client  查看注册中心是否注册有该服务，访问服务 http://localhost:2001/hello/sayhello?name=wenchaofu
#. ribbon-service 查看注册中心首付注册有该服务，访问服务 http://localhost:20000/hi?name=wenchao
#. zuul-service   查看注册中心首付注册有该服务，访问服务 http://localhost:30000/api-a/hi?name=forezp&token=22
#. config-server  查看注册中心首付注册有该服务，使用git
#. config-service 查看注册中心首付注册有该服务，http://localhost:9010/hi?name=wenchaofu
#. spring cloud bus 改造config-client 增加依赖 spring-cloud-starter-bus-amqp 。安装好rabbitMq并启动
   修改git 中的配置文件，刷新 http://localhost:9010/bus/refresh