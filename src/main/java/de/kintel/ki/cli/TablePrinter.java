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
import static de.kintel.ki.cli.Ansi.colorize;
import static java.lang.Math.max;

/**
 * Created by kintel on 21.01.2018.
 * This class is used to print the board on the CLI/Terminal.
 */
public class TablePrinter
{
    public static int currentRow;
    private final Logger logger = LoggerFactory.getLogger(TablePrinter.class);
    private final List<String> columns;
    private final Record headerRecord;
    private final String columnSeparator;

    /**
     * Convenient constructor for a Tableprinter. uses a whitespace to separate the columns.
     * @param columns The names of each column, separated by a whitespace
     */
    public TablePrinter(String... columns)
    {
        this("  ", ImmutableList.copyOf(columns));
    }

    /**
     * Constructor
     * @param columnSeparator The character, that will seperate the colums of th table
     * @param columns The names of each column, separated by columnSeparator
     */
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

    /**
     * Print a list of records  (which form a table, obviously)
     * @param records The records of the table.
     */
    public void print(List<Record> records)
    {
        StringBuilder sb = new StringBuilder("\n");
        if (Ansi.isEnabled()) {
            Map<String, Integer> columns = newLinkedHashMap();

            for (Iterator<String> iterator = this.columns.iterator(); iterator.hasNext(); ) {
                String column = iterator.next();

                int columnSize = 0;
                if (iterator.hasNext()) {
                    columnSize = column.length();

                    for (Record record : records) {
                        String value = record.getValue(column);
                        if (value != null) {
                            columnSize = Math.max(value.length() + 3, columnSize);
                        }
                    }
                }
                columns.put(column, columnSize);
            }

            for (final Record record : Iterables.concat(ImmutableList.of(headerRecord), records)) {
                String line = "";
                if( record == headerRecord ) {
                    line = String.format("Farben als Referenz: W: %s %s S: %s %s %n", colorize("Gruen", Color.GREEN), colorize("Gelb", Color.YELLOW), colorize("Rot", Color.RED), colorize("Magenta", Color.MAGENTA));
                }
                line += Joiner.on(columnSeparator).join(transform(columns.entrySet(), columnFormatter(record)));
                sb.append(line.replaceAll("\\s*$", ""));
                sb.append("\n");
                if( record != headerRecord ) {
                    currentRow = (currentRow + 1) % (records.size());
                }
            }
        }
        else {
            for (Record record : records) {
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

    /**
     * Format a column to maintain an even width of each cell in the table.
     * @param record
     * @return
     */
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

    /**
     * Method to generate spaces.
     * @param count the number of whitespaces to generate.
     * @return
     */
    private static String spaces(int count)
    {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < count; i++) {
            result.append(" ");
        }
        return result.toString();
    }
}
