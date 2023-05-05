package org.radonlab.raterm.conf;

import dev.dirs.ProjectDirectories;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.configuration2.CompositeConfiguration;
import org.apache.commons.configuration2.ConfigurationMap;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.radonlab.raterm.pref.Preference;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class Configs {
    @NotNull
    public static String appId;

    /**
     * Application configuration
     */
    public static PropertiesConfiguration appConfig;

    /**
     * Preference configuration
     */
    public static Preference preference;

    public static void load() {
        try {
            loadAppConfig();
            loadPreference();
        } catch (ConfigurationException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadAppConfig() throws ConfigurationException {
        // Load app config
        appConfig = new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class)
                .configure(new Parameters()
                        .fileBased()
                        .setURL(Configs.class.getResource("/application.properties")))
                .getConfiguration();
        appId = appConfig.getString("app.id");
    }

    private static void loadPreference() throws ConfigurationException, InvocationTargetException, IllegalAccessException {
        // Load preference
        CompositeConfiguration mergedPreference = new CompositeConfiguration();
        TomlConfiguration defaultPreference = new FileBasedConfigurationBuilder<>(TomlConfiguration.class)
                .configure(new Parameters()
                        .fileBased()
                        .setURL(Configs.class.getResource("/config.default.toml")))
                .getConfiguration();
        File userConfig = getUserConfig();
        if (userConfig != null) {
            TomlConfiguration userPreference = new FileBasedConfigurationBuilder<>(TomlConfiguration.class)
                    .configure(new Parameters()
                            .fileBased()
                            .setFile(userConfig))
                    .getConfiguration();
            mergedPreference.addConfiguration(userPreference);
        }
        mergedPreference.addConfiguration(defaultPreference);
        Map<String, Object> preferenceMap = new ConfigurationMap(mergedPreference)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(entry -> entry.getKey().toString(), Map.Entry::getValue));
        BeanUtils.populate(preference = new Preference(), preferenceMap);
    }

    private static @Nullable File getUserConfig() {
        try {
            String[] appIds = appId.split("\\.");
            String configDir = ProjectDirectories.from(appIds[0], appIds[1], appIds[2]).configDir;
            Path confFile = Paths.get(configDir, "config.toml");
            if (!Files.exists(confFile)) {
                Files.createDirectories(confFile.getParent());
                Files.createFile(confFile);
            }
            return confFile.toFile();
        } catch (IOException e) {
            log.warn("Fails to init user config", e);
            return null;
        }
    }
}
