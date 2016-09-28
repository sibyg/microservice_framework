package uk.gov.justice.services.test.utils.common.container;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;

/**
 * used by the default context when running in embedded local configuration
 */
public final class NonSerializableFactory implements ObjectFactory {

    public NonSerializableFactory() {}

    public static Object lookup(final String name) throws NamingException {
        if (NonSerializableFactory.getWrapperMap().get(name) == null) {
            throw new NamingException(name + " not found");
        }
        return NonSerializableFactory.getWrapperMap().get(name);
    }

    @Override
    public Object getObjectInstance(final Object obj, final Name name, final Context nameCtx,
                    final Hashtable<?, ?> env) throws Exception {
        Reference ref = (Reference) obj;
        RefAddr addr = ref.get("nns");
        String key = (String) addr.getContent();
        return NonSerializableFactory.getWrapperMap().get(key);
    }

    public static Map getWrapperMap() {
        return NonSerializableFactory.wrapperMap;
    }

    private static Map wrapperMap = Collections.synchronizedMap(new HashMap());
}
