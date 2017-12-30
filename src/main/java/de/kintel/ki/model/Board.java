package de.kintel.ki.model;

import com.google.common.base.Preconditions;
import de.kintel.ki.algorithm.PathClassifier;
import de.kintel.ki.algorithm.PathFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Board {

    Logger logger = LoggerFactory.getLogger(Board.class);

    private int height;
    private int width;

    private Field[][] fields;

    public Board(int height, int width) {
        this(height, width, GridFactory.getLaskaInitGrid());
    }

    public Board(int height, int width, Field[][] fields) {
        this.height = height;
        this.width = width;
        this.fields = fields;
    }

    public List<Field> getFieldsOccupiedBy(Player player) {
        final List<Field> fieldsCollector = new ArrayList<>();

        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                final Field field = getField(x, y);
                if(field.getOwner().isPresent() && field.getOwner().get().equals( player ) ) {
                    fieldsCollector.add(field);
                }
            }
        }
        return fieldsCollector;
    }

    public Coordinate2D getCoordinate(Field searchField) {
        for (int i = 0 ; i < height; i++){
            for (int j = 0 ; j < width; j++){
                Field current = fields[i][j];
                if( current.equals(searchField) ) {
                    return new Coordinate2D(i,j);
                }
            }
        }
        throw new IllegalArgumentException(String.format("Searched field %s is not on board.", searchField));
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

    public Field getField(Coordinate2D coordinate2D) {
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

        String[] letters = {" ", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        String[] letters2 = {" ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        for (int i = 0 ; i <= fields.length ; i++){
            for (int j = 0 ; j <= fields[0].length ; j++){
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
                sb.append(" \t");
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());
        return sb.toString();
    }

    public Field[][] getFields() {
        return fields;
    }

    public void move(Move move) {
        final Coordinate2D coordFrom = getCoordinate(move.from);
        final Coordinate2D coordTo = getCoordinate(move.to);
        final Field fieldFrom = getField(coordFrom);
        final Field fieldTo = getField(coordTo);

        logger.debug("Making move from {}({}) to {}({}) for player {}", move.from, coordFrom, move.to, coordTo, move.currentPlayer);

        ArrayDeque<Field> path = PathFinder.find(move);
        final PathClassifier.MoveType classify = PathClassifier.classify(path);

        if( classify.equals(PathClassifier.MoveType.MOVE)) {
            fieldTo.getSteine().addAll(getField(coordFrom).getSteine());
            fieldFrom.getSteine().clear();
        } else if (classify.equals(PathClassifier.MoveType.CAPTURE)) {
            final Optional<Field> opponentOpt = path.stream()
                                                    // find first field of opposite player
                                                    .filter(field -> field.getOwner()
                                                                          .isPresent() && !field.getOwner()
                                                                                                .get()
                                                                                                .equals(move.currentPlayer))
                                                    .findFirst();
            if( !opponentOpt.isPresent() ) {
                throw new IllegalStateException("No opponent field in path.");
            }

            final Field fieldOpponent = opponentOpt.get();
            fieldTo.getSteine().add( fieldOpponent.getSteine().getFirst() );
            fieldTo.getSteine().addAll(fieldFrom.getSteine());
            fieldFrom.getSteine().clear();
        }
    }
}
