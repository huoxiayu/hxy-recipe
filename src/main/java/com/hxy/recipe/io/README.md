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


