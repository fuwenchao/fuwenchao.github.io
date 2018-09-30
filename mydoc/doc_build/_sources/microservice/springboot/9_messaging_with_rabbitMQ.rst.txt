Springboot整合RabbitMQ
==============================


安装rabbitMQ并启动
---------------------------

略

POM
--------

.. code:: java

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-amqp</artifactId>
        </dependency>

创建消息接收者
---------------------

.. code:: java

    package hello;

    import java.util.concurrent.CountDownLatch;
    import org.springframework.stereotype.Component;

    @Component
    public class Receiver {

        private CountDownLatch latch = new CountDownLatch(1);

        public void receiveMessage(String message) {
            System.out.println("Received <" + message + ">");
            latch.countDown();
        }

        public CountDownLatch getLatch() {
            return latch;
        }

    }

注册监听者并发送消息
--------------------------

.. code:: java

    package hello;

    import org.springframework.amqp.core.Binding;
    import org.springframework.amqp.core.BindingBuilder;
    import org.springframework.amqp.core.Queue;
    import org.springframework.amqp.core.TopicExchange;
    import org.springframework.amqp.rabbit.connection.ConnectionFactory;
    import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
    import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;
    import org.springframework.context.annotation.Bean;

    @SpringBootApplication
    public class Application {

        static final String topicExchangeName = "spring-boot-exchange";

        static final String queueName = "spring-boot";

        @Bean
        Queue queue() {
            return new Queue(queueName, false);
        }

        @Bean
        TopicExchange exchange() {
            return new TopicExchange(topicExchangeName);
        }

        @Bean
        Binding binding(Queue queue, TopicExchange exchange) {
            return BindingBuilder.bind(queue).to(exchange).with("foo.bar.#");
        }

        @Bean
        SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                MessageListenerAdapter listenerAdapter) {
            SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
            container.setConnectionFactory(connectionFactory);
            container.setQueueNames(queueName);
            container.setMessageListener(listenerAdapter);
            return container;
        }

        @Bean
        MessageListenerAdapter listenerAdapter(Receiver receiver) {
            return new MessageListenerAdapter(receiver, "receiveMessage");
        }

        public static void main(String[] args) throws InterruptedException {
            SpringApplication.run(Application.class, args).close();
        }

    }

发送测试消息
----------------

.. code:: java

    package hello;

    import java.util.concurrent.TimeUnit;

    import org.springframework.amqp.rabbit.core.RabbitTemplate;
    import org.springframework.boot.CommandLineRunner;
    import org.springframework.stereotype.Component;

    @Component
    public class Runner implements CommandLineRunner {

        private final RabbitTemplate rabbitTemplate;
        private final Receiver receiver;

        public Runner(Receiver receiver, RabbitTemplate rabbitTemplate) {
            this.receiver = receiver;
            this.rabbitTemplate = rabbitTemplate;
        }

        @Override
        public void run(String... args) throws Exception {
            System.out.println("Sending message...");
            rabbitTemplate.convertAndSend(Application.topicExchangeName, "foo.bar.baz", "Hello from RabbitMQ!");
            receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
        }

    }

`springboot(八)：RabbitMQ详解`_

.. _`springboot(八)：RabbitMQ详解`: http://www.cnblogs.com/ityouknow/p/6120544.html