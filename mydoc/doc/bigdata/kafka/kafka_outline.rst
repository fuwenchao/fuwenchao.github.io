kafka学习大纲
====================

学习步骤
--------------

初识 -> 安装 -> kafka 生产者(向kafka写入数据) -> kafka 消费者(向kafka消费数据) -> 深入kafka -> 可靠的数据传递 -> 构建数据管道 -> 跨集群数据镜像 -> 管理kafka -> 监控kafka -> 流式处理 


1 初识
--------

类似有不同与传统消息系统(ActiveMQ,RabbitMQ)  -> kafka 分布式 -> 缓冲 解耦 -> 消息 字节数组 -> 键 散列 分区 ->  批次 延时性

-> 模式 -> 主题 多分区 跨服务器 -> 追加写入分区 -> FIFO -> 单分区可以保证有序


生产者 -> 分区器 根据 键 写入不同分区

消费者 -> 根据偏移量区分消息是否已经读过 -> 偏移量是一个不断递增的数值,在创建消息时,kafka会把它增加进消息里,在给定的分区里,每个消息的
偏移量都是唯一的 -> 消费者把每个分区读取的消息偏移量保存在zookeeper或者kafka中,如果消费者关闭或者重启,他的读取状态不会丢失


一个分区 -> 分区复制 -> 多个 broker -> 但是每个分区都有一个首领broker

消息保留时长 -> 时间 或者 数据量 -> 配置文件中 -> 主题级别

集群内复制 -> 自动

集群间复制 -> MirrorMaker 等工具


为什么选择kafka

::

  1. 多个生产者
  2. 多个消费者
  3. 基于磁盘的数据存储 *
  4. 伸缩性
  5. 高性能



起源 - LinkedIN


2 安装
------------


安装 JAVA -> 安装 Zookeeper -> 安装 Kafka Broker

**测试**

1. 创建并验证主题

  bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test

  bin/kafka-topics.sh --zookeeper localhost:2181 --describe --topic test

2. 往测试主题上发布消息

  bin/kafka-console-produceer.sh --broker-list localhost:9092 --topic test

3. 从测试主题读取消息

  bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic test --from-begining


**broker配置**

1. 常规配置

::

  1. broker.id
  2. port
  3. zookeeper.connect
  4. log.dirs
  5. num.recovery.threads.per.data.dir
  6. auto.create.topics.enable


2. 主题的默认配置

::

  1. num.partitions
  2. log.retention.ms
  3. log.retention.bytes
  4. log.segment.bytes
  5. log.segment.ms
  6. message.max.bytes

**硬件的选择**

1. 磁盘
2. 内存  jvm给小,留大内存给页面缓存
3. 网络
4. cpu




**Kafka集群**

1. 需要多少个 broker

2. broker配置


::

  1. zookeeper.connect
  2. broker.id
  3. ...

3. 操作系统调优

::

  1. 虚拟内存

    说明: 高吞吐量应用,避免内存交换;

    设置 vm.swappiness参数值小一些,建议1;该参数指明了虚拟机的子系统如何使用交换分区,而不是只把内存页从页面缓存里移除,要优先考虑减小页面缓存,而不是内存交换

    设置 vm.dirty_background_ratio设为小于10 ,建议5,不应该是0,这样会促使内核频繁刷新页面,从而降低内核为底层设备的磁盘写入提供缓冲的能力

    设置vm.dirty_ratio 参数可以增加被内核进程刷新到磁盘之前的脏页数量,60~80比较合理

  2. 磁盘

    a. XFS文件系统优于EXT4
    b. 建议禁用挂载点的noatime

  3. 网络

    一般情况系统内核没有针对大流量网络传输进行优化

    a. 对分配给socket读写缓冲区的内存大小做出调整

        对应参数非别为 net.core.wmem_default 和 net.core.rmem_defalut 合理值 131072 (128KB)
        net.core.wmem_max 和 net.core.rmem_max 合理值 2097152 (2M)

    b. 设置 TCP socket 的读写缓冲区

      net.ipv4.tcp_wmem 和 net.ipv4.tcp_rmem 这些参数值由三个整数组成,空格分隔,分别代表最小值 ,默认值 ,最大值;
      最大值不能大于 net.core.wmem_max 和 net.core.rmem_max 制定的大小

    c. 启用 TCP 时间窗扩展

      net.ipv4.tcp_window_scaling = 1 启用时间窗扩展,可以提升客户端传输数据的效率,传输的数据可以在服务器进行缓冲,

    d. 接受更多的并发连接

      把 net.ipv4.tcp_max_sync_backlog 设为比默认值 1024 更大的值,可以接受更多的并发连接

    e. 允许更多的数据包排队等待内核处理

      net.core.netdev_max_backlog 设为比默认值1000更大的值



4. 部署生产时注意事项


::

  1. 调优 JAVA 垃圾回收器

    说明: Kafka 对堆内存的使用率非常高,容易产生垃圾对象

    使用 G1 垃圾回收器

      理由:  G1会自动根据工作负载情况进行自我调节,而且他的停顿时间是恒定的,他可以轻松处理大块的堆内存,
            把堆内存分为若干小块的区域,每次停顿时并不会对整个对空间进行回收

      对G1的调优:

          a. MaxGCPauseMillis
          b. InitiatingHeapOccupancyPercent

    修改Kafka启动脚本

      export KAFKA_JVM_PERFORMANCE_OPTS="-SERVER -XX:+UseG1GC -XX:MaxGCPauseMillis=20 \
                                          -XX:InitiatingHeapOccupancyPercent=35\
                                          -XX:+DisableExplicitGC -Djava.awt.headless=true
      "

    2. 数据中心布局

      最好把集群的broker安装在不同的机架上

    3. 共享 Zookeeper

      最好不共享







3 Kafka生产者
-------------------


向Kafka写入数据


**生产流程**

构造消息对象 -> 分区器 -> 分区器根据消息的分区特征 -> 生产者就知道王那个主题那个分区发送消息了 -> 消息 放入一个 消息批次 中
-> 批次发往分区及主题 -> broker 返回响应 -> 成功的话返回RecordMetaData对象;失败返回一个错误 -> 生产者收到错误之后会尝试重新发送消息(内部机制) -> 几次之后如果还是失败,就返回错误信息


构造消息体 -> 创建Kafka生产者 -> 发送消息 

发送消息的几种方式

::

    方式一: 发送并网及
    方式二: 同步
    方式三: 异步

**生产者的配置**

大部分默认即可,不过有几个参数在内存使用,性能和可靠性方面对生产者影响较大

::

    a. acks 

        必须有多少个分区副本收到消息,生产者才会认为消息写入成功

    b. buffer.memory

        设置生产者内存缓冲区大小

    c. compression.type

        消息压缩格式

    d. retries

        决定了生产者重发消息的次数

    e. batch.size

        当有多个消息需要发送到同一个分区时,生产者会把他们放在同一个批次里.该参数指定了一个批次使用的内存大小

    f. linger.ms

        该参数指定了生产者在发送批次之前等待更多的消息加入批次的时间

    g. max.in.flight.requests.per.connection

        生产者在收到服务器响应之前可以发送多少个消息

        .....


4 kafka消费者
--------------------



消费者 和 消费者群组






