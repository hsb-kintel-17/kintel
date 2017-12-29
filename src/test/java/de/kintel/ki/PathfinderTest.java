package de.kintel.ki;

import com.google.common.collect.Lists;
import de.kintel.ki.algorithm.Pathfinder;
import de.kintel.ki.model.*;
import org.junit.Test;

import java.util.Deque;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class PathfinderTest {

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

        final Field srcField = fields[0][0];
        final Field targetField = fields[2][2];

        final Board board = new Board(fields.length, fields[0].length, fields);

        srcField.addStein(new Stein(Player.SCHWARZ));

        final Move move = new Move(board, srcField, targetField, Player.SCHWARZ);

        final Deque<Field> actualPath = Pathfinder.find(move);
        final List<Field> expectedPath = Lists.newArrayList( srcField, fields[1][1], targetField);

        assertThat(actualPath, is(expectedPath));

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

        final Field srcField = fields[1][1];
        final Field targetField = fields[3][1];

        final Board board = new Board(fields.length, fields[0].length, fields);

        srcField.addStein(new Stein(Player.SCHWARZ));

        final Move move = new Move(board, srcField, targetField, Player.SCHWARZ);

        final Deque<Field> actualPath = Pathfinder.find(move);
        final List<Field> expectedPath = Lists.newArrayList( srcField, fields[2][2], targetField);

        assertThat(actualPath, is(expectedPath));
    }

    @Test
    public void findLaskaGrid() {

        final Field[][] fields = GridFactory.getLaskaInitGrid();

        final Field srcField = fields[4][2];
        final Field targetField = fields[3][3];

        final Board board = new Board(fields.length, fields[0].length, fields);

        assertThat( srcField.getOwner().get(), is(Player.WEISS));

        final Move move = new Move(board, srcField, targetField, Player.WEISS);

        final Deque<Field> actualPath = Pathfinder.find(move);
        final List<Field> expectedPath = Lists.newArrayList( srcField, targetField);

        assertThat("Expect a straight movement from src to dest because fields are direct neighbours", actualPath, is(expectedPath));
    }

    @Test
    public void regression_26to37() {
        //Move{S(Coordinate2D{x=2, y=6}) to (Coordinate2D{x=3, y=7}) for player SCHWARZ
        final Field[][] fields = GridFactory.getLaskaInitGrid();

        final Field srcField = fields[2][6];
        final Field targetField = fields[3][7];

        final Board board = new Board(fields.length, fields[0].length, fields);

        assertThat( srcField.getOwner().get(), is(Player.SCHWARZ));

        final Move move = new Move(board, srcField, targetField, Player.SCHWARZ);

        final Deque<Field> actualPath = Pathfinder.find(move);
        final List<Field> expectedPath = Lists.newArrayList( srcField, targetField);

        assertThat("Expect a straight movement from src to dest because fields are direct neighbours", actualPath, is(expectedPath));
    }
}