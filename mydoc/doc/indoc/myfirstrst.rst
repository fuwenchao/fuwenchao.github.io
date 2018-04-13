Sphinx使用说明
===============



幻灯片
------

1. 使用rst2s5生成

    - 安装docutils 
        pip install docutils

    - 生成ppt
        rst2s5.py --theme small-white slid.rst slid.html

2 使用rst2html5

    - 参考:https://github.com/marianoguerra/rst2html5

    via pip:

        pip install rst2html5-tools

    locally:

        git clone https://github.com/marianoguerra/rst2html5.git
        cd rst2html5
        git submodule init
        git submodule update

        sudo python setup.py install

    - use it

            to generate a basic html document:

            rst2html5 examples/slides.rst > clean.html

            to generate a set of slides using deck.js:

            rst2html5 --deck-js --pretty-print-code --embed-content examples/slides.rst > deck.html

            to generate a set of slides using reveal.js:

            rst2html5 --jquery --reveal-js --pretty-print-code examples/slides.rst > reveal.html

            to generate a set of slides using impress.js:

            rst2html5 --stylesheet-path=html5css3/thirdparty/impressjs/css/impress-demo.css --impress-js examples/impress.rst > output/impress.html

            to generate a page using bootstrap:

            rst2html5 --bootstrap-css --pretty-print-code --jquery --embed-content examples/slides.rst > bootstrap.html

            to higlight code with pygments:

            rst2html5 --pygments examples/codeblock.rst > code.html

            note that you will have to add the stylesheet for the code to actually highlight, this just does the code parsing and html transformation.

            to embed images inside the html file to have a single .html file to distribute add the --embed-images option.

            post processors support optional parameters, they are passed with a command line option with the same name as the post processor appending "-opts" at the end, for example to change the revealjs theme you can do:

            rst2html5 --jquery --reveal-js --reveal-js-opts theme=serif examples/slides.rst > reveal.html

            you can also pass the base path to the theme css file:

            rst2html5 --jquery --reveal-js --reveal-js-opts theme=serif,themepath=~/mytheme examples/slides.rst > reveal.html

            it will look at the theme at ~/mytheme/serif.css

            options are passed as a comma separated list of key value pairs separated with an equal sign, values are parsed as json, if parsing fails they are passed as strings, for example here is an example of options:

            --some-processor-opts theme=serif,count=4,verbose=true,foo=null

            if a key is passed more than once that parameter is passed to the processor as a list of values, note that if only one value is passed it's passed as it is, the convenience function as_list is provided to handle this case if you want to always receive a list.

            to add custom js files to the resulting file you can use the --add-js post processor like this:

            rst2html5 slides.rst --add-js --add-js-opts path=foo.js,path=bar.js

            that command will add foo.js and bar.js as scripts in the resulting html file.
            Pretty Print Code Notes

            enable it:

            --pretty-print-code

            add language specific lexers:

            --pretty-print-code-opts langs=clj:erlang

            Note: you have to pass both options when passing opts to prettify like this:

            --pretty-print-code --pretty-print-code-opts langs=clj:erlang




其他普通文档
==============

    - sphinx
    

        中文文档-https://sphinx-doc-zh.readthedocs.io/en/latest/

        - 安装：

            sudo pip install sphinx
                在cmd中输入命令 easy_install -U Sphinx   
                在安装过程中，你会发现，sphinx依赖于docutils和pygment：
                docutils是sphinx的基础,其实使用docutils自带的脚本可以进行rst转html的工作
                但是无法识别一些扩展的元素和代码高亮,这些都是通过sphinx实现的。
                pygments是python实现的对python代码进行高亮的扩展,他和sphinx协同工作
                就可以在生成的文档中对代码进行高亮。pygments的官网还有很多其他代码高亮的项目链接,
                包括js，java等。

        - 快速开始：

            mkdir mydoc && cd mydoc
            sphinx-quickstart

            - 生成目录如下

            .. code:: java

                .
                ├── Makefile
                ├── _build
                ├── _static
                ├── conf.py
                └── index.rst




            让我们详细研究一下每个文件。

                - Makefile：编译过代码的开发人员应该非常熟悉这个文件，如果不熟悉，那么可以将它看作是一个包含指令的文件，
                            在使用 make 命令时，可以使用这些指令来构建文档输出.
                - _build：这是触发特定输出后用来存放所生成的文件的目录.
                - _static：所有不属于源代码（如图像）一部分的文件均存放于此处，稍后会在构建目录中将它们链接在一起。
                - conf.py：这是一个 Python 文件，用于存放 Sphinx 的配置值，包括在终端执行 sphinx-quickstart 时选中的那些值。
                - index.rst：文档项目的 root 目录。如果将文档划分为其他文件，该目录会连接这些文件。

        - 转rst为html

               sphinx安装完毕后，就可以将rst转成html了。在cmd下的命令为：

               sphinx-build -b html <srcDir> <dstDir> [filenames]

               srcDir是makefile和conf.py所在的目录，
               dstDir则可以随意指定，这里cd到doc的所在的文件夹下，执行：
               sphinx-build -b html doc/ doc_build/
               执行完成后，就可以在doc_build文件夹下看到rst文件对应的html文件了。
               注意：doc下面可以有子路径，但是doc下必须包含index.rst文件

拓展阅读
=============
        https://www.ibm.com/developerworks/cn/opensource/os-sphinx-documentation/