git使用总结
==========


.. image:: ./images/git-reset.png

**版本回退**

git reset --hard HEAD^

**回到未来的某个版本**

git reset --hard 1094a

版本号没必要写全，前几位就可以了，Git会自动去找。当然也不能只写前一两位，因为Git可能会找到多个版本号，就无法确定是哪一个了。   


**查看工作区和版本库里面最新版本的区别**

git diff HEAD -- readme.txt



**丢弃工作区的修改**

git checkout -- readme.txt

::

    命令git checkout -- readme.txt意思就是，把readme.txt文件在工作区的修改全部撤销，这里有两种情况：

    一种是readme.txt自修改后还没有被放到暂存区，现在，撤销修改就回到和版本库一模一样的状态；

    一种是readme.txt已经添加到暂存区后，又作了修改，现在，撤销修改就回到添加到暂存区后的状态。

    总之，就是让这个文件回到最近一次git commit或git add时的状态。

现在假定是凌晨3点，你不但写了一些胡话，还git add到暂存区了

庆幸的是，在commit之前，你发现了这个问题。用git status查看一下，修改只是添加到了暂存区，还没有提交：

**把暂存区的修改撤销掉（unstage），重新放回工作区**

git reset HEAD <file>

当我们用HEAD时，表示最新的版本


再用git status查看一下，现在暂存区是干净的，工作区有修改：

.. code:: java

    $ git status
    On branch master
    Changes not staged for commit:
      (use "git add <file>..." to update what will be committed)
      (use "git checkout -- <file>..." to discard changes in working directory)

        modified:   readme.txt

还记得如何丢弃工作区的修改吗？

.. code:: java

    $ git checkout -- readme.txt

    $ git status
    On branch master
    nothing to commit, working tree clean



查看分支：git branch

创建分支：git branch <name>

切换分支：git checkout <name>

创建+切换分支：git checkout -b <name>

合并某分支到当前分支：git merge <name>

删除分支：git branch -d <name>



解决冲突
-----------

1. git checkout -b feature1
2. change readme.txt
3. git add readme.txt
4. git checkout master
5. change readme.txt
6. git add readme.txt 
7. git commit -m "& simple"

now like 

.. image:: ./images/conflic1.png

8. git merge feature1

::

    Auto-merging readme.txt
    CONFLICT (content): Merge conflict in readme.txt
    Automatic merge failed; fix conflicts and then commit the result.

9. git status 可以告诉我们冲突的文件

::

    On branch master
    Your branch is ahead of 'origin/master' by 2 commits.
      (use "git push" to publish your local commits)

    You have unmerged paths.
      (fix conflicts and run "git commit")
      (use "git merge --abort" to abort the merge)

    Unmerged paths:
      (use "git add <file>..." to mark resolution)

        both modified:   readme.txt

    no changes added to commit (use "git add" and/or "git commit -a")

10. 修改 readme.txt

11. git add readme.txt 

12. git commit -m "conflict fixed"

now like 

.. image:: ./images/conflic2.png

13. git log --graph --pretty=oneline --abbrev-commit

14. git branch -d feature1



git回退 (master版)
----------------------

**这节我们讨论一下只用到主分支，，虽然这种情况不符合规范，但是在现实生活中，大部分用户是直接在master分支上工作的，所以这里我们不引入更加负责的分支操作，也不涉及tag操作，只讲在最简单的主分支上如何回退。** 

工作区 -> 暂存区 ->  本地仓库 -> 远程仓库

**git diff**

工作区 diff （ 暂存区 + 本地仓库 ）

**git diff --cached**

暂存区 diff 本地仓库

**git diff master origin/master**

本地 diff 远程

master ：本地仓库

origin/master ： 远程仓库

master 是主分支的意思，因为我们都在主分支工作（习惯，但是是不好的习惯）。所以两边都是master，而origin是远程的意思

知道怎么查看差异了，下一步就是看怎么回退。

**情况一 ： 还有没提交到暂存区**

也就是还没有 git add .

这时候我们用 git checkout -- file (git checkout .) 回退 或者 git reset --hard

**情况二：已暂存，未提交**

已经执行 git add . 但是还没有执行 git commit

::

    git reset
    git checkout .

    或者

    git reset --hard

**情况三：已提交，未推送**

这个时候你的代码已经进入本地仓库，但是没有进入远程

你已经做了 git add . 并且 git commit 动作，这个时候你的代码已经进入本地仓库，然后你后悔了，怎么办？

.. code :: java

    git reset --hard origin/master

origin/master代表远程仓库。既然你已经污染了你的本地仓库，那就从远程仓库把代码取回来吧。

**情况四：已推送**

你已经做了如下几个步骤

.. code:: java

    git add .
    git commit 
    git push

这个时候，如果你想恢复的话，由于你的本地仓库和远程仓库是等价的，你只需要先恢复本地仓库，然后push到远程

.. code:: java

    git reset --hard HEAD^
    git push -f 

