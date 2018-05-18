定时任务
==============


构建工程
--------------------

创建一个Springboot工程，在它的程序入口加上@EnableScheduling,开启调度任务。

.. code:: java

    @SpringBootApplication
    @EnableScheduling
    public class SpringbootSchedulingTasksApplication {

        public static void main(String[] args) {
            SpringApplication.run(SpringbootSchedulingTasksApplication.class, args);
        }
    }

创建定时任务
------------------------

创建一个定时任务，每过5s在控制台打印当前时间。


.. code:: java

    @Component
    public class ScheduledTasks {

        private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

        private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        @Scheduled(fixedRate = 5000)
        public void reportCurrentTime() {
            log.info("The time is now {}", dateFormat.format(new Date()));
        }
    }

通过在方法上加@Scheduled注解，表明该方法是一个调度任务。

    - @Scheduled(fixedRate = 5000) ：上一次开始执行时间点之后5秒再执行
    - @Scheduled(fixedDelay = 5000) ：上一次执行完毕时间点之后5秒再执行
    - @Scheduled(initialDelay=1000, fixedRate=5000) ：第一次延迟1秒后执行，之后按fixedRate的规则每5秒执行一次
    - @Scheduled(cron=” /5 “) ：通过cron表达式定义规则，什么是cro表达式，自行搜索引擎。


总结
------

在springboot创建定时任务比较简单，只需2步：

    1. 在程序的入口加上@EnableScheduling注解。
    2. 在定时方法上加@Scheduled注解。
