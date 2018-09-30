 异步方法
 ============

 POM
 -------

 .. code:: java

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-web</artifactId>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
        </dependency>

创建一个接收数据的实体：
------------------------------

.. code:: java

    @JsonIgnoreProperties(ignoreUnknown=true)
    public class User {

        private String name;
        private String blog;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBlog() {
            return blog;
        }

        public void setBlog(String blog) {
            this.blog = blog;
        }

        @Override
        public String toString() {
            return "User [name=" + name + ", blog=" + blog + "]";
        }

    }


创建一个请求的　github的service:
-----------------------------------

.. code:: java

    @Service
    public class GitHubLookupService {

        private static final Logger logger = LoggerFactory.getLogger(GitHubLookupService.class);

        private final RestTemplate restTemplate;

        public GitHubLookupService(RestTemplateBuilder restTemplateBuilder) {
            this.restTemplate = restTemplateBuilder.build();
        }

        @Async
        public Future<User> findUser(String user) throws InterruptedException {
            logger.info("Looking up " + user);
            String url = String.format("https://api.github.com/users/%s", user);
            User results = restTemplate.getForObject(url, User.class);
            // Artificial delay of 1s for demonstration purposes
            Thread.sleep(1000L);
            return new AsyncResult<>(results);
        }

    }

通过，RestTemplate去请求，另外加上类@Async 表明是一个异步任务。

开启异步任务：


.. code:: java

    @SpringBootApplication
    @EnableAsync
    public class Application extends AsyncConfigurerSupport {

        public static void main(String[] args) {
            SpringApplication.run(Application.class, args);
        }

        @Override
        public Executor getAsyncExecutor() {
            ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
            executor.setCorePoolSize(2);
            executor.setMaxPoolSize(2);
            executor.setQueueCapacity(500);
            executor.setThreadNamePrefix("GithubLookup-");
            executor.initialize();
            return executor;
        }

    }

通过@EnableAsync开启异步任务；并且配置AsyncConfigurerSupport，比如最大的线程池为2.

测试
-------

测试代码如下：

.. code:: java

    @Component
    public class AppRunner implements CommandLineRunner {

        private static final Logger logger = LoggerFactory.getLogger(AppRunner.class);

        private final GitHubLookupService gitHubLookupService;

        public AppRunner(GitHubLookupService gitHubLookupService) {
            this.gitHubLookupService = gitHubLookupService;
        }

        @Override
        public void run(String... args) throws Exception {
            // Start the clock
            long start = System.currentTimeMillis();

            // Kick of multiple, asynchronous lookups
            Future<User> page1 = gitHubLookupService.findUser("PivotalSoftware");
            Future<User> page2 = gitHubLookupService.findUser("CloudFoundry");
            Future<User> page3 = gitHubLookupService.findUser("Spring-Projects");

            // Wait until they are all done
            while (!(page1.isDone() && page2.isDone() && page3.isDone())) {
                Thread.sleep(10); //10-millisecond pause between each check
            }

            // Print results, including elapsed time
            logger.info("Elapsed time: " + (System.currentTimeMillis() - start));
            logger.info("--> " + page1.get());
            logger.info("--> " + page2.get());
            logger.info("--> " + page3.get());
        }

    }