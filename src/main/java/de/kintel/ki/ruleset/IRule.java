package de.kintel.ki.ruleset;

import de.kintel.ki.model.Board;
import de.kintel.ki.model.Move;
import org.springframework.core.Ordered;

import javax.annotation.Nonnull;

public interface IRule extends Ordered {
    boolean isValidMove(@Nonnull Move move, Board board);
}
