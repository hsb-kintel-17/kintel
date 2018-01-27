package de.kintel.ki.ruleset;

import de.kintel.ki.model.Board;
import de.kintel.ki.model.Move;
import org.eclipse.jdt.annotation.NonNull;
import org.springframework.core.Ordered;

public interface IRule extends Ordered {
    boolean isValidMove(@NonNull Move move, Board board);
}
