package io.confluent.examples.clients.basicavro;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class ProducerHumidity {

    private static final String TOPIC = "humidity";
    private static final Properties props = new Properties();
    private static String configFile;

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(final String[] args) throws IOException {
        if (args.length < 1) {
          // Backwards compatibility, assume localhost
          props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
          props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
        } else {
          // Load properties from a local configuration file
          // Create the configuration file (e.g. at '$HOME/.confluent/java.config') with configuration parameters
          // to connect to your Kafka cluster, which can be on your local host, Confluent Cloud, or any other cluster.
          // Documentation at https://docs.confluent.io/platform/current/tutorials/examples/clients/docs/java.html
          configFile = args[0];
          if (!Files.exists(Paths.get(configFile))) {
              throw new IOException(configFile + " not found.");
          } else {
            try (InputStream inputStream = new FileInputStream(configFile)) {
              props.load(inputStream);
            }
          }
        }

        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);

        try (KafkaProducer<String, Humidity> producer = new KafkaProducer<String, Humidity>(props)) {

            for (long i = 0; i < 10000; i++) {
                final String id = "Reading_" + Long.toString(i);
                final Humidity humidity = new Humidity(id, 1000.00d);
                final ProducerRecord<String, Humidity> record = new ProducerRecord<String, Humidity>(TOPIC, humidity.getId().toString(), humidity);
                producer.send(record);
                Thread.sleep(1000L);
            }

            producer.flush();
            System.out.printf("Successfully produced 10 messages to a topic called %s%n", TOPIC);

        } catch (final SerializationException e) {
            e.printStackTrace();
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }

    }

}
