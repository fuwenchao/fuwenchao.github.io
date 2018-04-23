config
============

简介
--------

    分布式系统中，服务多，方便配置统一管理，实时更新，
    建立分布式配置中心

    - 本地
    - 远程 git/svn



配置中心搭建 config
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