package de.kintel.ki.algorithm;

import de.kintel.ki.model.Coordinate2D;
import de.kintel.ki.model.Move;
import de.kintel.ki.model.Board;
import de.kintel.ki.model.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Finds path on grid.
 */
public class PathFinder {

    static Logger logger = LoggerFactory.getLogger(PathFinder.class);

    public static ArrayDeque<Field> find(Move move) {
        return calcPath(move.getBoard(), move.getSourceField(), move.getTargetField(), new ArrayDeque<>());
    }

    public static ArrayDeque<Field> calcPath(Board board, Field from, Field to, ArrayDeque<Field> path) {
        logger.debug("Try to find path from {}({}) to {}({})", from, board.getCoordinate(from), board.getCoordinate(to));

        if(path.isEmpty()) {
            // The path shall contain all fields from src to destination. This is the easiest way to archive
            path.add(from);
        }

        final Coordinate2D coordFrom = board.getCoordinate(from);
        final Coordinate2D coordTo = board.getCoordinate(to);

        final List<Field> surroundings = getDiagonalSurroundings(board, coordFrom);

        // Map Fields to coords for comparision
        List<Coordinate2D> coords = new LinkedList<>();
        for (Field currentField : surroundings ) {
            if(path.contains(currentField)) {
                // Don't move backwards
                continue;
            }
            final Coordinate2D currentCoord2D = board.getCoordinate(currentField);
            coords.add(currentCoord2D);
        }

        // Rekursions-Endbedingung
        if( ( coordFrom.getX() == coordTo.getX() && coordFrom.getY() == coordTo.getY() ) || coords.isEmpty() ) {
            return path;
        }

        // Sort by distance
        coords.sort(coordTo.distanceToOrder());

        // Shortest distance is at index 0
        Field closestField = board.getField(coords.get(0));

        path.add(closestField);

        // move nearer to target and repeat
        return calcPath(board, closestField, to, path);
    }


    /**
     * Get surrounding fields on the diagonals.
     * @param board
     * @param coordFrom coord to find surroundings of
     * @return the surrounding fields in diagonal
     */
    private static List<Field> getDiagonalSurroundings(Board board, Coordinate2D coordFrom){
        int[][] directions = new int[][]{{-1,-1}, {-1,1}, {1,1},  {1,-1}};
        int x = coordFrom.getX();
        int y = coordFrom.getY();
        List<Field> res = new ArrayList<>();
        for (int[] direction : directions) {
            int cx = x + direction[0];
            int cy = y + direction[1];
            if(cx >=0 && cx < board.getHeight()) {
                if(cy >= 0 && cy < board.getWidth()) {
                    final Field field = board.getField(cx, cy);
                    if( !field.isForbidden()) {
                        res.add(field);
                    }
                }
            }
        }
        return res;
    }
}
