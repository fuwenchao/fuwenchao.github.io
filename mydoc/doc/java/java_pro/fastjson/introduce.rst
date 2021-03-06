FastJson 使用指南
=========================

JSON String 与 JSON Object 的转换
---------------------------------------------

::

    // 简单对象型
    private static final String  JSON_OBJ_STR = "{\"studentName\":\"lily\",\"studentAge\":12}";

    // json字符串^数组类型
    private static final String  JSON_ARRAY_STR = "[{\"studentName\":\"lily\",\"studentAge\":12}
    ,{\"studentName\":\"lucy\",\"studentAge\":15}]";

    // 复杂格式json字符串
    private static final String  COMPLEX_JSON_STR = "{\"teacherName\":\"crystall\",\"teacherAge\":27,
    \"course\":{\"courseName\":\"english\",\"code\":1270},
    \"students\":[{\"studentName\":\"lily\",\"studentAge\":12}
    ,{\"studentName\":\"lucy\",\"studentAge\":15}]}";





json str ^^> json object
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

::

    JSONObject jsonObject = JSONObject.parseObject(JSON_STR);
    System.out.println("studentName:  " + jsonObject.getString("studentName") + ":" + "  studentAge:  "
            + jsonObject.getInteger("studentAge"));





json object ^^> json str
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

::

    // 第一种方式
    String jsonString = JSONObject.toJSONString(jsonObject);

    // 第二种方式
    String jsonString = jsonObject.toJSONString();
    System.out.println(jsonString);


json Str ^^> json Arr
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. code:: java

        JSONArray jsonArray = JSONArray.parseArray(JSON_ARRAY_STR);

        //遍历方式1
        int size = jsonArray.size();
        for (int i = 0; i < size; i++) {

            JSONObject jsonObject = jsonArray.getJSONObject(i);
            System.out.println("studentName:  " + jsonObject.getString("studentName") + ":" + "  studentAge:  "
                    + jsonObject.getInteger("studentAge"));
        }

        //遍历方式2
        for (Object obj : jsonArray) {

            JSONObject jsonObject = (JSONObject) obj;
            System.out.println("studentName:  " + jsonObject.getString("studentName") + ":" + "  studentAge:  "
                    + jsonObject.getInteger("studentAge"));
        }

json Arr ^^> json Str
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. code:: java

        //第一种方式
        String jsonString = JSONArray.toJSONString(jsonArray);

        // 第二种方式
        //String jsonString = jsonArray.toJSONString(jsonArray);
        System.out.println(jsonString);


复杂json格式字符串与JSONObject之间的转换
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

**复杂json格式字符串到JSONObject的转换**

.. code:: java

    JSONObject jsonObject = JSONObject.parseObject(COMPLEX_JSON_STR);

    String teacherName = jsonObject.getString("teacherName");
    Integer teacherAge = jsonObject.getInteger("teacherAge");

    System.out.println("teacherName:  " + teacherName + "   teacherAge:  " + teacherAge);

    JSONObject jsonObjectcourse = jsonObject.getJSONObject("course");
     //获取JSONObject中的数据
    String courseName = jsonObjectcourse.getString("courseName");
    Integer code = jsonObjectcourse.getInteger("code");

    System.out.println("courseName:  " + courseName + "   code:  " + code);

    JSONArray jsonArraystudents = jsonObject.getJSONArray("students");

    //遍历JSONArray
    for (Object object : jsonArraystudents) {

        JSONObject jsonObjectone = (JSONObject) object;
        String studentName = jsonObjectone.getString("studentName");
        Integer studentAge = jsonObjectone.getInteger("studentAge");

        System.out.println("studentName:  " + studentName + "   studentAge:  " + studentAge);
    }


**复杂JSONObject到json格式字符串的转换**

.. code:: java

::

    //第一种方式
    //String jsonString = JSONObject.toJSONString(jsonObject);

    //第二种方式
    String jsonString = jsonObject.toJSONString();


JSON格式字符串与javaBean之间的转换
-----------------------------------------

**json字符串-简单对象到JavaBean之间的转换**

.. code:: java


    // 第一种方式,使用TypeReference<T>类,由于其构造方法使用protected进行修饰,故创建其子类
    Student student = JSONObject.parseObject(JSON_OBJ_STR, new TypeReference<Student>() {});

    // 第二种方式,使用Gson的思想
    Student student = JSONObject.parseObject(JSON_OBJ_STR, Student.class);



**JavaBean到json字符串-简单对象的转换**

.. code:: java


    Student student = new Student("lily", 12);
    String jsonString = JSONObject.toJSONString(student);
    System.out.println(jsonString);




json字符串-数组类型与javaBean之间的转换
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

**json字符串-数组类型到JavaBean_List的转换**

.. code:: java

    //第二种方式,使用TypeReference<T>类,由于其构造方法使用protected进行修饰,故创建其子类
    List<Student> studentList = JSONArray.parseObject(JSON_ARRAY_STR, new TypeReference<ArrayList<Student>>() {});
    System.out.println("studentList:  " + studentList);

    //第三种方式,使用Gson的思想
    List<Student> studentList1 = JSONArray.parseArray(JSON_ARRAY_STR, Student.class);
    System.out.println("studentList1:  " + studentList1);



**JavaBean_List到json字符串-数组类型的转换**

.. code:: java

    Student student = new Student("lily", 12);
    Student studenttwo = new Student("lucy", 15);

    List<Student> students = new ArrayList<Student>();
    students.add(student);
    students.add(studenttwo);

    String jsonString = JSONArray.toJSONString(students);
    System.out.println(jsonString);


复杂json格式字符串与与javaBean之间的转换
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

**复杂json格式字符串到JavaBean_obj的转换**

.. code:: java

    //第一种方式,使用TypeReference<T>类,由于其构造方法使用protected进行修饰,故创建其子类
    Teacher teacher = JSONObject.parseObject(COMPLEX_JSON_STR, new TypeReference<Teacher>() {});
    System.out.println(teacher);

    //第二种方式,使用Gson思想
    Teacher teacher1 = JSONObject.parseObject(COMPLEX_JSON_STR, Teacher.class);
    System.out.println(teacher1);

**复杂JavaBean_obj到json格式字符串的转换**

.. code:: java

    //已知复杂JavaBean_obj
    Teacher teacher = JSONObject.parseObject(COMPLEX_JSON_STR, new TypeReference<Teacher>() {});
    String jsonString = JSONObject.toJSONString(teacher);
    System.out.println(jsonString);


参考文档 https://segmentfault.com/a/1190000011212806

github https://github.com/fuwenchao/ALiFastJson