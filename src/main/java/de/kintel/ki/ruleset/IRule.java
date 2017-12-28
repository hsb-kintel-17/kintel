package de.kintel.ki.ruleset;

import de.kintel.ki.model.Move;

@FunctionalInterface
public interface IRule {
    boolean isValidMove(Move move);
}
