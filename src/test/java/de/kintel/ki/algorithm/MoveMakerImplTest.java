package de.kintel.ki.algorithm;

import de.kintel.ki.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.contains;
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

        final Field fieldFrom = fields[0][0];
        final Field fieldTo = fields[2][2];

        final Board board = new Board(fields.length, fields[0].length, fields);

        final Stein schwarzerStein = new Stein(Player.SCHWARZ);

        fieldFrom.addStein( schwarzerStein );

        final Move move = new UMLMove(board, fieldFrom, fieldTo, Player.SCHWARZ);

        assertThat( fieldFrom.getSteine(), contains(schwarzerStein) );
        assertThat( fieldTo.isEmpty(), is(true) );

        moveMaker.makeMove(move);

        assertThat( fieldFrom.isEmpty(), is(true) );
        assertThat( fieldTo.getSteine(), contains(schwarzerStein) );
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

        final Field fieldFrom = fields[0][0];
        final Field fieldTo = fields[2][2];

        final Board board = new Board(fields.length, fields[0].length, fields);

        final Stein schwarzerStein = new Stein(Player.SCHWARZ);

        fieldFrom.addStein( schwarzerStein );

        final Move move = new UMLMove(board, fieldFrom, fieldTo, Player.SCHWARZ);

        assertThat( fieldFrom.getSteine(), contains(schwarzerStein) );
        assertThat( fieldTo.isEmpty(), is(true) );

        moveMaker.makeMove(move);

        assertThat( fieldFrom.isEmpty(), is(true) );
        assertThat( fieldTo.getSteine(), contains(schwarzerStein) );

        moveMaker.undoMove(move);

        assertThat( fieldFrom.getSteine(), contains(schwarzerStein) );
        assertThat( fieldTo.isEmpty(), is(true) );
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

        final Field fieldFrom = fields[0][0];
        final Field fieldBetween = fields[1][1];
        final Field fieldTo = fields[2][2];

        final Board board = new Board(fields.length, fields[0].length, fields);

        final Stein schwarzerStein = new Stein(Player.SCHWARZ);
        final Stein weisserStein = new Stein(Player.WEISS);

        fieldFrom.addStein(schwarzerStein);
        fieldBetween.addStein(weisserStein);

        final Move move = new UMLMove(board, fieldFrom, fieldTo, Player.SCHWARZ);

        assertThat( fieldFrom.getSteine(), contains(schwarzerStein) );
        assertTrue( fieldTo.isEmpty() );

        moveMaker.makeMove(move);

        assertTrue( fieldFrom.isEmpty() );
        assertTrue( fieldBetween.isEmpty() );
        assertThat( fieldTo.getSteine().pollFirst(), is(schwarzerStein) );
        assertThat( fieldTo.getSteine().pollFirst(), is(weisserStein) );
    }

    @Test
    public void makeMove_capture_undo_simple() {

        //       0   1   2
        //  0  | o |   |   |
        //  1  |   | w |   |
        //  2  |   |   |   |

        Field[][] fields = {
                {new Field(false), new Field(false), new Field(false)},
                {new Field(false), new Field(false), new Field(false)},
                {new Field(false), new Field(false), new Field(false)},
        };

        final Field fieldFrom = fields[0][0];
        final Field fieldBetween = fields[1][1];
        final Field fieldTo = fields[2][2];

        final Board board = new Board(fields.length, fields[0].length, fields);

        final Stein schwarzerStein = new Stein(Player.SCHWARZ);
        final Stein weisserStein = new Stein(Player.WEISS);

        fieldFrom.addStein(schwarzerStein);
        fieldBetween.addStein(weisserStein);

        final Move move = new UMLMove(board, fieldFrom, fieldTo, Player.SCHWARZ);

        assertThat( fieldFrom.getSteine(), contains(schwarzerStein) );
        assertTrue( fieldTo.isEmpty() );

        moveMaker.makeMove(move);

        assertTrue( fieldFrom.isEmpty() );
        assertTrue( fieldBetween.isEmpty() );
        assertThat( fieldTo.getSteine().peekFirst(), is(schwarzerStein) );
        assertThat( fieldTo.getSteine(), contains(schwarzerStein, weisserStein) );

        moveMaker.undoMove(move);

        assertThat( fieldFrom.getSteine(), contains(schwarzerStein) );
        assertThat( fieldBetween.getSteine(), contains(weisserStein) );
        assertTrue( fieldTo.isEmpty() );
    }
}