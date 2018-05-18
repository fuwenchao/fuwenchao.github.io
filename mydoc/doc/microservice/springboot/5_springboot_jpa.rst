SpringBoot 整合JPA
===========================

JPA全称Java Persistence API.JPA通过JDK 5.0注解或XML描述对象－关系表的映射关系，并将运行期的实体对象持久化到数据库中。

JPA 的目标之一是制定一个可以由很多供应商实现的API，并且开发人员可以编码来实现该API，而不是使用私有供应商特有的API。

JPA是需要Provider来实现其功能的，Hibernate就是JPA Provider中很强的一个，应该说无人能出其右。从功能上来说，JPA就是Hibernate功能的一个子集。

添加相关依赖
---------------

.. code:: java

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>


        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

配置数据源
-------------

在application.yml文件配置：

.. code:: java

    spring:
      profiles:
        active: prod
      datasource:
        driver-class-name: com.mysql.jdbc.Driver
        url: jdbc:mysql://localhost:3306/wenchao
        username: root
        password: 900525f
      jpa:
        hibernate:
          ddl-auto: update
        show-sql: true

注意，如果通过jpa在数据库中建表，将jpa.hibernate,ddl-auto改为create，建完表之后，要改为update,要不然每次重启工程会删除表并新建。




        - create： 每次加载hibernate时都会删除上一次的生成的表，然后根据你的model类再重新来生成新表，哪怕两次没有任何改变也要这样执行，这就是导致数据库表数据丢失的一个重要原因。
        - create-drop ：每次加载hibernate时根据model类生成表，但是sessionFactory一关闭,表就自动删除。
        - update：最常用的属性，第一次加载hibernate时根据model类会自动建立起表的结构（前提是先建立好数据库），以后加载hibernate时根据 model类自动更新表结构，即使表结构改变了但表中的行仍然存在不会删除以前的行。要注意的是当部署到服务器后，表结构是不会被马上建立起来的，是要等 应用第一次运行起来后才会。
        - validate ：每次加载hibernate时，验证创建数据库表结构，只会和数据库中的表进行比较，不会创建新表，但是会插入新值。




.. code:: java

    spring.datasource.url=jdbc:mysql://localhost:3306/test
    spring.datasource.username=root
    spring.datasource.password=root
    spring.datasource.driver-class-name=com.mysql.jdbc.Driver

    spring.jpa.properties.hibernate.hbm2ddl.auto=update
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect
    spring.jpa.show-sql= true

    - dialect 主要是指定生成表名的存储引擎为InneoDB
    - show-sql 是否打印出自动生产的SQL，方便调试的时候查看

**创建实体类**

通过@Entity 表明是一个映射的实体类， @Id表明id， @GeneratedValue 字段自动生成

.. code:: java

    @Entity
    public class Account {
        @Id
        @GeneratedValue
        private int id ;
        private String name ;
        private double money;

    ...  省略getter setter
    }

**Dao层**

数据访问层，通过编写一个继承自 JpaRepository 的接口就能完成数据访问,其中包含了几本的单表查询的方法，非常的方便。值得注意的是，这个Account 对象名，而不是具体的表名，另外Interger是主键的类型，一般为Integer或者Long

.. code:: java

    public interface AccountDao  extends JpaRepository<Account,Integer> {
    }

**Web层**

在这个栗子中我简略了service层的书写，在实际开发中，不可省略。新写一个controller，写几个restful api来测试数据的访问。


.. code:: java

    @RestController
    @RequestMapping("/account")
    public class AccountController {

        @Autowired
        AccountDao accountDao;

        @RequestMapping(value = "/list", method = RequestMethod.GET)
        public List<Account> getAccounts() {
            return accountDao.findAll();
        }

        @RequestMapping(value = "/{id}", method = RequestMethod.GET)
        public Account getAccountById(@PathVariable("id") int id) {
            return accountDao.findOne(id);
        }

        @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
        public String updateAccount(@PathVariable("id") int id, @RequestParam(value = "name", required = true) String name,
                                    @RequestParam(value = "money", required = true) double money) {
            Account account = new Account();
            account.setMoney(money);
            account.setName(name);
            account.setId(id);
            Account account1 = accountDao.saveAndFlush(account);

            return account1.toString();

        }

        @RequestMapping(value = "", method = RequestMethod.POST)
        public String postAccount(@RequestParam(value = "name") String name,
                                  @RequestParam(value = "money") double money) {
            Account account = new Account();
            account.setMoney(money);
            account.setName(name);
            Account account1 = accountDao.save(account);
            return account1.toString();

        }


    }
