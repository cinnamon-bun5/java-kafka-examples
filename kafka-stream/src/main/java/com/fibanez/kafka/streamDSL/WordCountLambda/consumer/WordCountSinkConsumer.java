package com.fibanez.kafka.streamDSL.WordCountLambda.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Properties;

/**
 * Created by fibanez on 11/6/17.
 */
public class WordCountSinkConsumer implements Runnable  {

    private static final Logger LOGGER = LoggerFactory.getLogger(WordCountSinkConsumer.class);

    private final KafkaConsumer<String, Integer> consumer;
    private final String topic;

    private boolean shutdown;

    public WordCountSinkConsumer(String topic) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "wordcount-lambda-consumer-group");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
        consumer = new KafkaConsumer<>(props);
        this.topic = topic;
    }

    @Override
    public void run() {
        consumer.subscribe(Collections.singletonList(this.topic));

        LOGGER.info("Consumer is waiting on topics {}",  this.topic);

        try {
            ConsumerRecords<String, Integer> records;

            while (!shutdown) {

                records = consumer.poll(1000);

                if (records.count() > 0) {
                    LOGGER.info("Number of Records = " + records.count());
                }

                for (ConsumerRecord<String, Integer> record : records) {
                    LOGGER.info("Received message ({},{}) ", record.key(), record.value());
                }
            }

        } catch (WakeupException e) {
            LOGGER.info("WakeupException handled");
            // ignore, we're closing
        } catch (Throwable t) {
            LOGGER.error("Unexpected error", t);
        } finally {
            consumer.close();
        }
    }

    public void shutdown() {
        shutdown = true;
    }
}
