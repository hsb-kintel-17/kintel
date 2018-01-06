package de.kintel.ki.algorithm;

import de.kintel.ki.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Finds path on grid.
 */
public class PathFinder {

    private static Logger logger = LoggerFactory.getLogger(PathFinder.class);

    public static Deque<Field> find(@Nonnull final Move move) {
        return calcPath(move.getBoard(), move.getSourceField(), move.getTargetField(), new ArrayDeque<>());
    }

    /**
     * Recursive algorithm to traverse fields between two points on the board and add them to a path.
     * The Precondition is that the move is valid {@link de.kintel.ki.ruleset.RulesChecker#isValidMove(Move)}.
     *
     * @param board The board to refer
     * @param from The field to start
     * @param to The target field
     * @param path The path to add the fields gradually
     * @return the path to the target field
     */
    private static Deque<Field> calcPath(@Nonnull final Board board, @Nonnull final Field from, @Nonnull final Field to, @Nonnull final Deque<Field> path) {
        logger.debug("Try to find path from {}({}) to {}({})", from, board.getCoordinate(from),
                     board.getCoordinate(to));

        if (path.isEmpty()) {
            // The path shall contain all fields from src to destination. This is the easiest way to archive
            path.add(from);
        }

        final Coordinate2D coordFrom = board.getCoordinate(from);
        final Coordinate2D coordTo = board.getCoordinate(to);

        final List<Field> surroundings = BoardUtils.getDiagonalSurroundings(board, coordFrom, 1);

        // Map Fields to surroundingsCoords for comparision
        List<Coordinate2D> surroundingsCoords = new LinkedList<>();
        for (Field currentField : surroundings) {
            if (path.contains(currentField)) {
                // Don't move backwards
                continue;
            }
            final Coordinate2D currentCoord2D = board.getCoordinate(currentField);
            surroundingsCoords.add(currentCoord2D);
        }

        // Recursion termination if piece reached target or no surroundings
        if ((coordFrom.getX() == coordTo.getX() && coordFrom.getY() == coordTo.getY()) || surroundingsCoords.isEmpty()) {
            return path;
        }

        // Sort by distance ASC
        surroundingsCoords.sort(coordTo.distanceToOrder());

        // Shortest distance is at index 0
        Field closestField = board.getField(surroundingsCoords.get(0));

        path.add(closestField);

        // move nearer to target and repeat
        return calcPath(board, closestField, to, path);
    }

}
