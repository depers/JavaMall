# pulsar-demo

该项目是pulsar使用的实验项目，我将通过该项目依次实验pulsar的核心功能，方便在生产项目中进行实践。

# 功能实验

## 1. 生产者和消费者的简单实践

### 实验的类
* 生产者：cn/bravedawn/producer/ProducerClient.java
* 消费者：cn/bravedawn/consumer/ConsumerClient.java
* 消息内容：cn/bravedawn/schema/DemoData.java

## 2. 重复消息删除功能

1. 首先开启namespace级别的重复数据删除的配置。这个可以参考类cn/bravedawn/broker/NamespaceConfigurationClient.java
2. 然后在生产者中设置三个关键参数：
  * 第一个是producerName，如果未设置名称，则将设置全局唯一名称 由 Pulsar 服务分配。然后应用程序将能够使用 Producer.getName() 获取分配的名称。如果应用程序选择自定义名称，则需要自行确保该名称全局唯一。
  * 第二个是initialSequenceId，该生产者生产消息的初始序列号，发送一个消息该序列号自增1，不设置的话从0开始。
  * 第三个是`sendTimeout(0, TimeUnit.SECONDS)`发送超时时间，将使超时时间设置为无穷大。这在使用 Pulsar 的消息去重功能时非常有用，因为客户端库将不断重试发布消息。不会有任何错误传播回应用程序。
3. 在发送消息的时候，在消息中携带sequenceId，用于标识该消息。具体代码实现参考cn/bravedawn/producer/DeduplicationProducerClient.java。
4. 经过测试发现，针对一个生产者，他的消息sequenceId是递增的，如果当前发送的消息的sequenceId小于等于Broker那里记录的当前生产者的sequenceId，这条消息就会被认为是重复消息

## 3. 批量发送消息

## 4. 消息消费超时和消费重试

## 5. 死信队列

## 6. 使用admin api 更新配置，是临时生效还是永久生效

## 7. 顺序消息

## 8. 优先级消息