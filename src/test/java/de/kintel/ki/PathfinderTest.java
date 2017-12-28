package de.kintel.ki;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class PathfinderTest {

    @Test
    public void find() {

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

        final List<Field> actualPath = Pathfinder.find(move);
        final List<Field> expectedPath = Lists.newArrayList( srcField, fields[1][1], targetField);

        assertThat(actualPath, is(expectedPath));

    }
}