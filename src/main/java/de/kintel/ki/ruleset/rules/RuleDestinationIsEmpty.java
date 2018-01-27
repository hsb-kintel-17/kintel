package de.kintel.ki.ruleset.rules;

import de.kintel.ki.model.Board;
import de.kintel.ki.model.Move;
import de.kintel.ki.ruleset.IRule;
import org.eclipse.jdt.annotation.NonNull;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class RuleDestinationIsEmpty implements IRule {

    @Override
    public boolean isValidMove(@NonNull final Move move, Board board) {
        return !board.getField(move.getTargetCoordinate()).peekHead().isPresent();
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
        return 2;
    }
}
