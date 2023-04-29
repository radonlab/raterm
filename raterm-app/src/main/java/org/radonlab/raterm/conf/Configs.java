package org.radonlab.raterm.conf;

import dev.dirs.ProjectDirectories;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.configuration2.CompositeConfiguration;
import org.apache.commons.configuration2.ConfigurationMap;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.jetbrains.annotations.NotNull;
import org.radonlab.raterm.pref.Preference;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Configs {
    @NotNull
    public static final String appId;

    /**
     * Application configuration
     */
    public static final PropertiesConfiguration application;

    /**
     * Preference configuration
     */
    public static final Preference preference;

    static {
        try {
            // Load app config
            application = new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class)
                    .configure(new Parameters()
                            .fileBased()
                            .setURL(Configs.class.getResource("/application.properties")))
                    .getConfiguration();
            appId = application.getString("app.id");
            // Load preference
            TomlConfiguration defaultPreference = new FileBasedConfigurationBuilder<>(TomlConfiguration.class)
                    .configure(new Parameters()
                            .fileBased()
                            .setURL(Configs.class.getResource("/config.default.toml")))
                    .getConfiguration();
            TomlConfiguration userPreference = new FileBasedConfigurationBuilder<>(TomlConfiguration.class)
                    .configure(new Parameters()
                            .fileBased()
                            .setFile(getUserConfig()))
                    .getConfiguration();
            CompositeConfiguration mergedPreference = new CompositeConfiguration();
            mergedPreference.addConfiguration(userPreference);
            mergedPreference.addConfiguration(defaultPreference);
            Map<String, Object> preferenceMap = new ConfigurationMap(mergedPreference)
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(entry -> entry.getKey().toString(), Map.Entry::getValue));
            BeanUtils.populate(preference = new Preference(), preferenceMap);
        } catch (ConfigurationException | IOException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static File getUserConfig() throws IOException {
        String[] appIds = appId.split("\\.");
        String configDir = ProjectDirectories.from(appIds[0], appIds[1], appIds[2]).configDir;
        Path confFile = Paths.get(configDir, "config.toml");
        if (!Files.exists(confFile)) {
            Files.createDirectories(confFile.getParent());
            Files.createFile(confFile);
        }
        return confFile.toFile();
    }
}
