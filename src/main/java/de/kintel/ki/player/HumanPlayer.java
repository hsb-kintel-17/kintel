package de.kintel.ki.player;

import de.kintel.ki.model.*;
import de.kintel.ki.ruleset.IRulesChecker;
import de.kintel.ki.util.UMLCoordToCoord2D;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Scanner;

public class HumanPlayer extends Participant {

    private final UMLCoordToCoord2D converter;
    private final IRulesChecker rc;
    private Board board;

    @Autowired
    public HumanPlayer(UMLCoordToCoord2D converter, Player player, IRulesChecker rc) {
        super(player);
        this.converter = converter;
        this.rc = rc;
    }

    @Override
    public Move getNextMove(Board board, int depth) {
        this.board = board;
        Scanner s = new Scanner(System.in);
        Move humanMove = null;
        boolean inputCorrect = false;

        while (!inputCorrect) {
            System.out.println("" + this.getPlayer() + " ist am Zug:");
            String humanInput = s.nextLine();

            humanMove = this.transform(humanInput);
            if (humanMove != null && rc.isValidMove(humanMove)) {
                inputCorrect = true;
            } else {
                System.out.println("Eingabe ist kein valider Zug!");
            }
        }

        return humanMove;
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
            Field startField = board.getField(coordSource);
            Field targetField = board.getField(coordTarget);

            move = new UMLMove(this.board, startField, targetField, this.getPlayer());
        }

        return move;
    }
}
