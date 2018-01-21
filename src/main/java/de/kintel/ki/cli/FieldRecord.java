package de.kintel.ki.cli;

import de.kintel.ki.model.Field;
import de.kintel.ki.model.Piece;
import de.kintel.ki.model.Rank;
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
    public String getValue(Column column) {
        final StringBuilder sb = new StringBuilder();
        field.getPieces().stream().skip(1).forEach(sb::append);
        return sb.toString();
    }

    @Override
    public String getColorizedValue(Column column) {
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
            }
        }

        final Color colorFinal = color;
       
        field.getPieces().stream().limit(1).map(s ->  colorize(""+s.toString().charAt(0), colorFinal) ).forEach(sb::append);
        field.getPieces().stream().skip(1).forEach(sb::append);
        return sb.toString();
    }

    public static List<Record> toFieldRecords(List<Field> fields) {
        return fields.stream().map(FieldRecord::new).collect(Collectors.toList());
    }
}
