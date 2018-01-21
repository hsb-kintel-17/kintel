package de.kintel.ki.cli;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
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
    private final List<Column> columns;
    private final Record headerRecord;
    private final String columnSeparator;

    public TablePrinter(Column... columns)
    {
        this("  ", ImmutableList.copyOf(columns));
    }

    public TablePrinter(String columnSeparator, Iterable<Column> columns)
    {
        Preconditions.checkNotNull(columnSeparator, "columnSeparator is null");
        Preconditions.checkNotNull(columns, "headers is null");

        this.columnSeparator = columnSeparator;
        this.columns = ImmutableList.copyOf(columns);
        SimpleRecord.Builder builder = SimpleRecord.builder();
        for (Column column : columns) {
            builder = builder.addValue(column, column.getHeader(), Color.CYAN);
        }
        this.headerRecord = builder.build();
    }

    public void print(Iterable<Record> records)
    {
        StringBuilder sb = new StringBuilder();
        if (Ansi.isEnabled()) {
            Map<Column, Integer> columns = newLinkedHashMap();

            for (Iterator<Column> iterator = this.columns.iterator(); iterator.hasNext(); ) {
                Column column = iterator.next();

                int columnSize = 0;
                if (iterator.hasNext()) {
                    columnSize = column.getHeader().length();

                    for (Record record : records) {
                        String value = record.getValue(column);
                        if (value != null) {
                            columnSize = Math.max(value.length(), columnSize);
                        }
                    }
                }
                columns.put(column, columnSize);
            }

            for (final Record record : Iterables.concat(ImmutableList.of(headerRecord), records)) {
                String line = Joiner.on(columnSeparator).join(transform(columns.entrySet(), columnFormatter(record)));
                sb.append(line.replaceAll("\\s*$", ""));
                sb.append("\n");
            }
        }
        else {
            for (Record record : records) {
                boolean first = true;
                for (Column column : columns) {
                    if (!first) {
                        sb.append("\t");
                    }
                    first = false;

                    String value = Objects.firstNonNull(record.getValue(column), "");

                    sb.append(value);
                }
                sb.append("\n");
            }
        }
        logger.info(sb.toString());
    }

    private Function<Entry<Column, Integer>, String> columnFormatter(final Record record)
    {
        return entry -> {
            Column column = entry.getKey();
            int columnSize = entry.getValue();

            String value = Objects.firstNonNull(record.getValue(column), "");
            String colorizedValue = Objects.firstNonNull(record.getColorizedValue(column), "");

            return colorizedValue + spaces(max(0, columnSize - value.length()));
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
