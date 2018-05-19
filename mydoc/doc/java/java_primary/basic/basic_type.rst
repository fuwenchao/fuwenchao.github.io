java基本数据类型的字面量
================================


java的基本数据类型一共有8种。其中：（每种类型后面列出了java中的该类型的字面量）


**四种整型：**

- int  4字节； 12  +12  -12  077 0xFF  0b101(JDK7中支持的二进制表示)  字面范围：0x800000000 --> 0x7FFFFFFF

- short  2字节； 2个字节内的int字面量可以认为是short类型的字面量，可以给short变量赋值  字面范围：（short）0x8000 -->  0x7FFF

- long 8字节；    int字面量形式后加字母L 字面范围：0x8000000000000000L  --> 0x7FFFFFFFFFFFFFFFL

- byte 1字节;      1个字节内的int字面量可以认为是byte类型的字面量，可以给byte变量赋值  字面范围：(byte)0x80 --> 0x7F

Java中没有所谓的无符号整型数。

.. code:: java

    public class Test
    {
        public static void main(String[] args)
        {
            short shortNum = (short)0x8000;
            int  intNum = 0x80000000;
            long longNum = 0x7FFFFFFFFFFFFFFFL;
            byte byet_num = (byte)0x80;
    　　　　　　        

            System.out.println(shortNum);//-32768
            System.out.println(intNum);//-2147483648
            System.out.println(longNum);//9223372036854775807
            System.out.println(byet_num);//-128
        }
        
    }

如果short和byte在代码中不用强转，则会报错，那是因为字面量的值超出了变量所能表示的范围。通过强转，可以把int的字面量（四个字节）截取后两个（short）或一个(byte)字节来赋值，作为当前变量的值。然后根据补码的计算规则确定当前的值的大小。上面赋的是两个最小值。


**两种浮点型：**

- float     4字节     整型和小数后加字母F或f的数，也可以是科学计数法表示的数加F或f。float能表示的最大最小值可以借助Float类的MAX_VALUE和MIN_VALUE这两个域来获取。

- double  8字节 　 整型或小数后加字母D或d的数，也可以是科学计数法表示的数加D或d，或是单纯的小数。double变量能表示的最大最小值同样也可以借助Double类。

 

**一种用于表示Unicode编码的字符单元的字符类型：**

char  2字节　　单引号里的单个字符。

 

**一种用于表示真值的类型：**

boolean  1/8字节（1位/1bit）  true和false。