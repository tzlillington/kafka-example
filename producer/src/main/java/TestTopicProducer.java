

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.Closeable;
import java.io.IOException;
import java.util.Properties;

public class TestTopicProducer implements Closeable {
    public static final String TEST_TOPIC = "test-topic";
    private final Producer<String, String> producer;

    public TestTopicProducer(String brokerHost, int brokerPort) {
        Properties props = new Properties();
        props.put("bootstrap.servers", brokerHost + ":" + brokerPort);
        props.put("acks", "all");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("client.id", "test-producer");
        producer = new KafkaProducer<>(props);
    }

    public void send(String message) {
        producer.send(new ProducerRecord<>(TEST_TOPIC, message));
        producer.flush();
    }

    @Override
    public void close() throws IOException {
        producer.close();
    }
}
