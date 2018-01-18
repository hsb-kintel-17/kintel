package de.kintel.ki.ruleset.rules;

import de.kintel.ki.algorithm.MoveClassifier;
import de.kintel.ki.model.*;
import de.kintel.ki.ruleset.IRule;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Optional;

@Component
public class RuleDirection implements IRule {

    @Override
    public boolean isValidMove(@Nonnull final Move move, Board board) {
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

}
