package de.kintel.ki;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Pathfinder {

    public static List<Field> find(Move move) {
        return calcPath(move.board, move.from, move.to, new ArrayList<>());
    }

    public static List<Field> calcPath(Board board, Field from, Field to, List<Field> path) {


        final Optional<Coordinate2D> coordFromOpt = board.getCoordinate(from);
        final Optional<Coordinate2D> coordToOpt = board.getCoordinate(to);
        if( !coordFromOpt.isPresent() || !coordToOpt.isPresent() ) {
            throw new IllegalStateException("Could not find fields on board.");
        }

        final Coordinate2D coordFrom = coordFromOpt.get();
        final Coordinate2D coordTo = coordToOpt.get();

        // Rekursions-Endbedingung
        if( coordFrom.getX() == coordTo.getX() && coordFrom.getY() == coordTo.getY() ) {
            return path;
        }

        final List<Field> surroundings = getDiagonalSurroundings(board, coordFrom.getX(), coordFrom.getX());
        int minX = -1;
        Field closestField = surroundings.get(0);

        for( Field currentField : surroundings ) {
            if( path.contains( currentField ) ) {
                // Do not move backwards
                continue;
            }

            final Optional<Coordinate2D> coordinateOpt = board.getCoordinate(currentField);
            if( !coordinateOpt.isPresent() ) {
                throw new IllegalStateException("Could not find surrounding field on board!");
            }

            final Coordinate2D currentCoord2D = coordinateOpt.get();
            int dx = Math.abs( currentCoord2D.getX() - coordTo.getX() );

            if( -1 == minX ) {
                minX = dx;
            } else if ( dx < minX) {
                minX = dx;
                closestField = currentField;
            } else {
                // The currentField is not closer than the current closest field.
            }
        }

        path.add(closestField);
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
                    final Optional<Field> field = board.getField(cy, cx);
                    if( field.isPresent() && !field.get().isForbidden()) {
                        res.add(field.get());
                    }
                }
            }
        }
        return res;
    }
}
