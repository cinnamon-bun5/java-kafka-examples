package com.fibanez.kafka.streamDSL.wordCount.demo;

import com.fibanez.kafka.streamDSL.wordCount.consumer.WordCountSimpleConsumer;
import com.fibanez.kafka.streamDSL.wordCount.consumer.WordCountSinkConsumer;
import com.fibanez.kafka.streamDSL.wordCount.producer.WordCountLambdaProducer;
import com.fibanez.kafka.streamDSL.wordCount.consumer.WordCountStreamConsumer;
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

    private  ExecutorService executor = Executors.newFixedThreadPool(4);

    public static void main(String[] args) {

        try {
            WorldCountLambdaDemo demo = new WorldCountLambdaDemo();
            demo.start("worldCountLambdaSource", "worldCountLambdaSink", false);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void start(String sourceTopic, String sinkTopic, boolean asyn) throws InterruptedException {

        LOGGER.info("Starting world count lambda demo");

        // Client
        WordCountLambdaProducer producer = new WordCountLambdaProducer(sourceTopic,asyn);
        WordCountSimpleConsumer simpleConsumer = new WordCountSimpleConsumer(sourceTopic);

        // Stream
        WordCountStreamConsumer streamConsumer = new WordCountStreamConsumer(sourceTopic, sinkTopic);
        WordCountSinkConsumer sinkConsumer = new WordCountSinkConsumer(sinkTopic);

        executor.submit(simpleConsumer);
        executor.submit(streamConsumer);
        executor.submit(sinkConsumer);
        executor.submit(producer);

        TimeUnit.SECONDS.sleep(20);

        simpleConsumer.shutdown();
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
