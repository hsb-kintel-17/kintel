package de.kintel.ki.ruleset;

import de.kintel.ki.model.Board;
import de.kintel.ki.model.Move;
import org.eclipse.jdt.annotation.NonNull;

public interface IRulesChecker {
    boolean isValidMove(@NonNull final Move move, Board board);
}
