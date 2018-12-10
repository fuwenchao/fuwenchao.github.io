js 增加时间的几种方式
==============================

1. HTML 行内注册
----------------------


<input type="button" value="第一种方法" onclick="alert('我被调用了')">

缺点：事件的代码太多，会使HTML的页面会乱掉 。未有分离HTML和Js代码


2. 也是行内进行注册
----------------------------

<input type="button" value="第二种方法" onclick="han()">

::

 function han(){
        alert("我被调用了");
    };

缺点：没有把HTML和Js代码进行分离。如果为按钮注册一个事件，需要翻到HTML上写onclick。并且函数名重复会很困扰。

3. 分离HTML和js的注册事件
--------------------------------

<input type="button" value="第三种方法" id="btn">  

::

    <script>
    function my$(id){
     return document.getElementById(id);
    //   通用函数  
    };
     my$("btn").onclick = han;
    //  注册事件,不能用han()否则会自动触发
    </script>

缺点：
如果在外部引入的js文件，有同名会被覆盖


4. 匿名
--------------

<input type="button" value="第四种方法" id="btn1">

::

    <script>
           function my$(id){
        return document.getElementById(id);
       //   通用函数 
       };
       my$("btn1").onclick = function(){
           alert("我被调用了");
       };
       // 针对性强
   </script>

优点：针对性强


5. 绑定方式
------------------

<input type="button" value="第五种方法" id="btn5">  

document.getElementById("btn5").addEventListener('click',han,false);




