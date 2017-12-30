package de.kintel.ki.algorithm;

import de.kintel.ki.model.*;
import de.kintel.ki.ruleset.RulesChecker;
import fr.pixelprose.minimax4j.Difficulty;
import fr.pixelprose.minimax4j.IA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kintel on 19.12.2017.
 */
@Component
@Scope("singleton")
public class KI extends IA<Move> {

    Logger logger = LoggerFactory.getLogger(KI.class);

    private final RulesChecker rulesChecker;
    private final MoveMaker moveMaker;
    private Weighting weighting;
    private Board board;
    private Player currentPlayer;

    ArrayDeque<Board> history = new ArrayDeque<>();

    /**
     * Creates a new IA using the {@link Algorithm#NEGAMAX} algorithm<br/>
     * {@link Algorithm#NEGASCOUT} performs slowly on several tests at the moment...
     */
    @Autowired
    public KI(RulesChecker rulesChecker, MoveMaker moveMaker, Weighting weighting) {
        this.rulesChecker = rulesChecker;
        this.moveMaker = moveMaker;
        this.weighting = weighting;
        this.board = new Board(7, 9);
        this.currentPlayer = Player.SCHWARZ;
    }

    /**
     * Get the IA difficulty level for the current player
     *
     * @return The difficulty
     */
    @Override
    public Difficulty getDifficulty() {
        // This is the level minimax will evaluate all moves.
        return () -> 2;
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
    public void makeMove(Move move) {
        logger.debug("TODO: implement makeMove " + move);
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
    public void unmakeMove(Move move) {
        logger.debug("TODO: implement unmakeMove " + move);
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

        final List<Move> possibleMoves = new ArrayList<>();
        final List<Field> fieldsOccupiedBy = board.getFieldsOccupiedBy(currentPlayer);

        for( Field fieldFrom : fieldsOccupiedBy ) {
            for (int i = 0; i < board.getHeight(); i++) {
                for (int j = 0; j < board.getWidth(); j++) {
                    Field fieldTo = board.getField(i, j);
                    Move move = new UMLMove(board, fieldFrom, fieldTo, currentPlayer);
                    if( rulesChecker.isValidMove( move )) {
                        possibleMoves.add( move );
                    }
                }
            }
        }

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
        return weighting.evaluate(board);
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

    @Override
    public String toString() {
        return board.toString();
    }

    public Board getBoard() {
        return board;
    }
}
