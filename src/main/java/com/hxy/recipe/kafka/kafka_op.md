#### 1、启动zk  
##### Linux:  
./bin/zookeeper-server-start.sh config/zookeeper.properties  
##### Windows:  
.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties

#### 2、启动kafka
##### Linux:
./bin/kafka-server-start.sh config/server.properties
##### Windows:
.\bin\windows\kafka-server-start.bat .\config\server.properties

#### 3、新建topic
##### Linux:
./bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test
##### Windows:
.\bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test

#### 4、发送消息
##### Linux:
./bin/kafka-console-producer.sh --broker-list localhost:9092 --topic test 
##### Windows:
.\bin\windows\kafka-console-producer.bat --broker-list localhost:9092 --topic test 

#### 5、消费消息
##### Linux:
./bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic test --from-beginning  
###### or  
./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test --from-beginning
##### Windows:
.\bin\windows\kafka-console-consumer.bat --zookeeper localhost:2181 --topic test --from-beginning  
###### or  
.\bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic test --from-beginning

#### 6、增加partition
##### Linux:
./bin/kafka-topics.sh --alter --zookeeper localhost:2181 --topic test --partitions 2
##### Windows:
.\bin\windows\kafka-topics.bat --alter --zookeeper localhost:2181 --topic test --partitions 2

#### 7、查看partition
##### Linux:
./bin/kafka-topics.sh --describe --zookeeper localhost:2181 --topic test
##### Windows:
.\bin\windows\kafka-topics.bat --describe --zookeeper localhost:2181 --topic test

#### 8、修改、查看其它配置
##### Linux:
./bin/kafka-configs.sh --zookeeper localhost:2181 --alter --entity-name test --entity-type topics --add-config retention.ms=86400000
./bin/kafka-configs.sh --zookeeper localhost:2181 --describe --entity-name test --entity-type topics
##### Windows:
.\bin\windows\kafka-configs.bat --zookeeper localhost:2181 --alter --entity-name test --entity-type topics --add-config retention.ms=86400000
.\bin\windows\kafka-configs.bat --zookeeper localhost:2181 --describe --entity-name test --entity-type topics
