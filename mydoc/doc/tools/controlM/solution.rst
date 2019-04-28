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

增补: controlM架构
----------------------

**整体架构**

.. image:: ./images/controlM_arch.png

.. image:: ./images/controlM_arch2.png

**作业定义的数据流向**

.. image:: ./images/job_data_flow.png


**案例**

.. image:: ./images/controlm_case1.png

5.限制并发数
-----------------

并发数限制的地方

1. Quality Resource 的 INFA_RC , DB_XXX_RC
2. INFA CONTAINER中的ETL连接连接配置的高级选项中
3. controlM 中的HOSTs MANAGER,在tools->config manager->hosts manager中配置
4. INFA WEB SERVICE 中连接并发数,在console中设置
5. INFA进程并发数,console中设置

逐级递增

详见 `ControlM调用informatica作业性能分析`_

.. _ControlM调用informatica作业性能分析: ./solution_concurrent_performance_report.html



6. container
-------------------

- 问题描述

::

    - Control-M for Databases job fails to complete successfully
    - Error: "<Failed to create or connect to Container. io exception: expected hessian reply at <> , error code <1> "
    - Control-M for Databases jobs give this kind of message to the proclog:
       0526 000500 32 JOB <Job Name> (ORDERID <Order ID>, RUNNO <Run Count>) FAILED TO SUBMIT.
       Error: Failed to create or connect to Container. io exception: expected hessian reply at <
    - Managing Control-M for Databases Account on Control-M Configuration Manager give the same error
    - Non Control-M for Databases job are running well

- 原因


::

    1. Incorrect installation of Control-M for Databases Fix Pack 2. Control-M for Databases container is not stop properly before fixpack installation or not start properly

- 解决步骤

