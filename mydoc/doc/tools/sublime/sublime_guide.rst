sublime 使用指南
=========================

列模式
--------------

1： 选中 + ctrl + shift + l

2： windows 鼠标右键 + shift 或者 鼠标中键 （增加选择ctrl 减少选择 alt）

3： ctrl + 鼠标左键 多行选中

4： ctrl + alt + 上下键

3： mac 鼠标右键 + option 或者 鼠标中键 （增加 cmd , 减少cmd + shift)



::

    Sublime text 3是一个非常强大的网站编辑工具。 
    这里小云深深的被它的快速编辑多行内容功能所吸引。 
    先说下，使用下面的功能要安装一个叫emmet的插件。


    下面就来看下具体的五种方式吧：
    1，鼠标选中多行，按下 Ctrl Shift L (Command Shift L) 即可同时编辑这些行； 
    2，鼠标选中文本，反复按 CTRL D (Command D) 即可继续向下同时选中下一个相同的文本进行同时编辑； 
    3，鼠标选中文本，按下 Alt F3 (Win) 或 Ctrl Command G(Mac) 即可一次性选择全部的相同文本进行同时编辑； 
    4，Shift 鼠标右键 (Win) 或 Option 鼠标左键 (Mac) 或使用鼠标中键可以用鼠标进行竖向多行选择； 
    5，Ctrl 鼠标左键(Win) 或 Command 鼠标左键(Mac) 可以手动选择同时要编辑。



命令面板
----------

shift + ctrl + p

js/css/sidebar/keybin

快捷键
-----------

key-bindings user 自定义快捷键

如果知道快捷键的命令名 

1. 按快捷键【ctrl + `] 或者 view -> show console

2. sublime.log_commands(True)

3. 打开命令面板 -> 执行命令 就可以看到精确的命令名

sublime 自定制
----------------------


所有自定制的内容保存在 **C:\Users\fuwc\AppData\Roaming\Sublime Text 3\Packages\User** 中，在菜单栏中选择 Preference -> Brows Packages 点击user

包管理器
----------

安装 package control 

install package

插件
--------

- AdvancedNewFile
    
alt + command + n 新建文件

- git

- syncedsidebar

- emmet 代码补齐 不如 HTML+tab

对一段增加一层标签 ： 选中 -> ctrl + shift + p -> warp with abbreviation

删除一级标签： remove tag


goto anything
------------------

ctrl + p

搜索文件

文件：行号

文件@函数名

#查找字符串

查找
-----

本文件 ctrl + f 

本文件 查找替换 ctrl + h



自定制代码片段 snippet
-------------------------

创建 snippet 文件 保存在user中 ; 文件命名格式为 xxxx.sublime-snippet

如


::

    <snippet>
        <content><![CDATA[
    Hello, ${1:this} is a ${2:snippet}.
    ]]></content>
        <!-- Optional: Set a tabTrigger to define how to trigger the snippet -->
        <tabTrigger>hello</tabTrigger>
        <!-- Optional: Set a scope to limit where the snippet will trigger -->
        <!-- <scope>source.python</scope> -->
    </snippet>

    <snippet>
        <content><![CDATA[
    ssssss, ${1:this} is a ${2:snippet}.
    ]]></content>
        <!-- Optional: Set a tabTrigger to define how to trigger the snippet -->
        <tabTrigger>get</tabTrigger>
        <!-- Optional: Set a scope to limit where the snippet will trigger -->
        <!-- <scope>source.python</scope> -->
    </snippet>



然后在文件中输入 hello ,然后输入tab键

- 查看snippet
 
ctrl + shift + p -> snippet


代码补全
-------------

1. snippet
2. API
3. completions files
4. words in buffer  [ctrl+e 自定义]

3的用法

定义补齐文件，保存在user中; 文件格式为　filename.sublime-completions


::

    {
            "scope": "text.html",

            "completions":
            [
                    { "trigger": "a", "contents": "<a href=\"$1\">$0</a>" },
                    { "trigger": "abbr", "contents": "<abbr>$0</abbr>" },
                    { "trigger": "test", "contents": "this ia test" },
                    { "trigger": "acronym", "contents": "<acronym>$0</acronym>" },
                    { "trigger": "script\t<script src=\"...\" />","contents": "<script src=\"$1\" />" },
            ]
    }


自动补全
---------


settings -> auto completion


批处理任务
--------------

menu->tools->build system-> new build system 这里打开一个文件，粘贴下面内容

::

    {
      "cmd": ["/Applications/Google Chrome.app/Contents/MacOS/Google Chrome", "$file"],
      "selector": "text.html"
    }

注意，根据 这里 的说明，selector 生效的前提是 menu->tools-> build system->Automatic 设置为 true 。

保存到 User/ 之下，名字叫 browse.sublime-build 。



------

参考
------

`happypeter`_

`官方文档`_

.. _`官方文档`: http://docs.sublimetext.info/en/latest/reference/completions.html



..  _`happypeter`:http://happypeter.github.io/happysublime




