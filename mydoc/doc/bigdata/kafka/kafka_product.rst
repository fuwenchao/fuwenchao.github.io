kafka生产者
==============

发送消息的几种方式
------------------

- 发送并忘记
- 同步发送
- 异步发送

生产者的配置
------------

::


    [main] INFO org.apache.kafka.clients.producer.ProducerConfig - ProducerConfig values: 
  //  acks = all  
  //  batch.size = 16384
  //  bootstrap.servers = [localhost:9092]
  //  buffer.memory = 33554432
  //  client.id = zhangsy
  //  compression.type = none
    connections.max.idle.ms = 540000
    enable.idempotence = false
    interceptor.classes = []
  //  key.serializer = class org.apache.kafka.common.serialization.StringSerializer
  //  linger.ms = 0
  //  max.block.ms = 60000
  //  max.in.flight.requests.per.connection = 5
  //  max.request.size = 1048576
    metadata.max.age.ms = 300000
    metric.reporters = []
    metrics.num.samples = 2
    metrics.recording.level = INFO
    metrics.sample.window.ms = 30000
    partitioner.class = class org.apache.kafka.clients.producer.internals.DefaultPartitioner
  //  receive.buffer.bytes = 32768
    reconnect.backoff.max.ms = 1000
    reconnect.backoff.ms = 50
  //  request.timeout.ms = 30000
  //  retries = 3
  //  retry.backoff.ms = 100
    sasl.jaas.config = null
    sasl.kerberos.kinit.cmd = /usr/bin/kinit
    sasl.kerberos.min.time.before.relogin = 60000
    sasl.kerberos.service.name = null
    sasl.kerberos.ticket.renew.jitter = 0.05
    sasl.kerberos.ticket.renew.window.factor = 0.8
    sasl.mechanism = GSSAPI
    security.protocol = PLAINTEXT
  //  send.buffer.bytes = 131072
    ssl.cipher.suites = null
    ssl.enabled.protocols = [TLSv1.2, TLSv1.1, TLSv1]
    ssl.endpoint.identification.algorithm = null
    ssl.key.password = null
    ssl.keymanager.algorithm = SunX509
    ssl.keystore.location = null
    ssl.keystore.password = null
    ssl.keystore.type = JKS
    ssl.protocol = TLS
    ssl.provider = null
    ssl.secure.random.implementation = null
    ssl.trustmanager.algorithm = PKIX
    ssl.truststore.location = null
    ssl.truststore.password = null
    ssl.truststore.type = JKS
    transaction.timeout.ms = 60000
    transactional.id = null
  //  value.serializer = class org.apache.kafka.common.serialization.StringSerializer





  bootstrap.servers： broker的地址
  key.serializer：关键字的序列化方式
  value.serializer：消息值的序列化方式
  acks：指定必须要有多少个分区的副本接收到该消息，服务端才会向生产者发送响应，可选值为：0,1,2，…，all
  buffer.memory：生产者的内存缓冲区大小。如果生产者发送消息的速度 > 消息发送到kafka的速度，那么消息就会在缓冲区堆积，导致缓冲区不足。这个时候，send()方法要么阻塞，要么抛出异常。
  max.block.ms：表示send()方法在抛出异常之前可以阻塞多久的时间，默认是60s
  compression.type：消息在发往kafka之前可以进行压缩处理，以此来降低存储开销和网络带宽。默认值是null，可选的压缩算法有snappy、gzip和lz4
  retries：生产者向kafka发送消息可能会发生错误，有的是临时性的错误，比如网络突然阻塞了一会儿，有的不是临时的错误，比如“消息太大了”，对于出现的临时错误，可以通过重试机制来重新发送
  retry.backoff.ms：每次重试之间间隔的时间，第一次失败了，那么休息一会再重试，休息多久，可以通过这个参数来调节
  batch.size：生产者在发送消息时，可以将即将发往同一个分区的消息放在一个批次里，然后将这个批次整体进行发送，这样可以节约网络带宽，提升性能。该参数就是用来规约一个批次的大小的。但是生产者并不是说要等到一个批次装满之后，才会发送，不是这样的，有时候半满，甚至只有一个消息的时候，也可能会发送，具体怎么选择，我们不知道，但是不是说非要等装满才发。因此，如果把该参数调的比较大的话，是不会造成消息发送延迟的，但是会占用比较大的内存。但是如果设置的太小，会造成消息发送次数增加，会有额外的IO开销
  linger.ms：生产者在发送一个批次之前，可以适当的等一小会，这样可以让更多的消息加入到该批次。这样会造成延时增加，但是降低了IO开销，增加了吞吐量
  client.id：服务器用来标志消息的来源，是一个任意的字符串
  max.in.flight.requests.per.connection：一个消息发送给kafka集群，在收到服务端的响应之前的这段时间里，生产者还可以发n-1个消息。这个参数配置retries，可以保证消息的顺序，后面会介绍
  request.timeout.ms：生产者在发送消息之后，到收到服务端响应时，等待的时间限制
  max.request.size：生产者发送消息的大小。可以是一个消息的大小，也可以发送的一个批次的消息大小
  receive.buffer.bytes和send.buffer.bytes：tcp socket接收和发送消息的缓冲区大小，其实指的就是ByteBuffer的大小



