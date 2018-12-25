vi使用技巧
===========

1. windows 转为 unix

::

    vim test.sh
    :set ff
    如果出现fileforma＝dos 表示是Windows上的换行符。
    :set fileformat=unix or :set ff=unix
    :wq!