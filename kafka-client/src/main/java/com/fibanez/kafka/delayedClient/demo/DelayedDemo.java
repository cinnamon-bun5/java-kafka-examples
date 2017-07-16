package com.fibanez.kafka.delayedClient.demo;

import com.fibanez.kafka.delayedClient.consumer.DelayedConsumer;
import com.fibanez.kafka.delayedClient.producer.DelayedProducer;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by fibanez on 10/6/17.
 */
public class DelayedDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(DelayedDemo.class);

    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(2);

    public static void main(String[] args) {

        try {
            DelayedDemo demo = new DelayedDemo();
            demo.start("delayedDemo", true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void start(String topic, boolean asyn) throws InterruptedException, SchedulerException {

        // ctrl-c kill
        Runtime.getRuntime().addShutdownHook(new Thread(() -> { shutdown(); }));

        LOGGER.info("Starting delayed demo");

        // NEW PRODUCER
        DelayedProducer producer = new DelayedProducer(topic,asyn);

        // NEW DELAYED CONSUMER
        DelayedConsumer delayedConsumer = new DelayedConsumer(producer.getKafkaProducer(), topic);

        int initialDelay = 0;
        int period = 30;
        scheduledExecutor.scheduleAtFixedRate(delayedConsumer, initialDelay, period, TimeUnit.SECONDS);
        executor.submit(producer);

        TimeUnit.SECONDS.sleep(period * 10);

        delayedConsumer.shutdown();
        producer.shutdown();

        shutdown();

        LOGGER.info("Finished delayed demo");

        System.exit(0);

    }

    public void shutdown() {
        try {
            scheduledExecutor.shutdown();
            scheduledExecutor.awaitTermination(5, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            LOGGER.error("tasks interrupted");
            scheduledExecutor.shutdownNow();
        }
        finally {
            if (!scheduledExecutor.isTerminated()) {
                LOGGER.error("cancel non-finished tasks");
            }
            LOGGER.info("shutdown scheduledExecutor finished");
        }

        try {
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            LOGGER.error("tasks interrupted");
        }
        finally {
            if (!executor.isTerminated()) {
                LOGGER.error("cancel non-finished tasks");
                executor.shutdownNow();
            }
            LOGGER.info("shutdown executor finished");
        }

    }


}
