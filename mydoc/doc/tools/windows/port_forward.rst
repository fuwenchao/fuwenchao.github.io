windows配置端口转发规则
==============================

配置
-----

197.168.122.1144:3128

netsh interface portproxy add v4tov4 listenport=1521 connectport=3128 connectaddress=197.168.122.14 protocol=tcp

netsh interface portproxy add v4tov4 listenport=3389 listenaddress=0.0.0.0 connectport=3389 connectaddress=192.168.100.101

::

    解释一下这其中的参数意义
    1.listenaddress -- 等待连接的本地ip地址
    2.listenport -- 本地监听的TCP端口（待转发）
    3.connectaddress -- 被转发端口的本地或者远程主机的ip地址
    4.connectport -- 被转发的端口

查看
------

netsh interface portproxy show all

删除
-------

netsh interface portproxy delete v4tov4 listenport=3340 listenaddress=10.1.1.110


netsh interface portproxy reset

参考
--------

https://blog.csdn.net/fly_hps/article/details/80634072