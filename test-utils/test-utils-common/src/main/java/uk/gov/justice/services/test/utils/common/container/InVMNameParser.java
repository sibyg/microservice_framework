package uk.gov.justice.services.test.utils.common.container;

import java.io.Serializable;
import java.util.Properties;

import javax.naming.CompoundName;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingException;

public class InVMNameParser implements NameParser, Serializable {

    private static final long serialVersionUID = 2925203703371001031L;

    static Properties syntax;

    static {
        InVMNameParser.syntax = new Properties();
        InVMNameParser.syntax.put("jndi.syntax.direction", "left_to_right");
        InVMNameParser.syntax.put("jndi.syntax.ignorecase", "false");
        InVMNameParser.syntax.put("jndi.syntax.separator", "/");
    }

    public static Properties getSyntax() {
        return InVMNameParser.syntax;
    }

    @Override
    public Name parse(final String name) throws NamingException {
        return new CompoundName(name, InVMNameParser.syntax);
    }
}
