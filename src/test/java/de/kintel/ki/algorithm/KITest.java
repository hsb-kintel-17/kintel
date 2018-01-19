package de.kintel.ki.algorithm;

import de.kintel.ki.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class KITest {

    @Autowired
    private KI ki;

    @Before
    public void setUp() {
        ki.setCurrentPlayer(Player.SCHWARZ.name());
    }

    @Test
    public void getPossibleMoves() {
        final List<Move> possibleMovesActual = ki.getPossibleMoves();
        assertThat(possibleMovesActual.size(), is(6));
    }

    @Test
    public void getBestMoveHasNoSideeffects() {
        Board board = new Board(7, 9, GridFactory.getLaskaInitGrid());
        ki.setBoard(board);

        checkBoardHasDefaultSetting(board);
        Move bestMove = ki.getBestMove(3);
        assertThat(bestMove, notNullValue());
        checkBoardHasDefaultSetting(board);
    }

    private void checkBoardHasDefaultSetting(Board board) {
        assertThat(board.getField(0, 0).getOwner().orElseThrow(IllegalStateException::new), is(Player.SCHWARZ));
        assertThat(board.getField(0, 2).getOwner().orElseThrow(IllegalStateException::new), is(Player.SCHWARZ));
        assertThat(board.getField(0, 4).getOwner().orElseThrow(IllegalStateException::new), is(Player.SCHWARZ));
        assertThat(board.getField(0, 6).getOwner().orElseThrow(IllegalStateException::new), is(Player.SCHWARZ));
        assertThat(board.getField(0, 8).getOwner().orElseThrow(IllegalStateException::new), is(Player.SCHWARZ));

        assertThat(board.getField(1, 1).getOwner().orElseThrow(IllegalStateException::new), is(Player.SCHWARZ));
        assertThat(board.getField(1, 3).getOwner().orElseThrow(IllegalStateException::new), is(Player.SCHWARZ));
        assertThat(board.getField(1, 5).getOwner().orElseThrow(IllegalStateException::new), is(Player.SCHWARZ));
        assertThat(board.getField(1, 7).getOwner().orElseThrow(IllegalStateException::new), is(Player.SCHWARZ));

        assertThat(board.getField(2, 2).getOwner().orElseThrow(IllegalStateException::new), is(Player.SCHWARZ));
        assertThat(board.getField(2, 4).getOwner().orElseThrow(IllegalStateException::new), is(Player.SCHWARZ));
        assertThat(board.getField(2, 6).getOwner().orElseThrow(IllegalStateException::new), is(Player.SCHWARZ));

        assertThat(board.getField(6, 0).getOwner().orElseThrow(IllegalStateException::new), is(Player.WEISS));
        assertThat(board.getField(6, 2).getOwner().orElseThrow(IllegalStateException::new), is(Player.WEISS));
        assertThat(board.getField(6, 4).getOwner().orElseThrow(IllegalStateException::new), is(Player.WEISS));
        assertThat(board.getField(6, 6).getOwner().orElseThrow(IllegalStateException::new), is(Player.WEISS));
        assertThat(board.getField(6, 8).getOwner().orElseThrow(IllegalStateException::new), is(Player.WEISS));

        assertThat(board.getField(5, 1).getOwner().orElseThrow(IllegalStateException::new), is(Player.WEISS));
        assertThat(board.getField(5, 3).getOwner().orElseThrow(IllegalStateException::new), is(Player.WEISS));
        assertThat(board.getField(5, 5).getOwner().orElseThrow(IllegalStateException::new), is(Player.WEISS));
        assertThat(board.getField(5, 7).getOwner().orElseThrow(IllegalStateException::new), is(Player.WEISS));

        assertThat(board.getField(4, 2).getOwner().orElseThrow(IllegalStateException::new), is(Player.WEISS));
        assertThat(board.getField(4, 4).getOwner().orElseThrow(IllegalStateException::new), is(Player.WEISS));
        assertThat(board.getField(4, 6).getOwner().orElseThrow(IllegalStateException::new), is(Player.WEISS));
    }

    @Test
    public void move_and_unmake() {

        Board board = ki.getBoard();
        final Coordinate2D coordinateFrom = new Coordinate2D(2, 2);
        final Coordinate2D coordinateTo = new Coordinate2D(3, 3);

        final Move move = new UMLMove( coordinateFrom, coordinateTo, Player.SCHWARZ);
        move.setForwardSourceRank(board.getField(coordinateFrom).getSteine().peekFirst().getRank());
        move.setForwardClassification(MoveClassifier.MoveType.MOVE);

        assertThat(board.getField(2, 2).isEmpty(), is(false));
        ki.makeMove(move);
        ki.unmakeMove(move);
        //board = move.getBoard();
        assertThat(board.getField(2, 2).isEmpty(), is(false));
    }

    @Test
    public void move_and_unmake2() {

        Board board = new Board(7, 9, GridFactory.getLaskaInitGrid());
        ki.setBoard(board);
        final Coordinate2D coordinateFrom = new Coordinate2D(2, 2);
        final Coordinate2D coordinateTo = new Coordinate2D(3, 1);

        final Move move = new UMLMove(coordinateFrom, coordinateTo, Player.SCHWARZ);
        move.setForwardSourceRank(board.getField(coordinateFrom).getSteine().peekFirst().getRank());
        move.setForwardClassification(MoveClassifier.MoveType.MOVE);

        assertThat(board.getField(2, 2).isEmpty(), is(false));
        assertThat(board.getField(3, 1).isEmpty(), is(true));
        ki.makeMove(move);
        ki.unmakeMove(move);

        //board = move.getBoard();
        assertThat(board.getField(2, 2).isEmpty(), is(false));
        assertThat(board.getField(3, 1).isEmpty(), is(true));
    }

}