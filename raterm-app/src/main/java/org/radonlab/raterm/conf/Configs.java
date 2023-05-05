package org.radonlab.raterm.conf;

import dev.dirs.ProjectDirectories;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.configuration2.CompositeConfiguration;
import org.apache.commons.configuration2.ConfigurationMap;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@Slf4j
public class Configs {
    /**
     * Preference configuration
     */
    public static Preference preference;

    public static void loadPreference(Properties properties) {
        try {
            // Load default preference
            CompositeConfiguration mergedPreference = new CompositeConfiguration();
            TomlConfiguration defaultPreference = new FileBasedConfigurationBuilder<>(TomlConfiguration.class)
                    .configure(new Parameters()
                            .fileBased()
                            .setURL(Configs.class.getResource("/config.default.toml")))
                    .getConfiguration();
            mergedPreference.addConfiguration(defaultPreference);
            // Load user preference
            String appId = properties.getProperty("app.id");
            File userConfig = getUserConfig(appId);
            if (userConfig != null) {
                TomlConfiguration userPreference = new FileBasedConfigurationBuilder<>(TomlConfiguration.class)
                        .configure(new Parameters()
                                .fileBased()
                                .setFile(userConfig))
                        .getConfiguration();
                mergedPreference.addConfigurationFirst(userPreference);
            }
            Map<String, Object> preferenceDataMap = new ConfigurationMap(mergedPreference)
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(entry -> entry.getKey().toString(), Map.Entry::getValue));
            BeanUtils.populate(preference = new Preference(), preferenceDataMap);
        } catch (ConfigurationException | InvocationTargetException | IllegalAccessException e) {
            log.warn("Fails to load preference", e);
        }
    }

    private static @Nullable File getUserConfig(String appId) {
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
