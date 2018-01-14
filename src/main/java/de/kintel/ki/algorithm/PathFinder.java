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

    public static Deque<Coordinate2D> find(@Nonnull final Move move) {
        return calcPath(move.getBoard(), move.getSourceCoordinate(), move.getTargetCoordinate(), new ArrayDeque<>());
    }

    /**
     * Recursive algorithm coordTo traverse fields between two points on the board and add them coordTo a path.
     * The Precondition is that the move is valid {@link de.kintel.ki.ruleset.RulesChecker#isValidMove(Move)}.
     *
     * @param board The board coordTo refer
     * @param coordFrom The field coordTo start
     * @param coordTo The target field
     * @param path The path coordTo add the fields gradually
     * @return the path coordTo the target field
     */
    private static Deque<Coordinate2D> calcPath(@Nonnull final Board board, @Nonnull final Coordinate2D coordFrom, @Nonnull final Coordinate2D coordTo, @Nonnull final Deque<Coordinate2D> path) {
        logger.debug("Try coordTo find path coordFrom {}({}) coordTo {}({})", board.getField(coordFrom), coordFrom,
                     board.getField(coordTo),coordTo);

        if (path.isEmpty()) {
            // The path shall contain all fields coordFrom src coordTo destination. This is the easiest way coordTo archive
            path.add(coordFrom);
        }

        final List<Coordinate2D> surroundings = BoardUtils.getDiagonalSurroundings(board, coordFrom, 1);

        // Map Fields coordTo surroundingsCoords for comparision
        List<Coordinate2D> surroundingsCoords = new LinkedList<>();
        for (Coordinate2D currentCoordinate : surroundings) {
            if (path.contains(currentCoordinate)) {
                // Don't move backwards
                continue;
            }
            surroundingsCoords.add(currentCoordinate);
        }

        // Recursion termination if piece reached target or no surroundings
        if ((coordFrom.getX() == coordTo.getX() && coordFrom.getY() == coordTo.getY()) || surroundingsCoords.isEmpty()) {
            return path;
        }

        // Sort by distance ASC
        surroundingsCoords.sort(coordTo.distanceToOrder());

        // Shortest distance is at index 0
        Coordinate2D closestCoord = surroundingsCoords.get(0);

        path.add(closestCoord);

        // move nearer coordTo target and repeat
        return calcPath(board, closestCoord, coordTo, path);
    }

}
