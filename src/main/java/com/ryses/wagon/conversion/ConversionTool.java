package com.ryses.wagon.conversion;

import com.ryses.wagon.conversion.domain.I18nFileComponent;

import java.io.File;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ConversionTool {

    public static Map<Object, Object> flatMap(Map<Object, Object> map, String prefix) {
        Map<Object, Object> result = new HashMap<>();

        map.forEach((key, value) -> {
            String newKey = prefix.isEmpty() ? key.toString() : prefix + "." + key;

            if (value instanceof Map) {
                result.putAll(flatMap((Map<Object, Object>) value, newKey));
            } else {
                result.put(newKey, value);
            }
        });

        return result;
    }

    public static I18nFileComponent getComponentParts(File file) {
        var pattern = Pattern.compile("^(?<domain>[a-zA-Z0-9\\-_]+)(?<subtype>[+a-zA-Z-]+)?\\.(?<locale>[a-z]{2})\\.(?<extension>ya?ml)$");
        var matcher = pattern.matcher(file.getName());

        if (!matcher.find()) {
            throw new RuntimeException(MessageFormat.format("File {0} is not a source file", file.getName()));
        }

        return new I18nFileComponent(
                file.getName(),
                matcher.group("domain"),
                matcher.group("locale"),
                matcher.group("subtype"),
                matcher.group("extension")
        );
    }
}
