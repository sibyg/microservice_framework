package uk.gov.justice.services.test.utils.common.container;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

/**
 * An in-VM JNDI InitialContextFactory. <br/>
 * Lightweight JNDI implementation used for testing.
 */
public class InVMInitialContextFactory implements InitialContextFactory {

    private static Map<Integer, Context> initialContexts;

    static {
        InVMInitialContextFactory.reset();
    }

    public static Hashtable<String, String> getJNDIEnvironment() {
        return InVMInitialContextFactory.getJNDIEnvironment(0);
    }

    /**
     * @return the JNDI environment to use to get this InitialContextFactory.
     */
    public static Hashtable<String, String> getJNDIEnvironment(final int serverIndex) {
        Hashtable<String, String> env = new Hashtable<>();
        env.put("java.naming.factory.initial",
                        "org.apache.activemq.artemis.jms.tests.tools.container.InVMInitialContextFactory");
        env.put(Constants.SERVER_INDEX_PROPERTY_NAME, Integer.toString(serverIndex));
        return env;
    }


    @Override
    public Context getInitialContext(final Hashtable<?, ?> environment) throws NamingException {
        // try first in the environment passed as argument ...
        String s = (String) environment.get(Constants.SERVER_INDEX_PROPERTY_NAME);

        if (s == null) {
            // ... then in the global environment
            s = System.getProperty(Constants.SERVER_INDEX_PROPERTY_NAME);

            if (s == null) {
                // try the thread name
                String tName = Thread.currentThread().getName();
                if (tName.contains("server")) {
                    s = tName.substring(6);
                }
                if (s == null) {
                    throw new NamingException("Cannot figure out server index!");
                }
            }
        }

        int serverIndex;

        try {
            serverIndex = Integer.parseInt(s);
        } catch (Exception e) {
            throw new NamingException("Failure parsing \"" + Constants.SERVER_INDEX_PROPERTY_NAME
                            + "\". " + s + " is not an integer");
        }

        // Note! This MUST be synchronized
        synchronized (InVMInitialContextFactory.initialContexts) {

            InVMContext ic = (InVMContext) InVMInitialContextFactory.initialContexts
                            .get(new Integer(serverIndex));

            if (ic == null) {
                ic = new InVMContext(s);
                ic.bind("java:/", new InVMContext(s));
                InVMInitialContextFactory.initialContexts.put(new Integer(serverIndex), ic);
            }

            return ic;
        }
    }

    public static void reset() {
        InVMInitialContextFactory.initialContexts = new HashMap<>();
    }
}
