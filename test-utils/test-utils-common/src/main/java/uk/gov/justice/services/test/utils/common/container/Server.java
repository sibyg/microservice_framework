package uk.gov.justice.services.test.utils.common.container;

import javax.naming.InitialContext;
import java.rmi.Remote;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.activemq.artemis.api.jms.JMSFactoryType;
import org.apache.activemq.artemis.core.security.Role;
import org.apache.activemq.artemis.core.server.ActiveMQServer;
import org.apache.activemq.artemis.jms.server.JMSServerManager;

/**
 * The remote interface exposed by TestServer.
 */
public interface Server extends Remote {

    int getServerID() throws Exception;

    /**
     * @param attrOverrides - server attribute overrides that will take precedence over values read
     *        from configuration files.
     */
    void start(HashMap<String, Object> configuration, boolean clearDatabase) throws Exception;

    /**
     * @return true if the server was stopped indeed, or false if the server was stopped already
     *         when the method was invoked.
     */
    boolean stop() throws Exception;

    /**
     * For a remote server, it "abruptly" kills the VM running the server. For a local server it
     * just stops the server.
     */
    void kill() throws Exception;

    /**
     * When kill is called you are actually schedulling the server to be killed in few milliseconds.
     * There are certain cases where we need to assure the server was really killed. For that we
     * have this simple ping we can use to verify if the server still alive or not.
     */
    void ping() throws Exception;

    /**
    */
    void startServerPeer() throws Exception;

    void stopServerPeer() throws Exception;

    boolean isStarted() throws Exception;

    ActiveMQServer getServerPeer() throws Exception;

    void createQueue(String name, String jndiName) throws Exception;

    void destroyQueue(String name, String jndiName) throws Exception;

    void createTopic(String name, String jndiName) throws Exception;

    void destroyTopic(String name, String jndiName) throws Exception;



    /**
     * Destroys a programatically created destination.
     */
    // boolean undeployDestinationProgrammatically(boolean isQueue, String name) throws Exception;
    void deployConnectionFactory(String clientId, JMSFactoryType type, String objectName,
                    int prefetchSize, int defaultTempQueueFullSize, int defaultTempQueuePageSize,
                    int defaultTempQueueDownCacheSize, boolean supportsFailover,
                    boolean supportsLoadBalancing, int dupsOkBatchSize, boolean blockOnAcknowledge,
                    final String... jndiBindings) throws Exception;

    void deployConnectionFactory(String objectName, int prefetchSize, int defaultTempQueueFullSize,
                    int defaultTempQueuePageSize, int defaultTempQueueDownCacheSize,
                    final String... jndiBindings) throws Exception;

    void deployConnectionFactory(String objectName, boolean supportsFailover,
                    boolean supportsLoadBalancing, final String... jndiBindings) throws Exception;

    void deployConnectionFactory(String clientID, String objectName, final String... jndiBindings)
                    throws Exception;

    void deployConnectionFactory(String objectName, int prefetchSize, final String... jndiBindings)
                    throws Exception;

    void deployConnectionFactory(String objectName, final String... jndiBindings) throws Exception;

    void deployConnectionFactory(String objectName, JMSFactoryType type,
                    final String... jndiBindings) throws Exception;

    void undeployConnectionFactory(String objectName) throws Exception;

    void configureSecurityForDestination(String destName, boolean isQueue, Set<Role> roles)
                    throws Exception;

    ActiveMQServer getActiveMQServer() throws Exception;

    InitialContext getInitialContext() throws Exception;

    void removeAllMessages(String destination, boolean isQueue) throws Exception;

    Long getMessageCountForQueue(String queueName) throws Exception;

    List<String> listAllSubscribersForTopic(String s) throws Exception;

    Set<Role> getSecurityConfig() throws Exception;

    void setSecurityConfig(Set<Role> defConfig) throws Exception;

    JMSServerManager getJMSServerManager() throws Exception;
}
