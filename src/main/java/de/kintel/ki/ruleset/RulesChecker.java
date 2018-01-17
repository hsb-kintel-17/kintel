package de.kintel.ki.ruleset;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import de.kintel.ki.model.Board;
import de.kintel.ki.model.Move;
import de.kintel.ki.ruleset.rules.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;

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
            new RuleDistance(),
            new RuleNotMultiplePiecesOnPath(),
            new RuleNotOwnColorOnPath()
        );
    }

    @Override
    public boolean isValidMove(@Nonnull final Move move, Board board) {
        for (IRule r : rules) {
            if (!r.isValidMove(move, board)) {
                return false;
            }
        }
        return true;
    }

}
