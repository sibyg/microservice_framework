package uk.gov.justice.raml.jms.it;

import static javax.json.Json.createObjectBuilder;
import static uk.gov.justice.services.messaging.DefaultJsonEnvelope.envelope;
import static uk.gov.justice.services.messaging.JsonObjectMetadata.metadataOf;
import static org.junit.Assert.fail;

import java.util.Properties;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.json.JsonObject;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.apache.openejb.OpenEjbContainer;
import org.apache.openejb.testing.Configuration;
import org.apache.openejb.testng.PropertiesBuilder;
import org.apache.openejb.util.NetworkUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.gov.justice.services.test.utils.common.container.LocalTestServerManager;

public abstract class AbstractJmsAdapterGenerationIT {

    /**
     * Logger instance
     */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractJmsAdapterGenerationIT.class);


    private final static ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory(
                    "tcp://localhost:61616?broker.persistent=false&jms.useAsyncSend=false");


    private static int port = -1;


    @BeforeClass
    public static void beforeClass() {
        port = NetworkUtil.getNextAvailablePort();
        try{
            LocalTestServerManager.startServer();
        }catch(Exception e){
            LOG.error("", e);
            fail(e.getMessage());
        }
    }

    @AfterClass
    public static void afterClass() {
        try{
            LocalTestServerManager.stopServer();
        }catch(Exception e){
            LOG.error("", e);
        }
    }

    @Configuration
    public Properties properties() {
        return new PropertiesBuilder().property("httpejbd.port", Integer.toString(port))
                        .property(OpenEjbContainer.OPENEJB_EMBEDDED_REMOTABLE, "true").build();
    }

    protected void sendEnvelope(String metadataId, String commandName, Destination queue)
                    throws JMSException {
        sendEnvelope(metadataId, commandName, queue, createObjectBuilder().build());
    }

    protected void sendEnvelope(String metadataId, String commandName, Destination queue,
                    JsonObject payload) throws JMSException {
        try (final Connection connection = getConnection();
                        final Session session = connection.createSession()) {
            TextMessage message = session.createTextMessage();
            message.setText(envelope().with(metadataOf(metadataId, commandName)).toJsonString());
            message.setStringProperty("CPPNAME", commandName);
            try (MessageProducer producer = session.createProducer(queue)) {
                producer.send(message);
            }
        }
    }

    private Connection getConnection() throws JMSException {
        final Connection connection = cf.createConnection();
        connection.start();
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            LOG.warn(e.toString());
        }
        return connection;
    }
}
