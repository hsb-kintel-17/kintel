package de.kintel.ki.ruleset.rules;

import de.kintel.ki.algorithm.Pathfinder;
import de.kintel.ki.model.Field;
import de.kintel.ki.model.Move;
import de.kintel.ki.model.Stein;
import de.kintel.ki.ruleset.IRule;

import java.util.Deque;
import java.util.Optional;

/**
 * There must be not the own color on the path.
 */
public class RuleNotOwnColorOnPath implements IRule {
    @Override
    public boolean isValidMove(Move move) {
        Deque<Field> path = Pathfinder.find(move);
        path.removeFirst();
        path.removeLast();
        return path.stream()
                   .map(Field::peekHead)
                   .filter(Optional::isPresent)
                   .map(Optional::get)
                   .map(Stein::getOwner)
                   .noneMatch(f -> f.equals(move.currentPlayer));
    }
}
