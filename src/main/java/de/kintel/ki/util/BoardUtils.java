package de.kintel.ki.util;

import de.kintel.ki.algorithm.MoveClassifier;
import de.kintel.ki.model.*;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BoardUtils {

    private static Logger logger = LoggerFactory.getLogger(BoardUtils.class);

    /**
     * Get surrounding fields on the diagonals.
     *
     * @param board     the board
     * @param coordFrom coord to find surroundings of
     * @param radius
     * @return the surrounding fields in diagonal
     */
    public static List<Coordinate2D> getDiagonalSurroundings(@NonNull final Board board, @NonNull final Coordinate2D coordFrom, int radius) {
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

    public static List<Move> getPossibleMoves(Board board, Player currentPlayer, MoveClassifier moveClassifier) {
        final List<Move> possibleMoves;
        // Moves the user must do
        final List<Move> zugzwaenge = new ArrayList<>();
        // Ordinary Moves
        final List<Move> moves = new ArrayList<>();
        final List<Coordinate2D> coordinatesOccupiedBy = board.getCoordinatesOccupiedBy(currentPlayer);

        for( Coordinate2D coordinateSource : coordinatesOccupiedBy ) {
            final List<Coordinate2D> diagonalSurroundings = BoardUtils.getDiagonalSurroundings(board, coordinateSource, 2);

            for( Coordinate2D coordinateTarget : diagonalSurroundings ) {
                Move move = new UMLMove(coordinateSource, coordinateTarget, currentPlayer);
                moveClassifier.classify(move, board);
                if(move.getForwardClassification() != MoveClassifier.MoveType.INVALID) {
                    if( move.getForwardClassification() == MoveClassifier.MoveType.CAPTURE ) {
                        move.setForwardOpponentRank( Optional.of(board.getField(Coordinate2D.between(coordinateSource, coordinateTarget)).getPieces().getFirst().getRank()));
                    } else {
                        move.setForwardOpponentRank(Optional.empty());
                    }

                    move.setForwardSourceRank(board.getField(coordinateSource).getPieces().getFirst().getRank());

                    if ( move.getForwardClassification() == MoveClassifier.MoveType.CAPTURE) {
                        zugzwaenge.add( move );
                    } else {
                        moves.add( move );
                    }
                }
            }
        }

        // If there are zugzwang moves then these moves are the only possible moves
        possibleMoves = zugzwaenge.isEmpty() ? moves : zugzwaenge;

        logger.info("{} possible moves for player {}", possibleMoves.size(), currentPlayer);
        logger.info("possible moves: {}", possibleMoves);

        return possibleMoves;
    }
}