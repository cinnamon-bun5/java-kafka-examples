package com.fibanez.kafka.streamDSL.demo;

import com.fibanez.kafka.streamDSL.consumer.WordCountLambdaConsumer;
import com.fibanez.kafka.streamDSL.consumer.WordCountLambdaStreamConsumer;
import com.fibanez.kafka.streamDSL.producer.WordCountLambdaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by fibanez on 10/6/17.
 */
public class WorldCountLambdaDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorldCountLambdaDemo.class);

    private  ExecutorService executor = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {

        try {
            WorldCountLambdaDemo demo = new WorldCountLambdaDemo();
            demo.start("worldCountLambdaSource", "worldCountLambdaSink", true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void start(String sourceTopic, String sinkTopic, boolean asyn) throws InterruptedException {

        LOGGER.info("Starting world count lambda demo");

        WordCountLambdaProducer producer = new WordCountLambdaProducer(sourceTopic,asyn);
        WordCountLambdaStreamConsumer streamConsumer = new WordCountLambdaStreamConsumer(sourceTopic, sinkTopic);
        WordCountLambdaConsumer sinkConsumer = new WordCountLambdaConsumer(sinkTopic);

        executor.submit(streamConsumer);
        executor.submit(sinkConsumer);

        TimeUnit.SECONDS.sleep(2);

        executor.submit(producer);

        TimeUnit.SECONDS.sleep(10);

        streamConsumer.shutdown();
        sinkConsumer.shutdown();
        producer.shutdown();

        shutdown();

        LOGGER.info("Finished world count lambda demo");

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
            LOGGER.info("shutdown finished");
        }

    }

}
