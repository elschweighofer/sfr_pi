package backend.kafka;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;


public class OLDTemperatureAggregator {
    private static final String INPUT_TOPIC = "temperature";
    private static final String OUTPUT_TOPIC = "temperature-average";

    public static void main(final String[] args) {

        // When configuring the default serdes of StreamConfig
        final Properties streamsConfiguration = new Properties();
        streamsConfiguration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.LongSerde.class);
        streamsConfiguration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SpecificAvroSerde.class);
        streamsConfiguration.put("schema.registry.url", "http://localhost:8081");
        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "my-first-streams-application");
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        final Map<String, String> serdeConfig = Collections.singletonMap("schema.registry.url",
                "http://localhost:8081");


        final StreamsBuilder builder = new StreamsBuilder();
        final KStream<Long, Temperature> textLines = builder.stream("temperature", Consumed.with(Serdes.Long(), getTemperatureSerde(null))).peek((k, v) -> System.out.println("[UNMAPPED] Key: {}, Value: {}" + k + "," + v));
        final Topology topology = builder.build();
        final KafkaStreams streams = new KafkaStreams(topology, streamsConfiguration);

        streams.start();
        // should stream filtered stream back to topic
        //onlyHighTemperatures.to("temperatures-high");
    }

    public static SpecificAvroSerde<Temperature> getTemperatureSerde(Properties envProps) {
        SpecificAvroSerde<Temperature> serde = new SpecificAvroSerde<>();
        serde.configure(Collections.singletonMap("schema.registry.url",
                "http://localhost:8081"), false);
        return serde;
    }
}
