Java NIO 之 SocketChannel
================================


Java NIO中的SocketChannel是一个连接到TCP网络套接字的通道。

可以通过以下2种方式创建SocketChannel：


    1. 打开一个SocketChannel并连接到互联网上的某台服务器。
    2. 一个新连接到达ServerSocketChannel时，会创建一个SocketChannel。

打开 SocketChannel
-----------------------

下面是SocketChannel的打开方式：

.. cdde:: java

    SocketChannel socketChannel = SocketChannel.open();
    socketChannel.connect(new InetSocketAddress("http://jenkov.com", 80));


关闭 SocketChannel
-------------------------

当用完SocketChannel之后调用SocketChannel.close()关闭SocketChannel：

    socketChannel.close();

从 SocketChannel 读取数据
----------------------------------

要从SocketChannel中读取数据，调用一个read()的方法之一。以下是例子：
::

    ByteBuffer buf = ByteBuffer.allocate(48);
    int bytesRead = socketChannel.read(buf);


首先，分配一个Buffer。从SocketChannel读取到的数据将会放到这个Buffer中。

然后，调用SocketChannel.read()。该方法将数据从SocketChannel 读到Buffer中。read()方法返回的int值表示读了多少字节进Buffer里。如果返回的是-1，表示已经读到了流的末尾（连接关闭了）。

写入 SocketChannel
-----------------------

写数据到SocketChannel用的是SocketChannel.write()方法，该方法以一个Buffer作为参数。示例如下：

.. code:: java

    String newData = "New String to write to file..." + System.currentTimeMillis();

    ByteBuffer buf = ByteBuffer.allocate(48);
    buf.clear();
    buf.put(newData.getBytes());

    buf.flip();

    while(buf.hasRemaining()) {
        channel.write(buf);
    }



注意SocketChannel.write()方法的调用是在一个while循环中的。Write()

方法无法保证能写多少字节到SocketChannel。所以，我们重复调用write()直到Buffer没有要写的字节为止。

非阻塞模式
-----------------

可以设置 SocketChannel 为非阻塞模式（non-blocking mode）.设置之后，就可以在异步模式下调用connect(), read() 和write()了。


**connect()**

如果SocketChannel在非阻塞模式下，此时调用connect()，该方法可能在连接建立之前就返回了。为了确定连接是否建立，可以调用finishConnect()的方法。像这样：

.. code:: java

    socketChannel.configureBlocking(false);
    socketChannel.connect(new InetSocketAddress("http://jenkov.com", 80));

    while(! socketChannel.finishConnect() ){
        //wait, or do something else...
    }

**write()**

非阻塞模式下，write()方法在尚未写出任何内容时可能就返回了。所以需要在循环中调用write()。前面已经有例子了，这里就不赘述了。

**read()**

非阻塞模式下,read()方法在尚未读取到任何数据时可能就返回了。
所以需要关注它的int返回值，它会告诉你读取了多少字节。
非阻塞模式与选择器

非阻塞模式与选择器搭配会工作的更好，
通过将一或多个SocketChannel注册到Selector，
可以询问选择器哪个通道已经准备好了读取，写入等。

Selector与SocketChannel的搭配使用会在后面详讲。

**一个serversocketchannel的完整例子:**

.. code:: java

    package me.wenchao.javapro.javanio;

    import java.io.IOException;
    import java.net.InetSocketAddress;
    import java.nio.ByteBuffer;
    import java.nio.channels.*;
    import java.nio.charset.Charset;
    import java.util.Iterator;

    /**
     * @Author wenchaofu
     * @DATE 23:18 2018/4/28
     * @DESC
     */
    public class NIOServer {
        public static void main(String[] args) throws IOException {
            ServerSocketChannel ssc = ServerSocketChannel.open();
            InetSocketAddress endpoint = new InetSocketAddress(8080);
            System.out.println("开始监听端口8080");
            ssc.socket().bind(endpoint);
            ssc.configureBlocking(false);

            Selector selector = Selector.open();
            ssc.register(selector, SelectionKey.OP_ACCEPT);

            Handler handler = new Handler(1024);
            while (true) {
                //select 返回有多少个通道就绪了，timeout为阻塞时间
                if (selector.select(2000)==0) {
                    System.out.println("Listening.....");
                    continue;
                }
                System.out.println("开始处理请求....");

                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    //SelectionKey 代表注册到该selector的通道
                    SelectionKey key = iterator.next();
                    if (key.isAcceptable()) {
                        handler.handlerAccept(key);

                    }
                    if (key.isReadable()) {
                        handler.handlerRead(key);
                    }
                    iterator.remove();
                }
            }



        }
        private static class Handler{
            private int bufferSize = 1024;
            private String localCharset = "UTF-8";
            public Handler(){

            }
            public Handler(int bufferSize){
                this(bufferSize,null);

            }
            public Handler(String localCharset){
                this(-1,localCharset);
            }
            public Handler(int bufferSize,String localCharset){
                if (bufferSize>0) {
                    this.bufferSize=bufferSize;
                }
                if (localCharset != null) {
                    this.localCharset = localCharset;
                }

            }

            public void handlerAccept(SelectionKey key) throws IOException {
                SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
                socketChannel.configureBlocking(false);
                //将通道注册到selector
                socketChannel.register(key.selector(),SelectionKey.OP_READ, ByteBuffer.allocate(bufferSize));
            }

            public void handlerRead(SelectionKey key) throws IOException {
                SocketChannel socketChannel = (SocketChannel) key.channel();
                ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
                byteBuffer.clear();
                if (socketChannel.read(byteBuffer)==-1) {
                    socketChannel.close();
                } else{
                    //将buffer转换为读状态
                    byteBuffer.flip();
                    String receivedString = Charset.forName(localCharset).newDecoder().decode(byteBuffer).toString();
                    System.out.println("接收到的字符串是  " + receivedString);

                    //返回数据给客户端

                    String sendString = "server send data is-->"+receivedString;

                    byteBuffer = ByteBuffer.wrap(sendString.getBytes(localCharset));
                    socketChannel.write(byteBuffer);
                    socketChannel.close();
                }


            }

        }
    }
