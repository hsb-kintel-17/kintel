package de.kintel.ki.cli;

/**
 * Created by kintel on 21.01.2018.
 */

import com.google.common.collect.ImmutableMap;
import org.fusesource.jansi.Ansi.Color;

import java.util.Map;

import static com.google.common.collect.Maps.newLinkedHashMap;
import static de.kintel.ki.cli.Ansi.colorize;

public class SimpleRecord implements Record {
    public static Builder builder() {
        return new Builder();
    }

    private final Map<String, String> values;
    private final Map<String, String> colorizedValues;

    SimpleRecord(Map<String, String> values, Map<String, String> colorizedValues) {
        this.values = ImmutableMap.copyOf(values);
        this.colorizedValues = ImmutableMap.copyOf(colorizedValues);
    }

    @Override
    public String getValue(String column) {
        return values.get(column);
    }

    @Override
    public String getColorizedValue(String column) {
        return colorizedValues.get(column);
    }

    public static class Builder {
        private final Map<String, String> values = newLinkedHashMap();
        private final Map<String, String> colorizedValues = newLinkedHashMap();

        public Builder addValue(String name, Object value) {
            String stringValue = value == null ? "" : value.toString();
            values.put(name, stringValue);
            colorizedValues.put(name, stringValue);
            return this;
        }

        public Builder addValue(String name, Object value, Color color) {
            String stringValue = value == null ? "" : value.toString();
            values.put(name, stringValue);
            colorizedValues.put(name, colorize(stringValue, color));
            return this;
        }

        public SimpleRecord build() {
            return new SimpleRecord(values, colorizedValues);
        }
    }
}
