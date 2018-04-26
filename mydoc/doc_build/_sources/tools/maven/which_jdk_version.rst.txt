maven中如何指定jdk的版本
------------------------


maven中jdk的配置分为全局配置和局部配置两种方式

**全局配置**

全局配置是指在${MAVEN_HOME}\conf\settings.xml中进行配置，
注${MAVEN_HOME}指的是maven的安装目录。
例如，要配置jdk1.8,打开settings.xml这个文件，
然后在<profiles> </profiles>之间添加如下代码。

.. code:: java

	<profile>
		<id>jdk18</id>
		<activation>
			<activeByDefault>true</activeByDefault>
			<jdk>1.8</jdk>
		</activation>
		<properties>
			<maven.compiler.source>1.8</maven.compiler.source>
			<maven.compiler.target>1.8</maven.compiler.target>
			<maven.compiler.compilerVersion>1.8</maven.compiler.compilerVersion>
		</properties>	
	</profile>

全局配置的好处就是省事、方便。
一次配置以后，再使用maven构建项目，项目编译时，默认使用jdk1.8进行编译。 


**局部配置**

局部配置就是只针对具体某个项目进行配置的。具体就是，在项目的pom.xml文件中添加如下代码，

.. cod:: java

	<build>
		<plugins>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
				<configuration>
					<source>1.7</source>
					<target>1.7</target>
				</configuration>
			</plugin>
		</plugins>
	</build>



经过自己的一番探索后发现，maven在新建工程时会优先读取自己
本地仓库配置文件中的jdk版本限制的那段内容，如果自己本地
仓库没有对jdk版本限制，这时才会去读取maven安装包中对JDK版本的限制的那段代码，
因此是有这样一个先后顺序的规律的。所以说我们只需要在自己本地maven仓库中配置对
JDK版本的限制就好了，不需要去maven安装包的仓库中进行类似的配置。




第二种方法就是将自己本地maven仓库和maven安装包仓库中
的有关jdk版本限制的内容全部注释掉或者删除掉，
这样新建的maven工程就会默认使用你本地安装的jdk的版本。

我自己安装的是jdk1.8,新建maven工程后如下图所示： 