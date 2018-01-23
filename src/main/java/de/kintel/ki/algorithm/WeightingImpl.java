package de.kintel.ki.algorithm;

import de.kintel.ki.model.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

@Component
@Qualifier("weightingHeightAndRank")
public class WeightingImpl implements Weighting {

    /**
     * Evaluate the state of the game <strong>for the current player</strong> after a move.
     * The greatest the value is, the better the position of the current player is.
     *
     * @param board the board
     * @return The evaluation of the position for the current player
     * @see #maxEvaluateValue()
     */
    @Override
    public double evaluate(@Nonnull Board board, @Nonnull Player currentPlayer) {
        final List<Coordinate2D> fieldsOccupiedBy = board.getCoordinatesOccupiedBy(currentPlayer);
        double heights = fieldsOccupiedBy.stream()
                        .map(coordinate -> board.getField(coordinate))
                        .mapToDouble(f -> f.getPieces()
                        .size())
                        .map(d -> d * Weight.WIN.getValue()).sum();
        int ranks = 0;
        for (Coordinate2D coord : fieldsOccupiedBy) {
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
                        val = 50;
                        break;
                    case rot:
                    case magenta:
                        val = 250;
                        break;
                }
                ranks += val * Weight.WIN.getValue();
            }
        }

        return heights + ranks;
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
        return 12*4.0 + 250*12;
    }
}
