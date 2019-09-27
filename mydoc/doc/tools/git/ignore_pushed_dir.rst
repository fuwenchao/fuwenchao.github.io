忽略掉已经提交的目录
=========================


创建idea项目的时候,不小心将 target 目录 和 out 目录 提交到了github,现需要重新忽略掉这两个目录


首先 github ignore

::

  /target/
  /out/
  !.mvn/wrapper/maven-wrapper.jar

  ### STS ###
  .apt_generated
  .classpath
  .factorypath
  .project
  .settings
  .springBeans
  .sts4-cache
  .DS_Store
  ### IntelliJ IDEA ###
  .idea
  *.iws
  *.iml
  *.ipr

  ### NetBeans ###
  /nbproject/private/
  /build/
  /nbbuild/
  /dist/
  /nbdist/
  /.nb-gradle/


然后忽略掉已经put的

::

  git rm -r --cached target
  git rm -r --cached out
  cat .gitignore
  git status
  git commit -m "rm out and target"
  git push
