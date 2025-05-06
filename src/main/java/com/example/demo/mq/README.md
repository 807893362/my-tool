# MQ 问题总结

# kafka
- 【同步异步】kafka对于消息的发送，可以支持同步和异步，同步会需要阻塞，而异步不需要等待阻塞的过程。
-- 从本质上来说，kafka都是采用异步的方式来发送消息到broker，但是kafka并不是每次发送消息都会直接发送到broker上，而是把消息放到了一个发送队列中，然后通过一个后台线程不断从队列取出消息进行发送，发送成功后会触发callback。kafka客户端会积累一定量的消息统一组装成一个批量消息发送出去，触发条件是前面提到的batch.size和linger.ms
-- 而同步发送的方法，无非就是通过future.get()来等待消息的发送返回结果，但是这种方法会严重影响消息发送的性能。
- 一个topic 可以配置几个partition，produce发送的消息分发到不同的partition中
- ，consumer接受数据的时候是按照group来接受，kafka确保每个partition只能同一个group中的同一个consumer消费
- ，如果想要重复消费，那么需要其他的组来消费。Zookeerper中保存这每个topic下的每个partition在每个group中消费的offset
## Broker（Server）
- 【数据切割】:一个 Kafka 集群由多个 Broker（就是 Server） 构成，每个 Broker 中含有集群的部分数据。Kafka 把 Topic 的多个 Partition 分布在多个 Broker 中。
--【好处1】如果把 Topic 的所有 Partition 都放在一个 Broker 上，那么这个 Topic 的可扩展性就大大降低了，会受限于这个 Broker 的 IO 能力。把 Partition 分散开之后，Topic 就可以水平扩展 。
--【好处2】一个 Topic 可以被多个 Consumer 并行消费。如果 Topic 的所有 Partition 都在一个 Broker，那么支持的 Consumer 数量就有限，而分散之后，可以支持更多的 Consumer。 
--【好处3】一个 Consumer 可以有多个实例，Partition 分布在多个 Broker 的话，Consumer 的多个实例就可以连接不同的 Broker，大大提升了消息处理能力。可以让一个 Consumer 实例负责一个 Partition，这样消息处理既清晰又高效。
## partition
- 【并行消费】一个topic 可以配置几个partition，produce发送的消息分发到不同的partition中，consumer接受数据的时候是按照group来接受，kafka确保每个partition只能同一个group中的同一个consumer消费，如果想要重复消费，那么需要其他的组来消费。
- 【物理数量】物理上把Topic分成一个或多个Partition，每个Partition在物理上对应一个文件夹，该文件夹下存储这个Partition的所有消息和索引文件。若创建topic1和topic2两个topic，且分别有13个和19个分区，则整个集群上会相应会生成共32个文件夹
- 【有序性】：但单独看 Partition 的话，Partition 内部消息是有序的。一个 Topic 跨 Partition 是无序的，如果强制要求 Topic 整体有序，就只能让 Topic 只有一个 Partition。
- 【容灾&数据冗余】：Kafka 为一个 Partition 生成多个副本，并且把它们分散在不同的 Broker。
## topic
- 【配置】消费者和生产者都必须指定
- Kafka 不像普通消息队列具有发布/订阅功能，Kafka 不会向 Consumer 推送消息。
- Consumer 必须自己从 Topic 的 Partition 拉取消息。
### topic相关指令
- kafka-topics.sh --create --zookeeper <zookeeper_host:port> --replication-factor <replication_factor> --partitions <num_partitions> --topic <topic_name>
-- 其中，`zookeeper_host:port`是Zookeeper的主机名和端口号，`replication_factor`是Topic的副本因子，`num_partitions`是Topic的分区数，`topic_name`是要创建的Topic的名称。
``
  eg: kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 3 --topic my_topic
``
- kafka-topics.sh --list --zookeeper <zookeeper_host:port>
-- 列出所有在Kafka中存在的Topic
## group
- 【与topic关系】不同的组可以绑定在同一个topic下。组内不会收到重复消息，不同组会收到相同的消息。
- 【配置】只有消费者需要指定
- 【消费组】Kafka 中有一个 Consumer Group（消费组）的概念，多个 Consumer 组团去消费一个 Topic。同组的 Consumer 有相同的 Group ID。
- 【防止重复消费】Consumer Group 机制会保障一条消息只被组内唯一一个 Consumer 消费，不会重复消费。
-- [广播&单播]这是Kafka用来实现一个Topic消息的广播（发给所有的Consumer）和单播（发给某一个Consumer）的手段。
- 【默认group】每个Consumer属于一个特定的Consumer Group（可为每个Consumer指定group name，若不指定group name则属于默认的group）
## kafka 广播
- 【通过Spring EL 表达式 动态group实现】
``@KafkaListener(topics = TOPIC.TOPIC ,groupId = CONSUMER_GROUP_PREFIX + TOPIC.TOPIC +  "-" + "#{T(java.util.UUID).randomUUID()})")
``


# rocket
- https://rocketmq.apache.org/zh/docs/domainModel/01main
## topic
- 消息传输和存储的顶层容器，用于标识同一类业务逻辑的消息。主题通过TopicName来做唯一标识和区分
- 【配置】消费者初始化需要指定,生产者发送消息时指定
## group
- 【与topic关系】不同的组可以绑定在同一个topic下。组内不会收到重复消息，不同组会收到相同的消息。
- 【配置】消费者和生产者都必须指定
- 【生产者指定的目的】发送普通消息时，用于标识使用，没有特别的用处
- 【默认】不指定会使用默认：DEFAULT_PRODUCER
## tag
- 【配置】只有消费者需要指定
- 【消费者订阅多个TAG】consumer.subscribe("TopicTest", "TagA||TagB");
- 【生产者只能指定单个TAG】
## Consumer MessageModel
- 【默认：集群模式】MessageModel.CLUSTERING
- 【广播模式】MessageModel.BROADCASTING