Kafka的顺序保证
-------------------

Kafka可以保证消息在一个分区里是有序的存放的，即生产者如果按照某个顺序发送消息，那么broker也会按照这个顺序来将消息写入到分区，消费者也会按照这个顺序来读取消息。但是在生产环境中，retries参数的设置一般都是非零的整数，比如retries=3，那么当消息1发送失败的时候，在执行重试之前，消息2被发送了，并且没有错误，那么消息2将会先于消息1到达broker，这样的话，消息就顺便不对了。那么在Kafka里，这个问题是如何保证顺序性的呢？
上面我们提到一个参数，叫做max.in.flight.requests.per.connection，这个参数可以控制在生产者收到响应之前，还可以发送多少个消息。如果把这个参数设置成1，那么即使发生了错误重试，那么消息2，也没有机会在消息1之前发送，而是必须等到消息1的响应被收到之后，消息2才可以发送。


生产者
-------

.. code:: java

  package producer;

  import org.apache.kafka.clients.producer.*;
  import java.util.Properties;
  import java.util.concurrent.Future;

  public class MyProducer {

      private static Properties kafkaProps = new Properties();

      /**
       * 初始化一些配置信息
       */
      public void initProperty(){
          kafkaProps.put("bootstrap.servers", "localhost:9092");
          kafkaProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
          kafkaProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
          kafkaProps.put("retries", 3);
          kafkaProps.put("acks", "all");
          kafkaProps.put("client.id", "zhangsy");
      }

      /**
       * 加载配置信息，生成一个生产者实例
       * @param props
       * @return
       */
      public Producer getProducer(Properties props){
          if (props == null || props.size() == 0)
              throw new IllegalArgumentException();
          return new KafkaProducer(kafkaProps);
      }

      /**
       * 同步发送消息
       * @param producer
       * @throws Exception
       */
      public void syncSend(Producer producer) throws Exception{
          for (int i = 0; i < 2; i++){

              ProducerRecord<String, String> record = new ProducerRecord<String, String>
                      ("country","name","UK"+String.valueOf(i));

              //同步发送消息，消息发送成功后，服务端会返回给一个RecordMetadata对象
              Future<RecordMetadata> future = producer.send(record);

              RecordMetadata metadata = future.get();

              System.out.println("offset:"+metadata.offset()+"\npartition:"+metadata.partition()
                      +"\ntopic:"+metadata.topic()+"\nserializedKeySize:"
                      +metadata.serializedKeySize()+"\nserializedValueSize:"+metadata.serializedValueSize()+"\n");
          }
          producer.close();
      }

      /**
       * 异步发送消息
       * @param producer
       */
      public void asyncSend(Producer producer){

          ProducerRecord<String, String> record = new ProducerRecord<String, String>("country","today","son");

          producer.send(record, new Callback(){
              public void onCompletion(RecordMetadata metadata, Exception e){
                  System.out.println("offset:"+metadata.offset()+"\npartition:"+metadata.partition()
                          +"\ntopic:"+metadata.topic()+"\nserializedKeySize:"
                          +metadata.serializedKeySize()+"\nserializedValueSize:"+metadata.serializedValueSize()+"\n");
                  if (e == null){
                      System.out.println("hello");
                  }
              }
          });

          producer.close();
      }


      public void start() throws Exception{
          initProperty();
  //        syncSend(getProducer(kafkaProps));
          asyncSend(getProducer(kafkaProps));
      }

      public static void main(String[] args) throws Exception{
          MyProducer myProducer = new MyProducer();
          myProducer.start();
      }

  }

