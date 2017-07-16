# kafka examples

## REQUIREMENTS 

* [Apache ZooKeeper](https://zookeeper.apache.org/) 
* [Apache Kafka](https://kafka.apache.org/) version 0.10.2.1 or higher [DOC](https://kafka.apache.org/quickstart)

Avro messages
 * [Confluent](https://www.confluent.io/) version 3.2.1 or higher [DOC](http://docs.confluent.io/3.2.1/platform.html)
   * [Schema Regisrty](http://docs.confluent.io/3.2.1/schema-registry/docs/index.html)
 

## QUICK START

* run Zookeeper
```
  > bin/zookeeper-server-start.sh config/zookeeper.properties
```
* run Kafka server
```
  > bin/kafka-server-start.sh config/server.properties
```
* run Schema Registry (optional)
```
  > ./bin/schema-registry-start ./etc/schema-registry/schema-registry.properties 
```

## Installation
```
  > git pull git@github.com:fibanez6/fibanez_java_kafka_examples.git
  > cd fibanez_java_kafka_examples
  > mvn clean install
```
## Demos
* [Simple Demo](../blob/master/kafka-client/src/main/java/com/fibanez/kafka/client/demo/SimpleDemo.java)
```
  > cd kafka-client/target
  >  java -cp kafka-client-1.0-SNAPSHOT-jar-with-dependencies.jar com.fibanez.kafka.client.demo.SimpleDemo
```
* [ByteArray Message Demo](../blob/master/kafka-client/src/main/java/com/fibanez/kafka/client/demo/ByteArrayDemo.java)
```
  > cd kafka-client/target
  >  java -cp kafka-client-1.0-SNAPSHOT-jar-with-dependencies.jar com.fibanez.kafka.client.demo.ByteArrayDemo
```
* [Avro Message Demo](../blob/master/kafka-client/src/main/java/com/fibanez/kafka/avro/demo/AvroDemo.java) (requires Schema Registry)
```
  > cd kafka-client/target
  >  java -cp kafka-client-1.0-SNAPSHOT-jar-with-dependencies.jar com.fibanez.kafka.avro.demo.AvroDemo
```
* [Delayed message Demo with ScheduledExecutorService](../blob/master/kafka-client/src/main/java/com/fibanez/kafka/delayedClient/demo/DelayedDemo.java)
```
  > cd kafka-client/target
  >  java -cp kafka-client-1.0-SNAPSHOT-jar-with-dependencies.jar com.fibanez.kafka.delayedClient.demo.DelayedDemo
```
* [Delayed message Demo with Quartz](../blob/master/kafka-client/src/main/java/com/fibanez/kafka/delayedClient/demo/DelayedWithQuartzDemo.java)
```
  > cd kafka-client/target
  >  java -cp kafka-client-1.0-SNAPSHOT-jar-with-dependencies.jar com.fibanez.kafka.delayedClient.demo.DelayedWithQuartzDemo
```
* [Stream DSL](../blob/master/kafka-stream/src/main/java/com/fibanez/kafka/streamDSL/wordCount/demo/WorldCountLambdaDemo.java)
```
  > cd kafka-stream/target
  >  java -cp kafka-client-1.0-SNAPSHOT-jar-with-dependencies.jar com.fibanez.kafka.streamDSL.wordCount.demo.WorldCountLambdaDemo
```



