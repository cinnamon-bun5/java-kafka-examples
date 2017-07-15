package com.fibanez.kafka.client.producer;

import com.fibanez.kafka.utils.StoppableRunnable;
import com.fibanez.kafka.client.model.KafkaMessage;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Created by fibanez on 10/6/17.
 */
public class ByteArrayProducer implements StoppableRunnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ByteArrayProducer.class);

    private final KafkaProducer<Integer, byte[]> producer;
    private final String topic;
    private final Boolean isAsync;

    private static String ssl_pwd ="{pwd}";

    public ByteArrayProducer(String topic, Boolean isAsync) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "ByteArrayProducer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());

        // SSL CONFIGURATION
        /*
        props.put("security.protocol", "SSL");
        props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, "/path_to_certificates/kafka.client.truststore.jks");
        props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, ssl_pwd);


        props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, "/path_to_certificates/kafka.client.keystore.jks");
        props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, ssl_pwd);
        props.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, ssl_pwd);
        */

        producer = new KafkaProducer<>(props);
        this.topic = topic;
        this.isAsync = isAsync;
    }

    @Override
    public void run() {
        int messageNo = 1;

        KafkaMessage message;

        while ( messageNo < 5 ) {
            try {
                message = new KafkaMessage();
                message.setMessageId((long) messageNo);
                message.setMessage("Message_" + messageNo);

                byte[] data = SerializationUtils.serialize(message);

                if (isAsync) { // Send asynchronously
                    producer.send(new ProducerRecord<>(
                            topic,
                            messageNo,
                            data), new ByteArrayCallBack(messageNo, message));
                }
                else { // Send synchronously
                    producer.send(new ProducerRecord<>(
                            topic,
                            messageNo,
                            data)).get();

                    LOGGER.info("Sent message: (" + messageNo + ", " + message + ")");
                }
            }
            catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
            ++messageNo;
        }

        shutdown();
    }

    @Override
    public void shutdown() {
        producer.close(5, TimeUnit.SECONDS);
    }
}


class ByteArrayCallBack implements Callback {

    private static final Logger LOGGER = LoggerFactory.getLogger(ByteArrayCallBack.class);

    private final int key;
    private final KafkaMessage message;

    public ByteArrayCallBack(int key, KafkaMessage message) {
        this.key = key;
        this.message = message;
    }
    /**
     * A callback method the user can implement to provide asynchronous handling of request completion. This method will
     * be called when the record sent to the server has been acknowledged. Exactly one of the arguments will be
     * non-null.
     *
     * @param metadata  The metadata for the record that was sent (i.e. the partition and offset). Null if an error
     *                  occurred.
     * @param exception The exception thrown during processing of this record. Null if no error occurred.
     */
    public void onCompletion(RecordMetadata metadata, Exception exception) {
        if (metadata != null) {
            long elapsedTime = System.currentTimeMillis() - metadata.timestamp();
            LOGGER.info(
                    "Sent message: topic = {}, partition = {}, offset = {}, timestamp = {} in {} ms \n Sent message({},{})",
                    metadata.topic(), metadata.partition(), metadata.offset(), new Date(metadata.timestamp()),elapsedTime, key, message);
        } else {
            LOGGER.error(exception.getMessage(), exception);
        }
    }
}