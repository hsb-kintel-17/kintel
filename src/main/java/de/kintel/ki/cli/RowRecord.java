package de.kintel.ki.cli;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import de.kintel.ki.model.Board;
import de.kintel.ki.model.Field;
import de.kintel.ki.model.Piece;
import de.kintel.ki.model.Rank;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.fusesource.jansi.Ansi.Color;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static de.kintel.ki.cli.Ansi.colorize;

/**
 * Created by kintel on 21.01.2018.
 */
public class RowRecord implements Record {

    private static int intRowCount = 0;
    private int rowCount = 0;
    private final List<Field> fields;

    public RowRecord(List<Field> fields) {
        Preconditions.checkArgument(fields.size() == 9, "A row must consist of 9 elements but only %s provided", fields.size());
        this.rowCount = intRowCount;
        intRowCount++;
        this.fields = fields;
    }

    @Override
    public String getValue(String column) {
        if(NumberUtils.toInt(column) == 0) {
            return columnToLabel(column);
        }
        int index = column.charAt(0) - 49;
        final String toString = fields.get(index).toString();
        return toString;
    }

    private String columnToLabel(String column) {
        char c = (char) ('G' - rowCount);
        return String.valueOf(c);
    }

    private String columnToLabelColorized(String column) {
        char c = (char) ('G' - rowCount);
        return colorize(String.valueOf(c), Color.CYAN);
    }

    @Override
    public String getColorizedValue(String column) {
        if(NumberUtils.toInt(column) == 0) {
            return columnToLabelColorized(column);
        }
        final StringBuilder sb = new StringBuilder();
        int index = column.charAt(0) - 49;
        Color color = Color.CYAN;
        final Optional<Piece> headOpt = fields.get(index).peekHead();
        if( headOpt.isPresent() ) {
            Rank rank = headOpt.get().getRank();
            switch (rank) {
                case gelb:
                    color = Color.YELLOW;
                    break;
                case gruen:
                    color = Color.GREEN;
                    break;
                case rot:
                    color = Color.RED;
                    break;
                case magenta:
                    color = Color.MAGENTA;
                    break;
                default:
                    color = Color.DEFAULT;
            }
        }

        String value = getValue(column);
        final int posFirstPiece = StringUtils.indexOfAny(value, 'S', 'W');
        final String content;
        if( posFirstPiece >= 0 ) {
            value = StringUtils.replaceOnce(value, "" + value.charAt(posFirstPiece), colorize("" + value.charAt(posFirstPiece), color));
        }
        sb.append(value);
        return sb.toString();
    }

    public static List<Record> toFieldRecords(Board board) {
        List<Record> collector = Lists.newArrayList();
        for (int i = 0; i < board.getFields().length; i++) {
            List<Field> row = Arrays.asList(board.getFields()[i]);
            collector.add(new RowRecord(row));
        }

        return collector;
    }
}
