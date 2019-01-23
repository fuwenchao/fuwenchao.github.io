HODS 装数遇到的问题
=========================


1. 表被锁

- 具体问题描述

在装载表的过程中，可能有多个任务同时在对某个表进行更新操作，导致表被锁，具体的报错信息如下


::

        
    Logging initialized using configuration in jar:file:/opt/cloudera/parcels/CDH-5.7.0-1.cdh5.7.0.p0.45/jars/hive-common-1.1.0-cdh5.7.\
    0.jar!/hive-log4j.properties
    Unable to acquire IMPLICIT, EXCLUSIVE lock hods@sym_fm_client after 100 attempts.
    FAILED: Error in acquiring locks: Locks on the underlying objects cannot be acquired. retry after some time

    Logging initialized using configuration in jar:file:/opt/cloudera/parcels/CDH-5.7.0-1.cdh5.7.0.p0.45/jars/hive-common-1.1.0-cdh5.7.\
    0.jar!/hive-log4j.properties
    Unable to acquire IMPLICIT, EXCLUSIVE lock hods@sym_fm_client@pm_end_date=9999-12-31 after 100 attempts.
    FAILED: Error in acquiring locks: Locks on the underlying objects cannot be acquired. retry after some time


- 解决方案

::

    show locks table_name;  -- 查看表上是否有锁

    unlock table table_name;  -- 解锁

    unlock table table_name partition(dt='9999-12-31');  -- 解锁某个分区


- 注意

表锁和分区锁是两个不同的锁，对表解锁，对分区是无效的，分区需要单独解锁

**MORE**

::

    SHOW LOCKS <TABLE_NAME>;  
    SHOW LOCKS <TABLE_NAME> extended;  
    SHOW LOCKS <TABLE_NAME> PARTITION (<PARTITION_DESC>);  
    SHOW LOCKS <TABLE_NAME> PARTITION (<PARTITION_DESC>) extended; 

拓展阅读: `Hive锁与并发模型`_

.. _`Hive锁与并发模型` : https://blog.csdn.net/xiao_jun_0820/article/details/53121226