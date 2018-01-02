package de.kintel.ki.algorithm;

import de.kintel.ki.model.Field;

import javax.annotation.Nonnull;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Classify path into types like simple move or capture.
 */
public class PathClassifier {

    /**
     * Available move types
     */
    public enum MoveType {
        /**
         * Simple move
         */
        MOVE,
        /**
         * Capture
         */
        CAPTURE
    }

    /**
     * Classify the path.
     * @param pathOriginal The path
     * @return The move type
     */
    public static MoveType classify(@Nonnull final Deque<Field> pathOriginal) {
        Deque<Field> path = new ArrayDeque<>(pathOriginal);
        if( path.size() >= 2 ) {
            // Remove src and target field because they are irrelevant
            path.removeFirst();
            path.removeLast();
        }

        // If all fields are empty on the path then it must be a simple move
        final boolean isSimpleMove = path.stream().allMatch(Field::isEmpty);

        return isSimpleMove ? MoveType.MOVE : MoveType.CAPTURE;
    }

}
