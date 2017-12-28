package de.kintel.ki;

import java.util.*;

public class Pathfinder {

    public static List<Field> find(Move move) {
        return calcPath(move.board, move.from, move.to, new ArrayList<>());
    }

    public static List<Field> calcPath(Board board, Field from, Field to, List<Field> path) {

        if(path.isEmpty()) {
            // The path shall contain all fields from src to destination. This is the easiest way to archive
            path.add(from);
        }

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
        // Map Fields to coords for comparision
        List<Coordinate2D> coords = new LinkedList<>();
        for (Field currentField : surroundings ) {
            if(path.contains(currentField)) {
                // Don't move backwards
                continue;
            }
            final Optional<Coordinate2D> coordinateOpt = board.getCoordinate(currentField);
            if( !coordinateOpt.isPresent() ) {
                throw new IllegalStateException("Could not find surrounding field on board!");
            }
            final Coordinate2D currentCoord2D = coordinateOpt.get();
            coords.add(currentCoord2D);
        }
        // Sort by distance
        coords.sort(coordTo.distanceToOrder());

        // Shortest distance is at index 0
        Field closestField = board.getField(coords.get(0)).orElseThrow(IllegalStateException::new);

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
