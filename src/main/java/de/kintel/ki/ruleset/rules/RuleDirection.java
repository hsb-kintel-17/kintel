package de.kintel.ki.ruleset.rules;

import de.kintel.ki.algorithm.PathClassifier;
import de.kintel.ki.model.Move;
import de.kintel.ki.model.Rank;
import de.kintel.ki.model.Piece;
import de.kintel.ki.ruleset.IRule;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Optional;

@Component
public class RuleDirection implements IRule {

    @Override
    public boolean isValidMove(@Nonnull final Move move) {
        final boolean isForward = move.isForward();
        final PathClassifier.MoveType moveType = move.getForwardClassification();
        final Optional<Piece> steinOpt = move.getBoard().getField(move.getSourceCoordinate()).peekHead();
        if( steinOpt.isPresent() ) {
            Rank rank = steinOpt.get().getRank();
            switch ( rank ) {
                case normal:
                    return isForward;
                // Gruene und Rote Steine dürfen rückwärts nur ziehen
                case gruen:
                case rot:
                    return !(!isForward && moveType.equals(PathClassifier.MoveType.CAPTURE));
                case gelb:
                case magenta:
                    return !(isForward && moveType.equals(PathClassifier.MoveType.CAPTURE));
                case gold:
                case purpur:
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }

}
