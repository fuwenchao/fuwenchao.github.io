MANIFEST.MF 文件详解
=========================



1. 在命令行中输入“jar -help”，就k可看到jar的详细用法了。

2. 示例：

   1）将两个class文档归档到一个jar文件中：jar -cvf  XX.jar  A.class B.class

   2 )使用清单文件Manifest.mf将dir目录下的所有文件归档到一个jar中：jar -cvfm   XX.jar  Manifest.mf dir/*

manifest 文件的格式很简单，每一行都是  " 名－值 "  对应的：

属性名开头，接着是 ":" ，然后是属性值，每行最多72个字符，如果需要增加，你可以在下一行续行，续行以空格开头，以空格开头的行       都会被视为前一行的续行。 
使用 m 选项，把指定文件名的manifest文件传入，

例如 

        jar cvfm myapplication.jar myapplication.mf [-C]classdir


现在我们来体验一下manifest文件的作用，如果现在我们有一个Java 应用程序打包在myapplication.jar中， 
main class为 

    com.example.myapp.MyAppMain ，

那么我们可以用以下的命令来运行 

    java -classpath myapplication.jar com.example.myapp.MyAppMain

这显然太麻烦了，现在我们来创建自己的manifest文件，如下：

::

    Manifest-Version: 1.0
    Created-By: JDJ example
    Main-Class: com.example.myapp.MyAppMain

这样我们就可以使用如下的命令来运行程序了：（明显简单多了，也不会造成无谓的拼写错误）

    java -jar myapplication.jar


管理JAR的依赖资源
------------------------



很少Java应用会仅仅只有一个jar文 件，一般还需要 其他类库。比如我的应用程序用到了Sun 的 Javamail classes ，在classpath中我需要包含activation.jar 和 mail.jar,这样在运行程序时,相比上面的例子,我们要增加一些:

    java -classpath mail.jar:activation.jar -jar myapplication.jar

在不同的操作系统中,jar包间的分隔符也不一样，在UNIX用“:”，在 window中使用 “;”，这样也不方便

同样，我们改写我们的manifest文件，如下

::

    Manifest-Version: 1.0
    Created-By: JDJ example
    Main-Class: com.example.myapp.MyAppMain
    Class-Path: mail.jar activation.jar

（加入了Class-Path: mail.jar activation.jar，用空格分隔两个jar包）
这样我们仍然可以使用和上例中相同的命令来执行该程序：

    java -jar myapplication.jar

Class-Path属性中包含了用空格分隔的jar文件，在这些jar文件名中要对特定的字符使用逃逸符，比如空格，要表示成" "，在路径的表示中，都采用“/” 来分隔目录()，无论是在什么操作系统中，(即使在window中)，而且这里用的是相对路径（相对于本身的JAR文 件）： 

::

    Manifest-Version: 1.0
    Created-By: JDJ example
    Main-Class: com.example.myapp.MyAppMain
    Class-Path: ext/mail.jar ext/activation.jar


Multiple Main Classes（多主类）
还有一种Multiple Main Classes情况，如果你的应用程序可能有命令行版本 和GUI版本，或者一些不同的应用却共享很多相同的代码，这时你可能有多个Main Class，我们建议你采取这样的策略：把共享的类打成lib包，然后把不同的应用打成不同的包，分别标志主类：如下

::


    Manifest for myapplicationlib.jar:
    Manifest-Version: 1.0
    Created-By: JDJ example
    Class-Path: mail.jar activation.jar

    Manifest for myappconsole.jar:
    Manifest-Version: 1.0
    Created-By: JDJ example
    Class-Path: myapplicationlib.jar
    Main-Class: com.example.myapp.MyAppMain

    Manifest for myappadmin.jar:
    Manifest-Version: 1.0
    Created-By: JDJ example
    Class-Path: myapplicationlib.jar
    Main-Class: com.example.myapp.MyAdminTool

在myappconsole.jar 和 myappadmin.jar的manifest文件中分别注明各自的 
Main Class Package Versioning
完成发布后，如果使用者想了解 ，哪些代码是谁的？目前是什么版本？使用什么版本的类库？解决的方法很多 ，manifest提供了一个较好的方法，你可以在manifest文件中描述每一个包的信息。


Java 秉承了实现说明与描述分离的原则，package 的描述 定义了package 是什么，实现说明 定义了谁提供了描述的实现，描述和实现包含 名、版本号和提供者。要得到这些信息，可以查看JVM的系统属性（使用 java.lang.System.getProperty() ）
在manifest文件中，我可以为每个package定义描述和实现版本，声明名字，并加入描述属性和实现属性，这些属性是

::

    Specification-Title
    Specification-Version
    Specification-Vendor
    Implementation-Title
    Implementation-Version
    Implementation-Vendor

当要提供一个类库或编程接口时，描述信息显得是很重要，见以下范例：

::

    Manifest-Version: 1.0
    Created-By: JDJ example
    Class-Path: mail.jar activation.jar
    Name: com/example/myapp/
    Specification-Title: MyApp
    Specification-Version: 2.4
    Specification-Vendor: example.com
    Implementation-Title: com.example.myapp
    Implementation-Version: 2002-03-05-A
    Implementation-Vendor: example.com

Package Version 查询
------------------------------

在manifest文件中加入package描述后，就可以使用Java提供的java.lang.Package class进行Package 的信息查询，这里列举3个最基本的获取package object的方法

::

    1.Package.getPackages():返回系统中所有定义的package列表
    2.Package.getPackage(String packagename):按名返回package
    3.Class.getPackage():返回给定class所在的package


使用者这方法就可以动态的获取package信息.
需要注意的是如果给定的package中没有class被加载,则也无法获得package 对象

 

Manifest 技巧
--------------------------


总是以Manifest-Version属性开头,
每行最长72个字符，如果超过的化，采用续行
确认每行都以回车结束，否则改行将会被忽略
如果Class-Path 中的存在路径，使用"/"分隔目录，与平台无关
使用空行分隔主属性和package属性
使用"/"而不是"."来分隔package 和class ,比如 com/example/myapp/
class 要以.class结尾，package 要以 / 结尾



打包可执行jar包时，MANIFEST.MF总是个让人头疼的东西，经常出现这种那种问题。 
一个例子： 


::

    ================================================================================ 
    Manifest-Version: 1.0 
    Main-Class: test.Main 
    Class-Path: ./ ./lib/commons-collections-3.2.jar ./lib/commons-dbcp-1.2.2.jar 
      ./lib/commons-lang-2.3.jar ./lib/commons-logging-1.1.jar 
    ================================================================================ 

各部分解释： 

::

    Manifest-Version MF文件版本号 
    Main-Class 包含main方法的类 
    Class-Path 执行这个jar包时的ClassPath 


以下是需要注意的各个要点： 

1. Manifest-Version、Main-Class和Class-Path后面跟着一个英文的冒号，冒号后面必须跟着一个空格，然后才是版本号、类和ClassPath。 
2. Class-Path中的各项应使用空格分隔，不是逗号或分号。 
3. Class-Path中如果有很多项，写成一行打包的时候会报错line too long，这时需要把Class-Path分多行写。注意：从第二行开始，必须以两个空格开头，三个以上我没试过，不过不用空格开头和一个空格开头都是不行的，我已经试过了。 
4. Class-Path写完之后最后一定要有一个空行。 
5. jar包内有些配置文件想放在jar包外面，比如文件config.properties：如果这个文件是以路径方式载入的，比如new file("./config/config.properties")，那么将config.properties放在jar包相同目录下的config目录下即可，也就是说“./”路径等价于jar包所在目录；如果这个文件是以ClassPath下的文件这种方式载入的，比如在Spring中载入classpath:config.properties，则在MF文件的配置文件的ClassPath中添加“./”，然后将这个配置文件与jar包放在同一个目录即可，当然也可以在MF文件的配置文件的ClassPath中添加“./config/”，然后把配置文件都放在jar包相同目录下的config目录下。
