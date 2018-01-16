package de.kintel.ki.ruleset;

import de.kintel.ki.model.Board;
import de.kintel.ki.model.Move;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface IRule {
    boolean isValidMove(@Nonnull Move move, Board board);
}
