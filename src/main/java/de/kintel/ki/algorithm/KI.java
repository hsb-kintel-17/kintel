package de.kintel.ki.algorithm;

import de.kintel.ki.model.*;
import de.kintel.ki.ruleset.RulesChecker;
import fr.avianey.minimax4j.impl.Negamax;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kintel on 19.12.2017.
 */
@Component
@Scope("singleton")
public class KI extends Negamax<Move> {

    private final Logger logger = LoggerFactory.getLogger(KI.class);

    private final RulesChecker rulesChecker;
    private final MoveMaker moveMaker;
    private final Weighting weighting;
    private final Board board;
    private Player currentPlayer;

    @Autowired
    public KI(@Nonnull RulesChecker rulesChecker, @Nonnull MoveMaker moveMaker, @Nonnull Weighting weighting) {
        this.rulesChecker = rulesChecker;
        this.moveMaker = moveMaker;
        this.weighting = weighting;
        this.board = new Board(7, 9);
        this.currentPlayer = Player.SCHWARZ;
    }

    /**
     * Tell weather or not the game is over.
     *
     * @return True if the game is over
     */
    @Override
    public boolean isOver() {
        return getPossibleMoves().isEmpty();
    }

    /**
     * Play the given move and modify the state of the game.
     * This function must set correctly the turn of the next player
     * ... by calling the next() method for example.
     *
     * @param move The move to play
     * @see #next()
     */
    @Override
    public void makeMove(@Nonnull Move move) {
        logger.debug("make move " + move);
        moveMaker.makeMove(move);
        next();
    }

    /**
     * Undo the given move and restore the state of the game.
     * This function must restore correctly the turn of the previous player
     * ... by calling the previous() method for example.
     *
     * @param move The move to cancel
     * @see #previous()
     */
    @Override
    public void unmakeMove(@Nonnull Move move) {
        logger.debug("unmake move " + move);
        moveMaker.undoMove(move);
        previous();
    }

    /**
     * List every valid moves for the current player.<br><br>
     * <i>"Improvement (of the alpha beta pruning) can be achieved without
     * sacrificing accuracy, by using ordering heuristics to search parts
     * of the tree that are likely to force alpha-beta cutoffs early."</i>
     * <br>- <a href="http://en.wikipedia.org/wiki/Alpha-beta_pruning#Heuristic_improvements">Alpha-beta pruning on Wikipedia</a>
     *
     * @return The list of the current player possible moves
     */
    @Override
    public List<Move> getPossibleMoves() {

        final List<Move> possibleMoves;
        // Moves the user must do
        final List<Move> zugzwaenge = new ArrayList<>();
        // Ordinary Moves
        final List<Move> moves = new ArrayList<>();
        final List<Field> fieldsOccupiedBy = board.getFieldsOccupiedBy(currentPlayer);

        for( Field fieldFrom : fieldsOccupiedBy ) {
            Coordinate2D coordFrom = board.getCoordinate(fieldFrom);
            final List<Field> diagonalSurroundings = BoardUtils.getDiagonalSurroundings(board, coordFrom, 2);

            for( Field surrounding : diagonalSurroundings ) {
                Move move = new UMLMove(board, fieldFrom, surrounding, currentPlayer);
                if( rulesChecker.isValidMove( move ) ) {
                    if ( move.getOpponentOpt().isPresent() ) {
                        zugzwaenge.add( move );
                    } else {
                        moves.add( move );
                    }
                }
            }
        }

        // If there are zugwang moves then these moves are the only possible moves
        possibleMoves = zugzwaenge.isEmpty() ? moves : zugzwaenge;

        logger.info("{} possible moves for player {}", possibleMoves.size(), currentPlayer);
        logger.info("possible moves: {}", possibleMoves);
        return possibleMoves;
    }

    /**
     * Evaluate the state of the game <strong>for the current player</strong> after a move.
     * The greatest the value is, the better the position of the current player is.
     *
     * @return The evaluation of the position for the current player
     * @see #maxEvaluateValue()
     */
    @Override
    public double evaluate() {
        return weighting.evaluate(board, currentPlayer);
    }

    /**
     * The absolute maximal value for the evaluate function.
     * This value must not be equal to a possible return value of the evaluation function.
     *
     * @return The <strong>non inclusive</strong> maximal value
     * @see #evaluate()
     */
    @Override
    public double maxEvaluateValue() {
        return weighting.maxEvaluateValue();
    }

    /**
     * Change current turn to the next player.
     * This method must not be used in conjunction with the makeMove() method.
     * Use it to implement a <strong>pass</strong> functionality.
     *
     * @see #makeMove(Move)
     */
    @Override
    public void next() {
        togglePlayer();
    }

    /**
     * Change current turn to the previous player.
     * This method must not be used in conjunction with the unmakeMove() method.
     * Use it to implement an <strong>undo</strong> functionality.
     *
     * @see #unmakeMove(Move)
     */
    @Override
    public void previous() {
        togglePlayer();
    }

    private void togglePlayer() {
        currentPlayer = currentPlayer.equals(Player.SCHWARZ) ? Player.WEISS : Player.SCHWARZ;
    }

    public Board getBoard() {
        return board;
    }

    @Override
    public String toString() {
        return board.toString();
    }

    public Move getBestMove(int depth) {
        if (getBestMoves(depth).size() >= 1) {
            return getBestMoves(depth).get(0);
        } else {
            return null;
        }
    }

}
