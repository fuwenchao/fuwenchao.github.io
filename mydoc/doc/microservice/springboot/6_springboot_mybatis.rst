springboot整合mybatis
===========================

引入依赖
-------------

.. code:: java

        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>1.3.0</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.0.29</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

引入数据源
------------

application.properties配置文件中引入数据源：

::

    spring.druid.driverClassName=com.mysql.jdbc.Driver
    spring.druid.url=jdbc:mysql://127.0.0.1:3306/houses?useUnicode=true&amp;amp;characterEncoding=UTF-8&amp;amp;zeroDateTimeBehavior=convertToNull
    spring.druid.username=root
    spring.druid.password=123456
    #druid\u75311.0.16\u5347\u7ea7\u52301.1.0,\u8fde\u63a5\u6c60\u914d\u7f6e\u8981\u6539\u6210maxActive,minIdle
    spring.druid.maxActive=30
    spring.druid.minIdle=5
    spring.druid.maxWait=10000
    spring.druid.validationQuery=SELECT 'x'

这样，springboot就可以访问数据了。

mybaties配置
-------------------

**mybatis-config.xml**

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

在application.properties中指定该配置文件

    mybatis.config-location=classpath:/mybatis/mybatis-config.xml


具体实现
---------------

详见代码 house_v1

**model**

.. code:: java

    package com.mooc.house.common.model;

    import java.util.Date;
    import java.util.List;

    import org.springframework.web.multipart.MultipartFile;

    import com.google.common.base.Joiner;
    import com.google.common.base.Splitter;
    import com.google.common.base.Strings;
    import com.google.common.collect.Lists;

    public class House {

      private Long id;
      private Integer type;
      private Integer price;
      private String  name;
      private String images;
      private Integer area;
      private Integer beds;
      private Integer baths;
      private Double  rating;
      
      private Integer roundRating = 0;
      private String  remarks;
      private String  properties;
      private String  floorPlan;
      private String  tags;
      private Date    createTime;
      private Integer cityId;
      private Integer    communityId;
      private String     address;
      
      private String     firstImg;
      
      private List<String> imageList = Lists.newArrayList();
      
      
      private List<String> floorPlanList = Lists.newArrayList();
      private List<String> featureList   = Lists.newArrayList();
      
      private List<MultipartFile> houseFiles;
      
      private List<MultipartFile> floorPlanFiles;
      
      
      private String priceStr;
      
      private String typeStr;
      
      
      private Long userId;
      
      private boolean bookmarked;
      
      private Integer state;
      
      private List<Long> ids;
      
      private String  sort = "time_desc";//price_desc,price_asc,time_desc
      
      public Long getId() {
        return id;
      }
      public void setId(Long id) {
        this.id = id;
      }
      public Integer getType() {
        return type;
      }
      public void setType(Integer type) {
        this.type = type;
        if (type.equals(1)) {
          this.typeStr = "For Sale";
        }else {
          this.typeStr = "For Rent";
        }
      }
      public Integer getPrice() {
        return price;
      }
      public void setPrice(Integer price) {
        this.price = price;
        this.priceStr = this.price + "万";
      }
     
      public Integer getArea() {
        return area;
      }
      public void setArea(Integer area) {
        this.area = area;
      }
      public Integer getBaths() {
        return baths;
      }
      public void setBaths(Integer baths) {
        this.baths = baths;
      }
      public String getRemarks() {
        return remarks;
      }
      public void setRemarks(String remarks) {
        this.remarks = remarks;
      }
      public String getProperties() {
        return properties;
      }
      public void setProperties(String properties) {
        this.properties = properties;
        this.featureList = Splitter.on(",").splitToList(properties);
      }
      public String getFloorPlan() {
        return floorPlan;
      }
      public void setFloorPlan(String floorPlan) {
        this.floorPlan = floorPlan;
        if (!Strings.isNullOrEmpty(floorPlan)) {
          this.floorPlanList = Splitter.on(",").splitToList(floorPlan);
        }
       
      }
      public boolean isBookmarked() {
        return bookmarked;
      }
      public void setBookmarked(boolean bookmarked) {
        this.bookmarked = bookmarked;
      }
      public String getTags() {
        return tags;
      }
      public List<Long> getIds() {
        return ids;
      }
      public void setIds(List<Long> ids) {
        this.ids = ids;
      }
      public void setTags(String tags) {
        this.tags = tags;
      }
      public List<String> getImageList() {
        return imageList;
      }
      public void setImageList(List<String> imageList) {
        this.imageList = imageList;
      }
      public String getSort() {
        return sort;
      }
      public void setSort(String sort) {
        this.sort = sort;
      }
      public Integer getState() {
        return state;
      }
      
      public String getFirstImg() {
        return firstImg;
      }
      public void setFirstImg(String firstImg) {
        this.firstImg = firstImg;
      }
      
      public void setState(Integer state) {
        this.state = state;
      }
      public Date getCreateTime() {
        return createTime;
      }
      public String getImages() {
        return images;
      }
      
      public void setImages(String images) {
        this.images = images;
        if (!Strings.isNullOrEmpty(images)) {
           List<String> list =  Splitter.on(",").splitToList(images);
           if (list.size() > 0) {
              this.firstImg = list.get(0);
              this.imageList = list;
           }
        }
      }
      
      public List<MultipartFile> getFloorPlanFiles() {
        return floorPlanFiles;
      }
      public void setFloorPlanFiles(List<MultipartFile> floorPlanFiles) {
        this.floorPlanFiles = floorPlanFiles;
      }
      public Long getUserId() {
        return userId;
      }
      public void setUserId(Long userId) {
        this.userId = userId;
      }
      public void setCreateTime(Date createTime) {
        this.createTime = createTime;
      }
      public Integer getCityId() {
        return cityId;
      }
      public void setCityId(Integer cityId) {
        this.cityId = cityId;
      }
      public Integer getCommunityId() {
        return communityId;
      }
      public void setCommunityId(Integer communityId) {
        this.communityId = communityId;
      }
      public String getName() {
        return name;
      }
      public void setName(String name) {
        this.name = name;
      }
      public String getPriceStr() {
        return priceStr;
      }
      public void setPriceStr(String priceStr) {
        this.priceStr = priceStr;
      }
      public String getTypeStr() {
        return typeStr;
      }
      public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
      }
      public Integer getBeds() {
        return beds;
      }
      public void setBeds(Integer beds) {
        this.beds = beds;
      }
      public String getAddress() {
        return address;
      }
      public void setAddress(String address) {
        this.address = address;
      }
      public List<String> getFloorPlanList() {
        return floorPlanList;
      }
      public void setFloorPlanList(List<String> floorPlanList) {
        this.floorPlanList = floorPlanList;
      }
      public List<String> getFeatureList() {
        return featureList;
      }
      public void setFeatureList(List<String> featureList) {
        this.featureList = featureList;
        this.properties = Joiner.on(",").join(featureList);
      }
      public Double getRating() {
        return rating;
      }
      public void setRating(Double rating) {
        this.rating = rating;
        this.roundRating = (int) Math.round(rating);
      }
      public Integer getRoundRating() {
        return roundRating;
      }
      public void setRoundRating(Integer roundRating) {
        this.roundRating = roundRating;
      }
      public List<MultipartFile> getHouseFiles() {
        return houseFiles;
      }
      public void setHouseFiles(List<MultipartFile> houseFiles) {
        this.houseFiles = houseFiles;
      }
      
      @Override
      public String toString() {
        return "House [id=" + id + ", type=" + type + ", price=" + price + ", name=" + name
            + ", images=" + images + ", area=" + area + ", beds=" + beds + ", baths=" + baths
            + ", rating=" + rating + ", roundRating=" + roundRating + ", remarks=" + remarks
            + ", properties=" + properties + ", floorPlan=" + floorPlan + ", tags=" + tags + ", createTime=" + createTime + ", cityId=" + cityId
            + ", communityId=" + communityId + ", address=" + address + ", firstImg=" + firstImg
            + ", floorPlanList=" + floorPlanList + ", featureList=" + featureList + ", houseFiles="
            + houseFiles + ", floorPlanFiles=" + floorPlanFiles + ", priceStr=" + priceStr
            + ", typeStr=" + typeStr + ", userId=" + userId  + ", bookmarked="
            + bookmarked + ", state=" + state + ", ids=" + ids + ", sort=" + sort + "]";
      }

    }


