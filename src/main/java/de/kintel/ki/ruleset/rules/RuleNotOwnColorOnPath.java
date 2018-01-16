package de.kintel.ki.ruleset.rules;

import de.kintel.ki.algorithm.PathFinder;
import de.kintel.ki.model.Board;
import de.kintel.ki.model.Coordinate2D;
import de.kintel.ki.model.Field;
import de.kintel.ki.model.Move;
import de.kintel.ki.ruleset.IRule;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Deque;
import java.util.Optional;

/**
 * There must be not the own color on the path.
 */
@Component
public class RuleNotOwnColorOnPath implements IRule {
    @Override
    public boolean isValidMove(@Nonnull final Move move,@Nonnull Board board) {
        Deque<Coordinate2D> path = PathFinder.find(move, board);
        path.removeFirst();
        path.removeLast();
        return path.stream()
                .map(coordinate -> board.getField(coordinate))
                .map(Field::getOwner)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .noneMatch(p -> p.equals(move.getCurrentPlayer()));
    }
}
