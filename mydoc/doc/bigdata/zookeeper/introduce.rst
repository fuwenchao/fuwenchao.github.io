zookeeper简介
=========================


安装
------------


伪分布式

完全分布式

命令
-----------


**启动** : zkServer.sh start

**查看工作状态** :  zkServer.sh status  、 jps
        


**登录zookeeper节点** : zkCli.sh

**查看集群当前的数据结构** : ls /


**其他命令**

::

 ZooKeeper -server host:port cmd args
        stat path [watch]
        set path data [version]
        ls path [watch]
        delquota [-n|-b] path
        ls2 path [watch]
        setAcl path acl
        setquota -n|-b val path
        history 
        redo cmdno
        printwatches on|off
        delete path [version]
        sync path
        listquota path
        rmr path
        get path [watch]
        create [-s] [-e] path data acl
        addauth scheme auth
        quit 
        getAcl path
        close 
        connect host:port
