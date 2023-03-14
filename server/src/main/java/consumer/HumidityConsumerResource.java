package consumer;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.acme.kafka.quarkus.Humidity;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.jboss.resteasy.reactive.RestStreamElementType;

import io.smallrye.mutiny.Multi;

@ApplicationScoped
@Path("/consumed-humidity")
public class HumidityConsumerResource {

    @Channel("humidity-from-kafka")
    Multi<Humidity> humidity;

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @RestStreamElementType(MediaType.TEXT_PLAIN)
    public Multi<String> stream() {
        return humidity.map(humidity -> String.format("'%s' from %s", humidity.getValue(), humidity.getDatetime()));
    }
}