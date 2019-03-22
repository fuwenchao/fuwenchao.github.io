linux 传输文件到 windows 失败
---------------------------------

报错日志类似如下

::

  root@Docker-Master:/# ssh guchen@10.3.2.35
  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  @ WARNING: REMOTE HOST IDENTIFICATION HAS CHANGED! @
  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  IT IS POSSIBLE THAT SOMEONE IS DOING SOMETHING NASTY!
  Someone could be eavesdropping on you right now (man-in-the-middle attack)!
  It is also possible that a host key has just been changed.
  The fingerprint for the DSA key sent by the remote host is
  SHA256:ahuAO/5uvmSOF1eSUj6p8a3P2q6SvP0/QpNrl0F+Fjg.
  Please contact your system administrator.
  Add correct host key in /root/.ssh/known_hosts to get rid of this message.
  Offending RSA key in /root/.ssh/known_hosts:3
  remove with:
  ssh-keygen -f "/root/.ssh/known_hosts" -R 10.3.2.35
  DSA host key for 10.3.2.35 has changed and you have requested strict checking.
  Host key verification failed.

经过google,出现这个问题的原因是,第一次使用SSH连接时，会生成一个认证，储存在客户端的known_hosts中.


可使用以下指令查看：

  ssh-keygen -l -f ~/.ssh/known_hosts

解决办法

  ssh-keygen -R 服务器端的ip地址