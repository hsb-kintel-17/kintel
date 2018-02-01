/*
 * hsb-kintel-17
 * Copyright (C) 2018 hsb-kintel-17
 * This file is covered by the LICENSE file in the root of this project.
 */

package de.kintel.ki.algorithm;

import de.kintel.ki.model.*;
import org.junit.Test;

import java.util.Deque;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class PathFinderTest {

    @Test
    public void findSimple() {

        //       0   1   2
        //  0  | o |   |   |
        //  1  |   |   |   |
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

        final Move move = new UMLMove(coordinateFrom, coordinateTarget, Player.SCHWARZ);

        final Deque<Coordinate2D> actualPath = PathFinder.find(move,board);

        assertThat(actualPath, hasItems(coordinateFrom, new Coordinate2D(1, 1), coordinateTarget));

    }

    @Test
    public void findBlocked() {

        //       0   1   2
        //  0  |   |   |   |
        //  1  |   | o |   |
        //  2  | x |   |   |
        //  3  |   |   |   |

        Field[][] fields = {
            {new Field(false), new Field(false), new Field(false)},
            {new Field(false), new Field(false), new Field(false)},
            {new Field(true), new Field(false), new Field(false)},
            {new Field(false), new Field(false), new Field(false)},
        };

        final Coordinate2D coordinateFrom = new Coordinate2D(1, 1);
        final Coordinate2D coordinateTarget = new Coordinate2D(3, 1);

        final Board board = new Board(fields.length, fields[0].length, fields);

        board.getField(coordinateFrom).addStein(new Piece(Player.SCHWARZ));

        final Move move = new UMLMove( coordinateFrom, coordinateTarget, Player.SCHWARZ);

        final Deque<Coordinate2D> actualPath = PathFinder.find(move,board);

        assertThat(actualPath, hasItems(coordinateFrom, new Coordinate2D(2, 2), coordinateTarget));
    }

    @Test
    public void findLaskaGrid() {

        final Field[][] fields = GridFactory.getLaskaInitGrid();

        final Coordinate2D coordinateFrom = new Coordinate2D(4, 2);
        final Coordinate2D coordinateTarget = new Coordinate2D(3, 3);

        final Board board = new Board(fields.length, fields[0].length, fields);

        assertTrue( board.getField(coordinateFrom).getOwner().isPresent() );
        assertThat( board.getField(coordinateFrom).getOwner().get(), is(Player.WEISS));

        final Move move = new UMLMove( coordinateFrom, coordinateTarget, Player.WEISS);

        final Deque<Coordinate2D> actualPath = PathFinder.find(move,board);

        assertThat("Expect a straight movement from src to dest because fields are direct neighbours", actualPath, hasItems(coordinateFrom, coordinateTarget));
    }

    @Test
    public void regression_26to37() {
        //Move{S(Coordinate2D{x=2, y=6}) to (Coordinate2D{x=3, y=7}) for player SCHWARZ
        final Field[][] fields = GridFactory.getLaskaInitGrid();

        final Coordinate2D coordinateFrom = new Coordinate2D(2, 6);
        final Coordinate2D coordinateTarget = new Coordinate2D(3, 7);

        final Board board = new Board(fields.length, fields[0].length, fields);

        assertTrue( board.getField(coordinateFrom).getOwner().isPresent() );
        assertThat( board.getField(coordinateFrom).getOwner().get(), is(Player.SCHWARZ));

        final Move move = new UMLMove( coordinateFrom, coordinateTarget, Player.SCHWARZ);

        final Deque<Coordinate2D> actualPath = PathFinder.find(move,board);

        assertThat("Expect a straight movement from src to dest because fields are direct neighbours", actualPath, hasItems(coordinateFrom, coordinateTarget));
    }
}