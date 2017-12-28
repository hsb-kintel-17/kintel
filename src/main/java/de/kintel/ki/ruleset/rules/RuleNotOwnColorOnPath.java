package de.kintel.ki.ruleset.rules;

import de.kintel.ki.model.Field;
import de.kintel.ki.model.Move;
import de.kintel.ki.algorithm.Pathfinder;
import de.kintel.ki.ruleset.IRule;

import java.util.List;

/**
 * There must be not the own color on the path.
 */
public class RuleNotOwnColorOnPath implements IRule {
    @Override
    public boolean isValidMove(Move move) {
        List<Field> path = Pathfinder.find(move);
        return path.stream()
                   .skip(1)
                   .skip(path.size())
                   .map(field -> field.peekHead()
                                      .get()
                                      .getOwner())
                   .noneMatch(f -> f.equals(move.currentPlayer));
    }
}
