package de.kintel.ki.ruleset;

import de.kintel.ki.model.Move;
import de.kintel.ki.ruleset.rules.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope("singleton")
public class RulesChecker implements IRulesChecker {

    private ArrayList<IRule> rules;

    public RulesChecker() {
        this.rules = new ArrayList<>();
        rules.add( new RuleNotSameColor() );
        rules.add( new RuleNotForbiddenField() );
        rules.add( new RuleDirection() );
        rules.add( new RuleDiagonal() );
        rules.add( new RuleDestinationIsEmpty() );
        rules.add( new RuleNotMultiplePiecesOnPath() );
        rules.add( new RuleNotOwnColorOnPath() );
    }

    @Override
    public boolean isValidMove(Move move) {
        for (IRule rule : rules ) {
            if( !rule.isValidMove(move) ) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Move> stripValidMoves(List<Move> moves) {
        return moves.stream().filter(this::isValidMove).collect(Collectors.toList());
    }
}