::

    1. Hold all the jobs that to be submitted to this Control-M/Agent
    2. Wait for all jobs to completed on this Control-M/Agent
    3. Stop the Control-M/Agent
    4. Stop the Control-M for Database container by running: ctmdbcontainer stop
    5. Ensure there is no Control-M/Agent or Control-M for Database processes running
        If found, kill the processes
        For example,
        $ ps aux | grep java | grep ctmdb
       ... ./JRE/bin/java -Xmx512m -XX:+HeapDumpOnOutOfMemoryError -Djavax.net.ssl.trustStore=./data/ctmdbcerts -classpath exe/ctmdb.jar:exe/jars/* com.bmc.cm.Start start 0
    6. Restart the Control-M/Agent
    7. Order the Control-M for Database job to start the Control-M for Database Container
    If the above does not resolve this incident and the Control-M for Databases fixpack is being installed before experiencing this problem:
    1. Hold all the jobs that to be submitted to this Control-M/Agent
    2. Wait for all jobs to completed on this Control-M/Agent
    3. Stop the Control-M/Agent
    4. Stop the Control-M for Database container by running: ctmdbcontainer stop
    5. Ensure there is no Control-M/Agent or Control-M for Database processes running
        If found, kill the processes
        For example,
        $ ps aux | grep java | grep ctmdb
       ... ./JRE/bin/java -Xmx512m -XX:+HeapDumpOnOutOfMemoryError -Djavax.net.ssl.trustStore=./data/ctmdbcerts -classpath exe/ctmdb.jar:exe/jars/* com.bmc.cm.Start start 0
    6. Uninstall the Control-M for Database fixpack by following the uninstall procedures provided in the Release Note of the Control-M for Databases fixpack
    7. Reinstall the Control-M for Database fixpack again by following all steps provided in the Release Note of the Control-M for Databases fixpack
    8. Restart the Control-M/Agent
    9. Order the Control-M for Database job to start the Control-M for Database Container


因调度异常造成任务循环发起到2022年
------------------------------------

-- 将edms的作业 HOLD 住


ctmpsm -UPDATEFOLDER EDMS HODS|FREE...

-- 将edms的作业设为END OK 状态

update public.cmr_ajf set status='Y',state='8' where appgroup ='EDMS' and dailyname = 'UD_EDMS' and odate between '20220101' and '20221231'



注意:

::

    controlM 上线需要双人,检查方向如下

    全局影响:

        1. 确认调度是否有问题,比如这回遇到的问题,调度循环发起,作业跑到2022年
        2. 确认全局变量最好不要定义,如若,请符合规范
        3. 检查是否设置资源限制,否则可能容易导致作业并发太大出现agent与server通讯异常

    局部影响:

        1. 检查作业依赖性
        2. 检查作业运行的HOST是否设置正确
        3. 检查Scheduler


调整controlM参数
-------------------------

- 调整 watchdog  参数

修改配置文件

     $HOME/ctm_server/data/config.dat

调整参数

::

        1. WD_CTMEXIT_1_CMD_LINE -LIMIT "2000 M" -PATH $HOME

            该参数设置WD进程对$HOME空间的自动监控,如果少于2000 M 则告警

        2. WD_CTMEXIT_1_CMD_LINE -LIMIT 50

                该参数设置WD进程对DB使用的自动监控,如果少于50% 则告警,因为超过50% 可能临时空间会满

        3. RUNINF_PURGE_LIMIT 

                默认20,建议调整为10,减少保留的统计信息数量,可以提高NEW DAY 性能

        4. RUNINF_PURGE_MODE

                对统计信息进行清理,缺省保留2天,参数取决IOALOG保留的天数,可以提高NEW DAY 性能

- 调整controlM参数

.. image:: ./images/contorlM_parameter.png


- 数据清理与养护

controlM运行过程中,会产生很多的临时数据,告警数据何警告数据,这些信息累计时间久了,会严重影响controlM的性能

**告警日志的清理**

将7天前的告警日志清理[ctm用户]

        erase_alerts -U <user> -P <password> [-D <date>] [-H <hour>] [-F]

        erase_alerts -U ctem -P xxxx -D 20180404 -H 0000 -F

**condition的清理**[em用户]

        清除时间段内的日期

        ctmcontb -DELETEFROM "*" "20080101" "20081231"



参数设置
----------------

controlM server 参数设置

    ctm_menu 命令

controlM agt 参数设置

    ctmagcfg

日切的作用是什么
--------------------


controlM的安装,HA安装
-----------------------
        


IOALOG
-----------


OOM-1
--------

https://bmcsites.force.com/casemgmt/sc_KnowledgeArticle?sfdcid=kA214000000l7AoCAI&type=Solution

Receive error "JVM OutOfMemory called" during export of database using the UTIL utility in Control-M/Enterprise Manager Server .

::

    1. Backup the $home/ctm_em/ini/EMSiteConfig.ini  of the Control-M/Enterprise Manager server
    Edit the file $home/ctm_em/ini/EMSiteConfig.ini .

    2. Find the below string in the first part of the file.( Add the line if not already present )

        HeapUTIL=XXXX 0

    3. Double the value of the 1st parameter of "HeapUTIL" until the problem is resolved, but make sure first 
    there is enough Real Memory available on the machine, 
    the value is expressed in Mbytes ( 1000 means 1000 Mbytes therefore 1 Gbyte )



    For example:
    AutoIncHeapSize=120
    HeapGTW=160 0
    HeapGSR=300 0
    HeapFRC=300 0
    HeapSLS=300 0
    HeapBIM=300 0
    HeapUTIL=1000 0


OOM-2
--------

Gateway shows disconnected and the Control-M/Server CE process log 
has the error message "Java.lang.OutOfMemoryError: GC overhead limit exceeded"

https://communities.bmc.com/docs/DOC-64890

::

    a) Increase the memory of the machine and assign more memory to CE process.

        For Windows:
        - edit file p_ctmce.bat under <home>\ctm_server\exe and increase the memory assigned to java, 
        by default it´s 512, increase it to 1024 or 2048

        For UNIX:
        - edit $CONTROLM_SERVER/exe*/p_ctmce and add the line "export CTMS_JVM_MAX_HEAP=1024" 
        just above the line beginning with "exec $CONTROLM_SERVER..."
     
    b) Limit the Sysout/output file size that can be viewed from the Control-M/Enterprise Manager 
        to a maximum of 1 mb ( The value is set in KyloBytes)

        Version 7.0.0:
        - Edit file config.dat located in <home>\ctm_server\data and add following keyword:

        SYSOUT_LIMIT_SIZE 1024

        Version 8.0.0 or 9.0.0:
        - Edit file config.dat located in <home>\ctm_server\data and add following keyword:

        OUTPUT_LIMIT_SIZE 1024

    c) Decrease the timeout of the keep alive of the connection of the CE to GTW.

        Update following variable in file config.dat located in <home>\ctm_server\data

        CTM_PRM_KPA_ROUNDTRIP_TIMEOUT 300

        The Control-M/Server will need to be recycled for the above changes to take effect.


