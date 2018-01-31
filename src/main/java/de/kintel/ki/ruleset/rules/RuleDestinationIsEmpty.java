package de.kintel.ki.ruleset.rules;

import de.kintel.ki.model.Board;
import de.kintel.ki.model.Move;
import de.kintel.ki.ruleset.IRule;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
@Scope("singleton")
public class RuleDestinationIsEmpty implements IRule {

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean isValidMove(@Nonnull final Move move, Board board) {
        return !board.getField(move.getTargetCoordinate()).peekHead().isPresent();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int getOrder() {
        return 2;
    }
}
