慕课网idea教程笔记
========================

二：高效定位代码
----------------------

**2.1：无处不在的跳转**

::

    项目之间的跳转 alt+ctrl+【  、】
    文件之间的跳转  
    最近使用的文件 ctrl+E
    recent file
    recent change file
    last edit location

**利用书签进行跳转**

::

    f11 或者 ctrl + f11(带标记的跳转)
    如果有标记的话 【ctrl + 标记号】进行跳转

**收藏(类和函数)**

::

    ctrl + shift + a --> add to favorites --> 找到快捷键 alt + shift + f

**插件的跳转功能**

::

    emacsideas
    ctrl + w 

**2.2：精准搜索**


- 搜索类 ctrl + n
- 搜索文件 ctrl + shift + n
- 搜索方法 ctrl + shift + alt + n


三：代码小助手们
------------------------

::

    3.1 ： 列操作 edit -> find -> select all occurrence -> ctrl + shift + alt + j  ； 2 。 鼠标中键
    3.2 : 跳转到错误位置 f12 / shift f12
    3.2 ： live templates

**自动补全**


postfix

::

    100.fori

3.4：alter+enter 

::

    自动创建函数
    list replace
    format
    实现接口
    单词拼写
    导入包

四：编写高质量代码
-------------------------

- 重构
- 抽取

五：寻找修改轨迹
----------------------------


**git集成**

::

    上下一个修改的地方 privious change 【 ctrl+ shift + alt + 上/下 】

local history

六：关联一切
-------------------------

- 关联spring
- 与数据库关联

七：断点
-------------------

run --> ... 里面查找

::

    查看所有断点 ctrl + shift + f8
    增加/取消 断点 ctrl + f8
    禁止所有断点  mute breakpoint
    条件断点： 现在某一行打一个断点 ，然后 ctrl + shift + f8 增加条件
    表达式求值： alt + f8
    运行到光标行 alt + f9
    set value: 改变运行过程中的值 ,在调试窗口 ，选中变量 f2

八 ： run
-----------------

::

    运行当前类 ctrl + shift + f10  (run context config)  --- debug : ctrl + shift + f9
    运行上一次运行的类 shift + f10   -- debug : shift + f9

    选择run那个类 shift + alt + f10 --- debug shift + alt + f9

九 文件操作
---------------------

在当前文件夹中新建 other --> New ...
改为 ctrl + alt + n

复制当前文件 f5

启动当前文件 f6

十：文本操作
---------------------
