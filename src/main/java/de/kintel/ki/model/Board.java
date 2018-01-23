package de.kintel.ki.model;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Component
@Scope("prototype")
public class Board implements Serializable {

    private static final long serialVersionUID = -3717253852145631360L;

    private static final Logger logger = LoggerFactory.getLogger(Board.class);

    private int height;

    private int width;

    private Field[][] fields;

    public Board(final int height, final int width) {
        this(height, width, GridFactory.getLaskaInitGrid());
    }

    public Board(final int height, final int width, @Nonnull final Field[][] fields) {
        this.height = height;
        this.width = width;
        this.fields = fields;
    }

    public List<Coordinate2D> getCoordinatesOccupiedBy(@Nonnull final Player player) {
        final List<Coordinate2D> fieldsCollector = new ArrayList<>();

        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                final Field field = getField(x, y);
                if(field.getOwner().isPresent() && field.getOwner().get().equals( player ) ) {
                    fieldsCollector.add(new Coordinate2D(x,y));
                }
                // If the maximum of pieces in the list there can be no other occupied field
                if( fieldsCollector.size() >= 12) {
                    break;
                }
            }
        }
        return fieldsCollector;
    }

    public Field getField(int x, int y) {
        Preconditions.checkArgument( x < height, "Can't access x out of bounds." );
        Preconditions.checkArgument( y < width, "Can't access y out of bounds." );

        final Field field = fields[x][y];
        if (null == field) {
            String msg = String.format("No field at %d%d", x, y);
            logger.error(msg);
            throw new IllegalStateException(msg);
        }

        return field;
    }

    public Field getField(@Nonnull final Coordinate2D coordinate2D) {
        return getField(coordinate2D.getX(), coordinate2D.getY());
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("");
        sb.append("\n");
        String[] letters = {" ", "G", "F", "E", "D", "C", "B", "A"};
        String[] letters2 = {" ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        for (int i = 0 ; i <= fields.length ; i++){
            for (int j = 0 ; j <= fields[0].length ; j++) {
                if (i == 0) {
                    sb.append(letters2[j]);
                } else if (j == 0){
                    sb.append(letters[i]);
                } else {
                    int i_offset = i-1;
                    int j_offset = j-1;
                    if( fields[i_offset][j_offset] != null && width >= 1) {
                        final Field field = fields[i_offset][j_offset];
                        if (field != null) {
                            sb.append(field);
                        }
                    }
                }
                sb.append(" | ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    public Field[][] getFields() {
        return fields;
    }

    public void setFields(@Nonnull final Field[][] fields) {
        this.fields = fields;
    }

    private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
        stream.writeInt(height);
        stream.writeInt(width);
        stream.writeObject(fields);
    }

    private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
        height = stream.readInt();
        width = stream.readInt();
        fields = (Field[][]) stream.readObject();
    }

    public Board deepCopy() {
        return SerializationUtils.roundtrip(this);
    }
}
