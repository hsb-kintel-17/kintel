package de.kintel.ki.util;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import de.kintel.ki.model.Coordinate2D;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by kintel on 17.01.2018.
 */
@Component
@Scope("singleton")
/**
 * Converts between UML coordinate like b2 and internal coordinates like 51 and vice versa.
 */
public class UMLCoordToCoord2D {

    private BiMap<Character, Integer> mapBoardLetters = HashBiMap.create();
    // static initializer
    {
        this.mapBoardLetters.put('g', 1);
        this.mapBoardLetters.put('f', 2);
        this.mapBoardLetters.put('e', 3);
        this.mapBoardLetters.put('d', 4);
        this.mapBoardLetters.put('c', 5);
        this.mapBoardLetters.put('b', 6);
        this.mapBoardLetters.put('a', 7);
    }

    /**
     * Convert UML coordinate to internal coordinate.
     * @param umlCoord uml coordinate like b2
     * @return internal coordinate like 51
     */
    public Coordinate2D convertUMLCoord(String umlCoord) {
        umlCoord = umlCoord.toLowerCase();

        // check length of input must be 2
        if (umlCoord.length() != 2) {
            return null;
        }

        // checks if letter is in mapping
        if (!this.mapBoardLetters.containsKey(umlCoord.charAt(0))) {
            return null;
        } else {
            // check coordinate is between [1...9]
            if (!(1 <= Character.getNumericValue(umlCoord.charAt(1)) && Character.getNumericValue(umlCoord.charAt(1)) <= 9)) {
                return null;
            }
        }
        // calculate offset, because internal coordinates start at 0,0
        int[] coordinates = { mapBoardLetters.get(umlCoord.charAt(0)) - 1, Character.getNumericValue(umlCoord.charAt(1)) - 1};

        return new Coordinate2D(coordinates[0], coordinates[1]);
    }

    /**
     * Convert internal coordinate to UML coordinate
     * @param coord internal coordinate like 51
     * @return uml coordinate like b2
     */
    public String convertCoordToUML(Coordinate2D coord) {
        return "" + mapBoardLetters.inverse().get(coord.getX() + 1) + (coord.getY() + 1);
    }
}
