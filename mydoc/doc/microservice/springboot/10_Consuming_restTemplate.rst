用restTemplate消费服务
==============================


创建一个springboot工程，去消费RESTFUL的服务。这个服务是 http:///gturnquist-quoters.cfapps.io/api/random ，它会随机返回Json字符串。

在Spring项目中，它提供了一个非常简便的类，叫RestTemplate，它可以很简便的消费服务。

消费服务
------------

通过RestTemplate消费服务，需要先context中注册一个RestTemplate bean。代码如下：

.. code:: java

    @SpringBootApplication
    public class SpringbootResttemplateApplication {

        private static final Logger log = LoggerFactory.getLogger(SpringbootResttemplateApplication.class);

    //  public static void main(String args[]) {
    //      RestTemplate restTemplate = new RestTemplate();
    //      String quote = restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", String.class);
    //      log.info(quote.toString());
    //  }

        public static void main(String args[]) {
            SpringApplication.run(SpringbootResttemplateApplication.class);
        }

        @Bean
        public RestTemplate restTemplate(RestTemplateBuilder builder) {
            return builder.build();
        }

        @Bean
        public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
            return args -> {
                String quote = restTemplate.getForObject(
                        "http://gturnquist-quoters.cfapps.io/api/random", String.class);
                log.info(quote.toString());
            };
        }
    }



