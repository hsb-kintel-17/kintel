package de.kintel.ki.ruleset.rules;

import de.kintel.ki.Field;
import de.kintel.ki.Move;
import de.kintel.ki.Pathfinder;
import de.kintel.ki.ruleset.IRule;

import java.util.List;

public class NotOwnColorOnPath implements IRule {
    @Override
    public boolean isValidMove(Move move) {
        List<Field> path = Pathfinder.find(move);
        return path.stream().map(field -> field.peekHead().get().getOwner()).anyMatch(f -> f.equals(move.currentPlayer));
    }
}
