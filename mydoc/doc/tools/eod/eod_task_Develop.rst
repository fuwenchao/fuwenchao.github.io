EOD任务开发
==================

新增系统
---------

数据源管理 --> 新增 --> 生成抽数框架（除了抽数任务） --> 生成框架par文件 generatePar.sh

增加表
--------

1. 确定表的卸载数据方式

2. eod -> 元数据管理 -> 新增 -> 选择表 -> 确定

3. 选中表 -> 最右侧走发布流程

4. 流程发布完了之后该表的卸数任务已经在 controlM 的 EOD_TEST 中生成

5. 重新生成所有表的卸数模板
    
    sh /mnt/etl/EoD/infa/scripts/shell/eod/TOOLS/createWorkflowPar.sh syscode prodid

6. 将EOD_TEST中的任务copy到正式任务 EOD_XXX 中

7. 测试

作业流程
----------

元数据 --> eod ods --> ogCompare: 对比获取元数据版本信息 --> tgCompare: ods 与 target 对比获得元数据变动差异到target tmp --> 告警: target(最终字段表) --> 生成抽数par文件 --> 抽数 --> 建立软连接 --> done 


问题汇总
----------

1. 轮询的任务怎么生成

    EOD_CTL 中，手工开发，可以拿之前的任务过来修改即可

2. 抽元数据任务如何生成

    同上

3. 生成正式表的抽数作业在infa中的那个位置

    正式的在EOD；测试的在EOD_TEST

4. 整个作业中有哪些par文件

    1. 轮询par文件 start.template ； 在01中
    2. 抽取元数据par文件 meta.template ; 在02中
    3. 抽取数据文件的par文件 .par.template; 在03中
    4. 作业结束par文件 end.template; 在05中


5. 模板文件如何生成

    01 02 05 template 文件生成

        /mnt/etl/EoD/infa/scripts/shell/eod/TOOLS/generatePar.sh

    03  template 文件生成

        /mnt/etl/EoD/infa/scripts/shell/eod/TOOLS/createWorkflowPar.sh




