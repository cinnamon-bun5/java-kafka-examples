package com.fibanez.kafka.client.demo;

import com.fibanez.kafka.client.consumer.ByteArrayConsumer;
import com.fibanez.kafka.client.producer.ByteArrayProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by fibanez on 10/6/17.
 */
public class ByteArrayDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(ByteArrayDemo.class);

    private  ExecutorService executor = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {

        try {
            ByteArrayDemo demo = new ByteArrayDemo();
            demo.start("byteArrayDemo", true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void start(String topic, boolean asyn) throws InterruptedException {

        LOGGER.info("Starting byte array demo");

        executor.submit(new ByteArrayConsumer(topic));
        executor.submit(new ByteArrayProducer(topic,asyn));

        TimeUnit.SECONDS.sleep(10);

        shutdown();

        LOGGER.info("Finished byte array demo");

        System.exit(0);
    }

    public void shutdown() {
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
            }
            executor.shutdownNow();
            LOGGER.error("shutdown finished");
        }

    }

}
