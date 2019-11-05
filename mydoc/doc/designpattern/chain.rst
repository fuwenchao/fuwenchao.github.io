责任链模式
=============


责任链模式是一条链，链上有多个节点，每个节点都有各自的责任。当有输入时，第一个责任节点看自己能否处理该输入，如果可以就处理。如果不能就交由下一个责任节点处理。依次类推，直到最后一个责任节点。


例子
-----

举几个例子：

1. 需求开发例子

假设现在有个需求来了，首先是实习生拿到这个需求。

如果实习生能够实现，直接实现。如果不行，他把这个需求交给初级工程师。

如果初级工程师能够实现，直接实现。如果不行，交给中级工程师。

如果中级工程师能够实现，直接实现。如果不行，交给高级工程师。

如果高级工程师能够实现，直接实现。如果不行，交给 CTO。

如果 CTO能够实现，直接实现。如果不行，直接跟产品说，需求不做。


场景
------


给定一个输入值，根据输入值执行不同逻辑。

我们一看，分分钟写出如下代码：

.. code:: java

  String input = "1";  
  if ("1".equals(input)) {    
      //TODO do something  
  } else if ("2".equals(input)) { 
      //TODO do something  
  } else if ("3".equals(input)) {   
     //TODO do something       
  }

或者如下代码：

.. code:: java

  String input = "1";   
  switch (input) { 
    case "1":    
      //TODO do something    
      break;   
    case "2":     
    //TODO do something     
     break;   
    case "3":   
     //TODO do something    
     break;  
    default:    
     //TODO do something    
    break;  
   }

如果每个分支里面的逻辑比较简单，那还好，如果逻辑复杂，假设每个 case 大概要 100 行代码处理，有 10 个 case，一下子就出来一个「千行代码」文件。****而且还不利于维护、测试和扩展。

如果能够想办法把代码拆分成每个 case 一个文件，这样不仅代码逻辑清晰了很多，而且不管是后续维护、扩展还是进行测试，都方便很多。

因此，本篇文章核心，责任链模式的妙用——拆分代码就来了。



责任链模式拆分代码
------------------------


1. 定义一个抽象类。

.. code:: java

  public abstract class BaseCase { 
      // 为 true 表明自己可以处理该 case 
      private boolean isConsume; 
      public BaseCase(boolean isConsume) {   
          this.isConsume = isConsume; 
      }
       // 下一个责任节点
       private BaseCase nextCase; 
      public void setNextCase(BaseCase nextCase) {  
          this.nextCase = nextCase; 
      }     
      public void handleRequest() {  
          if (isConsume) {    
              // 如果当前节点可以处理，直接处理     
              doSomething();  
          } else {     
              // 如果当前节点不能处理，并且有下个节点，交由下个节点处理     
              if (null != nextCase) {     
                  nextCase.handleRequest();    
              } 
          } 
      } 
      abstract protected void doSomething();
  }

2. 各个 case 来实现该抽象类。

这里列举一个 case，其他可以看代码。

.. code:: java

  public class OneCase extends BaseCase { 
      public OneCase(boolean isConsume) {   
          super(isConsume); 
      }
   
      @Override protected void doSomething() {   
      // TODO do something   
          System.out.println(getClass().getName()); 
      }
  }


3. 初始化各个 case，并指定每个 case 的下一个节点

.. code:: java

  String input = "1";      
  OneCase oneCase = new OneCase("1".equals(input));   
  TwoCase twoCase = new TwoCase("2".equals(input)); 
  DefaultCase defaultCase = new DefaultCase(true); 
  oneCase.setNextCase(twoCase); 
  twoCase.setNextCase(defaultCase);      
  oneCase.handleRequest();


好了，到此我们责任链模式拆分代码就告一段落了。


一个优化
------------

上面是责任链模式拆分代码的一个基本实现。

后面有同事给了建议，说可以参考 OkHttp 里面的 Interceptor 实现。

所以这边看了一下，做了如下改进。

先说一下大概思想吧。

将所有的 case 集中起来，通过遍历确定能够处理的 case。

同样是以上面的场景为例进行说明。

1. 定义一个接口。

.. code:: java

  interface BaseCase { 
  // 所有 case 处理逻辑的方法 
      void doSomething(String input, BaseCase baseCase);
  }


2. 建立一个责任链管理类，管理所有 case。

.. code:: java


  public class CaseChain implements BaseCase { 
      // 所有 case 列表 
      private List<BaseCase> mCaseList = new ArrayList<>(); 
      // 索引，用于遍历所有 case 列表 
      private int index = 0; 
      // 添加 case 
      public CaseChain addBaseCase(BaseCase baseCase) { 
          mCaseList.add(baseCase);  
          return this; 
      } 

      @Override public void doSomething(String input, BaseCase baseCase) { 
           // 所有遍历完了，直接返回   
          if (index == mCaseList.size()) return;  
          // 获取当前 case   
          BaseCase currentCase = mCaseList.get(index);  
          // 修改索引值，以便下次回调获取下个节点，达到遍历效果 
          index++;  
          // 调用 当前 case 处理方法 
          currentCase.doSomething(input, this); 
      }
  }


3. 各个 case 实现接口。这里以其中一个为例。

.. code:: java

  public class OneCase implements BaseCase {
          @Override 
          public void doSomething(String input, BaseCase baseCase) {  
              if ("1".equals(input)) {     
                  // TODO do something     
                  System.out.println(getClass().getName());     
                  return;  
               }   
              //当前没法处理，回调回去，让下一个去处理 
              baseCase.doSomething(input, baseCase);
          }
  }


4. 初始化各个 case

.. code:: java


  String input = "1";   
  CaseChain caseChain = new CaseChain();  
  caseChain.addBaseCase(new OneCase())
                   .addBaseCase(new TwoCase())
                   .addBaseCase(new DefaultCase());  
  caseChain.doSomething(input, caseChain);



总结
-----

本篇文章以实际项目中的场景为例，向你描述责任链模式的妙用。

看完文章，可能你只学到其形，而没有学到其神。

通过不断的使用以及自己经验的不断积累，相信达到形神兼备也是时间问题而已。

等你完全掌握之后，不再是「我要用责任链模式，因此写出了代码」。

而是「我写出了代码，才发现用到了责任链模式」。

正如《倚天屠龙记》里面张三丰教张无忌太极剑时，最后张无忌全都忘了一样。

温馨提示：

学习了新设计模式，难免有点手痒。

但是切记不要滥用设计模式。

不要为了设计而设计。

比如你就几个 case，而且处理逻辑就是弹个框。

你说你要用上设计模式？这样成本会更高，其实没必要。

所以学会是一回事，什么时候用又是另一回事了。

