package de.kintel.ki.model;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class BoardUtils {
    /**
     * Get surrounding fields on the diagonals.
     *
     * @param board     the board
     * @param coordFrom coord to find surroundings of
     * @param radius
     * @return the surrounding fields in diagonal
     */
    public static List<Coordinate2D> getDiagonalSurroundings(@Nonnull final Board board, @Nonnull final Coordinate2D coordFrom, int radius) {
        int[][] directions = new int[][]{{-1, -1}, {-1, 1}, {1, 1}, {1, -1}};
        int x = coordFrom.getX();
        int y = coordFrom.getY();
        List<Coordinate2D> surroundings = new ArrayList<>();
        for (int[] direction : directions) {
            for (int i = 1; i <= radius; i++) {

                int cx = x + i * direction[0];
                int cy = y + i * direction[1];
                if (cx >= 0 && cx < board.getHeight() &&
                    cy >= 0 && cy < board.getWidth()) {
                    final Field field = board.getField(cx, cy);
                    if (!field.isForbidden()) {
                        surroundings.add(new Coordinate2D(cx,cy));
                    }
                }
            }
        }
        return surroundings;
    }
}