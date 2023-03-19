package io.confluent.examples.clients.basicavro;


import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

import static io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;
import static org.apache.kafka.common.serialization.Serdes.Double;
import static org.apache.kafka.common.serialization.Serdes.Long;
import static org.apache.kafka.streams.StreamsConfig.*;

public class RunningAverage {

    final static String inputTopic = "temperature";
    final static String averageTopic = "temperature-average";
    //region buildStreamsProperties
    protected Properties buildStreamsProperties(final Properties envProps) {
        Properties config = new Properties();
        //config.putAll(envProps);

        config.put(APPLICATION_ID_CONFIG, "kafka-temperature");
        config.put(BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // to-do Backslash?
        config.put(DEFAULT_KEY_SERDE_CLASS_CONFIG, Long().getClass());
        config.put(DEFAULT_VALUE_SERDE_CLASS_CONFIG, Double().getClass());
        config.put(SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
        config.put(REPLICATION_FACTOR_CONFIG, 1);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        config.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);

        return config;
    }
    //endregion

    //region createTopics

    /**
     * Create topics using AdminClient API
     */
    private void createTopics(final Properties envProps) {
        Map<String, Object> config = new HashMap<>();

        config.put("bootstrap.servers", envProps.getProperty("bootstrap.servers"));
        AdminClient client = AdminClient.create(config);

        List<NewTopic> topics = new ArrayList<>();

        topics.add(new NewTopic(
                inputTopic,1, (short) 1));
                //envProps.getProperty("input.ratings.topic.name"),
                //parseInt(envProps.getProperty("input.ratings.topic.partitions")),
                //parseShort(envProps.getProperty("input.ratings.topic.replication.factor"))));

        topics.add(new NewTopic(averageTopic,1,(short) 1));
               /* envProps.getProperty("output.rating-averages.topic.name"),
                parseInt(envProps.getProperty("output.rating-averages.topic.partitions")),
                parseShort(envProps.getProperty("output.rating-averages.topic.replication.factor"))));
                */
        client.createTopics(topics);
        client.close();

    }
    //endregion

    private void run() {

        Properties envProps = this.loadEnvProperties();
        Properties streamProps = this.buildStreamsProperties(envProps);
        Topology topology = this.buildTopology(new StreamsBuilder(), envProps);

        this.createTopics(envProps);

        final KafkaStreams streams = new KafkaStreams(topology, streamProps);
        final CountDownLatch latch = new CountDownLatch(1);

        // Attach shutdown handler to catch Control-C.
        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
            @Override
            public void run() {
                streams.close(Duration.ofSeconds(5));
                latch.countDown();
            }
        });

        try {
            streams.cleanUp();
            streams.start();
            latch.await();
        } catch (Throwable e) {
            System.exit(1);
        }
        System.exit(0);
    }

    protected static KTable<Long, Double> getTemperatureAverageTable(KStream<Long , Temperature> temperatures,
                                                                       String avgTemperatureTopicName,
                                                                       SpecificAvroSerde<CountAndSum> countAndSumSerde) {

        // Grouping Ratings
        KGroupedStream<Long, Double> temperaturesById = temperatures
                .map((key, temperature) -> new KeyValue<>(temperature.getId(), temperature.getTemperature()))
                .groupByKey();

        final KTable<Long, CountAndSum> temperatureCountAndSum =
                temperaturesById.aggregate(() -> new CountAndSum(0L, 0.0),
                        (key, value, aggregate) -> {
                            aggregate.setCount(aggregate.getCount() + 1);
                            aggregate.setSum(aggregate.getSum() + value);
                            return aggregate;
                        },
                        Materialized.with(Long(), countAndSumSerde));

        final KTable<Long, Double> temperatureAverage =
                temperatureCountAndSum.mapValues(value -> value.getSum() / value.getCount(),
                        Materialized.as("average-temperature"));// to-do: warum anders benannt als output topic?

        // persist the result in topic
        temperatureAverage.toStream().to(avgTemperatureTopicName);
        return temperatureAverage;
    }

    //region buildTopology
    private Topology buildTopology(StreamsBuilder bldr,
                                   Properties envProps) {

        KStream<Long, Temperature> temperatureStream = bldr.stream(inputTopic,
                Consumed.with(Serdes.Long(), getTemperatureSerde(envProps)));

        getTemperatureAverageTable(temperatureStream, averageTopic, getCountAndSumSerde(envProps));

        // finish the topology
        return bldr.build();
    }
    //endregion

    public static SpecificAvroSerde<CountAndSum> getCountAndSumSerde(Properties envProps) {
        SpecificAvroSerde<CountAndSum> serde = new SpecificAvroSerde<>();
        serde.configure(getSerdeConfig(envProps), false);
        return serde;
    }

    public static SpecificAvroSerde<Temperature> getTemperatureSerde(Properties envProps) {
        SpecificAvroSerde<Temperature> serde = new SpecificAvroSerde<>();
        serde.configure(getSerdeConfig(envProps), false);
        return serde;
    }

    protected static Map<String, String> getSerdeConfig(Properties config) {
        final HashMap<String, String> map = new HashMap<>();

        final String srUrlConfig = config.getProperty(SCHEMA_REGISTRY_URL_CONFIG);
        map.put(SCHEMA_REGISTRY_URL_CONFIG, ofNullable(srUrlConfig).orElse(""));
        return map;
    }

    protected Properties loadEnvProperties() {
        final Config load = ConfigFactory.load();
        final Map<String, Object> map = load.entrySet()
                .stream()
                // ignore java.* and system properties
                .filter(entry -> Stream
                        .of("java", "user", "sun", "os", "http", "ftp", "line", "file", "awt", "gopher", "socks", "path")
                        .noneMatch(s -> entry.getKey().startsWith(s)))
                .peek(
                        filteredEntry -> System.out.println(filteredEntry.getKey() + " : " + filteredEntry.getValue().unwrapped()))
                .collect(toMap(Map.Entry::getKey, y -> y.getValue().unwrapped()));
        Properties props = new Properties();
        props.putAll(map);
        return props;
    }

    public static void main(String[] args) {
        new RunningAverage().run();
    }
}