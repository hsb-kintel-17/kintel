package de.kintel.ki.algorithm;

import de.kintel.ki.model.Board;
import de.kintel.ki.model.Player;
import de.kintel.ki.util.BoardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
@Qualifier("magicFormular")
public class WeigtingImplTest implements Weighting {

    private MoveClassifier moveClassifier;

    @Autowired
    public WeigtingImplTest(MoveClassifier moveClassifier) {
        this.moveClassifier = moveClassifier;
    }

    @Override
    public double evaluate(@Nonnull Board board, @Nonnull Player currentPlayer) {
        Player opponentPlayer = (currentPlayer == Player.WEISS) ? Player.SCHWARZ : Player.WEISS;

        int ownMoves = BoardUtils.getPossibleMoves(board, currentPlayer, moveClassifier).size();
        int opponentMoves = BoardUtils.getPossibleMoves(board, opponentPlayer, moveClassifier).size();


        return ownMoves - 1.5 * opponentMoves;
    }

    @Override
    public double maxEvaluateValue() {
        return 4*10 + 2*3;
    }
}
