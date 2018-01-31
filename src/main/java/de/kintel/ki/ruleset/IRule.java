package de.kintel.ki.ruleset;

import de.kintel.ki.model.Board;
import de.kintel.ki.model.Move;
import org.springframework.core.Ordered;

import javax.annotation.Nonnull;

public interface IRule extends Ordered {
    /**
     * Check if the move is a valid move on the current board. This means it does not violate the rule in any matter.
     * @param move the move to check
     * @param board the board to check against
     * @return true if valid, false otherwise
     */
    boolean isValidMove(@Nonnull Move move, Board board);
}
