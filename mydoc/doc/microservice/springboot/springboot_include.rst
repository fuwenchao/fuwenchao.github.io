引入springboot的两种方式
-----------------------------


1. pom中继承parent
#. pom中dependencyManager中import springBoot依赖



区别
******

第一种方式可以在properties中设置版本号而不用在dependency中加入依赖。
比如springboot中管理了mysql-connection,需要修改mysql的版本号的时候
直接dependency中加入mysql-connection，可以不加版本号，在properties中
加入mysql.version变量。

但是如果使用dependencyManager的方式引入的话，必须引入mysql的时候加入
版本号
