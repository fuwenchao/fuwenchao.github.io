spring中redis的使用
====================================

在springboot中用redis实现消息队列
-----------------------------------------


安装redis服务端
^^^^^^^^^^^^^^^^^^^^^^^^^^


略

POM
^^^^^^^^^^^^^^^^^^^^^^^^^^

.. code:: java

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>




创建一个消息接收者
^^^^^^^^^^^^^^^^^^^^^^^^^^

.. code:: java


    package hello;

    import java.util.concurrent.CountDownLatch;

    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.beans.factory.annotation.Autowired;

    public class Receiver {
        private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);

        private CountDownLatch latch;

        @Autowired
        public Receiver(CountDownLatch latch) {
            this.latch = latch;
        }

        public void receiveMessage(String message) {
            LOGGER.info("Received <" + message + ">");
            latch.countDown();
        }
    }

注册监听者并发送消息
^^^^^^^^^^^^^^^^^^^^^^^^^^

Spring Data Redis provides all the components you need to send and receive messages with Redis. Specifically, you need to configure:



    - A connection factory

    - A message listener container

    - A Redis template



You’ll use the Redis template to send messages and you will register the Receiver with the message listener container so that it will receive messages. The connection factory drives both the template and the message listener container, enabling them to connect to the Redis server.

This example uses Spring Boot’s default RedisConnectionFactory, an instance of JedisConnectionFactory which is based on the Jedis Redis library. The connection factory is injected into both the message listener container and the Redis template.




.. code:: java

    package hello;

    import java.util.concurrent.CountDownLatch;

    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;
    import org.springframework.context.ApplicationContext;
    import org.springframework.context.annotation.Bean;
    import org.springframework.data.redis.connection.RedisConnectionFactory;
    import org.springframework.data.redis.core.StringRedisTemplate;
    import org.springframework.data.redis.listener.PatternTopic;
    import org.springframework.data.redis.listener.RedisMessageListenerContainer;
    import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

    @SpringBootApplication
    public class Application {

        private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

        @Bean
        RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                MessageListenerAdapter listenerAdapter) {

            RedisMessageListenerContainer container = new RedisMessageListenerContainer();
            container.setConnectionFactory(connectionFactory);
            container.addMessageListener(listenerAdapter, new PatternTopic("chat"));

            return container;
        }

        @Bean
        MessageListenerAdapter listenerAdapter(Receiver receiver) {
            return new MessageListenerAdapter(receiver, "receiveMessage");
        }

        @Bean
        Receiver receiver(CountDownLatch latch) {
            return new Receiver(latch);
        }

        @Bean
        CountDownLatch latch() {
            return new CountDownLatch(1);
        }

        @Bean
        StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
            return new StringRedisTemplate(connectionFactory);
        }

        public static void main(String[] args) throws InterruptedException {

            ApplicationContext ctx = SpringApplication.run(Application.class, args);

            StringRedisTemplate template = ctx.getBean(StringRedisTemplate.class);
            CountDownLatch latch = ctx.getBean(CountDownLatch.class);

            LOGGER.info("Sending message...");
            template.convertAndSend("chat", "Hello from Redis!");

            latch.await();

            System.exit(0);
        }
    }



The bean defined in the listenerAdapter method is registered as a message listener in the message listener container defined in container and will listen for messages on the "chat" topic. Because the Receiver class is a POJO, it needs to be wrapped in a message listener adapter that implements the MessageListener interface required by addMessageListener(). The message listener adapter is also configured to call the receiveMessage() method on Receiver when a message arrives.

The connection factory and message listener container beans are all you need to listen for messages. To send a message you also need a Redis template. Here, it is a bean configured as a StringRedisTemplate, an implementation of RedisTemplate that is focused on the common use of Redis where both keys and values are `String`s.

The main() method kicks everything off by creating a Spring application context. The application context then starts the message listener container, and the message listener container bean starts listening for messages. The main() method then retrieves the StringRedisTemplate bean from the application context and uses it to send a "Hello from Redis!" message on the "chat" topic. Finally, it closes the Spring application context and the application ends.



redis缓存的使用
----------------------

Redis是目前业界使用最广泛的内存数据存储。相比memcached，Redis支持更丰富的数据结构，例如hashes, lists, sets等，同时支持数据持久化。除此之外，Redis还提供一些类数据库的特性，比如事务，HA，主从库。可以说Redis兼具了缓存系统和数据库的一些特性，因此有着丰富的应用场景。本文介绍Redis在Spring Boot中两个典型的应用场景。

POM
^^^^^^^^^^^^^^^^^

略


PROPERTIES
^^^^^^^^^^^^^^^^^

.. code:: java

    # REDIS (RedisProperties)
    # Redis数据库索引（默认为0）
    spring.redis.database=0  
    # Redis服务器地址
    spring.redis.host=192.168.0.58
    # Redis服务器连接端口
    spring.redis.port=6379  
    # Redis服务器连接密码（默认为空）
    spring.redis.password=  
    # 连接池最大连接数（使用负值表示没有限制）
    spring.redis.pool.max-active=8  
    # 连接池最大阻塞等待时间（使用负值表示没有限制）
    spring.redis.pool.max-wait=-1  
    # 连接池中的最大空闲连接
    spring.redis.pool.max-idle=8  
    # 连接池中的最小空闲连接
    spring.redis.pool.min-idle=0  
    # 连接超时时间（毫秒）
    spring.redis.timeout=0  


