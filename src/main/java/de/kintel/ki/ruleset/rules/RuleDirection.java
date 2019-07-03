/*
 * hsb-kintel-17
 * Copyright (C) 2018 hsb-kintel-17
 * This file is covered by the LICENSE file in the root of this project.
 */

package de.kintel.ki.ruleset.rules;

import de.kintel.ki.algorithm.MoveClassifier;
import de.kintel.ki.model.Board;
import de.kintel.ki.model.Move;
import de.kintel.ki.model.Piece;
import de.kintel.ki.model.Rank;
import de.kintel.ki.ruleset.IRule;
import org.eclipse.jdt.annotation.NonNull;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@Scope("singleton")
public class RuleDirection implements IRule {

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean isValidMove(@NonNull final Move move, Board board) {
        final boolean isForward = move.isForward();
        final MoveClassifier.MoveType moveType = move.getForwardClassification();
        final Optional<Piece> steinOpt = board.getField(move.getSourceCoordinate()).peekHead();
        if( steinOpt.isPresent() ) {
            Rank rank = steinOpt.get().getRank();
            switch ( rank ) {
                case normal:
                    return isForward;
                // Gruene und Rote Steine dürfen rückwärts nur ziehen
                case gruen:
                case rot:
                    return !(!isForward && moveType == MoveClassifier.MoveType.CAPTURE);
                //gelbe und magenta Steine dürfen in beide Richtungen ziehen und schlagen (also dürfen sie alles)
                case gelb:
                case magenta:
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int getOrder() {
        return 3;
    }
}
