package de.kintel.ki.ruleset.rules;

import de.kintel.ki.model.Move;
import de.kintel.ki.ruleset.IRule;

import javax.annotation.Nonnull;

public class RuleNotForbiddenField implements IRule {
    @Override
    public boolean isValidMove(@Nonnull final Move move) {
        return !move.getTargetField()
                    .isForbidden();
    }
}