CA process does not startup
------------------------------------

Control-M/Server configuration agent (CA process) does not startup.  
Messages similar to those below can be found in the CA log in the Control-M/Server's proclog directory. 

https://communities.bmc.com/docs/DOC-67890

CAUSE:

::

    The problem is that the waiting time has elapsed, before all the threads have come up.

(1)Please perform the following steps and verify it solves the problem:   

::

    1. Stop the CA process, via the command  "shut_ca".   
    2. Open the config.dat file, located under the Control-M/Server Home directory <CTMHOME>/data directory (If it is a HA - high availability environment, do this change at active node).   
    3. Add the following line to the file:   
    THREAD_STATE_RETRY_SLEEP 1000   
    4. Save the file.   
    5. Restart the CA process, using the command "start_ca" 
      
    6. if the CA process starts and stays running, the problem is resolved. 
  

(2)If the above steps do not resolve the problem, please perform the below to collect special debug information and open an issue with BMC Support:   

::

    1. Stop the CA process, via the command  "shut_ca"   
    2. Make a backup copy of the config.dat file, located in the Control-M/Server's home directory <CTMHOME>/data 
      
    3. Open the config.dat file, located in the Control-M/Server's home directory <CTMHOME>/data   
    4. Add the following lines:   
        CTM_CONFIG_AGENT_DEBUG_LEVEL 4   
        CTM_CONFIG_AGENT_MODULE_LEVEL 0   
    5. Save the file.   
    6. Re-start the CA process, via the command "start_ca" 
      
    7. Disable debug by modifying the config.dat file and removing the two lines added in step 4 above. 
      
        
  
  This will force the CA to run in special debug mode.  When the problem reoccurs, please open a new issue with BMC Support and provide the following information: 
  
  1. From The 'proclog' sub-directory of the Control-M/Server proclog directory, all files that start with CA.  
  
  2. The config.dat file from the Control-M/Server's home directory <CTMHOME>/data 
  
    
  
大量调度发起的解决方案
--------------------------

问题描述:

::

    因EDMS系统循环发起调度,导致大量调度循环发起(到2022年底),有40多万个作业待执行,导致7点时日切无法发起

解决方法

::
    
    整个日切分为三个阶段
    1.  FORMATTING AJF
    2.  DOWNLOADING
    3.  CLEAN JOBS

    第一阶段时的解决方法 详见 OOM-2 小节  
    第二阶段的解决方法   详见 OOM-1 小节
    第三阶段的解决方法
        1. 增加超时时间设置 (system parameter -> new day参数的 ctm_db_timeout)
        2. 增加数据库执行SQL的超时设置




ctmkilljob
-----------------


http://www.scheduler-usage.com/viewtopic.php?t=262

https://communities.bmc.com/thread/142784

::

    ctmkilljob -ORDERID $1 -JOBNAME $2 & (unix example) 

    > > > ctmkilljob
    > > > [ -ORDERID <uniqueOrderID>
    > > > [ -NODEID <name> ]
    > > > [ -MEMLIB <path> ]
    > > > [ -MEMNAME <filename> ]
    > > > [ -JOBNAME <name> ]