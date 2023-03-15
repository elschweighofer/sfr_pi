package producer;

import com.oracle.svm.core.annotate.Inject;
import io.smallrye.mutiny.Multi;
import org.acme.kafka.quarkus.Humidity;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import io.smallrye.reactive.messaging.kafka.Record;


@ApplicationScoped
@Path("/humidity")
public class HumidityResource {

    private static final Logger LOG = Logger.getLogger(HumidityResource.class);

    private Random random = new Random();
/*

    @Outgoing("humidity")
    public Multi<Record<Integer, Humidity>> humidityRecords() {
        return Multi.createFrom().ticks().every(Duration.ofMillis(5000))
                .onOverflow().drop()
                .map(tick -> {
                    Integer humidity = (int) BigDecimal.valueOf(random.nextGaussian() * 15 )
                            .setScale(1, RoundingMode.HALF_UP)
                            .doubleValue();

                    LOG.infov("humidity: " + humidity);
                    return Record.of(1, new Humidity(humidity,"datetime"));
                });
    }
*/
    /**    private static final Logger LOG = Logger.getLogger(HumidityResource.class);

     private Random random = new Random(); */
     @Inject
     @Channel("humidity")
    Emitter<Humidity> emitter;


     @POST
     public Response enqueueMovie(Humidity humidity) {
     LOG.infov("humidity sending to kafka: " + humidity);

     emitter.send(humidity);
     return Response.accepted().build();
     }

}