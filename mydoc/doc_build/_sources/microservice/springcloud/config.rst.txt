config
============

简介
--------

    分布式系统中，服务多，方便配置统一管理，实时更新，
    建立分布式配置中心

    - 本地
    - 远程 git/svn

随着线上项目变的日益庞大，每个项目都散落着各种配置文件，如果采用分布式的开发模式，需要的配置文件随着服务增加而不断增多。某一个基础服务信息变更，都会引起一系列的更新和重启，运维苦不堪言也容易出错。配置中心便是解决此类问题的灵丹妙药。

市面上开源的配置中心有很多，BAT每家都出过，360的QConf、淘宝的diamond、百度的disconf都是解决这类问题。国外也有很多开源的配置中心Apache Commons Configuration、owner、cfg4j等等。这些开源的软件以及解决方案都很优秀，但是我最钟爱的却是Spring Cloud Config，因为它功能全面强大，可以无缝的和spring体系相结合，够方便够简单颜值高我喜欢。

Spring Cloud Config是一个解决分布式系统的配置管理方案。它包含了Client和Server两个部分，Server提供配置文件的存储、以接口的形式将配置文件的内容提供出去，Client通过接口获取数据、并依据此数据初始化自己的应用。

其实就是Server端将所有的配置文件服务化，需要配置文件的服务实例去Config Server获取对应的数据。将所有的配置文件统一整理，避免了配置文件碎片化


配置中心搭建 使用git
------------------------


server端
^^^^^^^^^^^^

- 创建项目 config-server
- pom.xml 引入 config-server
- 主入口增加两个注解
    * @EnableConfigServer
    * @EnableEurekaClient
- 配置文件application.yml增加config信息
- 在git上增加配置文件 https://github.com/fuwenchao/springcloudconfig/config-client-dev.properties
  访问远程配置 http://localhost:9000/config-client/dev 返回配置的json信息

client端
^^^^^^^^^^

- 新建项目 config-client
- pom.xml 引入 config
- 加入注册中心
    * @EnableEurekaClient
- bootstrap.yml中配置config server信息
- application.yml 中配置常规信息


配置中心的高可用
-----------------

引入注册中心