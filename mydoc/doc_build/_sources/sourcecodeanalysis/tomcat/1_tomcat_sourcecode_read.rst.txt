tomcat 源码阅读环境搭建
============================


使用到的工具
-----------------

    - tomcat 8.5.30
    - idea 
    - maven

一：下载软件包
---------------

同时下载下载软件包和源码包

**下载地址**
    
::

    //https://tomcat.apache.org/download-80.cgi

    $ pwd
    /c/webApp/tomcat

    wenchaofu@wenchaofu-PC MINGW64 /c/webApp/tomcat
    $ ll
    total 17948
    drwxr-xr-x 1 wenchaofu 197121        0 四月 29 13:57 apache-tomcat-8.5.30/
    -rw-r--r-- 1 wenchaofu 197121 10126906 四月 29 13:56 apache-tomcat-8.5.30.zip
    drwxr-xr-x 1 wenchaofu 197121        0 四月 29 13:57 apache-tomcat-8.5.30-src/
    -rw-r--r-- 1 wenchaofu 197121  8238356 四月 29 13:57 apache-tomcat-8.5.30-src.zip

    wenchaofu@wenchaofu-PC MINGW64 /c/webApp/tomcat

二：生成maven工程
-----------------------

然后进入源码文件夹，新建pom.xml文件

.. code:: java

    <?xml version="1.0" encoding="UTF-8"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

        <modelVersion>4.0.0</modelVersion>
        <groupId>org.apache.tomcat</groupId>
        <artifactId>Tomcat8.5</artifactId>
        <name>Tomcat8.5</name>
        <version>8.5.23</version>

        <dependencies>
            <dependency>
                <groupId>junit</groupId>
                <artifactId>junit</artifactId>
                <version>4.12</version>
                <scope>test</scope>
            </dependency>
            <dependency>
                <groupId>org.easymock</groupId>
                <artifactId>easymock</artifactId>
                <version>3.4</version>
            </dependency>
            <dependency>
                <groupId>org.apache.ant</groupId>
                <artifactId>ant</artifactId>
                <version>1.10.1</version>
            </dependency>
            <dependency>
                <groupId>wsdl4j</groupId>
                <artifactId>wsdl4j</artifactId>
                <version>1.6.2</version>
            </dependency>
            <dependency>
                <groupId>javax.xml</groupId>
                <artifactId>jaxrpc</artifactId>
                <version>1.1</version>
            </dependency>
            <dependency>
                <groupId>org.eclipse.jdt</groupId>
                <artifactId>org.eclipse.jdt.core</artifactId>
                <version>3.13.0</version>
            </dependency>
        </dependencies>

        <build>
            <finalName>Tomcat8.5</finalName>
            <sourceDirectory>java</sourceDirectory>
            <testSourceDirectory>test</testSourceDirectory>
            <resources>
                <resource>
                    <directory>java</directory>
                </resource>
            </resources>
            <testResources>
                <testResource>
                    <directory>test</directory>
                </testResource>
            </testResources>
            <plugins>
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-compiler-plugin</artifactId>
                    <version>2.3</version>
                    <configuration>
                        <encoding>UTF-8</encoding>
                        <source>1.8</source>
                        <target>1.8</target>
                    </configuration>
                </plugin>
            </plugins>
        </build>

    </project>



三：导入项目到IDEA
----------------------


idea --> open --> 代开打开项目

自动下载依赖包

导入后直接build项目会报错，如下：

::

    Error:(29, 36) java: 找不到符号
      符号:   变量 CookieFilter
      位置: 类 util.TestCookieFilter
    Error:(35, 40) java: 找不到符号
      符号:   变量 CookieFilter
      位置: 类 util.TestCookieFilter
    Error:(42, 17) java: 找不到符号
      符号:   变量 CookieFilter
      位置: 类 util.TestCookieFilter

因为我使用的pom包含了test工程，而其中的类TestCookieFilter使用的CookieFilter类找不到，
解决办法就是把整个TestCookieFilter给注释掉好了，研究源码的who会care单元测试的内容。

四：配置启动参数
---------------------

**VM options 增加启动参数**

::

    -Dcatalina.home="C:\webApp\tomcat\apache-tomcat-8.5.30"

找到Bootstrap.java,启动main函数

**为什么配置这个参数**

查看Bootstrap.java 的静态代码块

代码细节不详述，这个静态代码块有个主要的逻辑是JVM加载Bootstrap.class时，会去读取系统设置的catalina.home变量代表的路径作为Tomcat安装路径。如果项目启动时没有设置catalina.home，会把当前路径（即项目所处的路径，对于我们就是tomcat源码文件夹）作为tomcat的安装路径（如果同时没有设置tomcat的工作路径，即catalina.base，那么就会把catalina.base设置为catalina.home，也就是当前路径。home和base的区别，大家可以另行查找文章，都解释的很清楚）。


显然我们的源码路径肯定不是tomcat的安装路径。因为源码文件夹里没有tomcat启动需要的lib/，以及其他配置（conf/、logs/、temp/、webapps/、work/等，这是与catalina.base相关联的）是否和软件版一致。这就是为什么我们在下载源码的同时需要下载tomcat软件包，就是用来给我们启动源码项目时指定catalina.home（顺带间接指定catalina.base）。


**源码之下，一切皆无秘密！**

--------

参考

https://blog.csdn.net/qq_27680317/article/details/78380540