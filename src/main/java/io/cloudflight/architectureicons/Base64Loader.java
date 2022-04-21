package io.cloudflight.architectureicons;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

class Base64Loader {

    private static final Map<String, Properties> propertiesMap = new HashMap<>();

    static String load(String identifier) {
        try {
            String[] ids = identifier.split("-");
            if (!propertiesMap.containsKey(ids[0])) {
                Properties properties = new Properties();
                properties.load(Icon.class.getResourceAsStream("/base64/" + ids[0] + ".properties"));
                propertiesMap.put(ids[0], properties);
            }
            return propertiesMap.get(ids[0]).getProperty(ids[1]);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
