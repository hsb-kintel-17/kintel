package de.kintel.ki.model;

import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public class BoardUtilsTest {

    @Test
    public void getDiagonalSurroundings() {
        final Field[][] fields = GridFactory.getRectGrid(5, 5);
        final Board board = new Board(fields.length, fields[0].length, fields);

        final List<Field> diagonalSurroundings = BoardUtils.getDiagonalSurroundings(board, new Coordinate2D(2, 2), 2);
        final List<Coordinate2D> surroundingsCoords = diagonalSurroundings.stream()
                                                               .map(board::getCoordinate)
                                                               .collect(Collectors.toList());

        assertThat(surroundingsCoords, containsInAnyOrder(
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

        final List<Field> diagonalSurroundings = BoardUtils.getDiagonalSurroundings(board, new Coordinate2D(0, 0), 2);
        final List<Coordinate2D> surroundingsCoords = diagonalSurroundings.stream()
                                                                          .map(board::getCoordinate)
                                                                          .collect(Collectors.toList());

        assertThat(surroundingsCoords, containsInAnyOrder(
                new Coordinate2D(1, 1),
                new Coordinate2D(2, 2)
        ));

    }
}