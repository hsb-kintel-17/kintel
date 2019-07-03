/*
 * hsb-kintel-17
 * Copyright (C) 2018 hsb-kintel-17
 * This file is covered by the LICENSE file in the root of this project.
 */

package de.kintel.ki.algorithm;

import de.kintel.ki.model.*;
import de.kintel.ki.util.BoardUtils;
import org.eclipse.jdt.annotation.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Component
@Qualifier("weightingHeightAndRank")
public class WeightingHeightAndRank implements Weighting {

    private MoveClassifier moveClassifier;

    @Autowired
    public WeightingHeightAndRank(MoveClassifier moveClassifier) {
        this.moveClassifier = moveClassifier;
    }

    /**
     * Evaluate the state of the game <strong>for the current player</strong> after a move.
     * The greatest the value is, the better the position of the current player is.
     *
     * @param board the board
     * @return The evaluation of the position for the current player
     * @see #maxEvaluateValue()
     */
    @Override
    public double evaluate(@NonNull Board board, @NonNull Player currentPlayer) {
        Player opponentPlayer = (currentPlayer == Player.SCHWARZ) ? Player.WEISS : Player.SCHWARZ;
        final List<Coordinate2D> coordsOccupiedBy = board.getCoordinatesOccupiedBy(currentPlayer);

        int eigeneSteinhoehe = 0;
        for (Coordinate2D coordOccupiedBy : coordsOccupiedBy) {
            Field fieldsOccupiedBy = board.getField(coordOccupiedBy);
            final Iterator<Piece> iterator = fieldsOccupiedBy.getPieces()
                                                             .iterator();
            while (iterator.hasNext() && iterator.next()
                                                 .getOwner() == currentPlayer) {
                eigeneSteinhoehe++;
            }
        }

        int ranks = 0;
        for (Coordinate2D coord : coordsOccupiedBy) {
            final Field field = board.getField(coord);
            final Optional<Piece> head = field.peekHead();
            if( head.isPresent() ) {
                int val = 0;
                switch (head.get().getRank()) {
                    case normal:
                        val = 1;
                        break;
                    case gelb:
                    case gruen:
                        val = 10;
                        break;
                    case rot:
                    case magenta:
                        val = 20;
                        break;
                }
                ranks += val;
            }
        }


        int endsieg = 0;
        final List<Move> possibleMoves = BoardUtils.getPossibleMoves(board, opponentPlayer, moveClassifier);
        if( possibleMoves.isEmpty() ) {
            return 20000;
        }

        int opponentMoves = BoardUtils.getPossibleMoves(board, opponentPlayer, moveClassifier).size();

        return 10*eigeneSteinhoehe + ranks + 10*(possibleMoves.size() - 1.5 * opponentMoves);
    }

    /**
     * The absolute maximal value for the evaluate function.
     * This value must not be equal to a possible return value of the evaluation function.
     *
     * @return The <strong>non inclusive</strong> maximal value
     * @see #evaluate(Board, Player)
     */
    @Override
    public double maxEvaluateValue() {
        return 20000;
    }
}
