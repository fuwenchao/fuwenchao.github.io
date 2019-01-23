controlM 问题汇总
========================


**1. controlM 连不通 infa**

- 描述

    controlM manager 中 etl1.bocd.com 中的 infa插件 的 INFA_ETL --> Test 失败

- 解决

    1. 登录41 ctmag/ctmag
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
    