package producer;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;

public class HumidityTransformer {
    private static StreamsBuilder builder = new StreamsBuilder();
    final private static String humidityTopic = "humidity";


    public static void buildAverageForHumidity() {
        KStream<String, String> textLines =
                builder.stream(humidityTopic, Consumed.with(Serdes.String(), Serdes.String()));

        textLines.map((key, val) -> {
                System.out.println((key));
            return null;
        });
    }

}