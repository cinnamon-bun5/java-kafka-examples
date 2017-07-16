package com.fibanez.kafka.delayedClient.demo;

import com.fibanez.kafka.delayedClient.job.DelayedJobConsumer;
import com.fibanez.kafka.delayedClient.producer.DelayedProducer;
import com.fibanez.kafka.delayedClient.quartz.ShutDownSchedulerListener;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by fibanez on 10/6/17.
 */
public class DelayedWithQuartzDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(DelayedWithQuartzDemo.class);

    private  ExecutorService executor = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {

        try {
            DelayedWithQuartzDemo demo = new DelayedWithQuartzDemo();
            demo.start("delayedDemo", true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void start(String topic, boolean asyn) throws InterruptedException, SchedulerException {

        LOGGER.info("Starting byte array delayed with quartz demo");

        // NEW CONSUMER
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "byteArray-delayed-consumer-group");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());

        KafkaConsumer<Integer, byte[]> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(topic));
        LOGGER.info("Consumer is waiting on topics {}", consumer.listTopics());

        // NEW PRODUCER
        DelayedProducer producer = new DelayedProducer(topic,asyn);

        // CRON
        JobDataMap datamap = new JobDataMap();
        datamap.put("consumer", consumer);
        datamap.put("producer", producer.getKafkaProducer());

        // LISTENER TO CLOSE PRODUCER AND EXECUTOR
        SchedulerListener shutdownListener = new ShutDownSchedulerListener(executor, Arrays.asList(producer), "delayed");

        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.getListenerManager().addSchedulerListener(shutdownListener);

        JobDetail job = JobBuilder
                .newJob(DelayedJobConsumer.class)
                .withIdentity("delayedConsumer", "kafkaGroup")
                .usingJobData(datamap)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .startNow()
                .withIdentity("triggerConsumer", "kafkaGroup")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(30).withRepeatCount(2 * 5))
                .build();

        // Tell quartz to schedule the job using our trigger
        scheduler.scheduleJob(job, trigger);
        // and start it off
        scheduler.start();

        executor.submit(producer);

        // ctrl-c kill
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                while (scheduler.getCurrentlyExecutingJobs().size() > 0) {
                    Thread.sleep(3000);
                }
                scheduler.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

    }


}
