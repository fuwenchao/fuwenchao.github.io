自定义Filter
===================

我们常常在项目中会使用filters用于录调用日志、排除有XSS威胁的字符、执行权限验证等等。Spring Boot自动添加了OrderedCharacterEncodingFilter和HiddenHttpMethodFilter，并且我们可以自定义Filter。

两个步骤：



        1. 实现Filter接口，实现Filter方法
        2. 添加@Configurationz 注解，将自定义Filter加入过滤链

.. code:: java

    @Configuration
    public class WebConfiguration {
        @Bean
        public RemoteIpFilter remoteIpFilter() {
            return new RemoteIpFilter();
        }
        
        @Bean
        public FilterRegistrationBean testFilterRegistration() {

            FilterRegistrationBean registration = new FilterRegistrationBean();
            registration.setFilter(new MyFilter());
            registration.addUrlPatterns("/*");
            registration.addInitParameter("paramName", "paramValue");
            registration.setName("MyFilter");
            registration.setOrder(1);
            return registration;
        }
        
        public class MyFilter implements Filter {
            @Override
            public void destroy() {
                // TODO Auto-generated method stub
            }

            @Override
            public void doFilter(ServletRequest srequest, ServletResponse sresponse, FilterChain filterChain)
                    throws IOException, ServletException {
                // TODO Auto-generated method stub
                HttpServletRequest request = (HttpServletRequest) srequest;
                System.out.println("this is MyFilter,url :"+request.getRequestURI());
                filterChain.doFilter(srequest, sresponse);
            }

            @Override
            public void init(FilterConfig arg0) throws ServletException {
                // TODO Auto-generated method stub
            }
        }
    }