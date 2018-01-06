package de.kintel.ki.algorithm;

import com.google.common.collect.Lists;
import de.kintel.ki.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class RankMakerImpl implements RankMaker {

    @Override
    public void processRankChange(Move move, boolean undo) {
        final Board board = move.getBoard();
        final Coordinate2D coordFrom = board.getCoordinate(move.getSourceField());
        final Coordinate2D coordTo = board.getCoordinate(move.getTargetField());
        final Field fieldFrom = board.getField(coordFrom);
        final Field fieldTo = board.getField(coordTo);

        boolean basementHit = move.getCurrentPlayer().equals(Player.WEISS) ? coordTo.getX() == 0 : coordTo.getX() == board.getHeight() - 1;

        if(basementHit) {
            if(!undo) {
                fieldTo.peekHead().ifPresent(h -> h.promote(move.getForwardClassification()));
                final ArrayList<Piece> pieces = Lists.newArrayList(fieldTo.getSteine());
                if (pieces.size() >= 2) {
                    pieces.get(1).degrade();
                }
            } else {
                if( move.getForwarFromRankOpt().isPresent() ) {
                    fieldFrom.peekHead().ifPresent(h -> h.setRank(move.getForwarFromRankOpt().get()));
                }
                if( move.getForwardClassification().equals(PathClassifier.MoveType.CAPTURE)) {
                    if( move.getForwarOpponentRankOpt().isPresent() ) {
                        move.getOpponentOpt().ifPresent(h -> h.peekHead().ifPresent( f -> f.setRank(move.getForwarOpponentRankOpt().get())));
                    } else {
                        throw new IllegalStateException("The should be the opponent Rank stored for redo but isn't." + move);
                    }
                }
            }
        }
    }

}
