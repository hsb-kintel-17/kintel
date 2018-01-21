package de.kintel.ki.cli;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import org.apache.commons.lang3.StringUtils;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Maps.newLinkedHashMap;
import static java.lang.Math.max;

/**
 * Created by kintel on 21.01.2018.
 */
public class TablePrinter
{
    private final Logger logger = LoggerFactory.getLogger(TablePrinter.class);
    private final List<String> columns;
    private final Record headerRecord;
    private final String columnSeparator;

    public TablePrinter(String... columns)
    {
        this("  ", ImmutableList.copyOf(columns));
    }

    public TablePrinter(String columnSeparator, List<String> columns)
    {
        Preconditions.checkNotNull(columnSeparator, "columnSeparator is null");
        Preconditions.checkNotNull(columns, "headers is null");

        this.columnSeparator = columnSeparator;
        this.columns = ImmutableList.copyOf(columns);
        SimpleRecord.Builder builder = SimpleRecord.builder();
        for (String column : columns) {
            builder = builder.addValue(column, column, Color.CYAN);
        }
        this.headerRecord = builder.build();
    }

    public void print(List<Record> records)
    {
        final ImmutableList<Record> records1 = ImmutableList.copyOf(records);

        StringBuilder sb = new StringBuilder("\n");
        if (Ansi.isEnabled()) {
            Map<String, Integer> columns = newLinkedHashMap();

            for (Iterator<String> iterator = this.columns.iterator(); iterator.hasNext(); ) {
                String column = iterator.next();

                int columnSize = 0;
                if (iterator.hasNext()) {
                    columnSize = column.length();

                    for (Record record : records1) {
                        String value = record.getValue(column);
                        if (value != null) {
                            columnSize = Math.max(value.length() + 3, columnSize);
                        }
                    }
                }
                columns.put(column, columnSize);
            }

            for (final Record record : Iterables.concat(ImmutableList.of(headerRecord), records1)) {
                String line = Joiner.on(columnSeparator).join(transform(columns.entrySet(), columnFormatter(record)));
                sb.append(line.replaceAll("\\s*$", ""));
                sb.append("\n");
            }
        }
        else {
            for (Record record : records1) {
                boolean first = true;
                for (String column : columns) {
                    if (!first) {
                        sb.append("\t");
                    }
                    first = false;

                    String value = MoreObjects.firstNonNull(record.getValue(column), "");

                    sb.append(value);
                }
                sb.append("\n");
            }
        }
        logger.info(sb.toString());
    }

    private Function<Entry<String, Integer>, String> columnFormatter(final Record record)
    {
        return entry -> {
            String column = entry.getKey();
            int columnSize = entry.getValue();

            String value = MoreObjects.firstNonNull(record.getValue(column), "");
            String colorizedValue = MoreObjects.firstNonNull(record.getColorizedValue(column), "");

            return StringUtils.center(colorizedValue + spaces(max(0, columnSize - value.length())), max(0, columnSize - value.length()));
        };
    }

    private static String spaces(int count)
    {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < count; i++) {
            result.append(" ");
        }
        return result.toString();
    }
}
