/*
 * hsb-kintel-17
 * Copyright (C) 2018 hsb-kintel-17
 * This file is covered by the LICENSE file in the root of this project.
 */

package de.kintel.ki.model;

import de.kintel.ki.util.BoardUtils;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public class BoardUtilsTest {

    @Test
    public void getDiagonalSurroundings() {
        final Field[][] fields = GridFactory.getRectGrid(5, 5);
        final Board board = new Board(fields.length, fields[0].length, fields);

        final List<Coordinate2D> diagonalSurroundings = BoardUtils.getDiagonalSurroundings(board, new Coordinate2D(2, 2), 2);

        assertThat(diagonalSurroundings, containsInAnyOrder(
                new Coordinate2D(0, 0),
                new Coordinate2D(0, 4),
                new Coordinate2D(1, 1),
                new Coordinate2D(1, 3),
                new Coordinate2D(3, 1),
                new Coordinate2D(3, 3),
                new Coordinate2D(4, 0),
                new Coordinate2D(4, 4)
        ));

    }

    @Test
    public void getDiagonalSurroundingsBorder() {
        final Field[][] fields = GridFactory.getRectGrid(5, 5);
        final Board board = new Board(fields.length, fields[0].length, fields);

        final List<Coordinate2D> diagonalSurroundings = BoardUtils.getDiagonalSurroundings(board, new Coordinate2D(0, 0), 2);

        assertThat(diagonalSurroundings, containsInAnyOrder(
                new Coordinate2D(1, 1),
                new Coordinate2D(2, 2)
        ));

    }
}