package de.kintel.ki.model;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * Returns all coordinates that are occupied by the specified player.
     * @param player the player to search for
     * @return all coordinates that are occupied by the specified player
     */
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

    /**
     * Retrieves field for the given coordinate like (0,0).
     * @param x
     * @param y
     * @return the field for the given coordinate
     */
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

    /**
     * Retrieves field for the given coordinate like (0,0).
     * @param coordinate2D the coordinate to retrieve
     * @return the field for the given coordinate.
     */
    public Field getField(@Nonnull final Coordinate2D coordinate2D) {
        return getField(coordinate2D.getX(), coordinate2D.getY());
    }

    /**
     * Serializes object to stream.
     * @param stream the stream to write to
     * @throws IOException if I/O errors occur while writing to the underlying
     *          stream
     */
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.writeInt(height);
        stream.writeInt(width);
        stream.writeObject(fields);
    }

    /**
     * Deserialize object from stream.
     * @param stream the stream to read from
     * @throws IOException if I/O errors occur while writing to the underlying
     *          stream
     * @throws ClassNotFoundException Class of a serialized object cannot be
     *          found.
     */
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        height = stream.readInt();
        width = stream.readInt();
        fields = (Field[][]) stream.readObject();
    }

    /**
     * Creates a deep copy of the board.
     * @return deep copy of the board
     */
    public Board deepCopy() {
        return SerializationUtils.roundtrip(this);
    }

    public Field[][] getFields() {
        return fields;
    }

    public void setFields(@Nonnull final Field[][] fields) {
        this.fields = fields;
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

    /**
     * Generates a String that is unique for the current state (fields, pieces, colors and their ranks) on the board.
     * Sequential calls on the same state produce the same string
     * @return a String that is unique for the current state (fields, pieces, colors and their ranks)
     */
    public String toStringWithRanks() {
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
                            sb.append(field.toStringWithRanks());
                        }
                    }
                }
                sb.append(" | ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
