springboot controller接受参数的几种常用方式
============================================


1. @PathVariable
-------------------------

获取路径上的参数


.. code:: java

    @GetMapping(value = "/say/{id}")
    public String say(@PathVariable("id") Integer id){
        return "id " + id ;
    }

2. @RequestParam
------------------------

获取查询参数，即 **url?name="xxx"** 这种形式

.. code:: java

    @GetMapping(value = "/partest/{id}")
    public String paraTest(@PathVariable("id") Integer id,@RequestParam(name="name") String name){
        return "id " + id  + " name " + name;
    }

查询url : loalhost:8080/hello/partest/100?name=wenchaofu

3. body 参数
-------------------------



body: 

::

    {
        "name":"wenchaofu",
        "age"：18，
        "hobby":"basketball"
    }



.. code:: java

    @PostMapping(path = "/demo1")
    public void demo1(@RequestBody Person person){
        sout(person.toString());
    }

or

.. code:: java

    @PostMapping(path = "demp1")
    public void demo1(@RequestBody Map<String,String> person){
        sout(person.get("name"));
    }


4. 无注解
---------------

.. code:: java

    @PostMapping(path = "/demo1")
    public void demo1(Person person){
        sout(person.toString());
    }

5. HttpServletRequest
-------------------------------

.. code:: java


    @GetMapping(value = "/partest")
    public String parTest2(HttpServletRequest httpServletRequest) {
        Map<String, String[]> parameterMap = httpServletRequest.getParameterMap();
        for (Map.Entry<String, String[]> stringEntry : parameterMap.entrySet()) {
            System.out.println(stringEntry.getKey() + "  " + stringEntry.getValue()[0] );
        }
        return null;
    }



url : http://localhost:8082/hello/partest?name=3