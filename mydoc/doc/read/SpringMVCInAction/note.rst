第一章：网站架构及其演进过程
=============================


**Session同步问题**

两种处理方式：

1. Session变化后自动同步到其他服务器。如Tomcat使用的就是这种方式

2。 用一个程序统一管理Session。可以使用专门的服务器安装Memcached等搞笑的缓存来统一管理Session，
    然后在应用程序中通过重写Request并覆盖getSession方法来获取制定服务器中的Session



心得： 我们只有真正了解它们所处理的问题才能对它们理解的更加透彻，使用的更加灵活。


第二章：协议和标准
===================


第三章：DNS的设置
===================


第四章：JAVA中的Socket的用法
==============================

NIO  —— BIO


NIO：类比于现在的配送方式

BIO：类比于上学时小生意同学的个体户配送方式


NIO

快递并不会一件一件送，而是很多件一起送，而且在中转站都有专门的分拣员负责按配送范围把货物分给不同的配送员

::

	Buffer  	-- 货物
	Channel 	-- 送货员（或者开往某个区域的配送车）
	Selector 	-- 中转站的分拣员


第五章：自己动手实现HTTP协议
=============================


第六章：详解Servlet
=============================

**tomcat和servlet的关系**

　　　　Tomcat 是Web应用服务器,是一个Servlet/JSP容器. Tomcat 作为Servlet容器,负责处理客户请求,把请求传送给Servlet,并将Servlet的响应传送回给客户.而Servlet是一种运行在支持Java语言的服务器上的组件. Servlet最常见的用途是扩展Java Web服务器功能,提供非常安全的,可移植的,易于使用的CGI替代品.



　Java Servlet API 是Servlet容器(tomcat)和servlet之间的接口，它定义了serlvet的各种方法，还定义了Servlet容器传送给Servlet的对象类，其中最重要的就是ServletRequest和ServletResponse。所以说我们在编写servlet时，需要实现Servlet接口，按照其规范进行操作。

第七章：Tomcat分析
==============================



Server  --> (n) Service  ---> (n)Connector  + (1)Container

Container  -> (1)Engine -> (n)Host -> (n)Context -> (n)Wrapper

Connector: 负责网络连接，Request 和 Response 的转换

Container：用于封装和管理Servlet

Catalina ----->(load/satrt/stop) Server

::

	load： 根据conf/server.xml文件创建Server并调用Server的init方法进行初始化。调用了Server的init
	start: 启动服务器。实际调用Server的start；然后Server的start调用所有Service的start方法；Service中的start方法又会调用所有
			包含的Connectors和Container的start方法
	stop：停止服务器。实际调用Server的stop


	init 和 stop 方法也一样


Bootstrap.main.init -> 初始化ClassLoader -> 创建Catalina （正常情况下启动Tomcat就是调用Bootstrap的main方法）
Bootstrap.main(start) -> bootstarp.setAwait() && bootstrap.load && bootstarp.start ->   catalinaDaemon.start

Bootstrap 的作用类似于一个CatalinaAdapter。这样做的好处是可以吧启动的入口和具体的管理类分开。从而可以很方便的创建出多种启动方式，每种启动方式只需要写一个CatalinaAdapter就可以了。











