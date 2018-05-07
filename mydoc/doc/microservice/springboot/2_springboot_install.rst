spring Boot 安装相关
=========================


怎么在idea中创建一个SpringBoot工程

略


神奇之处
--------------



- 你没有做任何的web.xml配置。
- 你没有做任何的sping mvc的配置; springboot为你做了。
- 你没有配置tomcat ;springboot内嵌tomcat.

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


