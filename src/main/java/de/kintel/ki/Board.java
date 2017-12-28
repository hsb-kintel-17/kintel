package de.kintel.ki;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Board {

    Logger logger = LoggerFactory.getLogger(Board.class);

    private int height;
    private int width;

    private Field[][] fields;

    public Board(int height, int width) {
        this.height = height;
        this.width = width;

        fields = new Field[height][width];

        // Initialize whole board with forbidden fields
        for (int i = 0 ; i < height ; i++){
            for (int j = 0 ; j < width ; j++){
                fields[i][j] = new Field(true);
            }
        }

        // x,y
        fields[0][0] = new Field(false);
        fields[0][2] = new Field(false);
        fields[0][4] = new Field(false);
        fields[0][6] = new Field(false);
        fields[0][8] = new Field(false);

        fields[1][1] = new Field(false);
        fields[1][3] = new Field(false);
        fields[1][5] = new Field(false);
        fields[1][7] = new Field(false);

        fields[2][2] = new Field(false);
        fields[2][4] = new Field(false);
        fields[2][6] = new Field(false);

        fields[3][1] = new Field(false);
        fields[3][3] = new Field(false);
        fields[3][5] = new Field(false);
        fields[3][7] = new Field(false);

        fields[4][2] = new Field(false);
        fields[4][4] = new Field(false);
        fields[4][6] = new Field(false);

        fields[5][1] = new Field(false);
        fields[5][3] = new Field(false);
        fields[5][5] = new Field(false);
        fields[5][7] = new Field(false);

        fields[6][0] = new Field(false);
        fields[6][2] = new Field(false);
        fields[6][4] = new Field(false);
        fields[6][6] = new Field(false);
        fields[6][8] = new Field(false);

        fields[0][0].addStein(new Stein(Player.WEISS));
        fields[0][2].addStein(new Stein(Player.WEISS));
        fields[0][4].addStein(new Stein(Player.WEISS));
        fields[0][6].addStein(new Stein(Player.WEISS));
        fields[0][8].addStein(new Stein(Player.WEISS));

        fields[1][1].addStein(new Stein(Player.WEISS));
        fields[1][3].addStein(new Stein(Player.WEISS));
        fields[1][5].addStein(new Stein(Player.WEISS));
        fields[1][7].addStein(new Stein(Player.WEISS));

        fields[2][2].addStein(new Stein(Player.WEISS));
        fields[2][4].addStein(new Stein(Player.WEISS));
        fields[2][6].addStein(new Stein(Player.WEISS));

        fields[6][0].addStein(new Stein(Player.SCHWARZ));
        fields[6][2].addStein(new Stein(Player.SCHWARZ));
        fields[6][4].addStein(new Stein(Player.SCHWARZ));
        fields[6][6].addStein(new Stein(Player.SCHWARZ));
        fields[6][8].addStein(new Stein(Player.SCHWARZ));

        fields[5][1].addStein(new Stein(Player.SCHWARZ));
        fields[5][3].addStein(new Stein(Player.SCHWARZ));
        fields[5][5].addStein(new Stein(Player.SCHWARZ));
        fields[5][7].addStein(new Stein(Player.SCHWARZ));

        fields[4][2].addStein(new Stein(Player.SCHWARZ));
        fields[4][4].addStein(new Stein(Player.SCHWARZ));
        fields[4][6].addStein(new Stein(Player.SCHWARZ));
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
        for (int i = 0 ; i < height ; i++){
            for (int j = 0 ; j < width ; j++){
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
        /*for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[0].length; j++) {
                if( j == 0) {
                    sb.append((char)((char) i  + 65) + "\t");
                }
                if(i == 0) {
                    sb.append(j + "\t");
                }
                sb.append(fields[i][j] + "\t");
            }
            sb.append("\n");
        }*/

        String[] letters = {" ", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        String[] letters2 = {" ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        for (int i = 0 ; i <= fields.length ; i++){
            for (int j = 0 ; j <= fields[0].length ; j++){
                if (i == 0) {
                    System.out.print(letters2[j]);
                } else if (j == 0){
                    System.out.print(letters[i]);
                } else {
                    int i_offset = i-1;
                    int j_offset = j-1;
                    if( fields[i_offset][j_offset] != null && width >= 1) {
                        final Field field = fields[i_offset][j_offset];
                        if (field != null) {
                            System.out.print(field);
                        }
                    }
                }
                System.out.print(" \t");
            }
            System.out.println();
        }

        return sb.toString();
    }

    public Field[][] getFields() {
        return fields;
    }

    public void move(Move move) {
        logger.debug("Making move from {}({}) to {}({})", move.from, getCoordinate(move.from), getCoordinate(move.to));
        logger.debug("Making move: {}", move);
        List<Field> path = Pathfinder.find(move);
    }
}
