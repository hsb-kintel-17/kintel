package de.kintel.ki.ruleset.rules;

import de.kintel.ki.algorithm.PathFinder;
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
    public boolean isValidMove(@Nonnull final Move move) {
        Deque<Field> path = PathFinder.find(move);
        path.removeFirst();
        path.removeLast();
        return path.stream()
                   .map(Field::getOwner)
                   .filter(Optional::isPresent)
                   .map(Optional::get)
                   .noneMatch(p -> p.equals(move.getCurrentPlayer()));
    }
}
