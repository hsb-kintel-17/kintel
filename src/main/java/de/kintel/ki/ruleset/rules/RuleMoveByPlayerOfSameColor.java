package de.kintel.ki.ruleset.rules;

import de.kintel.ki.model.Move;
import de.kintel.ki.ruleset.IRule;

import javax.annotation.Nonnull;

public class RuleMoveByPlayerOfSameColor implements IRule {

    @Override
    public boolean isValidMove(@Nonnull final Move move) {
        if( !move.getBoard().getField(move.getSourceCoordinate()).getOwner().isPresent() ) {
            throw new IllegalStateException("Source field has no owner.");
        }

        return move.getBoard().getField(move.getSourceCoordinate()).getOwner().get().equals(move.getCurrentPlayer());
    }

}
