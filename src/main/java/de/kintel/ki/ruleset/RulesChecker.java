package de.kintel.ki.ruleset;

import com.google.common.collect.Lists;
import de.kintel.ki.model.Move;
import de.kintel.ki.ruleset.rules.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.ArrayList;

@Component
@Scope("singleton")
public class RulesChecker implements IRulesChecker {

    private final ArrayList<IRule> rules;

    public RulesChecker() {
        this.rules = Lists.newArrayList(
            new RuleMoveByPlayerOfSameColor(),
            new RuleNotForbiddenField(),
            new RuleDestinationIsEmpty(),
            new RuleDirection(),
            new RuleDiagonal(),
            new RuleNotMultiplePiecesOnPath(),
            new RuleNotOwnColorOnPath()
        );
    }

    @Override
    public boolean isValidMove(@Nonnull final Move move) {
        return rules.stream().allMatch( r -> r.isValidMove(move));
    }

}
