package com.fibanez.kafka.delayedClient.consumer;

import com.fibanez.kafka.utils.StoppableRunnable;
import com.fibanez.kafka.delayedClient.model.KafkaDelayedMessage;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by fibanez on 10/6/17.
 */
public class DelayedConsumer implements StoppableRunnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(DelayedConsumer.class);

    private final KafkaConsumer<Integer, byte[]> consumer;
    private final String topic;

    private KafkaProducer producer;

    private AtomicBoolean shutdown = new AtomicBoolean(false);
    private CountDownLatch shutdownLatch = new CountDownLatch(1);

    public DelayedConsumer(KafkaProducer producer, String topic) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "byteArray-delayed-consumer-group");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());

        this.consumer = new KafkaConsumer<>(props);
        this.producer = producer;
        this.topic = topic;

        consumer.subscribe(Collections.singletonList(this.topic));
        LOGGER.info("Consumer is waiting on topics {}",  this.topic);
    }

    @Override
    public void run() {

        try {

            KafkaDelayedMessage message;
            ConsumerRecords<Integer, byte[]> records;

            if (!shutdown.get()) {

                consumer.resume(consumer.assignment());

                records = consumer.poll(10000);

                if (records.count() > 0) {
                    LOGGER.info("Number of Records = " + records.count());
                }

                for (ConsumerRecord<Integer, byte[]> record : records) {

                    message = (KafkaDelayedMessage) SerializationUtils.deserialize(record.value());

                    if (message.getDelayTo() < System.currentTimeMillis()) {
                        LOGGER.info(
                                "Consume message {} with delayTo {} : partition = {}, offset = {}, timestamp = {}",
                                record.key(), new Date(message.getDelayTo()), record.partition(), record.offset(), new Date(record.timestamp())
                        );
                    } else {
                        LOGGER.info("Requeue message {} with delayTo {} to: topic = {}",  record.key(), new Date(message.getDelayTo()), record.topic());
                        try {
                            LOGGER.debug("Record {} requeue to: topic = {}", record.key(), record.topic());

                            producer.send(new ProducerRecord<>( record.topic(), record.key(), record.value())).get();
                        } catch (Exception e) {
                            LOGGER.error(e.getMessage(), e);
                        }
                    }
                }

                LOGGER.info("consumer paused\n\n\n\n");
                consumer.pause(consumer.assignment());
            }
            else {
                consumer.close();
                shutdownLatch.countDown();
            }

        } catch (WakeupException e) {
            // ignore, we're closing
            LOGGER.info("WakeupException handled");
            consumer.close();
            shutdownLatch.countDown();
        } catch (Throwable t) {
            LOGGER.error("Unexpected error", t);
            consumer.close();
            shutdownLatch.countDown();
        }
    }

    @Override
    public void shutdown() {
        try {
            shutdown.set(true);
            consumer.wakeup();
            shutdownLatch.await(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOGGER.error("Error", e);
        }
    }
}
