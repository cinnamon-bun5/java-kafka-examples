package com.fibanez.kafka.delayedClient.job;

import com.fibanez.kafka.delayedClient.model.KafkaDelayedMessage;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.WakeupException;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by fibanez on 15/7/17.
 */
@DisallowConcurrentExecution
public class DelayedJobConsumer implements InterruptableJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(DelayedJobConsumer.class);

    private static AtomicBoolean shutdown = new AtomicBoolean(false);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobDataMap dataMap = context.getJobDetail().getJobDataMap();

        KafkaConsumer consumer = (KafkaConsumer) dataMap.get("consumer");
        KafkaProducer producer = (KafkaProducer) dataMap.get("producer");

        if (!shutdown.get()) {

            try {
                consumer.resume(consumer.assignment());

                KafkaDelayedMessage message;
                ConsumerRecords<Integer, byte[]> records;

                records = consumer.poll(10000);

                if (records.count() > 0) {
                    LOGGER.info("Number of Records = " + records.count());
                } else {
                    LOGGER.info("no records");
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
                        requeueRecord(producer, record);
                    }
                }

                LOGGER.info("consumer paused\n\n\n\n");
                consumer.pause(consumer.assignment());

            } catch (WakeupException e) {
                // ignore, we're closing
                LOGGER.info("WakeupException handled");
                consumer.close(2, TimeUnit.SECONDS);
            } catch (Throwable t) {
                LOGGER.error("Unexpected error", t);
                consumer.close(2, TimeUnit.SECONDS);
            }
        } else {
            consumer.wakeup();
        }

    }

    private void requeueRecord(KafkaProducer producer, ConsumerRecord record) {
        try {
            LOGGER.debug("Record {} requeue to: topic = {}", record.key(), record.topic());

            producer.send(new ProducerRecord<>( record.topic(), record.key(), record.value())).get();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }


    @Override
    public void interrupt() throws UnableToInterruptJobException {
        shutdown.set(true);
    }
}
