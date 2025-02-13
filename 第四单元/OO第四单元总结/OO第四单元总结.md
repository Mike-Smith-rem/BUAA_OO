# OO第四单元总结

## 第四单元架构设计

#### 第一次作业

- UML类图

![](C:\Users\abb255\Desktop\第四单元第一次作业.png)

- 这次作业直接按照实际的需要进行设计即可，最大的问题便在于如何处理数据的问题。
  - `MyUmlInteraction`最为关键的是构造方法：首先处理`UmlClass`和`UmlInterface`、其次继续处理`UmlGeneralization`和`UmlInterfaceRealization`等等，总共分成了四个层次；另外，为了处理这四个层次，需要使用大量的属性去标记、映射等等，从而在类中构造了一个非常复杂的结构。
  - 如何初始化也是类的重点：这个定义了几个`private`的方法用于初始化，包括将每一个`UmlClass`之间不同的元素进行连接、`UmlAssociate`的前后连接，以及利用DFS算法将父类相关属性和父类拥有的关系进行集成。整个构造完成之后，然后才去具体实现接口中的方法。
  - 第一次作业中很糟糕的一点就是属性过于复杂，属性太多容易弄混，而且有一点就是代码结构过于庞大，没有利用面对对象的思维去处理每一个类，缺乏深入的思考，导致第四单元整体做得比较糟心。



#### 第二次作业

- UML类图

由于实际上我在处理的时候还是分离的，而且相对于第一次架构并未进行任何修改，从而给出其他三个类的部分图。

![第四单元第二次作业](C:\Users\abb255\Desktop\第四单元第二次作业.png)

- 第二次作业相对于第一次作业任务量稍微小一点，在原有`UmlClassModel`不变的情况下，按照类似的方式构建另外两个类`MyUmlCollaboration`和`MyUmlStateMachine`即可。

  - `MyUmlCollaboration`：这个将其分成四个层次，分别是`UmlCollaboration`、`UmlInteraction`、`UmlLifeLine`和`UmlMessage`。其中对于`UmlEndPoint`直接存入数组即可。
  - 另外`MyUmlCollaboration`不需要进行很多初始化，因此实际上构建好映射关系之后，直接将实现接口方法。
  - `MyUmlStateMachine`：总共将其分成了五个层次。而且初始化和`UmlClass`一样复杂。需要利用DFS等算法将图关系进行分析和存储。

  实际上本次属性定义非常麻烦——由于在一个类中实现的，因此`Map`结构疯狂嵌套，自己最多可以嵌套在3-4层，导致分析过程非常复杂。

  

#### 第三次作业

- UML类图

  实际上和第二次作业基本一致（只是增加一些属性和类而已），因此就不放上来了。

- 第三次作业变动

  - 对于之前数据存储模式的变动：由于第三次作业中会出现错误形式，因此需要修改之前设计中数据存储的形式，（本人大致就是嵌套了一个`Map`来保存重复情况），整体上进行了一些修改。
  - 新增的检查模式需要另外书写DFS等算法。因此最后代码长度过大直接放弃`checkstyle`了。



## 四个单元中架构设计及OO方法理解渐进

对于OO的理解，感触最深的在第一二单元，因为第四单元基本上没有面向对象构造不同的类。

#### 第一单元

- 第一单元一方面有一定的面对对象，另一方面个人感觉对于数据结构的考核更加深入。第一单元最为深刻的便是第二次作业：第一次作业基本没用到所谓面对对象；第三次暴力WF也难以谈及面对对象。但是第二单元我**采用的树结构**使得我的设计拥有了一个基本类型：`node`。因此在**第二次作业中第一次采用面对对象的思维**去构造加法类减法类等符号类，以及三角函数、幂函数、常数等多种类型，而保持`node`为基本节点。这个结构设计出来真的是花了大量时间（三个晚上反复修改），但是最后设计成功后确实收获很多而且减小了第三次作业压力。

#### 第二单元

- 架构设计：第二单元比较灵活——当时介绍了很多模式：观察者模式、生产着消费者模式等等。但是个人实际上尝试使用这些模式结果依旧没有构建出来。第二单元只有第一次作业利用**生产着消费者模式**，通过以楼层作为`input`和`elevator`之间的交互媒介；第二次、第三次作业都是**利用尽可能简单的结构进行设计的**，并开始使用调度器，也就是——`input--调度--elevator`类似的结构。印象最深的是第三次作业，通过实现换乘来优化结构，当时是参考过课上同学的分享，花了很长时间写出来的调度，同时成就感也十足。
- OO方法：主要在于了解了不同的设计模式，然后试着实践过，从不同的设计角度去探索OO的思维；并理解多线程的特点。

