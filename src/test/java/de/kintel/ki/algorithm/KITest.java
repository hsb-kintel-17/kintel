package de.kintel.ki.algorithm;

import de.kintel.ki.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class KITest {

    @Autowired
    private KI ki;

    @Test
    public void getPossibleMoves() {
        final List<Move> possibleMovesActual = ki.getPossibleMoves();
        assertThat(possibleMovesActual.size(), is(6));
    }

    @Test
    public void move_and_unmake() {

        final Board board = ki.getBoard();
        final Field fieldFrom = board.getField(2, 2);
        final Field fieldTo = board.getField(3, 3);

        final Move move = new UMLMove(board, fieldFrom, fieldTo, Player.SCHWARZ);

        assertThat(board.getField(2, 2).isEmpty(), is(false));
        ki.makeMove(move);
        assertThat(board.getField(2, 2).isEmpty(), is(true));
        ki.unmakeMove(move);
        assertThat(board.getField(2, 2).isEmpty(), is(false));
    }
}