**Mapper接口**

.. code:: java

    package com.mooc.house.biz.mapper;

    import java.util.List;

    import org.apache.ibatis.annotations.Mapper;
    import org.apache.ibatis.annotations.Param;

    import com.mooc.house.common.model.Blog;
    import com.mooc.house.common.page.PageParams;

    @Mapper
    public interface BlogMapper {

      public List<Blog> selectBlog(@Param("blog")Blog query, @Param("pageParams")PageParams params);

      public Long selectBlogCount(Blog query);

    }

看到其他人的另一种写法[然后没看到写mybatis-config.xml]

.. code:: java

    @Mapper
    public interface AccountMapper {

        @Insert("insert into account(name, money) values(#{name}, #{money})")
        int add(@Param("name") String name, @Param("money") double money);

        @Update("update account set name = #{name}, money = #{money} where id = #{id}")
        int update(@Param("name") String name, @Param("money") double money, @Param("id") int  id);

        @Delete("delete from account where id = #{id}")
        int delete(int id);

        @Select("select id, name as name, money as money from account where id = #{id}")
        Account findAccount(@Param("id") int id);

        @Select("select id, name as name, money as money from account")
        List<Account> findAccountList();
    }


**Mapper.xml**

.. code:: java

    <?xml version="1.0" encoding="UTF-8" ?>
    <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
    <mapper namespace="com.mooc.house.biz.mapper.BlogMapper">

         <sql id="blogField">
             id,tags ,content,create_time,title
         </sql>
       
         <select id="selectBlog" resultType="blog">
           select <include refid="blogField"/>
           from   blog a
           <where>
             <if test="blog.id != null   and blog.id != 0">
                 and id   = #{blog.id}
             </if>
          </where>
           order by a.create_time desc
           <if test='pageParams.offset != null and pageParams.limit != null'>
                limit #{pageParams.offset}, #{pageParams.limit}
           </if>
           <if test='pageParams.offset == null and pageParams.limit != null'>
                limit #{pageParams.limit}
           </if>
        </select>
        
        <select id="selectBlogCount" resultType="long">
           select count(id)
           from   blog a
           <where>
             <if test="id != null   and id != 0">
                 and id   = #{id}
             </if>
          </where>
           order by a.create_time desc
        </select>
        
        

    </mapper> 

