String的使用
=================


String的常用方法
----------------------

.. image:: ./images/String_fun.png


创建String对象
------------------


.. image:: ./images/String_equals.png


s1 == s2 ture

s1 == s3 false



**String str4 = new String("imooc")**

str3 == str4 false


String的不可变性
----------------------

一旦创建不可修改

所谓的修改是创建了新的对象，所指向的内存空间不变

.. image:: ./images/String_immutable.png


字节数组与字符串的转换
-----------------------


bytes[] byte = str.getBytes("GBK");

String str = new String(byte,"GBK");


== 和 equals 的区别
--------------------------

== 比较的是 地址

equals 比较的是 值


StringBuilder
---------------------

可变的

当频繁操作字符串的时候，使用StringBuilder


StringBuffer
--------------

和 StringBuild基本相似

StringBuffer 是线程安全的

StringBuilder 性能略高

**方法**
 
delete 

substring

append

insert
