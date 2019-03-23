来客推后台PHP部署步骤
======================


(1)打开终端,输入命令:sudo apachectl -version

(2)输入命令：sudo php -v

（3）cd /private/etc/apache2

httpd.conf

---> LoadModule php .... so 注释放开


重启Apache服务

sudo apachectl restart

如果想要修改部署路径,可以在/private/etc/apache2目录下找到并打开httpd.conf文件,搜索DocumentRoot并修改部署路径

Apache服务端口号

Apache服务端口号默认为80,如果想要修改端口号,可以在/private/etc/apache2目录下找到并打开httpd.conf文件,搜索Listen 80并修改端口号


PHP
-----

PHP的启动只需要在Apache服务中进行一下配置即可直接使用

首先,在/private/etc/apache2目录下找到并打开httpd.conf文件

其次,搜索#LoadModule php5_module libexec/apache2/libphp5.so,将前方的#删除

/**如果因为权限问题不能修改的话，把它拖到桌面修改，然后把原先的删除，之后再把桌面的拖到里面去就ok***///

再次,重启Apache服务即可

最后,我们在路径下新建一个info.php测试程序试试效果吧

::

	<?php 
	phpinfo(); 
	?>


在浏览器中输入如下网址即可查看到PHP的信息

http://localhost/info.php



https://www.cnblogs.com/henusyj-1314/p/6485182.html