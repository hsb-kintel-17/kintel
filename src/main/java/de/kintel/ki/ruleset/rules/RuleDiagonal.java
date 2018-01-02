package de.kintel.ki.ruleset.rules;

import de.kintel.ki.model.Coordinate2D;
import de.kintel.ki.model.Move;
import de.kintel.ki.ruleset.IRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

public class RuleDiagonal implements IRule {

    private final Logger logger = LoggerFactory.getLogger(RuleDiagonal.class);

    @Override
    public boolean isValidMove(@Nonnull final Move move) {
        final Coordinate2D coordFrom = move.getBoard()
                                           .getCoordinate(move.getSourceField());
        final Coordinate2D coordTo = move.getBoard()
                                         .getCoordinate(move.getTargetField());

        int dx = Math.abs(coordFrom.getX() - coordTo.getX());
        int dy = Math.abs(coordFrom.getY() - coordTo.getY());

        if( dx != dy ) {
            logger.debug("Not a diagonal move: dx({}) != dy({})", dx, dy);
            return false;
        } else if( dx < 1 ) {
            logger.debug("Move is too short to be valid at all: dx({}) == dy({}) < 1", dx, dy);
            return false;
        } else {
            return true;
        }
    }
}
