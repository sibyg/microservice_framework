package uk.gov.justice.services.test.utils.common.container;


import java.util.Hashtable;

import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import javax.naming.spi.InitialContextFactoryBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class InVMInitialContextFactoryBuilder implements InitialContextFactoryBuilder {

    private static final Logger LOG =
                    LoggerFactory.getLogger(InVMInitialContextFactoryBuilder.class);


    public InVMInitialContextFactoryBuilder() {}


    @Override
    public InitialContextFactory createInitialContextFactory(final Hashtable<?, ?> environment)
                    throws NamingException {

        InitialContextFactory icf = null;

        if (environment != null) {
            String icfName = (String) environment.get("java.naming.factory.initial");

            if (icfName != null) {
                Class c = null;
                try {
                    c = Class.forName(icfName);
                } catch (ClassNotFoundException e) {
                    LOG.error("\"" + icfName + "\" cannot be loaded", e);
                    throw new NamingException("\"" + icfName + "\" cannot be loaded");
                }

                try {
                    icf = (InitialContextFactory) c.newInstance();
                } catch (InstantiationException e) {
                    LOG.error(c.getName() + " cannot be instantiated", e);
                    throw new NamingException(c.getName() + " cannot be instantiated");
                } catch (IllegalAccessException e) {
                    LOG.error(c.getName() + " instantiation generated an IllegalAccessException",
                                    e);
                    throw new NamingException(c.getName()
                                    + " instantiation generated an IllegalAccessException");
                }
            }
        }

        if (icf == null) {
            icf = new InVMInitialContextFactory();
        }

        return icf;
    }

}
