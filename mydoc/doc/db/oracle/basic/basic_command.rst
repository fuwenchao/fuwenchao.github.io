oracle基本命令
================

**切换用户**

::

    首先 登录

    sqlplus user/pwd@IP:port/dbname

    disconn

    conn / as sysdba

**增加用户**

create user etl identified by  etl;

grant create session to zhangsan; //授予zhangsan用户创建session的权限，即登陆权限，允许用户登录数据库 create session = connect

grant select any table to etl; --授予查询任何表

grant select any dictionary to etl;--授予 查询任何字典






**启动和关闭数据库实例**

**启动**

三个步骤：

1. 启动实例

    sys登录 -> startup nomount


2. 加载数据库

    -> alter database mount

3. 打开数据库

    -> alter database open

直接完成第1、2步 ： startup mount

直接完成第1、2、3步 ： startup open 

终止并重启实例 ： startup force


**关闭**

shutdown [normal|immediate|transactional|abort]

- normal 正常管理
- immediate 立即关闭
- transactional 当前所有事务都被提交之后关闭
- abort 以终止方式关闭





