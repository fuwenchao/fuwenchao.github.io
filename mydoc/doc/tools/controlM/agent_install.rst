代理安装
=================

- 代理IP

    15.0.18.82

- 创建用户

::

    [root@localhost soft]# groupadd -g 1008 controlm
    [root@localhost soft]# useradd -u 1008 -g controlm -d /home/ctmagt -s /bin/csh ctmagt



- 解压缩

1. mkdir ctm
2. pwd -> /home/ctmagt/ctm
3. tar -zxvf /tmp/soft/DRKAI.9.0.00_Linux-x86_64.tar.Z -C ctm/
4. ll ctm 

::

    drwxr-xr-x 3 ctmagt controlm  4096 6月   9 2015 CheckReq
    drwxr-xr-x 5 ctmagt controlm  4096 6月   9 2015 FORMS
    drwxr-xr-x 3 ctmagt controlm  4096 6月   9 2015 Setup_files
    -r-xr-xr-x 1 ctmagt controlm 12291 7月   6 2014 setup.sh


5. mv ctm soft

6. mkdir ctm;cd ctm;sh ~/soft/setup.sh

::

    在哪个路径下运行setup.sh,就安装在哪个地方

- 安装好之后，


1. ctmagt 重新登录 -> shut-ag


2. root用户运行

::

    cd /home/ctmagt/ctm/scripts/
    ./set_agent_mode  -> 1 开启非root模式

3. ctmagt用户 -> start-ag

4. ctmagt用户 -> ctmagcfg 设置server

::

    [ctmagt@localhost ~]$ ctmagcfg

                    Agent Configuration Utility

    1)      Agent-to-Server Port Number . . . : [7005]
    2)      Server-to-Agent Port Number . . . : [7006]
            For items 3 and 4 do not use IP address
    3)      Primary Control-M/Server Host . . : [ctmserver2.bocd.com.cn]
    4)      Authorized Control-M/Server Hosts : [ctmserver2.bocd.com.cn|ctmserver1.bocd.com.cn]
    5)      Diagnostic Level. . . . . . . . . : [0]
    6)      Comm Trace. . . . . .(0-OFF|1-ON) : [0]
    7)      Advanced parameters

    s)      Save
    q)      Quit



4. 设置编码格式为中文

ctmagt -> ctmagcfg -> 7 -> CJK

ctmagt -> ctmunixcfg -> 7 -> 1 -> UTF-8

4. GUI ccm 中增加 agent





插件安装
--------


插件类型

::

    agent -> KAI
    COB -> Java/Message/web Servive
    INF -> infa
    MQL -> 数据库

解压缩安装包

停止agent

    ctmagt -> shut-ag

sh set_up.sh (会自动探测安装路径)

启动agent

    start-ag


检测环境

    ctmserver -> ctmgetcm


切换SERVER
---------------

1. agent 切换 Server

  ctmagcfg -> 4 -> 1 删除以前server,增加现有server

2. ctmserver -> /etc/hosts 增加agent IP

3. agent -> /etc/hosts/ 增加 ctmserver IP

4. ctmserver -> ctmgetcm


agent 升级
---------------

1. 将升级包  DRKAI.9.0.19.000_Linux-x86_64.tar.Z 放入 em 的 /home/emuser/ctm_em/AUTO_DEPLOY 路径中

2. ccm -> manager -> deployment -> New Activity


