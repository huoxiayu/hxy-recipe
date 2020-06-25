## jdk nio核心概念
1. 缓冲buffer负责缓冲数据
2. 通道channel负责传输数据
3. 选择器selector可以用一个线程来对注册到选择器的多个通道进行选择，称之为IO多路复用。

## 网络应用
网络应用的基本架构：   
read request -> decode request -> process service -> encode reply -> send reply   
常见形式：
1. Thread-Per-connection，即一个线程处理所有工作   
典型流程：![avatar](https://github.com/huoxiayu/hxy-recipe/blob/huoxiayu/image/thread-per-connection.png)   
实现：ThreadPerConnectionServer 
2. reactor模式，事件驱动式的设计，资源消耗更少，不需要对每个连接维护一个线程，同时也减少了线程上下文切换的开销。缺点是：dispatching操作会更加耗时，编程难度增加  
    1. 单selector单线程
    典型流程：![avatar](https://github.com/huoxiayu/hxy-recipe/blob/huoxiayu/image/single-thread-reactor.png)     
    2. 单selector多线程
    3. 多selector多线程

## 主要参考资料
[Scalable IO in Java](http://gee.cs.oswego.edu/dl/cpjslides/nio.pdf)


