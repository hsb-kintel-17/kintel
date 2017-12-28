package de.kintel.ki.ruleset;

import de.kintel.ki.model.Move;
import de.kintel.ki.ruleset.rules.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RulesChecker implements IRulesChecker {

    private ArrayList<IRule> rules;

    public RulesChecker() {
        this.rules = new ArrayList<>();
        rules.add( new RuleNotForbiddenField() );
        rules.add( new RuleDestinationNotOccupied() );
        rules.add( new RuleNotOwnColorOnPath() );
        rules.add( new RuleNotSameColor() );
        rules.add( new RuleDiagonal() );
        rules.add( new RuleDirection() );
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
