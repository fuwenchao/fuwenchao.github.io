实例和数据库的关系
======================

关系
--------

一般情况下，一个数据库对应一个实例，一个oracle数据库服务器可以同时启动多个实例，每个实例对应一个数据库


一个数据库可以对应多个实例（RAC环境)

一个实例最多只能对应一个数据库

一个实例在它的生命周期内只能被一个数据库打开且只执行一次mount

一个数据库可以被多个实例同时打开使用。这种架构在RAC环境中使用的最为广泛

客户端不能直接访问数据库中的数据（本机直接访问除外），而是通过监听和实例来进行交互。数据库启动的不同阶段需要打开不同的数据库文件。


**oracle server = oracle db + instance**

instance是逻辑的， 由内存和后台进程组成；是临时的，实例中的数据不会被永久保存，所以在数据库或者系统重启之后，实例中的数据将会被清空，这是就需要oracle完善的恢复机制来保证业务数据不会丢失

数据库是物理的，由存储在磁盘中的文件组成，如控制文件，数据文件，日志文件等，所以在数据库或者系统重启之后，保存在数据库中的数据不会丢失


instance = 系统全局SGA + 服务进程 
+ （系统监控进程smon + 进程监控进程pmon + 数据写进程DBWn + 检查点进程CKPT + 日志写进程LGWR + 归档进程ARCn）


DB = 控制文件 + 数据文件 + 在线重做日志文件 + 参数文件 + 密码文件 + 归档日志文件

oracle服务器新增实例
----------------------------

主要分为5个步骤

1. 创建实例目录
2. 创建密码文件
3. 创建参数文件
4. 创建建库脚本并建库
5. 创建数据字典

其中，需要特别注意两点

    1. 目录的权限，即用户和所属的用户组都要是oracle，可以切换到已存在的oracle用户或者以root创建，然后赋权
    2. 创建实例的时候指定编码

下面以oracle 11g为例开始创建

说明：

- 此服务器已经有一个正在运行的oracle实例exdtdb
- 已有一个用户oracle，所属用户组 oinstall
- oracle环境变量 ORACLE_SID=exdtdb ORACLE_BASE=/u01/oracle/app/db
-新建的实例叫mydb


**实例创建步骤**

1. 切换到oracle用户，创建实例目录，这些目录是oracle进程遇到错误或者用户手动trace时或数据存放需要的

::

    su - oracle
    export ORACLE_SID=mydb
    mkdir -p $ORACLE_BASE/admin/mydb/{adump,bdump,cdump,udump,pfile}
    mkdir -p $ORACLE_BASE/oradata/mydb

2. 创建密码文件，文件名为 orapw + sid 此时为 orapwmydb

::

    orapwd file=$ORACLE_HOME/dbs/orapwmydb password=turboblog entries=5 force=y

3. 创建ora参数文件，所在目录和第二步相同。文件规则为 init<sid>.ora 此时为 initmydb.ora。此时将已经存在的参数文件复制修改即可

::

    cp $ORACLE_HOME/dbs/initexdtdb.ora $ORACLE_HOME/dbs/initmydb.ora
    // 注意修改修改复制后里面的 exdtdb
    // db_name=mydb
    // core_dump_dest=$ORACLE_BASE/admin/mydb/cdump
    // background_dump_dest=$ORACLE_BASE/admin/mydb/bdump
    // user_dump_dest=$ORACLE_BASE/admin/mydb/udump
    // control_files=...

4. 创建数据库

建库语句

::

    create database mydb controlfile 
    reuser logfile group 1 ('/u01/oracle/app/db/mydb/redo01.log') size 10M reuse,
    group 2 ('/u01/oracle/app/db/mydb/redo02.log') size 10M reuse,
    group 3 ('/u01/oracle/app/db/mydb/redo03.log') size 10M reuse
    datafile '/u01/oracle/app/db/oradata/mydb/system01.dbf' size 500m
    extent management local
    sysaux datafile '/u01/oracle/app/db/oradata/mydb/sysaux01.dbf' size 120M reuse autoextend on next 10240K maxsize unlimited 
    undo tablespace undotbs3 datafile  '/u01/oracle/app/db/oradata/mydb/undotbs01.dbf' size 500M
    default temporary tablespace temp 
        tempfile '/u01/oracle/app/db/oradata/mydb/temp01.dbf' size 500M
        extent management local uniform size 10M
    noarchivelog
    maxdatafiles 1000
    character set utf8
    national character set utf8
    maxlogfiles 10;


