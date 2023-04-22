package org.radonlab.raterm.conf;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.dataformat.toml.TomlMapper;
import org.apache.commons.configuration2.AbstractYAMLBasedConfiguration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.InputStreamSupport;

import java.io.*;
import java.util.Map;

public class TomlConfiguration extends AbstractYAMLBasedConfiguration implements FileBasedConfiguration, InputStreamSupport {

    private final ObjectMapper mapper = new TomlMapper();

    private final MapType type = mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class);

    @Override
    public void read(Reader reader) throws ConfigurationException, IOException {
        try {
            Map<String, Object> map = mapper.readValue(reader, type);
            load(map);
        } catch (StreamReadException | DatabindException e) {
            throw new ConfigurationException("Unable to load the configuration", e);
        }
    }

    @Override
    public void read(InputStream inputStream) throws ConfigurationException, IOException {
        read(new InputStreamReader(inputStream));
    }

    @Override
    public void write(Writer writer) throws ConfigurationException, IOException {
        try {
            Map<String, Object> map = constructMap(getNodeModel().getNodeHandler().getRootNode());
            mapper.writeValue(writer, map);
        } catch (StreamWriteException | DatabindException e) {
            throw new ConfigurationException("Unable to dump the configuration", e);
        }
    }
}
