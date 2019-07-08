将本地代码推送到github
==========================



1：git init 创建本地库

2：将代码copy到本地库

3：git add .

4：提交到本地库

      git commit -m "scala impatient learning first commit"

5：将本地库增加到远程,相当于建立关联 ，需先到github上建立该repo

        git remote add origin https://github.com/fuwenchao/scalaimpatient.git

6：将代码推送到远程

  上传之前先pull : 

  git pull origin master

  git push -u origin master




将本地dev推动到远程dev（如果远程没有，则创建）

 git push origin dev

（如果要推送到远程dev，当前需在dev分支下，master也是）

你的小伙伴从远程库clone时，默认情况下，你的小伙伴只能看到本地的master分支。不信可以用git branch命令看看：

::

  $ git branch
  * master

现在，你的小伙伴要在dev分支上开发，就必须创建远程origin的dev分支到本地，于是他用这个命令创建本地dev分支：

::

  $ git checkout -b dev origin/dev    #相当于本地dev分支与远程dev分支建立关联，这样就可以 
    直接push了，如果之前没有执行这一步，可以：
      git pull origin dev 本地分支与远程分支相关联，或者：
     git branch --set-upstream dev origin/dev

现在，他就可以在dev上继续修改，然后，时不时地把dev分支push到远程：

::

  $ git commit -m "add /usr/bin/env"
  [dev 291bea8] add /usr/bin/env
   1 file changed, 1 insertion(+)
  $ git push origin dev
  Counting objects: 5, done.
  Delta compression using up to 4 threads.
  Compressing objects: 100% (2/2), done.
  Writing objects: 100% (3/3), 349 bytes, done.
  Total 3 (delta 0), reused 0 (delta 0)
  To git@github.com:michaelliao/learngit.git
     fc38031..291bea8  dev -> dev




问题一：Git 里面的 origin 到底代表啥意思

::

  origin 是默认的远程版本库名称
  你可以在 .git/config 之中进行修改

  事实上 git push origin master 的意思是 git push origin master:master （将本地的 master 分支推送至远端的 master 分支，如果没有就新建一个）



::


  …or create a new repository on the command line
  echo "# sprintbootdemo" >> README.md
  git init
  git add README.md
  git commit -m "first commit"
  git remote add origin https://github.com/fuwenchao/sprintbootdemo.git
  git push -u origin master

  …or push an existing repository from the command line
  git remote add origin https://github.com/fuwenchao/sprintbootdemo.git
  git push -u origin master