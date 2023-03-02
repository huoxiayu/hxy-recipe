cuda initialization会创建cuda context

cuda context作为一个容器，负责管理所有对象的生命周期，比如cuda context会负责：
* 所有分配内存
* Modules，类似于动态链接库，以.cubin和.ptx结尾 【在jcuda中要使用】
* CUDA streams，管理执行单元的并发性
* CUDA events
* texture和surface引用
* kernel里面使用到的本地内存（设备内存）
* 用于调试、分析和同步的内部资源
* 用于分页复制的固定缓冲区
即调用这些函数的时候，需要有context存在

context的创建方式：
* 隐式调用，cuda runtime软件层的库会隐式创建cuda context(即默认是：延迟初始化（deferred initialization）)

reference:
https://zhuanlan.zhihu.com/p/266633373


