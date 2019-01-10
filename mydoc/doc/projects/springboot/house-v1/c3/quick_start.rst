Mysql House
====================

mysql 安装 8.0.13
-------------------

1. 下载解压

2. 新建my.ini

::

    [mysql]
    # 设置mysql客户端默认字符集
    default-character-set=utf8
    [mysqld]
    # 设置3306端口
    port=3306
    # 设置mysql的安装目录
    #basedir=D:\\dbdata\\mysql
    # 设置 mysql数据库的数据的存放目录，MySQL 8+ 不需要以下配置，系统自己生成即可，否则有可能报错
    # datadir=D:\\develop\\software\\mysql-8.0.13-winx64\\data
    # 允许最大连接数
    max_connections=20
    # 服务端使用的字符集默认为8比特编码的latin1字符集
    character-set-server=utf8
    # 创建新表时将使用的默认存储引擎
    default-storage-engine=INNODB

    #不需要设置data目录，自动生成，否则报错！双斜杠！！！！！！

3. 初始化mysql

**mysqld --initialize --console**

::

    C:\Windows\system32>mysqld --initialize --console
    2018-12-10T09:27:00.082982Z 0 [System] [MY-013169] [Server] D:\mysql-8.0.13\bin\mysqld.exe (mysqld 8.0.13) initializing of server in progress as process 88756
    2018-12-10T09:27:00.094303Z 0 [Warning] [MY-013242] [Server] --character-set-server: 'utf8' is currently an alias for the character set UTF8MB3, but will be an alias for UTF8MB4 in a future release. Please consider using UTF8MB4 in order to be unambiguous.
    2018-12-10T09:27:06.262658Z 5 [Note] [MY-010454] [Server] A temporary password is generated for root@localhost: 3vZ>eeFk9kRo
    2018-12-10T09:27:08.670384Z 0 [System] [MY-013170] [Server] D:\mysql-8.0.13\bin\mysqld.exe (mysqld 8.0.13) initializing of server has completed

    C:\Windows\system32>


4. 注册mysql服务

    mysqld install

5. 启动mysqll

    net start mysql

6. 修改密码并且

    ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '123456';


建库 导入数据
------------------

1. 创建数据库

    create database houses

2. 创建用户
    
    create user etl@localhost identified by 'etl';

3. 授权  #执行报错，未解决

::

    use houses;
    GRANT SELECT,INSERT,UPDATE,DELETE,CREATE,DROP,ALTER ON houses.* TO etl@localhost IDENTIFIED BY 'etl';
    grant select,update,insert,create on houses.* to etl@localhost identified by 'etl';

4. 导入表及数据

::

    mysql -uroot -p123456;
    use houses;
    source xxx.sql


启动应用
------------

1. 将biz的pom.xml的mysql-connector的版本改为 5.1.44

2. 将springboot的版本改为2.1.0（主要是1.4.7依赖的mysql的版本不支持5.8以上版本）。 parent中修改

::

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>2.1.0.RELEASE</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        
        </dependencies>
    </dependencyManagement>

3. common 模块中的 <artifactId>commons-beanutils</artifactId> 增加版本号

    <version>1.9.3</version>

4. 修改jdbc连接

    spring.druid.url=jdbc:mysql://127.0.0.1:3306/houses?serverTimezone=GMT%2B8

5. 启动应用
    
    http://localhost:8091