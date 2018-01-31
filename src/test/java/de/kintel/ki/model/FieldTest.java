/*
 * hsb-kintel-17
 * Copyright (C) 2018 hsb-kintel-17
 * This file is covered by the LICENSE file in the root of this project.
 */

package de.kintel.ki.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class FieldTest {

    private Field field;

    @Before
    public void setUp() {
        field = new Field(false);
    }

    @Test
    public void peekHead() {
        assertThat( field.peekHead(), is(Optional.empty()));
        field.addStein( new Piece(Player.SCHWARZ));
        assertThat( field.getPieces().size(), is(1));
        assertThat( field.peekHead().orElseThrow(IllegalStateException::new).getOwner(), is(Player.SCHWARZ));
        assertThat( field.getPieces().size(), is(1));
    }

    @Test
    public void getOwner() {
        assertThat( field.getOwner(), is(Optional.empty()));
        field.addStein( new Piece(Player.SCHWARZ));
        field.addStein( new Piece(Player.WEISS));
        assertThat( field.getOwner().orElseThrow(IllegalStateException::new), is(Player.WEISS));
    }

    @Test
    public void isEmpty() {
        assertThat( field.isEmpty(), is(true));
        field.addStein( new Piece(Player.WEISS));
        assertThat( field.isEmpty(), is(false));
    }

    @Test
    public void addStein() {
        final Piece piece = new Piece(Player.SCHWARZ);
        assertThat( field.isEmpty(), is(true));
        field.addStein( piece );
        assertThat( field.getPieces(), hasItem(piece));
    }

    @Test
    public void pollHead() {
        assertThat( field.pollHead(), is(Optional.empty()));
        final Piece piece1 = new Piece(Player.SCHWARZ);
        final Piece piece2 = new Piece(Player.WEISS);
        field.addStein( piece1 );
        field.addStein( piece2 );
        assertThat( field.getPieces().size(), is(2));
        assertThat( field.pollHead().orElseThrow(IllegalStateException::new), is(piece2));
        assertThat( field.getPieces().size(), is(1));
    }

    @Test
    public void isForbidden() {
        Field f = new Field(true);
        assertThat(f.isForbidden(), is(true));
        Field f2 = new Field(false);
        assertThat(f2.isForbidden(), is(false));
    }
}