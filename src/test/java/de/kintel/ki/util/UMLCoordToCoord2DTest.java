/*
 * hsb-kintel-17
 * Copyright (C) 2018 hsb-kintel-17
 * This file is covered by the LICENSE file in the root of this project.
 */

package de.kintel.ki.util;

import de.kintel.ki.model.Coordinate2D;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;


/**
 * Created by kintel on 17.01.2018.
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class UMLCoordToCoord2DTest {

    @Autowired
    private UMLCoordToCoord2D converter;

    @Test
    public void convertUMLCoord() {
        final Coordinate2D b2 = converter.convertUMLCoord("b2");
        final Coordinate2D upperBound = converter.convertUMLCoord( "g9" );
        final Coordinate2D lowerBound = converter.convertUMLCoord( "a1" );
        final Coordinate2D invalid = converter.convertUMLCoord("fg");
        final Coordinate2D outOfRange = converter.convertUMLCoord("i9");
        final Coordinate2D tooLong = converter.convertUMLCoord("i999");

        assertThat( b2, is( new Coordinate2D(5, 1) ) );
        assertThat( upperBound, is( new Coordinate2D(0, 8) ) );
        assertThat( lowerBound, is( new Coordinate2D(6, 0) ) );
        assertThat( invalid, nullValue());
        assertThat( outOfRange, nullValue() );
        assertThat( tooLong, nullValue() );
    }

    @Test
    public void convertCoordToUML() {
        final String b2 = converter.convertCoordToUML( new Coordinate2D(5,1) );
        final String upperBound = converter.convertCoordToUML( new Coordinate2D(6,8) );
        final String lowerBound = converter.convertCoordToUML( new Coordinate2D(0,0) );

        assertThat( b2, is( "b2") );
        assertThat( upperBound, is( "a9") );
        assertThat( lowerBound, is( "g1") );
    }
}