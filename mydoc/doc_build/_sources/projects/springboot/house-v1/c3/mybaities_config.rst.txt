spring-boot 以前方式
----------------------

xml

spring-boot 方式
-------------------

starter方式

.. code:: java

	<dependency>
	  <groupId>org.mybatis.spring.boot</groupId>
	  <artifactId>mybatis-spring-boot-starter</artifactId>
	  <version>1.2.0</version>
    </dependency>
    

    <dependency>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-starter-mail</artifactId>
    </dependency>


连接池配置

.. code:: java

	spring.druid.driverClassName=com.mysql.jdbc.Driver
	spring.druid.url=jdbc:mysql://127.0.0.1:3306/houses?useUnicode=true&amp;amp;characterEncoding=UTF-8&amp;amp;zeroDateTimeBehavior=convertToNull
	spring.druid.username=root
	spring.druid.password=123456
	#druid\u75311.0.16\u5347\u7ea7\u52301.1.0,\u8fde\u63a5\u6c60\u914d\u7f6e\u8981\u6539\u6210maxActive,minIdle
	spring.druid.maxActive=30
	spring.druid.minIdle=5
	spring.druid.maxWait=10000
	spring.druid.validationQuery=SELECT 'x'


	mybatis.config-location=classpath:/mybatis/mybatis-config.xml


mybaties配置

.. code:: java

	<?xml version="1.0" encoding="UTF-8"?>
	<!DOCTYPE configuration
	        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
	        "http://mybatis.org/dtd/mybatis-3-config.dtd">
	<configuration>
	    <settings>
	         <!-- 配置关闭缓存  -->
	         <setting name="cacheEnabled" value="false"/>
	         <setting name="mapUnderscoreToCamelCase" value="true"/>
	         <setting name="useGeneratedKeys" value="true"/>
	         <setting name="defaultExecutorType" value="REUSE"/>
	         <!-- 事务超时时间 -->
	         <setting name="defaultStatementTimeout" value="600"/>
	    </settings>
	    
	    
	    <typeAliases>
	       <typeAlias type="com.mooc.house.common.model.User" alias="user" />
	        <typeAlias type="com.mooc.house.common.model.Agency"      alias="agency"/>
	       <typeAlias type="com.mooc.house.common.model.House" alias="house" />
	       <typeAlias type="com.mooc.house.common.model.City" alias="city" />
	       <typeAlias type="com.mooc.house.common.model.Comment"     alias="comment"/>
	       <typeAlias type="com.mooc.house.common.model.Community" alias="community" />
	       <typeAlias type="com.mooc.house.common.model.HouseUser" alias="houseUser" />
	       <typeAlias type="com.mooc.house.common.model.Blog"        alias="blog"/>
	       <typeAlias type="com.mooc.house.common.model.User"        alias="user"/>
	       <typeAlias type="com.mooc.house.common.model.UserMsg"     alias="userMsg"/>
	       <typeAlias type="com.mooc.house.common.model.HouseUser"     alias="houseUser"/>
	    
	    </typeAliases>
	    
	    <mappers>
	       <mapper resource="mapper/user.xml" />
	       <mapper resource="mapper/blog.xml"/> 
	       <mapper resource="mapper/house.xml" />
	       <mapper resource="mapper/comment.xml"/> 
	       <mapper resource="mapper/agency.xml" />
	    </mappers>

	</configuration>



MybatisAutoConfigration  引入mybatis基础的Bean， 无须xml进行Bean的配置