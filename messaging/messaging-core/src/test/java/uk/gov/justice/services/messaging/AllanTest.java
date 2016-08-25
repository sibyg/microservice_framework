package uk.gov.justice.services.messaging;

import static java.util.UUID.randomUUID;
import static uk.gov.justice.services.messaging.DefaultJsonEnvelope.envelope;
import static uk.gov.justice.services.messaging.JsonObjectMetadata.metadataOf;

import org.junit.Test;

public class AllanTest {

    @Test
    public void shouldPrettyPrintAJsonObject() throws Exception {

        final JsonEnvelope envelope = envelope()
                .with(metadataOf(randomUUID(), "metadata-name")
                        .withCausation(randomUUID(), randomUUID(), randomUUID())
                        .withClientCorrelationId("clientId")
                )
                .withPayloadOf("value_1", "object", "name_1")
                .withPayloadOf("value_2", "object", "name_2")
                .withPayloadOf("value_3", "name_3")
                .build();


        System.out.println(envelope.toDebugStringPrettyPrint());

    }
}