package producer;

import io.smallrye.mutiny.Multi;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import io.smallrye.reactive.messaging.kafka.Record;


@ApplicationScoped
public class HumidityResource {

    private static final Logger LOG = Logger.getLogger(HumidityResource.class);

    private Random random = new Random();


    @Outgoing("humidity")
    public Multi<Record<Integer, String>> humidityRecords() {
        return Multi.createFrom().ticks().every(Duration.ofMillis(5000))
                .onOverflow().drop()
                .map(tick -> {
                    Integer humidity = (int) BigDecimal.valueOf(random.nextGaussian() * 15 )
                            .setScale(1, RoundingMode.HALF_UP)
                            .doubleValue();

                    LOG.infov("humidity: " + humidity);
                    return Record.of(Math.abs(humidity), "fake datetime");
                });
    }


}