package de.kintel.ki.ruleset.rules;

import de.kintel.ki.model.Move;
import de.kintel.ki.model.Rank;
import de.kintel.ki.model.Stein;
import de.kintel.ki.ruleset.IRule;

import java.util.Optional;

public class RuleDirection implements IRule {

    @Override
    public boolean isValidMove(Move move) {
        final Optional<Stein> steinOpt = move.from.peekHead();
        if( steinOpt.isPresent() ) {
            Rank rank = steinOpt.get().getRank();
            //TODO: distinguish rank
            switch ( rank ) {
                case normal:
                    return move.isForward();
                default:
                    return false;
            }
        }
        return false;
    }

}
