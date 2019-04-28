infa基本操作
===============



infa服务的日志
-------------------

一般存在路径cd $INFA_HOME/logs/


其中最主要的是catalina.out，node.log，记录了服务挂了的详细信息，节点的相关信息，也可以在web网页上也是可以看到提醒，不过如果挂了自然是打不开的，等服务重启以后在检查历史日志，包含网络问题，资料库和infa应用服务器之间同步的时间差距大，误操作，数据库满了等频率交大的会导致服务挂掉。



后台启动workflow
-----------------------

::

    pmcmd startworkflow -sv BOCD_REP_SERVICE -d Domain_node1 -u Administrator -p admin 
          -f BRDM_INF  -wait -paramfile /mnt/etl/EoD/infa/scripts/conf/brdm/brdm_inf.par WF_BRM_DS_ACCEP_BILL

infa启停服务
------------

::


  停止  $INFA_HOME/server/tomcat/bin/infaservice.sh shutdown

  启动  $INFA_HOME/server/tomcat/bin/infaservice.sh startup