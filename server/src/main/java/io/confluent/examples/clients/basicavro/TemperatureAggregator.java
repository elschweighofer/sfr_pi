package io.confluent.examples.clients.basicavro;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.Serialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.kstream.internals.TimeWindow;
import org.apache.kafka.streams.state.WindowStore;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;

public class TemperatureAggregator {
    private static final String INPUT_TOPIC = "temperature";
    private static final String OUTPUT_TOPIC = "temperature-average";

    public static void main(final String[] args) {

        // When configuring the default serdes of StreamConfig
        final Properties streamsConfiguration = new Properties();
        streamsConfiguration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, GenericAvroSerde.class);
        streamsConfiguration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, GenericAvroSerde.class);
        streamsConfiguration.put("schema.registry.url", "http://localhost:8081");

        final Map<String, String> serdeConfig = Collections.singletonMap("schema.registry.url",
                "http://localhost:8081");
        final Serde<GenericRecord> keyGenericAvroSerde = new GenericAvroSerde();
        keyGenericAvroSerde.configure(serdeConfig, true); // `true` for record keys
        final Serde<GenericRecord> valueGenericAvroSerde = new GenericAvroSerde();
        valueGenericAvroSerde.configure(serdeConfig, false); // `false` for record values


        final StreamsBuilder builder = new StreamsBuilder();
        final KStream<String , Integer> textLines = builder.stream("temperature");
        final Topology topology = builder.build();
        final KafkaStreams streams = new KafkaStreams(topology, streamsConfiguration);
        final KStream<String, Integer> onlyHighTemperatures = textLines.filter((key, value) -> value > 25 );


        streams.start();

        streams.close();
    }

}
