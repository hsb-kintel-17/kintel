package de.kintel.ki;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Pathfinder {

    static Logger logger = LoggerFactory.getLogger(Pathfinder.class);

    public static List<Field> find(Move move) {
        return calcPath(move.board, move.from, move.to, new ArrayList<>());
    }

    public static List<Field> calcPath(Board board, Field from, Field to, List<Field> path) {
        logger.debug("Try to find path from {}({}) to {}({})", from, board.getCoordinate(from), board.getCoordinate(to));

        if(path.isEmpty()) {
            // The path shall contain all fields from src to destination. This is the easiest way to archive
            path.add(from);
        }

        final Coordinate2D coordFrom = board.getCoordinate(from);
        final Coordinate2D coordTo = board.getCoordinate(to);

        // Rekursions-Endbedingung
        if( coordFrom.getX() == coordTo.getX() && coordFrom.getY() == coordTo.getY() ) {
            return path;
        }

        final List<Field> surroundings = getDiagonalSurroundings(board, coordFrom.getX(), coordFrom.getX());
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
     * @param x
     * @param y
     * @return the surrounding fields
     */
    private static List<Field> getDiagonalSurroundings(Board board, int x, int y){
        int[][] directions = new int[][]{{-1,-1}, {-1,1}, {1,1},  {1,-1}};

        List<Field> res = new ArrayList<>();
        for (int[] direction : directions) {
            int cx = x + direction[0];
            int cy = y + direction[1];
            if(cy >=0 && cy < board.getHeight()) {
                if(cx >= 0 && cx < board.getWidth()) {
                    final Field field = board.getField(cy, cx);
                    if( !field.isForbidden()) {
                        res.add(field);
                    }
                }
            }
        }
        return res;
    }
}
