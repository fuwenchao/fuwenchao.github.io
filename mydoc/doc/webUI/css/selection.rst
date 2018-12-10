selection
================

CSS 组合选择符
-----------------


-    后代选取器(以空格分隔) 
-    子元素选择器(以大于号分隔）
-    相邻兄弟选择器（以加号分隔）
-    普通兄弟选择器（以波浪号分隔）


**后代选取器**

::

    div p
    {
    background-color:yellow;
    }


**子元素选择器**

::

    div>p
    {
    background-color:yellow;
    }

**相邻兄弟选择器**

div之第一个p兄弟元素

::

    div+p
    {
        background-color:yellow;
    }


**普通兄弟选择器**

div之后所有的p兄弟元素

::

    div~p
    {
    background-color:yellow;
    }

伪类
----

CSS伪类是用来添加一些选择器的特殊效果。

由于状态的变化是非静态的，所以元素达到一个特定状态时，它可能得到一个伪类的样式；当状态改变时，它又会失去这个样式。由此可以看出，它的功能和class有些类似，但它是基于文档之外的抽象，所以叫伪类。

语法

伪类的语法：


    selector:pseudo-class {property:value;}

CSS类也可以使用伪类：

    selector.class:pseudo-class {property:value;}

**anchor伪类**

::

    a:link {color:#FF0000;} /* 未访问的链接 */
    a:visited {color:#00FF00;} /* 已访问的链接 */
    a:hover {color:#FF00FF;} /* 鼠标划过链接 */
    a:active {color:#0000FF;} /* 已选中的链接 */

伪类和CSS类

伪类可以与 CSS 类配合使用：

::

    a.red:visited {color:#FF0000;}       
    <a class="red" href="css-syntax.html">CSS Syntax</a>

如果在上面的例子的链接已被访问，它会显示为红色。


**first-child伪类**

:first-child 伪类来选择元素的第一个子元素

::

    p:first-child
    {
    color:blue;
    }
    </style>
    </head>

    <body>
    <p>I am a strong man.</p>
    <p>I am a strong man.</p>
    </body> 

第一个p将会变成blue

------------

::

    <body>
    <div> 
        <p>I am a strong man.</p>
        <p>I am a strong man.</p>
    </div>
    <p>I am a strong man.</p>
    <p>I am a strong man.</p>
    </body> 

div 中的第一个p 

-----

::

    <body>
    <div> 
        <p>I am a strong man.</p>
        <p>I am a strong man.</p>
    </div>
    <div>
        <p>I am a strong man.</p>
        <p>I am a strong man.</p>
    </diiv>
    </body> 

两个 div 中的第一个p 都将变为blue

-----

::

    p > i:first-child
    {
    color:blue;
    }
    </style>
    </head>

    <body>
    <p>I am a <i>strong</i> man. I am a <i>strong</i> man.</p>
    <p>I am a <i>strong</i> man. I am a <i>strong</i> man.</p> 

匹配所有<p> 元素中的第一个 <i> 元素

---------------


::

    p:first-child i
    {
    color:blue;
    }
    </style>
    </head>

    <body>
    <p>I am a <i>strong</i> man. I am a <i>strong</i> man.</p>
    <p>I am a <i>strong</i> man. I am a <i>strong</i> man.</p>
    </body> 

匹配所有作为第一个子元素的<p> 元素中的所有 <i> 元素

**:focus**

::

    input:focus
    {
        background-color:yellow;
    }
    </style>
    </head>

    <body>
    <form action="demo-form" method="get">
    First name: <input type="text" name="fname" /><br>
    Last name: <input type="text" name="lname" /><br>
    <input type="submit" value="Submit" />


**:first-letter**

::

    p:first-letter
    {
      font-size:200%;
      color:#8A2BE2;
    } 

每个 <p>元素的第一个字母选择的样式

**:first-line**

:first-line 选择器用来指定选择器第一行的样式。

::

    <style>
    p:first-line
    {
        background-color:yellow;
    }
    </style>
    </head>

    <body>
    <h1>WWF's Mission Statement</h1>
    <p>To stop the degradation of the planet's natural environment and to build a future in which humans live in harmony with nature, by; conserving the world's biological diversity, ensuring that the use of renewable natural resources is sustainable, and promoting the reduction of pollution and wasteful consumption.</p>
    </body>

**:first-of-type**

:first-of-type选择器匹配元素其父级是特定类型的第一个子元素。

::

    <style> 
    p:first-of-type
    {
        background:#ff0000;
    }
    </style>
    </head>
    <body>

    <h1>This is a heading</h1>
    <p>The first paragraph.</p>
    <p>The second paragraph.</p>
    <p>The third paragraph.</p>
    <p>The fourth paragraph.</p>

    </body>

注意与first-child的区别

**:after**

:after选择器向选定的元素之后插入内容。

::

    p:after
    {
      content:"- Remember this";
      background-color:yellow;
      color:red;
      font-weight:bold;
    }


在每个 <p>之后插入的内容和样式

**:before**

同上

**:checked**

为所有选中的输入元素设置背景颜色：

::

     <head>
    <meta charset="utf-8"> 
    <title>菜鸟教程(runoob.com)</title> 
    <style> 
    input:checked {
        height: 50px;
        width: 50px;
    }
    </style>
    </head>
    <body>

    <form action="">
    <input type="radio" checked="checked" value="male" name="gender" /> Male<br>
    <input type="radio" value="female" name="gender" /> Female<br>
    <input type="checkbox" checked="checked" value="Bike" /> I have a bike<br>
    <input type="checkbox" value="Car" /> I have a car 
    </form>

    </body>


**:disabled**


:disabled 选择器匹配每个禁用的的元素（主要用于表单元素）。:enabled 选择器不匹配任何无法禁用的元素。

::

    <style> 
    input[type="text"]:enabled
    {
        background:#ffff00;
    }
    input[type="text"]:disabled
    {
        background:#dddddd;
    }
    </style>
    </head>
    <body>

    <form action="">
    First name: <input type="text" value="Mickey" /><br>
    Last name: <input type="text" value="Mouse" /><br>
    Country: <input type="text" disabled="disabled" value="Disneyland" /><br>
    </form>

    </body>

**:empty**

:empty选择器选择每个没有任何子级的元素，包括文本节点。

::

    <style> 
    p:empty
    {
        width:100px;
        height:20px;
        background:#ff0000;
    }
    </style>
    </head>
    <body>

    <p></p>
    <p>A paragraph.</p>
    <p>Another paragraph.</p>

**:in-range**

输入的值在指定区间内时，设置指定样式:

::

    <style>
    input:in-range
    {
        border:2px solid yellow;
    }
    </style>
    </head>
    <body>

    <h3>:in-range 选择器实例演示。</h3>

    <input type="number" min="5" max="10" value="7" />

**:out-of-range**

同上

**:invalid 选择器**

:valid 和 :invalid 选择器分别匹配已满足或失败其输入验证要求的输入元素。

 :invalid 选择器只作用于能指定区间值的元素，例如 input 元素中的 min 和 max 属性，及正确的 email 字段, 合法的数字字段等。


::

    input:invalid
    {
        border:2px solid red;
    }
    </style>
    </head>
    <body>

    <h3> :invalid 选择器实例演示。</h3>

    <input type="email" value="supportEmail" />

    <p>请输入合法 e-mail 地址，查看样式变化。</p>
