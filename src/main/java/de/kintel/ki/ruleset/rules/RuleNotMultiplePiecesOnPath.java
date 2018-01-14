package de.kintel.ki.ruleset.rules;

import java.util.Optional;
import de.kintel.ki.algorithm.PathFinder;
import de.kintel.ki.model.Coordinate2D;
import de.kintel.ki.model.Field;
import de.kintel.ki.model.Move;
import de.kintel.ki.ruleset.IRule;

import javax.annotation.Nonnull;
import java.util.Deque;

/**
 * There must be none or one piece on the path to be a valid move.
 * (Mehrfachschlagen ist im Spiel ausgeschlossen TODO: Richtigkeit prüfen)
 */
public class RuleNotMultiplePiecesOnPath implements IRule {
    @Override
    public boolean isValidMove(@Nonnull final Move move) {
        Deque<Coordinate2D> path = PathFinder.find(move);
        path.removeFirst();
        path.removeLast();
        return path.stream()
                   .map(coordinate -> move.getBoard().getField(coordinate))
                   .map(Field::getOwner)
                   .filter(Optional::isPresent)
                   .map(Optional::get)
                   .count() <= 1;
    }
}
