package org.radonlab.raterm.conf;

import org.apache.commons.configuration2.AbstractConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class Configs {
    public static final AbstractConfiguration application;

    static {
        try {
            application = new Configurations().properties(Configs.class.getResource("/application.properties"));
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}
