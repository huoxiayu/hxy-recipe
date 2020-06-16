# ZooKeeper完全解读

### [论文](http://www.usenix.org/event/usenix10/tech/full_papers/Hunt.pdf)

### 什么是ZooKeeper
ZooKeeper是一个*开源*的*分布式协同*服务，起源于雅虎。  
 
当时雅虎的开发人员发现很多大型系统都需要依赖一个类似的系统来做*分布式协调*，但又不能有*单点*问题。   

后来雅虎的工程师尝试开发一个*通用*的、*无单点*的*分布式协同*服务，让开发人员开源专注于业务逻辑。这就是ZooKeeper。   

ZooKeeper开源之后被大量系统使用，如：   
1. Hadoop使用ZooKeeper来做NameNode的高可用。
2. Kafka使用ZooKeeper来实现Controller节点选举，成员管理等。

### ZooKeeper常见用法
说明：ZooKeeper适合存储应用元数据或者分布式协同相关的信息，不适合存储大量数据，更不是数据库，但得益于ZooKeeper优雅的设计，ZooKeeper可以做的事情很多。   
如：
1. *配置管理*，如配合可视化界面和本地缓存做配置中心。可视化界面用于方便权限管理和修改配置等，配合本地缓存提升读性能和可用性，ZooKeeper自带的发布订阅功能也方便配置更新，但ZooKeeper做配置中心没有灰度发布、版本控制等功能，本身不能保存大量数据，且只能对znode进行全量读和覆盖写。推荐只用ZooKeeper保存应用元数据。
2. *命名服务*或*DNS*或*集群成员管理*，如可以用ZooKeeper做服务发现等。由于ZooKeeper是一个CP系统，因此如果用ZooKeeper做服务发现推荐客户端实现一定的缓存或持久化策略，如将实例列表存储到磁盘等介质上，避免ZooKeeper集群无法提供服务时影响服务上线。
3. *Master选举*和*Master&Worker协同*和*分布式锁*等   

### ZooKeeper服务的使用
应用通过ZooKeeper客户端来使用ZooKeeper服务，ZooKeeper使用基于TCP的私有协议

### ZooKeeper的数据模型
ZooKeeper采用了叫做data-tree的层次模型（类似于文件系统），优点是便于表达层次关系（如规定/service/app1下的所有子节点代表所有app1的服务），且可以方便地表达命名空间的概念（如不同的应用可以使用不同的namespace，相互隔离）。   

data-tree是内存数据结构。data-tree中的每个节点称为znode，znode上存储着数据以及版本等信息。每个znode最多只能存储约1MB的数据。

ZooKeeper通过ZooKeeper客户端API向外提供服务。
1. 通过Unix风格的路径标示znode，如/service/app_x表示节点service下有一个子节点app_x
2. znode的数据只支持全量读取或写入（覆盖写是原子操作）
3. ZooKeeper的所有API都是wait-free的？

znode的种类：
1. 临时性znode -> ZooKeeper宕机或client在指定timeout时间没有响应就会删除
2. 持久性znode -> 一旦创建就不会丢失，即使ZooKeeper或者客户端宕机
3. 临时顺序性znode -> 顺序性znode关联了一个递增的整数(10位的int)
4. 持久顺序性znode -> 顺序性znode关联了一个递增的整数(10位的int)
[完整列表](https://github.com/apache/zookeeper/blob/branch-3.6/zookeeper-server/src/main/java/org/apache/zookeeper/CreateMode.java)

## 核心概念
### session
ZooKeeper会维护与客户端之间的session。   
客户端可以主动关闭session，并且ZooKeeper在timeout时间内没有收到来自客户端的数据也会主动关闭session。   
客户端发现session关闭后会自动和其他ZooKeeper节点重试。

### quorum
quorum模式的ZooKeeper包含多个节点，其中只有一个leader。   
leader可以处理读写请求，而follower和observer只能处理读请求，当follower和observer收到写请求时会转发给leader。follower参与选举而observer不参与选举。

### watch机制
ZooKeeper支持watch机制。具体来说，客户端在进行读取操作时（getData()、getChildren()、exists()）可以在指定path上注册一个watch，该path发生变化时，ZooKeeper会通知客户端。   
watch是轻量级的，ZooKeeper只会通知客户端发生了变化，但不包含变化的内容。   
watch是一次性的，触发后server端会remove掉watch，因此客户端如果想一直监听变化需要反复注册。

### ZooKeeper操作指南
ZooKeeper只依赖JDK
两种模式：standalone模式（单机模式）和quorum模式（集群模式）
基本操作：
1. zoo.cfg文件配置dataDir、dataLogDir和clientPort(默认2181)
2. ./zkServer.sh start
3. ./zkCli.sh -server 127.0.0.1:2181
4. quit
5. ./zkServer.sh stop

### Master&Worker架构
任意时刻集群中有最多一个master和若干worker。master实时监控worker的状态并且为worker分配任务。如果master宕机或者出现错误，backup-master（也可以是worker）会重新竞选master。  
master-worker是一个广泛使用的分布式架构。如：
1. 在Kafka集群中有多个broker（worker），Kafka会在这些broker中选举出一个controller（master），负责将topic、partition分配给各个broker。
2. HDFS中NameNode就是master，NameNode中保存了整个分布式文件系统的元信息，负责将数据块分配给集群中的DataNode（worker）保存。

### 如何用ZooKeeper实现Master&Worker架构
todo：代码事例





