package com.fibanez.kafka.streamDSL.wordCountLambda.consumer;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * Created by fibanez on 11/6/17.
 */
public class WordCountStreamConsumer implements Runnable  {

    private static final Logger LOGGER = LoggerFactory.getLogger(WordCountStreamConsumer.class);

    private final KafkaStreams streams;

    private final String topic;
    private final String outTopic;

    public WordCountStreamConsumer(String topic, String outTopic) {
        Properties props = new Properties();
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-lambda-consumer-id");
        props.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        // Records should be flushed every 10 seconds. This is less than the default
        // in order to keep this example interactive.
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 10 * 1000);

        this.topic = topic;
        this.outTopic = outTopic;

        // Set up serializers and deserializers, which we will use for overriding the default serdes
        // specified above.
        final Serde<String> stringSerde = Serdes.String();
        final Serde<Long> longSerde = Serdes.Long();

        // In the subsequent lines we define the processing topology of the Streams application.
        final KStreamBuilder builder = new KStreamBuilder();

        // Construct a `KStream` from the input topic "TextLinesTopic", where message values
        // represent lines of text (for the sake of this example, we ignore whatever may be stored
        // in the message keys).
        //
        // Note: We could also just call `builder.stream("TextLinesTopic")` if we wanted to leverage
        // the default serdes specified in the Streams configuration above, because these defaults
        // match what's in the actual topic.  However we explicitly set the deserializers in the
        // call to `stream()` below in order to show how that's done, too.
        final KStream<String, String> textLines = builder.stream(stringSerde, stringSerde, topic);

        final Pattern pattern = Pattern.compile("\\W+", Pattern.UNICODE_CHARACTER_CLASS);

        final KTable<String, Long> wordCounts = textLines
                // Split each text line, by whitespace, into words.  The text lines are the record
                // values, i.e. we can ignore whatever data is in the record keys and thus invoke
                // `flatMapValues()` instead of the more generic `flatMap()`.
                .flatMapValues(value -> Arrays.asList(pattern.split(value.toLowerCase())))
                // Count the occurrences of each word (record key).
                //
                // This will change the stream type from `KStream<String, String>` to `KTable<String, Long>`
                // (word -> count).  In the `count` operation we must provide a name for the resulting KTable,
                // which will be used to name e.g. its associated state store and changelog topic.
                //
                // Note: no need to specify explicit serdes because the resulting key and value types match our default serde settings
                .groupBy((key, word) -> word)
                .count("Counts");

        // Write the `KStream<String, Long>` to the output topic.
        wordCounts.to(stringSerde, longSerde, outTopic);

        // Now that we have finished the definition of the processing topology we can actually run
        // it via `start()`.  The Streams application as a whole can be launched just like any
        // normal Java application that has a `main()` method.
        streams = new KafkaStreams(builder, props);
    }


    @Override
    public void run() {
        // Always (and unconditionally) clean local state prior to starting the processing topology.
        // We opt for this unconditional call here because this will make it easier for you to play around with the example
        // when resetting the application for doing a re-run (via the Application Reset Tool,
        // http://docs.confluent.io/current/streams/developer-guide.html#application-reset-tool).
        //
        // The drawback of cleaning up local state prior is that your app must rebuilt its local state from scratch, which
        // will take time and will require reading all the state-relevant data from the Kafka cluster over the network.
        // Thus in a production scenario you typically do not want to clean up always as we do here but rather only when it
        // is truly needed, i.e., only under certain conditions (e.g., the presence of a command line flag for your app).
        // See `ApplicationResetExample.java` for a production-like example.
        streams.cleanUp();
        streams.start();

        LOGGER.info("Stream Consumer is waiting on topics {}",  this.topic);
    }

    public void shutdown() {
        streams.close(5, TimeUnit.SECONDS);
    }
}
