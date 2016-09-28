package uk.gov.justice.services.test.utils.common.container;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * Start an embedded Artemis LocalTestServer. <br/>
 *
 * Start the server in the @BeforeClass annotation block and stop the server <br/>
 * in the @AfterClass annotation block. <br/>
 * 
 * Example usage: <br/>
 * 
 * <code>
 * 
 *  &#64;BeforeClass
 *    public static void beforeClass() {
 *        LocalTestServerManager.startServer();
 *    }
 *    
 * &#64;AfterClass
 *    public static void beforeClass() {
 *        LocalTestServerManager.stopServer();
 *    }
 *  
 * </code>
 * 
 * @see LocalTestServer
 */
public final class LocalTestServerManager {

    /**
     * Logger instance
     */
    private static final Logger LOG = LoggerFactory.getLogger(LocalTestServerManager.class);


    /**
     * LocalTestServer instance
     */
    private static LocalTestServer localTestServer;

    /**
     * capture the initialisation state of the server
     */
    private static volatile boolean initialized;

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                stopServer();
            } catch (Exception e) {
            }
        }));
    }

    /**
     * 
     * 
     */
    private LocalTestServerManager() {}



    /**
     * Start the server and check that that it does not fail with a <br/>
     * java.lang.ExceptionInInitializerError due to logging dependencies.
     * 
     * @throws Exception
     * 
     * @see java.lang.ExceptionInInitializerError
     * 
     */
    public static synchronized void startServer() throws Exception {

        if (initialized) {

            LOG.warn("The artemis LocalTestServer is already running.");

            return;
        }

        LOG.info("Starting the Artemis LocalTestServer");

        localTestServer = new LocalTestServer();

        localTestServer.start(new HashMap<String, Object>(), true);

        initialized = true;
    }

    /**
     * Stop the server
     * 
     * @throws Exception
     */
    public static void stopServer() throws Exception {

        if (!initialized) {

            LOG.warn("The artemis LocalTestServer is not running.");

            return;
        }

        boolean stopped = (localTestServer != null) ? localTestServer.stop() : false;

        LOG.info(String.join("", "Is the Artemis LocalTestServer stopped ? ",
                        String.valueOf(stopped)));

        initialized = !stopped;
    }
}
