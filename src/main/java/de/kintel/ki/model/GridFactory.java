package de.kintel.ki.model;

public class GridFactory {

    /**
     * Generates and returns a grid with initial setting of the laska grid.
     * @return a grid with initial setting of the laska grid
     */
    public static Field[][] getLaskaInitGrid() {
        Field[][] fields = new Field[7][9];

        // Initialize whole board with forbidden fields
        for (int i = 0 ; i < 7 ; i++){
            for (int j = 0 ; j < 9 ; j++){
                fields[i][j] = new Field(true);
            }
        }

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

        fields[0][0].addStein(new Piece(Player.SCHWARZ));
        fields[0][2].addStein(new Piece(Player.SCHWARZ));
        fields[0][4].addStein(new Piece(Player.SCHWARZ));
        fields[0][6].addStein(new Piece(Player.SCHWARZ));
        fields[0][8].addStein(new Piece(Player.SCHWARZ));

        fields[1][1].addStein(new Piece(Player.SCHWARZ));
        fields[1][3].addStein(new Piece(Player.SCHWARZ));
        fields[1][5].addStein(new Piece(Player.SCHWARZ));
        fields[1][7].addStein(new Piece(Player.SCHWARZ));

        fields[2][2].addStein(new Piece(Player.SCHWARZ));
        fields[2][4].addStein(new Piece(Player.SCHWARZ));
        fields[2][6].addStein(new Piece(Player.SCHWARZ));

        fields[6][0].addStein(new Piece(Player.WEISS));
        fields[6][2].addStein(new Piece(Player.WEISS));
        fields[6][4].addStein(new Piece(Player.WEISS));
        fields[6][6].addStein(new Piece(Player.WEISS));
        fields[6][8].addStein(new Piece(Player.WEISS));

        fields[5][1].addStein(new Piece(Player.WEISS));
        fields[5][3].addStein(new Piece(Player.WEISS));
        fields[5][5].addStein(new Piece(Player.WEISS));
        fields[5][7].addStein(new Piece(Player.WEISS));

        fields[4][2].addStein(new Piece(Player.WEISS));
        fields[4][4].addStein(new Piece(Player.WEISS));
        fields[4][6].addStein(new Piece(Player.WEISS));

        return fields;
    }

    /**
     * Returns a rect grid with initialized fields.
     * @param height height
     * @param width width
     * @return the rect grid with initialized fields.
     */
    public static Field[][] getRectGrid(int height, int width) {
        Field[][] fields = new Field[height][width];
        // Initialize whole board with forbidden fields
        for (int i = 0 ; i < height ; i++){
            for (int j = 0 ; j < width ; j++){
                fields[i][j] = new Field(false);
            }
        }
        return fields;
    }
}
