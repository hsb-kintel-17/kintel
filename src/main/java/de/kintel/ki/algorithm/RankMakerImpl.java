/*
 * hsb-kintel-17
 * Copyright (C) 2018 hsb-kintel-17
 * This file is covered by the LICENSE file in the root of this project.
 */

package de.kintel.ki.algorithm;

import com.google.common.collect.Lists;
import de.kintel.ki.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class RankMakerImpl implements RankMaker {

    /**
     * Degrate and/or promote the pieces that were involved in the move, if necessary
     * @param move The move that was executed before.
     * @param board The board, on which the move was executed
     */
    @Override
    public void processRankChange(Move move, Board board) {
        final Coordinate2D coordTo = move.getTargetCoordinate();
        final Field fieldTo = board.getField(coordTo);

        boolean basementHit = move.getCurrentPlayer().equals(Player.WEISS) ? coordTo.getX() == 0 : coordTo.getX() == board.getHeight() - 1;

        if(basementHit) {

            if ( fieldTo.peekHead().isPresent() ) {
                fieldTo.peekHead().get().promote(move.getForwardClassification());
            }
            //degrate the captured piece
            final ArrayList<Piece> pieces = Lists.newArrayList(fieldTo.getPieces());
            if (pieces.size() >= 2 && move.getForwardClassification() == MoveClassifier.MoveType.CAPTURE) {
                pieces.get(1).degrade();
            }
        }
    }

}
