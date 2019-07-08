oracle 导入导出命令
=======================

exp导出
-------------


exp gd_base/11@192.168.13.211/oanet file=D:\export\gd_base.dmp log=D:\export\gd_base.log full=y

导出实例：

exp user/password@sid(实例) owner=user file=D:\user.dmp feedback=10000 buffer=10240000

::

    具体：
    owner指的是表的所有者（指明下载的用户名）
    freeback=10000代表显示处理记录条数，缺省为0，即不显示
    buffer=10240000定义了每一次读取导入/导出文件的数据量，设的越大，就越减少imp/exp进程读取数据的次数,从而提高了
    导入/导出效率。（设置缓存区域的大小，当数据满的时候,bind array(结束数组),执行inset（插入）.提交）
    indexes=n 是否下载索引，缺省为n,只是指索引的定义而非数据，exp不下载索引数据

.. image:: ./images/exp.png


实例

::

    -- 全量导出
    exp system/manager@TEST file=d:\daochu.dmp full=y

    -- 将数据库中system用户与sys用户的表导出
    exp system/manager@TEST file=d:\daochu.dmp owner=(system,sys)

    -- 将数据库中的表table1中的字段filed1以"00"打头的数据导出
    exp system/manager@TEST file=d:\daochu.dmp tables=(table1) query=\" where filed1 like '00%'\"

    -- 将数据库中的表table1/table2导出
    exp system/manager@TEST file=d:\daochu.dmp tables=(table1,table2)



expdp导出
------------

- sqlplus sys/sys@192.168.13.211/oanet as sysdba
- create or replace directory dump_dir as 'D:\fzb';
- 在操作系统上创建相应的目录，如在D盘目录下建立文件夹fzb
- 连接数据库执行导出命令

::

    把base库和dbwizard库全部导入

    expdp system/123123@192.168.13.211/oanet   directory=dump_dir dumpfile=XX.dmp   schemas=gd_base,gd_dbwizard;

    把该实例下所有数据库导出

    expdp system/123123@192.168.13.211/oanet   directory=dump_dir dumpfile=XX.dmp   Full=y;



imp导入
--------

imp user/password@sid(实例)  file=D:\user.dmp fromuser=user touser=user rows=y commit=y feedback=10000 buffer=10240000

::

    具体：
    fromuser=user 指明来源用户（就是当前的dmp文件来自user用户下的数据）
    touser=user  指明目的用户（就是把现在dmp文件中的数据导入到目标库user用户下的库）
    rows=y  是否上传表记录（确定导入的数据行）
    commit=y 上传数据缓存区中记录上载后立即执行提交（表示每个数据缓冲满了之后提交一次，而不是导完一张表提交一次。这样会大大减少对系统回滚段等资源的消耗，对顺利完成导入是有益的）
    freeback=10000  显示处理记录条数，缺省为0，即不显示
    buffer=10240000  上载数据缓存区，以字节为单位，缺省依赖操作系统

    indexes=n 指如果上传时索引已建立，此举项即使为n也无效，imp自动更新索引数据


.. image:: ./images/imp.png

ignore: 默认会去创建表，如果表存在的话，将会创建该表，然后报错，ignore=y即为忽略该错误，继续导入数据。默认追加的方式。

DESTROY=Y ： 覆盖已存在的表 默认N


实例

::

    imp system/manager@TEST file=d:\daochu.dmp tables=(table1,table2)


impdp
------


oracle10g之后impdp的table_exists_action参数

table_exists_action选项：

{skip 是如果已存在表，则跳过并处理下一个对象；append是为表增加数据；truncate是截断表，然后为其增加新数据；replace是删除已存在表，重新建表并追加数据}

例：

    impdp user/password directory=dpdir dumpfile=xxx.dmp table_exists_action=replace logfile=xxx.log
    



**oracle impdp导入dmp文件时更改用户及表空间方法**

impdp默认导入expdp的dmp文件时，是需要建立相同名称的表空间及临时表空间的；而且会自动创建相同名称的用户名。

但是有时候我们想更改这种默认设置，这个时候就要用到impdp的特殊参数remap_schema(更改用户名)及remap_tablespace（更改存储表空间）；

假设我们有一个example.dmp文件，原来用户为olduser,存储空间为example，example_temp;

我们需要更改用户名及存储表空间导入到新的库中，只需要按照如下步骤进行：

1、建立新的表空间(假设名称：newtablespace)及临时表空间（假设名称：newtablespace_temp)，语句如下

--表空间

create tablespace newtablespace
logging  
datafile 'D:\app\Administrator\oradata\newtablespace.dbf' 
size 50m  
autoextend on  
next 50m maxsize 20480m
extent management local;  
--临时表空间
create temporary tablespace newtablespace_temp 
tempfile 'D:\app\Administrator\oradata\newtablespace_temp.dbf' 
size 50m  
autoextend on  
next 50m maxsize 20480m  
extent management local;

--注：具体参数及参数值根据实际情况调整。

2、建立用户（此步骤可省略）

create user newuser identified by admin  
default tablespace newtablespace
temporary tablespace newtablespace_temp;

3、导入

在oracle服务器cmd执行如下命令：

impdp system/admin@DNACLIENT directory=DATA_PUMP_DIR dumpfile=example.DMP REMAP_SCHEMA=olduser:newuser  remap_tablespace=EXAMPLE:newtablespace,EXAMPLE_TEMP:newtablespace_temp

注：

1、此处directory使用了系统自带的，如果需要自定义，请使用 create directory命令创建;

2、remap_tablespace多个表空间转换用逗号隔开。

完成以上步骤，通过plsql利用newuser登录数据库，可以查看到newuser下的所有导入的表已转入newtablespace表空间了。

 

mark一下，供以后查看。