package de.kintel.ki;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class FieldTest {

    Field field;

    @Before
    public void setUp() {
        field = new Field(false);
    }

    @Test
    public void peekHead() {
        field.addStein( new Stein(Player.SCHWARZ));
        assertThat( field.peekHead().orElseThrow(IllegalStateException::new).getOwner(), is(Player.SCHWARZ));
    }

    @Test
    public void getOwner() {
        field.addStein( new Stein(Player.SCHWARZ));
        field.addStein( new Stein(Player.WEISS));
        assertThat( field.getOwner().orElseThrow(IllegalStateException::new), is(Player.WEISS));
    }
}