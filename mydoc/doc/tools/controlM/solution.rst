controlM 问题汇总
========================

版权所有 @wenchaofu

1. controlM 连不通 infa
----------------------------

- 描述

    controlM manager 中 etl1.bocd.com 中的 infa插件 的 INFA_ETL --> Test 失败

- 解决

    1. 登录41 ctmagt/ctmagt
    #. shut-ag
    #. start-ag

    重新测试，如果还是失败的话（可能性较大）

    ps –ef|grep p_ctm 杀掉所有进程

    start-ag

- 如果还是不行的话

    那可能是infa服务没有启动(可以手动执行pmcmd startworkflow命令测试一下)；

按照如下步骤启动

::


    首先登陆web界面

    node2:6007/adnimistartor/#admin/

    服务和节点  -> BOCD_REP_SERVICE -> 操作 -> 再次应用服务 -> 中止



- 如果还是不行

::

    重启 WEB 服务

    cd $INFA_HOME/server/tomcat/bin

    sh infaservice.sh  shutdown
    sh infaservice.sh  startup

- 如果还是不行

    
::

    首先登陆web界面

    node2:6007/administartor/#admin/

    服务和节点  -> Domain_node1 -> web 服务中心
    

2. controlM 接收不到返回信息
----------------------------

- 描述

    在agent上的任务已经跑完,查看日志已经返回success,但是controlM中该任务还是黄色(executing)状态

- 解决

    修改agent与en的通讯端口

::

    1. 登录 agent
    2. 切换用户 su - ctmagt
    3. ctmagcfg 命令
    4. 选择 7)      Advanced parameters
    5. 选择 3)      Tracker Event Port 修改端口号



3. EM重启
--------------

- 什么是EM?

用于客户端的GUI作业开发,通过gateway与server连接,管理与控制所有的server与agent

- 步骤

1. 使用emuser登录jobsched1
2. root_menu
3. 1

4. SERVER的重启
---------------------

调度作业,管理作业执行流程,

- 步骤

1. 使用ctmuser登录jobschd1
2. ctm_menu



.. image:: ./images/controlM_arch.png