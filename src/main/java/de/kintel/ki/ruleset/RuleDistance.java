package de.kintel.ki.ruleset;

import de.kintel.ki.model.Move;

import javax.annotation.Nonnull;

public class RuleDistance implements IRule {
    @Override
    public boolean isValidMove(@Nonnull Move move) {

        if(move.getOpponentOpt().isPresent()) {
            return move.getForwardPath().size() == 3;
        }
        return move.getForwardPath().size() == 2;
    }
}
