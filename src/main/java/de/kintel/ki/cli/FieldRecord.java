package de.kintel.ki.cli;

import de.kintel.ki.model.Field;
import de.kintel.ki.model.Piece;
import de.kintel.ki.model.Rank;
import org.apache.commons.lang3.StringUtils;
import org.fusesource.jansi.Ansi.Color;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static de.kintel.ki.cli.Ansi.colorize;

/**
 * Created by kintel on 21.01.2018.
 */
public class FieldRecord implements Record {

    private final Field field;

    public FieldRecord(Field field) {
        this.field = field;
    }

    @Override
    public String getValue(String column) {
        final StringBuilder sb = new StringBuilder();
        field.getPieces().forEach(sb::append);
        return sb.toString();
    }

    @Override
    public String getColorizedValue(String column) {
        final StringBuilder sb = new StringBuilder();
        Color color = Color.CYAN;
        final Optional<Piece> headOpt = field.peekHead();
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

        final Color colorFinal = color;
        final String value = getValue(column);
        final int posFirstPiece = StringUtils.indexOfAny(value, 'S', 'W');
        sb.append(StringUtils.replaceOnce(value, "" + value.charAt(posFirstPiece), colorize("" + value.charAt(posFirstPiece), colorFinal)));
        return sb.toString();
    }

    public static List<Record> toFieldRecords(List<Field> fields) {
        return fields.stream().map(FieldRecord::new).collect(Collectors.toList());
    }
}
