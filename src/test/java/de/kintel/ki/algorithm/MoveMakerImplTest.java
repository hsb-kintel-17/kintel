package de.kintel.ki.algorithm;

import de.kintel.ki.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class MoveMakerImplTest {

    @Autowired
    MoveMaker moveMaker;

    @Test
    public void makeMove_simple() {

        //       0   1   2
        //  0  | o |   |   |
        //  1  |   |  |   |
        //  2  |   |   |   |

        Field[][] fields = {
                {new Field(false), new Field(false), new Field(false)},
                {new Field(false), new Field(false), new Field(false)},
                {new Field(false), new Field(false), new Field(false)},
        };

        final Coordinate2D coordinateFrom = new Coordinate2D(0,0);
        final Coordinate2D coordinateTo = new Coordinate2D(2,2);

        final Board board = new Board(fields.length, fields[0].length, fields);

        final Piece schwarzerStein = new Piece(Player.SCHWARZ);

        board.getField(coordinateFrom).addStein( schwarzerStein );

        final Move move = new UMLMove(board, coordinateFrom, coordinateTo, Player.SCHWARZ);

        assertThat( board.getField(coordinateFrom).getSteine(), contains(schwarzerStein) );
        assertThat( board.getField(coordinateTo).isEmpty(), is(true) );

        moveMaker.makeMove(move);

        assertThat( board.getField(coordinateFrom).isEmpty(), is(true) );
        assertThat( board.getField(coordinateTo).getSteine(), contains(schwarzerStein) );
    }

    @Test
    public void makeMove_undoMove_simple() {

        //       0   1   2
        //  0  | o |   |   |
        //  1  |   |  |   |
        //  2  |   |   |   |

        Field[][] fields = {
                {new Field(false), new Field(false), new Field(false)},
                {new Field(false), new Field(false), new Field(false)},
                {new Field(false), new Field(false), new Field(false)},
        };

        final Coordinate2D coordinateFrom = new Coordinate2D(0,0);
        final Coordinate2D coordinateTo = new Coordinate2D(2,2);

        Board board = new Board(fields.length, fields[0].length, fields);

        Piece schwarzerStein = new Piece(Player.SCHWARZ);

        board.getField(coordinateFrom).addStein( schwarzerStein );

        final Move move = new UMLMove(board, coordinateFrom, coordinateTo, Player.SCHWARZ);

        assertThat( board.getField(coordinateFrom).getSteine(), contains(schwarzerStein) );
        assertThat( board.getField(coordinateTo).isEmpty(), is(true) );

        moveMaker.makeMove(move);
        board = move.getBoard();
        schwarzerStein = board.getField(coordinateTo).peekHead().get();

        assertThat( board.getField(coordinateFrom).isEmpty(), is(true) );
        assertThat( board.getField(coordinateTo).getSteine(), contains(schwarzerStein) );

        moveMaker.undoMove(move);
        board = move.getBoard();
        schwarzerStein = board.getField(coordinateFrom).peekHead().get();

        assertTrue( board.getField(coordinateFrom).getSteine().contains(schwarzerStein) );
        assertThat( board.getField(coordinateTo).isEmpty(), is(true) );
    }

    @Test
    public void makeMove_capture_simple() {

        //       0   1   2
        //  0  | o |   |   |
        //  1  |   | w |   |
        //  2  |   |   |   |

        Field[][] fields = {
                {new Field(false), new Field(false), new Field(false)},
                {new Field(false), new Field(false), new Field(false)},
                {new Field(false), new Field(false), new Field(false)},
        };

        final Coordinate2D coordinateFrom = new Coordinate2D(0,0);
        final Coordinate2D coordinateBetween = new Coordinate2D(1,1);
        final Coordinate2D coordinateTo = new Coordinate2D(2,2);

        final Board board = new Board(fields.length, fields[0].length, fields);

        final Piece schwarzerStein = new Piece(Player.SCHWARZ);
        final Piece weisserStein = new Piece(Player.WEISS);

        board.getField(coordinateFrom).addStein(schwarzerStein);
        board.getField(coordinateBetween).addStein(weisserStein);

        final Move move = new UMLMove(board, coordinateFrom, coordinateTo, Player.SCHWARZ);

        assertThat(  board.getField(coordinateFrom).getSteine(), contains(schwarzerStein) );
        assertTrue(  board.getField(coordinateTo).isEmpty() );

        moveMaker.makeMove(move);

        assertTrue(  board.getField(coordinateFrom).isEmpty() );
        assertTrue(  board.getField(coordinateBetween).isEmpty() );
        assertThat(  board.getField(coordinateTo).getSteine().pollFirst(), is(schwarzerStein));
        assertThat(  board.getField(coordinateTo).getSteine().pollFirst(), is(weisserStein) );
    }

    @Test
    public void makeMove_capture_undo_simple() {

        //       0   1   2
        //  0  | s |   |   |
        //  1  |   | w |   |
        //  2  |   |   |   |

        Field[][] fields = {
                {new Field(false), new Field(false), new Field(false)},
                {new Field(false), new Field(false), new Field(false)},
                {new Field(false), new Field(false), new Field(false)},
        };

        final Coordinate2D coordinateFrom = new Coordinate2D(0,0);
        final Coordinate2D coordinateBetween = new Coordinate2D(1,1);
        final Coordinate2D coordinateTo = new Coordinate2D(2,2);

        final Board board = new Board(fields.length, fields[0].length, fields);

        final Piece schwarzerStein = new Piece(Player.SCHWARZ);
        final Piece weisserStein = new Piece(Player.WEISS);

        board.getField(coordinateFrom).addStein(schwarzerStein);
        board.getField(coordinateBetween).addStein(weisserStein);

        final Move move = new UMLMove(board, coordinateFrom, coordinateTo, Player.SCHWARZ);

        assertThat( board.getField(coordinateFrom).getSteine(), contains(schwarzerStein) );
        assertTrue( board.getField(coordinateTo).isEmpty() );

        moveMaker.makeMove(move);

        assertTrue( board.getField(coordinateFrom).isEmpty() );
        assertTrue( board.getField(coordinateBetween).isEmpty() );
        assertThat( board.getField(coordinateTo).getSteine().peekFirst(), is(schwarzerStein) );
        assertThat( board.getField(coordinateTo).getSteine(), contains(schwarzerStein, weisserStein) );

        moveMaker.undoMove(move);

        assertThat( board.getField(coordinateFrom).getSteine(), contains(schwarzerStein) );
        assertThat( board.getField(coordinateBetween).getSteine(), contains(weisserStein) );
        assertTrue( board.getField(coordinateTo).isEmpty() );
    }

    @Test
    public void regression_makeMove_capture_undo_saeule() {

        //       0   1   2
        //  0  | s |    |   |
        //  1  |   |  ws|   |
        //  2  |   |    |   |

        // S(Coordinate2D{x=1, y=3}) to (Coordinate2D{x=3, y=5}) for player SCHWARZ
        // Erwartete Stellung:
        // A 	S 	▨ 	S 	▨ 	S 	▨ 	S 	▨ 	S
        // B 	▨ 	S 	▨ 	S 	▨ 	S 	▨ 	S 	▨
        // C 	▨ 	▨ 	S 	▨ 	WS 	▨ 	S 	▨ 	▨
        // D 	▨ 	 	▨ 	 	▨ 	 	▨ 	 	▨
        // E 	▨ 	▨ 	 	▨ 	W 	▨ 	W 	▨ 	▨
        // F 	▨ 	W 	▨ 	W 	▨ 	W 	▨ 	W 	▨
        // G 	W 	▨ 	W 	▨ 	W 	▨ 	W 	▨ 	W
        // Aber Stellung war:
        // A 	S 	▨ 	S 	▨ 	S 	▨ 	S 	▨ 	S
        // B 	▨ 	S 	▨ 	 	▨ 	S 	▨ 	S 	▨
        // C 	▨ 	▨ 	S 	▨ 	S 	▨ 	S 	▨ 	▨
        // D 	▨ 	 	▨ 	 	▨ 	SW 	▨ 	 	▨
        // E 	▨ 	▨ 	 	▨ 	W 	▨ 	W 	▨ 	▨
        // F 	▨ 	W 	▨ 	W 	▨ 	W 	▨ 	W 	▨
        // G 	W 	▨ 	W 	▨ 	W 	▨ 	W 	▨ 	W

        Field[][] fields = {
                {new Field(false), new Field(false), new Field(false)},
                {new Field(false), new Field(false), new Field(false)},
                {new Field(false), new Field(false), new Field(false)},
        };

        final Coordinate2D coordinateFrom = new Coordinate2D(0, 0);
        final Coordinate2D coordinateBetween = new Coordinate2D(1, 1);
        final Coordinate2D coordinateTo = new Coordinate2D(2, 2);

        final Board board = new Board(fields.length, fields[0].length, fields);

        final Piece schwarzerStein = new Piece(Player.SCHWARZ);
        final Piece weisserStein = new Piece(Player.WEISS);
        final Piece schwarzerStein2 = new Piece(Player.SCHWARZ);

        board.getField(coordinateFrom).addStein(schwarzerStein);
        board.getField(coordinateBetween).addStein(schwarzerStein2);
        board.getField(coordinateBetween).addStein(weisserStein);

        final Move move = new UMLMove(board, coordinateFrom, coordinateTo, Player.SCHWARZ);

        assertThat( board.getField(coordinateFrom).getSteine(), contains(schwarzerStein) );
        assertThat( board.getField(coordinateBetween).getSteine().peekFirst(), is( weisserStein ) );
        assertThat( board.getField(coordinateBetween).getSteine(), contains( weisserStein, schwarzerStein2 ) );
        assertTrue( board.getField(coordinateTo).isEmpty() );

        moveMaker.makeMove(move);

        assertTrue( board.getField(coordinateFrom).isEmpty() );
        assertThat( board.getField(coordinateBetween).getSteine().peekFirst(), is( schwarzerStein2 ) );
        assertThat( board.getField(coordinateTo).getSteine().peekFirst(), is(schwarzerStein) );
        assertThat( board.getField(coordinateTo).getSteine(), contains(schwarzerStein, weisserStein) );

        moveMaker.undoMove(move);

        assertThat( board.getField(coordinateFrom).getSteine(), contains(schwarzerStein) );
        assertThat( board.getField(coordinateBetween).getSteine().peekFirst(), is( weisserStein ) );
        assertThat( board.getField(coordinateBetween).getSteine(), contains( weisserStein, schwarzerStein2 ) );
        assertTrue( board.getField(coordinateTo).isEmpty() );
    }
}