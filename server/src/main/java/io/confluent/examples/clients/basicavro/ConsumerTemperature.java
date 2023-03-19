package io.confluent.examples.clients.basicavro;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class ConsumerTemperature {
    private static final String TOPIC = "temperature";
    private static final Properties props = new Properties();
    private static String configFile;

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(final String[] args) throws IOException {

        if (args.length < 1) {
          // Backwards compatibility, assume localhost
          props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
          props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
        } else {
          // Load properties from a local configuration file
          // Create the configuration file (e.g. at '$HOME/.confluent/java.config') with configuration parameters
          // to connect to your Kafka cluster, which can be on your local host, Confluent Cloud, or any other cluster.
          // Documentation at https://docs.confluent.io/platform/current/tutorials/examples/clients/docs/java.html
          configFile = args[0];
          if (!Files.exists(Paths.get(configFile))) {
               //System.out.print("should be exception");
              throw new IOException(configFile + " not found.");
          } else {
            try (InputStream inputStream = new FileInputStream(configFile)) {
              props.load(inputStream);
            }
          }
        }

        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-Temperature");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true); 

        try (final KafkaConsumer<Long, Temperature> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Collections.singletonList(TOPIC));

            while (true) {
                final ConsumerRecords<Long, Temperature> records = consumer.poll(Duration.ofMillis(100));
                for (final ConsumerRecord<Long, Temperature> record : records) {
                    final Long key = record.key();
                    final Temperature value = record.value();
                    System.out.printf("####### #### key = %s, value = %s%n", key, value);
                }
            }

        }
    }
}