#### 第三单元

- 架构设计：谈不上严格的架构设计，理解最深的还是**如何实现需求**和**规格化**。JML正类似生活中的客户和供应商的需求交互。规格化是一方面的问题：对于JML的理解或者如何书写JML语言，这是一个比较折磨人的问题——也是强测最容易爆炸的问题。设计是另外一个问题：存储大量数据、避免超时。本次作业超时和海量的测试数据对debug造成了相当大的困难，这一个单元基本上我都是凭感觉去找可能是哪个地方出现问题了，因为这么多数据中途出现一个问题实在无法定位，也是最难受的一点。最后专门说一下第二次作业。第二次作业开始发生超时，当时真的花了好长的时间构造出`previous-status`相关类去**临时存储、临时计算、保存**数据。在第三单元博客中我对其做出了和第一单元树结构一样大量的分析。不得不说每一个新的作业完成都沉浸了大量的思考。
- OO方法：主要是对于规格化和优化的进一步理解。

#### 第四单元

- 架构设计：第四单元算得上是相对较好了，可以面对对象也可以面向过程写一堆代码。可能核心在于任务量和代码量比较多而已。第四单元我又回归到最初的面向过程，进行了较为简单的面向过程设计，基本上也不存在很大的问题。

- OO方法：面向对象的使用。

## 四个单元中测试理解与实践的改进

#### 第一单元

- 第一单元测试比较简单，直接输入数据即可。但是难点在于确定结果的正确性。这里很多同学用的python的库直接通过相减得到的，由于个人并不是很明白python，因此实际上是借助别人的工具进行的测试。

#### 第二单元

- 第二单元测试涉及到多线程，可以采用`print`打印直接判断输出的位置是否合理进行分析。同时需要注意锁的问题，同样相对来说比较简单。

#### 第三单元

- 第三单元测试专门提及了**单元测试**。但是实际上单元测试本人进行的不是很多，主要是觉得这个基于类方法本身的测试相对而言可能作用不大，而且也比较麻烦。不过也了解了一些`JUnit`的测试方式，但是整体收获也不是很大。

#### 第四单元

- 第四单元给出了命令行，但是我捣鼓了半天没有得到对应的序列。有点头疼，迫于无奈基本上还是通过面向测评机机型测试，或者通过画UML进行逻辑分析，但是测试相对比较难做。

整体来说测试是学习OO过程中学得最糟糕和难受的地方，很多时候不知道怎么测试，也不知道测试什么，测试是否有效等问题，导致强测总是不尽人意，算得上是学习OO的一个比较大的遗憾吧。

## 课程收获

- 学会了面向对象的思维。通过学习这门课程，确实能够对于面对对象的思维有着更加深入的理解，能够比较面向过程和面向对象的区别，算得上是一个理论上的提升。
- 进行了一定程度的代码训练。个人感觉自己对于代码的结构更加有掌控感了。之前学习C语言的时候总是觉得无从下手，写一个算法写很长时间。通过学习OO，对于各种问题的分析能力有了一个很大的提升，这是我最大的收获。
- 了解有关代码的维护性、性能、优化等问题。从C语言只要写出来就行了，到现在考虑如何做优化、考虑时间复杂度、如何设计更好的框架等等，这是OO特色鲜明的一点，也是自己对于代码有了更进一步的认知，而非最开始学习C语言的编程小白了。

总而言之，代码思维和代码能力，这是OO课收获最大的两个方面。虽然自己在测试等方面做的还不够好，但是相对于学习课程前的自己已经有了长足的进步。

## 具体改进建议

这个地方给出一点点主观的建议：

- 对于测试的讲解和说明可以更加丰富一点。很多地方给出的指导是“对于测试，请同学们自行探索”确实有点无从下手。就个人来说自己的代码能力确实比较落后，每次完成作业，能够做出一点点的优化就已经很成功了，在能力方面可能确实缺乏相关做测试的方法体系——面向测评机测试过于真实。因此希望能够在测试方面做出更多指导。
- 可以适当拉长一下时间线。实际上学习OO基本上就没有周末了。非常特别的一个现象：周六周日图书馆六楼，放眼望去全是写OO的，每次过去还能碰到熟人。个人认为一些同学可能从周五考完试之后晚上才开始写代码，因此周六周日等同于做OO的时间，不妨将周末的22点延长至周一中午，同时顺延一下互测时间，这样给出两个~~通宵~~时间可能对同学们有益。
- 课上测试可以公布答案。实际上个人感觉课上测试的实际作用不大，每次都是一脸懵进去然后一脸懵出来，给出的代码看半天不知道在干什么，实在没有什么收获。个人认为可以公开每次课上测试的代码、答案或者整体逻辑。