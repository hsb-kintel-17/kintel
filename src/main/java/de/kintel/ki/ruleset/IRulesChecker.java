package de.kintel.ki.ruleset;

import de.kintel.ki.model.Board;
import de.kintel.ki.model.Move;

import javax.annotation.Nonnull;

public interface IRulesChecker {
    boolean isValidMove(@Nonnull final Move move, Board board);
}
