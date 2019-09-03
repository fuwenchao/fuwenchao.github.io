ODBC连接cloudera hive
=======================


  备注: 使用informatica原生ODBC DRIVER

步骤一: 下载 cloudera hive odbc 驱动
----------------------------------------

注意版本

下载地址

  https://www.cloudera.com/downloads/connectors/hive/odbc/2-5-16.html

测试的时候下载2-5-12版本不通过


步骤二: 安装 cloudera hive odbc 驱动
----------------------------------------


验证是否安装,如果已经安装,请先删除

  rpm -qa|grep -i odbc

  rpm -e filename

**安装**

  sudo yum localinstall ClouderaHiveODBC-2.5.16.1005-1.el6.x86_64.rpm

安装路径在
  
  /opt/cloudera/hiveodbc


步骤三: 修改环境变量
----------------------------------------

vi .bash_profile

  // 增加

  CLOUDERAHIVE=/opt/cloudera/hiveodbc/lib/64

  export LD_LIBRARY_PATH=$CLOUDERAHIVE:$LD_LIBRARY_PATH

  alias ssgtest='/home/infa/Informatica/9.6.1/tools/debugtools/ssgodbc/linux64/ssgodbc.linux64 -d hiveHODS'

source .bash_profile

步骤五: 修改 cloudera hive odbc 配置
----------------------------------------

vi $CLOUDERAHIVE/cloudera.hiveodbc.ini

::


  修改为
  DriverManagerEncoding=UTF-8

  # Generic ODBCInstLib
  #   iODBC
  注释掉下面
  #ODBCInstLib=libiodbcinst.so

  #   SimbaDM / unixODBC
  取消注释下面行
  ODBCInstLib=libodbcinst.so

其他不变

步骤六: 修改 odbc driver 配置
----------------------------------


cd $ODBCHOME

vi odbcinst.ini

::

  [ODBC Drivers] 节点下增加

    Cloudera ODBC Driver for Apache Hive 64-bit=Installed

  增加节点

  [Cloudera ODBC Driver for Apache Hive 64-bit]
  Description=Cloudera ODBC Driver for Apache Hive (64-bit)
  Driver=/opt/cloudera/hiveodbc/lib/64/libclouderahiveodbc64.so


vi odbc.ini

::

  [ODBC Data Sources] 节点下增加

    hiveHODS=Cloudera ODBC Driver for Apache Hive 64-bit

  增加节点

  [hiveHODS]
  # Description: DSN Description.
  # This key is not necessary and is only to give a description of the data source.
  Description=Cloudera ODBC Driver for Apache Hive (64-bit) DSN
  # Driver: The location where the ODBC driver is installed to.
  Driver=/opt/cloudera/hiveodbc/lib/64/libclouderahiveodbc64.so
  # The DriverUnicodeEncoding setting is only used for SimbaDM
  # When set to 1, SimbaDM runs in UTF-16 mode.
  # When set to 2, SimbaDM runs in UTF-8 mode.
  # Values for HOST, PORT, KrbHostFQDN, and KrbServiceName should be set here.
  # They can also be specified on the connection string.
  HOST=111.111.241.71
  PORT=10000
  Schema=default
  UID=hods
  FastSQLPrepare=0
  UseNativeQuery=0
  HiveServerType=3
  AuthMech=2


注意: 

  1. 不能将AuthMech设为0
  2. UID的值不重要
  3. 其他认证方式参照安装文档


步骤七: 命令测试
--------------------


::

  [infa@etl1 ~]$ ssgtest -v
  Connected
  ODBC version        = -03.52.0000-
  DBMS name           = -Hive-
  DBMS version        = -1.1.0-cdh5.16.2-
  Driver name         = -Cloudera ODBC Driver for Apache Hive-
  Driver version      = -2.5.16.1005-
  Driver ODBC version = -03.52-

  Enter SQL string: select 1;

  You are not licensed to use this ODBC driver with the DataDirect ODBC Driver Manager under the license you have purchased.  You can order a license by calling DataDirect Technologies at 800-876-3101 in North America and +44 (0) 1753-218 930 elsewhere.  Thank you for your cooperation.

  Error for thread 0
  {error} STATE=HY000, CODE=0, MSG=[DataDirect][ODBC lib] You are not licensed to use this ODBC driver with the DataDirect ODBC Driver Manager under the license you have purchased.  You can order a license by calling DataDirect Technologies at 800-876-3101 in North America and +44 (0) 1753-218 930 elsewhere.  Thank you for your cooperation.
  Enter SQL string: 

报错不管他,infa中可以通过



步骤八: infa中测试
-----------------------------

w -> 配置 连接

选择odbc

用户密码不重要

参考
----------

`如何在Kerberos的Linux上安装及配置Impala的ODBC驱动`_

.. _`如何在Kerberos的Linux上安装及配置Impala的ODBC驱动`: https://mp.weixin.qq.com/s?__biz=MzI4OTY3MTUyNg==&mid=2247486776&idx=1&sn=66b65d0663f5bd456db0c462bc967888&chksm=ec2add31db5d542756e31b2da064bdca57d899de45a90834d3b900f67dc521a9bf21adbd9509&mpshare=1&scene=1&srcid=07177XdCHropcXOIPEhwgIM8&pass_ticket=GsbxX7JtZFeDaUcTKU3%2B5xyDsZTjuQSMzude6M%2BrABjE3mEq4rERv9DzV5RWSzrd#rd


`powerBI odbc 连接impala实现自主分析`_

.. _`powerBI odbc 连接impala实现自主分析`: https://www.520mwx.com/view/21779

`How To Query Hive and Impala from Oracle using ODBC Heterogeneous Gateway`_

.. _`How To Query Hive and Impala from Oracle using ODBC Heterogeneous Gateway`: https://community.oracle.com/docs/DOC-1002634