package de.kintel.ki.ruleset.rules;

import de.kintel.ki.algorithm.PathFinder;
import de.kintel.ki.model.Field;
import de.kintel.ki.model.Move;
import de.kintel.ki.ruleset.IRule;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Deque;
import java.util.Iterator;
import java.util.Optional;

@Component
public class RuleMoveEndsAfterOpponent implements IRule {
    @Override
    public boolean isValidMove(@Nonnull Move move) {

        final Optional<Field> opponentOpt = move.getOpponentOpt();

        if( !opponentOpt.isPresent() ) {
            // If there is no opponent on this path we can't apply this rule
           return true;
        }

        Deque<Field> path = move.getForwardPath();
        path.removeFirst();
        path.removeLast();

        final Field opponentField = opponentOpt.get();
        final Iterator<Field> iterator = path.iterator();
        while(iterator.hasNext()) {
            final Field next = iterator.next();
            // remove all fields until opponent field
            if(next.equals(opponentField)) {
                iterator.remove();
                // Cut after opponent field
                break;
            } else {
                iterator.remove();
            }
        }
        // after the opponent field there must be no field
        return path.isEmpty();
    }
}
