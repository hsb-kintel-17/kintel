package de.kintel.ki.player;

import de.kintel.ki.algorithm.MoveClassifier;
import de.kintel.ki.algorithm.MoveMaker;
import de.kintel.ki.model.*;
import de.kintel.ki.util.BoardUtils;
import de.kintel.ki.util.UMLCoordToCoord2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Scanner;

public class HumanPlayer extends Participant {

    private static final Logger logger = LoggerFactory.getLogger(HumanPlayer.class);

    private final MoveClassifier moveClassifier;
    private final MoveMaker moveMaker;

    @Autowired
    public HumanPlayer(Board board, UMLCoordToCoord2D converter, Player player, MoveClassifier moveClassifier, MoveMaker moveMaker) {
        super(board, player, converter);
        this.moveClassifier = moveClassifier;
        this.moveMaker = moveMaker;
    }

    @Override
    public Move getNextMove(int depth) {
        Move move = askUserForMoveInput();

        boolean anyMatch;
        final List<Move> possibleMoves = BoardUtils.getPossibleMoves(getBoard(), getPlayer(), moveClassifier);
        do {
            anyMatch = false;

            for(Move possibleMove : possibleMoves){
                anyMatch |= move.getSourceCoordinate().equals(possibleMove.getSourceCoordinate()) && move.getTargetCoordinate().equals(possibleMove.getTargetCoordinate());
            }

            if(!anyMatch) {
                logger.error("Dieser Zug ist nicht gültig.");
                move = askUserForMoveInput();
            }

        } while (!anyMatch);


        return move;
    }

    private Move askUserForMoveInput() {
        Scanner s = new Scanner(System.in, "UTF-8");
        Move move = null;
        boolean inputCorrect = false;

        while (!inputCorrect) {
            logger.info("" + this.getPlayer() + " ist am Zug:");
            String humanInput = s.nextLine();

            move = this.transform(humanInput);
            if (move != null) {
                moveClassifier.classify(move, getBoard());
                inputCorrect = move.getForwardClassification() != MoveClassifier.MoveType.INVALID;
            } else {
                logger.error("Eingabe ist keine Koordinate!");
            }
        }
        return move;
    }

    @Override
    public void makeMove(Move move) {
        moveMaker.makeMove(move, getBoard());
    }

    /**
     * Transforms input by human like e3a5 into a move.
     *
     * @param humanInput string like e3a5
     * @return move
     */
    private Move transform(String humanInput) {
        Move move = null;

        if( humanInput.length() != 4) {
            return null;
        }

        Coordinate2D coordSource = converter.convertUMLCoord(humanInput.substring(0, 2));
        Coordinate2D coordTarget = converter.convertUMLCoord(humanInput.substring(2));

        if ( null != coordSource && null != coordTarget  ) {
            move = new UMLMove(coordSource, coordTarget, this.getPlayer());
        }

        return move;
    }
}
