controlM安装插件
=================

在 etl 服务器（informatica）上安装 controlM 各插件

首先需要安装controlM客户端
---------------------------

::

    解压缩 DRKAI....Z 包
    进入解压路径
    到home路径下全路径安装 setup.sh 
    安装补丁包： DAKAI....BIN

安装infamatica补丁包
------------------------

这样controlM服务器就可以找到这台机器并启动infa任务了

::

    1. 首先关系controlM客户端 shut-ag
    2. 安装包：解压 setup.sh
    3. 安装补丁包

其他同理

start-ag

ctmgetcm  --- controlM server

ctmagcfg  --- etl1





