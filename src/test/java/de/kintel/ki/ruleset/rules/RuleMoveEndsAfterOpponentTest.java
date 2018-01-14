package de.kintel.ki.ruleset.rules;

import de.kintel.ki.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class RuleMoveEndsAfterOpponentTest {

    @Autowired
    RuleMoveEndsAfterOpponent ruleMoveEndsAfterOpponent;

    @Test
    public void moveEndsAfterOpponent() {

        //       0   1   2
        //  0  | s |   |   |
        //  1  |   | w |   |
        //  2  |   |   |   |

        Field[][] fields = {
                {new Field(false), new Field(false), new Field(false)},
                {new Field(false), new Field(false), new Field(false)},
                {new Field(false), new Field(false), new Field(false)},
        };

        final Coordinate2D coordinateFrom = new Coordinate2D(0, 0);
        final Coordinate2D coordinateTarget = new Coordinate2D(2, 2);

        final Board board = new Board(fields.length, fields[0].length, fields);

        board.getField(coordinateFrom).addStein(new Piece(Player.SCHWARZ));
        fields[1][1].addStein(new Piece(Player.WEISS));

        final Move move = new UMLMove(board, coordinateFrom, coordinateTarget, Player.SCHWARZ);

        final boolean isValidMoveActual = ruleMoveEndsAfterOpponent.isValidMove(move);

        assertThat(isValidMoveActual, is(true));
    }

    @Test
    public void moveWithFieldAfterOpponent() {

        //       0   1   2   3
        //  0  | s |   |   |   |
        //  1  |   | w |   |   |
        //  2  |   |   |   |   |
        //  3  |   |   |   |   |

        Field[][] fields = {
                {new Field(false), new Field(false), new Field(false), new Field(false)},
                {new Field(false), new Field(false), new Field(false), new Field(false)},
                {new Field(false), new Field(false), new Field(false), new Field(false)},
                {new Field(false), new Field(false), new Field(false), new Field(false)},
        };

        final Coordinate2D coordinateFrom = new Coordinate2D(0, 0);
        final Coordinate2D coordinateTarget = new Coordinate2D(3, 3);

        final Board board = new Board(fields.length, fields[0].length, fields);

        board.getField(coordinateFrom).addStein(new Piece(Player.SCHWARZ));
        fields[1][1].addStein(new Piece(Player.WEISS));

        final Move move = new UMLMove(board, coordinateFrom, coordinateTarget, Player.SCHWARZ);

        final boolean isValidMoveActual = ruleMoveEndsAfterOpponent.isValidMove(move);

        assertThat(isValidMoveActual, is(false));
    }

    @Test
    public void ruleDoesntApplyForMoveWithoutOpponent() {

        //       0   1   2   3
        //  0  | s |   |   |   |
        //  1  |   |   |   |   |
        //  2  |   |   |   |   |
        //  3  |   |   |   |   |

        Field[][] fields = {
                {new Field(false), new Field(false), new Field(false), new Field(false)},
                {new Field(false), new Field(false), new Field(false), new Field(false)},
                {new Field(false), new Field(false), new Field(false), new Field(false)},
                {new Field(false), new Field(false), new Field(false), new Field(false)},
        };

        final Coordinate2D coordinateFrom = new Coordinate2D(0, 0);
        final Coordinate2D coordinateTarget = new Coordinate2D(3, 3);

        final Board board = new Board(fields.length, fields[0].length, fields);

        board.getField(coordinateFrom).addStein(new Piece(Player.SCHWARZ));

        final Move move = new UMLMove(board, coordinateFrom, coordinateTarget, Player.SCHWARZ);

        final boolean isValidMoveActual = ruleMoveEndsAfterOpponent.isValidMove(move);

        assertThat(isValidMoveActual, is(true));
    }
}