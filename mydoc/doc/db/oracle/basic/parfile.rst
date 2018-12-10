数据库参数文件
====================


**静态参数**

设置的参数在当前实例不生效，实例重启后生效



**动态参数**

在当前实例立即生效，重启后是否生效视配置而定

参数文件类型
---------------


linux环境下 默认在 $ORACLE_HOME/dbs

windows中 默认在 $ORACLE_HOME/database

RAC环境，建议将参数文件放在共享路径中


**文本类型参数文件**

即pfile参数文件，PFILE参数文件通常有两种命名方式， init$ORACLE_SID.ora 和 init.ora 。 可以使用 操作系统命令直接编辑该文件。



**二进制参数文件**

即SPFILE参数文件，SPFILE通常也有两种命名方式， spfile$ORACLE_SID.ora 和 spfile.ora 。 linux中，可以使用 strings 命令查看spfile文件内容，windows中，可以使用记事本查看文件内容，但是不允许使用操作系统命令来直接编辑该参数文件的内容，否则容易损坏改文件

::

    oracle 8 之前使用pfile启动数据库，缺点是系统无法动态修改参数。 在oracle 9i 之后使用spfile参数文件，可以在数据库运行期间修改多数的数据库参数

    11g中默认使用spfile启动数据库，此时pfile存在，但知识一个模板信息，是不可用的，必须创建一个pfile才可以使用pfile启动数据库，如下所示

    create pfile from spfile

    因为spfile是一个二进制文件，一旦修改就无法使用，所以最好通过spfile创建一个pfile，然后修改参数值，再从pfile创建spfile

    假设一个数据库使用pfile启动，spfile丢失，而pfile文件存储在默认目录下，则可以使用

    create spfile from pfile 

    在spfile的默认位置下创建spfile文件。如果不使用pfile的默认目录，也可以使用如下指令创建spfile

    create spfile from pfile = 'pfile文件名'


**spfile和pfile的相互转化**

::


    create spfile form pfile='path to pfile'
    create spfile from pfile
    create spfile='path/specialspfile.ora' from pfile='path/myinit.ora'
    create spfile='path/myspfile.ora' from pfile

参数设置
--------------

语法

alter system set k=v <comment='text'> <deferred> <scope=both|spfile|memory><sid='sid|*'>

comment='text' 对参数的注释，可以省略。会保存在spfile文件中

deferred 表示对当前会话不立即生效，对以后的会话生效。

scope 表示参数的作用范围

- both表示设置参数在当前实例中生效。并将参数修改后的值保存在spfile中。
- spfile表示参数仅保存在spfile文件中，要重启才能生效；
- memory 表示设置的参数仅作用域当前实例。如果系统使用的是pfile参数文件，那么在设置参数时，只能用memory选项或者不加选项，不加选项默认为memory
- sid 主要用于RAC环境。sid='sid'表示仅作用于指定实例。sid='*'表示参数作用于全部实例


查看参数
---------------

1. show parameter key

2. select value from v$parameter where name='key'

查看参数文件的位置
-------------------------

show parameter spfile


oracle启动实例时使用参数文件的顺序
--------------------------------------

oracle会先使用spfile<sid>.ora文件作为启动参数文件

如果不存在就查找 spfile.ora文件

如果两个文件都不存在

就会使用init<sid>.ora文件


如果上面三个文件都没有则无法启动数据库实例

先通过spfile文件来创建pfile文件

create pfile from spfile

shutdowm immediate

mv spfile<sid>.ora spfile<sid>.ora.bak

startup nomount

show parameter spfile

可以看出使用的是init<sid>.ora参数文件

用指定的pfile启动实例，但是我们无法指定启动实例时使用哪一个spfile
-----------------------------------------------------------------------

startup  pfile='$ORACLE_HOME/dbs/myinit.ora'

当执行没有pfile子句的startup命令，oracle实例会在操作系统的默认位置搜索一个服务器参数文件并从文件中读取初始化参数，如果没有找到服务器参数文件，实例将搜索一个文本初始化参数，如果服务器参数文件存在但你想使用一个文本初始化参数来覆盖那么在执行startup命令时指定pfile子句

举例

1. 启动，自动搜索spfile<sid>.ora
    
    startup

2. 删除spfile文件

    mv spfile<sid>.ora spfile<sid>.ora.bak

3. 再次启动

    startup

4. 查看参数

    show parameter spfile

    发现value字段为空

5. 删除pfile

    mv init<sid>.ora init<sid>.ora.bak

6. 启动

    startup

    报错

7. 启动带pfile

    startup pfile='path/myinit.ora'

    


spfile和pfile启动实例有何不同
-----------------------------------

用spfile启动的实例能把我们用sql做的系统参数修改保存到对应的spfile中，下次再用这个spfile启动实例时，以前的修改仍然有效
而用pfile启动的实例是不能把修改的系统参数写回到init<sid>.ora文件中，所以再次启动时，修改的参数又变回来了