添加cache的配置类
^^^^^^^^^^^^^^^^^^^^^^^


.. code:: java

    @Configuration
    @EnableCaching
    public class RedisConfig extends CachingConfigurerSupport{
        
        @Bean
        public KeyGenerator keyGenerator() {
            return new KeyGenerator() {
                @Override
                public Object generate(Object target, Method method, Object... params) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(target.getClass().getName());
                    sb.append(method.getName());
                    for (Object obj : params) {
                        sb.append(obj.toString());
                    }
                    return sb.toString();
                }
            };
        }

        @SuppressWarnings("rawtypes")
        @Bean
        public CacheManager cacheManager(RedisTemplate redisTemplate) {
            RedisCacheManager rcm = new RedisCacheManager(redisTemplate);
            //设置缓存过期时间
            //rcm.setDefaultExpiration(60);//秒
            return rcm;
        }
        
        @Bean
        public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
            StringRedisTemplate template = new StringRedisTemplate(factory);
            Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
            ObjectMapper om = new ObjectMapper();
            om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
            om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
            jackson2JsonRedisSerializer.setObjectMapper(om);
            template.setValueSerializer(jackson2JsonRedisSerializer);
            template.afterPropertiesSet();
            return template;
        }

    }

好了，接下来就可以直接使用了

.. code:: java

    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringApplicationConfiguration(Application.class)
    public class TestRedis {

        @Autowired
        private StringRedisTemplate stringRedisTemplate;
        
        @Autowired
        private RedisTemplate redisTemplate;

        @Test
        public void test() throws Exception {
            stringRedisTemplate.opsForValue().set("aaa", "111");
            Assert.assertEquals("111", stringRedisTemplate.opsForValue().get("aaa"));
        }
        
        @Test
        public void testObj() throws Exception {
            User user=new User("aa@126.com", "aa", "aa123456", "aa","123");
            ValueOperations<String, User> operations=redisTemplate.opsForValue();
            operations.set("com.neox", user);
            operations.set("com.neo.f", user,1,TimeUnit.SECONDS);
            Thread.sleep(1000);
            //redisTemplate.delete("com.neo.f");
            boolean exists=redisTemplate.hasKey("com.neo.f");
            if(exists){
                System.out.println("exists is true");
            }else{
                System.out.println("exists is false");
            }
           // Assert.assertEquals("aa", operations.get("com.neo.f").getUserName());
        }
    }

以上都是手动使用的方式，如何在查找数据库的时候自动使用缓存呢，看下面；

4、自动根据方法生成缓存

.. code:: java

    @RequestMapping("/getUser")
    @Cacheable(value="user-key")
    public User getUser() {
        User user=userRepository.findByUserName("aa");
        System.out.println("若下面没出现“无缓存的时候调用”字样且能打印出数据表示测试成功");  
        return user;
    }

其中value的值就是缓存到redis中的key


共享Session-spring-session-data-redis
------------------------------------------------



分布式系统中，sessiong共享有很多的解决方案，其中托管到缓存中应该是最常用的方案之一，

Spring Session官方说明

Spring Session provides an API and implementations for managing a user’s session information.
如何使用

1、引入依赖
^^^^^^^^^^^^^^^^^^^^^^

.. code:: java

    <dependency>
        <groupId>org.springframework.session</groupId>
        <artifactId>spring-session-data-redis</artifactId>
    </dependency>

2、Session配置：
^^^^^^^^^^^^^^^^^^^^^^

.. code:: java

    @Configuration
    @EnableRedisHttpSession(maxInactiveIntervalInSeconds = 86400*30)
    public class SessionConfig {
    }

    maxInactiveIntervalInSeconds: 设置Session失效时间，使用Redis Session之后，原Boot的server.session.timeout属性不再生效

好了，这样就配置好了，我们来测试一下

3、测试

添加测试方法获取sessionid

.. code:: java


    @RequestMapping("/uid")
        String uid(HttpSession session) {
            UUID uid = (UUID) session.getAttribute("uid");
            if (uid == null) {
                uid = UUID.randomUUID();
            }
            session.setAttribute("uid", uid);
            return session.getId();
        }

登录redis 输入 keys '*sessions*'


::

    t<spring:session:sessions:db031986-8ecc-48d6-b471-b137a3ed6bc4
    t(spring:session:expirations:1472976480000

其中 1472976480000为失效时间，意思是这个时间后session失效，db031986-8ecc-48d6-b471-b137a3ed6bc4 为sessionId,登录http://localhost:8080/uid 发现会一致，就说明session 已经在redis里面进行有效的管理了。
如何在两台或者多台中共享session

其实就是按照上面的步骤在另一个项目中再次配置一次，启动后自动就进行了session共享。



-----

参考
-------

`Messaging with Redis`_

.. _Messaging with Redis: https://spring.io/guides/gs/messaging-redis/


`在springboot中用redis实现消息队列`_

.. _`在springboot中用redis实现消息队列`: https://blog.csdn.net/forezp/article/details/71023652


`纯洁的微笑--spring boot(三)：Spring Boot中Redis的使用`_

.. _`纯洁的微笑--spring boot(三)：Spring Boot中Redis的使用`: http://www.cnblogs.com/ityouknow/p/5748830.html

