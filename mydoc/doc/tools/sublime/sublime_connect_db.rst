sublime连接数据库
========================

mysql
-------

软件开发，其实就是对数据库的操作，那么有一款得心应手的数据库客户端就尤为重要了。如果你使用的是IDE，大多数IDE都集成了操作数据库的功能。但是IDE这种吃内存的怪兽，我是伺候不起，而且IDE一般支持的语言并不多。对于既想要快速、又要能支持多语言，还要高颜值的编辑器，sublime text无疑是极好的选择。
sublime text 体态纤细，身姿优雅，小巧轻盈，插件丰富，绝对是一款让人欲罢不能的性感编辑器。
可如何用sublime text作为mysql的客户端。当然，你可以通过自定义新建“sublime-build”文件来实现，但这种体验很差，显示的结果排版也很不好，而且不方便切换数据库。
我们想要的是一款配置简单，快速切换，排版美观的数据库插件。而这一些 sqltools 均可满足你！



**安装sqltools**

在sublime text命令面板中搜索 sqltools即可找到该插件

https://packagecontrol.io/packages/SQLTools


此外，本机需要安装mysql，并设置mysql的环境变量

**配置数据库连接信息**

打开sqltools的自定义配置文件：SQLToolsConnections.sublime-settings

按照以下格式配置连接信息：

::

    {
        "connections": {
            "a_db": {
                "type"    : "mysql",
                "host"    : "127.0.0.1",
                "port"    : "3306",
                "username": "root",
                "password": "root",
                "database": "mysql",
                "encoding": "utf-8"
            },
            "b_db": {
                "type"    : "mysql",
                "host"    : "127.0.0.1",
                "port"    : "3306",
                "username": "senlong",
                "password": "admin123",
                "database": "mysql",
                "encoding": "utf-8"
            },
        },
        "default": "a_db"
    }


快捷键ctrl+alt+e可进行数据库切换

选择数据库后，键入sql语句，光标定位在sql语句上，快捷键ctrl+e+e，即可显示操作结果


**中文乱码**

如果你执行sql显示的中文是乱码，可以这样解决：

查看mysql的编码

mysql> show variables like "%character%";

如果character_set_client的值不是utf8，在mysql的配置文件中修改：


::

    以windows下的my.ini为例：
    [client]
    default-character-set=utf8

    [mysql]
    default-character-set=utf8

**告诫**


用sublime text直接操作数据库是很爽的体验，但切记进行线上数据库连接时，要将账号权限限制为只查，不然很容易在切换数据库时弄错，要是执行了一个truncate命令，那就准备葛优躺吧...



**快捷键**


::

    Works with PostgreSQL, MySQL, Oracle, MSSQL, SQLite, Vertica, Firebird
    Smart completions (except SQLite)
    Run SQL Queries   CTRL+e, CTRL+e
    View table description   CTRL+e, CTRL+d
    Show table records   CTRL+e, CTRL+s
    Show explain plan for queries   CTRL+e, CTRL+x
    Formatting SQL Queries   CTRL+e, CTRL+b
    View Queries history   CTRL+e, CTRL+h
    Save queries   CTRL+e, CTRL+q
    List and Run saved queries   CTRL+e, CTRL+l
    Remove saved queries   CTRL+e, CTRL+r
    Threading support to prevent lockups
    Query timeout (kill thread if query takes too long)



**参考**

https://www.jianshu.com/p/bd77910e4d2e?from=jiantop.com


http://docs.sublimetext.info/en/latest/reference/completions.html