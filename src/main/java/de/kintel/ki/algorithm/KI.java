package de.kintel.ki.algorithm;

import de.kintel.ki.libs.minimax4j.impl.ParallelNegamax;
import de.kintel.ki.model.Board;
import de.kintel.ki.model.Move;
import de.kintel.ki.model.Player;
import de.kintel.ki.util.BoardUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;


/**
 * Created by kintel on 19.12.2017.
 */

public class KI extends ParallelNegamax<Move> implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(KI.class);
    private MoveMaker moveMaker;
    private Weighting weighting;
    private Board board;
    private Player currentPlayer;
    private MoveClassifier moveClassifier;

    public KI(@NonNull MoveClassifier moveClassifier, @NonNull MoveMaker moveMaker, @NonNull Weighting weighting, @NonNull Board board) {
        this.moveMaker = moveMaker;
        this.weighting = weighting;
        this.board = board;
        this.moveClassifier = moveClassifier;
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
    public void makeMove(@NonNull Move move) {
        logger.debug("make move " + move);
        moveMaker.makeMove(move, board);
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
    public void unmakeMove(@NonNull Move move) {
        logger.debug("unmake move " + move);
        moveMaker.undoMove(move, board);
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
        return BoardUtils.getPossibleMoves(board, currentPlayer, moveClassifier);
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

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = Player.valueOf(currentPlayer);
    }

    public Board getBoard() {
        return board;
    }

    @Override
    public String toString() {
        return board.toString();
    }

    public Move getBestMove(int depth) {
        List<Move> possibleMoves = getPossibleMoves();
        // If there is only one move possible execute it without spending time on evaluation
        if(possibleMoves.size() == 1){
            return possibleMoves.get(0);
        }
        List<Move> bestmoves = getBestMoves(depth);
        if (!bestmoves.isEmpty()) {
            return bestmoves.get(0);
        } else {
            return null;
        }
    }

    private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
         stream.writeObject(board);
         stream.writeObject(currentPlayer);
    }

    private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
        board = (Board) stream.readObject();
        currentPlayer = (Player) stream.readObject();
    }

    public KI deepCopy() {
        return SerializationUtils.roundtrip(this);
    }

    @Override
    public ParallelNegamax<Move> clone() {
        final Board boardCopy = getBoard().deepCopy();
        final KI copy = new KI(moveClassifier, new MoveMakerImpl(new RankMakerImpl()), weighting, boardCopy);
        copy.setCurrentPlayer(currentPlayer.name());
        return copy;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Weighting getWeighting() {
        return weighting;
    }
}
