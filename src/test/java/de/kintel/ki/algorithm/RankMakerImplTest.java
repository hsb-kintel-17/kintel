/*
 * hsb-kintel-17
 * Copyright (C) 2018 hsb-kintel-17
 * This file is covered by the LICENSE file in the root of this project.
 */

package de.kintel.ki.algorithm;

import de.kintel.ki.model.*;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class RankMakerImplTest {

    @Autowired
    private MoveMaker moveMaker;

    @Test
    public void move() throws Exception {

        //       0   1   2
        //  0  | s |   |   |
        //  1  |   |   |   |
        //  2  |   |   |   |

        Field[][] fields = GridFactory.getRectGrid(3, 3);
        final Coordinate2D coordinateFrom = new Coordinate2D(0, 0);
        final Coordinate2D coordinateTo = new Coordinate2D(2, 2);

        final Board board = new Board(fields.length, fields[0].length, fields);

        final Piece schwarzerStein = new Piece(Player.SCHWARZ);

        board.getField(coordinateFrom).addStein( schwarzerStein );

        final Move move = new UMLMove( coordinateFrom, coordinateTo, Player.SCHWARZ);

        move.setForwardClassification(MoveClassifier.MoveType.MOVE);
        moveMaker.makeMove(move,board);

        assertThat( board.getField(coordinateFrom).isEmpty(), is(true) );
        assertThat( board.getField(coordinateTo).getPieces(), Matchers.contains(schwarzerStein) );
        assertThat( board.getField(coordinateTo).getPieces().peekFirst().getRank(), is(Rank.rot) );
    }


    @Test
    public void move_undo() throws Exception {

        //       0   1   2
        //  0  | s |   |   |
        //  1  |   |   |   |
        //  2  |   |   |   |

        Field[][] fields = GridFactory.getRectGrid(3, 3);
        final Coordinate2D coordinateFrom = new Coordinate2D(0, 0);
        final Coordinate2D coordinateTo = new Coordinate2D(2, 2);

        final Board board = new Board(fields.length, fields[0].length, fields);

        final Piece schwarzerStein = new Piece(Player.SCHWARZ);

        board.getField(coordinateFrom).addStein( schwarzerStein );

        final Move move = new UMLMove( coordinateFrom, coordinateTo, Player.SCHWARZ);
        move.setForwardClassification(MoveClassifier.MoveType.MOVE);
        move.setForwardSourceRank(schwarzerStein.getRank());

        assertThat( board.getField(coordinateFrom).isEmpty(), is(false) );
        assertThat( board.getField(coordinateFrom).getPieces(), Matchers.contains(schwarzerStein) );
        assertThat( board.getField(coordinateFrom).getPieces().peekFirst().getRank(), is(Rank.normal) );

        moveMaker.makeMove(move,board);

        assertThat( board.getField(coordinateFrom).isEmpty(), is(true) );
        assertThat( board.getField(coordinateTo).getPieces(), Matchers.contains(schwarzerStein) );
        assertThat( board.getField(coordinateTo).getPieces().peekFirst().getRank(), is(Rank.rot) );

        moveMaker.undoMove(move,board);

        assertThat( board.getField(coordinateFrom).isEmpty(), is(false) );
        assertThat( board.getField(coordinateFrom).getPieces(), Matchers.contains(schwarzerStein) );
        assertThat( board.getField(coordinateFrom).getPieces().peekFirst().getRank(), is(Rank.normal) );
    }

    @Test
    public void move2() throws Exception {

        //       0   1   2
        //  0  | sw |    |   |
        //  1  |    | ws |   |
        //  2  |    |    |   |

        Field[][] fields = GridFactory.getRectGrid(3, 3);
        final Coordinate2D coordinateFrom = new Coordinate2D(0, 0);
        final Coordinate2D coordinateOpponent = new Coordinate2D(1, 1);
        final Coordinate2D coordinateTo = new Coordinate2D(2, 2);

        final Board board = new Board(fields.length, fields[0].length, fields);

        final Piece schwarzerStein = new Piece(Player.SCHWARZ);
        final Piece weisserStein = new Piece(Player.WEISS);

        final Piece schwarzerStein2 = new Piece(Player.SCHWARZ);
        final Piece weisserStein2 = new Piece(Player.WEISS);
        weisserStein2.setRank(Rank.gelb);

        board.getField(coordinateFrom).addStein( weisserStein );
        board.getField(coordinateFrom).addStein( schwarzerStein );

        board.getField(coordinateOpponent).addStein( schwarzerStein2 );
        board.getField(coordinateOpponent).addStein( weisserStein2 );

        final Move move = new UMLMove( coordinateFrom, coordinateTo, Player.SCHWARZ);
        move.setForwardClassification(MoveClassifier.MoveType.CAPTURE);

        moveMaker.makeMove(move,board);

        assertThat( board.getField(coordinateFrom).isEmpty(), is(true) );
        assertThat( board.getField(coordinateTo).getPieces(), Matchers.contains(schwarzerStein, weisserStein, weisserStein2) );
        // schwarzer Stein wurde befördert
        assertThat( board.getField(coordinateTo).getPieces().peekFirst().getRank(), is(Rank.magenta) );
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
        final Coordinate2D coordinateFrom = new Coordinate2D(0, 0);
        final Coordinate2D coordinateOpponent = new Coordinate2D(1, 1);
        final Coordinate2D coordinateTo = new Coordinate2D(2, 2);

        final Board board = new Board(fields.length, fields[0].length, fields);

        final Piece schwarzerStein = new Piece(Player.SCHWARZ);
        final Piece weisserStein = new Piece(Player.WEISS);

        final Piece schwarzerStein2 = new Piece(Player.SCHWARZ);
        final Piece weisserStein2 = new Piece(Player.WEISS);
        weisserStein2.setRank(Rank.gelb);

        board.getField(coordinateFrom).addStein( weisserStein );
        board.getField(coordinateFrom).addStein( schwarzerStein );

        board.getField(coordinateOpponent).addStein( schwarzerStein2 );
        board.getField(coordinateOpponent).addStein( weisserStein2 );

        final Move move = new UMLMove( coordinateFrom, coordinateTo, Player.SCHWARZ);
        move.setForwardClassification(MoveClassifier.MoveType.CAPTURE);
        move.setForwardOpponentRank(Optional.of(weisserStein2.getRank()));
        move.setForwardSourceRank(Rank.normal);

        assertThat( board.getField(coordinateFrom).isEmpty(), is(false) );
        assertThat( board.getField(coordinateFrom).getPieces(), Matchers.contains( schwarzerStein, weisserStein) );
        assertThat( board.getField(coordinateFrom).getPieces().peekFirst().getRank(), is(Rank.normal) );
        assertThat( weisserStein2.getRank(), is(Rank.gelb) );

        moveMaker.makeMove(move,board);

        assertThat( board.getField(coordinateFrom).isEmpty(), is(true) );
        assertThat( board.getField(coordinateTo).getPieces(), Matchers.contains(schwarzerStein, weisserStein, weisserStein2) );
        // schwarzer Stein wurde befördert
        assertThat( board.getField(coordinateTo).getPieces().peekFirst().getRank(), is(Rank.magenta) );
        // weißer stein2 wurde degradiert
        assertThat( weisserStein2.getRank(), is(Rank.normal) );

        moveMaker.undoMove(move,board);

        assertThat( board.getField(coordinateFrom).isEmpty(), is(false) );
        assertThat( board.getField(coordinateFrom).getPieces(), Matchers.contains( schwarzerStein, weisserStein) );
        assertThat( board.getField(coordinateFrom).getPieces().peekFirst().getRank(), is(Rank.normal) );
        assertThat( board.getField(coordinateOpponent).getPieces(), Matchers.contains( weisserStein2, schwarzerStein2) );
        assertThat( board.getField(coordinateTo).isEmpty(), is(true) );
    }

    @Test
    public void move3_undo() throws Exception {

        //       0   1   2
        //  0  | sw |    |   |
        //  1  |    | w  |   |
        //  2  |    |    |   |

        Field[][] fields = GridFactory.getRectGrid(3, 3);
        final Coordinate2D coordinateFrom = new Coordinate2D(0, 0);
        final Coordinate2D coordinateOpponent = new Coordinate2D(1, 1);
        final Coordinate2D coordinateTo = new Coordinate2D(2, 2);

        final Board board = new Board(fields.length, fields[0].length, fields);

        final Piece schwarzerStein = new Piece(Player.SCHWARZ);
        final Piece weisserStein = new Piece(Player.WEISS);

        final Piece weisserStein2 = new Piece(Player.WEISS);
        weisserStein2.setRank(Rank.gelb);

        board.getField(coordinateFrom).addStein( weisserStein );
        board.getField(coordinateFrom).addStein( schwarzerStein );

        board.getField(coordinateOpponent).addStein( weisserStein2 );

        final Move move = new UMLMove( coordinateFrom, coordinateTo, Player.SCHWARZ);
        move.setForwardClassification(MoveClassifier.MoveType.CAPTURE);
        move.setForwardSourceRank(schwarzerStein.getRank());
        move.setForwardOpponentRank(Optional.of(weisserStein2.getRank()));

        assertThat( board.getField(coordinateFrom).isEmpty(), is(false) );
        assertThat( board.getField(coordinateFrom).getPieces(), Matchers.contains( schwarzerStein, weisserStein) );
        assertThat( board.getField(coordinateFrom).getPieces().peekFirst().getRank(), is(Rank.normal) );
        assertThat( weisserStein2.getRank(), is(Rank.gelb) );

        moveMaker.makeMove(move,board);

        assertThat( board.getField(coordinateFrom).isEmpty(), is(true) );
        assertThat( board.getField(coordinateTo).getPieces(), Matchers.contains(schwarzerStein, weisserStein, weisserStein2) );
        // schwarzer Stein wurde befördert
        assertThat( board.getField(coordinateTo).getPieces().peekFirst().getRank(), is(Rank.magenta) );
        // weißer stein2 wurde degradiert
        assertThat( weisserStein2.getRank(), is(Rank.normal) );

        moveMaker.undoMove(move,board);

        assertThat( board.getField(coordinateFrom).isEmpty(), is(false) );
        assertThat( board.getField(coordinateFrom).getPieces(), Matchers.contains( schwarzerStein, weisserStein) );
        assertThat( board.getField(coordinateFrom).getPieces().peekFirst().getRank(), is(Rank.normal) );
        assertThat( board.getField(coordinateOpponent).getPieces(), Matchers.contains( weisserStein2 ) );
        assertThat( board.getField(coordinateTo).isEmpty(), is(true) );
    }

}