package de.kintel.ki.ruleset.rules;

import de.kintel.ki.Move;
import de.kintel.ki.ruleset.IRule;

public class RuleNotForbiddenField implements IRule {
    @Override
    public boolean isValidMove(Move move) {
        return !move.to.isForbidden();
    }
}
