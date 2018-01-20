package de.kintel.ki.ruleset.rules;

import de.kintel.ki.algorithm.PathFinder;
import de.kintel.ki.model.Board;
import de.kintel.ki.model.Coordinate2D;
import de.kintel.ki.model.Field;
import de.kintel.ki.model.Move;
import de.kintel.ki.ruleset.IRule;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Deque;
import java.util.Optional;

/**
 * There must be not the own color on the path.
 */

@Component
@Scope("singleton")
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

    /**
     * Get the order value of this object.
     * <p>Higher values are interpreted as lower priority. As a consequence,
     * the object with the lowest value has the highest priority (somewhat
     * analogous to Servlet {@code load-on-startup} values).
     * <p>Same order values will result in arbitrary sort positions for the
     * affected objects.
     *
     * @return the order value
     * @see #HIGHEST_PRECEDENCE
     * @see #LOWEST_PRECEDENCE
     */
    @Override
    public int getOrder() {
        return 6;
    }
}