**Service**

.. code:: java

    @Service
    public class BlogService {
      
      @Autowired
      private BlogMapper blogMapper;

      public PageData<Blog> queryBlog(Blog query, PageParams params) {
        List<Blog> blogs =  blogMapper.selectBlog(query,params);
        populate(blogs);
        Long  count =  blogMapper.selectBlogCount(query);
        return PageData.<Blog>buildPage(blogs, count, params.getPageSize(), params.getPageNum());
      }

      private void populate(List<Blog> blogs) {
        if (!blogs.isEmpty()) {
          blogs.stream().forEach(item -> {
            String stripped =  Jsoup.parse(item.getContent()).text();
            item.setDigest(stripped.substring(0, Math.min(stripped.length(),40)));
            String tags = item.getTags();
            item.getTagList().addAll(Lists.newArrayList(Splitter.on(",").split(tags)));
          });
        }
      }

      public Blog queryOneBlog(int id) {
        Blog query = new Blog();
        query.setId(id);
        List<Blog> blogs = blogMapper.selectBlog(query, new PageParams(1, 1));
        if (!blogs.isEmpty()) {
          return blogs.get(0);
        }
        return null;
      }

      

    }


**开启声明式事务**

类似于

.. code:: java

    @Service
    public class AccountService2 {

        @Autowired
        AccountMapper2 accountMapper2;

        @Transactional
        public void transfer() throws RuntimeException{
            accountMapper2.update(90,1);//用户1减10块 用户2加10块
            int i=1/0;
            accountMapper2.update(110,2);
        }
    }

@Transactional，声明事务，并设计一个转账方法，用户1减10块，用户2加10块。在用户1减10 ，之后，抛出异常，即用户2加10块钱不能执行，当加注解@Transactional之后，两个人的钱都没有增减。当不加@Transactional，用户1减了10，用户2没有增加，即没有操作用户2 的数据。可见@Transactional注解开启了事物。

springboot 开启事物很简单，只需要加一行注解就可以了，前提你用的是jdbctemplate, jpa, mybatis，这种常见的orm。


**Controller**

构建restful API

.. code:: java

    package com.mooc.house.web.controller;

    import java.util.List;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.ModelMap;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestMethod;

    import com.mooc.house.biz.service.BlogService;
    import com.mooc.house.biz.service.CommentService;
    import com.mooc.house.biz.service.RecommendService;
    import com.mooc.house.common.constants.CommonConstants;
    import com.mooc.house.common.model.Blog;
    import com.mooc.house.common.model.Comment;
    import com.mooc.house.common.model.House;
    import com.mooc.house.common.page.PageData;
    import com.mooc.house.common.page.PageParams;

    @Controller
    public class BlogController {
      
      @Autowired
      private BlogService blogService;
      
      @Autowired
      private CommentService commentService;
      
      @Autowired
      private RecommendService recommendService;
      
      @RequestMapping(value="blog/list",method={RequestMethod.POST,RequestMethod.GET})
      public String list(Integer pageSize,Integer pageNum,Blog query,ModelMap modelMap){
        PageData<Blog> ps = blogService.queryBlog(query,PageParams.build(pageSize, pageNum));
        List<House> houses =  recommendService.getHotHouse(CommonConstants.RECOM_SIZE);
        modelMap.put("recomHouses", houses);
        modelMap.put("ps", ps);
        return "/blog/listing";
      }
      
      @RequestMapping(value="blog/detail",method={RequestMethod.POST,RequestMethod.GET})
      public String blogDetail(int id,ModelMap modelMap){
        Blog blog = blogService.queryOneBlog(id);
        List<Comment> comments = commentService.getBlogComments(id,8);
        List<House> houses =  recommendService.getHotHouse(CommonConstants.RECOM_SIZE);
        modelMap.put("recomHouses", houses);
        modelMap.put("blog", blog);
        modelMap.put("commentList", comments);
        return "/blog/detail";
      }
    }



--------

参考

https://blog.csdn.net/forezp/article/details/70768477