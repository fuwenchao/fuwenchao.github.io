Resource
===============



.. code:: java

    Resource rs = new ClassPathResource("conf/file.txt");
    EncodedResource er = new EncodedResource(rs,"UTF-8");
    String content = FileCopyUtils.copyToString(er.getReader());
    System.out.println(content);