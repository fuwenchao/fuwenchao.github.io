查看节点信息
======================


鼠标停留在一个节点上，自动显示出该节点的信息

::

    <li data-id="123457" data-account-name="xxxx"
            data-name="xxxx" data-email="cjf123@163.com" 
            data-mobile="13968789868">xxxx</li>
    <ul>
        <li data-id="123456" data-account-name="wwq"
            data-name="魏文庆" data-email="wwq123@163.com" 
            data-mobile="13524543878">wwq</li>
        <li data-id="123457" data-account-name="cjf"
            data-name="蔡剑飞" data-email="cjf123@163.com" 
            data-mobile="13968789868">cjf</li>
    </ul>
    <div id="card" style="display:none">
        <table>
            <caption id="accountName"></caption>
            <tr><th>姓名：</th><td id="name"></td></tr>
            <tr><th>邮箱：</th><td id="email"></td></tr>
            <tr><th>手机：</th><td id="mobile"></td></tr>
        </table>

    </div>



.. code:: java

    <script>
        function $(id){
            return document.getElementById(id);
        }
        
        var lis = document.getElementsByTagName('li');
        for(var i = 0, li;li = lis[i]; i++){
            li.onmouseenter = function(event){
                event = event || window.event;
                var user = event.target|| event.srcElement;
                var data = user.dataset;

                $('accountName').innerText = data.accountName;
                $('name').innerText = data.name;
                $('email').innerText = data.email;
                $('mobile').innerText = data.mobile;

                $('card').style.display = 'block';
            };

            li.onmouseleave = function(event){
                $('card').style.display = 'none';
            };

        }
        
    </script>