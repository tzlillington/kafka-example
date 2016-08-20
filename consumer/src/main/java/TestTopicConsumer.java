

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.Closeable;
import java.io.IOException;
import java.util.*;

public class TestTopicConsumer implements Closeable {
    public static final String TEST_TOPIC = "test-topic";
    public static final int TIMEOUT = 100;
    private final Consumer<String, String> consumer;

    public TestTopicConsumer(String brokerHost, int brokerPort) {
        Properties props = new Properties();
        props.put("bootstrap.servers", brokerHost + ":" + brokerPort);
        props.put("acks", "all");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("group.id", "test-consumer");
        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(TEST_TOPIC));
    }

    public List<String> receiveBatch() {
        ConsumerRecords<String, String> records = consumer.poll(TIMEOUT);
        List<String> messages = new ArrayList<>();
        for (ConsumerRecord<String, String> record : records) {
            messages.add(record.value());
        }

        return messages;
    }

    public void commit() {
        consumer.commitSync();
    }

    @Override
    public void close() throws IOException {
        consumer.close();
    }
}
