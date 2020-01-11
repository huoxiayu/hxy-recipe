# netty
## netty简介
netty是一个异步的、事件驱动的网络编程框架，用于快速开发可维护的高性能服务器或客户端。
## netty架构图
![netty架构图](https://netty.io/images/components.png)
## 快速开始

## 源码编译
### netty中对不同操作系统的支持
netty中涉及到了操作系统对底层IO的实现，因此工程上需要区分不同的操作系统。pom中有一个os.detected.classifier属性，在编译时os-maven-plugin会对os.detected.classifier做宏替换。
### netty的模板代码生成
另外在netty-common中有一些模板文件：KCollections.template、KObjectHashMap.template、KObjectMap.template以及一个groovy的脚本：codegen.groovy，在编译时groovy-maven-plugin会解析模板并且生成具体的java类。
### netty中的依赖管理
netty中有一个单独的bom项目，管理了各个module的版本。