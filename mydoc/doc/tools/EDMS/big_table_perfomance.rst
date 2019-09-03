关于外部数据平台的一个大表的优化思路
=======================================


表说明
---------------


表名： UDI_SERVICE_CONSUME_LOG

表用途： 该表为 实时交易日志表，7*24，目前每天 80W 左右的数据量，按月分区，每月数据量大约在 12G

出现问题:

前端界面的交易统计报表使用了该表，交易请求的时候每笔交易都插入了该表，后台数据清理使用了该表
因为该表为START_TIME字段varchar类型时间戳作为分区，但是在使用该表过程中并没有走分区 

::

    TO_CHAR(TO_TIMESTAMP(T.START_TIME, 'yyyy-mm-dd hh24:mi:ss ff3'),'yyyy-mm-dd')

且删除数据的方式为delete

--清除请求日志表三年前的数据

::

    DELETE FROM UDI_SERVICE_CONSUME_LOG T
    WHERE TO_CHAR(TO_TIMESTAMP(T.START_TIME, 'yyyy-mm-dd hh24:mi:ss ff3'),
    'yyyy-mm-dd') <=
    TO_CHAR(ADD_MONTHS(SYSDATE, -36), 'yyyy-mm-dd');


优化思路
---------

1. 将分区改为按日分区

2. 清理数据方式改为 drop 分区方式

3. 查询方式改为 between 方式


具体方案
-----------


1. 将分区改为按日分区

以前的已有分区不变，将之后的分区改为按日，在controlM调度中的init中首先创建一个月后的所有按日分区，按月执行，每月10号


2. 清理数据方式改为 drop 分区方式

动态获取分区，找到24个月前的所有分区并删除，按月执行

3. 查询方式改为 between 方式

- 涉及的存储过程

::

    SP_DEL_LOG_DATA                                     PROCEDURE 2019/4/19 14:34:40
    1. 删除，改写成公共删除程序

    SP_EXT_CONF_BQ_EXE_STATUS                           PROCEDURE 2019/4/19 14:34:44
    1. 将清理逻辑拿出来，放入公共清理程序
    2. 将UDI表增加日期限制，今天和明天 

    SP_EXT_COST_INTERFACE_DETAIL                            PROCEDURE 2019/4/19 14:34:45
    1. 增加日期限制并给为between


    SP_GJJ_CONTRACT_CUST_REQ_INFO                           PROCEDURE 2018/9/3 11:01:47
    1.该存储过程已作废，生产复核目标表是否有数据

    SP_GJJ_CONTRACT_ITEM_REQ_INFO                           PROCEDURE 2018/9/3 11:02:00
    1.该存储过程已作废，生产复核目标表是否有数据

    SP_GJJ_CONTRACT_REQ_INFO                            PROCEDURE 2018/9/3 11:01:30
    SP_GJJ_IDEN_ACCT_REQ_INFO                           PROCEDURE 2018/9/3 11:02:31
    SP_GJJ_PERSON_CUST_REQ_INFO                           PROCEDURE 2018/10/30 14:12:54
    SP_GJJ_PERSON_DTL_REQ_INFO                            PROCEDURE 2018/10/30 14:13:03
    SP_VOLUME_QUERY                           PROCEDURE 2018/11/8 14:29:37




- 涉及的前端查询


- 索引的增加





t1.START_TIME between #{startDate,jdbcType=VARCHAR} and to_char((to_date(#{endDate,jdbcType=VARCHAR},'yyyy-mm-dd')+1),'yyyy-mm-dd')