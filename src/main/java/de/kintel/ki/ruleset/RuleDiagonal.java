package de.kintel.ki.ruleset;

import de.kintel.ki.Coordinate2D;
import de.kintel.ki.Move;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class RuleDiagonal implements IRule {

    Logger logger = LoggerFactory.getLogger(RuleDiagonal.class);

    @Override
    public boolean isValidMove(Move move) {
        Optional<Coordinate2D> coordFromOpt = move.board.getCoordinate(move.from);
        Optional<Coordinate2D> coordToOpt = move.board.getCoordinate(move.to);

        if( !coordFromOpt.isPresent() || !coordToOpt.isPresent() ) {
           throw new IllegalStateException("Could not find coordinates for field.");
        }

        final Coordinate2D coordFrom = coordFromOpt.get();
        final Coordinate2D coordTo = coordToOpt.get();

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
