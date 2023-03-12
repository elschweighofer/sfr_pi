package org.acme.kafka;

import org.acme.kafka.quarkus.Humidity;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/humidity")
public class HumidityResource {
    private static final Logger LOGGER = Logger.getLogger(HumidityResource.class);

    @Channel("humidity")
    Emitter<Humidity> emitter;

    @POST
    public Response enqueueHumidity(Humidity humidity) {
        LOGGER.infof("Sending humidity value %s to Kafka", humidity.getValue());
        emitter.send(humidity);
        return Response.accepted().build();
    }

}