4.1. sqlplus / as sysdba
4.2. shutdown immediate;
4.3. startup nomount pfile=$ORACLE_HOME/dbs/initmydb.ora  // $ORACLE_HOME需要替换
4.4. 执行上面建库语句
4.5. 创建数据字典
::

    @?/rdbms/admin/catalog.sql;
    @?/rdbms/admin/catproc.sql;
    @?/sqlplus/admin/pupbld.sql;

4.6. 启动数据库
::

    exit;
    //以下跳转到linux环境oracle用户
    export ORACLE_SID=mydb
    // 
    sqlpuls / as sysdba
    startup force
    conn system/mangeer;
    select username from all_users;

-----

此时，一个新的oracle实例添加完毕，很多时候我们需要所有的实例都是开机启动的，需要做如下修改：

新增下面一行

::

    vi /etc/oratab

    exdtdb:/u01/oarcle/app/db/product/11.2/db_1:Y
    mydb:/u01/oarcle/app/db/product/11.2/db_1:Y


如果需要远程连接，监听中增加此实例即可。修改lintener.ora

::

    su - oracle
    cd $ORACLE_HOME/network/admin
    vi listener.ora
    (SID_DESC=
        (GLOBAL_DBNAME = mydb)
        (ORACLE_HOME = /u01/oarcle/app/db/product/11.2/db_1)
        (SID_NAME = mydb)
    )

重启oracle就可以连上

服务器重启后，如何启动oracle
---------------------------------------

su - oracle
lsnrctl status
lsnrctl start
export ORACLE_SID=mydb
sqlplus / as sysdba
startup
select status from v$instance


服务器上包含多个库时，listener.ora和tnsnames.ora的配置
----------------------------------------------------------------

1).listener.ora

::

    SID_LIST_LISTENER =
      (SID_LIST =
        (SID_DESC =
          (SID_NAME = PLSExtProc)
          (ORACLE_HOME = D:/oracle/product/10.1.0/Db_1)
          (PROGRAM = extproc)
        )
        (SID_DESC =
          (SID_NAME = orcl)
          (ORACLE_HOME = D:/oracle/product/10.1.0/Db_1)
          (global_dbname = orcl)
        )
        (SID_DESC =
          (SID_NAME = PRACTICE)
          (ORACLE_HOME = D:/oracle/product/10.1.0/Db_1)
          (global_dbname = PRACTICE)
        )
        (SID_DESC =
          (SID_NAME = RCAT)
          (ORACLE_HOME = D:/oracle/product/10.1.0/Db_1)
          (global_dbname = RCAT)
        )
      )

    LISTENER =
      (DESCRIPTION_LIST =
        (DESCRIPTION =
          (ADDRESS_LIST =
            (ADDRESS = (PROTOCOL = TCP)(HOST = doone.pan)(PORT = 1521))
          )
        )
      )



2).tnsnames.ora

::

    orcl =
      (DESCRIPTION =
        (ADDRESS = (PROTOCOL = TCP)(HOST = doone)(PORT = 1521))
        (CONNECT_DATA =
          (SERVER = DEDICATED)
          (SERVICE_NAME = orcl)
        )
      )
    rcat =
      (DESCRIPTION =
        (ADDRESS = (PROTOCOL = TCP)(HOST = doone)(PORT = 1521))
        (CONNECT_DATA =
          (SERVER = DEDICATED)
          (SERVICE_NAME = rcat)
        )
      )
    PRACTICE =
      (DESCRIPTION =
        (ADDRESS = (PROTOCOL = TCP)(HOST = doone)(PORT = 1521))
        (CONNECT_DATA =
          (SERVER = DEDICATED)
          (SERVICE_NAME = PRACTICE)
        )
      )
    EXTPROC_CONNECTION_DATA =
      (DESCRIPTION =
        (ADDRESS_LIST =
          (ADDRESS = (PROTOCOL = IPC)(KEY = EXTPROC))
        )
        (CONNECT_DATA =
          (SID = PLSExtProc)
          (PRESENTATION = RO)
        )
      )



连接时可以用conn user/pwd@orcl,conn user/pwd@ract,conn user/pwd@practice


多库时，不配置上面两文件，可能出现的问题.

::

    a.conn user/pwd 如果直接用这样连接，默认连到最后建的那库上

    b.ORA-01041: internal error. hostdef extension doesn't exist.

      出现这原因，一般是tns没配置对应库说明.　导致库关闭后，重启出现问题

    c.ORA-12170: TNS:Connect timeout occurred

      出现这错误，一般是listener.ora没配置对应库说明

    d.ORA-12514: TNS:listener does not currently know of service requested in connect
    descriptor　

    出现这错误，一般是listener.ora没配置对应库说明




参考
-----

blog.csdn.net/u014397507/article/details/78657542