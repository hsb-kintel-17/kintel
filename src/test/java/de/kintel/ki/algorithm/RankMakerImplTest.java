package de.kintel.ki.algorithm;

import de.kintel.ki.model.*;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class RankMakerImplTest {

    @Autowired
    MoveMaker moveMaker;

    @Test
    public void move() throws Exception {

        //       0   1   2
        //  0  | s |   |   |
        //  1  |   |   |   |
        //  2  |   |   |   |

        Field[][] fields = GridFactory.getRectGrid(3, 3);
        final Field fieldFrom = fields[0][0];
        final Field fieldTo = fields[2][2];

        final Board board = new Board(fields.length, fields[0].length, fields);

        final Piece schwarzerStein = new Piece(Player.SCHWARZ);

        fieldFrom.addStein( schwarzerStein );

        final Move move = new UMLMove(board, fieldFrom, fieldTo, Player.SCHWARZ);

        moveMaker.makeMove(move);

        assertThat( fieldFrom.isEmpty(), is(true) );
        assertThat( fieldTo.getSteine(), Matchers.contains(schwarzerStein) );
        assertThat( fieldTo.getSteine().peekFirst().getRank(), is(Rank.rot) );
    }


    @Test
    public void move_undo() throws Exception {

        //       0   1   2
        //  0  | s |   |   |
        //  1  |   |   |   |
        //  2  |   |   |   |

        Field[][] fields = GridFactory.getRectGrid(3, 3);
        final Field fieldFrom = fields[0][0];
        final Field fieldTo = fields[2][2];

        final Board board = new Board(fields.length, fields[0].length, fields);

        final Piece schwarzerStein = new Piece(Player.SCHWARZ);

        fieldFrom.addStein( schwarzerStein );

        final Move move = new UMLMove(board, fieldFrom, fieldTo, Player.SCHWARZ);

        assertThat( fieldFrom.isEmpty(), is(false) );
        assertThat( fieldFrom.getSteine(), Matchers.contains(schwarzerStein) );
        assertThat( fieldFrom.getSteine().peekFirst().getRank(), is(Rank.normal) );

        moveMaker.makeMove(move);

        assertThat( fieldFrom.isEmpty(), is(true) );
        assertThat( fieldTo.getSteine(), Matchers.contains(schwarzerStein) );
        assertThat( fieldTo.getSteine().peekFirst().getRank(), is(Rank.rot) );

        moveMaker.undoMove(move);

        assertThat( fieldFrom.isEmpty(), is(false) );
        assertThat( fieldFrom.getSteine(), Matchers.contains(schwarzerStein) );
        assertThat( fieldFrom.getSteine().peekFirst().getRank(), is(Rank.normal) );
    }

    @Test
    public void move2() throws Exception {

        //       0   1   2
        //  0  | sw |    |   |
        //  1  |    | ws |   |
        //  2  |    |    |   |

        Field[][] fields = GridFactory.getRectGrid(3, 3);
        final Field fieldFrom = fields[0][0];
        final Field fieldOpponent = fields[1][1];
        final Field fieldTo = fields[2][2];

        final Board board = new Board(fields.length, fields[0].length, fields);

        final Piece schwarzerStein = new Piece(Player.SCHWARZ);
        final Piece weisserStein = new Piece(Player.WEISS);

        final Piece schwarzerStein2 = new Piece(Player.SCHWARZ);
        final Piece weisserStein2 = new Piece(Player.WEISS);
        weisserStein2.setRank(Rank.gelb);

        fieldFrom.addStein( weisserStein );
        fieldFrom.addStein( schwarzerStein );

        fieldOpponent.addStein( schwarzerStein2 );
        fieldOpponent.addStein( weisserStein2 );

        final Move move = new UMLMove(board, fieldFrom, fieldTo, Player.SCHWARZ);

        moveMaker.makeMove(move);

        assertThat( fieldFrom.isEmpty(), is(true) );
        assertThat( fieldTo.getSteine(), Matchers.contains(schwarzerStein, weisserStein, weisserStein2) );
        // schwarzer Stein wurde befördert
        assertThat( fieldTo.getSteine().peekFirst().getRank(), is(Rank.magenta) );
        // weißer stein2 wurde degradiert
        assertThat( weisserStein2.getRank(), is(Rank.normal) );
    }

    @Test
    public void move2_undo() throws Exception {

        //       0   1   2
        //  0  | sw |    |   |
        //  1  |    | ws |   |
        //  2  |    |    |   |

        Field[][] fields = GridFactory.getRectGrid(3, 3);
        final Field fieldFrom = fields[0][0];
        final Field fieldOpponent = fields[1][1];
        final Field fieldTo = fields[2][2];

        final Board board = new Board(fields.length, fields[0].length, fields);

        final Piece schwarzerStein = new Piece(Player.SCHWARZ);
        final Piece weisserStein = new Piece(Player.WEISS);

        final Piece schwarzerStein2 = new Piece(Player.SCHWARZ);
        final Piece weisserStein2 = new Piece(Player.WEISS);
        weisserStein2.setRank(Rank.gelb);

        fieldFrom.addStein( weisserStein );
        fieldFrom.addStein( schwarzerStein );

        fieldOpponent.addStein( schwarzerStein2 );
        fieldOpponent.addStein( weisserStein2 );

        final Move move = new UMLMove(board, fieldFrom, fieldTo, Player.SCHWARZ);

        assertThat( fieldFrom.isEmpty(), is(false) );
        assertThat( fieldFrom.getSteine(), Matchers.contains( schwarzerStein, weisserStein) );
        assertThat( fieldFrom.getSteine().peekFirst().getRank(), is(Rank.normal) );
        assertThat( weisserStein2.getRank(), is(Rank.gelb) );

        moveMaker.makeMove(move);

        assertThat( fieldFrom.isEmpty(), is(true) );
        assertThat( fieldTo.getSteine(), Matchers.contains(schwarzerStein, weisserStein, weisserStein2) );
        // schwarzer Stein wurde befördert
        assertThat( fieldTo.getSteine().peekFirst().getRank(), is(Rank.magenta) );
        // weißer stein2 wurde degradiert
        assertThat( weisserStein2.getRank(), is(Rank.normal) );

        moveMaker.undoMove(move);

        assertThat( fieldFrom.isEmpty(), is(false) );
        assertThat( fieldFrom.getSteine(), Matchers.contains( schwarzerStein, weisserStein) );
        assertThat( fieldFrom.getSteine().peekFirst().getRank(), is(Rank.normal) );
        assertThat( fieldOpponent.getSteine(), Matchers.contains( weisserStein2, schwarzerStein2) );
        assertThat( fieldTo.isEmpty(), is(true) );
    }

    @Test
    public void move2_undo2() throws Exception {

        //       0   1   2
        //  0  | sw |    |   |
        //  1  |    | w  |   |
        //  2  |    |    |   |

        Field[][] fields = GridFactory.getRectGrid(3, 3);
        final Field fieldFrom = fields[0][0];
        final Field fieldOpponent = fields[1][1];
        final Field fieldTo = fields[2][2];

        final Board board = new Board(fields.length, fields[0].length, fields);

        final Piece schwarzerStein = new Piece(Player.SCHWARZ);
        final Piece weisserStein = new Piece(Player.WEISS);

        final Piece weisserStein2 = new Piece(Player.WEISS);
        weisserStein2.setRank(Rank.gelb);

        fieldFrom.addStein( weisserStein );
        fieldFrom.addStein( schwarzerStein );

        fieldOpponent.addStein( weisserStein2 );

        final Move move = new UMLMove(board, fieldFrom, fieldTo, Player.SCHWARZ);

        assertThat( fieldFrom.isEmpty(), is(false) );
        assertThat( fieldFrom.getSteine(), Matchers.contains( schwarzerStein, weisserStein) );
        assertThat( fieldFrom.getSteine().peekFirst().getRank(), is(Rank.normal) );
        assertThat( weisserStein2.getRank(), is(Rank.gelb) );

        moveMaker.makeMove(move);

        assertThat( fieldFrom.isEmpty(), is(true) );
        assertThat( fieldTo.getSteine(), Matchers.contains(schwarzerStein, weisserStein, weisserStein2) );
        // schwarzer Stein wurde befördert
        assertThat( fieldTo.getSteine().peekFirst().getRank(), is(Rank.magenta) );
        // weißer stein2 wurde degradiert
        assertThat( weisserStein2.getRank(), is(Rank.normal) );

        moveMaker.undoMove(move);

        assertThat( fieldFrom.isEmpty(), is(false) );
        assertThat( fieldFrom.getSteine(), Matchers.contains( schwarzerStein, weisserStein) );
        assertThat( fieldFrom.getSteine().peekFirst().getRank(), is(Rank.normal) );
        assertThat( fieldOpponent.getSteine(), Matchers.contains( weisserStein2 ) );
        assertThat( fieldTo.isEmpty(), is(true) );
    }

}